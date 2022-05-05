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
    $result = "User not registered"; 
    echo $result;
    return;
}else{
//Prepare query to insert into DB "?" to avoid SQL injections
$query = $mysqli->prepare("INSERT INTO registered_user (first_name, last_name, email, password, country) VALUES (?, ?, ?, ?, ?);");

//Hash password to enter to DB.
$user_hashed_db = hash("SHA256", $password);

echo $user_hashed_db;

//Bind values to object
$query->bind_param("sssss", $first_name, $last_name, $email, $password, $country);

//Perform query
$query->execute();

}

//DB password is stored as a hashed password.
$db_password = mysqli_fetch_array($password_query->get_result());
$db_pass = $db_password["password"];





//Hash Front-End Password for Comparison
$user_hashed_fe = hash("SHA256", $password);


if($user_hashed_fe == $user_hashed_db){
    echo "Password match";
}
else{
    echo "Password mismatch";
}
    






?>