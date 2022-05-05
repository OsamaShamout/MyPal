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

$result = "Success";

echo $result;
echo "_";
echo $user_id;
   
}else{
    $result = "User already exist";
    echo $result;
    return;

}


?>