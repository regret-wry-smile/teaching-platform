//定义模块时引入依赖  
var app = angular.module('app', ['ui.bootstrap', 'toastr']);
//作答记录
app.controller('answerRecordCtrl', function($scope, toastr) {
	$scope.setClass={
		classes:'',//班级id
		subject:'',//科目名称
		sujectHour:'',//课程id
		paper:'',//试卷id
		sujectHour1:''
	}
	$scope.classList=[];//班级数组
	$scope.subjectlists=[];//科目数组
	$scope.classhourList=[]//课程数组
	$scope.paperList=[];//试卷数组
	$scope.recordList=[]//作答记录数组
	/*查询班级列表*/
	var _selectClass = function() {
		$scope.result = JSON.parse(execute_student("select_class"));
		if($scope.result.ret=='success'){
			if($scope.result.item.length>0){
				angular.forEach($scope.result.item,function(i){
					var item={
						key:i.classId,
						value:i.className
					}
					$scope.classList.push(item);
					//console.log("班级"+JSON.stringify($scope.classList))
					$scope.setClass.classes=$scope.classList[0].key;
					$scope.classesobject=$scope.classList[0];
					$scope.setClass.classes1=angular.copy($scope.setClass.classes);								
	
				})			
			}
		}else{
			toastr.error($scope.result.message);
		}
	};
	_selectClass();
	//查询科目
	var _getsubject=function(){
		$scope.subjectlists= JSON.parse(execute_testPaper("get_subject"));
		if($scope.subjectlists.length>0){
			$scope.setClass.subject=$scope.subjectlists[0];
			$scope.setClass.subject1=angular.copy($scope.setClass.subject);
			$scope.classhourList=[];
			//_selectClassHour();
		}
	}
	_getsubject();
	//查询课程
	var _selectClassHour=function(){
		$scope.result=JSON.parse(execute_record("select_class_hour",$scope.setClass.classes,$scope.setClass.subject));
		//console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){			
			if($scope.result.item.length>0){
				angular.forEach($scope.result.item,function(i){
					console.log('122333'+JSON.stringify(i));
					var item={
						key:i.class_hour_id,
						value:i.class_hour_name
					}
				    $scope.classhourList.push(item);
						if($scope.classhourList.length>0){
						$scope.setClass.sujectHour=$scope.classhourList[0].key;
						$scope.sujectHourobject=$scope.classhourList[0]||{};
						$scope.setClass.sujectHour1=angular.copy($scope.setClass.sujectHour);
						
					}
					
				})
			}
		}else{
			toastr.error($scope.result.message);
		}
	}
	_selectClassHour();
	
	//查询试卷
	var _selectPaper=function(){
		var param={
			classHourId:$scope.setClass.sujectHour
		}
		console.log(JSON.stringify(param))
		$scope.result=JSON.parse(execute_testPaper("select_paper_by_classHourId",JSON.stringify(param)));
		console.log(JSON.stringify($scope.result));
		if($scope.result.ret=='success'){			
			if($scope.result.item.length>0){
				angular.forEach($scope.result.item,function(i){
					var item={
						key:i.testId,
						value:i.testName
					}
					$scope.paperList=[];
					$scope.paperList.push(item);
					if($scope.paperList.length>0){
						$scope.setClass.paper=$scope.paperList[0].key;
						$scope.paperobject=$scope.paperList[0];
						console.log("试卷"+JSON.stringify($scope.paperobject));
						$scope.setClass.paper1=angular.copy($scope.setClass.paper);
					}
					
				})
			}else{
				$scope.paperList=[];
			}
		}else{
			toastr.error($scope.result.message);
		}
	}
	_selectPaper();
	
	
	//查询记录
	var _selectRecord=function(){
		if($scope.setClass.sujectHour&&$scope.setClass.paper){
			var param={
				classId:$scope.setClass.classes,
				subjectName:$scope.setClass.subject,
				testId:$scope.setClass.paper,
				classHourId:$scope.setClass.sujectHour
			}
			console.log("记录"+JSON.stringify(param))
			$scope.result=JSON.parse(execute_record("select_record",JSON.stringify(param)));
			if($scope.result.ret=='success'){
				$scope.recordList=$scope.result.item;
			}else{
				toastr.error($scope.result.message);
			}
		}		
	}
	_selectRecord();
	//切换班级
	$scope.changeClass=function(classes){
		$scope.setClass.classes=classes;
		angular.forEach($scope.classList,function(i){
			if($scope.setClass.classes==i.key){
				$scope.classesobject=i;
			 	$scope.classhourList=[];
				$scope.setClass.sujectHour="";
				$scope.sujectHourobject=="";
				_selectClassHour();
				
			}
		})
		
	}
	//切换科目
	$scope.changeSubject=function(subject){
		$scope.setClass.subject	=subject;
		$scope.classhourList=[];
		$scope.setClass.sujectHour="";
		$scope.sujectHourobject=="";
		_selectClassHour();
	}
	
	//切换课程
	$scope.changeClassHour=function(sujectHour){
		$scope.setClass.sujectHour=sujectHour;
		angular.forEach($scope.classhourList,function(i){
			if($scope.setClass.sujectHour==i.key){
				$scope.sujectHourobject=i;
				$scope.paperList=[];
				$scope.setClass.paper='';
				$scope.paperobject='';
				_selectPaper();
			}
		})
		
	}
	
	return false;
	
	
	
	
		var _init=function(){
		_selectClass();
		_getsubject();
		_selectRecord();
	}();
	
	
	
	
	//切换试卷
	$scope.changePaper=function(paper){
		$scope.setClass.paper=paper;
		angular.forEach($scope.paperList,function(i){
			if($scope.setClass.paper==i.key){
				$scope.paperobject=i;
			}
		})
	}
	//全选
	$scope.selectAll = function(data) {
		if($scope.selected) {
			$scope.onechecked = [];
			angular.forEach($scope.recordList, function(i) {
				i.checked = true;
				var item = i;
				$scope.checkedId.push(i.studentId.toString());
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
		angular.forEach($scope.studentList, function(i) {
			var index = $scope.checkedId.indexOf(i.studentId);
			if(i.checked && index === -1) {
				var item = i;
				$scope.onechecked.push(item);
				$scope.checkedId.push(i.studentId.toString());

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
	$scope.deleteRcord=function(){
		if($scope.checkedId.length>0){
			var content="删除选中记录";
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
				var param={
					testId:$scope.setClass.paper,
					studentIds:$scope.checkedId
				}
				console.log(JSON.stringify(param))
				$scope.result = JSON.parse(execute_record("delete_record",param));
				if($scope.result.ret=='success'){					
					toastr.success($scope.result.message);
					_selectRecord();
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
			toastr.warning("请选择记录");
		}
		
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
})
app.directive('select1', function() {
	return {
		restrict: 'A',
		require: 'ngModel',
		scope:{
			defalutvalue:'=?',
			list:'=?'
		},
		link: function(scope, element, attrs, ngModelCtr) {
		scope.$watch('defalutvalue+list',function(){
			if(scope.defalutvalue){
				if(scope.list){
					var str='';
					for(var i=0;i<scope.list.length;i++){
						str+='<option value="'+scope.list[i]+'">'+scope.list[i]+'</option>';
					}
					$(element).html(str);
				}
				
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
app.directive('select2', function() {
	return {
		restrict: 'A',
		require: 'ngModel',
		scope:{
			defalutvalue:'=?',
			list:'=?'
		},
		link: function(scope, element, attrs, ngModelCtr) {
		scope.$watch('defalutvalue+list',function(){
			if(scope.defalutvalue){
				if(scope.list){
					var str='';
					for(var i=0;i<scope.list.length;i++){
						str+='<option value="'+scope.list[i].key+'">'+scope.list[i].value+'</option>';
					}
					$(element).html(str);
				}
				
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