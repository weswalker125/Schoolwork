all: load print query change clean
prep:
	mkdir -p bin obj
load: prep header.o load.o
	g++ -o bin/load obj/load.o obj/header.o
print: prep header.o print.o
	g++ -o bin/print obj/print.o obj/header.o
query: prep header.o query.o
	g++ -o bin/query obj/query.o obj/header.o
change: prep header.o change.o
	g++ -o bin/change obj/change.o obj/header.o
clean: prep header.o clean.o
	g++ -o bin/clean obj/clean.o obj/header.o
header.o: prep src/header.c src/header.h
	g++ -o obj/header.o -c src/header.c
load.o:  prep src/load.cpp src/header.h
	g++ -o obj/load.o -c src/load.cpp
print.o: prep src/print.cpp src/header.h
	g++ -o obj/print.o -c src/print.cpp
query.o: prep src/query.cpp src/header.h
	g++ -o obj/query.o -c src/query.cpp
change.o: prep src/change.cpp src/header.h
	g++ -o obj/change.o -c src/change.cpp
clean.o: prep src/clean.cpp src/header.h
	g++ -o obj/clean.o -c src/clean.cpp
DELETE:
	rm load.o load print.o print query.o query change.o change header.o header clean.o clean
