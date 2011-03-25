<?php

$url = $_GET['url'];

$opts = array(
  'http'=>array(
    'method'=>"GET",
    'header'=>"Accept-language: en\r\n" .
              "Cookie: foo=bar\r\n" .
			  "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; rv:1.7.3) Gecko/20041001 Firefox/0.10.1" .
			  "Content-Type: text/plain; charset=iso-8859-1"
  )
);

$context = stream_context_create($opts);   

$fp = fopen($url, 'r', false, $context);
if($fp)
fpassthru($fp);
fclose($fp);
exit;

?>
