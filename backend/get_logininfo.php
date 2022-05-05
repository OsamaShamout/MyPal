<?php 
include('db_config.php');

//Obtain Values from Front End
$email = $_POST["email"];
$password = $_POST["password"];
$user_id = 0;

echo $email;
echo $password;


//Prepare Query to Obtain Email from DB.
$email_query = $mysqli->prepare("SELECT email FROM registered_user WHERE email=?");
$email_query->bind_param("s",$email);
$email_query->execute();


// Check if the user exists through e-mail. (UNIQ).
if($email_query->get_result()->num_rows==0){
    $result = "User not registered"; 
    echo $result;
}else{
//Proceed to compare with password
//Prepare Query to Obtain Password from DB.
$password_query = $mysqli->prepare("SELECT password FROM registered_user WHERE username=?");
$password_query->bind_param("s",$email);
$password_query->execute();
}

//DB password is stored as a hashed password.
$db_password = mysqli_fetch_array($password_query->get_result());
$hashed_password_db = $db_password["password"];

echo $hashed_password_db;
$user_password = password_decrypt password_hash($password, PASSWORD_BCRYPT);

//Password verification Boolean function
if($verify_password = password_verify($user_password,$hashed_password)){
    //Get user data to provide for application
    $first_name = $mysqli->query("SELECT first_name FROM registered_user WHERE email='$email'");
    $last_name = $mysqli->query("SELECT last_name FROM registered_user WHERE email='$email'");
    $user_id = $mysqli->query("SELECT user_id FROM registered_user WHERE email='$email'");
    $result = "Success";

}

else{
    $result = "Password incorrect";
}

$response= array("Result" =>$result,"First Name"=> $first_name,"Last Name"=> $last_name,"User_id"=>$user_id);

$json_response = json_encode($response);
 
echo $json_response;

?>