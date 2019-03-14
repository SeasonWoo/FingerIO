//
//  RangeFinder.cpp
//  llap
//
//  Created by Wei Wang on 2/19/16.
//  Copyright © 2016 Nanjing University. All rights reserved.
//

#include "MatrixProcess.h"
#include "RangeFinder.h"
#include <cstdlib>
#include <cmath>
//#include <cstring>


RangeFinder::RangeFinder( UInt32 inMaxFramesPerSlice , UInt32 inNumFreq, float inStartFreq, float inFreqInterv )
{
    //Number of frequency
    mNumFreqs = inNumFreq;
    //Buffer size
    mBufferSize = inMaxFramesPerSlice;
    //Frequency interval
    mFreqInterv = inFreqInterv;
    //Receive data size
    mRecDataSize = 4*inMaxFramesPerSlice;
    //Sound speed
    mSoundSpeed = 331.3 + 0.606 * TEMPERATURE;
    //Init buffer
    for(UInt32 i=0; i<MAX_NUM_FREQS; i++){
        mSinBuffer[i]=(float*) calloc(2*inMaxFramesPerSlice, sizeof(float));
        mCosBuffer[i]=(float*) calloc(2*inMaxFramesPerSlice, sizeof(float));
        
        mFreqs[i]=inStartFreq+i*inFreqInterv;
        
        mWaveLength[i]=mSoundSpeed/mFreqs[i]*1000; //all distance is in mm
        
        mBaseBandReal[i]=(float*) calloc(mRecDataSize/CIC_DEC, sizeof(float));
        mBaseBandImage[i]=(float*) calloc(mRecDataSize/CIC_DEC, sizeof(float));
        mBaseBand[i]=(float*) calloc(2 * mRecDataSize/CIC_DEC, sizeof(float));

        for(UInt32 k=0;k<CIC_SEC;k++)
        {
            mCICBuffer[i][k][0]=(float*) calloc(mRecDataSize/CIC_DEC+CIC_DELAY, sizeof(float));
            mCICBuffer[i][k][1]=(float*) calloc(mRecDataSize/CIC_DEC+CIC_DELAY, sizeof(float));
        }
    }
    
    mPlayBuffer = (int16_t*) calloc(2*inMaxFramesPerSlice, sizeof(int16_t));
    
    mRecDataBuffer = (int16_t*) calloc(mRecDataSize, sizeof(int16_t));
    mFRecDataBuffer = (float*) calloc(mRecDataSize, sizeof(float));
    mTempBuffer = (float*) calloc(mRecDataSize, sizeof(float));
    mCurPlayPos = 0;
    mCurRecPos = 0;
    mCurProcPos= 0;
    mLastCICPos =0;
    mDecsize=0;
    mSocBufPos=0;

    InitBuffer();
}

void RangeFinder::InitBuffer()
{
    for(UInt32 i=0; i<mNumFreqs; i++){
        for(UInt32 n=0;n<mBufferSize*2;n++){
            mCosBuffer[i][n]=cos(2*PI*n/AUDIO_SAMPLE_RATE*mFreqs[i]);
            mSinBuffer[i][n]=-sin(2*PI*n/AUDIO_SAMPLE_RATE*mFreqs[i]);
        }
        mDCValue[0][i]=0;
        mMaxValue[0][i]=0;
        mMinValue[0][i]=0;
        mDCValue[1][i]=0;
        mMaxValue[1][i]=0;
        mMinValue[1][i]=0;
    }
    
    float mTempSample;
    for(UInt32 n=0;n<mBufferSize*2;n++){
        mTempSample=0;
        for(UInt32 i=0; i<mNumFreqs; i++){
            mTempSample+=mCosBuffer[i][n]*VOLUME;
        }
        mPlayBuffer[n]=(int16_t) (mTempSample/mNumFreqs*32767);
    }
    
}

RangeFinder::~RangeFinder()
{
    for (UInt32 i=0;i<mNumFreqs; i++)
    {
        if(mSinBuffer[i]!=NULL)
        {
            free(mSinBuffer[i]);
            mSinBuffer[i]=NULL;
        }
        if(mCosBuffer[i]!=NULL)
        {
            free(mCosBuffer[i]);
            mCosBuffer[i]=NULL;
        }
        if(mBaseBandReal[i]!=NULL)
        {
            free(mBaseBandReal[i]);
            mBaseBandReal[i]=NULL;
        }

        if(mBaseBand[i]!=NULL)
        {
            free(mBaseBand[i]);
            mBaseBand[i]=NULL;
        }



        if(mBaseBandImage[i]!=NULL)
        {
            free(mBaseBandImage[i]);
            mBaseBandImage[i]=NULL;
        }
        for(UInt32 k=0;k<CIC_SEC;k++)
        {
            if(mCICBuffer[i][k][0]!=NULL)
            {
                free(mCICBuffer[i][k][0]);
                mCICBuffer[i][k][0]=NULL;
            }
            if(mCICBuffer[i][k][1]!=NULL)
            {
                free(mCICBuffer[i][k][1]);
                mCICBuffer[i][k][1]=NULL;
            }
        }
    }
    if(mPlayBuffer!=NULL)
    {
        free(mPlayBuffer);
        mPlayBuffer= NULL;
    }
    if(mTempBuffer!=NULL)
    {
        free(mTempBuffer);
        mTempBuffer= NULL;
    }
    
    if(mRecDataBuffer!=NULL)
    {
        free(mRecDataBuffer);
        mRecDataBuffer= NULL;
    }
    if(mFRecDataBuffer!=NULL)
    {
        free(mFRecDataBuffer);
        mFRecDataBuffer= NULL;
    }
}

int16_t* RangeFinder::GetPlayBuffer(UInt32 inSamples)
{
    int16_t* playDataPointer = mPlayBuffer + mCurPlayPos;
    
    mCurPlayPos += inSamples;
    
    if(mCurPlayPos >=mBufferSize)
        mCurPlayPos =mCurPlayPos -mBufferSize;
    
    return playDataPointer;
}

void RangeFinder::AdvanceSocketBuffer(long length)
{
    if(length>0)
    {
        if(length>=mSocBufPos)
        {
            mSocBufPos=0;
            return;
        }
        else
        {   mSocBufPos= mSocBufPos-(UInt32) length;
            memmove(mSocketBuffer,mSocketBuffer+length,mSocBufPos);
            return;
        }
    }
    
}


int16_t* RangeFinder::GetRecDataBuffer(UInt32 inSamples)
{
    int16_t* RecDataPointer = mRecDataBuffer + mCurRecPos;
    
    mCurRecPos += inSamples;
    
    if(mCurRecPos >= mRecDataSize) //over flowed RecBuffer
    {
        mCurRecPos=0;
        RecDataPointer = mRecDataBuffer;
    }
    
    return RecDataPointer;
}

void RangeFinder::GetRecDataBuffer(short *recordData, int inSamples)
{
    int iOldPos = mCurRecPos;
    int iTmpPos =  mCurRecPos + inSamples;
    if(iTmpPos >= mRecDataSize) //over flowed RecBuffer
    {
        mCurRecPos=0;
        iOldPos = 0;
    }
    else
    {
        mCurRecPos += inSamples;
    }
    memmove(mRecDataBuffer + iOldPos * sizeof(short), recordData, inSamples * sizeof(short));
    //Log.i("phase", "rec:" + mRecDataBuffer[iOldPos+1] + "|" + mRecDataBuffer[iOldPos+2] + "|" +  mRecDataBuffer[iOldPos+3] + "|" +  mRecDataBuffer[iOldPos+4]);
}


float  RangeFinder::GetDistanceChange(short *datas, int inSamples)
{

    float distancechange=0;

    GetRecDataBuffer(datas, inSamples);

    //each time we process the data in the RecDataBuffer and clear the mCurRecPos
    //Get base band signal
    GetBaseBand();

    //Remove dcvalue from the baseband signal
    RemoveDC();

    //Send baseband singal via socket
    SendSocketData();

    //Calculate distance from the phase change
    distancechange=CalculateDistance();

    return distancechange;
}


float RangeFinder::GetDistanceChange(void)
{
    float distancechange=0;
    
    //each time we process the data in the RecDataBuffer and clear the mCurRecPos
    
    //Get base band signal
    GetBaseBand();
    
    //Remove dcvalue from the baseband signal
    RemoveDC();
    
    //Send baseband singal via socket
    SendSocketData();
    
    //Calculate distance from the phase change
    distancechange=CalculateDistance();
    
    return distancechange;
}

float RangeFinder::CalculateDistance()
{
	float distance=0;
    //DSPSplitComplex tempcomplex;
	float tempdata[4096],tempdata2[4096],tempdata3[4096],temp_val;
	float phasedata[MAX_NUM_FREQS][4096];
    int     ignorefreq[MAX_NUM_FREQS];
    
    
    if(mDecsize>4096)
        return 0;
    
    for(int f=0;f<mNumFreqs;f++)
    {
        ignorefreq[f]=0;
        //get complex number
        //tempcomplex.realp=mBaseBandReal[f];
        //tempcomplex.imagp=mBaseBandImage[f];
        
        //get magnitude
        vDSP_zvmags(mBaseBandReal[f], mBaseBandImage[f], tempdata, mDecsize);
		temp_val = vDSP_sve(tempdata, mDecsize);
        if(temp_val/mDecsize>POWER_THR) //only calculate the high power vectors
        {
            vDSP_zvphas(mBaseBandReal[f], mBaseBandImage[f], phasedata[f], mDecsize);
            //phase unwarp
            for(int i=1;i<mDecsize;i++)
            {
                while(phasedata[f][i]-phasedata[f][i-1]>PI)
                    phasedata[f][i]=phasedata[f][i]-2*PI;
                while(phasedata[f][i]-phasedata[f][i-1]<-PI)
                    phasedata[f][i]=phasedata[f][i]+2*PI;
            }
            if(fabs(phasedata[f][mDecsize-1]-phasedata[f][0])>PI/4)
            {
                for(int i=0;i<=1;i++)
                    mDCValue[i][f]=(1-DC_TREND*2)*mDCValue[i][f]+
                    (mMinValue[i][f]+mMinValue[i][f])/2*DC_TREND*2;
            }
            
            //prepare linear regression
            //remove start phase
            temp_val=-phasedata[f][0];
            vDSP_vsadd(phasedata[f],temp_val,tempdata,mDecsize);
            //divide the constants
            temp_val=2*PI/mWaveLength[f];
            vDSP_vsdiv(tempdata,temp_val,phasedata[f],mDecsize);
        }
        else //ignore the low power vectors
        {
            ignorefreq[f]=1;
        }
        
    }
    
    //linear regression
    for(int i=0;i<mDecsize;i++)
        tempdata2[i]=i;
    float sumxy=0;
    float sumy=0;
    int     numfreqused=0;
    for(int f=0;f<mNumFreqs;f++)
    {
        if(ignorefreq[f])
        {
            continue;
        }
        
        numfreqused++;
        
        vDSP_vmul(phasedata[f],tempdata2,tempdata,mDecsize);
		temp_val=vDSP_sve(tempdata,mDecsize);
        sumxy+=temp_val;
		temp_val=vDSP_sve(phasedata[f], mDecsize);
        sumy+=temp_val;
        
    }
    if(numfreqused==0)
    {
        distance=0;
        return distance;
    }
    
    float deltax=mNumFreqs*((mDecsize-1)*mDecsize*(2*mDecsize-1)/6-(mDecsize-1)*mDecsize*(mDecsize-1)/4);
    float delta=(sumxy-sumy*(mDecsize-1)/2.0)/deltax*mNumFreqs/numfreqused;
    
    float varsum=0;
    float var_val[MAX_NUM_FREQS];
    for(int i=0;i<mDecsize;i++)
        tempdata2[i]=i*delta;
    
    //get variance of each freq;
    for(int f=0;f<mNumFreqs;f++)
    {   
		var_val[f]=0;
        if(ignorefreq[f])
        {
            continue;
        }
        vDSP_vsub(tempdata2,phasedata[f],tempdata,mDecsize);
        vDSP_vsq(tempdata,tempdata3,mDecsize);
		var_val[f]=vDSP_sve(tempdata3,mDecsize);
        varsum+=var_val[f];
    }
    varsum=varsum/numfreqused;
    for(int f=0;f<mNumFreqs;f++)
    {
        if(ignorefreq[f])
        {
            continue;
        }
        if(var_val[f]>varsum)
            ignorefreq[f]=1;
    }
    
    //linear regression
    for(int i=0;i<mDecsize;i++)
        tempdata2[i]=i;
    
    sumxy=0;
    sumy=0;
    numfreqused=0;
    for(int f=0;f<mNumFreqs;f++)
    {
        if(ignorefreq[f])
        {
            continue;
        }
        
        numfreqused++;
        
        vDSP_vmul(phasedata[f],tempdata2,tempdata,mDecsize);
		temp_val = vDSP_sve(tempdata,mDecsize);
        sumxy+=temp_val;
		temp_val = vDSP_sve(phasedata[f],mDecsize);
        sumy+=temp_val;
        
    }
    if(numfreqused==0)
    {
        distance=0;
        return distance;
    }
    
    delta=(sumxy-sumy*(mDecsize-1)/2.0)/deltax*mNumFreqs/numfreqused;
    
    distance=-delta*mDecsize/2;
    return distance;
}

void RangeFinder::RemoveDC(void)
{
    int f,i;
    float tempdata[4096],tempdata2[4096],temp_val;
    float vsum,dsum,max_valr,min_valr,max_vali,min_vali;
    if(mDecsize>4096)
        return;
    
    //'Levd' algorithm to calculate the DC value;
    for(f=0;f<mNumFreqs;f++)
    {
        vsum=0;
        dsum=0;
        //real part
		max_valr = vDSP_maxv(mBaseBandReal[f],mDecsize);
		min_valr = vDSP_minv(mBaseBandReal[f],mDecsize);
        //getvariance,first remove the first value
        temp_val=-mBaseBandReal[f][0];
        vDSP_vsadd(mBaseBandReal[f],temp_val,tempdata,mDecsize);
		temp_val = vDSP_sve(tempdata,mDecsize);
        dsum=dsum+fabs(temp_val)/mDecsize;
        vDSP_vsq(tempdata,tempdata2,mDecsize);
		temp_val = vDSP_sve(tempdata2,mDecsize);
        vsum=vsum+fabs(temp_val)/mDecsize;
        
        //imag part
		max_vali = vDSP_maxv(mBaseBandImage[f],mDecsize);
		min_vali = vDSP_minv(mBaseBandImage[f],mDecsize);
        //getvariance,first remove the first value
        temp_val=-mBaseBandImage[f][0];
        vDSP_vsadd(mBaseBandImage[f],temp_val,tempdata,mDecsize);
		temp_val = vDSP_sve(tempdata,mDecsize);
        dsum=dsum+fabs(temp_val)/mDecsize;
        vDSP_vsq(tempdata,tempdata2,mDecsize);
		temp_val = vDSP_sve(tempdata2,mDecsize);
        vsum=vsum+fabs(temp_val)/mDecsize;
        
        mFreqPower[f]=(vsum+dsum*dsum);///fabs(vsum-dsum*dsum)*vsum;
        
        //Get DC estimation
        if(mFreqPower[f]>POWER_THR)
        {
            if ( max_valr > mMaxValue[0][f] ||
                (max_valr > mMinValue[0][f]+PEAK_THR &&
                 (mMaxValue[0][f]-mMinValue[0][f]) > PEAK_THR*4) )
            {
                mMaxValue[0][f]=max_valr;
            }
            
            if ( min_valr < mMinValue[0][f] ||
                (min_valr < mMaxValue[0][f]-PEAK_THR &&
                 (mMaxValue[0][f]-mMinValue[0][f]) > PEAK_THR*4) )
            {
                mMinValue[0][f]=min_valr;
            }
            
            if ( max_vali > mMaxValue[1][f] ||
                (max_vali > mMinValue[1][f]+PEAK_THR &&
                 (mMaxValue[1][f]-mMinValue[1][f]) > PEAK_THR*4) )
            {
                mMaxValue[1][f]=max_vali;
            }
            
            if ( min_vali < mMinValue[1][f] ||
                (min_vali < mMaxValue[1][f]-PEAK_THR &&
                 (mMaxValue[1][f]-mMinValue[1][f]) > PEAK_THR*4) )
            {
                mMinValue[1][f]=min_vali;
            }
            
            
            if ( (mMaxValue[0][f]-mMinValue[0][f]) > PEAK_THR &&
                (mMaxValue[1][f]-mMinValue[1][f]) > PEAK_THR )
            {
                for(i=0;i<=1;i++)
                    mDCValue[i][f]=(1-DC_TREND)*mDCValue[i][f]+
                    (mMaxValue[i][f]+mMinValue[i][f])/2*DC_TREND;
            }
            
        }
        
        //remove DC
        for(i=0;i<mDecsize;i++)
        {
            mBaseBandReal[f][i]=mBaseBandReal[f][i]-mDCValue[0][f];
            mBaseBandImage[f][i]=mBaseBandImage[f][i]-mDCValue[1][f];
        }
        
    }
}

void RangeFinder::SendSocketData(void)
{ int   i,index;
    
    if(1) //send baseband to matlab
    {
        index=mSocBufPos;
        for(i=0;i<16; i++) //number of frequencies
        {
            for(UInt32 k=0;k<mDecsize;k++) //iterate through samples
            {
                if(index<SOCKETBUFLEN-4) //ensure enough buffer
                {
                    mSocketBuffer[index++]=(char) (((short) mBaseBandReal[i][k]) &0xFF);
                    mSocketBuffer[index++]=(char) (((short) mBaseBandReal[i][k]) >> 8 );
                    mSocketBuffer[index++]=(char) (((short) mBaseBandImage[i][k]) &0xFF);
                    mSocketBuffer[index++]=(char) (((short) mBaseBandImage[i][k]) >> 8 );
                }
            }
            
        }
        mSocBufPos=index-1;
    }
}


void RangeFinder::GetBaseBand(void)
{
    UInt32 i,index,decsize,cid;
    decsize=mCurRecPos/CIC_DEC;
    mDecsize=decsize;
    
    //change data from int to float
    
    for(i=0;i<mCurRecPos; i++)
    {
        mFRecDataBuffer[i]= (float) (mRecDataBuffer[i]/32767.0);
    }
    
    for(i=0;i<mNumFreqs; i++)//mNumFreqs
    {
        vDSP_vmul(mFRecDataBuffer,mCosBuffer[i]+mCurProcPos,mTempBuffer,mCurRecPos); //multiply the cos
        cid=0;
        //sum CIC_DEC points of data, put into CICbuffer
        memmove(mCICBuffer[i][0][cid],mCICBuffer[i][0][cid]+mLastCICPos,CIC_DELAY*sizeof(float));
        index=CIC_DELAY;
        for(UInt32 k=0;k<mCurRecPos;k+=CIC_DEC)
        {
			mCICBuffer[i][0][cid][index] =  vDSP_sve(mTempBuffer+k,CIC_DEC);
            index++;
        }
        
        //prepare CIC first level
        memmove(mCICBuffer[i][1][cid],mCICBuffer[i][1][cid]+mLastCICPos,CIC_DELAY*sizeof(float));
        //Sliding window sum
        vDSP_vswsum(mCICBuffer[i][0][cid],0,mCICBuffer[i][1][cid]+CIC_DELAY,0,decsize,CIC_DELAY);
        //prepare CIC second level
        memmove(mCICBuffer[i][2][cid],mCICBuffer[i][2][cid]+mLastCICPos,CIC_DELAY*sizeof(float));
        //Sliding window sum
        vDSP_vswsum(mCICBuffer[i][1][cid],0,mCICBuffer[i][2][cid]+CIC_DELAY,0,decsize,CIC_DELAY);
        //prepare CIC third level
        memmove(mCICBuffer[i][3][cid],mCICBuffer[i][3][cid]+mLastCICPos,CIC_DELAY*sizeof(float));
        //Sliding window sum
        vDSP_vswsum(mCICBuffer[i][2][cid],0,mCICBuffer[i][3][cid]+CIC_DELAY,0,decsize,CIC_DELAY);
        //CIC last level to Baseband
        vDSP_vswsum(mCICBuffer[i][3][cid],0,mBaseBandReal[i],0,decsize,CIC_DELAY);
        
        
        vDSP_vmul(mFRecDataBuffer,mSinBuffer[i]+mCurProcPos,mTempBuffer,mCurRecPos); //multiply the sin
        cid=1;
        //sum CIC_DEC points of data, put into CICbuffer
        memmove(mCICBuffer[i][0][cid],mCICBuffer[i][0][cid]+mLastCICPos,CIC_DELAY*sizeof(float));
        index=CIC_DELAY;
        for(UInt32 k=0;k<mCurRecPos;k+=CIC_DEC)
        {
			mCICBuffer[i][0][cid][index] = vDSP_sve(mTempBuffer+k,CIC_DEC);
            index++;
        }
        
        //prepare CIC first level
        memmove(mCICBuffer[i][1][cid],mCICBuffer[i][1][cid]+mLastCICPos,CIC_DELAY*sizeof(float));
        //Sliding window sum
        vDSP_vswsum(mCICBuffer[i][0][cid],0,mCICBuffer[i][1][cid]+CIC_DELAY,0,decsize,CIC_DELAY);
        //prepare CIC second level
        memmove(mCICBuffer[i][2][cid],mCICBuffer[i][2][cid]+mLastCICPos,CIC_DELAY*sizeof(float));
        //Sliding window sum
        vDSP_vswsum(mCICBuffer[i][1][cid],0,mCICBuffer[i][2][cid]+CIC_DELAY,0,decsize,CIC_DELAY);
        //prepare CIC third level
        memmove(mCICBuffer[i][3][cid],mCICBuffer[i][3][cid]+mLastCICPos,CIC_DELAY*sizeof(float));
        //Sliding window sum
        vDSP_vswsum(mCICBuffer[i][2][cid],0,mCICBuffer[i][3][cid]+CIC_DELAY,0,decsize,CIC_DELAY);
        //CIC last level to Baseband
        vDSP_vswsum(mCICBuffer[i][3][cid],0,mBaseBandImage[i],0,decsize,CIC_DELAY);

        memmove(mBaseBand[i], mBaseBandReal[i], decsize * sizeof(float));
        memmove(mBaseBand[i] + decsize, mBaseBandImage[i], decsize * sizeof(float));
    }
    
    mCurProcPos=mCurProcPos+mCurRecPos;
    if(mCurProcPos >= mBufferSize)
        mCurProcPos= mCurProcPos - mBufferSize;
    mLastCICPos=decsize;
    mCurRecPos=0;
}


int  RangeFinder::getBaseBand(float *datas)
{
    int section = mDecsize * 2 * sizeof(float);
    for(int i = 0;i < mNumFreqs;i++)
    {
        memmove((char *)datas + i * section, mBaseBand[i], section);
    }
    return 0;
}