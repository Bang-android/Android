<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
      include('dbcon.php');

      $uid = $_POST['uid'];
      $upassword = $_POST['upassword'];

      $stmt = $con -> prepare("INSERT INTO user(uid, upassword) VALUES(:uid, :upassword)");
      $stmt -> bindParam('uid', $uid);
      $stmt -> bindParam('upassword', $upassword);

      if ($stmt -> execute()) {
        echo "Regist Success";
      } else {
        echo "Regist Fail";
      }
    }
?>
