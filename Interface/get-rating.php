<?php


    $inputFilterFeatures = array('band','crystal','dial','strap');
	//$inputFilterFeatures = $_GET["features"];

    //$inputFeaturesText = $_GET["productidvalue"].'FilteredFeatures.txt';
    $inputFeaturesText = 'Features.txt';
    //$inputAnaphora = $_GET["productidvalue"].'ReviewAnaphoraRes.txt';
    $inputAnaphora = "reviews_output.txt";
	foreach ($inputFilterFeatures as $feature) {
		file_put_contents($inputFeaturesText, $feature."\n", FILE_APPEND);
	}

    //$outputSentiments = $_GET["productidvalue"] . 'PhraseOutput.txt';
    $outputSentiments = 'PhraseOutput.txt';

	$getOpinionPhrases = 'java -jar PhraseSentimentExtractor.jar '.$inputFeaturesText.' ' .$inputAnaphora . ' ' . $outputSentiments;

	$result = shell_exec($getOpinionPhrases);
		

?>