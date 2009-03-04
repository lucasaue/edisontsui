//
// Matrix_time find the CPU time and wall time on N*N random generated matrix multiplication
// Parameter
//	-m (mode) i(integer)/f(float)
//	-s (size) 
//
// Author: edisontsui@gmail.com

#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <getopt.h>

#define RETURN_ERR -1
#define RETURN_SUCCESS 0

#define MY_RAND_MAX 10	// lower random cap to avoid int overflow

enum MODE
{
	MODE_INTEGER =0,
	MODE_FLOAT
};

// helper
void parseOption(int argc, char *argv[], MODE &mode, unsigned int &size, bool &debug)
{
	int c;
	while( (c = getopt(argc, argv, "m:s:d")) != -1)
	{
		switch(c)
		{
		case 'm':
			if(optarg[0] == 'i')
				mode = MODE_INTEGER;
			else if(optarg[0] == 'f')
				mode = MODE_FLOAT;
			break;
		case 's':
			int tmpsize;
			sscanf(optarg, "%d", &tmpsize);
			size = (unsigned int) tmpsize;
			break;
		case 'd':
			debug = true;
			break;
		default:
			break;
		}
	}
}

// Int
int initIntMatrix(int** &matrix, unsigned int size)
{
	/* Allocate pointer memory for the first dimension of a matrix[][]; */
	matrix = (int **) malloc(size * sizeof(int *));
	if(NULL == matrix)
	{
		free(matrix); 
		printf("Memory allocation failed while allocating for matrix[].\n"); 
		return RETURN_ERR;
	}

	/* Allocate integer memory for the second dimension of a matrix[][]; */
	for(unsigned int i = 0; i < size; ++i)
	{
		matrix[i] = (int *) malloc(size * sizeof(int));
		if(NULL == matrix[i])
		{
			free(matrix[i]);
			free(matrix);
			printf("Memory allocation failed while allocating for matrix[x][].\n");
			return RETURN_ERR;
		}
	}
	return RETURN_SUCCESS;
}

void randIntMatrix(int **matrix, unsigned int size)
{
	for(unsigned int i=0;i<size; ++i)
	{
		for(unsigned int j=0; j<size; ++j)
		{
			matrix[i][j] = 1 + (int) (MY_RAND_MAX * (rand() / (RAND_MAX + 1.0)));;
		}
	}
}

void printIntMatrix(int **matrix, unsigned int size)
{
	for(unsigned int i=0;i<size; ++i)
	{
		for(unsigned int j=0; j<size; ++j)
		{
			printf("%d ", matrix[i][j]);
		}
		printf("\n");
	}
}

int multiplyIntMatrix(unsigned int size, bool debug)
{
	clock_t startClock, endClock, diffClock;
	time_t startTime, endTime, diffTime;
	int **inputMatrix, **outputMatrix;

	// init
	if( RETURN_ERR == initIntMatrix(inputMatrix, size) || RETURN_ERR == initIntMatrix(outputMatrix, size))
		return RETURN_ERR;

	// fill in inputMatrix
	randIntMatrix(inputMatrix, size);

	// start measure
	startClock = clock();
	startTime = time(NULL);
	if( (clock_t)(-1) == startClock || (time_t)(-1) == startTime )
	{
		free(inputMatrix);
		free(outputMatrix);
		printf("Measurement error @ start\n");
		return RETURN_ERR;
	}

	//multiplication
	int sum;
	for(unsigned int i=0; i<size; ++i)
	{
		for(unsigned  int j=0; j<size; ++j)
		{
			sum = 0;
			for(unsigned int k=0; k<size; ++k)
				sum += inputMatrix[i][k]*inputMatrix[k][j];
			outputMatrix[i][j] = sum;
		}
	}

	// end measure
	endClock = clock();
	endTime = time(NULL);
	if( (clock_t)(-1) == endClock || (time_t)(-1) == endTime )
	{
		free(inputMatrix);
		free(outputMatrix);
		printf("Measurement error @ end\n");
		return RETURN_ERR;
	}

	// printout matrix if debug is set
	if( true == debug )
	{
		printf("Input Matrix: \n");
		printIntMatrix(inputMatrix, size);
		printf("\n");
		printf("Output Matrix: \n");
		printIntMatrix(outputMatrix, size);
		printf("\n");
	}

	// printout result
	diffTime = endTime - startTime; 
	diffClock = endClock - startClock; 
	printf ("time : start_time = %lu sec, end_time = %lu sec\n", startTime, endTime); 
	printf ("time : elapsed (wall clock) time = %lu sec\n", diffTime); 
	printf ("\n"); 
	printf ("clock : start_clock = %lu units, end_clock = %lu units\n", startClock, endClock); 
	printf ("clock : processor time used = %.3f sec\n", (double)diffClock/CLOCKS_PER_SEC); 

	return RETURN_SUCCESS;
}



// Float
float initFloatMatrix(float** &matrix, unsigned int size)
{
	/* Allocate pointer memory for the first dimension of a matrix[][]; */
	matrix = (float **) malloc(size * sizeof(float *));
	if(NULL == matrix)
	{
		free(matrix); 
		printf("Memory allocation failed while allocating for matrix[].\n"); 
		return RETURN_ERR;
	}

	/* Allocate integer memory for the second dimension of a matrix[][]; */
	for(unsigned int i = 0; i < size; ++i)
	{
		matrix[i] = (float *) malloc(size * sizeof(float));
		if(NULL == matrix[i])
		{
			free(matrix[i]);
			free(matrix);
			printf("Memory allocation failed while allocating for matrix[x][].\n");
			return RETURN_ERR;
		}
	}
	return RETURN_SUCCESS;
}

void randFloatMatrix(float **matrix, unsigned int size)
{
	for(unsigned int i=0;i<size; ++i)
	{
		for(unsigned int j=0; j<size; ++j)
		{
			matrix[i][j] = 1 + (float) (MY_RAND_MAX * (rand() / (RAND_MAX + 1.0)));;
		}
	}
}

void printFloatMatrix(float **matrix, unsigned int size)
{
	for(unsigned int i=0;i<size; ++i)
	{
		for(unsigned int j=0; j<size; ++j)
		{
			printf("%f ", matrix[i][j]);
		}
		printf("\n");
	}
}

float multiplyFloatMatrix(unsigned int size, bool debug)
{
	clock_t startClock, endClock, diffClock;
	time_t startTime, endTime, diffTime;
	float **inputMatrix, **outputMatrix;

	// init
	if( RETURN_ERR == initFloatMatrix(inputMatrix, size) || RETURN_ERR == initFloatMatrix(outputMatrix, size))
		return RETURN_ERR;

	// fill in inputMatrix
	randFloatMatrix(inputMatrix, size);

	// start measure
	startClock = clock();
	startTime = time(NULL);
	if( (clock_t)(-1) == startClock || (time_t)(-1) == startTime )
	{
		free(inputMatrix);
		free(outputMatrix);
		printf("Measurement error @ start\n");
		return RETURN_ERR;
	}

	//multiplication
	float sum;
	for(unsigned int i=0; i<size; ++i)
	{
		for(unsigned int j=0; j<size; ++j)
		{
			sum = 0;
			for(unsigned int k=0; k<size; ++k)
				sum += inputMatrix[i][k]*inputMatrix[k][j];
			outputMatrix[i][j] = sum;
		}
	}

	// end measure
	endClock = clock();
	endTime = time(NULL);
	if( (clock_t)(-1) == endClock || (time_t)(-1) == endTime )
	{
		free(inputMatrix);
		free(outputMatrix);
		printf("Measurement error @ end\n");
		return RETURN_ERR;
	}

	// printout matrix if debug is set
	if( true == debug )
	{
		printf("Input Matrix: \n");
		printFloatMatrix(inputMatrix, size);
		printf("\n");
		printf("Output Matrix: \n");
		printFloatMatrix(outputMatrix, size);
		printf("\n");
	}

	// printout result
	diffTime = endTime - startTime; 
	diffClock = endClock - startClock; 
	printf ("time : start_time = %lu sec, end_time = %lu sec\n", startTime, endTime); 
	printf ("time : elapsed (wall clock) time = %lu sec\n", diffTime); 
	printf ("\n"); 
	printf ("clock : start_clock = %lu units, end_clock = %lu units\n", startClock, endClock); 
	printf ("clock : processor time used = %.3f sec\n", (double)diffClock/CLOCKS_PER_SEC); 
	
	return RETURN_SUCCESS;
}

int main(int argc, char *argv[])
{
	MODE operationMode;
	unsigned int matrixSize;
	bool debug = false;

	if(argc < 5)
	{
		printf("Usage: %s -m(mode) i(int)/f(float) -s size\n", argv[0]);
		return RETURN_ERR;
	}

	// get option
	parseOption(argc, argv, operationMode, matrixSize, debug);

	if( MODE_INTEGER == operationMode )
	{
		multiplyIntMatrix(matrixSize, debug);
	}
	else if ( MODE_FLOAT == operationMode )
	{
		multiplyFloatMatrix(matrixSize, debug);	
	}

	return RETURN_SUCCESS;
}
