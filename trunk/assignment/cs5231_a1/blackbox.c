#include <iostream>
#include <stdio.h>
#include <string.h>
#include <gmp.h>
#include <time.h>
#include <gsl/gsl_statistics.h>
#include <getopt.h>

using namespace std;

#define DEFAULT_REFINE_ITERATE 30000
#define DEFAULT_KEY_ITERATE 2000
#define DEFAULT_THRESHOLD 0.3

enum MODE
{
	MODE_NONE =-1,
	MODE_GENERATE =0,
	MODE_CRACK =1
};

enum DEBUG_TYPE
{
	DEBUG_NONE		= 0x00,
	DEBUG_KEY		= 0x01,
	DEBUG_READFILE	= 0x02,
	DEBUG_KEYTIME	= 0x04,
	DEBUG_OPT		= 0x08,
	DEBUG_STAT		= 0x10,
	DEBUG_MSG		= 0x20
};

// parameter
//	m - encrypted msg
//	n - public n
//	t - temp val, final output
void myexp(int d[], mpz_t &m, mpz_t &n,  mpz_t &t)
{
int j,j1;

      mpz_set_si( t, 1);
      
      for (j1=0; d[j1]!=1; j1++);
      for (j=j1; j<1000;j++)
      {
        mpz_mul( t, t, t);
        mpz_mod( t, t, n);
        if (d[j]==1)
          {
            mpz_mul(t,t,m);
            mpz_mod(t,t,n);
          }  
      }
}

void makeKey(int key[], unsigned int keySize, int keyVal, int keyValSize)
{
	for(unsigned int i=0;i<keySize;++i)
	{
		key[i] = 0;
	}
	for(unsigned int i=0; i<keyValSize;++i)
	{
		key[keySize-i-1] = (keyVal>>i & 0x01 == 0x01)?1:0;
	}
}

int printKey(int d[], unsigned int keySize)
{
	cout << "d: ";
	for(unsigned int i=0;i<keySize;++i)
	{
		cout << d[i];
	}
	cout << endl;
}

int printKeyD(double d[], unsigned int keySize)
{
	cout << "d: ";
	for(unsigned int i=0;i<keySize;++i)
	{
		cout << d[i] << ",";
	}
	cout << endl;
}

void getDecryptTime(MODE mode, int d[], mpz_t &m, mpz_t &n,  mpz_t &t, unsigned int keyIterate, unsigned int refineIterate, int debug, double *keyTime)
{
	gmp_randstate_t state;
	gmp_randinit_default(state);
	gmp_randseed_ui( state, 2);
	mpz_urandomb( n, state, 1000);	

	for (unsigned int k=1;k<keyIterate;k++)
	{
		mpz_urandomb (m, state, 1000);
		if ((mode == MODE_GENERATE && k==1) || (debug & DEBUG_MSG)==DEBUG_MSG)
		{
			gmp_printf("%Zd\n", m);
		}
		if(k==1 && ((debug & DEBUG_KEY) == DEBUG_KEY))
		{
			gmp_printf("%Zd\n", m);
			printKey(d, 1000);
		}

		int timer1, timer2;
		timer1=clock();
		for (unsigned int i=0;i<=refineIterate;i++)
		{
			myexp ( d, m, n, t);
		}
		timer2=clock();

		keyTime[k-1] =  timer2-timer1;
		if(mode == MODE_GENERATE)
			cout << k << "   " << keyTime[k-1]<< endl;
	}
}
void parseOption(int argc, char *argv[], MODE &mode, float &threshold, int &dKeyMask, char *fileName, int &keyIterate, int &refineIterate, int &debug)
{
	int c;
	int errflg =0;
	while ((c = getopt(argc, argv, "m:k:f:r:e:t:d:")) != -1) 
	{
		switch(c) {
			case 'm':
				if(optarg[0] == 'g')
					mode = MODE_GENERATE;
				else if(optarg[0] == 'c')
					mode = MODE_CRACK;
				break;
			case 't':
				sscanf(optarg,"%f", &threshold);
				break;
			case 'f':
				strcpy(fileName, optarg);
				break;
			case 'r':
				sscanf(optarg, "%d", &refineIterate);
				break;
			case 'e':
				sscanf(optarg, "%d", &keyIterate);
				break;
			case 'k':
				sscanf(optarg, "%d", &dKeyMask);
				break;
			case 'd':
				sscanf(optarg, "%d", &debug);
				break;
			case '?':
				fprintf(stderr,
					"Unrecognized option\n");
				errflg++;
		}
		if (errflg) {
			fprintf(stderr, "usage:-m mode -t threshold -f file -k key -r refineIter -e keyIter");
			exit(2);
		}
	}
	if((debug & DEBUG_OPT) == DEBUG_OPT)
		printf("Mode: %d, Threshold: %f, File: %s, Key: %d, RefineIter: %d, KeyIter: %d, debug: %d\n", mode, threshold, fileName, dKeyMask, refineIterate, keyIterate, debug);
	if(mode == MODE_NONE || dKeyMask == -1 || (mode == MODE_CRACK && dKeyMask>30))
	{
		fprintf(stderr, "usage:-m mode -t threshold -f file -k key -r refineIter -e keyIter");
		exit(2);
	}
	return;
}

int readTimeFile(char *fileName, double *keyTime, int arraySize, int debug)
{
	FILE *fd = fopen(fileName, "r");
	if(fd == NULL)
	{
		printf("file: %s\n", fileName);
		return -1;
	}
	char buff[2049];
	fgets(buff, 2049, fd);
	int i =0;
	int rowno, val;
	while(fgets(buff, 2049, fd)!=NULL)
	{
		if(i>=arraySize)
			break;
		sscanf(buff, "%d %d", &rowno, &val);
		keyTime[i] = val;
		if((debug & DEBUG_READFILE) == DEBUG_READFILE)
			printf("readFile: %d %d %f\n", rowno, val, keyTime[i]);
		++i;
	}
	return 0;
}

int main(int argc, char *argv[])
{
MODE mode = MODE_NONE;
int keyIterate = DEFAULT_KEY_ITERATE;
int refineIterate = DEFAULT_REFINE_ITERATE;
int dKeyMask = -1;
char fileName[512];
float threshold = DEFAULT_THRESHOLD;
int debug = DEBUG_NONE;

parseOption(argc, argv, mode, threshold, dKeyMask, fileName, keyIterate, refineIterate, debug);

int dKey[1000];
mpz_t n;
mpz_t m;
mpz_t t;
mpz_init2(n,1000);				
mpz_init2(m,2048);
mpz_init2(t,2048);
struct 
{
	int key;
	float correlation;
} acceptedKey;

acceptedKey.key = 0;
acceptedKey.correlation = threshold;

if(mode == MODE_GENERATE)
{
	double *keyTime_t;
	// init 
	keyTime_t = (double *) malloc(keyIterate * sizeof(double));
	memset(keyTime_t, 0x0, keyIterate * sizeof(double));
	if(keyTime_t==NULL)
	{
		printf("malloc error\n");
		return -1;
	}
	// assign d - secret key
	makeKey(dKey, 1000, dKeyMask, sizeof(dKeyMask));
	getDecryptTime(mode, dKey, m, n,  t, keyIterate, refineIterate, debug, keyTime_t);
	free(keyTime_t);
}
else
{
	double *keyTime_t, *keyTime_m, *keyCorrelation;
	// init 
	keyTime_t = (double *) malloc(keyIterate * sizeof(double));
	memset(keyTime_t, 0x0, keyIterate * sizeof(double));
	keyTime_m = (double *) malloc(keyIterate * sizeof(double));
	memset(keyTime_m, 0x0, keyIterate * sizeof(double));
	keyCorrelation = (double *) malloc(dKeyMask * sizeof(double));
	memset(keyCorrelation, 0x0, dKeyMask * sizeof(double));
	if(keyTime_t==NULL || keyTime_m==NULL || keyCorrelation==NULL)
	{
		printf("malloc error\n");
		return -1;
	}
	if(readTimeFile(fileName, keyTime_t, keyIterate, debug)==-1)
	{
		printf("read error\n");
		return -1;
	}

	// start cracking
	int keyVal = 0;
	for(unsigned int j=0;j<dKeyMask;++j)
	{
		int stepMask = 0x1 << j;
		keyVal = keyVal | stepMask;
		makeKey(dKey,1000,keyVal,dKeyMask);

		getDecryptTime(mode, dKey, m, n,  t, keyIterate, refineIterate, debug, keyTime_m);
		if((debug & DEBUG_KEYTIME) == DEBUG_KEYTIME)
		{
			printKeyD(keyTime_t, keyIterate-1);
			printKeyD(keyTime_m, keyIterate-1);
		}
		// check correlation
		keyCorrelation[j] = gsl_stats_correlation(keyTime_t, 1, keyTime_m, 1, keyIterate-1);
		cout << "Iteration: " << j << ", keyVal:" << keyVal << ", correlation:" << keyCorrelation[j] << endl;
		
		if((debug & DEBUG_STAT) == DEBUG_STAT)
		{
			double cov = gsl_stats_covariance(keyTime_t, 1, keyTime_m, 1, keyIterate-1);
			double sd_t = gsl_stats_sd(keyTime_t, 1, keyIterate-1);
			double sd_m = gsl_stats_sd(keyTime_m, 1, keyIterate-1);
			double mean_t = gsl_stats_mean(keyTime_t, 1, keyIterate-1);
			double mean_m = gsl_stats_mean(keyTime_m, 1, keyIterate-1);
			printf("cov: %f, \n    T: sd: %f, mean: %f, \n    M: sd: %f, mean: %f\n", cov, sd_t, mean_t, sd_m, mean_m);
		}
		// if fail, reset keyVal
		if( keyCorrelation[j] < threshold)
		{
			cout << "key rejected, ref: " << acceptedKey.correlation << endl ;
			keyVal = keyVal & ~stepMask;
		}
		else
		{
			cout << "key accepted, ref: " << acceptedKey.correlation <<  endl;
			acceptedKey.key = keyVal;
			acceptedKey.correlation = keyCorrelation[j];
			//threshold = acceptedKey.correlation;
		}	 
	}
	cout << "Key: " << keyVal << endl;
	free(keyCorrelation);
	free(keyTime_m);
	free(keyTime_t);
}


}
