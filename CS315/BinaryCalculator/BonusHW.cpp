/*
 * Author: Wesley Walker
 * Completion Date: Sunday, March 28th, 2010
 *
 * Bonus Homework 1
 *
 * i: Binary message to encode, and number of
 * 	bit-encryption
 * o: All sub-computations, down to the encoded message,
 * 	then the decoded message, and time taken.
 * 	
*/
#include "Binaryclass.h"
#include "time.h"
#include <string>
using namespace std;

Binary VecFunctions;
struct Numbers //used to decide if numbers are positive or negative
{
	bool positive;
	vector<int> binary;
};

struct aANDb //data struct to return both coefficients, a and b at once
{
	Numbers a;
	Numbers b;
};

int call = 1; 
vector<int> generatePrime(int bits); //calls generate random (checks primality)
vector<int> generateRandom(int bits); //generates random binary vector
bool GCDisONE(vector<int> a, vector<int> b); //finds GCD 
aANDb coefficients(vector<int> PRODUCTminusONE, vector<int> e); //find a and b
vector<int> encode(vector<int> x, vector<int> e, vector<int> N); //encodes the message
vector<int> decode(vector<int> y, vector<int> d, vector<int> N); //decodes the encoded message
vector<int> stringToVec(string a); //converts a binary string to vector



int main(int argc, char** argv[])
{
	time_t start = 0; time_t end = 0; time_t elapsed =0;
	//used to compute time values

	//used as constants to compare to 
	vector<int> one; one.push_back(1);
	vector<int> zero; zero.push_back(0);

	//user inputs the binary string to decode
	string sMessage;
	cout << "Input the binary message to encode: ";
	cin >> sMessage;

	//make sMessage into a vector
	vector<int> message;
	message = stringToVec(sMessage);

	//user inputs integer 'n', defining the number
	//of bits for 2 random primes generated.
	int n;
	cout << "Input number of bits for primes: ";
	cin >> n;
	
	start = time(NULL); //get start time

	//Generate two random primes with n-bits.
	//generatePrime(int bit) generates a random prime
	//utilizing the 'randomBinary' function, and primality check
	vector<int> p, q; 
	p = generatePrime(n);
	q = generatePrime(n);
	while(p == q) //to ensure p and q aren't the same.
		q = generatePrime(n);
	
/* OUTPUT FOR TEST ONLY */
	//Output p and q:
	cout << "Binary p: ";
	VecFunctions.outputVector(p);
	cout << endl;
	cout << "Binary q: ";
	VecFunctions.outputVector(q);
	cout << endl << endl;
	
	cout << "In Decimal: ";
	vector<int> DECp, DECq;
	DECp = VecFunctions.BinToDec(p);
	VecFunctions.outputVector(DECp);
	cout << "	";
	DECq = VecFunctions.BinToDec(q);
	VecFunctions.outputVector(DECq);
	cout << endl << endl; 
/* OUTPUT FOR TEST ONLY */
	
	//N = p*q
	vector<int> N;
	N = VecFunctions.BinMultiply(p, q);

/* OUTPUT FOR TEST ONLY */
	cout << "Decimal N: ";
	vector<int> DECN = VecFunctions.BinToDec(N);
	VecFunctions.outputVector(DECN);
	cout << endl << endl;
/* OUTPUT FOR TEST ONLY */


	//Compute k, the number of bits in N
	int k;
	k = N.size();

/* OUTPUT FOR TEST ONLY */
	cout << "k: " << k << endl << endl;
/* OUTPUT FOR TEST ONLY */

	
	//Computer e, S.T. it's relatively prime to (p-1)(q-1)
	//1. Compute (p-1) and (q-1);
	vector<int>	PminusONE, QminusONE;
	PminusONE = VecFunctions.BinSub(one, p);
	QminusONE = VecFunctions.BinSub(one, q);

/* OUTPUT FOR TEST ONLY */
	cout << "p - 1: ";
	vector<int> DEC1 = VecFunctions.BinToDec(PminusONE);
	VecFunctions.outputVector(DEC1);
	cout << endl << "q - 1: ";
	vector<int> DEC2 = VecFunctions.BinToDec(QminusONE);
	VecFunctions.outputVector(DEC2);
	cout << endl;
/* OUTPUT FOR TEST ONLY */

	//2. Compute (p-1)(q-1), call it PRODUCTminusONE
	vector<int> PRODUCTminusONE;
	PRODUCTminusONE = VecFunctions.BinMultiply(PminusONE, QminusONE);

/* OUTPUT FOR TEST ONLY */
	cout << "(p-1)(q-1) :  ";
	vector<int> DECpro = VecFunctions.BinToDec(PRODUCTminusONE);
	VecFunctions.outputVector(DECpro);
	cout << endl << endl;
	
	//Select e to be relatively prime to PRODUCTminusONE);
	//compute a prime, make sure it's rel. prime to PRODUCTminusONE
	vector<int> e;
	bool relativelyPrime = false;
	Result temp11;
	int tries = 1;
	int bits = 5; //start with 5 bits for e, if no successful, we'll increment
	while(!relativelyPrime)
	{
		e = generatePrime(bits); //arbitrary bit length: 2 bits less than users input.
		if(tries % 5 == 0)
			bits++;
		temp11 = VecFunctions.BinDiv(PRODUCTminusONE, e);
		temp11.remainder = VecFunctions.EliminateLeadingZero(temp11.remainder);
		if(temp11.remainder != zero && temp11.remainder.size() != 0)
		{
			relativelyPrime = true;
		}
	}
/* OUTPUT FOR TEST ONLY */
	cout << "e: ";
	vector<int> DECe = VecFunctions.BinToDec(e);
	VecFunctions.outputVector(DECe);
	cout << endl << endl;
/* OUTPUT FOR TEST ONLY */

	//when it exits, we have e which is relatively prime to PRODUCTminusONE

	//determine coefficients 'a' and 'b' to find the inverse:
	// a*PRODUCTmineONE + b*e = 1
	aANDb coeffs;
	coeffs = coefficients(PRODUCTminusONE, e);

	cout << "a is: ";
	vector<int> a = coeffs.a.binary;
	if(coeffs.a.positive == false)
		cout << "-";
	vector<int> NEWa = VecFunctions.BinToDec(a);
	VecFunctions.outputVector(NEWa);
	cout << endl;

	cout << "b is: ";
	vector<int> b = coeffs.b.binary;
	if(coeffs.b.positive == false)
		cout << "-";
	vector<int> NEWb = VecFunctions.BinToDec(b);
	VecFunctions.outputVector(NEWb);
	cout << endl << endl;

	//output public data:
	cout << "PUBLIC KEYS: (N, e) = "; 
	VecFunctions.outputVector(DECN);
	cout <<	", "; 
	VecFunctions.outputVector(DECe);
	cout << endl << endl;

	//Now, b is the inverse or e mod N...
	//if b is negative:
	Numbers B;
	if(coeffs.b.positive == false)
	{
		B.positive = true;
		B.binary = VecFunctions.BinSub(coeffs.b.binary, PRODUCTminusONE);
	}
	else
		B = coeffs.b;
	
	//decode key: "d" is:
	vector<int> d = B.binary;
	d = VecFunctions.EliminateLeadingZero(d);
	//it's positive.
	//output d:
	vector<int> NEWd = VecFunctions.BinToDec(d);
	cout << "d is: ";
	VecFunctions.outputVector(NEWd); 
	cout << endl << endl;


	//encode:
	vector<int> encoded;
	encoded = encode(message, e, N);

	//Output encoded message:
	cout << "encoded message: ";
	VecFunctions.outputVector(encoded);
	cout << endl << endl;

	//decode:
	vector<int> decoded;
	decoded = decode(encoded, d, N);

	//Output decoded message:
	cout << "decoded message: ";
	VecFunctions.outputVector(decoded);
	cout << endl << endl;

	end = time(NULL); //get end time
	elapsed = end - start; //compute elapsed time
	cout << "Elapsed time: " << elapsed << endl;

	return 0;
}
/*
 * generatePrime
 * i: number of bits (length of prime)
 * o: prime of inputted bitlength.
*/
vector<int> generatePrime(int bits)
{
	vector<int> random, N, one, three, temp;
	
	//initialize one in binary ("1")
	one.push_back(1);
	//initialize three in binary ("11")
	three.push_back(1); three.push_back(1);

	//Check primality
	bool prime = false;
	
	//Primality check uses the theory:
	//if 3^(N-1) mod N = 1, then N is prime.
	while(!prime)
	{
		//Generate a random number
		random = generateRandom(bits);
		N = VecFunctions.BinSub(one, random); //N is "random - 1",
		N = VecFunctions.EliminateLeadingZero(N);
		//primality check used in book/discussed in class
		temp = VecFunctions.modexp(three, N, random);
		temp = VecFunctions.EliminateLeadingZero(temp);
		if(temp == one)
			prime = true;
	}

	//When the loop exits, the random number is prime
	return random;
}
/*
 * generateRandom
 * i: bits, number of bits length of random binary
 * o: random binary of bits-length
*/
vector<int> generateRandom(int bits)
{
	vector<int> number;
	//initialize correct size
	for(int i = 0; i < bits; i++)
		number.push_back(0);
	//seed for random function
	srand((unsigned int)time(NULL));
	
	int digit = 0;

	for(int i = 0; i < bits;  i++)
	{	//make it a one or zero 
		digit = rand() % 2;
		number[i] = digit;
	}

	number[0] = 1; //Ensures no leading zeros (correct number of bits)
	number[number.size() - 1] = 1; //Ensures the number is odd (all primes are odd)
	
	return number;
}
/*
	GCDisONE
	funcion takes 2 binary vectors, and looks to see if the greatest
	common divisor of the two is one.  If the GCD is 1, function returns 
	"true"... otherwise, function returns "false".
	i: Two binary vectors greater than zero
	o: T or F
*/
bool GCDisONE(vector<int> a, vector<int> b)
{
	vector<int> one;
	one.push_back(1);
	a = VecFunctions.EliminateLeadingZero(a);
	b = VecFunctions.EliminateLeadingZero(b);
	
	//if either is 1, the gcd must be one.
	if(a == one || b == one)
		return true;
	
	//if both are even, gcd is 2 or greater... 
	if(a[a.size() - 1] == 0 && b[b.size() - 1] == 0)
		return false;

	//Determine which is small (compare by brute force up to sqrt)
	vector<int> smaller, larger;
	if(a.size() < b.size())
	{
		smaller = a;
		larger = b;
	}
	else
	{
		smaller = b;
		larger = a;
	}

	//if the smaller divides into the larger, gcd is greater than one
	vector<int> remainder;
	Result divide;

	divide = VecFunctions.BinDiv(larger, smaller);
	remainder = divide.remainder;
	remainder = VecFunctions.EliminateLeadingZero(remainder);
	if(remainder.size() == 0 || remainder.size() == 1 && remainder[0] == 0) //no remainder
		return false; //one is the divisor of the other.

	
	//root is a pseudo-squareroot of the smaller number (not precise)
	//root will be the right most ((bits/2) + 1) bits of smaller
	vector<int> root;
	unsigned int size =  (smaller.size() / 2) + 1;
	for(unsigned int i = 0; i < size; i++)
		root.push_back(smaller[i]);

	//search for common divisors bigger than 1:
	bool FoundDivisor = false; //finder-flag
	vector<int> ct;
	ct.push_back(1); ct.push_back(1); //begin counter at 3
	//we've already taken notice of gcd being 2, and 1 is always
	//a common divisor.
	

	while(ct != root && !FoundDivisor)
	{
		divide = VecFunctions.BinDiv(a, ct);
		remainder = divide.remainder;
		remainder = VecFunctions.EliminateLeadingZero(remainder);
		if(remainder.size() == 0) //no remainder
		{ //if ct is a divisor to a, determine if it's a divisor to b
			divide = VecFunctions.BinDiv(b, ct);
			remainder = divide.remainder;
			remainder = VecFunctions.EliminateLeadingZero(remainder);

			if(remainder.size() == 0 || remainder.size() == 1 && remainder[0] == 0) //no remainder also
			{
				FoundDivisor = true;
				break; //must be a divisor greater than 1
			}
		}

		//ct wasn't a common divisor, increment ct:
		ct = VecFunctions.BinAdd(ct, one);
		ct = VecFunctions.EliminateLeadingZero(ct);
	}

	if(FoundDivisor)
		return false; //found a common divisor greater than 1
	
	else
		return true; //no other divisors were found
}

//The following is using a struct "Number" to incorporate negative numbers
/*
 * coefficients
 * i: (p-1)(q-1) and e
 * o: a and b, coefficients to a*(p-1)(q-1) + b*e = 1
*/
//Finding a & b.
aANDb coefficients(vector<int> PRODUCTminusONE, vector<int> e)
{
	//used as constants to compare to
	vector<int> neg, one, zero;
	neg.push_back(-1); one.push_back(1); zero.push_back(0);
	vector<Result> division;
	division.push_back(VecFunctions.BinDiv(PRODUCTminusONE, e));
	
	PRODUCTminusONE = e;
	e = division[0].remainder;
	
	//method demonstrated in class
	//keep record of quotient and remainder per line, 
	int i = 1;
	while(e != zero)
	{
		e = VecFunctions.EliminateLeadingZero(e);
		division.push_back(VecFunctions.BinDiv(PRODUCTminusONE, e));
		PRODUCTminusONE = e;
		e = division[i].remainder;
		i++;
	}
	
	//Now to find a and b
	//i holds the number of operations we'll need to make

	Numbers a, b, OLDa, OLDb, temp, temp2;
	
	OLDa.binary = PRODUCTminusONE;
	OLDa.positive = true;
	OLDb.binary = e;
	OLDb.positive = true;

	a = OLDb;

	temp.binary = VecFunctions.BinMultiply(division[i-1].quotient, OLDb.binary);
	b.binary = VecFunctions.BinSub(temp.binary, OLDa.binary);
	if(b.binary == neg)
	{	//if's its negative, 
		b.positive = false;
		b.binary = VecFunctions.BinSub(OLDa.binary, temp.binary);
	}

	else
		b.positive = true;

	OLDa = a;
	OLDb = b;

	//a = the old b,
	//b = old a minus (old b times current quotient)
	for(int j = i-2; j >= 0; j--)
	{
		a = OLDb;
		//check sign operations here
		temp.binary = VecFunctions.BinMultiply(division[j].quotient, OLDb.binary);
		if(OLDb.positive == false)
		{ //temp is negative
			//if it's pos minus neg: pos + pos
			if(OLDa.positive == true)
			{
				b.binary = VecFunctions.BinAdd(temp.binary, OLDa.binary);
				b.positive = true;

			}
			//if its neg minus neg -(pos+ pos)
			else if(OLDa.positive == false)
			{
				b.binary = VecFunctions.BinAdd(temp.binary, OLDa.binary);
				b.positive = false;
			}
		}
		else if(OLDb.positive == true)
		{	//temp is positive
			 //if it's neg minus pos: -(neg+pos)
			if(OLDa.positive == false)
			{
				b.binary = VecFunctions.BinAdd(temp.binary, OLDa.binary);
				b.positive = false;
			}
			else if(OLDa.positive = true)
			{
				temp2.binary = VecFunctions.BinSub(temp.binary, OLDa.binary);
				if(temp2.binary == neg)
				{
					b.positive = false;
					b.binary = VecFunctions.BinSub(OLDa.binary, temp.binary);
				}
				else
				{
					b = temp;
					b.positive = true;
				}
					
			}
		}

		OLDa = a;
		OLDb = b;
	}
	aANDb Final;
	Final.a = a;
	Final.b = b;

	return Final;
}
/* 
	encode
	i: the binary message to encode, x, and the public keys e and N
	o: encoded message
*/
vector<int> encode(vector<int> x, vector<int> e, vector<int> N)
{
	vector<int> encoded;
	encoded = VecFunctions.modexp(x, e, N);
	return encoded;
}

/* 
	decode
	i: the encoded binary message, the private key, d, and N
	o: the original message
*/
vector<int> decode(vector<int> y, vector<int> d, vector<int> N)
{
	y = VecFunctions.EliminateLeadingZero(y);
	d = VecFunctions.EliminateLeadingZero(d);
	N = VecFunctions.EliminateLeadingZero(N);

	vector<int> decoded;
	decoded = VecFunctions.modexp(y, d, N);
	return decoded;
}
/*
	stringToVec
	i: binary string (actual 'string')
	o: binary vector
*/
vector<int> stringToVec(string a)
{
	vector<int> vec;
	//for the length of the string, push 1 or 0 depending on
	//appropriate char.
	for(int unsigned i = 0; i < a.length(); i++)
	{
		if(a[i] == '1')
			vec.push_back(1);
		if(a[i] == '0')
			vec.push_back(0);
		if(a[i] != '1' && a[i] != '0')
		{
			cout << "Message cannot be encoded...\n";
			break;
		}
	}
	return vec;
}
