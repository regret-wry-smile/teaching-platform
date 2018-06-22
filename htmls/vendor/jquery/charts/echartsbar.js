/*我的月度出勤统计*/
$(function() {
	$.fn.personconut = function(options) {
		var defaults = {};
		var settings = $.extend(defaults, options);

		var personData = [];
		console.log('个人月度考勤统计参数：' + JSON.stringify(settings));
		$.ajax({
			type: "post",
			url: qlinkerservice + "searchPersonAttendanceCount",
			async: false,
			dataType: "json",
			data: {
				json: JSON.stringify(settings)
			},
			success: function(data) {
				console.log('个人月度考勤统计：' + JSON.stringify(data));
				if(data.ret == 'success' && data.item.listMapCount.length > 0) {
					personData = [data.item.listMapCount[0].requiredAttend, data.item.listMapCount[0].attendanceDays, data.item.listMapCount[0].normalPunchCard, data.item.listMapCount[0].fieldPunch, data.item.listMapCount[0].leaveBeforeTimes, data.item.listMapCount[0].absenceworkTotal, data.item.listMapCount[0].lateworkTimes, data.item.listMapCount[0].restDays, data.item.listMapCount[0].businessTotal, data.item.listMapCount[0].leaveTotal];
				} else {
					personData = [];
				}
			}
		});

		var attend_column_chart = echarts.init(document.getElementById('attend_column'));
		var attend_column_option = {
			color: ['#ff7e00'],
			tooltip: {},
			grid: {
				x: 20,
				x2: 20,
				y: 25
			},
			xAxis: {
				type: 'category',
				axisLine: {
					show: false,
					onZero: true,
				},
				data: [{
					value: '应出勤天数',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}, {
					value: '出勤天数',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}, {
					value: '正常打卡',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}, {
					value: '外勤打卡',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}, {
					value: '早退',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}, {
					value: '缺勤',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}, {
					value: '迟到',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}, {
					value: '休息',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}, {
					value: '出差',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}, {
					value: '请假',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}],
				axisTick: {
					alignWithLabel: true,
					show: false,
				},
				series: {
					barGap: '20%',
				}
			},
			yAxis: {
				show: false,
				min: 0,
				max: 22,
			},
			series: [{
				name: '我的月度出勤',
				type: 'bar',
				barWidth: '24px',
				label: {
					normal: {
						show: true,
						position: 'top',
						textStyle: {
							color: '#aeb2b7',
							fontSize: 18
						}
					}
				},
				data: personData,
				itemStyle: {
					normal: {
						color: function(params) {
							var colorList = ['#00a814', '#9900d9', '#3399f0', '#40d2b1', '#ffb400', '#ff6600', '#ffb400', '#c0c0c0', '#ff7315', '#00ddf1', '#ffb400', '#ffb400'];
							return colorList[params.dataIndex]
						},
						barBorderRadius: 4,
					}
				}
			}],
		};

		attend_column_chart.setOption(attend_column_option);

		//点击柱状图跳转相应页面的功能，其中param.name参数为横坐标的值   
		var options = '00';
		attend_column_chart.on('click', function eConsole(param) {
			//这个params可以获取你要的饼图中的当前点击的项的参数
			if(param.name == "出勤天数") {
				options = '01';
			} else if(param.name == "正常打卡") {
				options = '02';
			} else if(param.name == "外勤打卡") {
				options = '03';
			} else if(param.name == "早退") {
				options = '04';
			} else if(param.name == "缺勤") {
				options = '05';
			} else if(param.name == "迟到") {
				options = '06';
			} else if(param.name == "休息") {
				options = '07';
			} else if(param.name == "出差") {
				options = '08';
			} else if(param.name == "请假") {
				options = '09';
			}

			var attendoptions = localStorage.setItem("localattentlist", options);
			_getattendecharts();
		});
		var attenddetaillist = [];
		var _getattendecharts = function() {
			var date = $("#attendanceDate").val().split('-');
			var year = date[0],
				month = date[1];
			var param = {
				year: year,
				month: month,
				option: options
			}
			console.log('个人月度考勤统计参数：' + JSON.stringify(param));
			$.ajax({
				type: "post",
				url: qlinkerservice + "searchPersonAttendanceCount",
				dataType: "json",
				data: {
					json: JSON.stringify(param)
				},
				success: function(data) {

					console.log('个人月度考勤统计：' + JSON.stringify(data));
					if(data.ret == 'success') {
						if(data.item.listMapCount.length > 0) {
							attend_column_chart.setOption({
								series: [{
									data: [data.item.listMapCount[0].requiredAttend, data.item.listMapCount[0].attendanceDays, data.item.listMapCount[0].normalPunchCard, data.item.listMapCount[0].fieldPunch, data.item.listMapCount[0].leaveBeforeTimes, data.item.listMapCount[0].absenceworkTotal, data.item.listMapCount[0].lateworkTimes, data.item.listMapCount[0].restDays, data.item.listMapCount[0].businessTotal, data.item.listMapCount[0].leaveTotal]
								}]
							});
						} else {
							attend_column_chart.setOption({
								series: [{
									data: []
								}]
							});
						}
						/*if(data.item.listMapDetail.length > 0) {
							attenddetaillist= JSON.stringify(data.item.listMapDetail);
							var attendlist=localStorage.setItem("localattentlist",attenddetaillist);							
						} else {
							attenddetaillist= [];							
						}*/

					} else {
						attend_column_chart.setOption({
							series: [{
								data: []
							}]
						});
					}
				}
			});
		}
		$("#dateChange").click(function() {

			_getattendecharts();
		});
	}
});