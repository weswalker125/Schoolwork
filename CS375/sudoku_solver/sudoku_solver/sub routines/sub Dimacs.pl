sub ToDimacs {
	($row, $col, $val) = @_;
	 
	$dimacs = (81 * ($row-1)) + (9 * ($col-1)) + $val;
	
	return $dimacs;
} 