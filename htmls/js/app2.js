angular.module('someApp', []).controller('someController', function($scope, $interval) {
	$scope.projectType = get_project_type(); //软件类型,S为学生端T为老师端
	$scope.answerType = "客观题";  //选择答题类型
	var colors = new Array('#ff9936', '#4db3fb', '#8fda61', '#9587dc'); //柱状图颜色
	var option; //柱状图对象
	$scope.rankings=[];//名次数组
	$scope.isAnswer = -1; //默认处于未点击状态 0停止状态；1开始答题状态
	var myTimer;	//刷新柱状图定时器
	var rankings; //答题速度排名
	var myChart = echarts.init(document.getElementById('right'));
	/*选择答题类型*/
	$scope.selectType = function(answerType) {
		if (answerType != $scope.answerType) {
			$scope.answerType = answerType;
		}
		if($scope.isAnswer == 1){
			$scope.endAnswer(0);
		}
		if($scope.projectType){
			//清除答题缓存
			clear_answerMap();
			//清除名次
			$scope.rankings=[];
			$scope.shownodata= true;
			myChart.clear();
		}
		
	};
	$scope.shownodata=true;//显示‘未开始答题’
	//监听是否开始答题
	$scope.$watch('isAnswer',function(newvalue,oldvalue){
		if(newvalue==1){
			$scope.shownodata= false;
		}
	},true)

	/*柱状图点击的事件*/
	var handle = function(params){
		var objs = option.xAxis;
		if($scope.projectType){
			for (var i = 0; i < objs.data.length; i++) {
				var value = objs.data[i].value;
				objs.data[i].textStyle.color="#969696";
				if (params.name ==value ) {　
					
					option.series[0].data[params.dataIndex].itemStyle.normal.color = 'rgba(247,68,68,.5)';	
					
					objs.data[params.dataIndex].textStyle.color= 'rgba(247,68,68,.5)';
					var params = [$scope.answerType,value];
					select_answer(value); //同步选择答案
					$scope.rankings = JSON.parse(get_ranking(params));
					$scope.$apply();
					continue;
				} 
			 option.series[0].data[i].itemStyle.normal.color = colors[i];
			}
		}else{
			var objs = option.xAxis;
			for (var i = 0; i < objs.data.length; i++) {
				var value = objs.data[i].value;
				objs.data[i].textStyle.color="#969696";
				if (params.selectAnswer==value ) {
					option.series[0].data[i].itemStyle.normal.color = 'rgba(247,68,68,.5)';	
					objs.data[i].textStyle.color= 'rgba(247,68,68,.5)';
					var params = [$scope.answerType,value];
					$scope.rankings = JSON.parse(get_ranking(params));
					continue;
				} 
				option.series[0].data[i].itemStyle.normal.color = colors[i];
			}
		}
		myChart.setOption(option);
	}
	
	/*开始答题*/
	/*切换启停用*/
	$scope.startAnswer = function(val) {
		myChart.off('click', handle);
		if (val != $scope.isAnswer) {
			$scope.isAnswer = val;	
		}
		//获取答题名次
		$scope.rankings = JSON.parse(get_ranking());
		
		if($scope.projectType){
			//向硬件发送答题指令
			start_answer($scope.answerType);
		}
		
		if(myTimer != null){
			$interval.cancel(myTimer);  
		}
		/*定时获取答题数据*/
		myTimer = $interval(function(){
		//获取答题名次
		$scope.rankings = JSON.parse(get_ranking());
		var json = JSON.parse(get_answer_data($scope.answerType));
		switch ($scope.answerType) {
			case "客观题":
				_KEGUANTI(json);
				break;
			case "主观题":
				_ZHUGUANTI(json);
				break;
			case "判断题":
				_PANDUANTI(json);
				break;
		}
		myChart.setOption(option);
		},500);
		
	};
	
	 
	
	//关闭定时器
	$scope.stopTimer = function(params){  
	   $interval.cancel(myTimer);  
	}; 
	
	$scope.chartClick = function(){
		myChart.trigger('click',{'selectAnswer':selectAnswer});
	}
	
	/*停止答题*/
	$scope.endAnswer = function(val){
		$scope.isAnswer = val;
		$scope.stopTimer();
		/*柱状图点击事件*/
		myChart.on('click', handle);
		myChart.setOption(option);
		if($scope.projectType){
			//向硬件发送答题指令
			stop_answer();
		}
	}

	
		
	
	/*关闭窗口*/
	$scope.closeShell = function(){
		if($scope.projectType){
			$scope.isAnswer = -1; 
			$scope.stopTimer();
			stop_answer();
		}
		close_shell();
	}

	/*客观题*/
	var _KEGUANTI = function(json) {
		var yMax = json.max;
		var dataShadow = [];
		var data = [ {
			value : json.A,
			itemStyle : {
				normal : {
					color : colors[0]
				}
			}
		}, {
			value : json.B,
			itemStyle : {
				normal : {
					color : colors[1]
				}
			}
		}, {
			value : json.C,
			itemStyle : {
				normal : {
					color : colors[2]
				}
			}
		},
			{
				value : json.D,
				itemStyle : {
					normal : {
						color : colors[3]
					}
				}
			}
		];
		//x轴标签
		var xtitle = [ {
			value : '选A',
			textStyle : {
				fontSize : 24,
				color : '#969696'
			}
		},
			{
				value : '选B',
				textStyle : {
					fontSize : 24,
					color : '#969696'
				}
			},
			{
				value : '选C',
				textStyle : {
					fontSize : 24,
					color : '#969696'
				}
			},
			{
				value : '选D',
				textStyle : {
					fontSize : 24,
					color : '#969696'
				}
			} ];
		for (var i = 0; i < data.length; i++) {
			dataShadow.push(yMax);
		}
		option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			grid : {
				top : '25%',
				left : '3%',
				right : '4%',
				bottom : '8%',
				containLabel : true
			},
			xAxis : {
				type : 'category',
				axisLine : {
					show : false,
					onZero : false,
				},
				series : {
					barGap : '5%',
				},
				axisTick : {
					alignWithLabel : true,
					show : false,
				},
				data : xtitle
			},
			yAxis : [
				{
					show : false,
					type : 'value',
					axisLabel : {
						textStyle : {
							fontSize : 24
						}
					},
					axisLine : {
						show : false,
						onZero : false,
					},
					axisTick : {
						alignWithLabel : true,
						show : false,
					},
				}
			],
			dataZoom : [
				{
					type : 'inside'
				}
			],
			series : [
				{
					type : 'bar',
					label : {
						normal : {
							show : true,
							position : 'top',
							formatter : function(param) {
								 return (param.value / yMax * 100).toFixed(2)+ '%';
								
							},
							textStyle: {
	                            fontFamily : "微软雅黑",
	                            fontSize : 16,
	                        }
						}
					},
					data : data
				}, {
					type : 'bar',
					barGap : '-100%',
					label : {
						normal : {
							show : true,
							position : ['22%', '-45'],
							formatter : '{c}人',
							textStyle: {
	                            fontFamily : "微软雅黑",
	                            fontSize : 16,
	                        }
						}
					},
					data : data
				},
				{ // For shadow
					type : 'bar',
					barGap : '-100%',
					barCategoryGap : '60%',
					itemStyle : {
						normal : {
							color : 'rgba(0,0,0,0.05)'
						}
					},
					data : dataShadow,
					animation : false,
				}
			]
		};

	}

	/*主观题*/
	var _ZHUGUANTI = function(json) {
		var yMax = json.max;
		var dataShadow = [];
		var data = [ {
			value : json.Y,
			itemStyle : {
				normal : {
					color : '#ff9936',
					fontSize : 40
				}
			}
		}, {
			value : json.N,
			itemStyle : {
				normal : {
					color : '#9587dc',
					fontSize : 40
				}
			}
		} ];
		for (var i = 0; i < data.length; i++) {
			dataShadow.push(yMax);
		}
		var xtitle = [ {
			value : '懂',
			textStyle : {
				fontSize : 24,
				color : '#969696'
			}
		}, {
			value : '不懂',
			textStyle : {
				fontSize : 24,
				color : '#969696'
			}
		} ];
		option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			grid : {
				top : '30%',
				left : '3%',
				right : '4%',
				bottom : '8%',
				containLabel : true
			},
			xAxis : {
				type : 'category',
				axisLine : {
					show : false,
					onZero : false,
				},
				data : xtitle,
				series : {
					barGap : '20%',
				},
				axisTick : {
					alignWithLabel : true,
					show : false,
				},
			},
			yAxis : [
				{
					show : false,
					type : 'value',
					axisLabel : {
						textStyle : {
							fontSize : 24
						}
					},
					axisLine : {
						show : false,
						onZero : false,
					},
					axisTick : {
						alignWithLabel : true,
						show : false,
					},
				}
			],
			dataZoom : [
				{
					type : 'inside'
				}
			],
			series : [
				{
					type : 'bar',
					label : {
						normal : {
							show : true,
							position : 'top',
							formatter : function(param) {
								 return (param.value / yMax * 100).toFixed(2)+ '%'							
							},
							textStyle: {
	                            fontFamily : "微软雅黑",
	                            fontSize : 16,
	                        }
						}
					},
					data : data
				}, {
					type : 'bar',
					barGap : '-100%',
					label : {
						normal : {
							show : true,
							position : ['22%', '-45'],
							formatter : '{c}人',
							textStyle: {
	                            fontFamily : "微软雅黑",
	                            fontSize : 16,
	                        }
						}
					},
					data : data
				},
				{ // For shadow
					type : 'bar',
					barGap : '-100%',
					barCategoryGap : '80%',
					itemStyle : {
						normal : {
							color : 'rgba(0,0,0,0.05)'
						}
					},
					data : dataShadow,
					animation : false,
				}
			]
		};

	}

	/*判断题*/
	var _PANDUANTI = function(json) {
		var yMax = json.max;
		var dataShadow = [];
		var data = [ {
			value : json.Y,
			itemStyle : {
				normal : {
					color : '#ff9936'
				}
			}
		}, {
			value : json.N,
			itemStyle : {
				normal : {
					color : '#9587dc'
				}
			}
		} ];
		for (var i = 0; i < data.length; i++) {
			dataShadow.push(yMax);
		}
		var xtitle = [ {
			value : '对',
			textStyle : {
				fontSize : 24,
				color : '#969696'
			}
		}, {
			value : '错',
			textStyle : {
				fontSize : 24,
				color : '#969696'
			}
		} ];
		option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			grid : {
				top : '25%',
				left : '3%',
				right : '4%',
				bottom : '8%',
				containLabel : true
			},
			legend : {
				y : 'bottom',
				selectedMode : false,
				data : data
			},
			xAxis : {
				type : 'category',
				axisLine : {
					show : false,
					onZero : false,
				},
				data : xtitle,
				series : {
					barGap : '20%',
				},
				axisTick : {
					alignWithLabel : true,
					show : false,
				},
			},
			yAxis : [ {
				show : false,
				type : 'value',
				axisLabel : {
					textStyle : {
						fontSize : 24
					}
				},
				axisLine : {
					show : false,
					onZero : false,
				},
				axisTick : {
					alignWithLabel : true,
					show : false,
				},
			} ],
			dataZoom : [
				{
					type : 'inside'
				}
			],
			series : [

				{
					type : 'bar',
					label : {
						normal : {
							show : true,
							position : 'top',
							formatter : function(param) {
								 return (param.value / yMax * 100).toFixed(2)+ '%'							
							},
							textStyle: {
	                            fontFamily : "微软雅黑",
	                            fontSize : 16,
	                        }	
						}
					},
					data : data
				}, {
					type : 'bar',
					barGap : '-100%',
					label : {
						normal : {
							show : true,
							position : ['22%', '-45'],
							formatter : '{c}人',
							textStyle: {
	                            fontFamily : "微软雅黑",
	                            fontSize : 16,
	                        }	
													
						}
					},
					data : data
				}, { // For shadow
					type : 'bar',
					barGap : '-100%',
					barCategoryGap : '80%',
					itemStyle : {
						normal : {
							color : 'rgba(0,0,0,0.05)'
						}
					},
					data : dataShadow,
					animation : false,
				}
			]
		};

	}
	$scope.toImport = function(){
		if($scope.isAnswer == 1){
			$scope.getTip("请先停止答题！");
		}else{
			to_import();
		}
	}
	//确定保存
	$scope.isSave=false;
	$scope.startSave=function(){
		if($scope.isSave==false){
			$scope.isSave=true;
		}
	}
		//提示框
	$scope.getTip =  function (message) {
        var dblChoseAlert = simpleAlert({
            "title":"提示:",
            "content":message,
        })
    }
	
});