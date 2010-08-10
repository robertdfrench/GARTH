<?php

class SystemComponent {

  var $settings;

  function getSettings() {

    // System variables
    $settings['siteDir'] = './';

    // Database variables
    $settings['dbhost'] = 'localhost';
    $settings['dbusername'] = 'root';
    $settings['dbpassword'] = 'root';
    $settings['dbname'] = 'garth';

    return $settings;

  }

}
?>