<%@ page language="java" contentType="text/html; charset=GB2312"
	pageEncoding="GB2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>Insert title here</title>
<script language="javascript">
	//创建XMLHttp对象
	function create() {
		if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")
		}
	}
	//回调函数
	function callback() {
		if (xmlHttp.readyState == 4) {
			if (xmlHttp.status == 200) {
				//要实现的操作
				var xmlDoc=xmlHttp.responseText;
				var data=eval(xmlDoc);
                alert(data[0].score+","+data[0].fileName+","+data[0].content);
                alert(data[1].score+","+data[1].fileName+","+data[1].content);
			} else {
				alert("AJAX服务器返回错误！");
			}
		}
	}
	function run(url) {
		create();
		xmlHttp.open("POST", url, true);
		xmlHttp.onreadystatechange = callback;
		xmlHttp.send();
	}
	function ajaxJson() {
		run("test.do?method=jsonTest&&msg=" + new Date());
	}
</script>
</head>
<body>
	<form action="SearchServlet" method="post">
		<input type="text" name="searchstr" /><br> <input type="text"
			name="first" /><br> <input type="text" name="last" /><br>
		<input type="submit" value="提交" />
	</form>
</body>
</html>