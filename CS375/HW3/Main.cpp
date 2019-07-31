#include <iostream>
#include <fstream>
#include <string>

using namespace std;

int ToDimax(int edge, int color);

int main()
{
	ofstream outputFile;
	outputFile.open("Homework3SATconstraints.txt");

	outputFile << "p cnf 30 70" << endl;
	
	//Clauses that every edge has a color (15):
	for(int edge = 1; edge < 16; edge++)
	{
		for(int color = 1; color < 3; color++)
		{
			outputFile << ToDimax(edge, color) << " ";
		}
		outputFile << "0" << endl;
	}
	
	//Clauses that every edge has only one color (15)
	for(int edge = 1; edge < 16; edge++)
	{
		for(int color = 1; color < 3; color++)
		{
			outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		}
		outputFile << "0" << endl;
	}

	//Clauses that no triangle is all Red then blue (40)
	int edge, color;
	for(color = 1; color <3; color++)
	{
		edge = 1;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 2;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 4;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 1;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 7;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 5;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 1;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 10;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 8;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;

		
		edge = 1;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 13;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 11;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;
		

		edge = 4;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 7;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 14;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 4;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 10;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 3;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 4;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 13;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 6;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 7;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 10;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 9;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 7;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 13;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 12;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;

		
		edge = 10;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 13;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 15;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 2;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 5;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 14;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 2;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 8;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 3;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 2;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 11;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 6;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 5;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 8;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 9;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;

		
		edge = 5;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 11;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 12;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 8;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 11;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 15;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 14;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 3;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 9;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 14;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 6;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 12;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 3;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 6;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 15;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;


		edge = 9;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 12;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		edge = 15;
		outputFile << ((-1)*(ToDimax(edge, color))) << " ";
		outputFile << "0" << endl;
	}
}

int ToDimax(int edge, int color)
{
	int result = (edge - 1)*2 + (color - 1) + 1;
	return result;
}