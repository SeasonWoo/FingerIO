#ifndef _interpolate_h
#define _interpolate_h
#include <stdio.h>
#include <vector>
#include <array>
#include "string.h"
#include "GammaUtil.h"
class Interpolate
{
public:
	Interpolate() {};
	~Interpolate() {};
	int doInterpolate(vector<float> vData, int iSrcNum, int iDstNum, vector<float>& vdstInstroke);
	void WaitForEnter ();
	float CubicHermite (float A, float B, float C, float D, float t);
	 
	template <typename T>
	inline T GetIndexClamped(const std::vector<T>& points, int index);
	int test();

};
#endif