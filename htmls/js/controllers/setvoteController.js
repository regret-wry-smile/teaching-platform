//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('voteCtrl', function($rootScope,$scope,$modal,toastr) {
	$scope.voteInfo={
		title:"",//主题
		describe:"",//主题描述
		programs:[],
	}
	//添加小对象
	$scope.additem=function(){		
		if($scope.voteInfo.programs.length<5){
			if($scope.object){
				$scope.voteInfo.programs.push($scope.object);
				$scope.object="";
			}	
		}else{
			toastr.warning("最多只能设置5个评分对象");
		}
		
	}
	$scope.delObject=function($index){
		$scope.voteInfo.programs.splice($index,1);

	}
	//开始投票到投票统计页面
	$scope.startVote=function(){
		var param=$scope.voteInfo;
		//console.log(JSON.stringify(param))
		$scope.result=JSON.parse(execute_vote("start_vote",JSON.stringify(param)));
		if($scope.result.ret=='success'){		
			window.location.href="../../page/answermoudle/voteCount.html";
		}else{
			toastr.error($scope.result.message);
			window.location.href="../../page/answermoudle/voteCount.html";
		}
	}
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
	
})
//投票统计控制器
app.controller('quickVoteCountCtrl', function($rootScope,$scope,$modal,toastr) {
	$scope.voteInfo={};
	var _getvoteTitleInfo=function(){
		$scope.result=JSON.parse(execute_vote("get_voteTitleInfo"));
		if($scope.result.ret=='success'&&$scope.result.item){		
			$scope.voteInfo=$scope.result.item;
		}else{
			toastr.error($scope.result.message);			
		}		
	}
	var dom = document.getElementById("coutbar");
	var myChart = echarts.init(dom);
	$scope.voteInfoslist=[];//投票数组
	$scope.titleList=[];//标题数组
	$scope.colors = ['#ffffff','#c4d4ef','#14c629','#f4c96d','#86daf6'];
	$scope.data=[];
	var _getvote=function(){
		$scope.voteInfoslist=[];
		$scope.data=[];
		$scope.titleList=[];
		$scope.result=JSON.parse(execute_vote("get_vote"));
		console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){		
			$scope.voteInfoslist=$scope.result.item;
			if($scope.voteInfoslist.length>3){
				$scope.width="14%";
			}else{
				$scope.width="20%";
			}
			for(var i=0;i<$scope.voteInfoslist.length;i++){
			$scope.colors.push(i);
			
			$scope.titleList.push($scope.voteInfoslist[i].program);	
			//console.log("头部"+JSON.stringify($scope.titleList))		
				var item={
					name:$scope.voteInfoslist[i].program,
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
		    		
		            data:[$scope.voteInfoslist[i].agree,$scope.voteInfoslist[i].disagree,$scope.voteInfoslist[i].waiver],
				}
				$scope.data.push(item);
				//console.log(JSON.stringify($scope.data))
				
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
		  	/*toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },*/
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            data : ['赞成', '反对', '弃权'],
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
			//myChart.clear();
			toastr.error($scope.result.message);
		}
	}
	var _init=function(){
		_getvoteTitleInfo();
		_getvote();		
	}();	
	//$scope.voteInfoslist=[{"agree":4,"disagree":0,"num":"1","program":"张三","waiver":0},{"agree":2,"disagree":1,"num":"2","program":"李四","waiver":1},{"agree":0,"disagree":0,"num":"3","program":"王五","waiver":0}]
	
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
	//刷新投票统计
	$scope.refresVote=function(){
		_getvote();
	}
	//停止投票
	$scope.stopVoteCount=function(){
		$scope.result=JSON.parse(execute_vote("stop_vote"));
		//console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){	
			toastr.success($scope.result.message);
		}else{
			toastr.error($scope.result.message);
		}
	}
})
