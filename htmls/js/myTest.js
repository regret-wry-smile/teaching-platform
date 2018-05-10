angular.module('someApp', []).controller('someController', function($scope, $interval) {
	/*执行方法*/
	$scope.startAnswer = function(val) {
		/**
		 * 调用java方法
		 * 参数1,执行指令
		 * 参数2,执行参数
		 */
		var result = JSON.parse(execute_student("selectStudentInfo"));
		/*
		 * result
		 * 		ret 执行结果
		 * 		message 业务提示信息
		 * 		detail错误详细信息（调试用）
		 * 		item 返回信息
		 * */
		$scope.message = JSON.stringify(result);
		
	};
//	/*页面跳转*/
//	/**
//	 * 调用java方法
//	 * 参数1,执行指令
//	 */
//	$scope.endAnswer = function(val) {
//		page_redirect("to_myTest");
//	}
	
});