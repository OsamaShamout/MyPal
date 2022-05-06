<?php 

//get database information
include ("db_config.php");

$user_id = $_POST["user_id"];

//Prepare Query to Obtain Location from DB.
$activities_query = $mysqli->prepare("SELECT name, description ,location, date FROM activity_created WHERE user_id=? ORDER BY create_date DESC LIMIT 4 ");
$activities_query->bind_param("i",$user_id);
$activities_query->execute();

//DB Location from UserID.
$fetch_info = mysqli_fetch_array($activities_query->get_result());
$activity_name = $fetch_info["name"];
$activity_desc = $fetch_info["description"];
$activity_date = $fetch_info["date"];


echo $activity_name;
echo "\n";
echo $activity_desc;
echo "\n";
echo $activity_date;

?>