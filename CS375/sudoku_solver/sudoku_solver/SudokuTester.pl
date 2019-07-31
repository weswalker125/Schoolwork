#!usr/bin/perl -W

# Wesley Walker
# CS375
# Project1

# Input: DIMACS file
# Action: Send to SATsolver, store results.
# 		Determine uniqueness
#			To determine uniqueness, check the solution file; for each value 
#			in the solution set, add constraint to the DIMACS file which 
#			negates a single non-designer provided value.  If any one of these
#			returns satisfiable by MiniSAT the puzzle has a Type II flaw (more
#			than one solution).  If all besides the first return unsatisfiable,
#			the puzzle is well-designed.
# Output: Well designed || Type I flaw || Type II flaw

$puzzleFile = $ARGV[0];

#remove file extension
if($puzzleFile =~ /\s*.txt/) {	
	$resultFile = substr $puzzleFile, 0, (length($puzzleFile) - 4);
}
#add new extension
$resultFile .= "_Results.txt";

#run through MiniSAT
system("./MiniSAT $puzzleFile $resultFile");

#open resulting file
$result = &ReadSATresult($resultFile);

#Extract Problem number from Result file:
if($puzzleFile =~ /(\d+)/) {
	$problemNumber = $1;}

#No solution
if($result eq "FALSE") {
	$finalOutput = "Problem $problemNumber is unsolvable\n"; 
}
#Solution exists, determine if solution is unique
elsif($result eq "TRUE") {
	$finalOutput = &Uniqueness($puzzleFile, $resultFile, $problemNumber);
}
#Could not read MiniSAT's output
else {
	$finalOutput = "An error occurred\n";
}

print "$finalOutput";


######################################################

######################################################

######################################################


sub ReadSATresult {
	#Get function parameters
	($resultFile) = @_;
	
	#read in file
	open(RESULTS, "<$resultFile");
	$firstLine = <RESULTS>;
	close(RESULTS);
	chomp($firstLine); #remove line ending (may not be necessary)

	#Determine satisfiable/unsatisfiable
	if((lc($firstLine)) eq "sat") {
		$result = "TRUE";}
	elsif(lc($firstLine) eq "unsat") {
		$result = "FALSE";}
	#If the first line is unrecognizable
	else {
		return "N/A"; }
		
	return $result;
}

######################################################

sub Uniqueness {
	#Get function parameters
	($dimacsFile, $solutionFile, $problemNumber) = @_;
	
	#get list of Designer clues
	open(DIMAC, "<$dimacsFile");
	@constraints = <DIMAC>;
	close(DIMAC);
	
	#extract lines with one value (and a 0)
	#store into @designerClues
	foreach $line (@constraints) {
		@tokens = split(' ', $line);
		if(scalar(@tokens) == 2) {
			push(@designerClues, $tokens[0]);
		}
	}
	
	#Get solution list
	open(SOLUTION, "<$solutionFile");
	@results = <SOLUTION>;
	close(SOLUTION);
	
	#extract all positive values from solution
	#store into @solution
	foreach $line (@results) {
		@tokens = split(' ', $line);
		foreach $token (@tokens) {
			if("-" ne (substr $token, 0, 1)) {
				push(@solution, $token);}
		}
	}
	
	$attempts=1;
	foreach $value (@solution) {
		#We're only looking for numberic tokens (the solutions)
		#skip line containing "SAT" or "UNSAT"
		if(chomp($value) eq "SAT" || chomp($value) eq "UNSAT") {
			next;}
		#skip solution if this is a designer provided clue
		if(grep {$_ eq $value} @designerClues) {
			next;}
			
		elsif($value =~ /^[+-]?\d+$/) {
			print "loop: $attempts\n\n";
			#add line to original dimac file
			&AddLastLine($dimacsFile, "-$value 0\n");
			
			
			#run through MiniSAT
			$outputFile = "tmpSATsolution" . "$attempts" . ".txt";
			system("./MiniSAT $dimacsFile $outputFile");
			
			#get result:
			$result = &ReadSATresult($outputFile);
			
			#if satisfiable, type II flaw
			if($result eq "TRUE") {
				return "Problem $problemNumber has a Type-II flaw\n";
			}
			
			#if unsatisfiable, remove last line of dimacs file, retry
			&RemoveLastLine($dimacsFile);
			
			#keep looping
			$attempts = $attempts + 1;
		}
	}
	return "Problem $problemNumber is well-designed\n";
}

######################################################

sub AddLastLine {
	#Get function parameter
	($fileName, $newLine) = @_;
	
	#read in file
	open(FILE, "<$fileName");
	@lines = <FILE>;
	close(FILE);
	
	#write out:
	open(FILE2, ">$fileName");
	#increment number of constraints in header:
	$header = $lines[0];
	@headerElements = split(' ', $header);
	$headerElements[3]++;
	#write header
	print FILE2 "@headerElements\n";
	#write remainder of file
	for($ct=1; $ct < (scalar(@lines)); $ct++) {
		print FILE2 "$lines[$ct]";
	}
	#write new constraint
	print FILE2 "$newLine";
	
	close(FILE2);
}

######################################################

sub RemoveLastLine {
	#Get function parameter
	($fileName) = @_;
	
	#read in file
	open(FILE, "<$fileName");
	@lines = <FILE>;
	close(FILE);
	
	#write out:
	open(FILE2, ">$fileName");
	#decriment number of constraints in header:
	$header = $lines[0];
	@headerElements = split(' ', $header);
	$headerElements[3]--;
	#write header
	print FILE2 "@headerElements\n";
	#write remainder of file (excluding last line)
	for($ct=1; $ct < (scalar(@lines) - 1); $ct++) {
		print FILE2 "$lines[$ct]";
	}
	
	close(FILE2);
}