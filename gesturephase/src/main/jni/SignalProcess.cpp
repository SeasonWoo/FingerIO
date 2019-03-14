//
// Created by Administrator on 2017/9/1.
//
#include "SignalProcess.h"
#include "Spectrogram.h"
#include "ButterworthFilter.h"

#include "dtwrecogeopt.h"
#include "GammaUtil.h"
#include "bwareaopen.h"
#include <opencv2\opencv.hpp>
#include "interpolate.h"
#include <stdio.h>
#include <time.h>
#include "segAction.h"
using namespace std;
using namespace cv;
#define NUMSAMPLE 100

SignalProcess::SignalProcess()
{
	
}

SignalProcess::~SignalProcess()
{
	//m_logfile.close();
}

int  SignalProcess::init(string sTemplatePath)
{
	/*string logFile = sTemplatePath + "logfile.txt";
	m_logfile.open(sTemplatePath.c_str(), std::ios::out);*/
	iIndex = 0;
	f = 20000;
	fs = 44100;
	Tsolu = 1.0 / fs;
	iNfft = 8192;
	overlap = 8192 * 0.875;
	step = iNfft - overlap;
	bEraseNoise = false;

	noisenum = (int)(230 / (step*Tsolu));
	endmark = 1;
	segmark=0;
	return loadTemplate(sTemplatePath);
}

float  SignalProcess::doProcess(float *pSignal, int iLen)
{
    float  logpxx = 0;
    return logpxx;
}

int  SignalProcess::loadTemplate(string sPath)
{
	string sTemplatePath[6] = { sPath + "heng2.txt",sPath + "shu2.txt", sPath + "zuoxie2.txt", sPath + "youxie2.txt",sPath + "zuohu2.txt", sPath + "youhu2.txt" };
	int iLength = 0; 
	for (int i = 0; i < 6; i++)
	{
		vector<float> vTemplate;
		GammaUtil::readByLine(sTemplatePath[i], vTemplate);
		iLength += vTemplate.size();
		m_vvTemplateData.push_back(vTemplate);
	}	

	return iLength;
}


int SignalProcess::doDtw(vector<float> pStroke, int iSize, vector<float>& vDis, const string& sResultPath, const string&  fileName)
{
	int iType = 0;
	int iTemplateSize;
	for (size_t i = 0; i< 6; i++)
	{
	
		vector<float>& vTempData = m_vvTemplateData[i];
	    ostringstream ss;
        ss << i;
        string sIndex = ss.str();
	    string sResultFile = sResultPath + fileName + "vTempData_vs_" + sIndex + ".txt";
        //GammaUtil::writeVecPointYToFile(sResultFile, vTempData);

		iTemplateSize = vTempData.size();
		int iRun = iTemplateSize / 10;
		ostringstream ss1;
		ss1<< pStroke.size()<< "|" << iTemplateSize << "|" << iRun ;
		sResultFile = sResultPath + fileName + "vLine_vs_" + sIndex + ".txt";
		//GammaUtil::writeLineToFile(sResultFile, ss1.str());

		int iDis = stDtwRecoge.DTWDistanceFun(&pStroke[0], iSize, &vTempData[0], iTemplateSize, iRun);
		vDis.push_back(iDis);
	}

	float fMin = 0;
	for (size_t i = 0; i < vDis.size(); i++)
	{
		if (i == 0)
		{
			iType = 1;
			fMin = vDis[0];
		}
		else
		{
			if (fMin > vDis[i])
			{
				fMin = vDis[i];
				iType = i + 1;
			}
		}
	}

	return iType;
}

int SignalProcess::doProcessV2(float *pSignal, int iLen, float *costArr, int length, string sResultPath, string fileName)
{
    return -1;
}

int SignalProcess::doProcessV3(short *pSignal, int iLen,  string sResultPath, string fileName , int sensitive)
{
		//累积数据
	    Spectrogram stSpectrogram;
        ButterworthFilter stButterworthFilter;

        int iSignalLen = iLen;//stButterworthFilter.readSignalData(sSignalFile,&pSignal);
		//定义变量
		iDataNum = iSignalLen - noisenum;
        //stButterworthFilter.doButterFilter(pSignal, iSignalLen, vFilterSignal);
		for (int i = 0; i < iLen; i++)
		{
			vFilterSignal.push_back(pSignal[i]);
		}

		int iFrame = 5;
		int iPool = step *(iFrame - 1) + iNfft;
		int iMark = 0;
		int iStart = 1;
		int iWatingTime = 100;
		int count = 0;
		if(iIndex == 0 && (vFilterSignal.size() > noisenum) && !bEraseNoise)
		{
			vFilterSignal.erase(vFilterSignal.begin(), vFilterSignal.begin() + noisenum);
			bEraseNoise = true;
		}

		int iFrameSignalLen = iPool + 1;
		if (vFilterSignal.size() < (iFrameSignalLen))
		{
		    //没有累积数据直接返回
			return -1;
		}
		else
		{
			int iType = 0;
			iIndex++;
			//取iPool长的数据， 数据每次会变化，从index开始取
			float * pFrameSignal = &vFilterSignal[0];

			ostringstream ss;
			ss << iIndex;
			string sIndex = ss.str();
			string sResultFile = sResultPath + fileName + "vFilterSignal_vs_" + sIndex + ".txt";
			//GammaUtil::writeVecPointYToFile(sResultFile, vFilterSignal);

			// 3.STFT spectrogram
			float **logPx;
			int iDstRow = 0;
			int iDstColumn = 0;

             if (segmark==1)
                       { start=clock();}

			stSpectrogram.spectrogram(pFrameSignal, iFrameSignalLen, iNfft, overlap, iNfft, fs, &logPx, iDstRow, iDstColumn);
			//sResultFile = sResultPath + fileName + "stSpectrogram_vs.txt";

			//结果是59*4097
			const int centreFreq = 20000;
			const int nfft = 8192;
			const int Fs = 44100;
			float fsolu = (float)Fs / (float)nfft;
			int  centreBin = (centreFreq*nfft / Fs);

			//处理20 KHZ的数据
			//构造opencv矩阵 161*59
			iDstColumn = 101;
			Mat medianfilterSrcM(iDstColumn, iDstRow, CV_32F);
			initFloatMat(medianfilterSrcM, logPx, centreBin - 55);
			sResultFile = sResultPath + fileName + "renoisespectrum_vs_" + sIndex + ".txt";
			//GammaUtil::writeDataToFile(sResultFile, medianfilterSrcM);

			//openCv中值滤波 twice
			//Mat  medianfilterDesM(4097, 59, CV_32F); twice
			Mat medianfilterDesM(iDstColumn, iDstRow, CV_32F);
			medianBlur(medianfilterSrcM, medianfilterDesM, 3);
			sResultFile = sResultPath + fileName + "medianfilterDesM_" + sIndex + ".txt";
			//GammaUtil::writeDataToFile(sResultFile, medianfilterDesM);
			Mat medianfilterDesM2(iDstColumn, iDstRow, CV_32F);
			medianBlur(medianfilterDesM, medianfilterDesM2, 3);

			//构造repmat
			//Mat  repmatM(4097, 59, CV_32F);
			Mat repmatM(iDstColumn, iDstRow, CV_32F);
			if (endmark == 1)
			{
				constructAvgCol(medianfilterDesM2, m_VecAvgCol, 5);
			}

			constructRepmat(repmatM, m_VecAvgCol);
			sResultFile = sResultPath + fileName + "repmatM_vs_" + sIndex + ".txt";
			endmark = 0;
			//GammaUtil::writeDataToFile(sResultFile, repmatM);

			//构造图像相减图像
			//Mat  subdiffM(4097, 59, CV_32F);
			Mat  subdiffM(iDstColumn, iDstRow, CV_32F);
			subtract(medianfilterDesM2, repmatM, subdiffM);
			sResultFile = "./data/out/subdiffM_vs.txt";
			sResultFile = sResultPath + fileName + "subdiffM_vs_" + sIndex + ".txt";
			//GammaUtil::writeDataToFile(sResultFile, subdiffM);

			//矩阵里面负数赋值为0
			negativeToZero(subdiffM);
			sResultFile = sResultPath + fileName + "subdiffNegativeToZeroM_vs_" + sIndex + ".txt";
			//GammaUtil::writeDataToFile(sResultFile, subdiffM);

			//最后5列赋值为0
			//tailNoise(subdiffM, 5);
			sResultFile = sResultPath + fileName + "tailNoise_vs.txt";
			//GammaUtil::writeDataToFile(sResultFile, subdiffM);

			// PP(PP<2)=0;
			lessToZero(subdiffM, 8);

			//高斯滤波
			//Mat  gaussianM(4097, 59, CV_32F);
			Mat  gaussianM(iDstColumn, iDstRow, CV_32F);
			GaussianBlur(subdiffM, gaussianM, cv::Size(5, 5), 1, 1, BORDER_REPLICATE);
			sResultFile = sResultPath + fileName + "gaussianM_vs_" + sIndex + ".txt";
			//GammaUtil::writeDataToFile(sResultFile, gaussianM);

			//归一化
			nomalLizeFilter(gaussianM);
			sResultFile = sResultPath + fileName + "nomalLizeFilter_vs_" + sIndex + ".txt";
			//GammaUtil::writeDataToFile(sResultFile, gaussianM);

			//二值化
			//Mat  binMFloat(4097, 59, CV_32F);
			Mat  binMFloat(iDstColumn, iDstRow, CV_32F);
			threshold(gaussianM, binMFloat, 0.15, 255, CV_THRESH_BINARY);
			sResultFile = sResultPath + fileName + "binMFloat_vs_" + sIndex + ".txt";
			//GammaUtil::writeDataToFile(sResultFile, binMFloat);

			//矩阵类型转换
			Mat  binMUchar(iDstColumn, iDstRow, CV_8U);
			tranFloatMatTounCharMat(binMFloat, binMUchar);
			//sResultFile = sResultPath + fileName + "binMUchar_vs_" + sIndex + ".txt";
			//GammaUtil::writeUcharDataToFile(sResultFile, binMUchar);

			//填充轮廓以及去掉小轮廓
			//bwareImfill(binMUchar, 9);
			//imshow("binMIntestIng", binMIntestIng);
			//sResultFile = sResultPath + fileName + "bwareImfill_vs_" + sIndex + ".txt";
			//GammaUtil::writeUcharDataToFile(sResultFile, binMUchar);

           if (segmark==1)
           {
           endSignal=clock();
           time[0]+=(double)(endSignal-start)*1000/CLOCKS_PER_SEC;
           }



			//获取多普勒曲线
			vector<int> vecPointY;
			dopshift(binMUchar, vecPointY, centreBin);
			//sResultFile = sResultPath + fileName + "dopshift_vs_" + sIndex + ".txt";
			//GammaUtil::writeVecPointYToFile(sResultFile, vecPointY);

			//转换成HZ
			vector<float> vecHz;
			pointTransToHz(vecPointY, fsolu, vecHz);
			//sResultFile = sResultPath + fileName + "InterpolatePre_vs_" + sIndex + ".txt";
			//GammaUtil::writeVecPointYToFile(sResultFile, vecHz);
			iSignalLen = vecHz.size();
			//平滑smoothing
			//DopShiftv1=modMovingMean(DopShift);
			//中间值的处理 & remove the last frame
			GammaUtil::lessThenSetValue(vecHz, f);
			for (size_t i = 0; i < vecHz.size(); i++)
			{
				vfShift.push_back(vecHz[i]);
			}
			//sResultFile = sResultPath + fileName + "vfShiftpre_vs" + ".txt";
			//GammaUtil::writeVecPointYToFile(sResultFile, vfShift, false);

			//动作分割
			segAction stSegAction;
			vector<float> vAcc;
			int iRet = stSegAction.getAccelerate(&vfShift[0], vfShift.size(), vAcc);

			//vector<int> vpSegPos;
			int iStart = 0;
			int iMark = 0;
			int iActionNum = 0;
			int iThresPoint = 0;
			int iEnd = 0;
			SegInfo stSegInfo;		
			iRet = stSegAction.doSegRealTime(&vAcc[0], vAcc.size(), iMark, iThresPoint, iStart, iEnd,sensitive);
			stSegInfo.iStart = iStart;
			stSegInfo.iEnd = iEnd;

            if(segmark==1)
                        {
                        endDoppler=clock();
                        time[1]+=(double)(endDoppler-endSignal)*1000/CLOCKS_PER_SEC;}
                if (stSegInfo.iStart >0)
                {segmark=1;}

            if (stSegInfo.iEnd >0)
            { vt.push_back(time[0]);
            vt.push_back(time[1]);
            segmark=0;}





			sResultFile = sResultPath + fileName + "Seg" + sIndex + ".txt";
			//GammaUtil::writeVecPointYToFile(sResultFile, vSegX);
			//vpSegPos.clear();

			//匹配
			if (stSegInfo.iStart > 0 && stSegInfo.iEnd > 0 && stSegInfo.iEnd > stSegInfo.iStart)
			{
				//插值

				Interpolate stInterpolate;
				vector<float> vStroke;
				sResultFile = sResultPath + fileName + "_vfShift_vs_" + sIndex + ".txt";
				//GammaUtil::writeVecPointYToFile(sResultFile, vfShift);

				//sResultFile = sResultPath  + "jni_vSegX_vs_.txt";
				//ostringstream ss;
				//ss << stSegInfo.iStart << "|" << stSegInfo.iEnd << endl;
				//GammaUtil::writeLineToFile(sResultFile, ss.str(), false);

				for (int j = stSegInfo.iStart; j <= stSegInfo.iEnd; j++)
				{
					vStroke.push_back(vfShift[j]);
				}

				vector<float> vdstInstroke;
				stInterpolate.doInterpolate(vStroke, vStroke.size(), NUMSAMPLE, vdstInstroke);
				string sResultFile = sResultPath + fileName + "Interpolate_vs_" + sIndex + ".txt";
				//GammaUtil::writePointYToFile(sResultFile, &vdstInstroke[0], NUMSAMPLE);

				//dtw计算 动作类型
				vector<float> vDis;
	           startMatch=clock();
				iType = doDtw(vdstInstroke, NUMSAMPLE, vDis, sResultPath, fileName);
				//ostringstream ssType;
				//ssType<< iType << endl;
				//sResultFile = sResultPath  + "jni_Type_vs_.txt";
				//GammaUtil::writeLineToFile(sResultFile, ssType.str(), false);
				//sResultFile = sResultPath + fileName + "dtw_vs.txt";
				//GammaUtil::writeVecPointYToFile(sResultFile, vDis, false);
                endMatch=clock();
                time[2]=(double)(endMatch-startMatch)*1000/CLOCKS_PER_SEC;

                vt.push_back(time[2]);
                time[3]=time[0]+time[1]+time[2];
                time[4]=(double)iType;
                vt.push_back(time[3]);
                vt.push_back(time[4]);

               sResultFile = sResultPath + "time"+ ".txt";

               //GammaUtil::writeVecDoublePointYToFile(sResultFile,vt,false);
                vt.clear();
                time[0]=0;
                time[1]=0;



               // memset(time, 0, sizeof(time));
				//清理数据
				GammaUtil::cleanData(iType, stSegInfo, vfShift, vAcc);
				endmark = 1;
			}

			//if user finish the word writing?
			GammaUtil::finishOneActionV3(vAcc, iWatingTime, iType, iWord, vfShift, iMark);
			vFilterSignal.erase(vFilterSignal.begin(), vFilterSignal.begin() + iPool - overlap + 1);

			return iType;
		}
}

int SignalProcess::costTime(struct timeval& stbegin, struct timeval& stEnd)
{
     int timeuse = 1000000 * (stEnd.tv_sec - stbegin.tv_sec ) + stEnd.tv_usec - stbegin.tv_usec;
     return timeuse;
}