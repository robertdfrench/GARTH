#!/usr/bin/perl
open(SCAFFOLDING, ">scaffolding_data.sql");
print SCAFFOLDING "use garth;\n";
my $template_string = "INSERT INTO processes (system_pid, process_type, timestamp_for_start_of_execution, percent_complete) values (<system_pid>, '<process_type>', NOW(), <percent_complete>);";
my @process_types = qw(breeder judge zookeeper);
for (my $i = 1; $i < 30; $i++) {
	my $current_line = $template_string;
	my $system_pid = int(rand(1000));
	my $process_type = $process_types[int(rand(@process_types))];
	my $percent_complete = int(rand(1000)) / 10;

	$current_line =~ s/<system_pid>/$system_pid/;
	$current_line =~ s/<process_type>/$process_type/;
	$current_line =~ s/<percent_complete>/$percent_complete/;

	print SCAFFOLDING $current_line . "\n";
}


close(SCAFFOLDING);
