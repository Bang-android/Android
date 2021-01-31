<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
      include('dbcon.php');

      $uid = $_POST['uid'];
      $upassword = $_POST['upassword'];

      $stmt = $con -> prepare("SELECT COUNT(*) FROM user WHERE uid = :uid AND upassword = :upassword");
      $stmt -> bindParam('uid', $uid);
      $stmt -> bindParam('upassword', $upassword);

      if ($stmt -> execute()) {
        $row = $stmt -> fetchColumn();

        if ($row > 0) {
          echo "Login Success";
        } else {
          echo "Login Fail";
        }
      }
    }
?>
