//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
	app.controller('setStuManageCtrl', function($rootScope,$scope,$modal) {
		//批量导入学生
		$scope.patchImport = function() {
			var modalInstance = $modal.open({
			templateUrl: 'importFile.html',
			controller: 'uploadfileModalCtrl',
			size: 'md',
			/*resolve: {
				infos: function() {
					return items;
				}
			}*/
		});

		modalInstance.result.then(function(info) {
		}, function() {
		});
			//$state.go('setmodule.set')
		}
		//返回
		$scope.returnPage=function(){
			 window.location.href="../../page/setmodule/setmodule.html"; 
		}
		
		//打开添加班级弹框
	$scope.addClass = function() {
		alert(777)
		var modalInstance = $modal.open({
			templateUrl: 'addClassModal.html',
			controller: 'addClassModalCtrl',
			size: 'md',
			/*resolve: {
				infos: function() {
					return $scope.classes.length;
				}
			}*/
		});
		modalInstance.result.then(function(info) {
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	};
	//打开新增学生弹框
	$scope.addStudent = function() {
		var modalInstance = $modal.open({
			templateUrl: 'addStudentModal.html',
			controller: 'addStudentModalCtrl',
			size: 'md',
			/*resolve: {
				infos: function() {
					return $rootScope.className;
				}
			}*/
		});

		modalInstance.result.then(function(info) {

		}, function() {

			//$log.info('Modal dismissed at: ' + new Date());
		});
	};
	//编辑学生
	$scope.updateStdent=function(items){
		var modalInstance = $modal.open({
			templateUrl: 'addStudentModal.html',
			controller: 'editStudentModalCtrl',
			size: 'md',
			/*resolve: {
				infos: function() {
					return items;
				}
			}*/
		});

		modalInstance.result.then(function(info) {

		}, function() {

			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
	
	//一键配对
	$scope.quickBind=function(){
		var content="一键配对";
		var modalInstance = $modal.open({
			templateUrl: 'findBindModal.html',
			controller: 'findBindModalCtrl',
			size: 'sm',
			backdrop:false,
			/*resolve: {
				info: function() {
					return $rootScope.className;
				}
			}*/
		});
		modalInstance.result.then(function(info) {
			//_SELECTSTUDENT($rootScope.className);
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
		
	}
	})
	
	app.controller('uploadfileModalCtrl', function($scope,$modalInstance) {
		$scope.ok = function() {
			$modalInstance.close();
		}
		$scope.cancel=function(){
			$modalInstance.dismiss('cancel');
		}
	})
	//确认弹出框
	app.controller('sureModalCtrl',function($scope,$modalInstance,toastr,content){
		$scope.content='是否进行'+angular.copy(content)+'操作？';
		$scope.ok = function() {
			$modalInstance.close('success');
		}
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		}
	})
	//匹配绑定
	app.controller('findBindModalCtrl',function($scope,info,$modalInstance,$interval){
		//student_manage("start_bind",info);
		//定时器
		$scope.ok = function() {
			/*关闭定时器*/
			$interval.cancel(myTimer);
			$modalInstance.close();
		}
	})
	//添加学生控制器
	app.controller('addStudentModalCtrl',function($scope,$modalInstance){
	$scope.title="添加学生";
	$scope.student={
		num:'',
		stuNum:'',
		stuName:'',
		deviceNum:''
	}
	
	//$scope.blurStunum=function(){
		//var reg="/^[0-9]*/";
		/*if(!reg.test($scope.student.stuNum)){
		
			toastr.warning("数字长度不能超过10位");
		}else 
		if($scope.student.stuNum==undidined){
			toastr.warning("数字长度不能超过10位");
		}
	}
	$scope.blurStuname=function(){		
		if(!$scope.student.stuName.length>10){
			toastr.warning("长度不能超过10位");
		}
	}
	$scope.blurDevicenum=function(){
		
	}*/
	
	$scope.ok = function() {
		var param = $scope.student;
		//student_manage("add_student",infos,param.stuNum.toString(),param.stuName,param.deviceNum.toString());
		$modalInstance.close('success');
	}
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
})
//匹配绑定
app.controller('findBindModalCtrl',function($scope,info,$modalInstance,$interval){
	//student_manage("start_bind",info);
	$scope.ok = function() {
		/*关闭定时器*/
		$interval.cancel(myTimer);
		$modalInstance.close();
	}
})
//添加班级
app.controller('addClassModalCtrl',function($scope,$modalInstance,$rootScope){
	$scope.classes = {
		type:"本地",
		name:""
	}
	$scope.ok = function() {
		var param = $scope.classes;
		//student_manage("add_class",param.name,param.type,$rootScope.className);
		if(infos==0){
			$rootScope.className = param.name;
		}
		$modalInstance.close('success');
	}
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
})