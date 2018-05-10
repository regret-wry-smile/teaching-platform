angular.module('someApp', []).controller('someController', function($scope,$interval) {
	//获取窗口属性
	var property = JSON.parse(set_size());
	var mainDiv = document.getElementById("mainDiv");
	mainDiv.style.width = property["shellMaxWidth"]+"px";
	mainDiv.style.height = property["shellMaxHeight"]+"px";
	$scope.answerType = "客观题";
	var option;
	/*开始答题*/
	$scope.startAnswer = function(){ 
		var json = JSON.parse(start_answer($scope.answerType));
		switch($scope.answerType){
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
		/*柱状图点击事件*/
		myChart.on('click', function (params) {
		/*	alert("选择的答案:"+params.name);*/
			var objs=option.xAxis;
			for(var i=0;i<objs.data.length;i++){
			  var value = objs.data[i].value;
              if(value == params.name){
				option.series[0].data[params.dataIndex].itemStyle.normal.color = 'red';
				continue;
              }
			  option.series[0].data[i].itemStyle.normal.color = 'green';
           }
		   myChart.setOption(option);
		});
		myChart.setOption(option);
	};
	
	/*选择答题类型*/
	$scope.selectType = function(answerType ){  
		$scope.answerType = answerType ;
	}; 
	
	
	/*关闭窗口*/
	var _CLOSESHELL= function(){  
		stop_answer();
		close_shell();  
	};
	
	/*停止答题*/
	var _STOPANSWER= function(){
		stop_answer();
		/*获取答题名次*/ 
		$scope.rankings = JSON.parse(getRanking()); 
	};
	
	var myChart = echarts.init(document.getElementById('right'));
	
	/*客观题*/
	var _KEGUANTI = function(json){
		option = {
			tooltip : {
				trigger: 'axis',
				axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			}
			},
			 grid: {
				left: '3%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			xAxis: {
				type : 'category',
				data: [{
					value :'A', 
					textStyle: {
						fontSize: 20
					}
				},{
					value :'B', 
					textStyle: {
						fontSize: 20
					}
				},{
					value :'C', 
					textStyle: {
						fontSize: 20
					}
				},{
					value :'D', 
					textStyle: {
						fontSize: 20
					}
				}]
				
			},
			yAxis: [
				{
					type : 'value',
					axisLabel:{
						textStyle:{
						fontSize:20
						}						
					}
				}
			],
			series:  [{
					type:'bar',
					label: {
						normal: {
							show: true,
							position: 'inside'
						}
					},
					data:[{
							value:json.A,
							itemStyle:{normal:{color:'green'} }
						}, {
							value:json.B,
							itemStyle:{normal:{color:'green'} }
						}, {
							value:json.C,
							itemStyle:{normal:{color:'green'} }
						},{
							value:json.D,
							itemStyle:{normal:{color:'green'} }
						}]
				}]
		};
		
	}
	
	/*主观题*/
	var _ZHUGUANTI = function(json){
		option = {
			tooltip : {
				trigger: 'axis',
				axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			}
			},
			 grid: {
				left: '3%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			xAxis: {
				type : 'category',
				data: [{
					value :'懂', 
					textStyle: {
						fontSize: 20
					}
				},{
					value :'不懂', 
					textStyle: {
						fontSize: 20
					}
				}]
				
			},
			yAxis: [
				{
					type : 'value',
					axisLabel:{
						textStyle:{
						fontSize:20
						}						
					}
				}
			],
			series:  [
				{
					type:'bar',
					label: {
						normal: {
							show: true,
							position: 'inside'
						}
					},
					data:[{
							value:json.yes,
							itemStyle:{normal:{color:'green'} }
						}, {
							value:json.no,
							itemStyle:{normal:{color:'green'} }
						}]
				}
			]
		};
		
	}
	
	/*判断题*/
	var _PANDUANTI = function(json){
		option = {
			tooltip : {
				trigger: 'axis',
				axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			}
			},
			 grid: {
				left: '3%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			xAxis: {
				type : 'category',
				data: [{
					value :'对', 
					textStyle: {
						fontSize: 20
					}
				},{
					value :'错', 
					textStyle: {
						fontSize: 20
					}
				}]
				
			},
			yAxis: [
				{
					type : 'value',
					axisLabel:{
						textStyle:{
						fontSize:20
						}						
					}
				}
			],
			series:  [
				{
					type:'bar',
					label: {
						normal: {
							show: true,
							position: 'inside'
						}
					},
					data:[{
							value:json.yes,
							itemStyle:{normal:{color:'green'} }
						}, {
							value:json.no,
							itemStyle:{normal:{color:'green'} }
						}]
				}
			]
		};
	}
	
});