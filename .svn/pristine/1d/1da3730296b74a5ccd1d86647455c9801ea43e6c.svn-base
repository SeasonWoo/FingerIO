/*dtwrecoge.cpp**************************************************************/

#include "dtwrecogeopt.h"
#include <vector>
using namespace std;

#define REALMAX  17977000000
/*****************************************************************************/
/* DTWDistance������������֮���ƥ�����
/* A,B�ֱ�Ϊ��һ�ڶ������飬I��JΪ�����鳤�ȣ�rΪƥ�䴰�ڵĴ�С

/* r�Ĵ�Сһ��ȡΪ���鳤�ȵ�1/10��1/30
/* ������������֮���ƥ�����,������أ�1.0���������鳤��̫����
/*****************************************************************************/
float DtwRecogeOpt::DTWDistanceFun(float *A,int I,float *B,int J,int r)
{
	vector<vector<double> >  distance(DTWMAXNUM, vector<double>(DTWMAXNUM));
	vector<vector<double> >  dtwpath(DTWMAXNUM, vector<double>(DTWMAXNUM));

	int i,j;
	float dist;
	int istart,imax;
	int r2=r+ABS(I-J);/*ƥ�����*/
	float g1,g2,g3;
	int pathsig=1;/*·���ı�־*/

	/*����������Ч��*/
	if(I>REALMAX||J>REALMAX){
		//printf("Too big number\n");
		return -1.0;
	}
	
	/*����һЩ��Ҫ�ĳ�ʼ��*/
   for(i=0;i<I;i++)
   {
		for(j=0;j<J;j++)
		{
			dtwpath[i][j]=REALMAX;
			distance[i][j]=(A[i]-B[j])*(A[i]-B[j]);
			if(i==0 && j==0 )
			{
				dtwpath[0][0] = distance[0][0];
			}
		}
	}
   float D1 = 0;
   float D2 = 0;
   float D3 = 0;
	for(i=0;i<I;i++)
	{
		for(j=0;j<J;j++)
		{
			if(i==0 && j==0)
			{
				continue;
			}
			if(i>0)
			{
				D1 = dtwpath[i-1][j];
			}
			else
			{
				D1 = REALMAX;
			}

			if(j>0 && i>0 )
			{
				D2= dtwpath[i-1][j-1];
			}
			else
			{
				D2 = REALMAX;
			}

			if(j>0)
			{
				D3 = dtwpath[i][j-1];
			}
			else
			{
				D3 = REALMAX;
			}
			dtwpath[i][j] = distance[i][j] + MIN(MIN(D1,D2),D3);
		}
	}
	dist = dtwpath[I-1][J-1];
	return dist;
}/*end DTWDistance*/

