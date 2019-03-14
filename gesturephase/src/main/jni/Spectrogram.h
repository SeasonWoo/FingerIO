#ifndef SPECTRO_GRAM_H_
#define SPECTRO_GRAM_H_
#include <complex>
#include <math.h>
using namespace std;
class Spectrogram
{
public:
	Spectrogram();
	~Spectrogram(); 
	bool spectrogram(float* signalVector,int N, int win, int noverlap, int nfft,int fs, float ***pLogPXX, int& iDstRow, int &iDstColumn);
    bool spectrogramdouble(float* signalVector,int N, int win, int noverlap, int nfft,int fs);
	void FFT(complex<double>* f, int N, double d);
	void doFFT(float * pfSignal, int iDataSize, int iNfft, float* pfFFtReal, float * pfFFtImage);
	int tableFFT(double *dInput, double *dOutput, int nFFTLen);
	void doMidFFT(float * pfSignal, int iDataSize, int iNfft, float* pfFFtReal, float * pfFFtImage);
private:
	int log2(int N);
	int check(int n);
	int reverse(int N, int n);
	void ordina(complex<double>* f1, int N);
	void transform(complex<double>* f, int N);

	void doFFTDouble(float * pfSignal, int iDataSize, int iNfft, double* pfFFtReal, double * pfFFtImage);
	void doFastFFT(float * pfSignal, int iDataSize, int iNfft, float* pfFFtReal, float * pfFFtImage);
	void InitialFFT();
private:
	//fftw_complex * m_in, *m_out;
	int  m_iFFTLen;
	int  m_iDataLen;
	int  m_iFFTGrade;
};

#endif