<%@ page language="java" contentType="text/html; charset=GB2312" pageEncoding="GB2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<title>��������</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/index.css" rel="stylesheet" type="text/css" />
<script language="javascript">
	function ajaxjson() {
		var searchstr = encodeURI(encodeURI($("[name=searchstr]").val()));
/* 		var first = $("[name=first]").val();
		var last = $("[name=last]").val(); */
		$.getJSON('SearchServlet?' + 'searchstr=' + searchstr + '&first='
				+ '0' + '&last=' + '9', function(data) {
			console.info(data);
		})
	}
</script>
</head>
<body>
	<div id="container">
		<div id="bd">
			<div id="main">
				<h1 class="title">
					<div class="logo large"></div>
				</h1>
				<div class="inputArea">
					<input type="text" name="searchstr" class="searchInput" />
<!-- 				<br><input type="text" name="first" class="searchInput" /><br> 
					<input type="text" name="last" class="searchInput" />  -->
						<input
						type="button" class="searchButton" onclick="ajaxjson()" />
					<ul class="dataList">
						<li>���ѧ�����</li>
						<li>�������</li>
						<li>UI�����ѵҪ����Ǯ</li>
						<li>���ʦѧϰ</li>
						<li>�����кõ���վ</li>
					</ul>
				</div>

				<div class="historyArea">
					<p class="history">
						<label>����������</label>

					</p>
					<p class="history mysearch">
						<label>�ҵ�������</label> <span class="all-search"> <a
							href="javascript:;">רע���������վ</a> <a href="javascript:;">�û�����</a>
							<a href="javascript:;">������</a> <a href="javascript:;">�ʷ��ײ�</a>
						</span>

					</p>
				</div>
			</div>
			<!-- End of main -->
		</div>
		<!--End of bd-->

		<div class="foot">
			<div class="wrap">
				<div class="copyright">Copyright &copy;uimaker.com ��Ȩ����
					E-mail:admin@uimaker.com</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript">
	var suggest_url = "/suggest/"
	var search_url = "/search/"

	$('.searchList').on('click', '.searchItem', function() {
		$('.searchList .searchItem').removeClass('current');
		$(this).addClass('current');
	});

	function removeByValue(arr, val) {
		for (var i = 0; i < arr.length; i++) {
			if (arr[i] == val) {
				arr.splice(i, 1);
				break;
			}
		}
	}

	// ��������
	$(function() {
		$('.searchInput').bind(
				' input propertychange ',
				function() {
					var searchText = $(this).val();
					var tmpHtml = ""
					$.ajax({
						cache : false,
						type : 'get',
						dataType : 'json',
						url : suggest_url + "?s=" + searchText + "&s_type="
								+ $(".searchItem.current").attr('data-type'),
						async : true,
						success : function(data) {
							for (var i = 0; i < data.length; i++) {
								tmpHtml += '<li><a href="' + search_url + '?q='
										+ data[i] + '">' + data[i]
										+ '</a></li>'
							}
							$(".dataList").html("")
							$(".dataList").append(tmpHtml);
							if (data.length == 0) {
								$('.dataList').hide()
							} else {
								$('.dataList').show()
							}
						}
					});
				});
	})

	hideElement($('.dataList'), $('.searchInput'));
</script>
<script>
	var searchArr;
	//����һ��search�ģ��ж�������������ݴ洢��������ʷ��
	if (localStorage.search) {
		//����У�ת���� �������ʽ��ŵ�searchArr�������localStorage���ַ�������ʽ�洢������Ҫ����ת�����������ʽ��
		searchArr = localStorage.search.split(",")
	} else {
		//���û�У�����searchArrΪһ���յ�����
		searchArr = [];
	}
	//�Ѵ洢��������ʾ������Ϊ������ʷ
	MapSearchArr();

	function add_search() {
		var val = $(".searchInput").val();
		if (val.length >= 2) {
			//���������ťʱ��ȥ��
			KillRepeat(val);
			//ȥ�غ������洢�������localStorage
			localStorage.search = searchArr;
			//Ȼ���ٰ�����������ʾ����
			MapSearchArr();
		}

		window.location.href = search_url + '?q=' + val + "&s_type="
				+ $(".searchItem.current").attr('data-type')

	}

	function MapSearchArr() {
		var tmpHtml = "";
		var arrLen = 0
		if (searchArr.length >= 5) {
			arrLen = 5
		} else {
			arrLen = searchArr.length
		}
		for (var i = 0; i < arrLen; i++) {
			tmpHtml += '<a href="' + search_url + '?q=' + searchArr[i] + '">'
					+ searchArr[i] + '</a>'
		}
		$(".mysearch .all-search").html(tmpHtml);
	}
	//ȥ��
	function KillRepeat(val) {
		var kill = 0;
		for (var i = 0; i < searchArr.length; i++) {
			if (val === searchArr[i]) {
				kill++;
			}
		}
		if (kill < 1) {
			searchArr.unshift(val);
		} else {
			removeByValue(searchArr, val)
			searchArr.unshift(val)
		}
	}
</script>
</html>