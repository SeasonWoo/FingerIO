#ifndef _GAMMA_DTW_H_
#define _GAMMA_DTW_H_
struct pointOritation//�ڵ㷽����������ÿ��W��
{
	int frontI,frontJ;
};

class DTW
{
public:
	void gArray(int *p,int n,int m,int *g,struct pointOritation *pr);
	void printPath(struct pointOritation *po,int n,int m,int *g);
	
};

#endif
