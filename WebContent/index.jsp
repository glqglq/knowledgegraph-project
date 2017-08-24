<%@ page language="java" contentType="text/html; charset=GB2312"
	pageEncoding="GB2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>Insert title here</title>
<script type="text/javascript" src="jquery-3.2.1.min.js"></script>
<script language="javascript">
	function ajaxjson() {
		var searchstr = encodeURI(encodeURI($("[name=searchstr]").val()));
		var first = $("[name=first]").val();
		var last = $("[name=last]").val();
		$.getJSON('SearchServlet?' + 'searchstr=' + searchstr + '&first=' + first
				+ '&last=' + last, function(data) {
			console.info(data);
		})
	}
</script>
</head>
<body>
		<input type="text" name="searchstr" /><br> <input type="text"
			name="first" /><br> <input type="text" name="last" /><br>
		<input type="button" value="提交" onclick="ajaxjson()" />

</body>
</html>