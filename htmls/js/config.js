angular.module('someApp', []).controller('configController', function($scope, $interval) {
	//定义需要传的参数
	$scope.addconfig={
		num1:'', //IP地址
		num2:'',
		num3:'',
		num4:'',
		port:'',//端口号
		timeOut:'5',//连接超时
		time:'5' ,//连接次数
		className:'',//班级名称
		configType:0,//默认单机
		configUser:0//默认学生
	}
	//切换联机类型
	$scope.changetabs=function(val){
		if (val != $scope.addconfig.configType) {
			$scope.addconfig.configType = val;
		}
	}
	//切换用户类型
	$scope.changeUser=function(val){
		if (val != $scope.addconfig.configUser) {
			$scope.addconfig.configUser = val;
		}
		$scope.addconfig={
		num1:'', //IP地址
		num2:'',
		num3:'',
		num4:'',
		port:'',//端口号
		timeOut:'5',//连接超时
		time:'5' ,//连接次数
		className:'',//班级名称
		configType:$scope.addconfig.configType,
		configUser:$scope.addconfig.configUser
		}
	}
	//保存
	$scope.save = function(){ 
		update_config_file(JSON.stringify($scope.addconfig));
	}; 
	//取消
	$scope.cancel=function(){
		$scope.addconfig={
			num1:'', //IP地址
			num2:'',
			num3:'',
			num4:'',
			port:'',//端口号
			timeOut:'5',//连接超时
			time:'5' ,//连接次数
			className:'',//班级名称
			configType:$scope.addconfig.configType,
			configUser:$scope.addconfig.configUser
		}
	}
});