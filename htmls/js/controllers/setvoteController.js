//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('voteCtrl', function($rootScope,$scope,$modal) {
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
	//跳转到评分统计页面
	$scope.startMark=function(){				
		window.location.href="../../page/answermoudle/voteCount.html";
	}
	
})
//投票统计控制器
app.controller('quickMarkCountCtrl', function($rootScope,$scope,$modal) {
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
	/*//跳转到评分统计页面
	$scope.startMark=function(){				
		window.location.href="../../page/answermoudle/markCount.html";
	}*/
	
})
