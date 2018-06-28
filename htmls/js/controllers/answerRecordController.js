//定义模块时引入依赖  
var app = angular.module('app', ['ui.bootstrap', 'toastr']);
//作答记录
app.controller('answerRecordCtrl', function($scope, toastr) {
	$scope.setClass={
		classes:'',
		subject:'',
		sujectName:''
	}
	//$scope.curclassName='';//当前班级
	$scope.classList=[];//班级数组
	$scope.subjectlists=[];//科目数组
	$scope.classhourList=[]//课程数组
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
					$scope.classesobject=$scope.classList[0];
					$scope.setClass.classes1=angular.copy($scope.setClass.classes);								
	
				})			
			}
		}else{
			toastr.error($scope.result.message);
		}
	};
	//查询科目
	var _getsubject=function(){
		$scope.subjectlists= JSON.parse(execute_testPaper("get_subject"));
		if($scope.subjectlists.length>0){
			$scope.setClass.subject=$scope.subjectlists[0];
			$scope.setClass.subject1=angular.copy($scope.setClass.subject);
		}
	}
	//查询课程
	var _selectClassHour=function(){
		$scope.result=JSON.parse(execute_record("select_class_hour",$scope.setClass.classes,$scope.setClass.subject));
		//console.log(JSON.stringify($scope.result))
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
	//切换班级
	$scope.changeClass=function(classes){
		$scope.setClass.classes=classes;
		angular.forEach($scope.classList,function(i){
			if($scope.setClass.classes==i.key){
				$scope.classesobject=i;
				$scope.classhourList=[];
				_selectClassHour();
			}
		})
		
	}
	//切换科目
	$scope.changeSubject=function(subject){
		$scope.setClass.subject	=subject;
	}
	var _init=function(){
		_selectClass();
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