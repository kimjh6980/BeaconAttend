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
$return_array = array();
 
if ($id !="" ){   
      
        $sql="select class,name From profess_class where id='$id'";  
            $result=mysqli_query($link,$sql);  

                if($result != null){	  
                    while($row = mysqli_fetch_array($result)){
                        $row_array['class'] = $row['class'];
                        $row_array['name'] = $row['name'];
                        array_push($return_array, $row_array);
                    }
                    echo json_encode($return_array, JSON_UNESCAPED_UNICODE);
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
