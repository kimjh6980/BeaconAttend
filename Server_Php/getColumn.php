<?php

error_reporting(E_ALL); 
ini_set('display_errors',1); 

$link=mysqli_connect("localhost","root","wlsgur9215","BeaconAttend"); 
if (!$link)  
{ 
       echo "MySQL 접속 에러 : ";
          echo mysqli_connect_error();
             exit();
}  

mysqli_set_charset($link,"utf8");  

//POST 값을 읽어온다.
$classname=$_POST['classname'];
$addDay=$_POST['addDay'];
    $sql = "show columns from ".$classname." where Field=".$addDay.";";
    $colresult = mysqli_query($link, $sql);
#   $conresult = mysqli_fetch($sql);
    if(!$conresult['Field']) {
                $sql = "alter table $classname add ($addDay int(1) default 0)";
                $result = mysqli_query($link, $sql);

                if($result) {
                    echo "Success";
                    }
                else    {
                    echo "Error : ";
                    echo mysqli_error($link);
                    }
            }   else    {
                echo "column is already exist";
            }
mysqli_close($link);
?>
