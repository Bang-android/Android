<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
      include('dbcon.php');

      $oldTitle = $_POST['oldTitle'];
      $oldContent = $_POST['oldContent'];
      $oldDate = $_POST['oldDate'];
      $newTitle = $_POST['newTitle'];
      $newContent = $_POST['newContent'];
      $newDate = $_POST['newDate'];

      $stmt = $con -> prepare("UPDATE board SET title = :newTitle, content = :newContent, date = :newDate WHERE title = :oldTitle AND content = :oldContent AND date = :oldDate");
      $stmt -> bindParam(':newTitle', $newTitle);
      $stmt -> bindParam(':newContent', $newContent);
      $stmt -> bindParam(':newDate', $newDate);
      $stmt -> bindParam(':oldTitle', $oldTitle);
      $stmt -> bindParam(':oldContent', $oldContent);
      $stmt -> bindParam(':oldDate', $oldDate);

      if ($stmt -> execute()) {
        echo "Update Success";
      } else {
        echo "Update Fail";
      }
    }
?>
