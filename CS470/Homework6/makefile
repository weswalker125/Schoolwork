all: load print query change clean
load: header.o load.o
	g++ -o load load.o header.o
print: header.o print.o
	g++ -o print print.o header.o
query: header.o query.o
	g++ -o query query.o header.o
change: header.o change.o
	g++ -o change change.o header.o
clean: header.o clean.o
	g++ -o clean clean.o header.o
header.o: header.c header.h
	g++ -c header.c
load.o: load.cpp header.h
	g++ -c load.cpp
print.o: print.cpp header.h
	g++ -c print.cpp
query.o: query.cpp header.h
	g++ -c query.cpp
change.o: change.cpp header.h
	g++ -c change.cpp
clean.o: clean.cpp header.h
	g++ -c clean.cpp
DELETE:
	rm load.o load print.o print query.o query change.o change header.o header clean.o clean
