#!/usr/bin/perl
open(SCAFFOLDING, ">scaffolding_data.sql");
print SCAFFOLDING "use garth;\n";

######################################################
## Generate Experiment List                                        
######################################################
my $experiment_template_string = "INSERT INTO experiments (id, user_id, execution_time, description) values (<id>, 17, 2000, '<description>');";
my @description_bank = qw(Koza Optimization Clock Mating Twin Triplet Deathmatch Quick Simple Complex Holland Style Routine Schema Paradigm Reproduction Cycle Generation Continuous);
my $current_line = "";
my $description = "";
for (my $i = 1; $i < 5; $i++) {
	$current_line = $experiment_template_string;
	$description = "";
	for (my $j = 1; $j <= 3; $j++) {
		$description .= $description_bank[int(rand(@description_bank))] . " ";
	}
	chop($description);
	
	$current_line =~ s/<id>/$i/;
	$current_line =~ s/<description>/$description/;
	
	print SCAFFOLDING $current_line . "\n";
	
}


######################################################
## Generate Process List                                        
######################################################
my $process_template_string = "INSERT INTO processes (experiment_id, system_pid, process_type, timestamp_for_start_of_execution, percent_complete) values (<experiment_id>, <system_pid>, '<process_type>', NOW(), <percent_complete>);";
my @process_types = qw(breeder judge zookeeper);
my $current_line = "";
for (my $i = 1; $i < 30; $i++) {
	$current_line = $process_template_string;
	my $system_pid = int(rand(1000));
	my $process_type = $process_types[int(rand(@process_types))];
	my $percent_complete = int(rand(1000)) / 10;
	my $experiment_id = int(rand(5)) + 1;

	$current_line =~ s/<experiment_id>/$experiment_id/;
	$current_line =~ s/<system_pid>/$system_pid/;
	$current_line =~ s/<process_type>/$process_type/;
	$current_line =~ s/<percent_complete>/$percent_complete/;

	print SCAFFOLDING $current_line . "\n";
}


close(SCAFFOLDING);
