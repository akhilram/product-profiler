<?php

	// $xml_query = 'http://open.api.ebay.com/shopping?callname=FindReviewsAndGuides&responseencoding=XML&appid=Universi-599d-4cbd-b112-4d2624ef5a28&siteid=0&version=531&MaxResultsPerPage=20';
	// $xml_query .= '&ProductID.value=' . $_GET["productidvalue"];
	// $xml_query .= '&ProductID.type=' . $_GET["productidType"];

	// 	// var_dump($xml_query);

	// $inputAnaphora = $_GET["productidvalue"] . 'ReviewInput.txt';
	// $outputAnaphora = $_GET["productidvalue"] . 'ReviewAnaphoraRes.txt';

	// 	// $inputFeatures = $_GET["productidvalue"] . 'ReviewInput.txt';
	// $inputFeatures = $_GET["productidvalue"] . 'ReviewAnaphoraRes.txt';
	// $outputFeatures = $_GET["productidvalue"] . 'Features.txt';

	// $inputPhrase = $_GET["productidvalue"] . 'Features.txt';
	// 	// $outputPhrase = $_GET["productidvalue"] . 'Features.txt';

	// $ebayXML = simplexml_load_file($xml_query);

	// if((String)$ebayXML->Ack == 'Failure') {
	// 	echo '<h2>No reviews found<h2>';
	// 	return;
	// }

	// $result = exec('rm -rf ' . $inputAnaphora . ' 2>&1');

	// foreach ($ebayXML->ReviewDetails->Review as $review) {
	// 	$text = (String) $review->Text;
	// 	$text = preg_replace('!\s+!', ' ', $text);
	// 	file_put_contents($inputAnaphora, $text . "\n", FILE_APPEND);
	// }
	// $resolveAnaphora = 'python controller.py ' . $inputAnaphora . ' ' . $outputAnaphora . ' 2>&1';
	// 	// var_dump($resolveAnaphora);
	// $result = shell_exec($resolveAnaphora);

	// 	// var_dump($result);

	// $getFeatures = 'java -jar feature-extractor.jar ' . $inputFeatures . ' ' . $outputFeatures  . ' --filter-nouns --num-features 10 --num-iterations 3 2>&1';
	// $result = shell_exec($getFeatures);
	// $features = [];
	// $handle = fopen($outputFeatures, "r");
	// if ($handle) {
	// 	while (($line = fgets($handle)) !== false) {
	//         $features[] = $line;
	// 	}
	// 	fclose($handle);
	// } else {
	//     // error opening the file.
	// } 

	// $featureJSON = [];
	// $featureJSON["list"] = $features;

	// echo json_encode($featureJSON);
	
	$features = [];
	$features[] = "one";
	$features[] = "two";
	$features[] = "three";

	$featureJSON = [];
	$featureJSON["list"] = $features;

	echo json_encode($featureJSON);

?>