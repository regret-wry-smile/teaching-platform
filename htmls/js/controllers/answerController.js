//定义模块时引入依赖  
var app = angular.module('app', ['ui.bootstrap', 'toastr']);

app.controller('mainAnswerCtrl', function($scope, toastr,$window) {
	$scope.tabpane='s';
	$scope.selAnswerType=function(answerType){
		$scope.tabpane=answerType;
	}
})
//单选
app.controller('answerCtrl', function($scope, toastr,$window) {
	$scope.setanwser={
		classes:'333',
		suject:'111',
		sujectName:'111'
	}
	//跳转到答题中心页面
	$scope.startAnswer=function(){
		var param={
			id:"1",
			type:"m",
			range:"A-E"
		}
		$scope.result = JSON.parse(execute_answer("start_multiple_answer",JSON.stringify(param)));
		console.log("答题"+JSON.stringify($scope.result))
		return false;
		$scope.param = "classes=" + $scope.setClass.classes + "&suject=" + $scope.setClass.suject + "&sujectName=" + $scope.setClass.sujectName;
		var objectUrl = '../../page/answermoudle/stopAnswer.html' + '?' + $scope.param;
		console.log(JSON.stringify($scope.param))
		$window.location.href = objectUrl;
		
	}
	$scope.refresAnswerNum=function(){
		$scope.result = JSON.parse(execute_answer("get_multiple_answer_num"));
		console.log("获取答题"+JSON.stringify($scope.result))
	}
	
})
//多选
app.controller('answerMutilCtrl', function($scope, toastr,$window) {
	$scope.rangeList=["C","D","E","F"];
	$scope.range="C";
	$scope.range1=angular.copy($scope.range);
	$scope.changeRange=function(range){
		$scope.range=range;
	}
	//跳转到答题中心页面
	$scope.startAnswer=function(){
		
		var param={
			id:"1",
			type:"m",//多选
			range:"A-"+$scope.range
		}		
		$scope.result = JSON.parse(execute_answer("start_multiple_answer",JSON.stringify(param)));
		if($scope.result.ret=='success'){
			var objectUrl = '../../page/answermoudle/stopAnswerType.html';
			console.log(JSON.stringify($scope.param))
			$window.location.href = objectUrl;
		}else{
			toastr.error($scope.result.message);
		}
	}
	
	
})
app.controller('stopAnswerTypeCtrl', function($scope, toastr,$window) {
	$scope.isStopAswer=false;
	$scope.studentNum=0;
	//获取答题人数
	$scope.refresAnswerNum=function(){
		$scope.result = execute_answer("get_multiple_answer_num");
		$scope.studentNum= $scope.result;
		console.log("获取答题"+JSON.stringify($scope.result))
	}
	//停止答题
	$scope.stopAnswer=function(){
		$scope.isStopAswer=true;
	}
	
	
})
app.directive('select', function() {
	return {
		restrict: 'A',
		require: 'ngModel',
		scope:{
			defalutvalue:'=?'
		},
		link: function(scope, element, attrs, ngModelCtr) {
		scope.$watch('defalutvalue',function(){
			if(scope.defalutvalue){
				$(element).multiselect({
				width: "10rem",
				multiple: false,
				selectedHtmlValue: '请选择',
				defalutvalue:scope.defalutvalue,
				change: function() {
					$(element).val($(this).val());
					scope.$apply();
					if(ngModelCtr) {
						ngModelCtr.$setViewValue($(element).val());
						if(!scope.$root.$$phase) {
							scope.$apply();
						}
					}
				}
			});
			}
		})
			
			
			
		}
	}
})
app.directive('select1', function() {
	return {
		restrict: 'A',
		require: 'ngModel',
		scope:{
			defalutvalue:'=?',
			list:'=?'
		},
		link: function(scope, element, attrs, ngModelCtr) {
		scope.$watch('defalutvalue+list',function(){
			if(scope.defalutvalue){
				if(scope.list){
					var str='';
					for(var i=0;i<scope.list.length;i++){
						str+='<option value="'+scope.list[i]+'">'+scope.list[i]+'</option>';
					}
					$(element).html(str);
				}
				
				$(element).multiselect({
				multiple: false,
				selectedHtmlValue: '请选择',
				defalutvalue:scope.defalutvalue,
				change: function() {
					$(element).val($(this).val());
					scope.$apply();
					if(ngModelCtr) {
						ngModelCtr.$setViewValue($(element).val());
						if(!scope.$root.$$phase) {
							scope.$apply();
						}
					}
				}
			});
			}
		})
			
			
			
		}
	}
})
app.directive('select2', function() {
	return {
		restrict: 'A',
		require: 'ngModel',
		scope:{
			defalutvalue:'=?',
			list:'=?'
		},
		link: function(scope, element, attrs, ngModelCtr) {
		scope.$watch('defalutvalue+list',function(){
			if(scope.defalutvalue){
				if(scope.list){
					var str='';
					for(var i=0;i<scope.list.length;i++){
						str+='<option value="'+scope.list[i].key+'">'+scope.list[i].value+'</option>';
					}
					$(element).html(str);
				}
				
				$(element).multiselect({
				multiple: false,
				selectedHtmlValue: '请选择',
				defalutvalue:scope.defalutvalue,
				change: function() {
					$(element).val($(this).val());
					scope.$apply();
					if(ngModelCtr) {
						ngModelCtr.$setViewValue($(element).val());
						if(!scope.$root.$$phase) {
							scope.$apply();
						}
					}
				}
			});
			}
		})
			
			
			
		}
	}
})