#include <opencv2\opencv.hpp>
#include <string>
#include <vector>
using namespace std;
using namespace cv;

/*const int centreFreq = 20000;
const int nfft = 8192;
const int Fs = 44100;
float Fsolu = (float)Fs / (float)nfft;
int  centreBin = (centreFreq*nfft / Fs);
Mat DopShiftResultSrcM(4097, 59, CV_8U)*/

void initFloatMat(Mat& desImage, float **src, int iBegin);
void initBinMat(Mat& srcImage, uchar* num);
void bwareImfill(Mat& srcImage, int iareaSize);
int  countRectSize(Mat& srcImage, cv::Rect& rect);
void dopshift(Mat& srcImage, vector<int>& vecPointY, int centreBin);
void pointTransToHz(const vector<int>& vecPointSrc, float fsolu,  vector<float>& vecHz);
void negativeToZero(Mat& matM);
void tailNoise(Mat& matM, int lastcol);
void nomalLizeFilter(Mat& matM);
void tranFloatMatTounCharMat(Mat& srcMatM, Mat& desMat);
void tranbinMIntestIngShowMat(Mat& srcMatM, Mat& desMat);
void lessToZero(Mat& matM, float fData);

void constructAvgCol(Mat& medianfilterDesM, vector<float>& vecNoise, int colsAvg = 5);
void constructRepmat(Mat& repmatM, const vector<float>& vecNoise);