<?php 

//get database information
include ("db_config.php");

$creator_id = $_POST["user_id"];

//Prepare Query to Obtain Location from DB.
$activities_query = $mysqli->prepare("SELECT name, description ,location, date, activity_id FROM activity_created ORDER BY create_date DESC LIMIT 3");
$activities_query->bind_param("i",$creator_id);
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
 
//Display the JSON Object resulted. (Testing)
echo $json_response;

?>