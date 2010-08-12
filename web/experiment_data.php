<?php
/* Scaffolding file to feed fake content to UI
   Responds differently based on 'id' CGI variable
   This is just to provide some data to help construct UI*/
header('Content-type: text/json');

if ($_GET['id'] == 0) { ?>
{
	"process_list": [
		{"process_id": "1234", "worker_type": "judge", "cpu_usage": "90%", "wall_clock_used": "12.3s","%_complete": "61%"},
		{"process_id": "1235", "worker_type": "judge", "cpu_usage": "91%", "wall_clock_used": "12.3s","%_complete":  "61%"},
		{"process_id": "1236", "worker_type": "judge", "cpu_usage": "89%", "wall_clock_used": "12.3s","%_complete":  "61%"},
		{"process_id": "1237", "worker_type": "judge", "cpu_usage": "89%", "wall_clock_used": "12.4s","%_complete":  "62%"},
		{"process_id": "1238", "worker_type": "judge", "cpu_usage": "90%", "wall_clock_used": "12.4s","%_complete":  "62%"},
		{"process_id": "1239", "worker_type": "judge", "cpu_usage": "91%", "wall_clock_used": "12.4s","%_complete":  "62%"},
		{"process_id": "1240", "worker_type": "breeder", "cpu_usage": "81%","wall_clock_used":  "10.3s","%_complete":  "70%"},
		{"process_id": "1241", "worker_type": "breeder", "cpu_usage": "80%","wall_clock_used":  "10.3s","%_complete":  "70%"},
		{"process_id": "1242", "worker_type": "breeder", "cpu_usage": "80%", "wall_clock_used": "10.3s","%_complete":  "70%"},
		{"process_id": "1243", "worker_type": "breeder", "cpu_usage": "80%", "wall_clock_used": "10.3s","%_complete":  "70%"},
		{"process_id": "1240", "worker_type": "zookeeper", "cpu_usage": "10%","wall_clock_used":  "100.3s","%_complete":  "N/A"}
	]
}
<?php } elseif ($_GET['id'] == 1) { ?>
{
	"process_list": [
		{"process_id": "1714", "worker_type": "judge", "cpu_usage": "90%","wall_clock_used":  "18.3s","%_complete":  "51%"},
		{"process_id": "1715", "worker_type": "judge", "cpu_usage": "91%","wall_clock_used":  "18.3s","%_complete":  "51%"},
		{"process_id": "1716", "worker_type": "judge", "cpu_usage": "89%", "wall_clock_used": "18.3s","%_complete":  "51%"},
		{"process_id": "1717", "worker_type": "judge", "cpu_usage": "89%","wall_clock_used":  "18.4s","%_complete":  "52%"},
		{"process_id": "1718", "worker_type": "breeder", "cpu_usage": "80%","wall_clock_used":  "18.3s","%_complete":  "30%"},
		{"process_id": "1719", "worker_type": "breeder", "cpu_usage": "80%", "wall_clock_used": "18.3s","%_complete":  "30%"},
		{"process_id": "1720", "worker_type": "zookeeper", "cpu_usage": "10%","wall_clock_used":  "180.3s","%_complete":  "N/A"}
	]
}
		
<?php } else { ?>
{
	"process_list": [
		{"process_id": "234", "worker_type": "judge", "cpu_usage": "90%","wall_clock_used":  "12.3s","%_complete":  "61%"},
		{"process_id": "235", "worker_type": "judge", "cpu_usage": "91%","wall_clock_used":  "12.3s","%_complete":  "61%"},
		{"process_id": "236", "worker_type": "judge", "cpu_usage": "89%","wall_clock_used":  "12.3s","%_complete":  "61%"},
		{"process_id": "237", "worker_type": "judge", "cpu_usage": "89%","wall_clock_used":  "12.4s","%_complete":  "62%"},
		{"process_id": "238", "worker_type": "judge", "cpu_usage": "90%","wall_clock_used":  "12.4s","%_complete":  "62%"},
		{"process_id": "239", "worker_type": "judge", "cpu_usage": "91%", "wall_clock_used": "12.4s", "%_complete": "62%"},
		{"process_id": "240", "worker_type": "breeder", "cpu_usage": "81%","wall_clock_used":  "10.3s","%_complete":  "70%"},
		{"process_id": "241", "worker_type": "breeder", "cpu_usage": "80%","wall_clock_used":  "10.3s","%_complete":  "70%"},
		{"process_id": "242", "worker_type": "breeder", "cpu_usage": "80%","wall_clock_used":  "10.3s","%_complete":  "70%"},
		{"process_id": "243", "worker_type": "breeder", "cpu_usage": "80%", "wall_clock_used": "10.3s", "%_complete": "70%"},
		{"process_id": "240", "worker_type": "zookeeper", "cpu_usage": "10%", "wall_clock_used": "200.3s","%_complete":  "N/A"},
		{"process_id": "240", "worker_type": "zookeeper", "cpu_usage": "10%","wall_clock_used":  "200.3s","%_complete":  "N/A"},
		{"process_id": "240", "worker_type": "zookeeper", "cpu_usage": "10%","wall_clock_used":  "200.3s","%_complete":  "N/A"},
		{"process_id": "240", "worker_type": "zookeeper", "cpu_usage": "10%","wall_clock_used":  "200.3s","%_complete":  "N/A"}
	]
}
		
<?php }