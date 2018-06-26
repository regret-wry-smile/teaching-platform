//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('quickMarkCtrl', function($rootScope,$scope,$modal) {
	$scope.markInfo={
		title:"",//主题
		describe:""//主题描述
		programs:[]
	}
	var _startScore=function(){
		var param={
			
		}
		$scope.result=JSON.parse(execute_score("execute_score",JSON.stringify(param)));
		if($scope.result.ret=='success'){			
			if($scope.result.item.length>0){
				angular.forEach($scope.result.item,function(i){
					var item={
						key:i.class_hour_id,
						value:i.class_hour_name
					}
					$scope.classhourList.push(item);
					$scope.setClass.sujectName=$scope.classhourList[0].key;
					$scope.sujectNameobject=$scope.classhourList[0];
					console.log(JSON.stringify($scope.sujectNameobject))
					$scope.setClass.sujectName1=angular.copy($scope.setClass.sujectName);
				})
			}
		}else{
			toastr.error($scope.result.message);
		}
		
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
//评分统计控制器
app.controller('quickMarkCountCtrl', function($rootScope,$scope,$modal) {
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
	//跳转到评分统计页面
	$scope.startMark=function(){				
		window.location.href="../../page/answermoudle/markCount.html";
	}
	
})
