<?php 

//get database information
include ("db_config.php");



//prepare query to avoid SQL injections
$query = $mysqli->prepare("SELECT * FROM registered_user WHERE email=");

$query = "SELECT * FROM products WHERE code = '$code'";
$result = mysql_query($query);
if ($result) {
  if (mysql_num_rows($result) > 0) {
    echo 'found!';
  } else {
    echo 'not found';
  }
} else {
  echo 'Error: '.mysql_error();
}
//perform query
$query->execute();

//store query in an array
$array = $query->get_result();

//initalize an array
$response = [];

//add for every row response array (an object lira rate into array)
while($lira_rate = $array->fetch_assoc()){
    //sav table
    $response[] = $lira_rate;
}


$json_response = json_encode($response);
 
 echo $json_response;

?>