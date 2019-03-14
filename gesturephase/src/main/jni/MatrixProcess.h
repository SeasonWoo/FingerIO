
#ifndef MatrixProcess_h
#define MatrixProcess_h

/*class MatrixProcess
{
public:
	MatrixProcess() {};
	~MatrixProcess() {};
*/

	int vDSP_zvmags(float *real, float *image, float *result, int iLen);
	
	int vDSP_zvphas(float *real, float *image, float *result, int iLen);
	
	int vDSP_vsadd(float *phasedata, float temp_val, float *result, int iLen);
	
	int  vDSP_vsdiv(float *A, float B, float *result, int iLen);

	int  vDSP_vsub(float *A, float *B, float *result, int iLen);

	int vDSP_vsq(float *A, float *result, int iLen);

	int  vDSP_vswsum(float *A, int srcIndex, float *result, int iDestIndex, int iNum, int iDelay);

	float  vDSP_sve(float *A, int iLen);

	int vDSP_vmul(float *A, float *B, float *result, int iLen);

	float  vDSP_maxv(float *A, int iLen);

	short  maxShortArray(short *A, int iLen);

	char  maxCharArray(char *A, int iLen);

	float vDSP_minv(float *A, int iLen);

	//int  moveArrayElem(float *src, int k, float *result);

	void memmove(void *dest, void *src, int length);
/*
}
*/	





#endif /* MatrixProcess_h */
