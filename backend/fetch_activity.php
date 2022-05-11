<?php 

//get database information
include ("db_config.php");

$activity_id = $_POST["activity_id"];

//Prepare Query to Obtain Location from DB.
$activities_query = $mysqli->prepare("SELECT * FROM activity_created WHERE activity_id=?");
$activities_query->bind_param("i",$activity_id);
$activities_query->execute();

//DB Location from UserID.
$array = $activities_query->get_result();

//Initalize an array
$response = [];

//Add for every row response array (an object lira rate into array)
while($obj = $array->fetch_assoc()){
    //Save table rows
    $response[] = $obj;
}

//Encode the response into a JSON object.
$json_response = json_encode($response);

echo $json_response


?>