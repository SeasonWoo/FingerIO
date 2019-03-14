#include <math.h>
#include <string.h>
#include "MatrixProcess.h"
/**
* Created by Administrator on 2017/6/20.
*/


int vDSP_zvmags(float *real, float *image,	float *result, int iLen)
{
	for (int i = 0; i< iLen; i++)
	{
		result[i] = real[i] * real[i] + image[i] * image[i];
	}

	return 0;
}

int vDSP_zvphas(float *real, float *image, float *result,	int iLen)
{
	for (int i = 0; i< iLen; i++)
	{
		result[i] = (float)atan2((double)image[i], (double)real[i]);
	}	
	return 0;
}


int vDSP_vsadd(float *phasedata, float temp_val, float *result, int iLen)
	{

		for (int i = 0; i< iLen; i++)
		{
			result[i] = phasedata[i] + temp_val;
		}
		return 0;
	}

int vDSP_vsdiv(float *A, float B, float *result, int iLen)
	{

		for (int i = 0; i< iLen; i++)
		{
			result[i] = A[i] / B;
		}
		return 0;
	}

int vDSP_vsub(float *A, float *B, float *result, int iLen)
	{

		for (int i = 0; i< iLen; i++)
		{
			result[i] = A[i] - B[i];
		}
		return 0;
	}

int vDSP_vsq(float *A, float *result, int iLen)
	{
		for (int i = 0; i< iLen; i++)
		{
			result[i] = A[i] * A[i];
		}
		return 0;
	}

int vDSP_vswsum(float *A, int srcIndex, float *result, int iDestIndex, int iNum, int iDelay)
	{
		for (int i = srcIndex; i< iNum; i++)
		{
			float fResult = 0;
			for (int j = 0; j<iDelay; j++)
			{
				fResult += A[i + j];
			}
			result[i + iDestIndex] = fResult;
		}
		return 0;
	}

float vDSP_sve(float *A, int iLen)		
	{
		float result = 0;
		for (int i = 0; i< iLen; i++)
		{
			result += A[i];
		}
		return result;
	}

int vDSP_vmul(float *A, float *B, float *result, int iLen)
	{
		for (int i = 0; i< iLen; i++)
		{
			result[i] = A[i] * B[i];
		}
		return 0;
	}

float vDSP_maxv(float *A, int iLen)
{
	float result = A[0];
	for (int i = 0; i< iLen; i++)
	{
		if (result < A[i])
		{
			result = A[i];
		}
	}

	return result;
}
short  maxShortArray(short *A, int iLen)
	{
		short result = A[0];
		for (int i = 0; i< iLen; i++)
		{
			if (result < A[i])
			{
				result = A[i];
			}
		}

		return result;
	}
char  maxCharArray(char *A, int iLen)
{
	char result = A[0];
	for (int i = 0; i< iLen; i++)
	{
		if (result < A[i])
		{
			result = A[i];
		}
	}

	return result;
}



float vDSP_minv(float *A, int iLen)
{
	float result = A[0];
	for (int i = 0; i< iLen; i++)
	{
		if (A[i]< result)
		{
			result = A[i];
		}
	}

	return result;
}

/*int  MatrixProcess::moveArrayElem(float *src, int k, float *result)
{
	int length = src.length;
	int newk = k % length;
	//float *result = new float[length];
	for (int i = 0; i < length; i++) {
		int newPosition = (i + newk) % length;
		result[newPosition] = src[i];
	}

	return 0;
}*/

void memmove(void *dest, void *src, int length)
{
	//float *newArray = new float[length];
	//System.arraycopy(src, 0, dest, 0, length);
	memcpy(dest, src, length);
	//return newArray;
}

