//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('setPaperManageCtrl', function($rootScope,$scope,$modal) {
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/setmodule/setmodule.html"; 
	}
	//跳转到添加试卷页面
	$scope.addTestPaper=function(){				
		window.location.href="../../page/setmodule/addtestPage.html";
	}
	
	$scope.PaperInfo=[{id:1,name:"哈哈哈哈"}]
	
	//编辑试卷
	$scope.editPaper=function(item){
		$rootScope.editPaperInfo=item;
		
		//window.location.href="../../page/setmodule/edittestPage.html";
	}
	
	//删除题目 
	$scope.delPaper=function(item){
		var content="删除题目";
		var modalInstance = $modal.open({
			templateUrl: 'sureModal.html',
			controller: 'sureModalCtrl',
			size: 'sm',
			backdrop:false,
			resolve: {
				content: function() {
					return content;
				}
			}
		});
		modalInstance.result.then(function(info) {
			/*var param={
				id:item.id
			}
			console.log(JSON.stringify(param))
			param=JSON.stringify(param)			
			$scope.result=JSON.parse(execute_student("delete_class",param));
			if($scope.result.ret=='success'){
				toastr.success($scope.result.message);
				$scope.isActive = 0;
				_selectClass();
			}else{
				toastr.error($scope.result.message);
			}*/
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
})
//添加试卷控制器
app.controller('addPaperManageCtrl',function($rootScope,$scope,$modal){
	//返回返回试卷管理页面
	$scope.returnPage=function(){
		 window.location.href="../../page/setmodule/testPaperManage.html"; 
	}
	$scope.importSubject=function(){
		var modalInstance = $modal.open({
		templateUrl: 'importFile.html',
		controller: 'uploadfileModalCtrl',
		size: 'md',
		backdrop:false
	});

	modalInstance.result.then(function(info) {
	}, function() {
	});
	}
	
})
//编辑试卷控制器
app.controller('editPaperManageCtrl',function($rootScope,$scope,$modal){
	
	//返回返回试卷管理页面
	$scope.returnPage=function(){
		 window.location.href="../../page/setmodule/testPaperManage.html"; 
	}
	$scope.addSubject=function(){
		
		var modalInstance = $modal.open({
			templateUrl: 'addSubjectModal.html',
			controller: 'addSubjectModalCtrl',
			size: 'md',
			backdrop:false,
			/*resolve: {
				infos: function() {
					return item;
				}
			}*/
		});
		modalInstance.result.then(function(info) {

		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
	//编辑题目
	$scope.editSuject=function(){
		var modalInstance = $modal.open({
			templateUrl: 'addSubjectModal.html',
			controller: 'editSubjectModalCtrl',
			size: 'md',
			backdrop:false,
			/*resolve: {
				infos: function() {
					return item;
				}
			}*/
		});
		modalInstance.result.then(function(info) {
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
	//删除题目 
	$scope.delSuject=function(item){
		var content="删除题目";
		var modalInstance = $modal.open({
			templateUrl: 'sureModal.html',
			controller: 'sureModalCtrl',
			size: 'sm',
			backdrop:false,
			resolve: {
				content: function() {
					return content;
				}
			}
		});
		modalInstance.result.then(function(info) {
			/*var param={
				id:item.id
			}
			console.log(JSON.stringify(param))
			param=JSON.stringify(param)			
			$scope.result=JSON.parse(execute_student("delete_class",param));
			if($scope.result.ret=='success'){
				toastr.success($scope.result.message);
				$scope.isActive = 0;
				_selectClass();
			}else{
				toastr.error($scope.result.message);
			}*/
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
})
//新增试卷控制器
app.controller('addSubjectModalCtrl',function($rootScope,$modalInstance,$scope,$modal){
	$scope.title="新增试卷";
	$scope.ok = function() {
		/*var param = $scope.classInfo;
		$scope.result= JSON.parse(execute_student("insert_class",JSON.stringify(param)));
		console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
			$modalInstance.close('success');
		}else{
			toastr.error($scope.result.message);
		}	*/	
	}
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
})
//编辑试卷控制器
app.controller('editSubjectModalCtrl',function($rootScope,$modalInstance,$scope,$modal){
	$scope.title="编辑试卷";
	$scope.ok = function() {
		/*var param = $scope.classInfo;
		$scope.result= JSON.parse(execute_student("insert_class",JSON.stringify(param)));
		console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
			$modalInstance.close('success');
		}else{
			toastr.error($scope.result.message);
		}	*/	
	}
	$scope.cancel = function() {
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
//导入试卷控制器
app.controller('uploadfileModalCtrl', function($scope,$modalInstance,toastr) {
	$scope.fileType='0';//0:本地导入;1:服务获取
	$scope.fileType1=angular.copy($scope.fileType);
	//切换文件类型
	$scope.changefileType=function(fileType){
		$scope.fileType=fileType;
	}
	$scope.filepath='';	
	$scope.fileChanged=function(){
		if(document.querySelector('#uploadFile').value){
			$scope.filepath= document.querySelector('#uploadFile').value;
			
		}
	}
	$scope.ok = function() {
		if($scope.fileType=='0'){
			if($scope.filepath){
				var extStart = $scope.filepath.lastIndexOf(".");
				var ext = $scope.filepath.substring(extStart, $scope.filepath.length).toUpperCase();
				if(ext != ".XLS" && ext != ".XLSX") {
					toastr.warning("只能导入.XLSX、.XLS类型文件");
					return ;
				}else{
					console.log("参数"+JSON.stringify($scope.filepath))
					$scope.result=JSON.parse(execute_testPaper("import_paper",$scope.filepath));
					if($scope.result.ret=='success'){
						toastr.success($scope.result.message);
						$modalInstance.close('success');
					}else{
						console.log($scope.result.detail);
						toastr.error($scope.result.message);
					}
					
					//$('#myModal').modal('show');
				}
			}else{
				toastr.warning("请选择文件");
			}
		}else{
			$scope.result=JSON.parse(execute_testPaper("import_server",$scope.filepath));
			if($scope.result.ret=='success'){
				toastr.success($scope.result.message);
				$modalInstance.close('success');
			}else{
				toastr.error($scope.result.message);
			}
		}
		
		
	}
	$scope.cancel=function(){
		$modalInstance.dismiss('cancel');
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