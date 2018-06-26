//定义模块时引入依赖  
//var app = angular.module('app', ['ngCookies','ui.bootstrap', 'toastr']);
var app = angular.module('app', ['ui.bootstrap', 'toastr']);
//app.controller('setClassCtrl', function($scope, toastr,$cookies, $cookieStore,$modal,$window) {
app.controller('setClassCtrl', function($scope, toastr,$modal,$window) {
	$scope.setClass={
		classes:'',
		subject:'',
		sujectName:''
	}
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
	//切换班级
	$scope.changeClass=function(classes){
		$scope.setClass.classes=classes;
		angular.forEach($scope.classList,function(i){
			if($scope.setClass.classes==i.classId){
				$scope.classesobject=i;
			}
		})
		
	}
	//查询科目
	var _getsubject=function(){
		$scope.subjectlists= JSON.parse(execute_testPaper("get_subject"));
		if($scope.subjectlists.length>0){
			$scope.setClass.subject=$scope.subjectlists[0];
			$scope.setClass.subject1=angular.copy($scope.setClass.subject);
		}
	}
	//切换科目
	$scope.changeSubject=function(subject){
		$scope.setClass.subject	=subject;
	}
	//查询课程
	var _selectClassHour=function(){
		$scope.result=JSON.parse(execute_record("select_class_hour",$scope.setClass.classes,$scope.setClass.subject));
		console.log(JSON.stringify($scope.result))
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
	//切换课程
	$scope.changeClassHour=function(sujectName){
		$scope.setClass.sujectName=sujectName;
		angular.forEach($scope.classhourList,function(i){
			if($scope.setClass.sujectName==i.key){
				$scope.sujectNameobject=i;
			}
		})
		
	}
	//打开添加课时弹框
	$scope.addClassHour=function(){
		var item={
			classId:$scope.setClass.classes,
			className:$scope.classesobject.value,
			subjectName:$scope.setClass.subject
		}
		var modalInstance = $modal.open({
			templateUrl: 'addClassHourModal.html',
			controller: 'addClassHourCtrl',
			size: 'md',
			backdrop:false,
			resolve: {
				infos: function() {
					return item;
				}
			}
		});
		modalInstance.result.then(function(info) {
			_selectClassHour();
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
		
	}
	var _init=function(){
		_selectClass();
		_getsubject();
		_selectClassHour();
	}();
	//跳转到答题中心页面
	$scope.startClass=function(){
		/*var classInfo={
			classitem:$scope.setClass.classesobject,
			houritem:$scope.setClass.sujectNameobject,
			sujectitem:$scope.setClass.subject
		}*/
		// $cookieStore.put('classInfo',classInfo);
        // Get cookie
        //var favoriteCookie = $cookieStore.get('myFavorite');
        // Removing a cookie
       // $cookieStore.remove('myFavorite');
       var param={
       	classHourId:$scope.sujectNameobject.key,
       	classHourName:$scope.sujectNameobject.value
       }
       console.log(JSON.stringify($scope.setClass.classes))
      $scope.result=JSON.parse(execute_record("start_class",$scope.setClass.classes,JSON.stringify(param))); 
	    if($scope.result.ret=='success'){
	      	$scope.objectUrl = '../../page/answermoudle/answerCenter.html';
	      	$window.location.href = $scope.objectUrl;	
	    }
		//$scope.param = "classId=" + $scope.classesobject.key + "&className=" + $scope.classesobject.value + "&classhourid=" + $scope.sujectNameobject.key+"&classhourname=" +$scope.sujectNameobject.value+ "&suject="+$scope.setClass.subject;			
		//console.log(JSON.stringify($scope.param))
		//$scope.objectUrl = '../../page/answermoudle/answerCenter.html' + '?' + $scope.param;
		
		
		//window.location.href = "../../page/answermoudle/answerCenter.html?backurl=" + window.location.href;
		
	}
	//签到	
	$scope.signIn=function(){
		var param={
			classId:$scope.classesobject.key
		}
		$scope.result=JSON.parse(execute_attendance("sign_in_start",JSON.stringify(param)));
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
			/*$scope.param = "classId=" + $scope.setClass.classesobject.key + "&className=" + $scope.setClass.classesobject.value + "&classhourid=" + $scope.sujectNameobject.key+"&classhourname=" +$scope.sujectNameobject.value+ "&suject="+$scope.setClass.subject;			
			$scope.objectUrl = '../../page/answermoudle/userAttend.html' + '?' + $scope.param;
			$window.location.href =$scope.objectUrl;*/
		}else{
			toastr.error($scope.result.message);	
		}
	}
})
app.controller('addClassHourCtrl', function($rootScope,$scope,$modal,$modalInstance,toastr,infos) {	
	$scope.title="添加课程";
	$scope.classInfo={
		classHourName:''
	}
	if(infos){
		$scope.classInfo=angular.copy(infos);
		console.log(JSON.stringify(infos))
	}
	$scope.ok = function() {
		var param=$scope.classInfo;
		$scope.result=JSON.parse(execute_record("insert_class_hour",JSON.stringify(param)));
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
			$modalInstance.close(param);
		}else{
			toastr.error($scope.result.message);	
		}
		
	}
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
})
app.config(['$locationProvider', function($locationProvider) {  
	    //$locationProvider.html5Mode(true);  
 	$locationProvider.html5Mode({
	enabled: true,
	requireBase: false
	});
}]);
//设置签到控制器
app.controller('setSignCtrl', function($rootScope,$scope,$modal,toastr,$location,$window) {
	alert(777)
	if($location.search()){
		$scope.classInfo=$location.search();
		console.log(JSON.stringify($location.search()))
	}
	//签到
	$scope.signIn=function(){
		var param={
			classId:$scope.classInfo.classId
		}
		//$scope.result=JSON.parse(execute_attendance("sign_in_start",JSON.stringify(param)));
		//if($scope.result.ret=='success'){
			//toastr.success($scope.result.message);
			//console.log(JSON.stringify($scope.result))
			//$scope.param = "classId=" + $scope.setClass.classesobject.key + "&className=" + $scope.setClass.classesobject.value + "&classhourid=" + $scope.sujectNameobject.key+"&classhourname=" +$scope.sujectNameobject.value+ "&suject="+$scope.setClass.subject;			
		//console.log(JSON.stringify($scope.param))
		//$scope.objectUrl = '../../page/answermoudle/answerCenter.html' + '?' + $scope.param;
			$window.location.href = "../../page/answermoudle/userAttend.html";
		/*}else{
			toastr.error($scope.result.message);	
		}*/
	}
})
app.controller('userAttendCtrl', function($rootScope,$scope,$modal,toastr) {
	
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