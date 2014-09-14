<?php
	$con=mysqli_connect("localhost","root","Blawls420","bikes");
	// Check connection
	if (mysqli_connect_errno()) {
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}

	$ID = mysqli_real_escape_string($con, $_GET['ID']);
	$locationx = mysqli_real_escape_string($con, $_GET['locationx']);
	$locationy = mysqli_real_escape_string($con, $_GET['locationy']);

//	mysqli_query($con,"INSERT INTO bikelist (ID, availability, locationx, locationy) VALUES ($ID, 'true', $locationx, $locationy)");

	mysqli_query($con,"UPDATE bikelist SET available='true', locationx=$locationx, locationy=$locationy WHERE ID=$ID");

//	mysqli_query($con,"UPDATE bikelist SET available='true' WHERE ID=$ID");

	$sql = "SELECT combo FROM bikelist WHERE ID=$ID";

	$result = mysqli_query($con,$sql);

	$row = mysqli_fetch_array($result);
	echo '{"combo" : "'.$row['combo'].'"}';

mysqli_close($con);
?>
