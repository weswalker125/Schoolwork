#ifndef EVENTNODE_H
#define EVENTNODE_H

#include <string>
using namespace std;

//Method of saving the data (in the format of event.data)
//in separate entities, as specified in the program description.

struct EventNode
{
	string EventID, PatientNumber, CodeID, EventDate;
	EventNode* next;
};
#endif

