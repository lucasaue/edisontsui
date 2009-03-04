#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <GL/glut.h>

#include <GL/GLAux.h>

#define YELLOWMAT   1
#define BLUEMAT 2

void myinit (void) 
{
	GLfloat yellow_diffuse[] = { 0.7, 0.7, 0.0, 1.0 };
	GLfloat yellow_specular[] = { 1.0, 1.0, 1.0, 1.0 };

	GLfloat blue_diffuse[] = { 0.1, 0.1, 0.7, 1.0 };
	GLfloat blue_specular[] = { 0.1, 1.0, 1.0, 1.0 };

	GLfloat position_one[] = { 1.0, 1.0, 1.0, 0.0 };

	glNewList(YELLOWMAT, GL_COMPILE);
	glMaterialfv(GL_FRONT, GL_DIFFUSE, yellow_diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, yellow_specular);
	glMaterialf(GL_FRONT, GL_SHININESS, 64.0);
	glEndList();

	glNewList(BLUEMAT, GL_COMPILE);
	glMaterialfv(GL_FRONT, GL_DIFFUSE, blue_diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, blue_specular);
	glMaterialf(GL_FRONT, GL_SHININESS, 45.0);
	glEndList();

	glLightfv(GL_LIGHT0, GL_POSITION, position_one);

	glEnable(GL_LIGHT0);
	glEnable(GL_LIGHTING);
	glDepthFunc(GL_LEQUAL);
	glEnable(GL_DEPTH_TEST);

	glClearStencil(0x0);
	glEnable(GL_STENCIL_TEST);
}

void display(void)
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	/* draw blue sphere where the stencil is 1 */
	glStencilFunc (GL_EQUAL, 0x1, 0x1);
	glCallList (BLUEMAT);
	auxSolidSphere (0.5);

	/* draw the tori where the stencil is not 1 */
	glStencilFunc (GL_NOTEQUAL, 0x1, 0x1);
	glStencilOp (GL_KEEP, GL_KEEP, GL_KEEP);
	glPushMatrix();
	glRotatef (45.0, 0.0, 0.0, 1.0);
	glRotatef (45.0, 0.0, 1.0, 0.0);
	glCallList (YELLOWMAT);
	auxSolidTorus (0.275, 0.85);
	glPushMatrix();
	glRotatef (90.0, 1.0, 0.0, 0.0);
	auxSolidTorus (0.275, 0.85);
	glPopMatrix();
	glPopMatrix();

	glFlush();
}

void myReshape(GLsizei w, GLsizei h)
{
	glViewport(0, 0, w, h);

	glClear(GL_STENCIL_BUFFER_BIT);
	/* create a diamond-shaped stencil area */
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(-3.0, 3.0, -3.0, 3.0, -1.0, 1.0);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	glStencilFunc (GL_ALWAYS, 0x1, 0x1);
	glStencilOp (GL_REPLACE, GL_REPLACE, GL_REPLACE);
	glBegin(GL_QUADS);
	glVertex3f (-1.0, 0.0, 0.0);
	glVertex3f (0.0, 1.0, 0.0);
	glVertex3f (1.0, 0.0, 0.0);
	glVertex3f (0.0, -1.0, 0.0);
	glEnd();

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(45.0, (GLfloat) w/(GLfloat) h, 3.0, 7.0);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glTranslatef(0.0, 0.0, -5.0);
}


int main(int argc, char** argv)
{
	auxInitDisplayMode (AUX_SINGLE | AUX_RGBA 
		| AUX_DEPTH | AUX_STENCIL);
	auxInitPosition (0, 0, 400, 400);
	auxInitWindow (argv[0]);
	myinit ();
	auxReshapeFunc ((AUXRESHAPEPROC)myReshape);
	auxMainLoop((AUXMAINPROC)display);
}