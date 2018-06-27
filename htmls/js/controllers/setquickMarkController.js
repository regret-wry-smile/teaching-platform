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
		}
	}
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; //跳转到评分统计页面
	}
	
})
//评分统计控制器
app.controller('quickMarkCountCtrl', function($rootScope,$scope,$modal,toastr) {
	$scope.markInfo={}
	var _getscore=function(){
		$scope.result=JSON.parse(execute_score("get_score"));
		console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){		
			$scope.markInfo=$scope.result.item;
		}else{
			toastr.error($scope.result.message);
		}
	}
	var _init=function(){
		_getscore();
	}();
	/*$scope.markInfo={num:"001",program:"哈哈哈哈哈哈",total:100,peopleSum:10};*/
	$scope.average=$scope.markInfo.total/$scope.markInfo.peopleSum;
	$scope.data=[$scope.markInfo.total,$scope.markInfo.peopleSum,$scope.average];
	var dom = document.getElementById("coutbar");
	var myChart = echarts.init(dom);
	var colors = ['#5793f3', '#d14a61', '#675bba'];
	option = {
    color: ['#fff'],
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
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
    series : [
        {
            name:'评分统计',
            type:'bar',
            barWidth: '50%',
           /* data:[100, 52, 80],*/
         	data:$scope.data,
            itemStyle: {
	    		normal: {
			        label: {
			            show: true,
			            position: 'top',
			            textStyle: {
			                color: '#fff',
		                    fontSize:'16'
			            },
		            }
	       		}
    		}
        }
    ]
	}
	if(option && typeof option === "object") {
		myChart.setOption(option, true);
	}
	
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
	//跳转到评分统计页面
	$scope.startMark=function(){				
		window.location.href="../../page/answermoudle/markCount.html";
	}
	
})
