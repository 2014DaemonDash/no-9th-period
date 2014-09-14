<?php

	$con=mysqli_connect("localhost","root","Blawls420","bikes");
	// Check connection
	if (mysqli_connect_errno()) {
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}

	$combo = mysqli_real_escape_string($con, $_GET['combo']);
	$user = mysqli_real_escape_string($con, $_GET['user']);
	$ID = 1;
	$result = mysqli_query($con,"SELECT * FROM bikelist");
 	while($row = mysqli_fetch_array($result)) {
   		$ID = $row['ID']+1;
	}
	$locationx = mysqli_real_escape_string($con, $_GET['locationx']);
	$locationy = mysqli_real_escape_string($con, $_GET['locationy']);
	$description = mysqli_real_escape_string($con, $_GET['description']);
	
//        mysqli_query($con,"INSERT INTO bikelist (combo, ID, locationx, locationy, available, description) VALUES (111,1,1,1,1,1)");

	$flag = mysqli_query($con,"INSERT INTO bikelist (combo, ID, locationx, locationy, available, description, owner) VALUES ('$combo', $ID, $locationx, $locationy, 'true', '$description', '$user')");
	
	if($flag){
		echo '{ "id": '.$ID.'}';
	}
	mysqli_close($con);

?>
