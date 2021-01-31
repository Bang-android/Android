<?php
    if ($_SERVER['REQUEST_METHOD'] == 'GET') {
      include('dbcon.php');

      $result = array();

      $stmt = $con -> prepare('SELECT * FROM board');

      if ($stmt -> execute()) {
        while($row = $stmt -> fetch()) {
          $result[] = $row;
        }
      }

      header('Content-Type: application/json; charset=utf8');
      $json = json_encode($result, JSON_PRETTY_PRINT|JSON_UNESCAPED_UNICODE);
      echo $json;
  }
?>
