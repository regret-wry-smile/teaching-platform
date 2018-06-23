//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('uploadfileModalCtrl', function($scope,$modalInstance) {
		$scope.ok = function() {
			$modalInstance.close();
		}
		$scope.cancel=function(){
			$modalInstance.cancel();
		}
	})