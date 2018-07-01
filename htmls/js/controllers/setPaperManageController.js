//定义模块时引入依赖  
var app = angular.module('app', ['ui.bootstrap', 'toastr']);
app.controller('setPaperManageCtrl', function($rootScope, $scope, $modal, toastr, $window) {
	$('#myModal').modal('hide');
		//隐藏loading
	var _hideModal=function(){
		$('#myModal').modal('hide');
	}
	//显示loading
	var _showModal=function(){
		$('#myModal').modal('show');
	}
	$scope.testInfolist=[];//服务试卷数组
		//返回设置页面
		$scope.returnPage = function() {
				window.location.href = "../../page/setmodule/setmodule.html";
			}
			//跳转到添加试卷页面
		$scope.addTestPaper = function() {
			var modalInstance = $modal.open({
				templateUrl: 'selsubjectModal.html',
				controller: 'selsubjectCtrl',
				size: 'md',
				backdrop: false,
				resolve: {
					/*content: function() {
						return content;
					}*/
				}
			});
			modalInstance.result.then(function(info) {
				if(info) {
					$scope.subject = angular.copy(info); //选择的科目
					$scope.testId = execute_testPaper("create_test_id");
					$scope.param = "testId=" + $scope.testId + "&subject=" + $scope.subject;
					console.log(JSON.stringify($scope.param))
					$scope.objectUrl = '../../page/setmodule/addtestPage.html' + '?' + $scope.param;
					$window.location.href = $scope.objectUrl;
				}
			}, function() {
				//$log.info('Modal dismissed at: ' + new Date());
			});

		}

		//查询所有试卷
		var _selectPaper = function() {
			var param = {};
			var result = JSON.parse(execute_testPaper("select_paper", JSON.stringify(param)));
			console.log(JSON.stringify(result));
			if(result.ret == 'success') {
				$rootScope.paperInfoList = result.item;
			} else {
				toastr.error(result.message);
			}

		};
		//$rootScope.paperInfoList=[{"atype":"1","classHourId":"","describe":"6546","id":7,"remark":"","subject":"语文","testId":"4Y0001","testName":"中天测试"},{"atype":"1","classHourId":"","describe":"54256436","id":8,"remark":"","subject":"语文","testId":"T11","testName":"景县助手器测试"},{"atype":"0","classHourId":"","describe":"单选、多选、判断、数字","id":6,"remark":"","subject":"语文","testId":"CS1001","testName":"120题测试"},{"atype":"0","classHourId":"","describe":"6436","id":9,"remark":"","subject":"数学","testId":"fdce0e256f0b469492e81e5decc6ff5a","testName":"643"},{"atype":"0","classHourId":"","describe":"5325","id":10,"remark":"","subject":"外语","testId":"4c02d72b2275492e8e29c414d4e2c539","testName":"32"},{"atype":"0","classHourId":"","describe":"5236","id":11,"remark":"","subject":"语文","testId":"89ef955b77c24e2d9753e0b9bcc0b04d","testName":"32"},{"atype":"0","classHourId":"","describe":"6243623","id":12,"remark":"","subject":"语文","testId":"2b21f81469264fd9b7a98ed62f5b3cd7","testName":"32"},{"atype":"0","classHourId":"","describe":"243","id":13,"remark":"","subject":"语文","testId":"c228abc801194db1a4a2a3ee3767cbb2","testName":"623"},{"atype":"0","classHourId":"","describe":"51225","id":14,"remark":"","subject":"语文","testId":"18a66b04b59c49eabc30699abc6ee182","testName":"1"}];
		_selectPaper();
		//导入试卷
		$scope.importPaper = function() {
				var modalInstance = $modal.open({
					templateUrl: 'importFile.html',
					controller: 'uploadfileModalCtrl',
					size: 'md',
					backdrop: false
				});

				modalInstance.result.then(function(selitem) {
					
					$rootScope.selitem=selitem;
					console.log("班级和科目"+JSON.stringify(selitem))
					_selectPaper();
				}, function() {});
			}
			//编辑试卷
		$scope.editPaper = function(item) {
			$scope.param = "atype=" + item.atype + "&describe=" + item.describe + "&id=" + item.id + "&subject=" + item.subject + "&testId=" + item.testId + "&testName=" + item.testName;
			$scope.objectUrl = '../../page/setmodule/edittestPage.html' + '?' + $scope.param;
			console.log(JSON.stringify(item))
			$window.location.href = $scope.objectUrl;

			//window.location.href="../../page/setmodule/edittestPage.html";
		}
		

		//删除题目 
		$scope.delPaper = function(item) {
			var content = "删除题目";
			var modalInstance = $modal.open({
				templateUrl: 'sureModal.html',
				controller: 'sureModalCtrl',
				size: 'sm',
				backdrop: false,
				resolve: {
					content: function() {
						return content;
					}
				}
			});
			modalInstance.result.then(function(info) {
				var param = {
					id: item.id,
					testId: item.testId
				}
				console.log(JSON.stringify(param))
				param = JSON.stringify(param)
				$scope.result = JSON.parse(execute_testPaper("delete_paper", param));
				if($scope.result.ret == 'success') {
					toastr.success($scope.result.message);
					_selectPaper();
				} else {
					toastr.error($scope.result.message);
				}
			}, function() {
				//$log.info('Modal dismissed at: ' + new Date());
			});
		}
		
		$scope.refreTestPaper=function(){
			console.log("ahhaha"+JSON.stringify(testinfo))
			$scope.testInfolist=JSON.parse(testinfo).testInfo;
			/*var selitem = {
				classId: $scope.selclass,
				subject: $scope.selsubject
			}*/
			var modalInstance = $modal.open({
				templateUrl: 'selserverFile.html',
				controller: 'serverFileModalCtrl',
				size: 'md',
				backdrop: false,
				resolve: {
					infos: function() {
						return $scope.testInfolist;
					},
					subjectInfo: function() {
						return $rootScope.selitem;
					}

				}
			});

			modalInstance.result.then(function(info) {
				/*//查询所有试卷
				var param = {};
				var result = JSON.parse(execute_testPaper("select_paper", JSON.stringify(param)));
				console.log(JSON.stringify(result));
				if(result.ret == 'success') {
					$rootScope.paperInfoList = [];
					$rootScope.paperInfoList = result.item;
				} else {
					toastr.error(result.message);
				}*/
			}, function() {});
			
			
		}
		//查询所有列表
		$scope.refreTestPaper2=function(){
			_selectPaper();
		}
		$scope.showLoading=function(){
			_showModal();
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
	$scope.removeLoading=function(){
		_hideModal();
	}
})
	//选择科目控制器
app.controller('selsubjectCtrl', function($rootScope, $scope, $modal, $modalInstance, toastr) {
	$scope.sujectlists = []; //科目数组
	$scope.sujectlists = JSON.parse(execute_testPaper("get_subject"));
	if($scope.sujectlists.length > 0) {
		$scope.selsubject = $scope.sujectlists[0];
		$scope.selsubject1 = angular.copy($scope.selsubject);
	}
	$scope.ok = function() {
		var param = $scope.selsubject;
		$modalInstance.close(param);
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
//添加试卷控制器
app.controller('addPaperManageCtrl', function($rootScope, $scope, $modal, toastr, $location, $window) {
	if($location.search()) {
		$scope.testId = $location.search().testId;
		$scope.subject = $location.search().subject;
	}
	$scope.paperInfo = {
		testName: '', //试卷名称
		describe: '' //试卷描述
	}
	$scope.subjectList = []; //题目数组
	//返回返回试卷管理页面
	$scope.returnPage = function() {
			window.location.href = "../../page/setmodule/testPaperManage.html";
		}
		//添加题目
	$scope.addSubject = function() {
			var modalInstance = $modal.open({
				templateUrl: 'addSubjectModal.html',
				controller: 'addSubjectModalCtrl',
				size: 'md',
				backdrop: false,
				resolve: {
					infos: function() {
						return $scope.testId;
					}
				}
			});
			modalInstance.result.then(function(info) {
				var param = {
					testId: $scope.testId,
				}
				console.log(JSON.stringify(param))
				$scope.result = JSON.parse(execute_testPaper("select_question", JSON.stringify(param)));
				if($scope.result.ret == 'success') {
					$scope.subjectList = $scope.result.item;

				} else {
					toastr.error($scope.result.message);
				}

			}, function() {
				//$log.info('Modal dismissed at: ' + new Date());
			});
		}
		//保存试卷
	$scope.savePaper = function() {
		if($scope.subjectList.length > 0) {
			var param = {
				testId: $scope.testId,
				subject: $scope.subject,
				testName: $scope.paperInfo.testName, //试卷名称
				describe: $scope.paperInfo.describe, //试卷描述			
			}
			$scope.result = JSON.parse(execute_testPaper("insert_paper", JSON.stringify(param)));
			if($scope.result.ret == 'success') {
				toastr.success($scope.result.message);
				history.go(-1);
			} else {
				toastr.error($scope.result.message);
				console.log(JSON.stringify($scope.result.detail))
			}
		} else {
			toastr.warning('该试卷没有题目，请先添加题目');
		}
	}

})

//编辑试卷控制器
app.controller('editPaperManageCtrl', function($rootScope, $scope, $modal, $location, $window, toastr) {
		//console.log(JSON.stringify($location.search()))
		if($location.search()) {
			$scope.paperInfo = $location.search();
		}
		$scope.subjectList = []; //题目数组
		$scope.checkedId = [];
		$scope.onechecked = [];
		//查询该试卷的题目
		var _selectQuestion = function() {
			var param = {
				testId: $scope.paperInfo.testId
			}
			console.log(JSON.stringify(param));
			$scope.result = JSON.parse(execute_testPaper("select_question", JSON.stringify(param)));
			console.log(JSON.stringify($scope.result));
			if($scope.result.ret == 'success') {
				$scope.subjectList = $scope.result.item;
			} else {
				toastr.error($scope.result.message);
			}
		}

		//全选
		$scope.selectAll = function(data) {
			if($scope.selected) {
				$scope.onechecked = [];
				angular.forEach($scope.subjectList, function(i) {
					i.checked = true;
					var item = i;
					$scope.checkedId.push(i.id.toString());
					$scope.onechecked.push(item);

				})
			} else {
				angular.forEach($scope.subjectList, function(i) {
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
			angular.forEach($scope.subjectList, function(i) {
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

			if($scope.subjectList.length === $scope.onechecked.length) {
				$scope.selected = true;
			} else {
				$scope.selected = false;
			}
		}
		var _init = function() {
			_selectQuestion();
		}();
		//添加题目
		$scope.addSubject = function() {
				var modalInstance = $modal.open({
					templateUrl: 'addSubjectModal.html',
					controller: 'addSubjectModalCtrl',
					size: 'md',
					backdrop: false,
					resolve: {
						infos: function() {
							return $scope.paperInfo.testId;
						}
					}
				});
				modalInstance.result.then(function(info) {
					_selectQuestion();

				}, function() {
					//$log.info('Modal dismissed at: ' + new Date());
				});
			}
			//编辑题目
		$scope.editSuject = function(item) {
				console.log(JSON.stringify(item))
				var modalInstance = $modal.open({
					templateUrl: 'addSubjectModal.html',
					controller: 'editSubjectModalCtrl',
					size: 'md',
					backdrop: false,
					resolve: {
						infos: function() {
							return item;
						}
					}
				});
				modalInstance.result.then(function(info) {
					_selectQuestion();
				}, function() {
					//$log.info('Modal dismissed at: ' + new Date());
				});
			}
			//删除题目 
		$scope.delSuject = function(item) {
				if($scope.checkedId.length > 0) {
					var content = "删除题目";
					var modalInstance = $modal.open({
						templateUrl: 'sureModal.html',
						controller: 'sureModalCtrl',
						size: 'sm',
						backdrop: false,
						resolve: {
							content: function() {
								return content;
							}
						}
					});
					modalInstance.result.then(function(info) {
						var param = $scope.checkedId;
						$scope.result = JSON.parse(execute_testPaper("delete_question", JSON.stringify(param)));
						if($scope.result.ret == 'success') {
							toastr.success($scope.result.message);
							_selectQuestion();
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
					toastr.warning("请先勾选至少一个题目");
				}
			}
			//修改试卷
		$scope.savePaper = function() {
			if($scope.subjectList.length > 0) {
				var param = {
						id: $scope.paperInfo.id,
						testId: $scope.paperInfo.testId,
						subject: $scope.paperInfo.subject,
						testName: $scope.paperInfo.testName, //试卷名称
						describe: $scope.paperInfo.describe, //试卷描述			
					}
					//console.log(JSON.stringify(param))
				$scope.result = JSON.parse(execute_testPaper("update_paper", JSON.stringify(param)));
				if($scope.result.ret == 'success') {
					toastr.success($scope.result.message);
					history.go(-1);
				} else {
					toastr.error($scope.result.message);
					console.log(JSON.stringify($scope.result.detail))
				}
			} else {
				toastr.warning('该试卷没有题目，请先添加题目');
			}
		}
	})
	//新增题目控制器
app.controller('addSubjectModalCtrl', function($rootScope, $modalInstance, $scope, $modal, toastr, infos) {
		$scope.title = "新增题目";		
		$scope.letterList=[{key:"0",value:"单选"},{key:"1",value:"多选"}];		
		$scope.rangeList=[{key:"A-D",value:"A-D"},{key:"A-F",value:"A-F"}];
		$scope.testInfo = {
			questionType: '2', //(0单选，1多选，2判断，3数字)
			selType: $scope.letterList[0].key,
			/*range:'A-D',*/
			trueAnswer: 'T'
		}
		
		if(infos) {
			$scope.testId = angular.copy(infos);
		}
		$scope.testInfo.questionType1 = angular.copy($scope.testInfo.questionType);
		$scope.testInfo.selType1=angular.copy($scope.testInfo.selType);
		$scope.testInfo.range=$scope.rangeList[0].key;
		$scope.testInfo.range1=angular.copy($scope.testInfo.range);
		//切换答案类型
		$scope.changequesType = function(quesType) {
			$scope.testInfo.questionType = quesType;
			if($scope.testInfo.questionType == '-1') {
				$scope.testInfo.trueAnswer = '';
				$scope.testInfo.selType = $scope.letterList[0].key;
				$scope.testInfo.range = $scope.rangeList[0].key;
				$scope.testInfo.selType1 = angular.copy($scope.testInfo.selType);
				$scope.testInfo.range1 = angular.copy($scope.testInfo.range);
			} else if($scope.testInfo.questionType == '2') {
				$scope.testInfo.trueAnswer = 'T';
			} else if($scope.testInfo.questionType == '3') {
				$scope.testInfo.trueAnswer = '';
			}
		}
		
		//查询该试卷的题目
		var _selectQuestion = function() {
			var param = {
				testId: $scope.testId,
				questionId:$scope.testInfo.questionId
			}
			console.log(JSON.stringify(param));
			$scope.result = JSON.parse(execute_testPaper("select_question", JSON.stringify(param)));
			console.log(JSON.stringify($scope.result));
			if($scope.result.ret == 'success') {
				if($scope.result.item.length>0){
					toastr.warning("该题号已存在，请重新输入");
					setTimeout(function(){
						$scope.testInfo.questionId='';
					},800)
				}
			} else {
				toastr.error($scope.result.message);
			}
		}

		//校验题号
		$scope.blurtitle=function(){
			if($scope.testInfo.questionId){
				_selectQuestion();
			}
		}
		var _test=function(){
			if($scope.testInfo.trueAnswer){
				if($scope.testInfo.questionType == '-1') {				
				if($scope.testInfo.range == 'A-D'){
					var regu1 = /^[A-D]$/;
					var re = new RegExp(regu1);
					if(re.test($scope.testInfo.trueAnswer)) {
					} else {
						toastr.warning("请输入A-D单选字母");
						setTimeout(function() {
							$scope.testInfo.trueAnswer = ''
						}, 800)
						return false;
					}
				}else{
					var regu3 = /^[A-F]$/;
					var re = new RegExp(regu3);
					if(re.test($scope.testInfo.trueAnswer)) {} else {
						toastr.warning("请输入A-F单选字母");
						setTimeout(function() {
							$scope.testInfo.trueAnswer = ''
						}, 800)
						return false;
					}
				}
			}else{
				if($scope.testInfo.range == 'A-D'){
					var regu2 = /^[A-D]+$/;
					var re = new RegExp(regu2);
					if(re.test($scope.testInfo.trueAnswer)) {} else {
						toastr.warning("请输入A-D多选字母");
						setTimeout(function() {
							$scope.testInfo.trueAnswer = ''
						}, 800)
						return false;
					}
				}else{
					var regu4 = /^[A-F]+$/;
					var re = new RegExp(regu4);
					if(re.test($scope.testInfo.trueAnswer)) {} else {
						toastr.warning("请输入A-F多选字母");
						setTimeout(function() {
							$scope.testInfo.trueAnswer = ''
						}, 800)
						return false;
					}
				}
			}
			}
		}
		$scope.changeselType = function(selType) {
			$scope.testInfo.selType = selType;
			//alert(JSON.stringify(selType))
			_test();

		}
		//检验对错
		$scope.changeJudge = function(trueAnswer) {
			$scope.testInfo.trueAnswer = trueAnswer;
			_test();
			
		}
		$scope.changeRange=function(range){
			$scope.testInfo.range=range;
			_test();
		}
		$scope.ok = function() {
			if($scope.testInfo.questionType == '-1') {
				alert($scope.testInfo.selType)
				if($scope.testInfo.selType=='0'){
					$scope.testInfo.questionType = '0';
				}else{
					$scope.testInfo.questionType = '1';
				}
				if($scope.testInfo.selType=='单选'){
					$scope.testInfo.questionType = '0';
				}else if($scope.testInfo.selType=='多选'){
					$scope.testInfo.questionType = '1';
				}
			}
			var param = {
				//id:$scope.idnum,
				testId: $scope.testId,
				questionId: $scope.testInfo.questionId,
				question: $scope.testInfo.question,
				questionType: $scope.testInfo.questionType,
				trueAnswer: $scope.testInfo.trueAnswer,
				range: $scope.testInfo.range
			}
			console.log("参数" + JSON.stringify(param))
			$scope.result = JSON.parse(execute_testPaper("insert_question", JSON.stringify(param)));
			if($scope.result.ret == 'success') {
				toastr.success($scope.result.message);
				$modalInstance.close('success');
			} else {
				toastr.error($scope.result.message);
			}
		}
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		}
	})
	//编辑题目控制器
app.controller('editSubjectModalCtrl', function($rootScope, $modalInstance, $scope, $modal, toastr, infos) {
		$scope.title = "编辑题目";		
		$scope.letterList=[{"key":"0",value:"单选"},{"key":"1",value:"多选"}];		
		$scope.rangeList=[{"key":"A-D",value:"A-D"},{"key":"A-F",value:"A-F"}];
		if(infos) {
			$scope.testInfo = angular.copy(infos);
			if(typeof $scope.testInfo.questionId == 'string') {
				$scope.testInfo.questionId = parseInt($scope.testInfo.questionId);
			}
			if($scope.testInfo.questionType == '0' || $scope.testInfo.questionType == '1') {
				$scope.testInfo.selType = $scope.testInfo.questionType;
				$scope.testInfo.selType1 = angular.copy($scope.testInfo.selType);
				$scope.testInfo.questionType = '-1';
			}
			$scope.testInfo.questionType1 = angular.copy($scope.testInfo.questionType);
			$scope.testInfo.range1 = angular.copy($scope.testInfo.range);
		}
		
		//查询该试卷的题目
		var _selectQuestion = function() {
			var param = {
				testId: $scope.testInfo.testId,
				questionId:$scope.testInfo.questionId
			}
			console.log(JSON.stringify(param));
			$scope.result = JSON.parse(execute_testPaper("select_question", JSON.stringify(param)));
			console.log(JSON.stringify($scope.result));
			if($scope.result.ret == 'success') {
				if($scope.result.item.length>0){
					toastr.warning("该题号已存在，请重新输入");
					setTimeout(function(){
						$scope.testInfo.questionId='';
					},800)
				}
			} else {
				toastr.error($scope.result.message);
			}
		}

		//校验题号
		$scope.blurtitle=function(){
			if($scope.testInfo.questionId){
				_selectQuestion();
			}
		}
		var _test=function(){
			if($scope.testInfo.trueAnswer){
				if($scope.testInfo.questionType == '-1') {				
				if($scope.testInfo.range == 'A-D'){
					var regu1 = /^[A-D]$/;
					var re = new RegExp(regu1);
					if(re.test($scope.testInfo.trueAnswer)) {
					} else {
						toastr.warning("请输入A-D单选字母");
						setTimeout(function() {
							$scope.testInfo.trueAnswer = ''
						}, 800)
						return false;
					}
				}else{
					var regu3 = /^[A-F]$/;
					var re = new RegExp(regu3);
					if(re.test($scope.testInfo.trueAnswer)) {} else {
						toastr.warning("请输入A-F单选字母");
						setTimeout(function() {
							$scope.testInfo.trueAnswer = ''
						}, 800)
						return false;
					}
				}
			}else{
				if($scope.testInfo.range == 'A-D'){
					var regu2 = /^[A-D]+$/;
					var re = new RegExp(regu2);
					if(re.test($scope.testInfo.trueAnswer)) {} else {
						toastr.warning("请输入A-D多选字母");
						setTimeout(function() {
							$scope.testInfo.trueAnswer = ''
						}, 800)
						return false;
					}
				}else{
					var regu4 = /^[A-F]+$/;
					var re = new RegExp(regu4);
					if(re.test($scope.testInfo.trueAnswer)) {} else {
						toastr.warning("请输入A-F多选字母");
						setTimeout(function() {
							$scope.testInfo.trueAnswer = ''
						}, 800)
						return false;
					}
				}
			}
			}
		}
		$scope.changeselType = function(selType) {
			$scope.testInfo.selType = selType;
			_test();

		}
		//检验对错
		$scope.changeJudge = function(trueAnswer) {
			$scope.testInfo.trueAnswer = trueAnswer;
			_test();
			
		}
		$scope.changeRange=function(range){
			$scope.testInfo.range=range;
			_test();
		}
		$scope.ok = function() {

			if(typeof $scope.testInfo.questionId == 'number') {
				$scope.testInfo.questionIds = parseInt($scope.testInfo.questionId);
			}
			if($scope.testInfo.questionType == '-1') {
				if($scope.testInfo.selType=='0'){
					$scope.testInfo.questionType = '0';
				}else{
					$scope.testInfo.questionType = '1';
				}
			}
			var param = {
				id: $scope.testInfo.id,
				testId: $scope.testInfo.testId,
				questionId: $scope.testInfo.questionIds,
				question: $scope.testInfo.question,
				questionType: $scope.testInfo.questionType,
				trueAnswer: $scope.testInfo.trueAnswer,
				range: $scope.testInfo.range
			}
			console.log("参数" + JSON.stringify(param))
			$scope.result = JSON.parse(execute_testPaper("update_question", JSON.stringify(param)));
			if($scope.result.ret == 'success') {
				toastr.success($scope.result.message);
				$modalInstance.close('success');
			} else {
				toastr.error($scope.result.message);
			}
		}
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		}
	})
	//确认弹出框
app.controller('sureModalCtrl', function($scope, $modalInstance, toastr, content) {
		$scope.content = '是否进行' + angular.copy(content) + '操作？';
		$scope.ok = function() {
			$modalInstance.close('success');
		}
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		}
	})
	//导入试卷控制器
app.controller('uploadfileModalCtrl', function($rootScope, $scope, $modalInstance, toastr, $modal) {
	
		$('#myModal').modal('hide');
		//隐藏loading
		var _hideModal=function(){
			$('#myModal').modal('hide');
		}
		//显示loading
		var _showModal=function(){
			$('#myModal').modal('show');
		}
		$scope.fileType = '0'; //0:本地导入;1:服务获取
		$scope.fileType1 = angular.copy($scope.fileType);
		$scope.sujectlists = []; //科目数组
		$scope.classList = []; //班级数组
		/*查询班级列表*/
		var _selectClass = function() {
			$scope.result = JSON.parse(execute_student("select_class"));
			if($scope.result.ret == 'success') {
				if($scope.result.item.length > 0) {
					angular.forEach($scope.result.item, function(i) {
						if(i.atype == '1') {
							var item = {
								key: i.classId,
								value: i.className
							}
							$scope.classList.push(item);
							//console.log("班级"+JSON.stringify($scope.classList))
							$scope.selclass = $scope.classList[0].key;
							$scope.selclass1 = angular.copy($scope.selclass);
						}
					})
				}

			} else {
				toastr.error($scope.result.message);
			}
		};

		$scope.sujectlists = JSON.parse(execute_testPaper("get_subject"));
		if($scope.sujectlists.length > 0) {
			$scope.selsubject = $scope.sujectlists[0];
			$scope.selsubject1 = angular.copy($scope.selsubject);
		}
		//切换文件类型
		$scope.changefileType = function(fileType) {
				$scope.fileType = fileType;
				if($scope.fileType == '1') {
					_selectClass();
				}
			}
			//切换班级
		$scope.changeClass = function(selclass) {
				$scope.selclass = selclass;
			}
			//切换科目
		$scope.changeSubject = function(selsubject) {
			$scope.selsubject = selsubject;
		}
		$scope.filepath = '';
		$scope.fileChanged = function() {
			if(document.querySelector('#uploadFile').value) {
				$scope.filepath = document.querySelector('#uploadFile').value;
			}
		}
		$scope.ok = function() {
			if($scope.fileType == '0') {
				if($scope.filepath) {
					var extStart = $scope.filepath.lastIndexOf(".");
					var ext = $scope.filepath.substring(extStart, $scope.filepath.length).toUpperCase();
					if(ext != ".XLS" && ext != ".XLSX") {
						toastr.warning("只能导入.XLSX、.XLS类型文件");
						return;
					} else {
						$('#myModal').modal('show');
						setTimeout(function() {
							//console.log("参数"+JSON.stringify($scope.filepath))
							$scope.result = JSON.parse(execute_testPaper("import_paper", $scope.filepath));
							console.log(JSON.stringify($scope.result))
							if($scope.result.ret == 'success') {
								$('#myModal').modal('hide');
								toastr.success($scope.result.message);
								window.location.href = "../../page/setmodule/testPaperManage.html";
								$modalInstance.close('success');

							} else {
								console.log($scope.result.detail);
								toastr.error($scope.result.message);
								$('#myModal').modal('hide');
							}
						}, 500)
					}
				} else {
					toastr.warning("请选择文件");
				}
			} else {
				$scope.result = JSON.parse(execute_testPaper("select_paper_server", $scope.selclass, $scope.selsubject));	
				var selitem = {
					classId: $scope.selclass,
					subject: $scope.selsubject
				}
				$modalInstance.close(selitem);
				_showModal();
				
				/*if($scope.result.ret == 'success') {
					console.log(JSON.stringify($scope.result))
					$modalInstance.close('success');
					var selitem = {
						classId: $scope.selclass,
						subject: $scope.selsubject
					}
					var modalInstance = $modal.open({
						templateUrl: 'selserverFile.html',
						controller: 'serverFileModalCtrl',
						size: 'md',
						backdrop: false,
						resolve: {
							infos: function() {
								return $scope.result.item;
							},
							subjectInfo: function() {
								return selitem;
							}

						}
					});

					modalInstance.result.then(function(info) {
						//查询所有试卷
						var param = {};
						var result = JSON.parse(execute_testPaper("select_paper", JSON.stringify(param)));
						console.log(JSON.stringify(result));
						if(result.ret == 'success') {
							$rootScope.paperInfoList = [];
							$rootScope.paperInfoList = result.item;
						} else {
							toastr.error(result.message);
						}
					}, function() {});
				} else {
					toastr.error($scope.result.message);
					console.log(JSON.stringify($scope.result.detail))
				}*/
			}

		}
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		}
		
		
	})
	//选择服务文件
app.controller('serverFileModalCtrl', function($scope, $modalInstance, toastr, infos, subjectInfo) {
	$scope.serverFileList = []; //服务文件数组
	if(infos) {
		angular.forEach(infos, function(i) {
			var item = {
				value: i.xm,
				key: i.id,
				xmid: i.xmid
			}
			$scope.serverFileList.push(item);
			$scope.selserverFile = $scope.serverFileList[0].key;
			$scope.selFileobject = $scope.serverFileList[0];
			$scope.selserverFile1 = angular.copy($scope.selserverFile)
		})
	}
	if(subjectInfo) {
		$scope.subjectInfo = angular.copy(subjectInfo);
	}
	//切换服务试卷
	$scope.changeserFile = function(selserverFile) {
		$scope.selserverFile = selserverFile;
		angular.forEach($scope.serverFileList, function(i) {
			if($scope.selserverFile == i.key) {
				$scope.selFileobject = i;
			}
		})

	}
	$scope.ok = function() {
		var param = {
			subjectName: $scope.subjectInfo.subject,
			id: $scope.selFileobject.key,
			xmid: $scope.selFileobject.xmid,
			xm: $scope.selFileobject.value,
			classId: $scope.subjectInfo.classId
		}
		console.log(JSON.stringify(param));
		var result = JSON.parse(execute_testPaper("import_paper_server", JSON.stringify(param)));
		console.log(JSON.stringify(result));
		$modalInstance.close('success');
		$('#myModal').modal('show');
		/*if(result.ret == 'success') {
			$scope.paperInfoList = result.item;
			$modalInstance.close('success');
		} else {
			toastr.error(result.message);
		}*/

	}
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
	
	
})

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
						$(element).html(str);
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
app.filter('questionType', function() {
	return function(questionType) {
		var statename = '';
		switch(questionType) {
			case '2':
				{
					statename = '判断';
					break;
				}
			case '0':
				{
					statename = '单选';
					break;
				}
			case '1':
				{
					statename = '多选';
					break;
				}
			case '3':
				{
					statename = '数字';
					break;
				}
			case '4':
			{
				statename = '主观题';
				break;
			}
		}
		return statename;
	}
});
app.filter('AnswerType', function() {
	return function(AnswerType) {
		var statename = '';
		switch(AnswerType) {
			case 'T':
				{
					statename = '对';
					break;
				}
			case 'F':
				{
					statename = '错';
					break;
				}
			default:
				{
					statename = AnswerType;
					break;
				}
		}
		return statename;
	}
});