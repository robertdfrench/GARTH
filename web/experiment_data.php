<?php
include('includes/DbConnector.php');


function format_json_property($process, $property) {
	return "\"$property\": \"" . $process[$property] . "\"";
}


header("Content-type: text/json");
mysql_connect('localhost','root','');
mysql_select_db('garth');
$sth = mysql_query("SELECT id, system_pid as process_id, process_type as worker_type, NOW() - timestamp_for_start_of_execution as wall_clock_used, percent_complete FROM processes WHERE experiment_id = " . $_GET['id']);

# Start JSON object denoting a process_list array
echo "{\"process_list\": [";

$process_list = array();
while($process = mysql_fetch_assoc($sth)) {
	$property_strings = array();
	array_push($property_strings, format_json_property($process, 'process_id'));
	array_push($property_strings, format_json_property($process, 'worker_type'));
	array_push($property_strings, format_json_property($process, 'wall_clock_used'));
	array_push($property_strings, format_json_property($process, 'percent_complete'));

	array_push($process_list, "{". join(",", $property_strings) . "}");
}
echo join(",", $process_list);

#end JSON object
echo "]}";


?>
