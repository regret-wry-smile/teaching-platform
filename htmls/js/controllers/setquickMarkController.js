//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('quickMarkCtrl', function($rootScope,$scope,$modal,toastr) {
	$scope.markInfo={
		title:"",//主题
		describe:"",//主题描述
		programs:[],
	}
	//添加小对象
	$scope.additem=function(){		
		if($scope.markInfo.programs.length<5){
			if($scope.object){
				$scope.markInfo.programs.push($scope.object);
				$scope.object="";
			}	
		}else{
			toastr.warning("最多只能设置5个评分对象");
		}
		
	}
	$scope.delObject=function($index){
		$scope.markInfo.programs.splice($index,1);

	}
	$scope.startMark=function(){
		var param=$scope.markInfo;
		//console.log(JSON.stringify(param))
		$scope.result=JSON.parse(execute_score("start_score",JSON.stringify(param)));
		if($scope.result.ret=='success'){		
			window.location.href="../../page/answermoudle/markCount.html";
		}else{
			toastr.error($scope.result.message);
			window.location.href="../../page/answermoudle/markCount.html";
		}
	}
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; //跳转到评分统计页面
	}
	
})
//评分统计控制器
app.controller('quickMarkCountCtrl', function($rootScope,$scope,$modal,toastr) {
	$scope.markInfo={};
	
	$scope.markInfoslist=[];
	$scope.titleList=[];//标题数组
	$scope.colors = ['#ffffff','#c4d4ef','#14c629','#f4c96d','#86daf6'];
	$scope.data=[];
	$scope.datalist=[];
	var dom = document.getElementById("coutbar");
	var myChart = echarts.init(dom);
	var _getScoreTitleInfo=function(){
		$scope.result=JSON.parse(execute_score("get_scoreTitleInfo"));
		if($scope.result.ret=='success'&&$scope.result.item){		
			$scope.markInfo=$scope.result.item;
			console.log($scope.result);
		}else{
			toastr.error($scope.result.message);			
		}		
	}	
	var _getscore=function(){
		$scope.markInfoslist=[];
		$scope.datalist=[];
		$scope.data=[];
		$scope.titleList=[];
		$scope.result=JSON.parse(execute_score("get_score"));
		console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){			
			$scope.markInfoslist=$scope.result.item;
			if($scope.markInfoslist.length>3){
				$scope.width="14%";
			}else{
				$scope.width="20%";
			}
			for(var i=0;i<$scope.markInfoslist.length;i++){
			$scope.colors.push(i);
			$scope.titleList.push($scope.markInfoslist[i].program);	
			console.log("头部"+JSON.stringify($scope.titleList))
				if($scope.markInfoslist[i].total!=0&&$scope.markInfoslist[i].peopleSum!=0){
					var average=($scope.markInfoslist[i].total/$scope.markInfoslist[i].peopleSum).toFixed(1);
				}else{
					var average=$scope.markInfoslist[i].total/$scope.markInfoslist[i].peopleSum;
				}			
			$scope.datalist=[$scope.markInfoslist[i].total,$scope.markInfoslist[i].peopleSum,average];
			console.log("头部"+JSON.stringify($scope.datalist))
				var item={
					name:$scope.markInfoslist[i].program,
		            type:'bar',
		            barWidth: $scope.width,
		            itemStyle: {
			    		normal: {
					        label: {
					            show: true,
					            position: 'top',
					            textStyle: {
					                color: "#fff",
				                    fontSize:'16'
					            },
				            }
			       		}
		    		},
		    		
		            data:$scope.datalist,
				}
				$scope.data.push(item);
				console.log(JSON.stringify($scope.data))
				
			}
			
			option = {
		    color: $scope.colors,
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data: $scope.titleList,
		        textStyle:{
		        	fontSize:'16',
		        	color:'#fff'
		        }
		    },
		  	toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            data : ['总分', '人数', '平均分'],
		            axisTick: {
		                alignWithLabel: true
		            },
		            axisLine: {
						lineStyle: {
							color: '#dcdcdc', //这里是为了突出显示加上的，可以去掉
							show: false
						}
					},
					axisLabel: {        
		                show: true,
		                textStyle: {
		                    color: '#fff',
		                    fontSize:'16'
		                }
		            },
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		          	splitLine: false, //是否显示网格线
		          	/*axisTick:{
				        show:false
				    },*/
					axisLine: {
						lineStyle: {
							color: '#dcdcdc', //这里是为了突出显示加上的，可以去掉
							show: false
						}
					},
					axisLabel: {        
		                show: true,
		                textStyle: {
		                    color: '#fff',
		                    fontSize:'16'
		                }
		            },
		        }
		    ],
		    series : $scope.data
			}
			//myChart.clear();
			if(option && typeof option === "object") {
				myChart.setOption(option, true);
			}
		}else{
			toastr.error($scope.result.message);
			//myChart.clear();
		}
	}
	var _init=function(){
		_getScoreTitleInfo();
		_getscore();		
	}();
	//$scope.markInfoslist=[{"num":"1","peopleSum":5,"program":"1","total":100},{"num":"2","peopleSum":1,"program":"2","total":89},{"num":"3","peopleSum":3,"program":"3","total":79},{"num":"4","peopleSum":4,"program":"f","total":79},{"num":"5","peopleSum":"2","program":"g","total":79}]
	//刷新评分
	$scope.refresScore=function(){
		_getscore();
	}
	//停止评分
	$scope.stopMarkCount=function(){
		$scope.result=JSON.parse(execute_score("stop_score"));
		//console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){	
			toastr.success($scope.result.message);
		}else{
			toastr.error($scope.result.message);
		}
	}
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
})
