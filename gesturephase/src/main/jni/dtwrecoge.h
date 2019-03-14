/***dtwrecoge.h*********************************************************************/
#ifndef dtwrecoge_h
#define dtwrecoge_h

#include<stdio.h>
#include<math.h>
#include "GammaUtil.h"

class DtwRecoge
{
public:
/*****************************************************************************/
/* DTWDistance，求两个数组之间的匹配距离
/* A,B分别为第一第二个数组，I，J为其数组长度，r为匹配窗口的大小
/* r的大小一般取为数组长度的1/10到1/30
/* 返回两个数组之间的匹配距离,如果返回－1.0，表明数组长度太大了
/*****************************************************************************/
double DTWDistanceFun(double *A,int I,double *B,int J,int r);

/*****************************************************************************/
/* DTWTemplate，进行建立模板的工作
/* 其中A为已经建立好的模板，我们在以后加入训练样本的时候，
/* 以已建立好的模板作为第一个参数，I为模板的长度，在这个模板中不再改变
/* B为新加入的训练样本，J为B的长度，turn为训练的次数，在第一次
/* 用两个数组建立模板时，r为1，这是出于权值的考虑
/* temp保存匹配最新训练后的模板，建议temp[DTWMAXNUM]，函数返回最新训练后模板的长度
/* 如果函数返回-1，表明训练样本之间距离过大，需要重新选择训练样本，
/* tt为样本之间距离的阈值，自行定义
/*****************************************************************************/
int DTWTemplate(double *A,int I,double *B,int J,double *temp,int turn,double tt,double *rltdistance);
private:
	double distance[DTWMAXNUM][DTWMAXNUM]; /*保存距离*/
    double dtwpath[DTWMAXNUM][DTWMAXNUM]; /*保存路径*/
};
#endif