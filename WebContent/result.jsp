
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>搜索引擎</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/result.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<!-- <script type="text/javascript" src="js/global.js"></script> -->
<script type="text/javascript" src="js/pagination.js"></script>

</head>
<body>
	<div id="container">
		<div id="hd" class="ue-clear">
			<a href="/"><div class="logo"></div></a>
			<div class="inputArea">
				<input type="text" class="searchInput" value='${param["searchstr"]}' /> <input
					type="button" class="searchButton" onclick="add_search()" />
			</div>
		</div>
<!-- 		<div class="nav">
			<ul class="searchList">
				<li class="searchItem current" data-type="article">文章</li>
				<li class="searchItem" data-type="question">问答</li>
				<li class="searchItem" data-type="job">职位</li>
			</ul>
		</div> -->
		<div id="bd" class="ue-clear">
			<div id="main">
<!-- 				<div class="sideBar">

					<div class="subfield">网站</div>
					<ul class="subfieldContext">
						<li><span class="name">伯乐在线</span> <span class="unit">(None)</span>
						</li>
						<li><span class="name">知乎</span> <span class="unit">(9862)</span>
						</li>
						<li><span class="name">拉勾网</span> <span class="unit">(9862)</span>
						</li>
						<li class="more"><a href="javascript:;"> <span
								class="text">更多</span> <i class="moreIcon"></i>
						</a></li>
					</ul>


					<div class="sideBarShowHide">
						<a href="javascript:;" class="icon"></a>
					</div>
				</div> -->
				<div class="resultArea">
					<p class="resultTotal">
						<span class="info">找到约&nbsp;<span class="totalResult"></span>&nbsp;条结果(用时<span
							class="time"></span>秒)，共约<span class="totalPage"></span>页
						</span>
					</p>
					<div class="resultList">
					</div>
                    
                </div>
                <!-- 分页 -->
                <div class="pagination ue-clear"></div>
                <!-- 相关搜索 -->
                
                
                
            </div>
            <div class="historyArea">
            	<div class="hotSearch">
                	<h6>热门搜索</h6>
                    <ul class="historyList">
                        
                            <li><a href="/search?q=linux">linux</a></li>
                        
                    </ul>
                </div>
                <div class="mySearch">
                	<h6>我的搜索</h6>
                    <ul class="historyList">

                    </ul>
                </div>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
</div>

<div id="foot">Copyright &copy;中国科学院计算技术研究所 版权所有  E-mail:gongluqi@ict.ac.cn</div>
</body>
<script language="javascript">
	$(document).ready(
					function() {
						var searchstr = document.getElementsByClassName("searchInput")[0].value;
						var page_id = "${param['page_id']}";
						$.getJSON('SearchServlet?' + 'searchstr=' + searchstr
								+ '&page_id=' + page_id, function(data) {
							console.info(data);
							$(".totalResult").text(data[10].item_count);
							$(".time").text(data[10].duration);
							$(".totalPage").text(data[10].page_count);
							for(var i = 0;i < 10;i++){
								$(".resultList").append('<div class="resultItem"><div class="itemHead"></div></div>');
								$(".itemHead:last").append('<a target="_blank" class="title">' + data[i].fileName + '</a>');
								$(".itemHead:last").append('<span class="divsion">-</span>');
								$(".itemHead:last").append('<span class="dependValue"></span>');
								$(".dependValue:last").append('<span class="label">得分：</span>');
								$(".dependValue:last").append('<span class="label">' + data[i].score + '</span>');
								$(".resultItem:last").append('<div class="itemBody">' + data[i].content.substr(0,200) + '......</div>');
							}
							//分页
							$(".pagination").pagination(parseInt($(".totalResult").text()), {
								current_page :parseInt("${param['page_id']}") - 1, //当前页码
								items_per_page :10,
								display_msg :true,
								callback :pageselectCallback
							});
						})
					});
	
	function pageselectCallback(page_id, jq) {
		/* window.location.href=search_url+'?q='+key_words+'&p='+page_id */
		window.location.href='result.jsp?searchstr=' + document.getElementsByClassName("searchInput")[0].value +'&page_id=' + (page_id + 1);
	}
</script>
<script type="text/javascript">
    var search_url = "SearchServlet"

/* 	$('.searchList').on('click', '.searchItem', function(){
		$('.searchList .searchItem').removeClass('current');
		$(this).addClass('current');	
	}); */
	
/* 	$.each($('.subfieldContext'), function(i, item){
		$(this).find('li:gt(2)').hide().end().find('li:last').show();		
	}); */

	function removeByValue(arr, val) {
      for(var i=0; i<arr.length; i++) {
        if(arr[i] == val) {
          arr.splice(i, 1);
          break;
        }
      }
    }
/* 	$('.subfieldContext .more').click(function(e){
		var $more = $(this).parent('.subfieldContext').find('.more');
		if($more.hasClass('show')){
			
			if($(this).hasClass('define')){
				$(this).parent('.subfieldContext').find('.more').removeClass('show').find('.text').text('自定义');
			}else{
				$(this).parent('.subfieldContext').find('.more').removeClass('show').find('.text').text('更多');	
			}
			$(this).parent('.subfieldContext').find('li:gt(2)').hide().end().find('li:last').show();
	    }else{
			$(this).parent('.subfieldContext').find('.more').addClass('show').find('.text').text('收起');
			$(this).parent('.subfieldContext').find('li:gt(2)').show();	
		}
		
	}); */
	
/* 	$('.sideBarShowHide a').click(function(e) {
		if($('#main').hasClass('sideBarHide')){
			$('#main').removeClass('sideBarHide');
			$('#container').removeClass('sideBarHide');
		}else{
			$('#main').addClass('sideBarHide');	
			$('#container').addClass('sideBarHide');
		}
        
    }); */
/* 	var key_words = "java" */
	
	
	setHeight();
	$(window).resize(function(){
		setHeight();	
	});
	
	function setHeight(){
		if($('#container').outerHeight() < $(window).height()){
			$('#container').height($(window).height()-33);
		}	
	}
</script>
<script type="text/javascript">
/*     $('.searchList').on('click', '.searchItem', function(){
        $('.searchList .searchItem').removeClass('current');
        $(this).addClass('current');
    });

    // 联想下拉显示隐藏
    $('.searchInput').on('focus', function(){
        $('.dataList').show()
    });

    // 联想下拉点击
    $('.dataList').on('click', 'li', function(){
        var text = $(this).text();
        $('.searchInput').val(text);
        $('.dataList').hide()
    });

    hideElement($('.dataList'), $('.searchInput')); */
</script>
<script>
/*     var searchArr;
    //定义一个search的，判断浏览器有无数据存储（搜索历史）
    if(localStorage.search){
        //如果有，转换成 数组的形式存放到searchArr的数组里（localStorage以字符串的形式存储，所以要把它转换成数组的形式）
        searchArr= localStorage.search.split(",")
    }else{
        //如果没有，则定义searchArr为一个空的数组
        searchArr = [];
    }
    //把存储的数据显示出来作为搜索历史
    MapSearchArr();

    function add_search(){
        var val = $(".searchInput").val();
        if (val.length>=2){
            //点击搜索按钮时，去重
            KillRepeat(val);
            //去重后把数组存储到浏览器localStorage
            localStorage.search = searchArr;
            //然后再把搜索内容显示出来
            MapSearchArr();
        }

        window.location.href=search_url+'?q='+val+"&s_type="+$(".searchItem.current").attr('data-type')

    }

    function MapSearchArr(){
        var tmpHtml = "";
        var arrLen = 0
        if (searchArr.length > 6){
            arrLen = 6
        }else {
            arrLen = searchArr.length
        }
        for (var i=0;i<arrLen;i++){
            tmpHtml += '<li><a href="/search?q='+searchArr[i]+'">'+searchArr[i]+'</a></li>'
        }
        $(".mySearch .historyList").append(tmpHtml);
    }
    //去重
    function KillRepeat(val){
        var kill = 0;
        for (var i=0;i<searchArr.length;i++){
            if(val===searchArr[i]){
                kill ++;
            }
        }
        if(kill<1){
            searchArr.unshift(val);
        }else {
            removeByValue(searchArr, val)
            searchArr.unshift(val)
        }
    } */
</script>
</html>