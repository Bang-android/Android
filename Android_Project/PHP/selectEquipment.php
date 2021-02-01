<?php
    if ($_SERVER['REQUEST_METHOD'] == 'GET') {
      include('dbcon.php');

        // 결과를 받았다고 가정해서 작성한 것!
      $stmt = $con -> prepare("SELECT * FROM equipment WHERE name = 'arm curl machine' OR name = 'bench press machine' OR name = 'cable crossover machine'");

      $result = array();

      if ($stmt -> execute()) {
        while ($row = $stmt -> fetch()) {
          $result[] = $row;
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode($result, JSON_PRETTY_PRINT|JSON_UNESCAPED_UNICODE);
        echo $json;
      }
    }
?>
