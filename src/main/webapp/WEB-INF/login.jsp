<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Access-Control-Allow-Header" content="*">
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <title>登录</title>
  <script src="../js/jquery-3.3.1.min.js"></script>
  <script src="../js/jquery.md5.js"></script>
  <script src="../js/jquery.cookie.js"></script>
</head>
<body>
    <br />
    <br />
    <br />
    <br />
    <br />
    <center>正在加载中, 请稍候...</center>
</body>
<script>


  $(function () {
	  var error = "${error}" ;
	  if(error != ""){
		  alert(error);
		  return ;
	  }
	  
	  var username = "${username}" ; 
	  var password = "${password}" ;
      $.ajax({
        type: "POST",
//         url: "/system/auth/login", //接口路径
        url: "/__api/global/user/login?locale=zh_cn", //接口路径
        data: JSON.stringify({
          username: username,
          password: password,
          captcha:"bypass"
        }),
        
	    contentType: "application/json",
        dataType: "json",
        success: function(response){ 
        console.log(response);
          var data = response;
          if (data.statusCode == 0) {
            window.location.replace("/index.html");
            $.cookie("TOKEN",data.data.token);
        //  }else if(data.statusCode==1001){
        //    window.location.replace("license.html");
          }
        }
      });
  })

</script>
</html>
