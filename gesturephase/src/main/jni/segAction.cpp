
#include "segAction.h"
#include <stdio.h>      /* printf */
#include <stdlib.h>     /* abs */


#define ACC_GET_RESET_MAX 3
#define ACC_SEG_START_MIN  26
#define ACC_SEG_END_SHIFT  15

//#define ACC_SEG_START_REAL_TIME_MIN  60
//#define ACC_SEG_END_SHIFT  15
int segAction::getAccelerate(float * pdata, int iLen, vector<float>& vAcc)
{
	if (pdata)
	{
		vAcc.push_back(0);
		vAcc.push_back(0);
		for (int i = 2; i < (iLen-3); i++)
		{

			vAcc.push_back( (2 * (pdata[i + 1] - pdata[i - 1]) + pdata[i + 2] - pdata[i - 2]) / 8);
			if (abs((int)vAcc[i])  < ACC_GET_RESET_MAX)
			{
				vAcc[i] =0 ;
			}
		}
		vAcc.push_back(0);
		vAcc.push_back(0);
		vAcc.push_back(0);

	}
	return 0;
}



int segAction::doseg(float * pAcc, int iLen, vector<int>& vpSegPos, int& iActionNum)
{
	if (pAcc)
	{		
		int iStart = 0;
		int iEnd = 0;
		iActionNum = 0;

	
		int i = 0;
		while(i<iLen)
		{
			if (abs((int)(pAcc[i])) > ACC_SEG_START_MIN)
			{

				int iSignum = 0;
				iSignum = (pAcc[i] > 0 ? 1 : -1);

				for (int m = i; m > 0; m--)
				{
					if (pAcc[m] == 0 || pAcc[m] * iSignum < 0)
					{
						vpSegPos.push_back(m);
						iStart = m;
						break;
					}					

				}

				for (int n = (iStart + 5); n < iLen; n++)
				{
					bool bAllLittle = true;
					for (int j = n; j < (n + 8); j++)
					{
						if (abs(pAcc[j]) >= ACC_SEG_END_SHIFT)
						{
							bAllLittle = false;
							break;
						}
					}

					if (bAllLittle)
					{
						vpSegPos.push_back(n);
						iActionNum += 1;
						i = n;
						break;
					}

				}

			}
			i++;
		}

	}
	return 0;
}

int segAction::doSegRealTime(float * pAcc, int iLen, int & iMark, int & iThresPoint,int & iStart, int & iEnd,int sensitive)
{
	if (pAcc)
	{		
		int i = 0;
		while (i<iLen)
		{
			if (iMark == 0) // need to find the start point
			{
				if (abs((int)(pAcc[i])) > sensitive)
				{

					int iSignum = 0;
					iThresPoint = i;
					iSignum = (pAcc[i] > 0 ? 1 : -1);

					for (int m = i; m > 0; m--)
					{
						if (pAcc[m] == 0 || pAcc[m] * iSignum < 0)
						{
							iStart = m;
							//vpSegPos.push_back(m);
							iMark = 1;
							break;
						}

					}
				 }
			 }
			else
			{
				for (int n = (iThresPoint); n < iLen; n++)
				{
					if ((n + 10) < iLen)
					{
						bool bAllLittle = true;
						for (int j = n; j < (n + 10); j++)
						{
							if (abs(pAcc[j]) >= ACC_SEG_END_SHIFT)
							{
								bAllLittle = false;
								break;
							}
						}

						if (bAllLittle)
						{
							//vpSegPos.push_back(n);
							iEnd = n;
							i = n;
							iMark = 0;
							break;
						}
						else
						{
							continue;
						}
					}
					else
					{
						return 0;
					}
						

				}

			}			

			
			i++;
		}

	}

	if (iStart > iEnd || iMark != 0)
	{
		iStart = 0;
		iEnd = 0;
		iMark = 0;
	}
	return 0;
}