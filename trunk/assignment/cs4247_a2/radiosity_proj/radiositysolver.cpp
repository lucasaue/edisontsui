#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#include "common.h"
#include "vector3.h"
#include "quadmodel.h"


/////////////////////////////////////////////////////////////////////////////
// CONSTANTS THAT YOU CHANGE FOR DIFFERENT INPUT MODEL AND
// TO CONTROL HOW GOOD THE SOLUTION YOU WANT
/////////////////////////////////////////////////////////////////////////////

// Input model filename.
static const char inputModelFilename[] = "cornell_box.in";

// Output model filename. This model contains the radiosity solution.
static const char outputModelFilename[] = "cornell_box.out";	

// Threshold for subdiving the original quads to get shooter quads.
static const float maxShooterQuadEdgeLength = 70.0f;

// Threshold for subdiving the shooter quads to get gatherer quads.
static const float maxGathererQuadEdgeLength = 30.0f;	


/**********************************************************
 ********************** ADD HERE **************************
 * ADD SOME MORE CONSTANTS TO CONTROL WHEN
 * THE RADIOSITY ALGORITHM SHOULD TERMINATE.
 **********************************************************/

static const float hemicubeSize = 2;
static const float viewFrustumFar = 10000;

/////////////////////////////////////////////////////////////////////////////
// CONSTANTS
/////////////////////////////////////////////////////////////////////////////

// Window size.
static const int winWidthHeight = 800;     // Window width & height in pixels. Must be even number.

// Use white background, so that it will not conflict
// with the colors of the the gatherer quads.
static const float backgroundColor[4] = { 1.0f, 1.0f, 1.0f, 1.0f };

// An integer corresponding to the RGB color [255, 255, 255].
static const int backgroundColorInt = (255 * 256 + 255) * 256 + 255;


/////////////////////////////////////////////////////////////////////////////
// GLOBAL VARIABLES
/////////////////////////////////////////////////////////////////////////////

// The 3D model.
static QM_Model model;

// OpenGL display list.
static GLuint gathererQuadsDList = 0;

// Pre-computed delta form factors lookup tables.
static float *topDeltaFormFactors = NULL;
static float *sideDeltaFormFactors = NULL;



/////////////////////////////////////////////////////////////////////////////
// HELPER FUNCTIONS.
/////////////////////////////////////////////////////////////////////////////

static GLuint RGBToUnsignedInt( const GLubyte rgb[3] )
	// Convert RGB 8-bit triplets to an integer.
	// Note that R is the lowest byte of rgb[3].
{
	return ( ( rgb[2] * 256u ) + rgb[1] ) * 256u + rgb[0];
}


static void UnsignedIntToRGB( GLubyte rgb[3], unsigned int i )
	// Convert an integer to RGB 8-bit triplets.
	// The input integer must have value from 0 to (2^24 - 1).
	// Note that R is the lowest byte of rgb[3].
{
	rgb[0] = i % 256u;
	i = i / 256u;
	rgb[1] = i % 256u;
	rgb[2] = i / 256u;
}



static void ReadColorBuffer( GLubyte *buf, bool frontBuffer, int x, int y, int width, int height )
	// Read the RGB color buffer in the window region of size width x height.
	// The bottom-left corner of this window region is at (x, y).
	// If frontBuffer == true, it reads the front buffer, otherwise, the back buffer.
	// The read color buffer region is stored in the 1-D array buf[], which must be 
	// pre-allocated enough memory space.
{
	glPushAttrib( GL_ALL_ATTRIB_BITS );
	glPixelStorei( GL_PACK_ALIGNMENT, 1 );
	if ( frontBuffer )
		glReadBuffer( GL_FRONT );
	else
		glReadBuffer( GL_BACK );
	glReadPixels( x, y, width, height, GL_RGB, GL_UNSIGNED_BYTE, (void *)buf );
	glPopAttrib();
}




static GLuint MakeGathererQuadsDisplayList( const QM_Model *m )
	// Build a OpenGL display list for all the gatherer quads.
	// Each gatherer quad is rendered in a unique color.
	// Used for rendering the quads for the hemicube.
{
	GLubyte rgb[3];
	GLuint dlist = glGenLists( 1 );
	if ( dlist == 0 ) ShowFatalError( __FILE__, __LINE__, "Cannot create display list" );
	glNewList( dlist, GL_COMPILE );
		glBegin( GL_QUADS );
			for ( int q = 0; q < m->totalGatherers; q++ )
			{
				QM_GathererQuad *quad = m->gatherers[q];
				UnsignedIntToRGB( rgb, (unsigned int) q );
				glColor3ubv( rgb );
				glVertex3fv( quad->v[0] );
				glVertex3fv( quad->v[1] );
				glVertex3fv( quad->v[2] );
				glVertex3fv( quad->v[3] );
			}
		glEnd();
	glEndList();
	return dlist;
}




static float TriangleArea( const float v1[3], const float v2[3], const float v3[3] )
	// Return the area of the triangle defined by the 3 input vertices.
{
	float normal[3];
	VecTriNormal( normal, v1, v2, v3 );
	return 0.5f * VecLen( normal );
}



static float ComputeHemicubeWidth( const QM_ShooterQuad *shooterQuad )
	// Compute the width of the hemicube such that it is within the boundary of the quad.
{
	const float SQRT_2 = 1.414214f;

	float c01 = TriangleArea( shooterQuad->centroid, shooterQuad->v[0], shooterQuad->v[1] );
	float c12 = TriangleArea( shooterQuad->centroid, shooterQuad->v[1], shooterQuad->v[2] );
	float c23 = TriangleArea( shooterQuad->centroid, shooterQuad->v[2], shooterQuad->v[3] );
	float c30 = TriangleArea( shooterQuad->centroid, shooterQuad->v[3], shooterQuad->v[0] );

	float h01 = 2.0f * c01 / VecDist( shooterQuad->v[0], shooterQuad->v[1] );
	float h12 = 2.0f * c12 / VecDist( shooterQuad->v[1], shooterQuad->v[2] );
	float h23 = 2.0f * c23 / VecDist( shooterQuad->v[2], shooterQuad->v[3] );
	float h30 = 2.0f * c30 / VecDist( shooterQuad->v[3], shooterQuad->v[0] );

	return SQRT_2 * Min3( Min2( h01, h12), h23, h30 );
}

static void RenderHemicubePixel(float center[3], float lookAt[3], float up[3], \
								float frustumLeft, float frustumRight, float frustumBottom, float frustumTop, float frustumNear, float frustumFar)
	// Render Individual side
{
	glClearColor(0,0,0,1);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glPushMatrix();

	// Set up view frustum
	glMatrixMode( GL_PROJECTION );
	glLoadIdentity();
	glFrustum(frustumLeft, frustumRight, frustumBottom, frustumTop, frustumNear, frustumFar);

	// Set up camera
	glMatrixMode( GL_MODELVIEW );
	glLoadIdentity();
	gluLookAt(center[0], center[1], center[2], lookAt[0], lookAt[1], lookAt[2], up[0], up[1], up[2]);

	// draw scene
	glCallList(gathererQuadsDList);

	glPopMatrix();

	glFlush();

}
static void RenderHemicube(QM_ShooterQuad* shooter, float upVect[3])
	// Render Hemicube in shooter point of view
	// Put the texture maps to textureMap for further processing
{
	if(NULL == shooter)
		return;

	float lookAtVec[3];
	
	// Render top Hemicube
	VecSum(lookAtVec, shooter->centroid, shooter->normal);
	RenderHemicubePixel(shooter->centroid, lookAtVec, upVect, -hemicubeSize/2, hemicubeSize/2, -hemicubeSize/2, hemicubeSize/2, hemicubeSize/2, viewFrustumFar);

	// Render 4 side Hemicube
	float tmpVec1[3], tmpVec2[3];

	// Side 1 - view direction = centroid x normal
	VecCrossProd(tmpVec1, shooter->centroid, shooter->normal);
	VecSum(lookAtVec, shooter->centroid, tmpVec1);
	RenderHemicubePixel(shooter->centroid, lookAtVec, upVect, -hemicubeSize/2, hemicubeSize/2, 0, hemicubeSize/2, hemicubeSize/2, viewFrustumFar);

	// Side 2 - flip previous view direction
	VecNeg(tmpVec2, tmpVec1);
	VecSum(lookAtVec, shooter->centroid, tmpVec2);
	RenderHemicubePixel(shooter->centroid, lookAtVec, upVect, -hemicubeSize/2, hemicubeSize/2, 0, hemicubeSize/2, hemicubeSize/2, viewFrustumFar);

	// Side 3 - view direction = previous view direction x normal
	VecCrossProd(tmpVec1, tmpVec2, shooter->normal);
	VecSum(lookAtVec, shooter->centroid, tmpVec1);
	RenderHemicubePixel(shooter->centroid, lookAtVec, upVect, -hemicubeSize/2, hemicubeSize/2, 0, hemicubeSize/2, hemicubeSize/2, viewFrustumFar);

	// Side 4 - flip previous view direction
	VecNeg(tmpVec2, tmpVec1);
	VecSum(lookAtVec, shooter->centroid, tmpVec2);
	RenderHemicubePixel(shooter->centroid, lookAtVec, upVect, -hemicubeSize/2, hemicubeSize/2, 0, hemicubeSize/2, hemicubeSize/2, viewFrustumFar);
}

static void ComputeTopFaceDeltaFormFactors( float deltaFormFactors[], int numPixelsOnWidth )
	// Compute the delta form factors on the top face of the hemicube.
	// The results are store in the 1-D array deltaFormFactors[] of 
	// size of (numPixelsOnWidth x numPixelsOnWidth) elements.
	// Note that numPixelsOnWidth must be a even number.
{
	/********************** ADD HERE **************************/
	// Un-optimized code, calculate all value (instead of copying)
	// dF = dA/(pi*(x^2 + y^2 +1 )^2
	float x = 0;
	float y = 0;
	float dA = sqrt((float) 2/800);
	for(unsigned int i=0; i<numPixelsOnWidth; ++i)
	{
		for(unsigned int j=0; j<numPixelsOnWidth; ++j)
		{
			x = -1 + 1.0f/800 + (float)i/400;
			y = -1 + 1.0f/800 + (float)j/400;
			deltaFormFactors[numPixelsOnWidth * i + j] = dA/sqrt(x*x + y*y + 1);
		}
	}
}


static void ComputeSideFaceDeltaFormFactors( float deltaFormFactors[], int numPixelsOnWidth )
	// Compute the delta form factors on a side face of the hemicube.
	// The results are store in the 1-D array deltaFormFactors[] of 
	// size of [(numPixelsOnWidth/2) x numPixelsOnWidth] elements.
	// Note that numPixelsOnWidth must be a even number.
{
	/********************** ADD HERE **************************/
	// Un-optimized code, calculate all value (instead of copying)
	// dF = dA*z/(pi*(z^2 + y^2 +1 )^2
	float z = 0;
	float y = 0;
	float dA = sqrt((float) 2/800);
	for(unsigned int i=0; i<numPixelsOnWidth/2; ++i)
	{
		for(unsigned int j=0; j<numPixelsOnWidth; ++j)
		{
			z = -1 + 1.0f/800 + (float)i/400;
			y = -1 + 1.0f/800 + (float)j/400;
			deltaFormFactors[numPixelsOnWidth * i + j] = dA * z/sqrt(z*z + y*y + 1);
		}
	}
}




/////////////////////////////////////////////////////////////////////////////
// The display callback function.
// This is where the progressive refinement radiosity computation is performed.
/////////////////////////////////////////////////////////////////////////////

static void ComputeRadiosity( void )
{
	// Allocate temporary memory for reading in the colorbuffer.
	GLubyte *colorBuf = (GLubyte *) CheckedMalloc( sizeof(GLubyte) * 3 * winWidthHeight * winWidthHeight );

	// Allocate temporary memory for recording new RGB radiosity for each gatherer quad.
	float *newRadiosities = (float *) CheckedMalloc( sizeof(float) * 3 * model.totalGatherers );

	int iterationCount = 0;

	// To maintain a constant up vector, arbitary pick one
	float upVector[3] = {1,1,1};

	if( NULL == topDeltaFormFactors || NULL == sideDeltaFormFactors)
		return;

	while( 1 )
	{
		/********************** ADD HERE **************************/
		// Find shooter with greatest unshot power (maxShooter)
		QM_ShooterQuad *maxShooter = NULL;
		float maxUnshotPower = 0;
		
		for(unsigned int i=0; i<model.totalShooters; ++i)
		{
			QM_ShooterQuad *currShooter = model.shooters[i];
		
			if(NULL == currShooter)
				continue;

			if(VecLen(currShooter->unshotPower) > maxUnshotPower)
			{
				maxShooter = currShooter;
				maxUnshotPower = VecLen(maxShooter->unshotPower);
			}
		}

		if(NULL == maxShooter)
			break;

		// Render Hemicube from maxShooter point of view
		// ToDo: RenderHemicube(QM_ShooterQuad*, float upVect[3], Texture* top, Texture* side);
		RenderHemicube(maxShooter, upVector);

		return;

		// Get gatherer radiosity

		// Get gatherer form factors.

		// for each gatherer quad
		// {
		//		for each R,G,B component
		//		{
		//			- Calculate received radiosity (newRadiosity).
		//			- Add new radiosity value to gatherer's radiosity.
		//			- new Unshot power = newRadiosity * gatherer's area;
		//			- Update gatherer's parent's unshot power with new
		//				unshot power. (Gatherer's parent is a shooter)
		//		}
		//	}

		//	Set maxShooter's unshot power to 0.



		iterationCount++;
	}
	
	free( colorBuf );
	free( newRadiosities );
	printf( "Radiosity computation completed.\n" );

	printf( "Computing vertex radiosities...\n" );
	QM_ComputeVertexRadiosities( &model );

	printf( "Writing output model file...\n" );
	QM_WriteGatherersToFile( outputModelFilename, &model );

	printf( "DONE.\n" );
	printf( "Press ENTER to exit program.\n" );

	char ch;
	scanf( "%c", &ch );
	exit( 0 );
}




/////////////////////////////////////////////////////////////////////////////
// The reshape callback function.
/////////////////////////////////////////////////////////////////////////////

static void MyReshape( int w, int h )
{
	if ( w != winWidthHeight || h != winWidthHeight )
		ShowFatalError( __FILE__, __LINE__, "Window size has been changed" );
}



/////////////////////////////////////////////////////////////////////////////
// Initialize some OpenGL states.
/////////////////////////////////////////////////////////////////////////////

static void InitOpenGL( void )
{
	// Set background color.
    glClearColor( backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3] ); 

	glShadeModel( GL_FLAT );
	glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
    glEnable( GL_DEPTH_TEST );
	glDisable( GL_DITHER );
	glDisable( GL_BLEND );
	glDisable( GL_LIGHTING );
	glDisable( GL_CULL_FACE );
}



/////////////////////////////////////////////////////////////////////////////
// Initialize for the progressive refinement radiosity computation.
/////////////////////////////////////////////////////////////////////////////

static void InitRadiosityComputation( void )
{
// Check that we have 24-bit RGB colorbuffer for item buffering.
	GLint Rbits, Gbits, Bbits;
	glGetIntegerv( GL_RED_BITS, &Rbits );
	glGetIntegerv( GL_GREEN_BITS, &Gbits );
	glGetIntegerv( GL_BLUE_BITS, &Bbits );
	printf( "R = %d bits, G = %d bits, B = %d bits\n", Rbits, Gbits, Bbits );

	if ( Rbits != 8 || Gbits != 8 || Bbits != 8 )
		ShowFatalError( __FILE__, __LINE__, "Colorbuffer is not 24-bit RGB" );

// Read input model file.
	printf( "Reading input model file...\n" );
	model = QM_ReadFile( inputModelFilename );

// Subdivide the original quads to shooter quads and gatherer quads.
	printf( "Subdividing original quads...\n" );
	QM_Subdivide( &model, maxShooterQuadEdgeLength, maxGathererQuadEdgeLength );

// Make OpenGL display list for the gatherer quads.
	gathererQuadsDList = MakeGathererQuadsDisplayList( &model );

// Pre-compute the delta form factors for the fixed window resolution.
	topDeltaFormFactors = (float *) CheckedMalloc( sizeof(float) * winWidthHeight * winWidthHeight );
	sideDeltaFormFactors = (float *) CheckedMalloc( sizeof(float) * winWidthHeight * winWidthHeight / 2 );
	ComputeTopFaceDeltaFormFactors( topDeltaFormFactors, winWidthHeight );
	ComputeSideFaceDeltaFormFactors( sideDeltaFormFactors, winWidthHeight );

// Initialize the unshot power of the shooter quads.
	for ( int s = 0; s < model.totalShooters; s++ )
	{
		QM_ShooterQuad *shooterQuad = model.shooters[s];
		shooterQuad->unshotPower[0] = shooterQuad->area * shooterQuad->surface->emission[0];
		shooterQuad->unshotPower[1] = shooterQuad->area * shooterQuad->surface->emission[1];
		shooterQuad->unshotPower[2] = shooterQuad->area * shooterQuad->surface->emission[2];
	}

// Initialize the radiosity of the gatherer quads.
	for ( int g = 0; g < model.totalGatherers; g++ )
	{
		QM_GathererQuad *gathererQuad = model.gatherers[g];
		gathererQuad->radiosity[0] = gathererQuad->surface->emission[0];
		gathererQuad->radiosity[1] = gathererQuad->surface->emission[1];
		gathererQuad->radiosity[2] = gathererQuad->surface->emission[2];
	}
}




/////////////////////////////////////////////////////////////////////////////
// The main function.
/////////////////////////////////////////////////////////////////////////////

int main( int argc, char** argv )
{   
	printf( "Do not minimize, move, resize, or cover the drawing window.\n" );
	printf( "Press ENTER to start the radiosity computation.\n" );
	char ch;
	scanf( "%c", &ch );

// Initialize GLUT and create the drawing window.
    glutInit( &argc, argv );
    glutInitDisplayMode ( GLUT_RGB | GLUT_SINGLE | GLUT_DEPTH );
    glutInitWindowSize( winWidthHeight, winWidthHeight ); // Window must be square and size is fixed.
    glutCreateWindow( "Radiosity Solver" );
    InitOpenGL();

// Initialize for the progressive refinement radiosity computation.
	InitRadiosityComputation();

// Register the callback functions.
    glutDisplayFunc( ComputeRadiosity ); 
    glutReshapeFunc( MyReshape );

// Enter GLUT event loop.
    glutMainLoop();
    return 0;
}
