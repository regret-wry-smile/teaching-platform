//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('setPaperManageCtrl', function($rootScope,$scope,$modal) {
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/setmodule/setmodule.html"; 
	}
	//跳转到添加试卷页面
	$scope.addTestPaper=function(){				
		window.location.href="../../page/setmodule/addtestPage.html";
	}
})
app.controller('addPaperManageCtrl',function($rootScope,$scope,$modal){
	//返回返回试卷管理页面
	$scope.returnPage=function(){
		 window.location.href="../../page/setmodule/testPaperManage.html"; 
	}
	
})
