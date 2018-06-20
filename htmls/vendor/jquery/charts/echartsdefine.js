$(function() {
	$.fn.deskteamconut = function(options) {
		var x, drag = this,
			defaults = {};
		var settings = $.extend(defaults, options);

		// console.log(JSON.stringify(settings));

		var team_column_chart = echarts.init(document.getElementById('team_column'));
		var team_column_option = {
			color: ['#ff7e00'],
			tooltip: {},
			grid: {
				x: 5,
				x2: 5,
				y: 20,
				y2:20
			},
			xAxis: {
				type: 'category',
				axisLine: {
					show: false,
					onZero: true,
				},
				data: [{
					value: '正常',
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
					value: '早退',
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
					barGap: '1%',
				}
			},
			yAxis: {
				show: false,
				min: 0,
				max: 22,
			},
			series: [{
				name: '团队今日出勤统计',
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
				data: [settings.normalAttendance, settings.lateAttendance, settings.leaveEarly],
				itemStyle: {
					normal: {
						color: function(params) {
							var colorList = ['#47bd3e', '#ffb400', '#ff6600'];
							return colorList[params.dataIndex]
						},
						barBorderRadius: 4,
					}
				}
			}],
		};
		team_column_chart.setOption(team_column_option);
	}

	$.fn.deskpersonconut = function(options) {
		var x, drag = this,
			defaults = {};
		var settings = $.extend(defaults, options);

		// console.log(JSON.stringify(options));

		var attend_column_chart = echarts.init(document.getElementById('attend_column'));
		var attend_column_option = {
			color: ['#ff7e00'],
			tooltip: {},
			grid: {
				x: 5,
				x2: 5,
				y: 20,
				y2:20
			},
			xAxis: {
				type: 'category',
				axisLine: {
					show: false,
					onZero: true,
				},
				data: [
				{
					value: '应出勤天数',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				},
				{
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
					value: '异常打卡',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				},
				/*{
					value: '请假',
					textStyle: {
						fontSize: 12,
						color: '#aeb2b7'
					}
				}*/
				],
				axisTick: {
					alignWithLabel: true,
					show: false,
				},
				series: {
					barGap: '1%',
				}
			},
			yAxis: {
				show: false,
				min: 0,
				max: 22,
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
			
//				data: [settings.requiredAttend,settings.attendanceDays, settings.normalPunchCard, settings.exceptionTotal, settings.leaveTotal],
				data: [settings.requiredAttend,settings.attendanceDays, settings.normalPunchCard, settings.exceptionTotal],
				itemStyle: {
					normal: {
						color: function(params) {
//							var colorList = ['#9900d9','#00a0e9', '#00a814', '#e60012', '#f39800'];
							var colorList = ['#8957a1','#3399f0', '#0061e5', '#f00'];
							return colorList[params.dataIndex]
						},
						barBorderRadius: 4,
					}
				}
			}],
		};
		attend_column_chart.setOption(attend_column_option);
	}

	$.fn.deskteamrate = function(options) {
		var x, drag = this,
			defaults = {};
		var settings = $.extend(defaults, options);

		// console.log(JSON.stringify(settings));

		for (var i = 0;i < settings.sex_group_info.length;i ++) {
			if (settings.sex_group_info[i].Sex == '1') {
				var rate_male = settings.sex_group_info[i].rate;
			}
		}
		for (var i = 0;i < settings.years_group_sql.length;i ++) {
			if (settings.years_group_sql[i].years == '90') {
				var rate_90 = settings.years_group_sql[i].rate;
			} else if (settings.years_group_sql[i].years == '80') {
				var rate_80 = settings.years_group_sql[i].rate;
			}
		}

		$('#sex_circle').circleProgress({
			value: rate_male,
			size: 81,
			thickness: 5,
			fill: {
				gradient: ["#957dff"]
			}
		});

		$('#year90_circle').circleProgress({
			value: rate_90,
			size: 81,
			thickness: 5,
			fill: {
				gradient: ["#30afff"]
			}
		});

		$('#year80_circle').circleProgress({
			value: rate_80,
			size: 81,
			thickness: 5,
			fill: {
				gradient: ["#ff6600"]
			}
		});

	}
});

/*薪酬统计水平柱状图*/
$(function() {		
	var hr_column_chart = echarts.init(document.getElementById('depart-salary-column'));
	var hr_column_option = {
		color: ['#ff7e00'],
		tooltip: {},
		//去四周空白
		grid: {
			x: 5,
			x2: 5,
			y: 5,
			y2:20
		},
		yAxis: {
			type: 'category',
			axisTick: {
				alignWithLabel: true,
				show: false
			},
			axisLabel: false,
			axisLine: {
				lineStyle: {
					color: '#dcdcdc',
					show: false
				}
			}
		},
		xAxis: {
			min: 0, //设置最小刻度值为0
			max: 15000, //设置最小刻度值为15000
			splitNumber: 3, //设置坐标轴的分割段数
			axisTick: {
				alignWithLabel: true,
				show: false
			},
			axisLabel: {
				interval: 0
			},
			splitLine: false, //是否显示网格线
			axisLine: {
				lineStyle: {
					color: '#dcdcdc', //这里是为了突出显示加上的，可以去掉
					show: false
				}
			},
		},
		series: [{
			type: 'bar',
			barWidth: '26',
			label: {
				normal: {
					show: true,
					position: 'top',
					textStyle: {
						color: '#242424',
						fontSize: 18
					}
				}
			},
			barGap: '-100%',
			data: [15000],
			itemStyle: {
				normal: {
					color: '#eeeeee'
				}
			}
		}, {
			name: '部门平均薪酬',
			type: 'bar',
			barWidth: '26',
			z: 10,
			data: [6050],
			itemStyle: {
				normal: {
					color: '#0068b7'
				}
			}
		}]
	};
	hr_column_chart.setOption(hr_column_option);
});