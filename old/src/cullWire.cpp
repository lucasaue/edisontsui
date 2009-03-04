#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <GL/glut.h>

#define ANGLE_STEP 0.5
#define RADIUS_STEP 1.1

bool culling = false;
bool wire = false;
GLenum wireface = GL_BACK;
GLenum cullface = GL_BACK;


void updateCameraPosition(float dangle, float dradius) {
	static struct {
		float angle;
		float radius;
	} position = { 0.0, 10.0 };

	position.angle += dangle;
	position.radius *= dradius;

	float lx = sin(position.angle);
	float lz = cos(position.angle);
	glLoadIdentity();
	gluLookAt(position.radius*lx,0,position.radius*lz, 
		0.0,0.0,0.0,
		0.0f,1.0f,0.0f);
}

void renderScene() {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glPushMatrix();

	if(culling)
	{
		glEnable(GL_CULL_FACE);
		glCullFace(cullface);
	}
	else
	{
		glDisable(GL_CULL_FACE);
	}
	if(wire)
	{
		glPolygonMode(wireface, GL_LINE);
	}
	else
	{
		glPolygonMode(wireface, GL_FILL);
	}
	// face
	glBegin(GL_QUADS);
	// green CW
	glColor3f(1.0, 1.0, 1.0);
	glVertex3i(2,1,0);
	glColor3f(0.0, 0.0, 1.0);
	glVertex3i(2,-1,0);
	glColor3f(1.0, 0.0, 0.0);
	glVertex3i(-2,-1,0);
	glColor3f(0.0, 1.0, 0.0);
	glVertex3i(-2,1,0);
	// red CCW - default front
	//glColor3f(1.0, 0.0, 0.0);
	//glVertex3i(2,1,0);
	//glVertex3i(-2,1,0);
	//glVertex3i(-2,-1,0);
	//glVertex3i(2,-1,0);
	glEnd();

	glPopMatrix();

	glutSwapBuffers();
} 

void changeSize(int w, int h) {
	if(h == 0)
		h = 1;
	float ratio = 1.0* w / h;

	//everything related to setting the right viewport
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glViewport(0, 0, w, h);
	//the last two arguments are zNear and zFar
	gluPerspective(45,ratio,1,1000);

	//back to normal: updating camera position
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	updateCameraPosition(0,1);
}

void normalKey(unsigned char key, int x, int y) {
	switch (key)
	{
	case 27:
		//escape
		exit(0);
		break;
	case 'c':
		culling = !culling;
		break;
	case 'v':
		cullface = GL_BACK;
		break;
	case 'b':
		cullface = GL_FRONT;
		break;
	case 'w':
		wire = !wire;
		break;
	case 'e':
		wireface = GL_BACK;
		break;
	case 'r':
		wireface = GL_FRONT;
		break;
	default:
		break;
	}
}

void specialKey(int key, int x, int y) {
	switch (key) {
	case GLUT_KEY_LEFT:
		updateCameraPosition(ANGLE_STEP,1);
		break;
	case GLUT_KEY_RIGHT:
		updateCameraPosition(-ANGLE_STEP,1);
		break;
	case GLUT_KEY_UP:
		updateCameraPosition(0,1.0/RADIUS_STEP);
		break;
	case GLUT_KEY_DOWN:
		updateCameraPosition(0,RADIUS_STEP);
		break;
	default:
		;
	}
}

void init() {
	glShadeModel(GL_SMOOTH);
	glEnable(GL_DEPTH_TEST);

	//glEnable(GL_CULL_FACE);
	//glCullFace(GL_FRONT);
}

int main(int argc, char *argv[]) {
	glutInit(&argc, argv);
	glutInitWindowPosition(-1, -1);
	glutInitWindowSize(640, 480);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
	glutCreateWindow("Project 1 - Task 1");

	init();

	glutReshapeFunc(changeSize);
	glutDisplayFunc(renderScene);
	glutKeyboardFunc(normalKey);
	glutSpecialFunc(specialKey);

	glutIdleFunc(renderScene);

	glutMainLoop();
}
