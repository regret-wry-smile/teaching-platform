//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('setPaperManageCtrl', function($rootScope,$scope,$modal) {
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/setmodule/setmodule.html"; 
	}
})