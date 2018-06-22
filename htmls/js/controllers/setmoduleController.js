//定义模块时引入依赖  
var app = angular.module('app', ['ui.bootstrap', 'toastr']);
app.controller('setmoduleCtrl', function($scope, toastr) {
	$scope.infoAllNameList = []; //信道设置数组
	$scope.attendstatus = '1';
	$scope.sendpower = '3';
	$scope.sendpower1=angular.copy($scope.sendpower);
	/*获取所有组名*/
	var _getAllName = function() {
		/**
		 * 调用java方法
		 * 参数1,执行指令
		 * 参数2,执行参数,没有参数就不传
		 */
		$scope.result = JSON.parse(execute_set("get_all_name"));
		/*
		 * result
		 * 		ret 执行结果
		 * 		message 业务提示信息
		 * 		detail错误详细信息（调试用）
		 * 		item 返回信息
		 * */
			if($scope.result.ret == 'success') {
				$scope.infoAllNameList = $scope.result.item;
				console.log(JSON.stringify($scope.infoAllNameList))
				if($scope.infoAllNameList.length > 0) {
					$scope.chain = $scope.infoAllNameList[0];
					$scope.chain1=angular.copy($scope.chain);
				}
			} else {
				toastr.error($scope.result.message);
			}

	};
	$scope.changeChain = function(chain) {
		$scope.chain = chain;
		console.log(JSON.stringify($scope.chain))
	}
	$scope.changeSendPower = function(sendpower) {
			$scope.sendpower = sendpower;
			console.log(JSON.stringify($scope.sendpower))
		}
		/*读取设置*/
	$scope.readSet = function() {
		$scope.result = JSON.parse(execute_set("read_setting"));
		if($scope.result.ret == 'success') {
			toastr.success($scope.result.message);
		} else {
			toastr.error($scope.result.message);
		}
	};

	/*设置*/
	$scope.set = function() {
		var param = {
			name: $scope.chain,
			power: $scope.sendpower
		}
		console.log(JSON.stringify(param))
		$scope.result = JSON.parse(execute_set("set", JSON.stringify(param)));
		if($scope.result.ret == 'success') {
			toastr.success($scope.result.message);
		} else {
			toastr.error($scope.result.message);
		}
	};

	/*设置默认值*/
	$scope.setDefault = function() {
		$scope.result = JSON.parse(execute_set("set_default"));
		if($scope.result.ret == 'success') {
			toastr.success($scope.result.message);
		} else {
			toastr.error($scope.result.message);
		}
	};
	var _init = function() {
		_getAllName();
	}();
	//跳转到学生管理页面
	$scope.stuManage = function() {
			window.location.href = "../../page/setmodule/setStuManage.html?backurl=" + window.location.href;
		}
		//跳转到试卷管理页面
	$scope.paperManage = function() {
		window.location.href = "../../page/setmodule/testPaperManage.html?backurl=" + window.location.href;
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