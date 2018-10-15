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
 
if ($id !="" and $enc_pw != "" and $phonenum !="" ){   
      
        $sql="select pw From profess where id='$id' and phonenum ='$phonenum'";  
            $result=mysqli_query($link,$sql);  

                if($result != null){	  
                    $row = mysqli_fetch_row($result);
                    if($row[0] == $enc_pw)  {
                           echo "Success";  
                    }
                    else    {
                        echo "pwIncorrect";
                    }
                 }  
                    else{  
                               echo "Error:"; 
                                      echo mysqli_error($link);
                                          } 
                     
} else {
        echo "NoData";
}


mysqli_close($link);
?>
