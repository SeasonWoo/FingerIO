#ifndef _BUTTERWORTHFILTER_H_
#define _BUTTERWORTHFILTER_H_
#include "Butterworth.h"
#include "Biquad.h"
#include <iostream>  
#include <fstream>  
using namespace std;
class ButterworthFilter
{
public:
	ButterworthFilter(){};
	~ButterworthFilter(){};
	int readByLine(string& sFileName, vector<float>& vecSrc);
	int doButterFilter(float * pSignal, int iLen, vector<float>& ppFilterData);
	int readSignalData(string& sFileName, float ** pSignal);
};
#endif