#ifndef _GAMMA_UTIL_H_
#define _GAMMA_UTIL_H_
#include <iostream>  
#include <fstream>  
#include <vector>
#include <opencv2\opencv.hpp>

using namespace cv;
using namespace std;

#define DTWMAXNUM 200
#define MAX(a,b) ((a)>(b)?(a):(b))
#define MIN(a,b) ((a)<(b)?(a):(b))
#define ABS(a) ((a)>0?(a):(-(a)))
#define DTWVERYBIG 100000000.0
struct SegInfo
{
	int iStart;
	int iEnd;
};


class GammaUtil
{
public:
	static int  readByLine(string& sFileName, vector<float>& vecSrc);
	static int  readByLine(string& sFileName, float ** pData);
	static void writeDataToFile(string& sFileName, Mat& imageMat, bool replace = true);
	static void  writeUcharDataToFile(string& sFileName, Mat& imageMat, bool replace = true);
	static void  writeVecPointYToFile(string& sFileName, vector<int>& vecPointY, bool replace = true);
	static void  writePointYToFile(string& sFileName, float *pfPointY, int iSize, bool replace = true);
	static void  writeVecPointYToFile(string& sFileName, vector<float>& vecPointY, bool replace = true);
	static void  lessThenSetValue(vector<float>& vData, float fData);
	static void  finishOneAction(vector<float>& vAcc, int iWatingTime, int& iType, int& iWord,  vector<float>& vfShift);
	static void  cleanData(int iType, SegInfo& stSegInfo, vector<float>& vfShift, vector<float>& vAcc);
	static void  writeLineToFile(std::ofstream& m_logfile, const string& str);
	static void  writeLineToFile(const string& sFileName, const string& str, bool replace = true);
	static void  finishOneActionV3(vector<float>& vAcc, int iWatingTime, int& iType, int& iWord, vector<float>& vfShift, int iMark);
	static void writeVecDoublePointYToFile(string& sFileName, vector<double>& vecPointY, bool replace = true);
};

#endif