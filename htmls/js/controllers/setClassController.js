//定义模块时引入依赖  
var app = angular.module('app', ['ui.bootstrap', 'toastr']);
app.controller('setClassCtrl', function($scope, toastr) {
	$scope.setClass={
		classes:'',
		suject:'',
		sujectName:''
	}
	$scope.classList=[];//班级数组
	/*查询班级列表*/
	var _selectClass = function() {
		$scope.result = JSON.parse(execute_student("select_class"));
		if($scope.result.ret=='success'){
			if($scope.result.item.length>0){
				angular.forEach($scope.result.item,function(i){
					var item={
						key:i.classId,
						value:i.className
					}
					$scope.classList.push(item);
					//console.log("班级"+JSON.stringify($scope.classList))
					$scope.setClass.classes=$scope.classList[0].key;
					$scope.setClass.classes1=angular.copy($scope.setClass.classes);								
	
				})			
			}
		}else{
			toastr.error($scope.result.message);
		}
	};
	
	var _init=function(){
		_selectClass();
	}();
	/*$scope.classList=[{id:1,name:"哈哈哈哈哈"},{id:2,name:"嘻嘻嘻嘻"}];
	$scope.$watch('setClass.classes',function(newvalue,oldvalue){
		$scope.setClass.classes=$scope.classList[0].id;
		$scope.setClass.classes1=angular.copy($scope.setClass.classes);
	},true)*/
	
	//跳转到答题中心页面
	$scope.startClass=function(){
		window.location.href = "../../page/answermoudle/answerCenter.html?backurl=" + window.location.href;
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