// ButterworthFilter.cpp : �������̨Ӧ�ó������ڵ㡣
//
#include "ButterworthFilter.h"
#include<stdlib.h>
#include<stdio.h>

int  ButterworthFilter::readByLine(string& sFileName, vector<float>& vecSrc)
{
	ifstream infile;
	//string str = "D:/visual studio 2017/Projects/ButterworthFilter/ButterworthFilter/src.txt";
	infile.open(sFileName.c_str());
	if (!infile.is_open())
	{
		cout << "not open true" << endl;
		return -1;
	}

	string line;
	while (!infile.eof()) {
		std::getline(infile, line);
		float fTemp = atof(line.c_str());
		vecSrc.push_back(fTemp);
		//cout << line << endl;
	}
	infile.close();
	return vecSrc.size();
}


int  ButterworthFilter::readSignalData(string& sFileName, float ** pSignal)
{
	vector<float> vecSrc;
	readByLine(sFileName, vecSrc);

	float* src = new float[vecSrc.size()];
	for (int i = 0; i < vecSrc.size(); i++)
	{
		src[i] = vecSrc[i];
	}
	*pSignal = src;
	return vecSrc.size();
}
int ButterworthFilter::doButterFilter(float * pSignal, int iLen, vector<float>& vFilterData)
{

	int filterOrder = 8;
	//filterOrder = 12;
	double overallGain = 1.0;
	const double EPSILON = 1.0e-4;

	vector <Biquad> coeffs;  // second-order sections (sos)
	Butterworth butterworth;

	/*bool designedCorrectly = butterworth.bandPass(44100,  // fs
		500,    // freq1
		1000,   // freq2
		filterOrder,
		coeffs,
		overallGain);*/


	bool designedCorrectly = butterworth.bandPass(44100,  // fs
		19600,    // freq1
		20400,   // freq2
		filterOrder,
		coeffs,
		overallGain);

	/*vector<float>   src;
	src.reserve(10000);
	vector<float>   des;
	des.reserve(10000);*/	


	vector<float> des;	

	vector <Biquad>  coeffs6;
	Biquad stBiquad1;
	stBiquad1.b0 = 1;
	stBiquad1.b1 = -2;
	stBiquad1.b2 = 1;
	stBiquad1.a1 = -1.80493815664976;
	stBiquad1.a2 = -0.890527525995228;
	Biquad stBiquad2;
	stBiquad2.b0 = 1;
	stBiquad2.b1 = -2;
	stBiquad2.b2 = 1;
	stBiquad2.a1 = -1.83077555721034;
	stBiquad2.a2 = -0.900624907085692;
	Biquad stBiquad3;
	stBiquad3.b0 = 1;
	stBiquad3.b1 = -2;
	stBiquad3.b2 = 1;
	stBiquad3.a1 = -1.80915506065600;
	stBiquad3.a2 = -0.912436016715456;
	Biquad stBiquad4;
	stBiquad4.b0 = 1;
	stBiquad4.b1 = 2;
	stBiquad4.b2 = 1;
	stBiquad4.a1 = -1.87334725506703;
	stBiquad4.a2 = -0.932808794977009;
	Biquad stBiquad5;
	stBiquad5.b0 = 1;
	stBiquad5.b1 = 2;
	stBiquad5.b2 = 1;
	stBiquad5.a1 = -1.84867912466845;
	stBiquad5.a2 = -0.965744013182314;
	Biquad stBiquad6;
	stBiquad6.b0 = 1;
	stBiquad6.b1 = 2;
	stBiquad6.b2 = 1;
	stBiquad6.a1 = -1.92119448938752;
	stBiquad6.a2 = -0.976252895259380;
	Biquad stBiquad[6];
	stBiquad[0] = stBiquad1;
	stBiquad[1] = stBiquad2;
	stBiquad[2] = stBiquad3;
	stBiquad[3] = stBiquad4;
	stBiquad[4] = stBiquad5;
	stBiquad[5] = stBiquad6;
	BiquadChain stBiquadChain(6);
	stBiquadChain.resize(6);
	stBiquadChain.processBiquad(pSignal, des, 1, iLen, stBiquad);
	float g = 2.76653524249896e-08;

	for (size_t i =0; i< iLen; i++)
	{
		vFilterData.push_back( des[i]* g);
	}


	//delete pSignal;
	//delete des;
	
	/*
	string filename = "D:/visual studio 2017/Projects/ButterworthFilter/ButterworthFilter/butterresult1.txt";
	
	std::ofstream m_logfile;
	m_logfile.open(filename.c_str(), std::ios::app);
	for (int i =0; i< 69866; i++)
	{
		string msg = std::to_string(des[i]* g);
		m_logfile << msg << "\n" << std::flush;
	}
	m_logfile.close();
	*/


	
    return 0;
}

