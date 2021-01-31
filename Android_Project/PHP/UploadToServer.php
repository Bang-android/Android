<?php

    $file_path = "uploads/";

    if (!is_dir($file_path)) {
      mkdir($file_path);
    }

    $file_path = $file_path . basename($_FILES['uploaded_file']['name']);
    if (move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
      echo "Image Upload Success";
    } else {
      echo "Image Upload Fail";
    }
?>
