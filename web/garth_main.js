/**************************
* Entry point for Garth UI
***************************/
function garth_main() {
	console.log("[DEBUG] entering garth_main");
	$.getJSON('experiment_list.php', garth_display_experiment_list);
}

/**********************************
* Display Lefthand Experiment Menu
***********************************/
function garth_display_experiment_list(json_data) {
	console.log("[DEBUG] entering garth_load_experiment");
	for(var i = 0; i < json_data.experiment_list.length; i++) {
		$("#garth_experiment_list").append(garth_format_experiment_list_item(json_data.experiment_list[i]));
	}
}

/**************************
* Retrieve Experiment Data
***************************/
function garth_retrieve_experiment_data(experiment_id) {
	console.log("[DEBUG] entering garth_retrive_experiment_data");
	$.getJSON("experiment_data.php?id=" + experiment_id, garth_display_experiment_data)
}

/**********************************
* Display Experiment Information
***********************************/
function garth_display_experiment_data(json_data) {
	console.log("[DEBUG] entering garth_display_experiment_data");
	$("#garth_running_processes_list").html("<tr><th>Process ID</th><th>Worker Type</th><th>Wall Clock Used</th><th>% Complete</th></tr>");
	for(var i = 0; i < json_data.process_list.length; i++) {
		$("#garth_running_processes_list").append(garth_format_process_list_item(i, json_data.process_list[i]));
	}
}

/**************************************************************
* Formats a <tr> node for the Process List table
* Eventually this needs to be stored in a template
* and substituted
**************************************************************/
function garth_format_process_list_item(id, process_hash) {
	console.log("[DEBUG] entering garth_format_process_list_item");
	function cell(property) {
		return "<td>" + process_hash[property] + "</td>";
	}
	return "<tr>" + cell('process_id') + cell('worker_type') + cell('wall_clock_used') + cell('percent_complete') + "</tr>";
}

/**************************************************************
* Formats a <li> node for the lefthand Experiment List menu
* Eventually this needs to be stored in a template
* and substituted
**************************************************************/
function garth_format_experiment_list_item(experiment) {
	console.log("[DEBUG] entering garth_format_experiment_list_item");
	var id_string = " id='garth_experiment_list_item." + experiment.id + "'";
	var onclick_string = " onClick='javascript:garth_retrieve_experiment_data(" + experiment.id + ")'";
	return "<li " + id_string + onclick_string + ">" + experiment.description + "</li>";
}
