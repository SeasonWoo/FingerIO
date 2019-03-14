//// main.c// DTW//// Created by 许湘扬 on 16/12/22.// Copyright (c) 2016年 许湘扬. All rights reserved.//

#include <stdio.h>
#include "dtw.h"

void DTW::gArray(int *p,int n,int m,int *g,struct pointOritation *pr)
{
	*(g+(n-1)*m)=(*(p+(n-1)*m))*2;//起始点（最左下角的点）

	for (int i=1; i<m; i++)//最下面一横
	{
	*(g+(n-1)*m+i)=*(g+(n-1)*m+i-1)+*(p+(n-1)*m+i);
	(pr+(n-1)*m+i)->frontI=n-1;
	(pr+(n-1)*m+i)->frontJ=i-1;
	}

	for (int i=n-2; i>=0; i--)//最左边的一竖
	{
		*(g+i*m+0)=*(g+(i+1)*m+0)+*(p+i*m+0);
		(pr+i*m+0)->frontI=i+1;
		(pr+i*m+0)->frontJ=0;
	}
	//计算剩余网格的G值
	for (int i=n-2; i>=0; i--)
	{
		for (int j=1; j<m; j++)
		{
			int left,under,incline;
			left=*(g+i*m+j-1)+*(p+i*m+j);
			under=*(g+(i+1)*m+j)+*(p+i*m+j);
			incline=*(g+(i+1)*m+j-1)+(*(p+i*m+j))*2;

			//从左、下、斜三个方向选出最小的
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
	//输出G数组
	for (int i=0; i<n; i++)
	{
		for (int j=0; j<m; j++)
		{
		printf("%d\t",*(g+i*m+j));
		}printf("\n");
		}
		//输出方向数组
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
	//从最后一个点向前输出路径节点
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
struct pointOritation pOritation[6][4];//用来存放
gArray(*d,6,4,*g,*pOritation);
printPath(*pOritation, 6, 4,*g);
}
*/