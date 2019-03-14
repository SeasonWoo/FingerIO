
#include "interpolate.h"


typedef std::vector<float> TPointList1D;
typedef std::vector<std::array<float,2>> TPointList2D;
  
void Interpolate::WaitForEnter ()
{
    printf("Press Enter to quit");
    fflush(stdin);
    getchar();
}
 
// t is a value that goes from 0 to 1 to interpolate in a C1 continuous way across uniformly sampled data points.
// when t is 0, this will return B.  When t is 1, this will return C.
float Interpolate::CubicHermite (float A, float B, float C, float D, float t)
{
    float a = -A/2.0f + (3.0f*B)/2.0f - (3.0f*C)/2.0f + D/2.0f;
    float b = A - (5.0f*B)/2.0f + 2.0f*C - D / 2.0f;
    float c = -A/2.0f + C/2.0f;
    float d = B;
  
    return a*t*t*t + b*t*t + c*t + d;
}
 
template <typename T>
inline T Interpolate::GetIndexClamped(const std::vector<T>& points, int index)
{
    if (index < 0)
        return points[0];
    else if (index >= int(points.size()))
        return points.back();
    else
        return points[index];
}

int Interpolate::test()
{
    const int c_numSamples = 100;
 
    // show some 1d interpolated values
    {     
		TPointList1D points;
		/*points.push_back(0.0f);
		points.push_back(1.6f);
		points.push_back(2.3f);
		points.push_back(3.5f);
		points.push_back(4.3f);
		points.push_back(5.9f);
		points.push_back(6.8f);*/
        printf("1d interpolated values.  y = f(t)n\n");


		string sSignalFile =  "./data/stroke59.txt";
		float *pSignal = NULL;
		int iSignalLen = GammaUtil::readByLine(sSignalFile,points);

		TPointList1D dstTPointList1D;
		vector<float> dstInstroke;;


        for (int i = 0; i < c_numSamples; ++i)
        {
            float percent = ((float)i) / (float(c_numSamples - 1));
            float x = (points.size()-1) * percent;
 
            int index = int(x);
            float t = x - floor(x);
            float A = GetIndexClamped(points, index - 1);
            float B = GetIndexClamped(points, index + 0);
            float C = GetIndexClamped(points, index + 1);
            float D = GetIndexClamped(points, index + 2);
 
            float y = CubicHermite(A, B, C, D, t);
			dstInstroke.push_back(y);
            printf("[%d]  Value at %0.2f = %0.2fn\n",i, x, y);
        }
        printf("n");
    }
	return 0;
}

int Interpolate::doInterpolate(vector<float> vData,int iSrcNum,int iDstNum, vector<float>& vdstInstroke)
{	
	// show some 1d interpolated values
	{
		
		printf("1d interpolated values.  y = f(t)n\n");

		TPointList1D dstTPointList1D;		

		for (int i = 0; i < iDstNum; ++i)
		{
			float percent = ((float)i) / (float(iDstNum - 1));
			float x = (vData.size() - 1) * percent;

			int index = int(x);
			float t = x - floor(x);
			float A = GetIndexClamped(vData, index - 1);
			float B = GetIndexClamped(vData, index + 0);
			float C = GetIndexClamped(vData, index + 1);
			float D = GetIndexClamped(vData, index + 2);

			float y = CubicHermite(A, B, C, D, t);
			vdstInstroke.push_back(y);
			printf("[%d]  Value at %0.2f = %0.2fn\n", i, x, y);
		}
		
		printf("n");
	}
	return 0;
}
 