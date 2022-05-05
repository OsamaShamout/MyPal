<?php

include('db_config.php');


$first_name = $_POST["first_name"];
$last_name = $_POST["last_name"];
$location = $_POST["location"];
$user_id = $_POST["user_id"];



//Prepare Query to Obtain Email from DB.
$update_query = $mysqli->prepare("UPDATE registered_user SET first_name = $first_name, last_name = $last_name, country = $location WHERE user_id = ?;);
$update_query->bind_param("i",$user_id);
$update_query->execute();


$query = $mysqli->prepare("INSERT INTO activity_created (name, description, location, date, capacity, activity_creator, creator_id, activity_tag) VALUES (?, ?, ?, ?, ?, ?, ?, ?);"); 


//Bind values to object
$query->bind_param("ssssssis", $name, $description, $location, $date, $capacity, $activity_creator, $user_id, $tag);


//Perform query
$query->execute();

echo "Success";
?>