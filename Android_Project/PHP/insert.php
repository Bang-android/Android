<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
      include('dbcon.php');

      $title = $_POST['title'];
      $content = $_POST['content'];
      $date = $_POST['date'];
      $uid = $_POST['uid'];

      $stmt = $con -> prepare("INSERT INTO board(title, content, date, uid) VALUES(:title, :content, :date, :uid)");
      $stmt -> bindParam(':title', $title);
      $stmt -> bindParam(':content', $content);
      $stmt -> bindParam(':date', $date);
      $stmt -> bindParam(':uid', $uid);

      if ($stmt -> execute()) {
        echo "Insert Success";
      } else {
        echo "Insert Fail";
      }
    }
?>
