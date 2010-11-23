#!/usr/bin/perl
##################
# COPYRIGHT 2010 Korovasoft.
#
# See LICENSE file for details.
##################
use DBI;
my $dbh = DBI->connect("DBI:mysql:distributed_ga","root","");
my $query_string = "select 
(select AVG(fitness) from ksdga_gene_pool where fitness > -1) as \"Average Fitness\",
(select MIN(fitness) from ksdga_gene_pool where fitness > -1) as \"Minimum Fitness\",
(select MAX(fitness) from ksdga_gene_pool where fitness > -1) as \"Maximum Fitness\",
(select COUNT(*) from ksdga_gene_pool) as \"Number of Organisms\",
(select COUNT(*) from ksdga_gene_pool where fitness = -1) as \"Number of New Organisms\",
(select (MAX(id) - 1000) from ksdga_gene_pool) as \"Number of Insertions\"";
my $keys = ["Average Fitness","Minimum Fitness","Max Fitness   ","Pop","UnEval","Created"];
my $mins = ["---------------","---------------","--------------","---","------","-------"];
my $plus = ["+++++++++++++++","+++++++++++++++","++++++++++++++","+++","++++++","+++++++"];
my $mins_on = 0;
my $sth = $dbh->prepare($query_string);
while(1) {
	printHeader();
	for($i = 0; $i < 15; $i++) {
		$sth->execute();
		my $row = $sth->fetchrow_arrayref();
		printDataRow($row);
		sleep(2);
	}
}

sub printHeader {
	printArrayRef($plus);
	printArrayRef($keys);
	printArrayRef($plus);
	$mins_on = 0;
}

sub printDataRow {
	my $dataRow = shift;
	printArrayRef($mins) if $mins_on;
	printArrayRef($dataRow);
	$mins_on = 1;
}

sub printArrayRef {
	my $arrayRef = shift;
	print join("\t|",@$arrayRef) . "\n";
}
