<?php
/**
 * Created by IntelliJ IDEA.
 * User: Meryl
 * Date: 15/10/2017
 * Time: 11:38 PM
 */

$linkedFile = fopen("C:/Users/user/Desktop/tweet_1.0_data/linked\linked.txt",'r') or die("Unable to open file!");
while(!feof($linkedFile)) {
    $splitData = explode(', ', fgets($linkedFile));
    if (!empty($splitData) && sizeof($splitData) > 2) {
        for ($x = 1; $x < sizeof($splitData) - 2; $x++) {
            for ($y = $x + 1; $y < sizeof($splitData) - 1; $y++) {
                $first[$x] = $splitData[$x];
                $second[$y] = $splitData[$y];
                $test  = $splitData[0] . ', ' . $first[$x] . ', ' . $second[$y];
                file_put_contents("test.csv", $test."\n", FILE_APPEND);
            }
            file_put_contents("test.csv", "\n", FILE_APPEND);
        }

    }
}





?>


