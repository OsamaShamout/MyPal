<?php

include('db_config.php');


$name = $_POST["name"];
$description = $_POST["description"];
$location = $_POST["location"];
$date = $_POST["date"];
$capacity = $_POST["capacity"];
$tag = $_POST["tag"];
$user_id = $_POST["user_id"];


//Prepare Query to Obtain Email from DB.
$email_query = $mysqli->prepare("SELECT email FROM registered_user WHERE email=?");
$email_query->bind_param("s",$email);
$email_query->execute();

// Check if the user exists through e-mail. (UNIQ).
if($email_query->get_result()->num_rows==0){
  $query = $mysqli->prepare("INSERT INTO ac (first_name, last_name, email, password, country) VALUES (?, ?, ?, ?, ?);");   //Prepare query to insert into DB "?" to avoid SQL injections

//Hash password to enter to DB.
$user_hashed_db = hash("SHA256", $password);


//Bind values to object
$query->bind_param("sssss", $first_name, $last_name, $email, $user_hashed_db, $country);

//Perform query
$query->execute();

$result = "Success";

echo $result;

   
}else{
    $result = "User already exist";
    echo $result;
    return;

}


?>