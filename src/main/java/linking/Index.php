<!--
<html>

<body style="font-size:10pt;" >

<a href = "Index.php?first=Angela_Merkel&second=Hillary_Rodham_Clinton"> Angie und Hillary</a><br>
<a href = "Index.php?first=Angela_Merkel&second=Joachim_Sauer"> Angie und ihr Mann</a><br>
<a href = "Index.php?first=Angela_Merkel&second=Dagmar_Krause"> Angie und Dagmar_Krause</a><br>


<form action = "Index.php">
first: <input type="text" width ="30" name = "first" value = "Leipzig"><br>
second: <input type="text" width ="30" name = "second" value = "Dresden"><br>
(prefix http://dbpedia.org/resource/ will be added automatically)<br>
<input type="submit">
</form>
--!>

<?php include('RelationFinder.php');?>
<?php

/*
$linkedFile = fopen("C:/Users/user/Desktop/tweet_1.0_data/linked\linked(simple).txt",'r') or die("Unable to open file!");
while(!feof($linkedFile)) {
    $splitData = explode(', ', fgets($linkedFile));
    if (!empty($splitData) && sizeof($splitData) > 2) {
        for ($x = 1; $x < sizeof($splitData) - 2; $x++) {
            for ($y = $x + 1; $y < sizeof($splitData) - 1; $y++) {
                $firstData[$x] = $splitData[$x];
                $secondData[$y] = $splitData[$y];
                $test  = $splitData[0] . ', ' . $firstData[$x] . ', ' . $secondData[$y];
                file_put_contents("test.csv", $test."\n", FILE_APPEND);
                echo 'test';
*/
               // $first = 'http://dbpedia.org/resource/'.$firstData[$x];
                //$second = 'http://dbpedia.org/resource/'.$secondData[$y];
                $first = 'http://dbpedia.org/resource/Barack_Obama';
                $second = 'http://dbpedia.org/resource/Donald_Trump';
                echo 'test';
                $rf = new RelationFinder();
                $ignoredProperties = array(
                    'http://www.w3.org/1999/02/22-rdf-syntax-ns#type',
                    'http://www.w3.org/2004/02/skos/core#subject',
                    'http://www.w3.org/2002/07/owl#sameAs',
                    'http://dbpedia.org/property/wikiPageUsesTemplate',
                    'http://dbpedia.org/property/wordnet_type',
                    'http://dbpedia.,org/property/wikilink',
                    'http://dbpedia.org/ontology/wikiPageWikiLink',
                    'http://purl.org/dc/terms/subject',
                    'http://www.w3.org/2000/01/rdf-schema#seeAlso',
                    'http://purl.org/linguistics/gold/hypernym'

                );
                //file_put_contents("output.csv", $splitData[0].$string."\n", FILE_APPEND);
                $maxDistance = 3;
                // get all queries we are interested in
                $queries = $rf->getQueries($first, $second, $maxDistance, 10, array(), $ignoredProperties, true);
                // execute queries one by one
                for($distance = 1; $distance <= $maxDistance; $distance++) {
                    echo '<b>Executing queries for distance '.$distance.'</b><br />';
                    foreach($queries[$distance] as $query) {
                        //echo 'Running following query:<br /><pre>'.htmlentities($query).'</pre><br/>';
                        $startTime = microtime(true);
                        $string = $rf->executeSparqlQuery($query, "CSV");
                        $runTime = microtime(true) - $startTime;
                        echo $first .$string . " \n";
                        file_put_contents("output.csv", $string."\n", FILE_APPEND);
                        echo 'runtime: '.$runTime." seconds \n";
                    }
                }

/*
            }
        }
    }
}



*/

?>

<!--
</body>
</html>
--!>