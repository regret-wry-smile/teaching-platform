//定义模块时引入依赖  
var app = angular.module('app', ['ui.bootstrap', 'toastr']);

app.controller('mainAnswerCtrl', function($scope, toastr, $window) {
		$scope.tabpane = 's';
		$scope.selAnswerType = function(answerType) {
			$scope.tabpane = answerType;
		}
	})
	//单选
app.controller('answerCtrl', function($scope, toastr, $window) {
		//切换答题类型
		$scope.selType = 'char'; //默认字母
		$scope.selAnswerType = function(selType) {
			$scope.selType = selType;
		}

		//跳转到答题中心页面
		$scope.startAnswer = function() {
			var param = {
				type: $scope.selType
			}
			console.log("答题" + JSON.stringify(param))
			$scope.result = JSON.parse(execute_answer("single_answer", JSON.stringify(param)));
			console.log("答题" + JSON.stringify($scope.result));
			if($scope.result.ret == 'success') {
				$scope.param = "answerType=" + $scope.selType;		
				console.log(JSON.stringify($scope.param))
				$scope.objectUrl = '../../page/answermoudle/stopsingeAnswer.html' + '?' + $scope.param;
				$window.location.href = $scope.objectUrl;
			}else{
				toastr.error($scope.result.message);
			}
		}
	})
	//多选
app.controller('answerMutilCtrl', function($scope, toastr, $window) {
		$scope.rangeList = ["C", "D", "E", "F"];
		$scope.range = "C";
		$scope.range1 = angular.copy($scope.range);
		$scope.changeRange = function(range) {
				$scope.range = range;
			}
			//跳转到答题中心页面
		$scope.startAnswer = function() {
			var param = {
				id: "1",
				type: "m", //多选
				range: "A-" + $scope.range
			}
			$scope.result = JSON.parse(execute_answer("start_multiple_answer", JSON.stringify(param)));
			if($scope.result.ret == 'success') {
				$window.location.href = "../../page/answermoudle/stopAnswerType.html";
			} else {
				toastr.error($scope.result.message);
			}
		}

	})
	//停止多选答题
app.controller('stopAnswerTypeCtrl', function($scope, toastr, $window) {	
		$scope.isStopAswer = false;
		$scope.studentNum = 0;
		var rangeList = []; //答题范围
		$scope.data = []; //柱状图数据
		$scope.resultmap = {};
		$scope.rangeList=[];
		var colors=[];
		var letterlist=[];//选择的字母数组
		$scope.answerResult=[];//选择的作答结果
		$scope.answerRate=0;//正确率
		//获取答题人数
		$scope.refresAnswerNum = function() {
				$scope.result = JSON.parse(execute_answer("get_multiple_answer_num"));
				if($scope.result) {
					$scope.studentCount = $scope.result.total;
					$scope.studentNum = $scope.result.answerNum;
					console.log("获取答题" + JSON.stringify($scope.result))
				}

			}
			//停止答题
		var dom = document.getElementById("recordbar");
		var myChart = echarts.init(dom);
		$scope.stopAnswer = function() {
			$scope.result = JSON.parse(execute_answer("stop_multiple_answer"));
			if($scope.result.ret == 'success') {
				$scope.isStopAswer = true;
				rangeList = JSON.parse(execute_answer("get_multiple_range"));
				console.log("答题范围" + JSON.stringify(rangeList))
					//$scope.rangeList=["A","B","C","D","E"];
				$scope.resultmap = JSON.parse(execute_answer("get_multiple_answer_detail"));
				//$scope.resultmap={"A":[{"classId":"BJ1001","className":"自动测试","iclickerId":"3429966709","id":1000662,"status":"1","studentId":"10000002","studentName":"学002"}],"E":[{"classId":"BJ1001","className":"自动测试","iclickerId":"3429469477","id":1000661,"status":"1","studentId":"10000001","studentName":"学001"}]};
				//console.log("data"+JSON.stringify($scope.resultmap))
				$scope.data = [];
				if(rangeList.length > 0) {
					for(var i = 0; i < rangeList.length; i++) {
						if($scope.resultmap != null) {
							//console.log("item" + JSON.stringify($scope.resultmap))
							if($scope.resultmap[rangeList[i]]) {
								//var item = $scope.resultmap[rangeList[i]].length;
								var item={
									value:$scope.resultmap[rangeList[i]].length,
									itemStyle:{normal:{color:'#fff'}
									}
								}
							} else {
								//var item = 0;
								var item={
									value:0,
									itemStyle:{normal:{color:'#fff'}
									}
								}
							}
						} else {
							//var item = 0;
							var item={
								value:0,
								itemStyle:{normal:{color:'#fff'}
								}
							}
						}
						$scope.data.push(item);
						var coloritem="#fff";
						colors.push(coloritem);
					}
				} else {
					//var item = 0;
					var item={
						value:0,
						itemStyle:{normal:{color:'#fff'}
						}
					}
					$scope.data.push(item);
				}
				//var colors = ['#5793f3', '#d14a61', '#675bba'];
				option = {
					color: ['#fff'],
					tooltip: {
						trigger: 'axis',
						axisPointer: { // 坐标轴指示器，坐标轴触发有效
							type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
						}
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: {
						type: 'category',
						//data : ['A', 'B', 'C','D','E','F'],
						data: rangeList,
						axisTick: {
							alignWithLabel: true
						},
						axisLine: {
							lineStyle: {
								color: '#dcdcdc', //这里是为了突出显示加上的，可以去掉
								show: false
							}
						},
						axisLabel: {
							show: true,
							textStyle: {
								color: '#fff',
								fontSize: '16'
							},
						},
					},
					yAxis: [{
						show: false,
						type: 'value',
						splitLine: false, //是否显示网格线
						axisLine: {
							lineStyle: {
								color: '#dcdcdc', //这里是为了突出显示加上的，可以去掉
								show: false
							}
						},
						axisLabel: {
							show: true,
							textStyle: {
								color: '#fff',
								fontSize: '16'
							},
						},
					}],
					series: [{
						name: '记录统计',
						type: 'bar',
						barWidth: '50%',
						//data:[7, 13, 5,14,2,0],
						data: $scope.data,
						label: {
							normal: {
									show: true,
									position: 'top',
									textStyle: {
									color: '#fff',
									fontSize: '16'
								}
							}
						},
						itemStyle: {  
							normal:{  
		　　　　　　　　　　　　      color: function (params){
									var colorList = colors;
									return colorList[params.dataIndex];
								}
							}
						},
					}]

				};
				/*柱状图点击事件*/
				myChart.on('click', function(params) {
				var objs=option.xAxis;		
				 console.log("阿山海上海:"+JSON.stringify(params.name));
					for(var i=0;i<objs.data.length;i++){						
					  var datavalue = objs.data[i];		
					   console.log("value:"+JSON.stringify(datavalue));
		              if(datavalue == params.name){
		              	if(option.series[0].data[params.dataIndex].itemStyle.normal.color=='#fff'){
		              		option.series[0].data[params.dataIndex].itemStyle.normal.color = '#5ed6be';
		              		letterlist.push(datavalue);
						      letterlist = letterlist.sort(function (item1, item2) {
						          return item1.localeCompare(item2)
						      })						 
								$scope.selresultmap = JSON.parse(execute_answer("get_answer_info_sum"));
								console.log(JSON.stringify(letterlist));
								console.log(JSON.stringify($scope.selresultmap));
								if($scope.selresultmap!=null){
									for(var j=0;j<letterlist.length;j++){
										console.log("啥哈哈"+JSON.stringify(letterlist[j]))
										if($scope.selresultmap[letterlist[j]]){
											$scope.answerResult=$scope.selresultmap[letterlist[j]];
										}else{
											$scope.answerResult=[];
										}
										
									}									
									console.log("哈哈哈"+JSON.stringify($scope.answerResult))
					              	$scope.answerRate=($scope.answerResult.length/$scope.studentCount)*100;
					              	//监听正确率
									$scope.$watch('answerRate',function(newvalue,oldvalue){
										if(newvalue=oldvalue){
											$scope.answerRate=newvalue;
										}
									},true)
									
								}
		              	}else{
		              		option.series[0].data[params.dataIndex].itemStyle.normal.color='#fff';
		              		if(option.series[0].data[params.dataIndex].itemStyle.normal.color == '#5ed6be'){
		              			letterlist.push(datavalue);
							      letterlist = letterlist.sort(function (item1, item2) {
							          return item1.localeCompare(item2)
							      })						 
									$scope.selresultmap = JSON.parse(execute_answer("get_answer_info_sum"));
									console.log(JSON.stringify(letterlist));
									console.log(JSON.stringify($scope.selresultmap));
									if($scope.selresultmap!=null){
										for(var j=0;j<letterlist.length;j++){
											console.log("啥哈哈"+JSON.stringify(letterlist[j]))
											if($scope.selresultmap[letterlist[j]]){
												$scope.answerResult=$scope.selresultmap[letterlist[j]];
											}else{
												$scope.answerResult=[];
											}
											
										}									
										console.log("哈哈哈"+JSON.stringify($scope.answerResult))
						              	$scope.answerRate=($scope.answerResult.length/$scope.studentCount)*100;
						              	//监听正确率
										$scope.$watch('answerRate',function(newvalue,oldvalue){
											if(newvalue=oldvalue){
												$scope.answerRate=newvalue;
											}
										},true)
										
									}
		              		}
		              		
		              	}
		              	
						continue;
		              }
		               console.log("选择的答案:"+JSON.stringify(option.series[0].data[params.dataIndex]));		        
					  /*option.series[0].data[i].itemStyle.normal.color = '#fff';*/
		           }
					myChart.setOption(option);
				});
			if(option && typeof option === "object") {
					myChart.setOption(option, true);
				}
			} else {
				toastr.error($scope.result.message);
			}

		}
		
		//查看详情
		$scope.viewDetail=function(){
			$scope.userdetailshow=true;		
		}
		//返回
		$scope.returnPage=function(){
			$scope.userdetailshow=false;		
		}

	})
	//停止单选答题
app.controller('stopSingeAnswerCtrl', function($scope,$location, toastr, $window) {
		if($location.search()){
			$scope.answerType=$location.search().answerType;	//单选类型(数字,字母,判断)
		}
		$scope.userdetailshow=false;//默认答题详情页面隐藏
		$scope.isStopAswer = false;
		$scope.studentNum = 0;
		var rangeList = []; //答题范围
		$scope.data = []; //柱状图数据
		//var resultmap = [];
		var colors=[];//颜色数组
		$scope.answerResult=[];//选择的正确人数数组
		$scope.answerRate=0;
		//获取答题人数
		$scope.refresAnswerNum = function() {
			$scope.result = JSON.parse(execute_answer("get_single_answer_num"));
			$scope.studentNum = $scope.result.current;
			$scope.totalStudent=$scope.result.totalStudent;
			console.log("获取答题" + JSON.stringify($scope.result))
		}

		//停止答题
		var dom = document.getElementById("recordbar");
		var myChart = echarts.init(dom);
		$scope.stopAnswer = function() {
			$scope.result = JSON.parse(execute_answer("stop_single_answer"));
			if($scope.result.ret == 'success') {
				$scope.isStopAswer = true;
				if($scope.answerType=='char'){
					rangeList = ["A", "B", "C", "D"];
				}else if($scope.answerType=='number'){
					rangeList = ["1", "2", "3", "4","5","6","7","8","9"];
				}else if($scope.answerType=='judge'){
					rangeList = ["对", "错"];
				}
				
				console.log("答题范围" + JSON.stringify(rangeList))
					//$scope.rangeList=["A","B","C","D","E"];
				$scope.resultmap = JSON.parse(execute_answer("get_single_answer"));
				//$scope.resultmap={"A":10,"B":3};
				console.log("data" + JSON.stringify($scope.resultmap));
				$scope.data = [];
				/*if(rangeList.length > 0) {
					for(var i = 0; i < rangeList.length; i++) {
						if($scope.resultmap != null) {
							console.log("item" + JSON.stringify($scope.resultmap))
							if($scope.resultmap[rangeList[i]]) {
								var item = $scope.resultmap[rangeList[i]];
							} else {
								var item = 0;
							}
						} else {
							var item = 0;
						}

						$scope.data.push(item);

					}
				} else {
					var item = 0;
					$scope.data.push(item);
				}*/
				
				
				if(rangeList.length > 0) {
					for(var i = 0; i < rangeList.length; i++) {
						if($scope.resultmap != null) {
							//console.log("item" + JSON.stringify($scope.resultmap))
							if($scope.resultmap[rangeList[i]]) {
								//var item = $scope.resultmap[rangeList[i]];
								var item={
									value:$scope.resultmap[rangeList[i]],
									itemStyle:{normal:{color:'#fff'}
									}
								}
							} else {
								//var item = 0;
								var item={
									value:0,
									itemStyle:{normal:{color:'#fff'}
									}
								}
							}
						} else {
							//var item = 0;
							var item={
								value:0,
								itemStyle:{normal:{color:'#fff'}
								}
							}
						}
						$scope.data.push(item);
						var coloritem="#fff";
						colors.push(coloritem);
					}
				} else {
					//var item = 0;
					var item={
						value:0,
						itemStyle:{normal:{color:'#fff'}
						}
					}
					$scope.data.push(item);
				}
				
				
				/*var colors = ['#5793f3', '#d14a61', '#675bba'];*/
				option = {
					color: ['#fff'],
					tooltip: {
						trigger: 'axis',
						axisPointer: { // 坐标轴指示器，坐标轴触发有效
							type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
						}
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: {
						type: 'category',
						//data : ['A', 'B', 'C','D','E','F'],
						data: rangeList,
						axisTick: {
							alignWithLabel: true
						},
						axisLine: {
							lineStyle: {
								color: '#dcdcdc', //这里是为了突出显示加上的，可以去掉
								show: false
							}
						},
						axisLabel: {
							show: true,
							textStyle: {
								color: '#fff',
								fontSize: '16'
							}
						}
					},
					yAxis: [{
						show: false,
						type: 'value',
						splitLine: false, //是否显示网格线
						/*axisTick:{
				        show:false
				    },*/
						axisLine: {
							lineStyle: {
								color: '#dcdcdc', //这里是为了突出显示加上的，可以去掉
								show: false
							}
						},
						axisLabel: {
							show: true,
							textStyle: {
								color: '#fff',
								fontSize: '16'
							}
						},
					}],
					series: [{
						name: '记录统计',
						type: 'bar',
						barWidth: '50%',
						//data:[7, 13, 5,14,2,0],
						data: $scope.data,
						label: {
							normal: {
									show: true,
									position: 'top',
									textStyle: {
									color: '#fff',
									fontSize: '16'
								}
							}
						},
						itemStyle: {  
							normal:{  
		　　　　　　　　　　　　      color: function (params){
									var colorList = colors;
									return colorList[params.dataIndex];
								}
							}
						},
					}]

				};
				/*柱状图点击事件*/
				myChart.on('click', function(params) {
				var objs=option.xAxis;		
				 console.log("阿山海上海:"+JSON.stringify(params.name));
				 /*if(params.name=="对"){
				   	params.name="true";
				   }else{
			   	 	params.name="false";
				   }*/
					for(var i=0;i<objs.data.length;i++){						
					  var datavalue = objs.data[i];		
					  console.log(JSON.stringify(objs.data[i]))
					  
		              if(datavalue == params.name){
		              	console.log("value:"+JSON.stringify(datavalue));
		              	option.series[0].data[params.dataIndex].itemStyle.normal.color = '#5ed6be';
		              	var dataparam={
		              		answer:datavalue
		              	}
		              	$scope.answerResult=JSON.parse(execute_answer("get_single_answer_studentName",JSON.stringify(dataparam)));
		              	console.log("哈哈哈"+JSON.stringify($scope.answerResult))
		              	$scope.answerRate=($scope.answerResult.length/$scope.totalStudent)*100;
		              	//监听正确率
						$scope.$watch('answerRate',function(newvalue,oldvalue){
							if(newvalue=oldvalue){
								$scope.answerRate=newvalue;
							}
						},true)
						continue;
		              }	        
					  option.series[0].data[i].itemStyle.normal.color = '#fff';
		           }
					myChart.setOption(option);
				});
				if(option && typeof option === "object") {
					myChart.setOption(option, true);
				}
			} else {
				toastr.error($scope.result.message);
			}
		}
		
		//查看详情
		$scope.viewDetail=function(){
			$scope.userdetailshow=true;		
		}
		//返回
		$scope.returnPage=function(){
			$scope.userdetailshow=false;		
		}
	})
	//随堂检测
app.controller('classCheckCtrl', function($scope, toastr, $window) {
	$scope.classInfo = {}; //班级信息
	$scope.paperInfoList = []; //试卷数组
	//获取当前班级信息
	var _isStartClass = function() {
			$scope.result = JSON.parse(execute_record("get_classInfo"));
			console.log("巴巴爸爸" + JSON.stringify($scope.result));
			if($scope.result.ret == 'success' && $scope.result.item) {
				$scope.classInfo = $scope.result.item;
			} else {
				$scope.classInfo = {};
			}
		}
		//查询试卷
	var _selectPaper = function() {
		var param = {}
		$scope.result = JSON.parse(execute_testPaper("select_now_paper", JSON.stringify(param)));
		console.log(JSON.stringify($scope.result));
		if($scope.result.ret == 'success') {
			$scope.paperInfoList = $scope.result.item;
			if($scope.result.item.length>0){
				$scope.paperInfoList[0].checked=true;
				$scope.onePaperInfo=$scope.paperInfoList[0];
			}
		} else {
			toastr.error($scope.result.message);
		}

	};
	//单选试卷
	$scope.selectOne = function(paperInfo) {
		if(paperInfo.checked==true){
			$scope.onePaperInfo = paperInfo;
			angular.forEach($scope.paperInfoList,function(i){
				if($scope.onePaperInfo.testId!=i.testId){
					i.checked=false;///单选 
				}
			})
		}else{
			$scope.onePaperInfo='';			
		}
		
	}

	var _init = function() {
		_isStartClass();
		_selectPaper();
	}();

	//开始客观答题
	$scope.startObjective = function() {
		if($scope.onePaperInfo){
		$scope.result = JSON.parse(execute_answer("start_class_test_objective", $scope.onePaperInfo.testId));
		if($scope.result.ret == 'success') {
			$scope.param = "testId=" + $scope.onePaperInfo.testId + "&answerType=" + "1" ;
			$scope.objectUrl = '../../page/answermoudle/classCheck.html' + '?' + $scope.param;
			$window.location.href =$scope.objectUrl;
		} else {
			toastr.error($scope.result.message);
		}
		}else{
			toastr.warning("请选择试卷");
		}
	}
	//开始主观答题
	$scope.AnswerSrcoe = function() {
		if($scope.onePaperInfo){
			$scope.result = JSON.parse(execute_answer("start_class_test_subjective", $scope.onePaperInfo.testId));
			if($scope.result.ret == 'success') {
				$scope.param = "testId=" + $scope.onePaperInfo.testId + "&answerType=" + "0" ;
				$scope.objectUrl = '../../page/answermoudle/classCheck.html' + '?' + $scope.param;
				$window.location.href =$scope.objectUrl;
			} else {
				toastr.error($scope.result.message);
			}
		}else{
			toastr.warning("请选择试卷");
		}
		
	}

})
app.config(['$locationProvider', function($locationProvider) {  
	    //$locationProvider.html5Mode(true);  
 	$locationProvider.html5Mode({
	enabled: true,
	requireBase: false
	});
}]);
app.controller('classuserCheckCtrl', function($scope, toastr,$location, $window,$modal) {
	if($location.search()){
		$scope.paperInfo=$location.search();	//试卷id	答题类型answerType("0":主观答题,"1":客观答题)
		console.log(JSON.stringify($scope.paperInfo))
	}
	$scope.AllanswerInfo = []; //作答信息数组	
	$scope.oneanswerList=[];//个人答题详情
	//查询每个人的个人信息
	var _getAllanswerInfo = function() {
		$scope.result = JSON.parse(execute_answer("get_everybody_answerInfo"));
		//console.log(JSON.stringify($scope.result))
		if($scope.result.ret == 'success') {
			$scope.AllanswerInfo = $scope.result.item;
		} else {
			toastr.error($scope.result.message);
		}
	}
	/*for(var i=0;i<120;i++){
		var item={"answerCount":0,"percent":0,"studentId":"10000002","studentName":"学002"};	
		$scope.AllanswerInfo.push(item);
	}*/

	
	//收取试卷
	$scope.isgatherPaper=true;//是否是收取试卷按钮
	$scope.gatherPaper=function(){
	if($scope.paperInfo.answerType=="1"){			
		$scope.result = JSON.parse(execute_answer("stop_class_test_objective",$scope.paperInfo.testId));
	}else{
		$scope.result = JSON.parse(execute_answer("stop_class_test_subjective",$scope.paperInfo.testId));
	}	
	console.log(JSON.stringify($scope.result))
	if($scope.result.ret == 'success') {
		$scope.isgatherPaper=false;
		} else {
			$scope.isgatherPaper=true;
			toastr.error($scope.result.message);
		}
	}
	//查询主观答题和客观答题的单个人的信息
	$scope.selectRecord=function(item){
		var param={
			testId:$scope.paperInfo.testId,
			studentId:item.studentId
		}
		if($scope.paperInfo.answerType=="1"){			
			$scope.result = JSON.parse(execute_record("select_objective_record",JSON.stringify(param)));
		}else{
			$scope.result = JSON.parse(execute_record("select_subjective_record",JSON.stringify(param)));
		}
		//console.log(JSON.stringify($scope.result))
		if($scope.result.ret == 'success') {
			$scope.oneanswerList=[];
			$scope.oneanswerList = $scope.result.item;
			var modalInstance = $modal.open({
				templateUrl: 'oneAnswerDetailModal.html',
				controller: 'oneAnswerDetailCtrl',
				size: 'md',
				backdrop:false,
				resolve: {
					infos: function() {
						return $scope.oneanswerList;
					},
					type:function(){
						return $scope.paperInfo.answerType;
					}
				}
			});
		
			modalInstance.result.then(function(info) {
			}, function() {
		});
			
		} else {
			toastr.error($scope.result.message);
		}
	}
	
	
	var _init = function() {
		_getAllanswerInfo();
	}();
	//刷新
	$scope.refreClassTest = function() {
		_getAllanswerInfo();
	}
	
})
//个人答题详情
app.controller('oneAnswerDetailCtrl', function($scope,$modalInstance,toastr,infos,type) {
	$scope.oneanswerLists=[];//个人答题详情
	if(type){
		$scope.answerType=type;//作答类型（主客）
	}
	if(infos){
		$scope.oneanswerLists=angular.copy(infos);
		for(var i=0;i<120;i++){
			var item={"answer":"2","classHourId":"24323e6175624858bcd3859c4fbb4ab5","classId":"BJ1001","id":12681,"question":"","questionId":"6","questionType":"4","result":"","score":"5.0","studentId":"10000003","studentName":"学003","subject":"语文","testId":"4Y0001","trueAnswer":""}
			$scope.oneanswerLists.push(item);
		}
		
	}
	
	$scope.cancel=function(){
		$modalInstance.dismiss('cancel');
	}
	
})

//人员列表
app.controller('userAnswerListCtrl', function($scope, toastr,$location, $window,$modal) {
	$scope.userAnswerList=[];//人员作答数组
	if($location.search()){
		$scope.userAnswerList=$location.search().userAnswerList;
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
						width: "10rem",
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
			default:{
				statename = AnswerType;
				break;
			}
		}
		return statename;
	}
});