<?php
  $host = 'localhost';
  $dbname = 'testdb';
  $username = 'bang';
  $userpassword = 'bang';

  try {
    $con = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $userpassword);
  } catch (PDOException $e) {
    die("Failed to connect to the database: " . $e -> getMessage());
  }

  $con -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
  $con -> setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
?>
