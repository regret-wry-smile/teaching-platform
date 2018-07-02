//定义模块时引入依赖  
var app = angular.module('app', ['ui.bootstrap', 'toastr']);
//作答记录
app.controller('answerRecordCtrl', function($scope, toastr,$modal) {
	$scope.setClass = {
		classes: '', //班级id
		subject: '', //科目名称
		sujectHour: '', //课程id
		paper: '', //试卷id
		sujectHour1: ''
	}
	$scope.classList = []; //班级数组
	$scope.subjectlists = []; //科目数组
	$scope.classhourList = [] //课程数组
	$scope.paperList = []; //试卷数组
	$scope.recordList = [] //作答记录数组
	$scope.onechecked = [];
	$scope.checkedId = [];
	$scope.checkedstudentIds=[];//学生id数组
	$('#myModal').modal('hide');
	//隐藏loading
	var _hideModal=function(){
		$('#myModal').modal('hide');
	}
	//显示loading
	var _showModal=function(){
		$('#myModal').modal('show');
	}
	
		/*查询班级列表*/
	var _selectClass = function() {
		$scope.result = JSON.parse(execute_student("select_class"));
		if($scope.result.ret == 'success') {
			$scope.classList=[];
			if($scope.result.item.length > 0) {
				angular.forEach($scope.result.item, function(i) {
					var item = {
						key: i.classId,
						value: i.className
					}
					$scope.classList.push(item);
					console.log("班级"+JSON.stringify($scope.classList))
					$scope.setClass.classes = $scope.classList[0].key;
					$scope.classesobject = $scope.classList[0];
					$scope.setClass.classes1 = angular.copy($scope.setClass.classes);

				})
			}
		} else {
			toastr.error($scope.result.message);
		}
	};
	//查询课程
	var _selectClassHour = function() {
		$scope.result = JSON.parse(execute_record("select_class_hour", $scope.setClass.classes, $scope.setClass.subject));
		//console.log(JSON.stringify($scope.result))
		$scope.classhourList=[];
		if($scope.result.ret == 'success') {			
			if($scope.result.item.length > 0) {
				angular.forEach($scope.result.item, function(i) {
					var item = {
						key: i.class_hour_id,
						value: i.class_hour_name
					}					
					$scope.classhourList.push(item);
					if($scope.classhourList.length > 0) {
						$scope.setClass.sujectHour = $scope.classhourList[0].key;
						$scope.sujectHourobject = $scope.classhourList[0] || {};
						$scope.setClass.sujectHour1 = angular.copy($scope.setClass.sujectHour);	
						_selectPaper();
					}

				})
			}else{
				$scope.classhourList=[];
				$scope.paperList=[];
			}
		} else {
			toastr.error($scope.result.message);
		}
	}
	
	//查询科目
	var _getsubject = function() {
		$scope.subjectlists=[];
		$scope.subjectlists = JSON.parse(execute_testPaper("get_subject"));
		if($scope.subjectlists.length > 0) {
			$scope.setClass.subject = $scope.subjectlists[0];
			$scope.setClass.subject1 = angular.copy($scope.setClass.subject);
			_selectClassHour();
		}
	}
	

	//查询试卷
	var _selectPaper = function() {
		var param = {
			classHourId: $scope.setClass.sujectHour
		}
		console.log("课程id"+JSON.stringify(param))
		$scope.result = JSON.parse(execute_testPaper("select_paper_by_classHourId", JSON.stringify(param)));
		console.log(JSON.stringify($scope.result));
		$scope.paperList=[];
		if($scope.result.ret == 'success') {			
			if($scope.result.item.length > 0) {
				angular.forEach($scope.result.item, function(i) {
					var item = {
						key: i.testId,
						value: i.testName
					}
					
					$scope.paperList.push(item);
					if($scope.paperList.length > 0) {
						$scope.setClass.paper = $scope.paperList[0].key;
						$scope.paperobject = $scope.paperList[0];
						console.log("试卷" + JSON.stringify($scope.paperobject));
						$scope.setClass.paper1 = angular.copy($scope.setClass.paper);
					}

				})
			} else {
				$scope.paperList = [];
			}
		} else {
			toastr.error($scope.result.message);
		}
	}
	

	//查询记录
	var _selectRecord = function() {
		if($scope.setClass.sujectHour && $scope.setClass.paper) {
			var param = {
				classId: $scope.setClass.classes,
				subject: $scope.setClass.subject,
				testId: $scope.setClass.paper,
				classHourId: $scope.setClass.sujectHour
			}

			_showModal();
			//console.log("记录" + JSON.stringify(param))
			$scope.result = JSON.parse(execute_record("select_record", JSON.stringify(param)));
			//console.log("记录" + JSON.stringify($scope.result))
			/*if($scope.result.ret == 'success') {
				$scope.recordList=[];
				$scope.recordList = $scope.result.item;
			} else {
				toastr.error($scope.result.message);
			}*/
		}
	}
	//_selectRecord();
	//切换班级

	$scope.changeClass = function(classes) {
			$scope.setClass.classes = classes;
			angular.forEach($scope.classList, function(i) {
				if($scope.setClass.classes == i.key) {
					$scope.classesobject = i;			
					_selectClassHour();
					_selectRecord();

				}
			})

		}
		//切换科目
	$scope.changeSubject = function(subject) {
		$scope.setClass.subject = subject;
		_selectClassHour();
		_selectRecord();
	}

	//切换课程
	$scope.changeClassHour = function(sujectHour) {
		$scope.setClass.sujectHour = sujectHour;
		angular.forEach($scope.classhourList, function(i) {
			if($scope.setClass.sujectHour == i.key) {
				$scope.sujectHourobject = i;
				_selectPaper();
				_selectRecord();
			}
		})

	}
	//切换试卷
	$scope.changePaper=function(paper){
		$scope.setClass.paper = paper;		
		angular.forEach($scope.paperList, function(i) {
			if($scope.setClass.paper == i.key) {
				$scope.paperobject = i;
				_selectRecord();
			}
		})
	}
	var _init = function() {
		_selectClass();
		_getsubject();
		_selectClassHour();
		_selectPaper();
		_selectRecord();
	}();
		//全选
	$scope.selectAll = function(data) {
		if($scope.selected) {
			$scope.onechecked = [];
			angular.forEach($scope.recordList, function(i) {
				i.checked = true;
				var item = i;
				$scope.checkedId.push(i.id.toString());
				$scope.onechecked.push(item);

			})
		} else {
			angular.forEach($scope.recordList, function(i) {
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
		angular.forEach($scope.recordList, function(i) {
			//console.log("是什么"+JSON.stringify(i))
			var index = $scope.checkedId.indexOf(i.id);
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

		if($scope.recordList.length === $scope.onechecked.length) {
			$scope.selected = true;
		} else {
			$scope.selected = false;
		}
	}

	//删除记录
	$scope.deleteRcord = function() {
		if($scope.onechecked.length > 0) {
			var content = "删除选中记录";
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
				for(var i=0;i<$scope.onechecked.length;i++){
					$scope.checkedstudentIds.push($scope.onechecked[i].studentId);
				}
				var param = {
					testId: $scope.setClass.paper,
					studentIds: $scope.checkedstudentIds
				}
				console.log(JSON.stringify(param))
				$scope.result = JSON.parse(execute_record("delete_record", JSON.stringify(param)));
				if($scope.result.ret == 'success') {
					toastr.success($scope.result.message);
					_selectRecord();
					$scope.onechecked = [];
					$scope.checkedId = [];
					$scope.selected = false;
				} else {
					toastr.error($scope.result.message);
				}

			}, function() {

				//$log.info('Modal dismissed at: ' + new Date());
			});
		} else {
			toastr.warning("请选择记录");
		}

	}
	
	//导出
	$scope.exportRecord=function(){		
		if($scope.setClass.classes&&$scope.setClass.paper&&$scope.setClass.sujectHour&&$scope.setClass.classes){
			var param={
				classId:$scope.setClass.classes,
				subject:$scope.setClass.subject,
				classHourId:$scope.setClass.sujectHour,
				testId:$scope.setClass.paper
			}	
			
			$scope.result=JSON.parse(execute_record('test_export',JSON.stringify(param)));
			if($scope.result.ret=='success'){				
				//toastr.success($scope.result.message);
			}else{
				toastr.error($scope.result.message);
				console.log(JSON.stringify($scope.result.message))
			}
		}else{
			toastr.warning("缺少必要条件，不能导出");
		}
		
	}
	//显示loading
	$scope.showLoading=function(){
		_showModal();
	}
	//显示loading
	$scope.removeLoading=function(){
		_hideModal();
	}
	//提示框
	$scope.getTip = function() {
		if (ret == 'true') {
			toastr.success(message);
		}
		else {
			toastr.error(message);
		}
	}
	$scope.refreSelectRecord = function(){
		var retDate = JSON.parse(result);
		if(result.ret == 'error'){
			toastr.error("查询失败！");
		}else{
			$scope.recordList=[];
			$scope.recordList = retDate.item;
		}
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
/*app.directive('select', function() {
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
})*/
app.directive('select', function() {
	return {
		restrict: 'A',
		require: 'ngModel',
		scope: {
			defalutvalue: '=?'
		},
		link: function(scope, element, attrs, ngModelCtr) {
			scope.$watch('defalutvalue', function() {
				if(scope.defalutvalue) {
					$(element).multiselect({
						width: "10rem",
						multiple: false,
						selectedHtmlValue: '请选择',
						defalutvalue: scope.defalutvalue,
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
		scope: {
			defalutvalue: '=?',
			list: '=?'
		},
		link: function(scope, element, attrs, ngModelCtr) {
			scope.$watch('defalutvalue+list', function() {

				if(scope.defalutvalue) {
					if(scope.list) {
						var str = '';
						for(var i = 0; i < scope.list.length; i++) {
							str += '<option value="' + scope.list[i] + '">' + scope.list[i] + '</option>';
						}
						if(str) {
							$(element).html(str);
						}
					}

					$(element).multiselect({
						multiple: false,
						selectedHtmlValue: '请选择',
						defalutvalue: scope.defalutvalue,
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
		scope: {
			defalutvalue: '=?',
			list: '=?'
		},
		link: function(scope, element, attrs, ngModelCtr) {
			scope.$watch('defalutvalue+list', function() {
				if(scope.defalutvalue) {
					if(scope.list) {
						var str = '';
						for(var i = 0; i < scope.list.length; i++) {
							str += '<option value="' + scope.list[i].key + '">' + scope.list[i].value + '</option>';
						}
						$(element).html(str);
					}
					
				}
				$(element).multiselect({
						multiple: false,
						selectedHtmlValue: '请选择',
						defalutvalue: scope.defalutvalue,
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
			});
			

		}
	}
})