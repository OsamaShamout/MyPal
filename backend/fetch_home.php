<?php 

//get database information
include ("db_config.php");

$user_id = $_POST["user_id"];

//Prepare Query to Obtain Location from DB.
$location_query = $mysqli->prepare("SELECT country FROM registered_user WHERE user_id=?");
$location_query->bind_param("i",$user_id);
$location_query->execute();

//DB Location from UserID.
$full_location = mysqli_fetch_array($location_query->get_result());
$location_now = $full_location["country"];

echo $location_now;


?>