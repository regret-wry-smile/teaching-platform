//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
	app.controller('setmoduleCtrl', function($scope) {
		$scope.stuManage = function() {
			  window.location.href="../../page/setmodule/setStuManage.html?backurl="+window.location.href; 
		}
	})
