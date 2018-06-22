//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
	app.controller('setmoduleCtrl', function($scope,toastr) {
	$scope.infoAllNameList=[];//信道设置数组
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
		$scope.infoAllNameList=$scope.result.item;
	};
	/*读取设置*/
	$scope.readSet = function() {
		$scope.result = JSON.parse(execute_set("read_setting"));
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
		}		
	};
	
	/*设置*/
	$scope.set = function() {
		var param = {
			name:$scope.chain,
			power:$scope.sendpower
		}
		param =JSON.stringify(param);
		$scope.result = JSON.parse(execute_set("set",param));
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
		}	
	};
	
	/*设置默认值*/
	$scope.setDefault = function() {
		$scope.result = JSON.parse(execute_set("set_default"));
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
		}
	};
	var _init=function(){		
		_getAllName();
	}();
	//跳转到学生管理页面
	$scope.stuManage = function() {
	  	window.location.href="../../page/setmodule/setStuManage.html?backurl="+window.location.href; 
	}
	//跳转到试卷管理页面
	$scope.paperManage = function() {
	  	window.location.href="../../page/setmodule/testPaperManage.html?backurl="+window.location.href; 
	}
		
	})
