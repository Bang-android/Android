<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
      include('dbcon.php');

      $uid = $_POST['uid'];

      $stmt = $con -> prepare("SELECT COUNT(*) FROM user WHERE uid = :uid");
      $stmt -> bindParam(':uid', $uid);

      if ($stmt -> execute()) {
        $row = $stmt -> fetchColumn();

        if ($row == 0) {
          echo '사용 가능';
        } else {
          echo '중복된 아이디';
        }
      }
    }
?>
