<?php
	$con=mysqli_connect("localhost","root","Blawls420","bikes");
	// Check connection
	if (mysqli_connect_errno()) {
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}

	$ID = mysqli_real_escape_string($con, $_GET['ID']);

	mysqli_query($con,"UPDATE bikelist SET available='false' WHERE ID=$ID");

        $sql = "SELECT combo FROM bikelist WHERE ID=$ID";
        $result = mysqli_fetch_array(mysqli_query($con,$sql));

	echo '{"combo" : "'.$result['combo'].'"}';
mysqli_close($con);

?>
