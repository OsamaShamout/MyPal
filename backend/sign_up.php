<?php

include('db_config.php');


$first_name = $_POST["first_name"];
$last_name = $_POST["last_name"];
$email = $_POST["email"];
$password = $_POST["password"];
$country = $_POST["country"];
$user_id = 0;


//Prepare Query to Obtain Email from DB.
$email_query = $mysqli->prepare("SELECT email FROM registered_user WHERE email=?");
$email_query->bind_param("s",$email);
$email_query->execute();

// Check if the user exists through e-mail. (UNIQ).
if($email_query->get_result()->num_rows==0){
  $query = $mysqli->prepare("INSERT INTO registered_user (first_name, last_name, email, password, country) VALUES (?, ?, ?, ?, ?);");   //Prepare query to insert into DB "?" to avoid SQL injections

//Hash password to enter to DB.
$user_hashed_db = hash("SHA256", $password);


//Bind values to object
$query->bind_param("sssss", $first_name, $last_name, $email, $user_hashed_db, $country);

//Perform query
$query->execute();

//Prepare Query to Obtain Created user_id from DB.
$user_id_query = $mysqli->prepare("SELECT user_id FROM registered_user WHERE email=?");
$user_id_query->bind_param("s",$email);
$user_id_query->execute();

//DB pasuser_id retrieved for sharedPref
$db_userid = mysqli_fetch_array($user_id_query->get_result());
$user_id = $db_userid["user_id"];

//Prepare Query to Obtain Name from DB.
    $name_query = $mysqli->prepare("SELECT CONCAT(first_name , ' ' , last_name) AS Name FROM registered_user WHERE user_id=?");
    $name_query->bind_param("i",$user_id);
    $name_query->execute();

    //DB Full Name from UserID.
    $full_name = mysqli_fetch_array($name_query->get_result());
    $activity_creator = $full_name["Name"];
    
    //Prepare Query to Obtain Location from DB.
    $location_query = $mysqli->prepare("SELECT country FROM registered_user WHERE user_id=?");
    $location_query->bind_param("i",$user_id);
    $location_query->execute();
    
    //DB Location from UserID.
    $full_location = mysqli_fetch_array($location_query->get_result());
    $location_now = $full_location["country"];

$result = "Success";

echo $result;
echo "_";
echo $user_id;
echo "_";
echo $activity_creator;
echo "_";
echo $country;
   
}else{
    $result = "User already exist";
    echo $result;
    return;

}


?>