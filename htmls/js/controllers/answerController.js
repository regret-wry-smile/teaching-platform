//定义模块时引入依赖  
var app = angular.module('app', ['ui.bootstrap', 'toastr']);
app.controller('answerCtrl', function($scope, toastr,$window) {
	$scope.setClass={
		classes:'333',
		suject:'111',
		sujectName:'111'
	}
	//跳转到答题中心页面
	$scope.startAnswer=function(){
		$scope.param = "classes=" + $scope.setClass.classes + "&suject=" + $scope.setClass.suject + "&sujectName=" + $scope.setClass.sujectName;			
		var objectUrl = '../../page/answermoudle/stopAnswer.html' + '?' + $scope.param;
		console.log(JSON.stringify($scope.param))
		$window.location.href = objectUrl;
		
	}
})
app.config(['$locationProvider', function($locationProvider) {  
	    //$locationProvider.html5Mode(true);  
 	$locationProvider.html5Mode({
	enabled: true,
	requireBase: false
	});
}]);
app.controller('stopAnswerCtrl', function($scope, toastr,$window,$location) {
	alert(JSON.stringify($location))
	 console.log($location.search().classes);
      console.log($location.search().suject);
      console.log($location.search().sujectName);
	
})
 