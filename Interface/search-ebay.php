<?php

$_GET["keywords"] = urlencode($_GET["keywords"]);

$xml_query = 'http://open.api.ebay.com/shopping?callname=FindProducts&responseencoding=XML&appid=Universi-599d-4cbd-b112-4d2624ef5a28&siteid=0&version=525';
$xml_query .= '&QueryKeywords=' . $_GET["keywords"];
$xml_query .= '&MaxEntries=20';
$xml_query .= '&ProductSort=ReviewCount';
$xml_query .= '&SortOrder=Descending';

$ebayXML = simplexml_load_file($xml_query);

$json_result = [];
$json_result["ack"] = (string) $ebayXML->Ack;
$json_result["resultCount"] = (int) $ebayXML->TotalProducts;

$itemCount = 0;
foreach ($ebayXML->Product as $item) {
    if(isset($item->StockPhotoURL) && $item->ReviewCount >=20 && $itemCount <5){
    
	$item_array = [];
	$basicInfo = [];
	$sellerInfo = [];
	$shippingInfo = [];
    

    foreach($item->ProductID as $productid){
        foreach($productid->attributes() as $type => $value){
            if($type=="type" && $value=="Reference"){
            $basicInfo["productID"] = (string)$productid;
            }
        }
       
        
    }
	$basicInfo["title"] = (string) $item->Title;
	$basicInfo["viewItemURL"] = (string)$item->DetailsURL;
	$basicInfo["galleryURL"] = (string)$item->StockPhotoURL;
	$basicInfo["reviewCount"] = (int)$item->ReviewCount;
	
	$item_array["basicInfo"] = $basicInfo;

	$json_result["item" . $itemCount++] = $item_array;
    }
}

$json_result["query"] = $xml_query;

echo json_encode($json_result);

?>