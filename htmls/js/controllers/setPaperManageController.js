//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('setPaperManageCtrl', function($rootScope,$scope,$modal,toastr) {
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/setmodule/setmodule.html"; 
	}
	//跳转到添加试卷页面
	$scope.addTestPaper=function(){				
		window.location.href="../../page/setmodule/addtestPage.html";
	}
	
	//查询所有试卷
	var _selectPaper = function(){
		var param = {};
		var result = JSON.parse(execute_testPaper("select_paper",JSON.stringify(param)));
		console.log(JSON.stringify(result));
		if(result.ret == 'success'){
			$scope.paperInfoList = result.item;
		}else{
			toastr.error(result.message);
		}
		
	};
	_selectPaper();
	
	//编辑试卷
	$scope.editPaper=function(item){
		$scope.param = "atype=" + item.atype + "&describe=" + item.describe + "&id=" + item.id+"&subject=" +item.subject+ "&testId="+item.testId +"&testName=" +item.testName;			
		$scope.objectUrl = '../../page/setmodule/edittestPage.html' + '?' + $scope.param;
		console.log(JSON.stringify(item))
		$window.location.href = $scope.objectUrl;			
		
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
app.controller('addPaperManageCtrl',function($rootScope,$scope,$modal,toastr){
	$scope.paperInfo={
		testName:'',//试卷名称
		describe:''//试卷描述
	}
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
	//添加题目
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
			
			$rootScope.sureSave=function(){
				if(info){
					console.log(JSON.stringify(info))
				}
				
				var param={
					testInfo:$scope.paperInfo,
					questionInfos:info
					
				}
				console.log(JSON.stringify(param))
				$scope.result= JSON.parse(execute_testPaper("insert_paper",JSON.stringify(param)));
				if($scope.result.ret=='success'){
					toastr.success($scope.result.message);
				}else{
					toastr.error($scope.result.message);
				}
			}
			
			
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
	//保存试卷
	$scope.savePaper=function(){
		$rootScope.sureSave();
	}
	
	
})

app.config(['$locationProvider', function($locationProvider) {  
	    //$locationProvider.html5Mode(true);  
 	$locationProvider.html5Mode({
	enabled: true,
	requireBase: false
	});
}]);
//编辑试卷控制器
app.controller('editPaperManageCtrl',function($rootScope,$scope,$modal,$location,$window,toastr){
	console.log(JSON.stringify($location.search()))
	if($location.search()){
		$scope.paperInfo=$location.search();
	}
	$scope.questionInfoList=[];//题目数组
	//查询该试卷的题目
	var _selectQuestion=function(){		
		var param = {
			testId:$scope.paperInfo.testId
		}
		$scope.result = JSON.parse(execute_testPaper("select_question",JSON.stringify(param)));
		console.log(JSON.stringify($scope.result));
		if($scope.result.ret == 'success'){
			$scope.questionInfoList = $scope.result.item;
		}else{
			toastr.error($scope.result.message);
		}
	}
      //item":[{"atype":"0","describe":"单选、多选、判断、数字","id":1,"remark":"","subject":"语文","testId":"CS1001","testName":"120题测试"}]
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
			_selectQuestion();
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
			_selectQuestion();
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
				_selectQuestion();
			}else{
				toastr.error($scope.result.message);
			}*/
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
})
//新增题目控制器
app.controller('addSubjectModalCtrl',function($rootScope,$modalInstance,$scope,$modal,toastr){
	$scope.title="新增试卷";
	$scope.testInfo={
		questionType:'3',
		selType:'1',
		range:"A-D",
		trueAnswer:''
		
	}
	$scope.testInfo.questionType1=angular.copy($scope.testInfo.questionType);
	$scope.testInfo.selType1=angular.copy($scope.testInfo.selType);
	$scope.testInfo.range1=angular.copy($scope.testInfo.range);
	//切换答案类型
	$scope.changequesType=function(quesType){
		$scope.testInfo.questionType=quesType;
	}
	//检验对错
	$scope.blurJudge=function(trueAnswer){
		$scope.testInfo.trueAnswer=trueAnswer;
		switch($scope.testInfo.questionType) {
			case '3':
				{
					if($scope.testInfo.trueAnswer!='T'||$scope.testInfo.trueAnswer!='F'){
						toastr.warning("请输入大写的T或F");
						setTimeout(function(){
							$scope.testInfo.trueAnswer='';
						},1000);
					}										
					break;
				}
			case '0':
				{	
					if($scope.testInfo.selType=='1'){
							console.log(JSON.stringify($scope.testInfo.trueAnswer))
						if($scope.testInfo.trueAnswer.length>2){
							
						}
					}
					$scope.lettercon="A-D";
					$scope.lettercon="A-F";
					break;
				}
			case '4':
				{
					
					break;
				}
		}
		
	}
	
	$scope.blurtrueAnswer=function(trueAnswer){
		$scope.testInfo.trueAnswer=trueAnswer;
		switch($scope.testInfo.questionType) {
			case '3':
				{
					if($scope.testInfo.trueAnswer!='T'||$scope.testInfo.trueAnswer!='F'){
						toastr.warning("请输入大写的T或F");
						setTimeout(function(){
							$scope.testInfo.trueAnswer='';
						},1000);
					}										
					break;
				}
			case '0':
				{	
					if($scope.testInfo.selType=='1'){
							if($scope.testInfo.range){
								
							}
						if($scope.testInfo.trueAnswer.length>2){
							console.log(JSON.stringify($scope.testInfo.trueAnswer))
						}else{
							console.log(JSON.stringify($scope.testInfo.trueAnswer))
						}
					}
					$scope.lettercon="A-D";
					$scope.lettercon="A-F";
					break;
				}
			case '4':
				{
					
					break;
				}
		}
	}
	$scope.ok = function() {
		/*var param={
			/*question:$scope.testInfo.question,
			questionType:$scope.testInfo.questionType,
			trueAnswer:$scope.testInfo.trueAnswer,
			range:$scope.testInfo.range*/
		
		/*testInfo:{},
		
		
		}*/
		var param=[{
			question:$scope.testInfo.question,
			questionType:$scope.testInfo.questionType,
			trueAnswer:$scope.testInfo.trueAnswer,
			range:$scope.testInfo.range
		}]		
		$modalInstance.close(param);	
	}
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
})
//编辑题目控制器
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
						 window.location.href="../../page/setmodule/testPaperManage.html"; 
						
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
				 window.location.href="../../page/setmodule/testPaperManage.html"; 
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