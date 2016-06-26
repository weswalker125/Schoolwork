/* Title: cs216program5
 *
 * Author: Wesley Walker
 * Completion Date: 10/24/09
 * 
 *
 * Program 3 description:
 * The programs reads in 2 files, event.data and patient.data
 * the files are read into separate classes using the C++ 
 * Standard Template Library (#include <list>).
 * The program accomodates any number of records in the files
 * It's assumed that patient.data and event.data are in the current 
 * directory, if not, the program will display error message and terminate.
 *
 * After reading in data from the files, a menu is presented to the user,
 * from here the user decides which function to use.  Generally, each option
 * takes an input and looks from something in the class relating to it (specific
 * to each function).
 *
 *
 * Program 5 Alteration:
 * On a general scale, Program 3 was cut down, removing loops and their exits strategies
 * to provide the user with one input, and one output.  The input comes from a CGI rather
 * than directly from the user.  The output will be the standard line for the individual 
 * patient requested.  (Output to the CGI).
 *
 * Inputs are passed by "Command line arguments" from the CGI.
*/
#include <iostream>
#include <fstream>
#include <string>
#include <list>

using namespace std;

//my own headers
#include "EventNode.h"
#include "PatientNode.h"
//end headers


int SelectAnOption(string userInput, string IDNAME); //provides interface menu to user ^M
void OptionOne(string userInput); //"Option" functions perform the required
				//action selected by the user 
void findEIDs(string ID);//finds requested id
void OptionTwo(string userInput);
void OptionThree(string userInput);	
string BirthdayChange(string bday); //changes the format of birthday 

//Global "lists"
	list<EventNode> Elist;
	list<PatientNode> Plist;
	EventNode Event;		//Node to insert in list
	PatientNode Patient; //Node to insert in list
//end global "lists"

bool PNamefound, PIDfound, Efound, EIDfound, GlobalEFIND;

//iterators (global because they're used in multiple functions
	list<PatientNode>::iterator Pcurrent;
	list<PatientNode>::iterator Pstop;

	list<EventNode>::iterator Ecurrent;
	list<EventNode>::iterator Estop;
//end iterators

int main(int argc, char *argv[])
{
	//argc should be 2 parameters, the search method, and the ID/Name/Event.
	string SEARCHMETHOD = argv[1];
	//SEARCHMETOD will be a number "1", "2", or "3"
	string IDNAME = argv[2];
	//IDNAME will either be a Patient number, a Patient name, or an Event ID.
	
	string patientdotdata, eventdotdata;
	
	patientdotdata = "patient.data";
	eventdotdata = "event.data";
	
	//read in patient.data and event.data
	ifstream patientData, eventData;//1 filestream per file to open
	
	//BEGIN PATIENT LIST READ-IN
	patientData.open(patientdotdata.c_str());
	
	if(!patientData) //file not found
	{
		printf("patient.data was not found in the current directory...n");
		return 1;
 
	}
	
	//Read the first item from the file
		patientData >> Patient.IDnumber;
		patientData >> Patient.LastName;
		patientData >> Patient.FirstName;
		patientData >> Patient.Birthdate;
		patientData >> Patient.Floor;
		patientData >> Patient.DNAFile;
	//Initialize iterators to start and end of list
	Pcurrent = Plist.begin();
	Pstop = Plist.end();
	//insert
	Plist.insert(Pcurrent, Patient);	//Insert first item
	//Read next item
		patientData >> Patient.IDnumber;
		patientData >> Patient.LastName;
		patientData >> Patient.FirstName;
		patientData >> Patient.Birthdate;
		patientData >> Patient.Floor;
		patientData >> Patient.DNAFile;
		
	//While items in the file
	while(patientData)
	{
		//Initial iterators
		Pcurrent = Plist.begin();
		Pstop = Plist.end();
		//Put new items in list 
		while(Pcurrent != Pstop && Pcurrent->IDnumber < Patient.IDnumber)
			Pcurrent++;
		//Found proper place to insert new item
		//inserts in numerical order, no reason for it, just better^M
		//organization^M
		Plist.insert(Pcurrent, Patient); //Pcurrent pointing [where] to insert
		//Read next item
		patientData >> Patient.IDnumber;
		patientData >> Patient.LastName;
		patientData >> Patient.FirstName;
		patientData >> Patient.Birthdate;
		patientData >> Patient.Floor;
		patientData >> Patient.DNAFile;
	}
	patientData.close();

//Start Event Data read-in

	eventData.open(eventdotdata.c_str());
	
	if(!eventData) //file not found
	{
		printf("event.data was not found in the current directory...n");
		return 1;
	}
	
	//Read the first item from the file
		eventData >> Event.EventID;
		eventData >> Event.PatientNumber;
		eventData >> Event.CodeID;
		eventData >> Event.EventDate;
	
	//Initialize iterators to start and end of list
	Ecurrent = Elist.begin();
	Estop = Elist.end();
	//insert
	Elist.insert(Ecurrent, Event);	//Insert first item
	//Read next item
		eventData >> Event.EventID;
		eventData >> Event.PatientNumber;
		eventData >> Event.CodeID;
		eventData >> Event.EventDate;
		
	//While items in the file
	while(eventData)
	{
		//Initial iterators
		Ecurrent = Elist.begin();
		Estop = Elist.end();
		//Put new items in list 
		while(Ecurrent != Estop && Ecurrent->EventID[0] < Event.EventID[0])
			Ecurrent++;
		//Found proper place to insert new item (alphabetical)
		Elist.insert(Ecurrent, Event); //Pcurrent pointing [where] to insert
		//Read next item
			eventData >> Event.EventID;
			eventData >> Event.PatientNumber;
			eventData >> Event.CodeID;
			eventData >> Event.EventDate;
	}
	eventData.close();
	
	//Main exit strategy
	int ToExit;
	ToExit = SelectAnOption(SEARCHMETHOD, IDNAME);
	
	if(ToExit == 0)
		return 0;
	else
	{
		cout << "Something went wrong" << endl;
		return 0;
	}
}
//End main


int SelectAnOption(string userInput, string IDNAME)
{	
	if(userInput == "1")
		OptionOne(IDNAME);

	if(userInput == "2")
		OptionTwo(IDNAME);
		
	if(userInput == "3")
		OptionThree(IDNAME);
		
	return 0;
}


void OptionOne(string userInput)
{
	//Option 1 displays patient records
	//user inputs a Patient ID,
	//function finds the id, outputs the patients information
	//including any events the patient belongs to.
	GlobalEFIND = false;

	PIDfound = false;
	Efound = false;

	
		//Traverse through list looking for the input ID
		for(Pcurrent = Plist.begin(); Pcurrent != Plist.end(); Pcurrent++)
		{
			if(Pcurrent->IDnumber == userInput)
			{
				PIDfound = true;//found our patient!
				break; //breaks to keep the iterator at
			}		//its current position.
		}			//(so we can use it)
				
		if(PIDfound) //found a match
		{
			string BDAY;
			BDAY = BirthdayChange(Pcurrent->Birthdate);	//formats birthday to the preferred display
				
				cout << "Patient Number" << "&" << Pcurrent->IDnumber << endl;
				cout << "Last Name&" << Pcurrent->LastName << endl;
				cout << "First Name&" << Pcurrent->FirstName << endl;
				cout << "Birth date&" << BDAY << endl;
				cout << "Floor&" << Pcurrent-> Floor << endl;
				cout << "DNA file name&" << Pcurrent->DNAFile << endl;
				findEIDs(userInput);
				
				if(!GlobalEFIND)
					cout << "Event ID(s): " << "&" << "none";
		}

		else
			cout << "Was not found\n\n";
		
	
	return;
}

void findEIDs(string ID)
{		//Function looks for the ID in event's list that was passed by
		//either OptionOne or OptionTwo 
	int ct = 0;
	for(Ecurrent = Elist.begin(); Ecurrent != Elist.end(); Ecurrent++)
		{
			//Traverse list til you find it
			if(Ecurrent->PatientNumber == ID)
			{
			
				//if it's the first one you find, output
				//proper pre-text.
				cout << "Event ID(s): " << "&" << Ecurrent->EventID << endl;
				GlobalEFIND = true;
			}
		}
	return;
}

void OptionTwo(string userInput)
{
	//Option 2 displays data by patient name
	//user inputs a last name, function outputs
	//their patient info, (things like floor, birthday, ID number,
	//and events they belong to)
	GlobalEFIND = false;
		PNamefound = false;
		Efound = false;
		
		userInput[0] = toupper(userInput[0]);	//matches input to case of file's input.
		for(int i=1; userInput[i] != '\0'; i++)
			userInput[i] = tolower(userInput[i]);
			
			//Traverse through list looking for the input ID
			
		string ID;
		for(Pcurrent = Plist.begin(); Pcurrent != Plist.end(); Pcurrent++)
		{	//Traverse through list til end/find it.
			if(Pcurrent->LastName == userInput)
			{
				ID = Pcurrent->IDnumber;
				PNamefound = true;
				break;
			}
		}
				
		if(PNamefound) //found a match
		{
			string BDAY;
			BDAY = BirthdayChange(Pcurrent->Birthdate);	//formats birthday to the preferred display
				
			cout << "Patient Number&" << Pcurrent->IDnumber << endl;
			cout << "Last Name&" << Pcurrent->LastName << endl;
			cout << "First Name&" << Pcurrent->FirstName << endl;
			cout << "Birth date&" << BDAY << endl;
			cout << "Floor&"  << Pcurrent-> Floor << endl;
			cout << "DNA file name&" << Pcurrent->DNAFile << endl;
			findEIDs(ID);

			if(!GlobalEFIND)
				cout << "Event ID(s): " << "&" << "none";
		}
		else
			cout << "Was not found...\n\n";
	return;
}

void OptionThree(string userInput)
{
	//Option 3 displays data by Event IDs
	//user inputs an event id
	//function searches for id and output's the remaining information
	//of the event (which includes the patient's number)
	EIDfound = false;
	Efound = false;

		if(userInput[0] > 64 && userInput[0] < 123)
		{
			//this limits the possible ascii values inputted to
			//mainly alphabetical characters.  (eliminates
			//a hefty amount of possible bad input.)
			userInput[0] = toupper(userInput[0]); //allows the user to input lower or uppercase, doesn't matter.

			//Traverse through list looking for the input ID
			for(Ecurrent = Elist.begin(); Ecurrent != Elist.end(); Ecurrent++)
			{
				if(Ecurrent->EventID == userInput)
				{
					EIDfound = true;//found it!
					break;
				}
			}
			if(EIDfound)//if we obtained a match
			{
				for(Pcurrent = Plist.begin(); Pcurrent != Plist.end(); Pcurrent++)
				{	//Traverse list to find matching patient to the event.
					if(Pcurrent->IDnumber == Ecurrent->PatientNumber)
					{
						Efound = true;
						break;
					}
				}
			}
			if(Efound) //found a match
			{
				string EventDate;
				EventDate = BirthdayChange(Ecurrent->EventDate);	//formats birthday to the preferred display

				cout << "Event ID" << "&" << Ecurrent->EventID << endl;
				cout << "Patient Number" << "&" << Ecurrent->PatientNumber << endl;
				cout << "Code ID" << "&" << Ecurrent->CodeID << endl;
				cout << "Event date" << "&" << EventDate << endl;
				
			}
			else
				cout << "Event ID not found\n";
		}
		else
			cout << "bad input...\n";
	
	return;
}

string BirthdayChange(string bday) //changes the format of birthday 
{	
	//Pre format: "01/24/1990"
	//Post format: "January 24 1990"
	string month, day, year, total;
	if(bday[0] == '1')
	{
		if(bday[1] == '/')
		{
			month = "January ";
			if(bday[2] == '0'){
				day = bday[3];
				year = bday.substr(5);}
			else
			{
				day = bday.substr(2,2);
				year = bday.substr(5);
			}
		}
		if(bday[1] == '0')
		{
			month = "October ";
			if(bday[3] == '0'){
				day = bday[4];
				year = bday.substr(6);}
			else
			{
				day = bday.substr(3,2);
				year = bday.substr(6);
			}
		}
		if(bday[1] == '1')
		{
			month = "November ";
			if(bday[3] == '0'){
				day = bday[4];
				year = bday.substr(6);}
			else
			{
				day = bday.substr(3,2);
				year = bday.substr(6);
			}
		}
		if(bday[1] == '2')
		{
			month = "December ";
			if(bday[3] == '0'){
					day = bday[4];
					year = bday.substr(6);}
			else
			{
				day = bday.substr(3,2);
				year = bday.substr(6);
			}
		}
	}
	if(bday[0] == '2')
	{
		month = "February ";
		if(bday[2] == '0'){
				day = bday[3];
				year = bday.substr(5);}
			else
			{
				day = bday.substr(2,2);
				year = bday.substr(5);
			}
	}
	if(bday[0] == '3')
	{
		month = "March ";
		if(bday[2] == '0'){
				day = bday[3];
				year = bday.substr(5);}
			else
			{
				day = bday.substr(2,2);
				year = bday.substr(5);
			}
	}
	if(bday[0] == '4')
	{
		month = "April ";
		if(bday[2] == '0'){
				day = bday[3];
				year = bday.substr(5);}
			else
			{
				day = bday.substr(2,2);
				year = bday.substr(5);
			}
	}
	if(bday[0] == '5')
	{
		month = "May ";
		if(bday[2] == '0'){
				day = bday[3];
				year = bday.substr(5);}
			else
			{
				day = bday.substr(2,2);
				year = bday.substr(5);
			}
	}
	if(bday[0] == '6')
	{
		month = "June ";
		if(bday[2] == '0'){
				day = bday[3];
				year = bday.substr(5);}
			else
			{
				day = bday.substr(2,2);
				year = bday.substr(5);
			}
	}
	if(bday[0] == '7')
	{
		month = "July ";
		if(bday[2] == '0'){
				day = bday[3];
				year = bday.substr(5);}
			else
			{
				day = bday.substr(2,2);
				year = bday.substr(5);
			}
	}
	if(bday[0] == '8')
	{
		month = "August ";
		if(bday[2] == '0'){
				day = bday[3];
				year = bday.substr(5);}
			else
			{
				day = bday.substr(2,2);
				year = bday.substr(5);
			}
	}
	if(bday[0] == '9')
	{
		month = "September ";
		if(bday[2] == '0'){
				day = bday[3];
				year = bday.substr(5);}
			else
			{
				day = bday.substr(2,2);
				year = bday.substr(5);
			}
	}
	total = month + day + " " + year;
	return total;
}
