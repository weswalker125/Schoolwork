#ifndef PATIENTNODE_H
#define PATIENTNODE_H

#include <string>
using namespace std;
//Method of saving the information (formatted as "patient.data")
//into separate entities, as specified in the program description.
struct PatientNode
{
	string IDnumber, LastName, FirstName, Birthdate, Floor, DNAFile;
	PatientNode* next;
};
#endif

