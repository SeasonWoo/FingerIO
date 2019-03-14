#include "bwareaopen.h"
#include<vector>
using namespace std;

void initFloatMat(Mat& desImage, float **src, int iBegin)
{
	for (int i = 0;i < desImage.rows; i++)
	{
		for (int j = 0;j < desImage.cols; j++)
		{
			int z = i+ iBegin;
			desImage.at<float>(i, j) = src[j][z];
		}
	}
}

void constructAvgCol(Mat& medianfilterDesM, vector<float>& vecNoise, int colsAvg)
{
	for (int i = 0;i < medianfilterDesM.rows; i++)
	{
		float sum = 0;
		for (int j = 0; j < colsAvg; j++)
		{
			sum += medianfilterDesM.at<float>(i, j);
		}
		vecNoise.push_back(sum/colsAvg);
	}
}

void constructRepmat(Mat& repmatM, const vector<float>& vecNoise)
{	
	//Ȼ�����ÿ�и���
	for (size_t j = 0; j < repmatM.cols; j++)
	{
		for (int i = 0; i < repmatM.rows; i++)
		{
			repmatM.at<float>(i, j) = vecNoise[i];
		}
	}
}

void negativeToZero(Mat& matM)
{
	for (size_t i = 0; i < matM.rows; i++)
	{
		for (int j = 0; j< matM.cols; j++)
		{
			if (matM.at<float>(i, j) < 0)
			{
				matM.at<float>(i, j) = 0;
			}
		}
	}
}



void lessToZero(Mat& matM, float fData)
{
	for (size_t i = 0; i < matM.rows; i++)
	{
		for (int j = 0; j< matM.cols; j++)
		{
			if (matM.at<float>(i, j) < fData)
			{
				matM.at<float>(i, j) = 0;
			}
		}
	}
}

void  nomalLizeFilter(Mat& matM)
{
	float max = 0;
	for (int i = 0;i < matM.rows; i++)
	{
		for (int j = 0;j < matM.cols; j++)
		{
			if (matM.at<float>(i, j) > max)
			{
				max = matM.at<float>(i, j);
			}
		}
	}

	for (int i = 0;i < matM.rows; i++)
	{
		for (int j = 0;j < matM.cols; j++)
		{
			if (max > 0)
			{
				matM.at<float>(i, j) = matM.at<float>(i, j) / max;
			}
			else
			{
				matM.at<float>(i, j) = 0;
			}
			
		}
	}
}


void tailNoise(Mat& matM, int lastcol)
{
	for (size_t j = matM.cols - lastcol; j < matM.cols; j++)
	{
		for (int i = 0; i < matM.rows; i++)
		{
			matM.at<float>(i, j) = 0;
		}
	}
}

void dopshift(Mat& srcImage, vector<int>& vecPointY, int centreBin)
{
	int maxPoint = 0;
	int minPoint = INT_MAX;
	int sumPoint = 0;
	int numPoint = 0;
	for (int i = 0;i <srcImage.cols;i++)
	{
		maxPoint = 0;
		minPoint = INT_MAX;
		sumPoint = 0;
		numPoint = 0;
		for (int j = 0; j <srcImage.rows; j++)
		{
			if (srcImage.at<uchar>(j, i) != 0)
			{
				sumPoint += j+1;
				numPoint++;
				if (j > maxPoint)
				{
					maxPoint = j+ 1;
				}

				if (j < minPoint)
				{
					minPoint = j+1;
				}
			}
		}

		if (numPoint > 0)
		{
			int avgPoint = sumPoint/numPoint;
			if (avgPoint >= 51)
			{
				int tmp = maxPoint + centreBin - 51;
				vecPointY.push_back(tmp);
			}
			else
			{
				int tmp = minPoint + centreBin - 51;
				vecPointY.push_back(tmp);
			}
		}
		else
		{
			vecPointY.push_back(centreBin);
		}
	}
}

void pointTransToHz(const vector<int>& vecPointSrc, float fsolu,  vector<float>& vecHz)
{
	for (int i = 0; i < vecPointSrc.size(); i++)
	{
		float tmp = ceil(fsolu*vecPointSrc[i]) + 1;
		vecHz.push_back(tmp);
	}
}

void bwareImfill(Mat& srcImage, int iareaSize)
{
	vector<vector<Point>> contours;
	vector<Vec4i> hierarchy;
	CvRect rect;
	findContours(srcImage, contours, CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE);
	int maxarea = 0;
	int index = -1;
	for (size_t i = 0; i < contours.size(); i++)
	{
		int tmparea = fabs(contourArea(contours[i], true));
		cv::Rect rect = cv::boundingRect(cv::Mat(contours[i]));
		int countSize = countRectSize(srcImage, rect);
		if (tmparea > maxarea)
		{
			maxarea = tmparea;
			index = i;
		}

		if (countSize < iareaSize)
		{
			drawContours(srcImage, contours, i, Scalar::all(1), CV_FILLED);
		}
		else
		{
			drawContours(srcImage, contours, i, Scalar::all(1), CV_FILLED);
		}
	}

	//drawContours(srcImage, contours, index, Scalar::all(1), CV_FILLED);
}

int  countRectSize(Mat& srcImage, cv::Rect& rect)
{
	int size = 0;
	for (size_t i = 0; i < rect.width; i++)
	{
		for (size_t j = 0; j < rect.height; j++)
		{	
			int x = rect.x + i;
			int y = rect.y + j;
			if (x < srcImage.cols && y < srcImage.rows)
			{
				if (srcImage.at<uchar>(y, x) == 1)
				{
					++size;
				}

			}
		}
	}
	
	return size;
}

void initBinMat(Mat& srcImage, uchar* num)
{
	for (int i = 0;i < srcImage.rows; i++)
	{
		for (int j = 0;j < srcImage.cols; j++)
		{
			if (*(num + i*srcImage.cols + j) == 1)
			{
				srcImage.at<uchar>(i, j) = 255;
			}
			else
			{
				srcImage.at<uchar>(i, j) = 0;
			}
		}
	}
}

void tranFloatMatTounCharMat(Mat& srcMatM, Mat& desMat)
{
	for (int i = 0;i < srcMatM.rows; i++)
	{
		for (int j = 0;j < srcMatM.cols; j++)
		{	
			float tmp = srcMatM.at<float>(i, j);
			if (tmp == 255)
			{
				desMat.at<uchar>(i, j) = (uchar)1;
			}
			else
			{
				desMat.at<uchar>(i, j) = (uchar)0;
			}
		}
	}
}

void tranbinMIntestIngShowMat(Mat& srcMatM, Mat& desMat)
{
	for (int i = 0;i < srcMatM.rows; i++)
	{
		for (int j = 0;j < srcMatM.cols; j++)
		{
			float tmp = srcMatM.at<uchar>(i, j);
			if (tmp == 1)
			{
				desMat.at<uchar>(i, j) = (uchar)255;
			}
			else
			{
				desMat.at<uchar>(i, j) = (uchar)0;
			}
		}
	}
}