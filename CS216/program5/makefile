#Link rule for cs216program5 
cs216program5: main.o
	g++ -o cs216program5 main.o
#Compile rule for main.cpp
main.o : main.cpp EventNode.h PatientNode.h
	g++ -c main.cpp
#Delete rule to delete object modules
DeleteObj:
	rm main.o
