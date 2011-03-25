<?php

$url = $_GET['url'];

//echo @file_get_contents();

$ch = curl_init();
echo $url;
curl_setopt( $ch, CURLOPT_USERAGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; rv:1.7.3) Gecko/20041001 Firefox/0.10.1" );
curl_setopt( $ch, CURLOPT_URL, $url );
//curl_setopt( $ch, CURLOPT_ENCODING, "iso-8859-1" );
curl_setopt( $ch, CURLOPT_HTTPHEADER, "Content-Type: text/plain; charset=iso-8859-1");

$content = curl_exec( $ch );
curl_close ( $ch );

echo $content;

?>
