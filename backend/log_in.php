<?php 
include('db_config.php');

//Obtain Values from Front End
$email = $_POST['email'];
$password = $_POST['password'];
$user_id = 0;

//Prepare Query to Obtain Email from DB.
$email_query = $mysqli->prepare("SELECT email from registered_user where email=?");
$email_query->bind_param("s",$email);
$email_query->execute();

// Check if the user exists through e-mail. (UNIQ).
if($email_query->get_result()->num_rows==0){
    $result = "User not registered."; 
}else{
//Proceed to compare with password
//Prepare Query to Obtain Password from DB.
$password_query = $mysqli->prepare("SELECT password from registered_user where username=?");
$password_query->bind_param("s",$email);
$password_query->execute();

//DB password is stored as a hashed password.
$db_password = mysqli_fetch_array($password_query->get_result());
$hashed_password_db = $db_password["password"];
$user_password = password_hash($password, PASSWORD_DEFAULT);

//Password verification Boolean function
if($verify_password = password_verify($user_password,$hashed_password)){
    //Get user data to provide for application
    $first_name = $mysqli->query("SELECT first_name from registered_user where email='$email'");
    $last_name = $mysqli->query("SELECT last_name from registered_user where email='$email'");
    $user_id = $mysqli->query("SELECT user_id from registered_user where email='$email'");


}else{
    $result = "Password incorrect.";
}

$response= array("Result" =>$result,"First Name"=> $first_name,"Last Name"=> $last_name,"User_id"=>$user_id);

$json_response = json_encode($response);
 
echo $json_response;