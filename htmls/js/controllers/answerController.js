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
				$window.location.href = "../../page/answermoudle/stopsingeAnswer.html";
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
							console.log("item" + JSON.stringify($scope.resultmap))
							if($scope.resultmap[rangeList[i]]) {
								var item = $scope.resultmap[rangeList[i]].length;
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
				}

				var colors = ['#5793f3', '#d14a61', '#675bba'];
				option = {
					color: ['#fff'],
					tooltip: {
						trigger: 'axis',
						axisPointer: { // 坐标轴指示器，坐标轴触发有效
							type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
						}
					},
					calculable: true,
					toolbox: {
						show: true,
						feature: {
							mark: {
								show: true
							},
							dataView: {
								show: true,
								readOnly: false
							},
							magicType: {
								show: true,
								type: ['line', 'bar']
							},
							restore: {
								show: true
							},
							saveAsImage: {
								show: true
							}
						}
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: [{
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
							clickable: true
						},
						triggerEvent: true
					}],
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
							},
						},
					}],
					series: [{
						name: '记录统计',
						type: 'bar',
						barWidth: '50%',
						//data:[7, 13, 5,14,2,0],
						data: $scope.data,
						itemStyle: {
							normal: {
								label: {
									show: true,
									position: 'top',
									textStyle: {
										/*color: '#fff',*/
										color: function (params){
											var colorList = ['green','green','green'];
											return colorList[params.dataIndex];
										},
										fontSize: '16'
									},
								}
							}
						}
					}]

				};
				/*柱状图点击事件*/
				myChart.on('click', function(params) {
					console.log("选择的答案:" + JSON.stringify(params));
					myChart.setOption(option);
				});

				if(option && typeof option === "object") {
					myChart.setOption(option, true);
				}
			} else {
				toastr.error($scope.result.message);
			}

		}

	})
	//停止单选答题
app.controller('stopSingeAnswerCtrl', function($scope, toastr, $window) {
		$scope.isStopAswer = false;
		$scope.studentNum = 0;
		var rangeList = []; //答题范围
		$scope.data = []; //柱状图数据
		var resultmap = [];
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
				rangeList = ["A", "B", "C", "D"];
				console.log("答题范围" + JSON.stringify(rangeList))
					//$scope.rangeList=["A","B","C","D","E"];
				$scope.resultmap = JSON.parse(execute_answer("get_single_answer"));
				//$scope.resultmap={"A":10,"B":3};
				console.log("data" + JSON.stringify($scope.resultmap));
				$scope.data = [];
				if(rangeList.length > 0) {
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
				}

				var colors = ['#5793f3', '#d14a61', '#675bba'];
				option = {
					color: ['#fff'],
					tooltip: {
						trigger: 'axis',
						axisPointer: { // 坐标轴指示器，坐标轴触发有效
							type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
						}
					},
					toolbox: {
						show: true,
						feature: {
							mark: {
								show: true
							},
							dataView: {
								show: true,
								readOnly: false
							},
							magicType: {
								show: true,
								type: ['line', 'bar']
							},
							restore: {
								show: true
							},
							saveAsImage: {
								show: true
							}
						}
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: [{
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
					}],
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
						itemStyle: {
							normal: {
								label: {
									show: true,
									position: 'top',
									textStyle: {
										color: '#fff',
										fontSize: '16'
									}
								}
							}
						}
					}]

				};
				/*柱状图点击事件*/
				myChart.on('click', function(params) {
					console.log("选择的答案:" + JSON.stringify(params));
					myChart.setOption(option);
				});

				if(option && typeof option === "object") {
					myChart.setOption(option, true);
				}
			} else {
				toastr.error($scope.result.message);
			}
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
		var result = JSON.parse(execute_testPaper("select_paper", JSON.stringify(param)));
		console.log(JSON.stringify(result));
		if(result.ret == 'success') {
			$scope.paperInfoList = result.item;

		} else {
			toastr.error(result.message);
		}

	};
	$scope.selectOne = function(paperInfo) {
		$scope.onePaperInfo = paperInfo;
		console.log(JSON.stringify($scope.onePaperInfo))

	}

	var _init = function() {
		_isStartClass();
		_selectPaper();
	}();

	//开始客观答题
	$scope.startObjective = function() {
		$scope.result = JSON.parse(execute_answer("start_class_test_objective", $scope.onePaperInfo.testId));
		if($scope.result.ret == 'success') {
			$scope.param = "testId=" + $scope.onePaperInfo.testId;
			$window.location.href = "../../page/answermoudle/classCheck.html?"+$scope.param;
		} else {
			toastr.error($scope.result.message);
		}
	}
	//开始主观答题
	$scope.AnswerSrcoe = function() {
		$scope.result = JSON.parse(execute_answer("start_class_test_subjective", $scope.onePaperInfo.testId));
		if($scope.result.ret == 'success') {
			$scope.param = "testId=" + $scope.onePaperInfo.testId;
			$window.location.href = "../../page/answermoudle/classCheck.html?"+$scope.param;
		} else {
			toastr.error($scope.result.message);
		}
	}

})
app.controller('classuserCheckCtrl', function($scope, toastr, $window,$location) {
	if($location.search()){
		$scope.paperInfo=$location.search();	//试卷id	
	}
	$scope.AllanswerInfo = []; //作答信息数组
	//查询每个人的个人信息
	var _getAllanswerInfo = function() {
		$scope.result = JSON.parse(execute_answer("get_everybody_answerInfo"));
		console.log(JSON.stringify($scope.result))
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
	$scope.gatherPaper=function(){
		$scope.result = JSON.parse(execute_answer("stop_class_test_objective"));
		console.log(JSON.stringify($scope.result))
			if($scope.result.ret == 'success') {
				$scope.AllanswerInfo = $scope.result.item;
			} else {
				toastr.error($scope.result.message);
			}
		}
	
	
	var _init = function() {
		//_getAllanswerInfo();
	}();
	$scope.refreClassTest = function() {
		//_getAllanswerInfo();
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