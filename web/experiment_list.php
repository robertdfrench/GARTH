<?php
include('includes/DbConnector.php');
header("Content-type: text/json");
mysql_connect('localhost','root','');
mysql_select_db('garth');
$sth = mysql_query("SELECT * FROM experiments");

# Start JSON object denoting an experiment_list array
echo "{\"experiment_list\": [";

$experiment_list = array();
while($experiment = mysql_fetch_assoc($sth)) {
	array_push($experiment_list, "{\"description\": \"" . $experiment['description'] . "\", \"id\": " . $experiment['id'] . "}");
}

# Add JSON objects denoting experiments to experiment_list array
echo join(",", $experiment_list);

# Close JSON object and experiment_list array
echo "]}";
?>
