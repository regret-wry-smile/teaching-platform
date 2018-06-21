//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
	app.controller('setStuManageCtrl', function($rootScope,$scope,$modal) {
	$scope.studenList=[];//学生列表数组
		/*获取学生*/
	var _select_student = function(val) {
		var param = {
				classId:'BJ1001'
		}
		param =JSON.stringify(param);
		$scope.result = JSON.parse(execute_student("select_student",param));
		$scope.studenList=$scope.result.item;
		alert(JSON.stringify($scope.result))
	};
	var _init=function(){
		_select_student();
	}();
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
	//全选
	$scope.selectAll = function(data) {
		if($scope.selected) {
			$scope.onechecked = [];
			angular.forEach($scope.studentList, function(i) {
				i.checked = true;
				var item = i;
				$scope.checkedId.push(i.id.toString());
				$scope.onechecked.push(item);

			})
		} else {
			angular.forEach($scope.studentList, function(i) {
				i.checked = false;
				$scope.onechecked = [];
				$scope.checkedId = [];
			})
		}

	};
	//单选
	$scope.selectOne = function(param) {
		$scope.onechecked = [];
		$scope.checkedId = [];
		angular.forEach($scope.studentList, function(i) {
			var index = $scope.checkedId.indexOf(i.id);
			//console.log(JSON.stringify(index));
			if(i.checked && index === -1) {
				var item = i;
				$scope.onechecked.push(item);
				$scope.checkedId.push(i.id.toString());

			} else if(!i.checked && index !== -1) {
				$scope.selected = false;
				$scope.onechecked.splice(index, 1);
				$scope.checkedId.splice(index, 1);
			};
		})

		if($scope.studentList.length === $scope.onechecked.length) {
			$scope.selected = true;
		} else {
			$scope.selected = false;
		}
		console.log($scope.onechecked);
	}
	
	//删除学生
	$scope.deleteStudent=function(){
		/*if($scope.checkedId.length>0){*/
			var content="删除选中学生";
			var modalInstance = $modal.open({
				templateUrl: 'sureModal.html',
				controller: 'sureModalCtrl',
				size: 'sm',
				resolve: {
					content: function() {
						return content;
					}
				}
			});

			modalInstance.result.then(function(info) {
				//student_manage("delete_student",$rootScope.className,$scope.checkedId);
				$scope.onechecked = [];
				$scope.checkedId = [];
				$scope.selected=false;
			}, function() {

				//$log.info('Modal dismissed at: ' + new Date());
			});
		/*}else{
			toastr.warning("请选择学生");
		}*/
		
	}
	//清除白名单
	$scope.clearStu=function(){
		var content="清除白名单";
		var modalInstance = $modal.open({
			templateUrl: 'sureModal.html',
			controller: 'sureModalCtrl',
			size: 'sm',
			resolve: {
				content: function() {
					return content;
				}
			}
		});

		modalInstance.result.then(function(info) {
			//发送指令
			student_manage("clear_bind",$rootScope.className);
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
	app.controller('findBindModalCtrl',function($scope,$modalInstance,$interval){
		//student_manage("start_bind",info);
		//定时器
		$scope.ok = function() {
			/*关闭定时器*/
			/*$interval.cancel(myTimer);*/
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