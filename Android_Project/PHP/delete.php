<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
      include('dbcon.php');

      $title = $_POST['title'];
      $content = $_POST['content'];
      $date = $_POST['date'];
      $uid = $_POST['uid'];

      $stmt = $con -> prepare("DELETE FROM board WHERE title = :title AND content = :content AND date = :date AND uid = :uid");
      $stmt -> bindParam(':title', $title);
      $stmt -> bindParam(':content', $content);
      $stmt -> bindParam(':date', $date);
      $stmt -> bindParam(':uid', $uid);

      if ($stmt -> execute()) {
        echo "Delete Success";
      } else {
        echo "Delete Fail";
      }
    }
?>
