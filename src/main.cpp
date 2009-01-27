#include <stdio.h>
#include <math.h>
#include <GL/glut.h>

// global var
float angle =0.0f;
float red = 0.0f;
float blue = 0.0f;
float green = 0.0f;

float x = 0.0f;
float y = 1.75f;
float z = 5.0f;
float lx = 0.0f;		// line of sight
float ly = 0.0f;
float lz = -1.0f;
float camAngle = 0.0f;

float deltaAngle = 0.0f;	// for key repeat
float deltaMove = 0.0f;

static GLint snowman_display_list;


// helper func
void orientMe(float ang) {

	lx = sin(ang);
	lz = -cos(ang);
	glLoadIdentity();
	gluLookAt(x, y, z, 
		x + lx,y + ly,z + lz,
		0.0f,1.0f,0.0f);
}

void moveMeFlat(int direction) {
	x = x + direction*(lx)*0.1;
	z = z + direction*(lz)*0.1;
	glLoadIdentity();
	gluLookAt(x, y, z, 
		x + lx,y + ly,z + lz,
		0.0f,1.0f,0.0f);
}

void drawSnowMan() {

	glColor3f(red, green, blue);

	// Draw Body	
	glTranslatef(0.0f ,0.75f, 0.0f);
	glutSolidSphere(0.75f,20,20);


	// Draw Head
	glTranslatef(0.0f, 1.0f, 0.0f);
	glutSolidSphere(0.25f,20,20);

	// Draw Eyes
	glPushMatrix();
	glColor3f(0.0f,0.0f,0.0f);
	glTranslatef(0.05f, 0.10f, 0.18f);
	glutSolidSphere(0.05f,10,10);
	glTranslatef(-0.1f, 0.0f, 0.0f);
	glutSolidSphere(0.05f,10,10);
	glPopMatrix();

	// Draw Nose
	glColor3f(1.0f, 0.5f , 0.5f);
	glRotatef(0.0f,1.0f, 0.0f, 0.0f);
	glutSolidCone(0.08f,0.5f,10,2);
}

GLuint createDL() 
{
	GLuint snowManDL;

	// Create the id for the list
	snowManDL = glGenLists(1);

	// start list
	glNewList(snowManDL,GL_COMPILE);

	// call the function that contains 
	// the rendering commands
	drawSnowMan();

	// endList
	glEndList();

	return(snowManDL);
}

void initScene() 
{

	glEnable(GL_DEPTH_TEST);
	snowman_display_list = createDL();
}

// GLUT hock
void renderScene(void) {
	if(deltaMove != 0.0f)
	{
		moveMeFlat(deltaMove);
	}
	if(deltaAngle != 0.0f)
	{
		camAngle += deltaAngle;
		orientMe(camAngle);
	}

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Draw ground

	glColor3f(red-0.2f, green-0.1f, blue-0.2f);
	glBegin(GL_QUADS);
	glVertex3f(-100.0f, 0.0f, -100.0f);
	glVertex3f(-100.0f, 0.0f,  100.0f);
	glVertex3f( 100.0f, 0.0f,  100.0f);
	glVertex3f( 100.0f, 0.0f, -100.0f);
	glEnd();

	// Draw 36 SnowMen
	// reinit to make snowman array color correct (if fixed color dun need to reinit)
	initScene();

	for(int i = -3; i < 3; i++)
		for(int j=-3; j < 3; j++) {
			glPushMatrix();
			glTranslatef(i*10.0,0,j * 10.0);
			glCallList(snowman_display_list);;
			glPopMatrix();
		}
		glutSwapBuffers();
}

void renderSceneEx1(void)
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glPushMatrix();

	glColor3f(red, green, blue);

	glRotatef(angle, 0, 1, 0);
	glBegin(GL_TRIANGLES);
	{
		glVertex3f(-0.5, -0.5, 0);
		glVertex3f(0.5, 0, 0);
		glVertex3f(0, 0.5, 0);
	}
	glEnd();

	glPopMatrix();
	glutSwapBuffers();
	angle++;
}

void testResize(int width, int height)
{
	if(height == 0)
		height = 1;
	float ratio = (float) width/(float) height;
	
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	glViewport(0,0, width, height);
	
	gluPerspective(45, ratio, 1, 1000);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt(x, y, z, x+lx,y+ly, z+lz, 0, 1, 0);
}

void testProcessNormalKey(unsigned char key, int x, int y)
{
	// Note: Shift + r => 'R' not 'r', diff key no
	int mod = glutGetModifiers();
	switch(key)
	{
	case 27:
		{
			exit(0);
		}
		break;
	case 'r':
		{
			(mod == GLUT_ACTIVE_ALT)?red=1:red=0;
		}
		break;
	case 'g':
		{
			(mod == GLUT_ACTIVE_ALT)?green=1:green=0;
		}
		break;
	case 'b':
		{
			(mod == GLUT_ACTIVE_ALT)?blue=1:blue=0;
		}
		break;
	default:
		break;
	}
}

void testProcessSpecKey(int key, int x, int y)
{
	int mod;
	switch(key) 
	{
		case GLUT_KEY_F1 : 
			mod = glutGetModifiers();
			if (mod == (GLUT_ACTIVE_CTRL|GLUT_ACTIVE_ALT)) 
			{
				red = 0.0; 
				green = 0.0; 
				blue = 0.0;
			}
			break;
		case GLUT_KEY_F2: 
			red = 1.0; 
			green = 0.0; 
			blue = 0.0; 
			break;
		case GLUT_KEY_F3: 
			red = 0.0; 
			green = 1.0; 
			blue = 0.0; 
			break;
		case GLUT_KEY_F4: 
			red = 0.0; 
			green = 0.0; 
			blue = 1.0; 
			break;
		case GLUT_KEY_LEFT : 
			deltaAngle -= 0.01f;
			orientMe(angle);
			break;
		case GLUT_KEY_RIGHT : 
			deltaAngle +=0.01f;
			orientMe(angle);
			break;
		case GLUT_KEY_UP : 
			deltaMove = 1;
			break;
		case GLUT_KEY_DOWN : 
			deltaMove = -1;
			break;

		default:
			break;
	}
}

void releaseKey(int key, int x, int y)
{
	switch(key)
	{
	case GLUT_KEY_LEFT:
	case GLUT_KEY_RIGHT:
		deltaAngle = 0.0f;
		break;
	case GLUT_KEY_UP:
	case GLUT_KEY_DOWN:
		deltaMove = 0.0f;
		break;
	default:
		break;
	}
}
// main, use GLUT to hock func and do work
void main(int argc, char **argv)
{
	printf("Start GLUT code\n");
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH | GLUT_DOUBLE | GLUT_RGBA);		// GLUT_DOUBLE cant work?
	glutInitWindowPosition(-1, -1);
	glutInitWindowSize(800, 600);
	glutCreateWindow("Edison GLUT basic");

	initScene();

	glutDisplayFunc(renderScene);
	glutIdleFunc(renderScene);

	glutReshapeFunc(testResize);

	glutIgnoreKeyRepeat(1);
	glutKeyboardFunc(testProcessNormalKey);
	glutSpecialFunc(testProcessSpecKey);
	glutSpecialUpFunc(releaseKey);

	glEnable(GL_DEPTH_TEST);

	printf("Enter GLUT MainLoop\n");
	glutMainLoop();
	printf("GLUT Mainloop end\n");
}