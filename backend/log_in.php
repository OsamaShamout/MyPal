<?php 
include('db_config.php');

//Obtain Values from Front End
$email = $_POST["email"];

//front_end password
$password = $_POST["password"];
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
//Proceed to compare with password
//Prepare Query to Obtain Password from DB.
$password_query = $mysqli->prepare("SELECT password FROM registered_user WHERE email=?");
$password_query->bind_param("s",$email);
$password_query->execute();
}


//DB password is stored as a hashed password.
$db_password = mysqli_fetch_array($password_query->get_result());
$db_pass = $db_password["password"];


//Hash Front-End Password for Comparison
$user_hashed_fe = hash("SHA256", $password);


if($user_hashed_fe == $db_pass){
    //Prepare Query to Obtain User_ID from DB.
    $userid_query = $mysqli->prepare("SELECT user_id FROM registered_user WHERE email=?");
    $userid_query->bind_param("s",$email);
    $userid_query->execute();

    //DB pasuser_id retrieved for sharedPref
    $db_userid = mysqli_fetch_array($userid_query->get_result());
    $user_id = $db_userid["user_id"];

    echo "Password match";
    echo $user_id;
}
else{
    echo "Password mismatch";
}
    


?>