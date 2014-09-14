<?php
	$con=mysqli_connect("localhost","root","Blawls420","bikes");
	// Check connection
	if (mysqli_connect_errno()) {
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}

	$locationx = mysqli_real_escape_string($con, $_GET['locationx']);
	$locationy = mysqli_real_escape_string($con, $_GET['locationy']);
	$range = mysqli_real_escape_string($con, $_GET['range']);


	$sql = "SELECT * FROM bikelist WHERE ABS(locationx-$locationx)<$range&&ABS(locationy-$locationy)<$range";
//	$sql = "SELECT * FROM bikelist WHERE locationx-$locationx<0.02, locationy-$location<0.02";

	$result = mysqli_query($con,$sql);

	echo '{ "bikesNearby" : [';
	$flag = 0;
	while($row = mysqli_fetch_array($result)){
		if($row['available'] == 'true'){
			if($flag){ 
				echo ' , ';
			} else {
				$flag = 1;
			}
			echo '{"id" : '.$row['ID'].', "locationx" : '.$row['locationx'].' , "locationy" : '.$row['locationy'].' , "description" : "'.$row['description'].'" }';
		}
	}
	echo ']}';

mysqli_close($con);
?>

