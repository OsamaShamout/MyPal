<?php 

//get database information
include ("db_config.php");

$user_id = $_POST["user_id"];
$activity_id = $_POST["activity_id"];
$members_to_register = $_POST["members_registered"];
$capacity = $_POST["capacity"];
$name = $_POST["name"];


//Prepare Query to Obtain Location from DB.
$activities_query = $mysqli->prepare("UPDATE activity_created SET capacity=? WHERE activity_id=?");
$activities_query->bind_param("si",$capacity, $activity_id);
$activities_query->execute();

//Prepare query to insert into DB "?" to avoid SQL injections
$query = $mysqli->prepare("INSERT INTO activity_registered (user_id, activity_id, members_registered, name) VALUES (?, ?, ?, ?);");

//Bind values to object
$query->bind_param("ssss", $user_id, $activity_id, $members_to_register, $name);

//Perform query
$query->execute();




?>