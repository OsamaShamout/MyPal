<?php 

//get database information
include ("db_config.php");

$user_id = $_POST["user_id"];


//DB Location from UserID.
$full_location = mysqli_fetch_array($location_query->get_result());
$location_now = $full_location["country"];

echo $location_now;


?>