//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('voteCtrl', function($rootScope,$scope,$modal) {
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
	$scope.startMark=function(){
		var param=$scope.voteInfo;
		//console.log(JSON.stringify(param))
		$scope.result=JSON.parse(execute_vote("start_vote",JSON.stringify(param)));
		if($scope.result.ret=='success'){		
			window.location.href="../../page/answermoudle/voteCount.html";
		}else{
			toastr.error($scope.result.message);
		}
	}
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
	//跳转到评分统计页面
	$scope.startVote=function(){				
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
