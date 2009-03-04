//#include <stdlib.h>
//#include <stdio.h>
//#include <string.h>
//#include <math.h>
//#include <GL/glut.h>
//
//#define ANGLE_STEP 0.5
//#define RADIUS_STEP 1.1
//
//static const float l0pos[] = { 5.0, 5.0, 0.0, 1.0 };
//static const float l0dir[] = { -1.0, -1.0, 0.0 };
//static const float l1pos[] = { -5.0, 5.0, 0.0, 1.0 };
//static const float l1dir[] = { 1.0, -1.0, 0.0 };
//static const float no_ambient[] = { 0.0, 0.0, 0.0, 1.0 };
//static const float tungsten_diffuse[] = { 1.0, 214.0/255, 170.0/255, 1.0 };
//static const float tungsten_specular[] = { 1.0, 214.0/255, 170.0/255, 1.0 };
//
//void updateCameraPosition(float dangle, float dradius) {
//	static struct {
//		float angle;
//		float radius;
//	} position = { 1.0, 5.0 };
//
//	position.angle += dangle;
//	position.radius *= dradius;
//
//	float lx = cos(position.angle);
//	float lz = sin(position.angle);
//	glLoadIdentity();
//	gluLookAt(position.radius*lx,1,position.radius*lz, 
//		0.0,0.0,0.0,
//		0.0f,1.0f,0.0f);
//}
//
//void renderScene() {
//	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//
//	glPushMatrix();
//
//	//the teapot
//	glColor3f(185.0/255, 185.0/255, 185.0/255);
//	glutSolidTeapot(1);
//
//	//the lights
//	glLightfv(GL_LIGHT0, GL_POSITION, l0pos);
//	glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, l0dir);
//	glLightfv(GL_LIGHT1, GL_POSITION, l1pos);
//	glLightfv(GL_LIGHT1, GL_SPOT_DIRECTION, l1dir);
//	glLightfv(GL_LIGHT1, GL_AMBIENT, no_ambient);
//	glLightfv(GL_LIGHT1, GL_SPECULAR, tungsten_specular);
//	glLightfv(GL_LIGHT1, GL_DIFFUSE, tungsten_diffuse);
//
//	//the table
//	//glColor3f(108.0/255, 40.0/255, 40.0/255);
//	glColor3f(0.0, 1.0, 0.0);
//	glBegin(GL_QUADS);
//	glVertex3i(2,-1,3);
//	glVertex3i(-2,-1,3);
//	glVertex3i(-2,-1,-3);
//	glVertex3i(2,-1,-3);
//	glEnd();
//
//	glPopMatrix();
//
//	glutSwapBuffers();
//} 
//
//void changeSize(int w, int h) {
//	if(h == 0)
//		h = 1;
//	float ratio = 1.0* w / h;
//
//	//everything related to setting the right viewport
//	glMatrixMode(GL_PROJECTION);
//	glLoadIdentity();
//	glViewport(0, 0, w, h);
//	//the last two arguments are zNear and zFar
//	gluPerspective(45,ratio,1,1000);
//
//	//back to normal: updating camera position
//	glMatrixMode(GL_MODELVIEW);
//	glLoadIdentity();
//	updateCameraPosition(0,1);
//}
//
//void normalKey(unsigned char key, int x, int y) {
//	if (key == 27) { //escape
//		exit(0);
//	}
//}
//
//void specialKey(int key, int x, int y) {
//	switch (key) {
//	case GLUT_KEY_LEFT:
//		updateCameraPosition(ANGLE_STEP,1);
//		break;
//	case GLUT_KEY_RIGHT:
//		updateCameraPosition(-ANGLE_STEP,1);
//		break;
//	case GLUT_KEY_UP:
//		updateCameraPosition(0,1.0/RADIUS_STEP);
//		break;
//	case GLUT_KEY_DOWN:
//		updateCameraPosition(0,RADIUS_STEP);
//		break;
//	default:
//		;
//	}
//}
//
//void init() {
//	glShadeModel(GL_SMOOTH);
//	glEnable(GL_LIGHTING);
//	glEnable(GL_LIGHT0);
//	glEnable(GL_LIGHT1);
//	glEnable(GL_DEPTH_TEST);
//}
//
//int main(int argc, char *argv[]) {
//	glutInit(&argc, argv);
//	glutInitWindowPosition(-1, -1);
//	glutInitWindowSize(640, 480);
//	glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
//	glutCreateWindow("Project 1 - Task 1");
//
//	init();
//
//	glutReshapeFunc(changeSize);
//	glutDisplayFunc(renderScene);
//	glutKeyboardFunc(normalKey);
//	glutSpecialFunc(specialKey);
//
//	glutIdleFunc(renderScene);
//
//	glutMainLoop();
//}
