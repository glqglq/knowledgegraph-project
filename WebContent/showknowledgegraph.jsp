
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
<script type="text/javascript" src="js/echarts.js"></script>

</head>
<body>
	<div id="container">
		<div id="hd" class="ue-clear">
			<a href="search.jsp"><div class="logo"></div></a>
			<div class="inputArea">
				抽取关系数目：<input type="text" class="searchInput" value='${param["searchstr"]}' />
				<input type="button" class="extractButton" onclick="geturl()" />
			</div><br/>
		</div>
		<div class="nav">
			<ul class="searchList">
				<li class="searchItem" data-type="article"><a href="search.jsp">搜索</a></li>
				<li class="searchItem current" data-type="question"><a href="showknowledgegraph.jsp?relationship_num=50">图谱</a></li>
			</ul>
		</div>
		<div id="bd" class="ue-clear">
			<div id="main">
                <div id="graph" style="height:1000px;width:1000px"></div>
            </div>

        </div><!-- End of main -->
    </div><!--End of bd-->
</div>

<div id="foot">Copyright &copy;中国科学院计算技术研究所 版权所有  E-mail:gongluqi@ict.ac.cn</div>
</body>
<script language="javascript">
	function geturl() {
		window.location.href = "showknowledgegraph.jsp?relationship_num="
				+ document.getElementsByClassName("searchInput")[0].value;
	}
	$(document).ready(
			function() {
				var myChart = echarts.init(document.getElementById('graph'));
				myChart.showLoading();
				$.getJSON('kg.json', function (json) {
				    myChart.hideLoading();
				    var relationship_num = "${param['relationship_num']}";
				    
				    //console.info(relationship_num);
				    var linkstemp = json.edges.map(function (edge) {
	                    return {
	                        source: edge.sourceID,
	                        target: edge.targetID
	                    };
	                })
	                if(relationship_num.length != 0)
	                	linkstemp = linkstemp.slice(0,relationship_num);
				    var datatempnow = json.nodes.map(function (node) {
				    	for(var i = 0;i < linkstemp.length;i++){
				    		//console.info(node.id + " " + linkstemp[i].source)
				    		if(node.id == linkstemp[i].source || node.id == linkstemp[i].target)
				    			return {
		                        id: node.id,
		                        name: node.label,
		                        symbolSize: node.size*2,
		                        value:node.size,
		                        itemStyle: {
		                            normal: {
		                                color: node.color
		                            }
		                        }
		                    };
				    	}
				    });
				    
				    var j = 0;
				    var datatemp = [];
				    for(var i = 0;i < datatempnow.length;i++)
				    	if(typeof(datatempnow[i]) != "undefined")
				    		datatemp[j++] = datatempnow[i];
				    console.info(linkstemp);
				    
				    
				    myChart.setOption(option = {
				    	title: {
				            text: '装备知识图谱'
				        },
				        animationDurationUpdate: 1400,
				        animationEasingUpdate: 'quinticInOut',
				        series : [
				            {
								//legendHoverLink:false,
								
/*    				            	coordinateSystem:'polar',
   				            	polarIndex:1, */
/*  							xAxisIndex:1000,
								yAxisIndex:1000, */
				                type: 'graph',
				                layout: 'force',
				                //draggable:true,
				                //progressiveThreshold: 700,
				                data: datatemp,
				                links: linkstemp,
				                left:'5%',
				                top:'5%',
				                right:'5%',
				                bottom:'5%',
				                animation:false,
				                label: {
				                	normal: {
				                		show:true,
				                		color:'auto',
				                		fontSize:18,
				                		position:'bottom'
				                	},
				                    emphasis: {
				                        position: 'right',
				                        show: true
				                    }
				                },
				                force:{
					                layoutAnimation:false,	
					                repulsion:1100,
					                edgeLength:500,
					                gravity:1,
				                },
				                roam: true,
				                nodeScaleRatio:1,
				                //symbolOffset:[0,'50%'],
				                //edgeSymbol:['circle','arrow'],
				                focusNodeAdjacency: true,
  				                lineStyle: {
				                    normal: {
				                        //width: 0.5,
				                        //curveness: 0.5,
				                        opacity: 0.3
				                    },
				                    emphasis: {
				                    	opacity: 0.5,
				                    	color:'#000000',
				                    }
				                } 
				            }
				        ]
				    }, true);
				});
			});
</script>

</html>