//
// Created by Administrator on 2017/9/1.
//

#ifndef ANDROID_PHASE_PLAY_STATIC_SIGNALPROCESS_H
#define ANDROID_PHASE_PLAY_STATIC_SIGNALPROCESS_H
#include <time.h>  
#include <string>
#include <vector>
#include <opencv2\opencv.hpp>
#include "dtwrecogeopt.h"
using namespace std;
using namespace cv;
class SignalProcess
{
public:
    SignalProcess();
    ~SignalProcess();
	int init(string sTemplatePath);
    float doProcess(float *pSignal, int iLen);
    int  doProcessV2(float *pSignal, int iLen, float *costArr, int length, string sResultPath, string fileName);
    int costTime(struct timeval& stbegin, struct timeval& stend);
	int  doDtw(vector<float> pStroke, int iSize, vector<float>& vDis, const string& sResultPath, const string&  fileName);
	int  getNoiseNum() {	return noisenum;}
	int  doProcessV3(short *pSignal, int iLen,  string sResultPath, string fileName,int sensitive);
private:	
	int  loadTemplate(string sPath);
	
private:
	//¶¨Òå±äÁ¿
	vector<float> vfShift;
	vector<int> vType;
	int iWord;
	//vector<int> vSegX;
	
	vector<vector<float> > m_vvTemplateData;//Ä£°åÎÄ¼þ
	int           iIndex;
	vector<float> vFilterSignal; //ÐèÒªÀÛ»ýÊý¾Ý
	//std::ofstream m_logfile;
	int noisenum ;
	float * pSignalNoNoise;
	int iDataNum;
	DtwRecogeOpt stDtwRecoge;
	float f;
	int fs;
	float Tsolu;
	int iNfft;
	int overlap ;
	float step;
	bool bEraseNoise;
	//Mat  m_repmatM;
	vector<float> m_VecAvgCol;      //¹¹ÔìÇ°5ÁÐ¾ùÖµ£¬ÓÃÀ´×ö±³¾°²î
	int  endmark;
    int  segmark;

            double time[5];
            vector<double> vt;
            clock_t start,endSignal,endDoppler,startMatch,endMatch,Total;
};
#endif //ANDROID_PHASE_PLAY_STATIC_SIGNALPROCESS_H
