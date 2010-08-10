<?php
////////////////////////////////////////////////////////////////////////////////////////
// Class: DbConnector
// Purpose: Connect to a database, MySQL version
///////////////////////////////////////////////////////////////////////////////////////

class DbConnector {

  var $settings;
  var $theQuery;
  var $link;

  // System variables
  $settings['siteDir'] = './';

  // Database variables
  $settings['dbhost'] = 'localhost';
  $settings['dbusername'] = 'root';
  $settings['dbpassword'] = 'root';
  $settings['dbname'] = 'garth';

  //*** Function: DbConnector, Purpose: Connect to the database ***
  function DbConnector(){

    // Get the main settings from the array we just loaded
    $host = $settings['dbhost'];
    $db = $settings['dbname'];
    $user = $settings['dbusername'];
    $pass = $settings['dbpassword'];

    // Connect to the database
    $this->link = mysql_connect($host, $user, $pass);
    mysql_select_db($db);
    register_shutdown_function(array(&$this, 'close'));

  }

  //*** Function: query, Purpose: Execute a database query ***
  function query($query) {

    $this->theQuery = $query;
    return mysql_query($query, $this->link);

  }

  //*** Function: fetchArray, Purpose: Get array of query results ***
  function fetchArray($result) {

    return mysql_fetch_array($result);

  }

  //*** Function: close, Purpose: Close the connection ***
  function close() {

    mysql_close($this->link);

  }


}
?>