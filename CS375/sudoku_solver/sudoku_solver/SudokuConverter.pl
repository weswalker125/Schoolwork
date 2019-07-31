#!usr/bin/perl -W

# Wesley Walker
# CS375
# Project1

# Input: Designer clues puzzle file - an ASCII file consisting of 
#		Sudoku clues in the format place(r,c,v).
# Output: DIMACS file including general Sudoku constraints
#		as well as the DIMACS representation of the designer clues.

#arguments passed at command line execution:
$puzzleFile = $ARGV[0];

#get general constraints
($generalConstraints, $numberOfGeneralConstraints) = &GenerateGeneralConstraints;

#General only flag:
if($ARGV[0] eq "-g") {
	#generate only the general sudoku constraints
	&WriteToFile("GeneralSudokuConstraints.txt", $generalConstraints);
	exit;
}

#get specific constraints
($specificConstraints, $numberOfSpecificConstraints) = &GenerateSpecificConstraints($puzzleFile);

#total number of constraints (for header to DIMACS file)
$totalNumberOfConstraints = ($numberOfSpecificConstraints + $numberOfGeneralConstraints);

#output all constraints to file
#remove file extension
if($puzzleFile =~ /\s*.txt/) {
	$txt = ".txt";
	$empty = "";
	
	$puzzleFile =~ s/$txt/$empty/g;
}
#add new extension
$puzzleFile .= "_Constraints.txt";

#header to SATsolver input file
$header = "p cnf 729 $totalNumberOfConstraints\n";

#store whole file
$allConstraints = $header . $specificConstraints . $generalConstraints;

#Write
&WriteToFile($puzzleFile, $allConstraints);



######################################################

######################################################

######################################################

sub WriteToFile {
	#Get function parameters
	($fileName, $string) = @_;
	#open
	open(OUTPUTFILE, ">$fileName");
	#write
	print OUTPUTFILE $string;
	close(OUTPUTFILE);
}

######################################################

sub ToDimacs {
	#Get function parameters
	($row, $col, $val) = @_;
	#Compute dimacs representation
	$dimacs = (81 * ($row-1)) + (9 * ($col-1)) + $val;
	return $dimacs;
} 

######################################################

sub GenerateSpecificConstraints {
	#Get function parameter
	($designerFile) = @_;
	$specificConstraints = "";

	#read in file
	open(DESIGNERCLUES, "<$designerFile");
	@lines = <DESIGNERCLUES>;
	close(DESIGNERCLUES);
	
	#get total number of specific constraints
	$numberOfLines = @lines;
	
	#Get row/column/value on each clue
	foreach $line (@lines) {
		$row = substr $line, 6, 1;
		$column = substr $line, 8, 1;
		$value = substr $line, 10, 1;

		#add dimacs constraints to string
		$specificConstraints .= &ToDimacs($row, $column, $value) . " 0\n";
	}
	
	#return the dimacs constraints
	return ($specificConstraints, $numberOfLines);
}

######################################################

sub GenerateGeneralConstraints {
	$numberct = 0;
	
	# row constraints
	$rowConstraints = "";
	for($row=1; $row<10; $row++) {
		for($val=1; $val<10; $val++) {
			for($col=1; $col<10; $col++) {
				$rowConstraints .= &ToDimacs($row,$col,$val) . " ";
			}
			
			$rowConstraints .= " 0\n"; #add 0 to denote end of constraint
			$numberct++; #Count the number of constraints generated
		}
	}
	#81 constraints
	
	# column constraints
	$columnConstraints = "";
	for($col=1; $col<10; $col++) {
		for($val=1; $val<10; $val++) {
			for($row=1; $row<10; $row++) {
					$columnConstraints .= &ToDimacs($row, $col, $val) . " ";
			}
			
			$columnConstraints .= " 0\n"; #add 0 to denote end of constraint
			$numberct++; #Count the number of constraints generated
		}
	}
	#81 constraints
	
	# square constraints
	$squareConstraints = "";
	for($square=1; $square<10; $square++) {
		#Determine which row to start with
		if(($square >= 1) && ($square <= 3)){
			$initialrow = 1;}
		elsif(($square >= 4) && ($square <= 6)){
			$initialrow = 4;}
		elsif(($square >= 7) && ($square <= 9)){
			$initialrow = 7;}
		
		#Determine which column to start with
		if(($square == 1) || ($square == 4) || ($square == 7)) {
			$initialcolumn = 1;}
		if(($square == 2) || ($square == 5) || ($square == 8)) {
			$initialcolumn = 4;}
		if(($square == 3) || ($square == 6) || ($square == 9)) {
			$initialcolumn = 7;}
		
		for($val=1; $val<10; $val++) {	
			for($row = $initialrow; $row < $initialrow + 3; $row++) {
				for($col = $initialcolumn; $col < $initialcolumn + 3; $col++) {
					$squareConstraints .= &ToDimacs($row, $col, $val) . " ";
				}
			}
			
			$squareConstraints .= " 0\n"; #add 0 to denote end of constraint
			$numberct++;#Count the number of constraints generated
		}
	}
	#81 constraints
	
	# one-value-per-cell constraints
	$singularConstraints = "";
	#No two values can exist in a single cell
	for($row=1; $row<10; $row++) {
		for($column=1; $column<10; $column++) {
			for($value=1; $value<10; $value++) {
				for($otherValue = $value+1; $otherValue<10; $otherValue++) {
					#Generally, this is listing all combinations in 9C2
					$singularConstraints .= ((-1) * &ToDimacs($row,$column,$value)) . " ";
					$singularConstraints .= ((-1) * &ToDimacs($row,$column,$otherValue));
					$singularConstraints .= " 0\n"; #add 0 to denote end of constraint

					$numberct++; #Count the number of constraints generated
				}
			}
		}
	}
	#2916 constraints

	#Combine constraints into one variable
	$generalConstraints = $rowConstraints . $columnConstraints . $squareConstraints . $singularConstraints;
	
	return ($generalConstraints, $numberct);
}