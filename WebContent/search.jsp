<%@ page language="java" contentType="text/html; charset=GB2312" pageEncoding="GB2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<title>搜索引擎</title>
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
						<li>如何学好设计</li>
						<li>界面设计</li>
						<li>UI设计培训要多少钱</li>
						<li>设计师学习</li>
						<li>哪里有好的网站</li>
					</ul>
				</div>

				<div class="historyArea">
					<p class="history">
						<label>热门搜索：</label>

					</p>
					<p class="history mysearch">
						<label>我的搜索：</label> <span class="all-search"> <a
							href="javascript:;">专注界面设计网站</a> <a href="javascript:;">用户体验</a>
							<a href="javascript:;">互联网</a> <a href="javascript:;">资费套餐</a>
						</span>

					</p>
				</div>
			</div>
			<!-- End of main -->
		</div>
		<!--End of bd-->

		<div class="foot">
			<div class="wrap">
				<div class="copyright">Copyright &copy;uimaker.com 版权所有
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

	// 搜索建议
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
	//定义一个search的，判断浏览器有无数据存储（搜索历史）
	if (localStorage.search) {
		//如果有，转换成 数组的形式存放到searchArr的数组里（localStorage以字符串的形式存储，所以要把它转换成数组的形式）
		searchArr = localStorage.search.split(",")
	} else {
		//如果没有，则定义searchArr为一个空的数组
		searchArr = [];
	}
	//把存储的数据显示出来作为搜索历史
	MapSearchArr();

	function add_search() {
		var val = $(".searchInput").val();
		if (val.length >= 2) {
			//点击搜索按钮时，去重
			KillRepeat(val);
			//去重后把数组存储到浏览器localStorage
			localStorage.search = searchArr;
			//然后再把搜索内容显示出来
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
	//去重
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