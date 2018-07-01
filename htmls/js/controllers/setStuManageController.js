//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
	app.controller('setStuManageCtrl', function($rootScope,$scope,$modal,toastr) {
	$scope.studentList=[];//学生列表数组
	$scope.checkedId=[];
	$scope.onechecked = [];
	$scope.classList=[];//班级列表数组
/*	$scope.classList=[{"atype":"1","classId":"536666","className":"64326","id":28},{"atype":"0","classId":"77754","className":"754745","id":60}];*/
	$scope.isActive = 0;
	$scope.classobject='';
	$('#myModal').modal('hide');//默认隐藏loading
	//隐藏loading
	var _hideModal=function(){
		$('#myModal').modal('hide');
	}
	//显示loading
	var _showModal=function(){
		$('#myModal').modal('show');
	}
	
	/*查询学生列表*/
	var _selectStudent = function() {
		var param = {
			classId:$scope.classId
		}
		console.log(JSON.stringify(param))
		$scope.result = JSON.parse(execute_student("select_student",JSON.stringify(param)));
		console.log("學生"+JSON.stringify($scope.result))
		if($scope.result.ret=='success'){
			$scope.studentList=[];
			$scope.studentList=$scope.result.item;
			
		}else{
			toastr.error($scope.result.message);
		}
		
	};
	/*查询班级列表*/
	var _selectClass = function() {
		$scope.result = JSON.parse(execute_student("select_class"));
		if($scope.result.ret=='success'){
			$scope.classList=$scope.result.item;
			console.log("班级"+JSON.stringify($scope.classList))
			if($scope.classList.length>0){
				$scope.classId=$scope.classList[0].classId;
				$scope.classobject=$scope.classList[0];
				$scope.isActive = 0; 
				$scope.studentList=[];
				
			}else{
				$scope.studentList=[];
			}
		}else{
			toastr.error($scope.result.message);
		}
	};

	var _init=function(){
		_selectClass();
		_selectStudent();
	}();		
	//打开添加班级弹框
	$scope.addClass = function() {
		var modalInstance = $modal.open({
			templateUrl: 'addClassModal.html',
			controller: 'addClassModalCtrl',
			size: 'md',
			backdrop:false,
			/*resolve: {
				infos: function() {
					return $scope.classes.length;
				}
			}*/
		});
		modalInstance.result.then(function(info) {
			console.log("班级id"+JSON.stringify(info))
			if(info){
				_selectClass();
				for(var i=0;i<$scope.classList.length;i++){
					if(info==$scope.classList[i].classId){
						$scope.classId=info;
						$scope.classobject=$scope.classList[i];
						$scope.isActive =$scope.classList.length-1;
						_selectStudent();
						
					}
				}
			}		
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	};
	/*切换班级*/
	$scope.changeClass=function(item,$index){			
		$scope.isActive = $index; 
		$scope.classId=item.classId;
		$scope.classobject=item;
		console.log("时告诉刚刚说"+JSON.stringify($scope.classobject))
		_selectStudent();
	}
	
	//本地导入学生
	$scope.patchImport = function() {
			var modalInstance = $modal.open({
				templateUrl: 'importFile.html',
				controller: 'uploadfileModalCtrl',
				size: 'md',
				backdrop:false,
				resolve: {
					infos: function() {
						return $scope.classobject;
					}
				}
			});
		
			modalInstance.result.then(function(info) {
				if(info){
					alert(222)
					_selectClass();
					for(var i=0;i<$scope.classList.length;i++){
						if(info==$scope.classList[i].classId){
							alert(555)
						$scope.classId=info;
						$scope.classobject=$scope.classList[i];
						$scope.isActive =$scope.classList.length-1;
						$scope.changeClass($scope.classobject,$scope.isActive)
					}
					}
				}
			}, function() {
		});
		}
	//打开编辑班级弹框
	$scope.editClass = function(item,$index) {
		var modalInstance = $modal.open({
			templateUrl: 'addClassModal.html',
			controller: 'editClassModalCtrl',
			size: 'md',
			backdrop:false,
			resolve: {
				infos: function() {
					return item;
				}
			}
		});
		modalInstance.result.then(function(info) {
			_selectClass();
			$scope.isActive = $index; 
			$scope.classId=item.classId;
			_selectStudent();
			
			
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	};
	
	// 删除班级	 
	$scope.deleteClass=function(item){
		var content="删除班级";
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
			//如果要删除的班级就是当前选中的班级，就默认选择第一个
			var param={
				id:item.id,
				classId:item.classId
			}
			console.log(JSON.stringify(param))	
			$scope.result=JSON.parse(execute_student("delete_class",JSON.stringify(param)));
			if($scope.result.ret=='success'){
				toastr.success($scope.result.message);
				_selectClass();	
				_selectStudent();	
			}else{
				toastr.error($scope.result.message);
			}
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
	//打开新增学生弹框
	$scope.addStudent = function() {
		var modalInstance = $modal.open({
			templateUrl: 'addStudentModal.html',
			controller: 'addStudentModalCtrl',
			size: 'md',
			backdrop:false,
			resolve: {
				infos: function() {
					return $scope.classobject;
				}
			}
		});

		modalInstance.result.then(function(info) {
			_selectStudent();
		}, function() {

			//$log.info('Modal dismissed at: ' + new Date());
		});
	};
	//编辑学生
	$scope.updateStdent=function(item){
		var modalInstance = $modal.open({
			templateUrl: 'addStudentModal.html',
			controller: 'editStudentModalCtrl',
			size: 'md',
			backdrop:false,
			resolve: {
				infos: function() {
					return item;
				}
			}
		});

		modalInstance.result.then(function(info) {
			_selectStudent();
		}, function() {

			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
	
	//一键配对
	$scope.quickBind=function(){		
		if($scope.classobject.classId){//调用配对指令
			var param = {
				classId: $scope.classobject.classId
			}
			$scope.result = JSON.parse(execute_student("bind_start", JSON.stringify(param)));
			//		console.log(JSON.stringify(result));
			if($scope.result.ret == "success") {
				var content = "一键配对";
				var modalInstance = $modal.open({
					templateUrl: 'findBindModal.html',
					controller: 'findBindModalCtrl',
					size: 'md',
					backdrop: false,
					/*resolve: {
						info: function() {
							return $rootScope.className;
						}
					}*/
				});
				modalInstance.result.then(function(info) {
					//_SELECTSTUDENT($rootScope.className);
					_selectStudent();
				}, function() {
					//$log.info('Modal dismissed at: ' + new Date());
				});
			} else {
				toastr.error($scope.result.message);
			}
		}else{
			toastr.warning("当前没有班级，请先添加班级");
		}
		
		
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
	}
	
	//删除学生
	$scope.deleteStudent=function(){
		if($scope.checkedId.length>0){
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
				var param=$scope.checkedId;
				console.log(JSON.stringify(param))
				$scope.result = JSON.parse(execute_student("delete_student",param));
				if($scope.result.ret=='success'){					
					toastr.success($scope.result.message);
					_selectStudent();
					$scope.onechecked = [];
					$scope.checkedId = [];
					$scope.selected=false;
				}else{
					toastr.error($scope.result.message);
				}
				
			}, function() {

				//$log.info('Modal dismissed at: ' + new Date());
			});
		}else{
			toastr.warning("请选择学生");
		}
		
	}
	//解绑
	$scope.unbindStu=function(){
		if($scope.checkedId.length>0){
			var content="解绑";
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
				var param=$scope.checkedId;
				console.log(JSON.stringify(param))
				$scope.result = JSON.parse(execute_student("clear_student",JSON.stringify(param)));
				if($scope.result.ret=='success'){
					toastr.success($scope.result.message);
					_selectStudent();
					$scope.onechecked = [];
					$scope.checkedId = [];
					$scope.selected=false;
				}else{
					toastr.error($scope.result.message);
					alert($scope.result.detail);
				}
			}, function() {
			});
		}else{
			toastr.warning("请选择学生");
		}
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
			var param={
				classId:$scope.classId
			}
			$scope.result = JSON.parse(execute_student("clear_bind",JSON.stringify(param)));
			if($scope.result.ret=='success'){
				_selectStudent();
				toastr.success($scope.result.message);
			}else{
				toastr.error($scope.result.message);
			}
		}, function() {
			//$log.info('Modal dismissed at: ' + new Date());
		});
	}
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/setmodule/setmodule.html"; 
	}
	//刷新学生
	$scope.refreshStudent = function(){
		var param = {
			classId:classId
		}
		console.log(JSON.stringify(param))
		$scope.result = JSON.parse(execute_student("select_student",JSON.stringify(param)));
		console.log("學生"+JSON.stringify($scope.result))
		if($scope.result.ret=='success'){
			$scope.studentList=[];
			$scope.studentList=$scope.result.item;
			
		}else{
			toastr.error($scope.result.message);
		}
		
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
//导入学生控制器	
app.controller('uploadfileModalCtrl', function($scope,$modalInstance,toastr,infos) {	
	$scope.isfileType=false;
	$('#myModal').modal('hide');
	//隐藏loading
	var _hideModal=function(){
		$('#myModal').modal('hide');
	}
	//显示loading
	var _showModal=function(){
		$('#myModal').modal('show');
	}
	//console.log("吃哈哈哈"+JSON.stringify(infos));
	if(infos){
		$scope.fileType=angular.copy(infos.atype);
		$scope.fileType1=angular.copy($scope.fileType);
		if($scope.fileType=='0'){			
			$scope.isfileType=false;
		}else{
			$scope.isfileType=true;
		}
		
	}else{
		$scope.fileType='0';//0:本地导入;1:服务导入
		$scope.fileType1=angular.copy($scope.fileType);
		$scope.isfileType=false;
	}
	/*$scope.fileType='0';//0:本地导入;1:服务导入
	$scope.fileType1=angular.copy($scope.fileType);
	//切换文件类型
	$scope.changefileType=function(fileType){
		$scope.fileType=fileType;
	}*/
	$scope.filepath='';	
	$scope.fileChanged=function(){
		if(document.querySelector('#uploadFile').value){
			$scope.filepath= document.querySelector('#uploadFile').value;			
		}
	}
	$scope.ok = function() {
		//$('#myModal').modal('hide');
		if($scope.fileType=='0'){
			if($scope.filepath){
				var extStart = $scope.filepath.lastIndexOf(".");
				var ext = $scope.filepath.substring(extStart, $scope.filepath.length).toUpperCase();
				if(ext != ".XLS" && ext != ".XLSX") {
					toastr.warning("只能导入.XLSX、.XLS类型文件");
					return ;
				}else{	
					_showModal();
					$scope.result=JSON.parse(execute_student("import_student",$scope.filepath));
					//console.log("哈哈哈哈"+JSON.stringify($scope.result))
					if($scope.result.ret=='success'){
						toastr.success($scope.result.message);
						_hideModal();
						$modalInstance.close($scope.result.remak);
					}else{
						toastr.error($scope.result.message);
					}
					
					//$('#myModal').modal('show');
				}
			}else{
				toastr.warning("请选择文件");
			}
		}else{
			_showModal();
			$scope.result=JSON.parse(execute_student("import_server",infos.classId));
			$modalInstance.close();
			console.log(JSON.stringify($scope.result))
			/*if($scope.result.ret=='success'){
				toastr.success($scope.result.message);				
				$modalInstance.close($scope.result.remark);
				_hideModal();
			}else{
				toastr.error($scope.result.message);
				_hideModal();
			}*/
		}
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
app.controller('findBindModalCtrl',function($scope,$modalInstance,toastr){
	var _getBindInfo = function(){
		var result = JSON.parse(execute_student("get_bind_info"));
		$scope.bindInfo = result;
	}
	
	$scope.refreshBindCard = function(){
		_getBindInfo();
	}
	_getBindInfo();
	$scope.ok = function(){
		var result = JSON.parse(execute_student("bind_stop"));
		if(result.ret == 'success'){
			toastr.success("指令发送成功！");
		}else{
			toastr.error("指令发送失败！");
		}
		/*关闭定时器*/
		$modalInstance.close();
	}
})
//添加学生控制器
app.controller('addStudentModalCtrl',function($scope,$modalInstance,toastr,infos){
	console.log(JSON.stringify(infos))
	$scope.title="添加学生";
	if(infos){
		$scope.classId=infos.classId;
		$scope.className=infos.className;
	}
	
	$scope.student={		
//		studentId:'',
		studentName:'',
//		iclickerId:'',
		classId:$scope.classId,
		className:$scope.className
	}
	
	/*查询学生列表*/
	var _selectStudent = function() {
		if($scope.student.studentId){
			var param = {
				classId:$scope.classId,
				studentId:$scope.student.studentId
			}
		}
		if($scope.student.iclickerId){
			var param = {
				classId:$scope.classId,
				iclickerId:$scope.student.iclickerId
			}
		}
		
		console.log(JSON.stringify(param))
		param =JSON.stringify(param);
		$scope.result = JSON.parse(execute_student("select_student",param));
		console.log("學生"+JSON.stringify($scope.result))
		if($scope.result.ret=='success'){
			if($scope.result.item.length>0){
				if($scope.student.studentId){
					toastr.warning('该学号已存在，请重新输入');
					 $timeout(function () {
					  $scope.student.studentId='';
				   	}, 2000);
				}
				if($scope.student.iclickerId){
					toastr.warning('该答题器编号已存在，请重新输入');
					 $timeout(function () {
					  $scope.student.iclickerId='';
				   	}, 2000);
				}
			}
			
		}else{
			
			toastr.error($scope.result.message);
		}
		
	};
	//校验学号
	$scope.blurStunum=function(){
		_selectStudent();
	}
	$scope.blurDevicenum=function(){
		_selectStudent();
	}
	$scope.ok = function() {
		if(typeof $scope.student.studentId=='number'){
			$scope.student.studentIdint=JSON.stringify($scope.student.studentId)
		}
		if(typeof $scope.student.iclickerId=='number'){
			$scope.student.iclickerIdint=JSON.stringify($scope.student.iclickerId)
		}
		var param = {
			studentName:$scope.student.studentName,
			classId:$scope.student.classId,
			className:$scope.student.className,
			studentId:$scope.student.studentIdint,
			iclickerId:$scope.student.iclickerIdint,
			status:'0'//班級类型
		}
		console.log(JSON.stringify(param))
		$scope.result=JSON.parse(execute_student("insert_student",JSON.stringify(param)));
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
			$modalInstance.close('success');
		}else{
			console.log(JSON.stringify($scope.result.detail))
			toastr.error($scope.result.message);
		}
	}
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
})
	//编辑学生控制器
app.controller('editStudentModalCtrl',function($scope,$modalInstance,toastr,infos){
	$scope.title="编辑学生";
	if(infos){
		$scope.student=angular.copy(infos);
		console.log(JSON.stringify(infos))
		if(typeof infos.studentId=='string'){
			$scope.student.studentId=parseInt(infos.studentId)
		}
		if(typeof infos.iclickerId=='string'){
			$scope.student.iclickerId=parseInt(infos.iclickerId)
		}
	}		
		/*查询学生列表*/
	var _selectStudent = function() {
		if($scope.student.studentId){
			var param = {
				classId:$scope.student.classId,
				studentId:$scope.student.studentId
			}
		}
		if($scope.student.iclickerId){
			var param = {
				classId:$scope.classId,
				iclickerId:$scope.student.iclickerId
			}
		}
		
		console.log(JSON.stringify(param))
		param =JSON.stringify(param);
		$scope.result = JSON.parse(execute_student("select_student",param));
		console.log("學生"+JSON.stringify($scope.result))
		if($scope.result.ret=='success'){
			if($scope.result.item.length>0){
				if($scope.student.studentId){
					toastr.warning('该学号已存在，请重新输入');
					 $timeout(function () {
					  $scope.student.studentId='';
				   	}, 2000);
				}
				if($scope.student.iclickerId){
					toastr.warning('该答题器编号已存在，请重新输入');
					 $timeout(function () {
					  $scope.student.iclickerId='';
				   	}, 2000);
				}
			}
			
		}else{
			
			toastr.error($scope.result.message);
		}
		
	};
	//校验学号
	$scope.blurStunum=function(dirty){
		if(dirty&&dirty==true){
			_selectStudent();
		}
	}
	$scope.blurDevicenum=function(dirty){
		if(dirty&&dirty==true){
			_selectStudent();
		}
		
	}
	$scope.ok = function() {
		if(typeof $scope.student.studentId=='number'){
			$scope.student.studentIdint=JSON.stringify($scope.student.studentId)
		}
		if(typeof $scope.student.iclickerId=='number'){
			$scope.student.iclickerIdint=JSON.stringify($scope.student.iclickerId)
		}
		var param = {
			id:$scope.student.id,
			studentName:$scope.student.studentName,
			classId:$scope.student.classId,
			className:$scope.student.className,
			studentId:$scope.student.studentIdint,
			iclickerId:$scope.student.iclickerIdint,
			status:$scope.student.status//班級类型
		}
		console.log(JSON.stringify(param))
		$scope.result=JSON.parse(execute_student("update_student",JSON.stringify(param)));
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
			$modalInstance.close('success');
		}else{
			toastr.error($scope.result.message);
		}
	}
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
})

//添加班级
app.controller('addClassModalCtrl',function($scope,$modalInstance,$rootScope,toastr,$timeout){
	$scope.classInfo = {
		atype:"0",//班级类型,默认本地
		className:"",//班级名称
		classId:"" //班级id
	}
	$scope.classInfo.atype1=angular.copy($scope.classInfo.atype);
	$scope.title="添加班级";
	//班级id查重
	$scope.selectClass=function(){
		if($scope.classInfo.classId){
			var param={
				classId:$scope.classInfo.classId
			}		
			$scope.result= JSON.parse(execute_student("select_class",JSON.stringify(param)));
			console.log(JSON.stringify($scope.result))
			if($scope.result.ret=='success'){
				if($scope.result.item.length>0){
					toastr.warning('该班级已存在，请重新输入');
					 $timeout(function () {
					  $scope.classInfo.classId='';
				   	}, 2000);
				} 
			}else{
				toastr.error($scope.result.message);
			}
		}
				
	}
	//班级名称查重
	$scope.selectClassName=function(){
		if($scope.classInfo.className){
			var param={
				className:$scope.classInfo.className
			}		
			$scope.result= JSON.parse(execute_student("select_class",JSON.stringify(param)));
			console.log(JSON.stringify($scope.result))
			if($scope.result.ret=='success'){
				if($scope.result.item.length>0){
					toastr.warning('该班级名称已存在，请重新输入');
					 $timeout(function () {
					  $scope.classInfo.className='';
				   	}, 2000);
				} 
			}else{
				toastr.error($scope.result.message);
			}
		}
				
	}
	$scope.ok = function() {
		var param = $scope.classInfo;
		//console.log("参数"+JSON.stringify(param))
		$scope.result= JSON.parse(execute_student("insert_class",JSON.stringify(param)));
		//console.log("滴滴滴滴"+JSON.stringify($scope.result))
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
			$modalInstance.close($scope.classInfo.classId);
		}else{
			toastr.error($scope.result.message);
		}		
	}
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
})
//编辑班级
app.controller('editClassModalCtrl',function($scope,$modalInstance,$rootScope,toastr,infos){
	if(infos){
		$scope.classInfo=angular.copy(infos);
	}
	$scope.classInfo.atype1=angular.copy($scope.classInfo.atype);
	$scope.title="编辑班级";
	$scope.noedit=true;
	$scope.ok = function() {
		var param = $scope.classInfo;
		$scope.result= JSON.parse(execute_student("update_class",JSON.stringify(param)));
		console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){
			toastr.success($scope.result.message);
			$modalInstance.close('success');
		}else{
			toastr.error($scope.result.message);
		}		
	}
	$scope.cancel = function() {
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
