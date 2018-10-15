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
$id=$_POST['id'];
$cname=$_POST['cname'];
$cnum=$_POST['cnum'];
$day=$_POST['day'];

    $sql = "select id from ".$cname."_".$cnum." where id=".$id.";";
    $result = mysqli_query($link, $sql);
    $count = mysqli_num_rows($result);
    if(!$count || $count == 0 ) {
        $sql = "insert into ".$cname."_".$cnum."(id, ".$day.")values(".$id.", 1);";
        $result = mysqli_query($link, $sql);
        echo "Success";
    }   else    {
        if ($id !="" and $cname != "" and $cnum != "" and $day != "" ){   
 	   $sql = "update ".$cname."_".$cnum." set ".$day."=1 where id=".$id.";";
    
            	$result=mysqli_query($link,$sql);  

                if($result != null){	  
                    echo "Success";
                 }  
                    else{  
                          echo "에러 발생 : "; 
                          echo mysqli_error($link);
                    } 
		} else {
		        echo "데이터를 입력하세요 ";
		}
    }

mysqli_close($link);
?>
