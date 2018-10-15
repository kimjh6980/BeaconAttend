<?php

header('Content-Type: text/html; charset=utf-8');

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
mysqli_query($link, "set session character_set_connection=utf8;");
mysqli_query($link, "set session character_set_results=utf8;");
mysqli_query($link, "set session character_set_client=utf8;");


//POST 값을 읽어온다.
$id=$_POST['id'];
$cname=$_POST['cname'];
$cnum=$_POST['cnum'];
#$initckname=$_POST['ckname']; 
#$ckname = iconv("utf8","euckr",$initckname);
$ckname = $_POST['ckname'];

    if ($cname != "" and $cnum != "" and $ckname != "") {
        $sql = "select id from profess_class where class='".$cname."_".$cnum."';";
        $result = mysqli_query($link, $sql);
        $count = mysqli_num_rows($result);
        if(!$count || $count == 0) {
            $sql = "insert into profess_class(id, class, name) values(".$id.",'".$cname."_".$cnum."','".$ckname."');";
            $result = mysqli_query($link, $sql);
                $sql="create table if not exists "."$cname"."_"."$cnum"." (id INT(8) NOT NULL);";
                $result=mysqli_query($link, $sql);
                if($result != null) {
                    echo "분반이 생성되었습니다.";
                }        else    {
                         echo "Error : ".mysqli_error($link);
            }
        }   else    {
            echo "class is already exist";
        }
    } else {
        echo "데이터를 입력하세요 ";
    }


mysqli_close($link);
?>
