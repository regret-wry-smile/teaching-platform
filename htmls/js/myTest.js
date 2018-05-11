angular.module('someApp', []).controller('someController', function($scope, $interval) {
	/*获取所有组名*/
	$scope.getAllName = function(val) {
		/**
		 * 调用java方法
		 * 参数1,执行指令
		 * 参数2,执行参数,没有参数就不传
		 */
		var result = JSON.parse(execute_set("get_all_name"));
		/*
		 * result
		 * 		ret 执行结果
		 * 		message 业务提示信息
		 * 		detail错误详细信息（调试用）
		 * 		item 返回信息
		 * */
		alert(JSON.stringify(result));
	};
	/*登录*/
	$scope.login = function(val) {
		var param = {
				loginId:'luozheng',
				password:123
		}
		param =JSON.stringify(param);
		var result = JSON.parse(execute_set("login",param));
		alert(JSON.stringify(result));
	};
	
	/*读取设置*/
	$scope.read_setting = function(val) {
		var result = JSON.parse(execute_set("read_setting"));
		alert(JSON.stringify(result));
	};
	
	/*设置*/
	$scope.set = function(val) {
		var param = {
				name:'第三组',
				power:2
		}
		param =JSON.stringify(param);
		var result = JSON.parse(execute_set("set",param));
		alert(JSON.stringify(result));
	};
	
	/*设置默认值*/
	$scope.set_default = function(val) {
		var result = JSON.parse(execute_set("set_default"));
		alert(JSON.stringify(result));
	};
	
});