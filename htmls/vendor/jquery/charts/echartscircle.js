$(function(){
	$.fn.teamconut = function(options) {
		var x, drag = this,
			defaults = {};
		var settings = $.extend(defaults, options);

		var teamData = [];

		console.log('团队当天出勤统计参数123：' + JSON.stringify(settings));
		if (settings.partmentid) {
			$.ajax({
				type: "post",
				url: qlinkerservice + "searchGroupAttendanceCountRecord",
				async: false,
				dataType: "json",
				data: {
					json: JSON.stringify(settings)
				},
				success: function(data) {
				
					console.log('团队当天出勤统计：' + JSON.stringify(data));
					if (data.ret == 'success') {
						if (data.item.length > 0) {
							$('#realCount').text(data.item[0].realCount);
							$('#requiredCount').text(data.item[0].requiredCount);
							$('#averageDept').text(data.item[0].averageDept);
							teamData = [data.item[0].normalAttendance, data.item[0].fieldAttendance, data.item[0].leaveEarly, data.item[0].lateAttendance, data.item[0].absenseAttendance, data.item[0].leaveApplication, data.item[0].restCount, data.item[0].bussiness];
						} else {
							$('#realCount').text('');
							$('#requiredCount').text('');
							$('#averageDept').text('');
							teamData = [];
						}
					} else {
						alert("发生错误！");
					}
				}
			});
		}

		var team_attend_column_chart = echarts.init(document.getElementById('team_attend_column'));
		var team_attend_column_option = {
			color: ['#ff7e00'],
			tooltip: {},
			grid: {
				x: 20,
				x2: 20,
				y: 25,
				height: 120
			},
			xAxis: {
				type: 'category',
				axisLine: {
					show: false,
					onZero: true,
				},
				data: [{
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
					value: '迟到',
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
				}/*, {
					value: '缺卡',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}*/, {
					value: '请假',
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
				max: 100,
			},
			series: [{
				name: '团队今日出勤',
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
				data: teamData,
				itemStyle: {
					normal: {
						color: function(params) {
							var colorList = ['#3399f0', '#40d2b1', '#ffb400', '#ff6600', '#c50000', '#00b5f8', '#ffd92a', '#c0c0c0', '#ff7315'];
							return colorList[params.dataIndex]
						},
						barBorderRadius: 4,
					}
				}
			}],
		};

		team_attend_column_chart.setOption(team_attend_column_option);

		$("#dateChange").click(function() {
			var param = {
				date: $("#attendanceDate").val(),
				partmentid: $("#partmentId").text()
			}
			console.log('团队当天出勤统计参数：' + JSON.stringify(param));
			$.ajax({
				type: "post",
				url: qlinkerservice + "searchGroupAttendanceCountRecord",
				dataType: "json",
				data: {
					json: JSON.stringify(param)
				},
				success: function(data) {
					console.log('团队当天出勤统计：' + JSON.stringify(data));
					if (data.ret == 'success') {
						if (data.item.length > 0) {
							$('#realCount').text(data.item[0].realCount);
							$('#requiredCount').text(data.item[0].requiredCount);
							$('#averageDept').text(data.item[0].averageDept);
							team_attend_column_chart.setOption({
						        series: [{
						            data: [data.item[0].normalAttendance, data.item[0].fieldAttendance, data.item[0].leaveEarly,  data.item[0].lateAttendance, data.item[0].absenseAttendance, data.item[0].leaveApplication, data.item[0].restCount, data.item[0].bussiness]
						        }]
							});
						} else {
							$('#realCount').text('');
							$('#requiredCount').text('');
							$('#averageDept').text('');
							team_attend_column_chart.setOption({
						        series: [{
						            data: []
						        }]
							});	
						}
					} else {
						alert("发生错误！");
					}
				}
			});
		});
		$("#attendanceChange").click(function() {
			var param = {
				date: $("#attendanceDate").val(),
				partmentid: $("#partmentId").text()
			}
			console.log('团队当天出勤统计参数：' + JSON.stringify(param));
			$.ajax({
				type: "post",
				url: qlinkerservice + "searchGroupAttendanceCountRecord",
				dataType: "json",
				data: {
					json: JSON.stringify(param)
				},
				success: function(data) {
					console.log('团队当天出勤统计：' + JSON.stringify(data));
					if (data.ret == 'success') {
						if (data.item.length > 0) {
							$('#realCount').text(data.item[0].realCount);
							$('#requiredCount').text(data.item[0].requiredCount);
							$('#averageDept').text(data.item[0].averageDept);
							team_attend_column_chart.setOption({
						        series: [{
						            data: [data.item[0].normalAttendance, data.item[0].fieldAttendance, data.item[0].leaveEarly, data.item[0].lateAttendance, data.item[0].absenseAttendance, data.item[0].absenseAttendance, data.item[0].noArrange, data.item[0].restCount, data.item[0].bussiness]
						        }]
							});
						} else {
							$('#realCount').text('');
							$('#requiredCount').text('');
							$('#averageDept').text('');
							team_attend_column_chart.setOption({
						        series: [{
						            data: []
						        }]
							});
						}
					} else {
						alert("发生错误！");
					}
				}
			});
		});
	}
});
