# wDatabase 2.0

Author: Wesley Walker

Submitted: November 25th, 2012

Course: CS505

Assignment: Project 2


## BUILD/EXECUTION
The project is developed in C# on the Windows platform.  To build this project, simply open the solution file
(./src/CS505P2.sln) in Visual Studio 2012, locate the "Build" option, then execute.  To execute the project
without building it yourself, simply open the file located at (`./build/x86/CS505P2.exe OR ./build/x64/CS505P2.exe` depending on your executing platform).

The program accesses a Microsoft SQL Server 2012 installation installed on my machine, the expected 
server name is "DATAXCORE", and the expected database is "CS505DB".  These specifications are the same
as those in Project 1.

## DOCUMENTATION
The operation manual for wDatabase2 can be found at `./documentation/wDatabase_Manual.pdf`.  The manual contains screen captures of the execution process, showing the desired requirements being met upon execution.  

The code itself (located throughout ./src/*.cs) is documented in hopes of maintaining a transparent program design and implementation based on the specifications.  The bulk of logic can be found in the "Service Layer" `./src/CS505P2.Service/Services.cs` specifically in the functions `ParseUserQuery(string userQuery)` and `PopulateTablets(string filename, bool output)`. 

All user-interface logic is found in `./src/CS505P2/*.cs` and GUI design in `./src/CS505P2/*.xaml`.

## DEVELOPMENT DETAILS
This project was designed and implemented in Microsoft Visual Studio 2010, all code is creditted to myself.
The language is C# on the Windows Presentation Form (WPF) platform.  The back-end (database) was provided by Microsoft SQL Server 2012.  These platforms were chosen based on a level of comfort to the author (via the recency of use at work). 

The project CS505P2.Console is merely a driver (console application) used to test functionality of small units of the service layer during the development process.  CS505P2.Console is not neccessary for execution of the wDatabase 2.0 program.