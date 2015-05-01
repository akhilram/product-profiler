<?php

    //getProduct ID

    $productid = $_GET["productid"];
	$inputFilterFeatures = $_GET["features"];

    //File to store the features
    $result = shell_exec("rm -rf *Features.txt");
    $result = shell_exec("rm -rf *PhraseOutput.txt");
    $result = shell_exec("rm -rf *svm*txt");

    $inputFilename = $productid.'Features.txt';
    $inputFeaturesText = fopen($inputFilename,'w');

    //Get the review from Anaphora step
    $inputAnaphora = $_GET["productid"].'ReviewAnaphoraRes.txt';
    //$inputAnaphora = "reviews_output.txt";
    for($i=0;$i<count($inputFilterFeatures);$i++){
        fwrite($inputFeaturesText, $inputFilterFeatures[$i]."\n");
    }
	fclose($inputFeaturesText);

    $outputSentiments = $productid."PhraseOutput.txt";
    
	$getOpinionPhrases = 'java -jar PhraseSentimentExtractor.jar '.$inputFilename.' '.$inputAnaphora.' '.$outputSentiments;
    set_time_limit(120);
	$result = shell_exec($getOpinionPhrases);

    $svm_formatter = 'python3 svm_formatter.py --test '.$outputSentiments.' svm_watch_dict '.$productid."svm_input.txt";
    $result = shell_exec($svm_formatter);

    $svm_classify = './svm_classify '.$productid."svm_input.txt".' svm_watch.nb '.$productid."svm_rating.txt";
    $result = shell_exec($svm_classify);

    $format_rating = 'python3 ExtractRating.py '.$productid."svm_input.txt ".$productid."svm_rating.txt ". ' final_rating.txt';

    $outputFile = fopen('final_rating.txt','r');
    $final_rating = array();
    if ($outputFile) {
    while (($buffer = fgets($outputFile, 4096)) !== false) {
        list($feature,$rating) = explode(':',$buffer);
        $map["feature"] = $feature;
        $map["rating"] = floatval(rtrim($rating));
        $final_rating[] = $map;
    }
    if (!feof($outputFile)) {
        echo "Error: unexpected fgets() fail\n";
    }
    fclose($outputFile);
    }

    echo json_encode($final_rating);


    
		

?>