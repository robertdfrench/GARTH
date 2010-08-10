<!--
 *  Index File for GARTH Monitoring System
 *
 *  Created 08/09/2010 by Caleb Wherry
 *
 *  Copyright 2010 Korovasoft, Inc. All Rights Reserved.
 *  Licensed for use subject to terms of LICENSE file.
 *
-->
<html>
<title>GARTH - Genetic AlgoRiTHm </title>
</head>
<body>

<center>
<h1>GARTH Monitoring System<h1>
</center>

// Require the database class
require_once('includes/DbConnector.php');

// Create an object (instance) of the DbConnector
$connector = new DbConnector();

// Execute the query to retrieve articles
$result = $connector->query('SELECT id,title FROM cmsarticles ORDER BY id DESC LIMIT 0,5');

// Get an array containing the results.
// Loop for each item in that array
while ($row = $connector->fetchArray($result)){

echo '<p> <a href="viewArticle.php?id='.$row['ID'].'">';
echo $row['title'];
echo '</a> </p>';

}
?>

</body>
</html>
