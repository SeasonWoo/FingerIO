/***dtwrecoge.h*********************************************************************/
#ifndef dtwrecogeOpt_h
#define dtwrecogeOpt_h

#include<stdio.h>
#include<math.h>
#include "GammaUtil.h"


class DtwRecogeOpt
{
public:
	DtwRecogeOpt(){};
	~DtwRecogeOpt(){};

float DTWDistanceFun(float *A, int I, float *B, int J, int r);

//int DTWTemplate(double *A,int I,double *B,int J,double *temp,int turn,double tt,double *rltdistance);

private:
	//double distance[DTWMAXNUM][DTWMAXNUM]; /*�������*/
    //double dtwpath[DTWMAXNUM][DTWMAXNUM]; /*����·��*/
};
#endif