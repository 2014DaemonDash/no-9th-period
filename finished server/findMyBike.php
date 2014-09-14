<?php
	$con=mysqli_connect("localhost","root","Blawls420","bikes");
	// Check connection
	if (mysqli_connect_errno()) {
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}

	$user = mysqli_real_escape_string($con, $_GET['user']);


	$sql = "SELECT * FROM bikelist WHERE owner='$user'";
//	$sql = "SELECT * FROM bikelist WHERE locationx-$locationx<0.02, locationy-$location<0.02";

	$result = mysqli_query($con,$sql);

	echo '{ "bikesNearby" : [';
	$flag = 0;
	while($row = mysqli_fetch_array($result)){
		if($flag){ 
			echo ' , ';
		} else {
			$flag = 1;
		}
		echo '{"id" : '.$row['ID'].', "locationx" : '.$row['locationx'].' , "locationy" : '.$row['locationy'].' , "description" : "'.$row['description'].'", "combo" : "'.$row['combo'].'" }';
	}
	echo ']}';

mysqli_close($con);
?>

