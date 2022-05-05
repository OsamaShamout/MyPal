<?php

include('db_config.php');


$name = $_POST["name"];
$description = $_POST["description"];
$location = $_POST["location"];
$date = $_POST["date"];
$capacity = $_POST["capacity"];
$user_id = $_POST["user_id"];
$tag = $_POST["tag"];


//Prepare Query to Obtain Email from DB.
$name_query = $mysqli->prepare("SELECT CONCAT(first_name , ' ' , last_name) AS Name FROM registered_user WHERE user_id=?");
$name_query->bind_param("i",$user_id);
$name_query->execute();

//DB Full Name from UserID.
$full_name = mysqli_fetch_array($name_query->get_result());
$activity_creator = $full_name["Name"];


$query = $mysqli->prepare("INSERT INTO activity_created (name, description, location, date, capacity, activity_creator, creator_id, activity_tag) VALUES (?, ?, ?, ?, ?, ?, ?, ?);"); 


//Bind values to object
$query->bind_param("ssssssis", $name, $description, $location, $date, $capacity, $activity_creator, $user_id, $tag);


//Perform query
$query->execute();

echo "Success";
?>