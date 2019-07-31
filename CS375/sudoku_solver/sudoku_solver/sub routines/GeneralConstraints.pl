#!usr/bin/perl -W

# Script to generate all the general constraints to a sudoku puzzle
# No designer clues

# row constraints
	$rowConstraints = "";
	for($row=1; $row<10; $row++)
	{
		for($val=1; $val<10; $val++)
		{
			for($col=1; $col<10; $col++)
			{
				$rowConstraints = $rowConstraints . &ToDimacs($row,$col,$val) . " ";
			}
			
			$rowConstraints = $rowConstraints  . " 0\n";
		}
	}
	#81 constraints
	
	# column constraints
	$columnConstraints = "";
	for($col=1; $col<10; $col++)
	{
		for($val=1; $val<10; $val++)
		{
			for($row=1; $row<10; $row++)
			{
					$columnConstraints = $columnConstraints . &ToDimacs($row, $col, $val) . " ";
			}
			
			$columnConstraints = $columnConstraints  . " 0\n";
		}
	}
	#81 constraints
	
	# square constraints
	$squareConstraints = "";
	for($square=1; $square<10; $square++)
	{
		if(($square >= 1) && ($square <= 3)){
			$initialrow = 1;}
		elsif(($square >= 4) && ($square <= 6)){
			$initialrow = 4;}
		elsif(($square >= 7) && ($square <= 9)){
			$initialrow = 7;}
		
		if(($square == 1) || ($square == 4) || ($square == 7)) {
			$initialcolumn = 1;}
		if(($square == 2) || ($square == 5) || ($square == 8)) {
			$initialcolumn = 4;}
		if(($square == 3) || ($square == 6) || ($square == 9)) {
			$initialcolumn = 7;}
		
		for($val=1; $val<10; $val++)
		{	
			for($row = $initialrow; $row < $initialrow + 3; $row++)
			{
				for($col = $initialcolumn; $col < $initialcolumn + 3; $col++)
				{
					$squareConstraints = $squareConstraints . &ToDimacs($row, $col, $val) . " ";
				}
			}
			
			$squareConstraints = $squareConstraints . " 0\n";
		}
	}
	#81 constraints
	
	# one-value-per-cell constraints
	$singularConstraints = "";
	#No two values can exist in a single cell
	for($row=1; $row<10; $row++)
	{
		for($column=1; $column<10; $column++)
		{
			for($value=1; $value<10; $value++)
			{
				for($otherValue = $value+1; $otherValue<10; $otherValue++)
				{
					$singularConstraints .= ((-1) * &ToDimacs($row,$column,$value)) . " ";
					$singularConstraints .= ((-1) * &ToDimacs($row,$column,$otherValue));
					$singularConstraints .= " 0\n";
				}
			}
		}
	}
	#2916 constraints

	$generalConstraints = $rowConstraints . $columnConstraints . $squareConstraints . $singularConstraints;
	
	&WriteToFile("Constraints.txt", $generalConstraints);
	
	
sub ToDimacs {
	($row, $col, $val) = @_;
	 
	$dimacs = (81 * ($row-1)) + (9 * ($col-1)) + $val;
	
	return $dimacs;
} 

sub WriteToFile {
	($fileName, $string) = @_;
	
	open(OUTPUTFILE, ">$fileName");
	print OUTPUTFILE $string;
}