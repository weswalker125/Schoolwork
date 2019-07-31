#include "Binaryclass.h"

/*
   DecToBin
   i: decimal number
   o: binary vector
   Function takes in the Decimal representation of a given number, the converts
   said number to a binary string (stored as a vector), using the common procedure
   of "divide by 2... what's the remainder? (repeat)"
*/
vector<int> Binary::DecToBin(int decimal)
{
	vector<int> temp, binary; //temp stores the string initially (but backwards)
	
	if(decimal < 0) { //if the input is negative it probably was larger than INT_MAX
		cout << "decimal number invalid, probable overflow \n";
		binary.push_back(0); 
		binary.push_back(0); 
		return binary; }//return a 0 vector "00"
	
	if(decimal == 0) { //return a 0 vector "00"
		binary.push_back(0); 
		binary.push_back(0); 
		return binary; }
	
	if(decimal == 1) { //return a 1 vector "01"
		binary.push_back(0); 
		binary.push_back(1); 
		return binary; }
	
	while(decimal != 0) { //when it equals 0, we've divided all we can.
		temp.push_back(decimal % 2);  //either a 0 or a 1
		decimal = decimal / 2; 
		binary.push_back(0); } //push a 0 so binary's size is equal to temp's.
	
	for(unsigned int i = 0; i < temp.size(); i++) { //take the reverse of temp
		binary[i] = temp[(temp.size() - (i+1))]; } // store as binary
	
	return binary; //return binary
}//END DecToBin
/*-----------------------------------------------------------------------------*/

/* 
   BinToDec
   i: vector containing a "binary array" (not an array literally) type: int
   o: decimal representation of the said binary input
*/
vector<int> Binary::BinToDec(vector<int> binary)
{
	vector<int> temp, temp2, one, two, three, digit, zero, newBinary, FINAL;
	//digit holds significant digit in the binary
	//newBinary doesn't have leading zeroes
	//FINAL is returned 
	one.push_back(1); two.push_back(2); three.push_back(3);
	//one holds vector containing "1"
	//two holds vector containing "2"
	//three holds vector containing "3"
	vector<int> BinTwo;
	BinTwo.push_back(1);
	BinTwo.push_back(0);
	//BinTwo holds vector of binary representation of "2" (01)
	
	unsigned int ct = 0; 
	int ZERO = 0;  //counts 0's before significant digit
	bool found = false; 
	
	while( ct < binary.size()) { //find the first significant digit
		if(binary[ct] == 0 && !found) {
			ZERO++; }
		else 
			found = true;
		ct++; }
	
	if(!found) {
		vector<int> zero;
		zero.push_back(0);
		return zero; }
		
	//eliminate leading zeros
	for(unsigned int i = 0; i < binary.size() - ZERO; i++) 
		newBinary.push_back(binary[i + ZERO]); 

	//Special cases (2-bit results)
	if(newBinary.size() == 1)
		return one;
	if(newBinary.size() == 2) {
		vector<int> BinTwo;
		BinTwo.push_back(1);
		BinTwo.push_back(0);

		vector<int> BinThree;
		BinThree.push_back(1);
		BinThree.push_back(1);

		if(newBinary == BinTwo)
			return two;
		if(newBinary == BinThree)
			return three; }
	//End special cases

	digit.push_back(newBinary[1]); 
	temp = DecAdd(two, digit); 

	//converting to decimal
	for(unsigned int i = 1; i < newBinary. size() - 1; i++) {
		temp2 = TimesTwo(temp);  //temp*2
		if(newBinary[i+1] == 1)
			temp2 = DecAdd(one, temp2);
		temp = temp2; } //store that back as temp
	
	//eliminate leading zeroes
	ct = 0; 
	ZERO = 0; 
	found = false; 
	
	while(ct < temp2.size()) { //while less than temp2's size
		if(temp2[ct] == 0 && !found) //find first significant digit
			ZERO++;
		else 
			found = true; //found it!
		ct++; }
	
	//new vector with just significant portion of binary string
	for(unsigned int i = 0; i < temp2.size() - ZERO; i++) 
		FINAL.push_back(temp2[i + ZERO]);
		
	
	return FINAL;
}//END BinToDec function
/*-----------------------------------------------------------------------------*/

/*
   BinMultiply
   i: two binary vectors,
   o: the product of the two inputs

   multiplication function developed in Homework2
   detailed explanation of algorithm is found in Homework2's PDF
*/
vector<int> Binary::BinMultiply(vector<int> one, vector<int> two)
{
	vector<int> add, product; 

	if(one.size() != two.size()) //if binaries are of unequal size
	{
		vector<int> bigger, smaller; 
		if(one.size() > two.size()) //determine which is larger
		{
			smaller = two; 
			bigger = one; 
		}
		else //determine which is larger
		{
			smaller = two; 
			bigger = one; 
		}
		
		product.push_back(0); 
		
		//traverse through smaller list, (right to left)
		for(unsigned int i = 0; i < smaller.size(); i++)
		{
			if(smaller[smaller.size() - (i+1)] == 1)
			{
				add = bigger;   //only keep current sum and next to add
				for(unsigned int ct = 0; ct < i; ct++) {
					add.push_back(0); }
				product = BinAdd(add, product);  //sum up current and next to add
			}
		}
	}
	
	else
	{
		product.push_back(0); 
		
		//traverse through list, (right to left)
		for(unsigned int i = 0; i < one.size(); i++)
		{
			if(one[one.size() - (i+1)] == 1)
			{
				add = two; 
				for(unsigned int ct = 0; ct < i; ct++){
					add.push_back(0); }
				product = BinAdd(add, product); 
			}
		}
	}
	product = EliminateLeadingZero(product); //Eliminate leading zeros 
											//(leave significant portion only)
	return product;
} //END BinMultipy
/*-----------------------------------------------------------------------------*/


/*
   TimesTwo
   i: int vector (decimal representation of number)
   o: input vector "multiplied" by 2;
   used to convert from binary to decimal
*/
vector<int> Binary::TimesTwo(vector<int> pre)
{	//(multiplying by two)
	vector<int> carry, PRODUCT;
	int product;

	for(unsigned int i = 0; i <= pre.size(); i++) {
		carry.push_back(0);   //initialize PRODUCT and carry
		PRODUCT.push_back(0); }

	for(unsigned int i = 0; i < pre.size(); i++) {
		product = 2*(pre[pre.size() - (i+1)]) + carry[carry.size() - (i+1)]; 
		PRODUCT[PRODUCT.size() - (i+1)] = product % 10; 
		carry[PRODUCT.size() - (i+2)] = product / 10; }

	PRODUCT[0] = carry[0]; 
	return PRODUCT;
}//END TimesTwo
/*-----------------------------------------------------------------------------*/

/*
   DecAdd
   i: two vectors containing integers (decimals, not binary)
   o: the sum of the two vectors
   This function adds vectors of integers, it's an alternative way of 
   adding numbers when the numbers can grow VERY large. Reasoning:
   INT_MAX is 2^31, we'll be needing to add larger numbers, easiest way 
   to store them is in a vector.
*/
vector<int> Binary::DecAdd(vector<int> one, vector<int> two)
{
	vector<int> result, carry, smaller, bigger;
	int sum; //each sum (per index)
	
	//if one and two are not of equal length
	if(one.size() != two.size()) {
		//one is larger than two
		if(one.size() > two.size()) {
			bigger = one; 
			smaller = two; }
		//otherwise two is larger
		else {
			bigger = two; 
			smaller = one; }
		
		//initialize result and carry
		for(unsigned int i = 0; i <= bigger.size(); i++) {	
			carry.push_back(0); 
			result.push_back(0); }
		
		unsigned int ct = 0; 
		//sum up til you reach the end of the smaller vector
		while(ct < smaller.size()) {
			sum = smaller[smaller.size() - (ct+1)] + bigger[bigger.size() - (ct+1)] + carry[carry.size() - (ct+1)];
			result[result.size() - (ct+1)] = sum % 10;
			carry[result.size() - (ct+2)] = sum / 10;
			ct++; }
		
		while(ct < bigger.size()) { //the remainder of the list is the larger vector and carry
			sum = bigger[bigger.size() - (ct+1)] + carry[carry.size() - (ct+1)];
			result[result.size() - (ct+1)] = sum % 10;
			carry[result.size() - (ct+2)] = sum / 10;
			ct++; }
		
		result[0] = carry[0]; 
	}//END - if one and two are not of equal length
	
	//else, one and two are of equal length
	else {
		for(unsigned int i = 0; i <= one.size(); i++) {
			carry.push_back(0);  
			result.push_back(0); }
		unsigned int ct = 0; 
		while(ct < two.size()) {
			sum = one[one.size() - (ct+1)] + two[two.size() - (ct+1)] + carry[carry.size() - (ct+1)];
			result[result.size() - (ct+1)] = sum % 10;
			carry[result.size() - (ct+2)] = sum / 10;
			ct++; }
		while(ct < one.size()) {
			sum = one[one.size() - (ct+1)] + carry[carry.size() - (ct+1)];
			result[result.size() - (ct+1)] = sum % 10;
			carry[result.size() - (ct+2)] = sum / 10;
			ct++; }
		
		result[0] = carry[0]; 
	}//END - else, one and two are of equal length
	
	return result;
}//END DecAdd function
/*-----------------------------------------------------------------------------*/

/* 
   BinAdd
   i: Two vectors containing a "binary array" (not an array literally) type: int
   o: One vector holding the binary representation of the sum of the inputs.
*/
vector<int> Binary::BinAdd(vector<int> one, vector<int> two)
{
	//Vector Definitions:
	//"SUM" holds the sum of the two inputs (the returned value)
	//"carry" is a vector which holds the leftovers when adding, 
	//	e.g.:  (1 + 1) = 0 with a carry of 1.
	//"smaller" contains and alters the smaller of the two input vectors (increase size)
	vector<int> SUM, carry, bigger, smaller, BinSum;
	int sum = 0;
	unsigned int place = 1;

	if(one.size() != two.size()) { //incase the two imputs are of different lengths
		if(two.size() > one.size()) { //determine which is longer
			bigger = two; 
			smaller = one; }

		else { //determine which is longer
			bigger = one; 
			smaller = two; }

		//Add two vectors where the sizes aren't equal
		for(unsigned int i = 0; i <= bigger.size(); i++) {
			carry.push_back(0);  //initialize both vectors
			SUM.push_back(0); }

		while(place <= smaller.size()) { //traverse the smaller list (til you reach the end)
			sum = smaller[smaller.size() - place] + 
				bigger[bigger.size() - place] + carry[carry.size() - place];
			 	//add each corresponding index of the three strings
			BinSum = DecToBin(sum); 
			//convert sum from decimal to binary
			
			SUM[SUM.size() - place] = BinSum[1];  //store the sum 
			carry[carry.size() - (place + 1)] = BinSum[0];  //carry if needed
			place++; }
		
		while(place <= bigger.size()) { //Traverse remaining indexes (rest of big list)
		//same procedure as above
			sum = carry[carry.size() - place] + bigger[bigger.size() - place]; 
			BinSum = DecToBin(sum); 
			
			SUM[SUM.size() - place] = BinSum[1]; 
			carry[carry.size() - (place+1)] = BinSum[0]; 
			place++; }
		
		SUM[SUM.size() - place] = carry[carry.size() - place]; 
	} //END "if size not equal"
	
	else { //the sizes are equal
		for(unsigned int i = 0; i < one.size() + 1; i++) {
			carry.push_back(0);  //initialize vectors
			SUM.push_back(0); }
		
		while(place <= one.size()) { //same procedure as above, just no need to 
									// traverse the "remaining" index of the list
			sum = one[one.size() - place] + two[two.size() - place] + carry[carry.size() - place];
			 
			BinSum = DecToBin(sum); 
			SUM[SUM.size() - place] = BinSum[1]; 
			carry[carry.size() - (place+1)] = BinSum[0]; 
			place++; }
		
		SUM[SUM.size() - place] = carry[carry.size() - place]; 
		//if there's a carry at the end, its the farmost left digit
	}
	
	return SUM;
}//END BinAdd
/*-----------------------------------------------------------------------------*/

/*
   BinSub **NOTE : b - a **
   i: two vectors a, and b... b must be larger than a
   o: the result of b-a
   NOTE: This method of subtraction was gathered from 
   http://www.wisc-online.com/objects/ViewObject.aspx?ID=TMH3503
   I had not heard of this method to subtract, but all the code supplied 
   was by MYSELF, only the algorithm-method was learned from the provided
   website
*/
vector<int> Binary::BinSub(vector<int> a, vector<int> b)
{	//subract a from b (b must be larger)
	vector<int> complement, result, zero, one, temp;
	one.push_back(1); zero.push_back(0);
	a = EliminateLeadingZero(a);
	//if a == 0, just return b
	if(a.size() == 0 || a == zero)
		return b;
	//if a and b are not of equal size
	if(a.size() < b.size()) {
		//initialize a vector of b's size
		for(unsigned int i = 0; i < b.size(); i++)
			temp.push_back(0);
		int tempct = temp.size()-1;
		//store a's info in new vector with leading zeros
		if(EliminateLeadingZero(a) == one)
			temp[temp.size() - 1] = a[0];

		for(unsigned int i = a.size() - 1; i > 0; i--) {
			temp[tempct] = a[i];
			tempct--;
			if(i == 1)
				temp[tempct] = a[0]; }
		a = temp; //store new vector in a
	}//now they're of equal size

	//easier condition, if both vectors are the same binary number
	if(a == b) {
		vector<int> zero;
		zero.push_back(0);
		return zero; }
	if(a.size() < b.size())
		//initialize complement to be the same size as b
		for(unsigned int i = 0; i < b.size(); i++)
			complement.push_back(0);
	
	else
		for(unsigned int i = 0; i < a.size(); i++)
			complement.push_back(0);


	//create the complement of a (0's become 1's, 1's become 0's)
	for(unsigned int i = a.size() - 1; i > 0; i--) {
		if(i == 1) {
			if(a[0] == 1)
				complement[0] = 0;
			else
				complement[0] = 1; }
		if(a[i] == 1)
			complement[i] = 0;
		else
			complement[i] = 1;
	}
	//Binary subtraction is (essentially) the addition of the first number, and the other's complement
	result = BinAdd(complement, b);	
	//if we have a carry, the subtraction's result is positive
	if(result[0] == 1) {
		result[0] = 0;
		result = BinAdd(result, one); }
	
	//if no carry was developed, the result is negative
	else {
		vector<int> neg;
		neg.push_back(-1);
		return neg; }

	return result;
}//END BinSub
/*-----------------------------------------------------------------------------*/

/*
   BinDiv **Remainder is correct, quotient needs repair**
   i: a dividend and divisor (binary vectors)
   o: (in this case) the remainder of the division
   Essentially this is a full functioning division implementation,
   but for this program the quotient is not needed, so only the remainder
   is returned.  So basically this is acting like a mod function. 

   *I thought it'd be more worthwhile to develop a full division function
   incase I need it in a future program*
*/
Result Binary::BinDiv(vector<int> dividend, vector<int> divisor)
{
	Result final;
	vector<int> temp, quotient, remainder, neg, zero, one;
	zero.push_back(0); //zero vector (for return case)
	neg.push_back(-1); //neg is the value returned if subtraction returns negative
	one.push_back(1); //one vector (for return case)

	//waste of time to go through all of this if it's mod 1
	if(divisor == one)
	{
		final.quotient = dividend;
		final.remainder = zero;
		return final;
	}

	//if Divisor is larger than dividend:
	if(divisor.size() > dividend.size()) {
		vector<int> temp2;
		for(unsigned int i = 0; i < divisor.size() - dividend.size(); i++)
			temp2.push_back(0); //zeros in front
		for(unsigned int i = 0; i < dividend.size(); i++)
			temp2.push_back(dividend[i]);//the remaining portion is the dividend
		dividend = temp2; } //store back as dividend
	//divisor and dividend are same size (crucial for subtraction to return correct remainder)


	//create portion to subtract from (long division technique)
	unsigned int i = 0;
	while(i < divisor.size()) {
		temp.push_back(dividend[i]);
		i++; }
	
	while(i <= dividend.size()) {
		//subtract (long division technique
		remainder = BinSub(divisor, temp);		
		while(remainder == neg) {
			if(i >= dividend.size()) {
				if(remainder == neg)
				{
					quotient.push_back(0);
					final.quotient = quotient;
					final.remainder = temp;
					return final;
				}
				final.quotient = quotient;
				final.remainder = remainder;
				return final; }

			temp.push_back(dividend[i]); //bring down next number from dividend
			quotient.push_back(0); //didn't divide in whole
			i++;	
			
			remainder = BinSub(divisor, temp);
			if(i >= dividend.size()) { //we've reached the end
				
				if(remainder == neg) // if it's negative, we've gone too far
				{
					quotient.push_back(0);
					final.quotient = quotient;
					final.remainder = temp;
					return final; 
				}
				quotient.push_back(1);
				final.quotient = quotient;
				final.remainder = remainder; 
				return final; } //return
		}
				
		quotient.push_back(1); //successful divide
		temp = remainder; //part to subtract from (long division technique)
		if(i == dividend.size()) { //we've reached the end
			if(remainder == neg)
			{
				final.quotient = quotient;
				final.remainder = zero;
				return final; 
			}
			final.quotient = quotient;
			final.remainder = remainder; 
			return final; }
		
		temp.push_back(dividend[i]);
		//eliminate leading zeros
		temp = EliminateLeadingZero(temp);
		i++;
	}
	//something didn't happen correctly
	final.remainder = zero;
	return final;
}//END BinDiv
/*-----------------------------------------------------------------------------*/

/*
   EliminateLeadingZero
   i: vector of binary number,
   o: vector representing the input, without the unnecessary
   leading zeros... ("Them pesky zeros!")
*/
vector<int> Binary::EliminateLeadingZero(vector<int> input)
{
	//eliminate leading zeros on temp
	unsigned int ct = 0;
	int ZERO = 0;
	bool found = false;
	while(ct < input.size()) { //traverse the input
		if(input[ct] == 0 && !found)
			ZERO++;	//find out how many leading zeros there are
		else
			found = true; //Found a zero
		ct++; }
		
	if(!found) {
		vector<int> zero;
		zero.push_back(0);
		return zero; }

	vector<int> temp;
		for(unsigned int j = 0; j < input.size() - ZERO; j++)
			temp.push_back(input[j + ZERO]); //only push back significant binaries
		input = temp;
		return input;
}//END EliminateLeadingZero
/*-----------------------------------------------------------------------------*/

/*
   modexp
   i: a base, exponent and number to mod by
   o: vector representing the result of x^y mod N
*/
vector<int> Binary::modexp(vector<int> base, vector<int> expo, vector<int> mod)
{
	//initialize one in binary ("1");
	vector<int> one; one.push_back(1);
	
	vector<int> result = one;
	Result divide;
	
	for(unsigned int i = 0; i < expo.size(); i++)
	{
		//Even case
		if(expo[i] == 0)
		{
			result = BinMultiply(result, result);
			divide = BinDiv(result, mod);
			result = divide.remainder;
		}
		
		//Odd case
		else
		{
			result = BinMultiply(result, result);
			result = BinMultiply(result, base);
			divide = BinDiv(result, mod);
			result = divide.remainder;
		}
		result = EliminateLeadingZero(result);
	}
	
	return result;
}//END modexp
/*-----------------------------------------------------------------------------*/

/*
	outputVector
	i: integer vector, a, regardless of being binary or decimal
	o: cout's the vector in order.
*/
void Binary::outputVector(vector<int> a)
{
	for(unsigned int i = 0; i < a.size(); i++)	
		cout << a[i];
}


double Binary::BinToInt(vector<int> a)
{
	double decimal = 0;
	const double two = 2;

	for(unsigned int i = 0; i < a.size(); i++)
	{
		if(a[i] == 1)
			decimal = decimal + a[i]*pow(two, ((double) a.size() - (1+i)));
	}
	return decimal;
}
