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
/*****************************************************************************/
/* DTWDistance������������֮���ƥ�����
/* A,B�ֱ�Ϊ��һ�ڶ������飬I��JΪ�����鳤�ȣ�rΪƥ�䴰�ڵĴ�С
/* r�Ĵ�Сһ��ȡΪ���鳤�ȵ�1/10��1/30
/* ������������֮���ƥ�����,������أ�1.0���������鳤��̫����
/*****************************************************************************/
float DTWDistanceFun(float *A, int I, float *B, int J, int r);
//double DTWDistanceFun(double *A,int I,double *B,int J,int r);

/*****************************************************************************/
/* DTWTemplate�����н���ģ��Ĺ���
/* ����AΪ�Ѿ������õ�ģ�壬�������Ժ����ѵ��������ʱ��
/* ���ѽ����õ�ģ����Ϊ��һ��������IΪģ��ĳ��ȣ������ģ���в��ٸı�
/* BΪ�¼����ѵ��������JΪB�ĳ��ȣ�turnΪѵ���Ĵ������ڵ�һ��
/* ���������齨��ģ��ʱ��rΪ1�����ǳ���Ȩֵ�Ŀ���
/* temp����ƥ������ѵ�����ģ�壬����temp[DTWMAXNUM]��������������ѵ����ģ��ĳ���
/* �����������-1������ѵ������֮����������Ҫ����ѡ��ѵ��������
/* ttΪ����֮��������ֵ�����ж���
/*****************************************************************************/
//int DTWTemplate(double *A,int I,double *B,int J,double *temp,int turn,double tt,double *rltdistance);

private:
	//double distance[DTWMAXNUM][DTWMAXNUM]; /*�������*/
    //double dtwpath[DTWMAXNUM][DTWMAXNUM]; /*����·��*/
};
#endif