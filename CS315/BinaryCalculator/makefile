all: BonusHW
prep:
	mkdir -p bin obj

#Line rule for BonusHW
BonusHW: BonusHW.o Binaryclass.o
	g++ -o bin/BonusHW obj/BonusHW.o obj/Binaryclass.o
#Compile rule for BonusHW
BonusHW.o : prep BonusHW.cpp
	g++ -c BonusHW.cpp -o obj/BonusHW.o
#Compile rule for Binaryclass
Binaryclass.o : prep Binaryclass.cpp Binaryclass.h
	g++ -c Binaryclass.cpp -o obj/Binaryclass.o
#Delete rule to delete object modules
Delect Obj:
	rm -rf obj/ bin/
