#ifndef __SEG_ACTION_H_
#define __SEG_ACTION_H_
#include <vector>
using namespace std;
class segAction {

public:
	segAction() {};
	~segAction() {};
	/*
	*���㲨�α仯�ļ��ٶ�
	*@���룺pdata ��������  iLen, ���ݳ���
	*@�����ppAcc ���εļ��ٶȣ� ά����pdata��ͬ
	*/
	int getAccelerate(float * pdata, int iLen, vector<float>& vAcc);
	/*
	*���ж������
	*@���룺pAcc ÿ����ļ��ٶ� iLen ���ٶȵĵ���
	*@�����vpSegPoss ��ֵĶ�������ʼ����λ�ã� iActionNum ��ֵĶ�������
	*/
	int doseg(float * pAcc, int iLen, vector<int>& vpSegPos, int& iActionNum);

	/*
	*����ʵʱ�������
	*@���룺pAcc ÿ����ļ��ٶ� iLen ���ٶȵĵ���
	*@�����vpSegPoss ��ֵĶ�������ʼ����λ�ã� iActionNum ��ֵĶ�������
	*/
	int doSegRealTime(float * pAcc, int iLen, int& iMark, int& iThresPoint, int & iStart, int& iEnd,int sensitive);

private:
};



#endif
