<?php 

//get database information
include ("db_config.php");

$user_id = $_POST["user_id"];



//Prepare Query to Obtain Name from DB.
$name_query = $mysqli->prepare("SELECT CONCAT(first_name , ' ' , last_name) AS Name FROM registered_user WHERE user_id=?");
$name_query->bind_param("i",$user_id);
$name_query->execute();

//DB Full Name from UserID.
$full_name = mysqli_fetch_array($name_query->get_result());
$activity_creator = $full_name["Name"];

echo $activity_creator;
echo "_";

//Prepare Query to Obtain Location from DB.
$location_query = $mysqli->prepare("SELECT country FROM registered_user WHERE user_id=?");
$location_query->bind_param("i",$user_id);
$location_query->execute();

//DB Location from UserID.
$full_location = mysqli_fetch_array($location_query->get_result());
$location_now = $full_location["country"];

echo $location_now;


?>