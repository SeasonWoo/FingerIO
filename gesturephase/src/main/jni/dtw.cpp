//// main.c// DTW//// Created by ������ on 16/12/22.// Copyright (c) 2016�� ������. All rights reserved.//

#include <stdio.h>
#include "dtw.h"

void DTW::gArray(int *p,int n,int m,int *g,struct pointOritation *pr)
{
	*(g+(n-1)*m)=(*(p+(n-1)*m))*2;//��ʼ�㣨�����½ǵĵ㣩

	for (int i=1; i<m; i++)//������һ��
	{
	*(g+(n-1)*m+i)=*(g+(n-1)*m+i-1)+*(p+(n-1)*m+i);
	(pr+(n-1)*m+i)->frontI=n-1;
	(pr+(n-1)*m+i)->frontJ=i-1;
	}

	for (int i=n-2; i>=0; i--)//����ߵ�һ��
	{
		*(g+i*m+0)=*(g+(i+1)*m+0)+*(p+i*m+0);
		(pr+i*m+0)->frontI=i+1;
		(pr+i*m+0)->frontJ=0;
	}
	//����ʣ�������Gֵ
	for (int i=n-2; i>=0; i--)
	{
		for (int j=1; j<m; j++)
		{
			int left,under,incline;
			left=*(g+i*m+j-1)+*(p+i*m+j);
			under=*(g+(i+1)*m+j)+*(p+i*m+j);
			incline=*(g+(i+1)*m+j-1)+(*(p+i*m+j))*2;

			//�����¡�б��������ѡ����С��
			int min=left;
			*(g+i*m+j)=min;
			(pr+i*m+j)->frontI=i;
			(pr+i*m+j)->frontJ=j-1;

			if (min>under)
			{
				min=under;
				*(g+i*m+j)=min;
				(pr+i*m+j)->frontI=i+1;
				(pr+i*m+j)->frontJ=j;
			}
			if (min>incline)
			{
				min=incline;
				*(g+i*m+j)=min;
				(pr+i*m+j)->frontI=i+1;
				(pr+i*m+j)->frontJ=j-1;
			}
		}
	}
	//���G����
	for (int i=0; i<n; i++)
	{
		for (int j=0; j<m; j++)
		{
		printf("%d\t",*(g+i*m+j));
		}printf("\n");
		}
		//�����������
		for (int i=0; i<n; i++)
		{
		for (int j=0; j<m; j++)
		{
		printf("(%d,%d)\t",(pr+i*m+j)->frontI,(pr+i*m+j)->frontJ);
		}printf("\n");
	}
}


void DTW::printPath(struct pointOritation *po,int n,int m,int *g)
{
	//�����һ������ǰ���·���ڵ�
	int i=0,j=m-1;
	while (1)
	{
		int ii=(po+i*m+j)->frontI,jj=(po+i*m+j)->frontJ;
		if(i==0 && j==0)
			break;
		printf("(%d,%d):%d\n",i,j,*(g+i*m+j));
		i=ii;
		j=jj;
	}
}

/*
int main()
{
	int d[6][4]={
	{2,1,7,5},
	{1,5,1,6},
	{4,7,2,4},
	{5,2,4,3},
	{3,4,8,2},
	{2,1,5,1},
    };
int g[6][4];
struct pointOritation pOritation[6][4];//�������
gArray(*d,6,4,*g,*pOritation);
printPath(*pOritation, 6, 4,*g);
}
*/