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
$id=isset($_POST['id']) ? $_POST['id'] : '';  
$enc_pw=isset($_POST['pw']) ? $_POST['pw'] : '';
$phonenum=isset($_POST['phonenum']) ? $_POST['phonenum'] : ''; 
$sign=$_POST['sign'];

if ($id !="" and $enc_pw != "" and $phonenum !="" ){   
     $sql="select * from ".$sign." where id='".$id."';";
     $result = mysqli_query($link, $sql);
     $count = mysqli_num_rows($result);
     if(!$count || $count == 0) {
        $sql="insert into ".$sign."(id, pw, phonenum) values(".$id.",'".$enc_pw."','".$phonenum."');";  
            $result=mysqli_query($link,$sql);  

                if($result){  
                            echo "$sign";
                           echo "가입을 축하합니다.";  
                               }  
                   else{  
                               echo "에러 발생 : "; 
                                      echo mysqli_error($link);
                                          } 
     }  else    {
         echo "이미 가입된 ID입니다.";
     }
                     
} else {
        echo "데이터를 입력하세요 ";
}


mysqli_close($link);
?>

