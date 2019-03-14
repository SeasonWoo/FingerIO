/*dtwrecoge.cpp**************************************************************/

#include "dtwrecoge.h"
#include "GammaUtil.h"

#define REALMAX  17977000000
/*****************************************************************************/
/* DTWDistance������������֮���ƥ�����
/* A,B�ֱ�Ϊ��һ�ڶ������飬I��JΪ�����鳤�ȣ�rΪƥ�䴰�ڵĴ�С

/* r�Ĵ�Сһ��ȡΪ���鳤�ȵ�1/10��1/30
/* ������������֮���ƥ�����,������أ�1.0���������鳤��̫����
/*****************************************************************************/
double DtwRecoge::DTWDistanceFun(double *A,int I,double *B,int J,int r)
{
	int i,j;
	double dist;
	int istart,imax;
	int r2=r+ABS(I-J);/*ƥ�����*/
	double g1,g2,g3;
	int pathsig=1;/*·���ı�־*/

	/*����������Ч��*/
	if(I>DTWMAXNUM||J>DTWMAXNUM){
		//printf("Too big number\n");
		return -1.0;
	}
	
	/*����һЩ��Ҫ�ĳ�ʼ��*/
	for(i=0;i<I;i++){
		for(j=0;j<J;j++){
			dtwpath[i][j]=0;
			distance[i][j]=DTWVERYBIG;
		}
	}
	
	/*��̬�滮����С����*/
	/*�����Ҳ��õ�·���� -------
	                          . |
	                        .   |
	                      .     |
	                    .       |
	 */
	distance[0][0]=(double)2*ABS(A[0]-B[0]);
	for(i=1;i<=r2;i++){
		distance[i][0]=distance[i-1][0]+ABS(A[i]-B[0]);
	}
	for(j=1;j<=r2;j++){
		distance[0][j]=distance[0][j-1]+ABS(A[0]-B[j]);
	}
	
	for(j=1;j<J;j++){
		istart=j-r2;
		if(j<=r2)
			istart=1;
		imax=j+r2;
		if(imax>=I)
			imax=I-1;
		
		for(i=istart;i<=imax;i++){
			g1=distance[i-1][j]+ABS(A[i]-B[j]);
			g2=distance[i-1][j-1]+2*ABS(A[i]-B[j]);
			g3=distance[i][j-1]+ABS(A[i]-B[j]);
			g2=MIN(g1,g2);
			g3=MIN(g2,g3);
			distance[i][j]=g3;
		}
	}
		
	dist=distance[I-1][J-1]/((double)(I+J));
	return dist;
}/*end DTWDistance*/

/*****************************************************************************/
/* DTWTemplate�����н���ģ��Ĺ���
/* ����AΪ�Ѿ������õ�ģ�壬�������Ժ����ѵ��������ʱ��
/* ���ѽ����õ�ģ����Ϊ��һ��������IΪģ��ĳ��ȣ������ģ���в��ٸı�
/* BΪ�¼����ѵ��������JΪB�ĳ��ȣ�turnΪѵ���Ĵ������ڵ�һ��
/* ���������齨��ģ��ʱ��rΪ1�����ǳ���Ȩֵ�Ŀ���
/* temp����ƥ������ѵ�����ģ�壬����temp[DTWMAXNUM]��������������ѵ����ģ��ĳ���
/* �����������-1������ѵ������֮����������Ҫ����ѡ��ѵ��������
/* ttΪ����֮�����ķ�ֵ�����ж���
/* rltdistance������룬��һ���������齨��ģ��ʱ�������⸳��һ��ֵ��
/* ������ǰһ�η��ص�ֵ����ò���
/*****************************************************************************/
int DtwRecoge::DTWTemplate(double *A,int I,double *B,int J,double *temp,int turn,double tt,double *rltdistance)
{
	double dist;
	int i,j;
	int pathsig=1;
	dist=DTWDistanceFun(A,I,B,J,(int)(I/30));
	if(dist>tt){
		printf("\nSample doesn't match!\n");
		return -1;
	}
		
	if(turn==1)
		*rltdistance=dist;
	else{
		*rltdistance=((*rltdistance)*(turn-1)+dist)/turn;
	}
	/*Ѱ��·��,�����Ҳ���������������*/
	i=I-1;
	j=J-1;
	while(j>=1||i>=1){
		double m;
		if(i>0&&j>0){
			m=MIN(MIN(distance[i-1][j],distance[i-1][j-1]),distance[i][j-1]);
			if(m==distance[i-1][j]){
				dtwpath[i-1][j]=pathsig;
				i--;
			}
			else if(m==distance[i-1][j-1]){
				dtwpath[i-1][j-1]=pathsig;
				i--;
				j--;
			}
			else{
				dtwpath[i][j-1]=pathsig;
				j--;
			}
		}
		else if(i==0){
			dtwpath[0][j-1]=pathsig;
			j--;
		}
		else{/*j==0*/
			dtwpath[i-1][0]=pathsig;
			i--;
		}
	}
	dtwpath[0][0]=pathsig;
	dtwpath[I-1][J-1]=pathsig;
	
	/*����ģ��*/
	for(i=0;i<I;i++){
		double ftemp=0.0;
		int ntemp=0;
		for(j=0;j<J;j++){
			if(dtwpath[i][j]==pathsig){
				ftemp+=B[j];
				ntemp++;
			}
		}
		ftemp/=((double)ntemp);
		temp[i]=(A[i]*turn+ftemp)/((double)(turn+1));/*ע�������Ȩֵ*/
	}
	
	return I;/*����ģ��ĳ���*/
}//end DTWTemplate