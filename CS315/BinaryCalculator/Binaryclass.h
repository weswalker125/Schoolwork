#include <vector>
#include <iostream>
#include <cmath>
using namespace std;

class Result
{
public:
	vector<int> quotient;
	vector<int> remainder;
};

class Binary {
public:
	vector<int> DecToBin(int decimal);
	vector<int> BinAdd(vector<int> one, vector<int> two);
	vector<int> BinSub(vector<int> a, vector<int> b);
	vector<int> BinMultiply(vector<int> one, vector<int> two);
	Result BinDiv(vector<int> dividend, vector<int> divisor);
	vector<int> TimesTwo(vector<int> pre);
	vector<int> EliminateLeadingZero(vector<int> temp);
	vector<int> DecAdd(vector<int>one, vector<int> two);
	vector<int> BinToDec(vector<int> OLDbinary);
	vector<int> modexp(vector<int> base, vector<int> expo, vector<int> mod);
	void outputVector(vector<int> a);
	double BinToInt(vector<int> a);

private:
	vector<int> binary;
};
