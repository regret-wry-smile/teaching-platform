$(function() {
	$.fn.peoplenormal = function(options) {
		var x, drag = this,
			defaults = {};
		var settings = $.extend(defaults, options);

		var monthData = [], staffData = [], entryData = [], leaveData = [];

		console.log('异动统计报表参数：' + JSON.stringify(settings));
		if (settings.partment_uuid) {
			$.ajax({
				type: "post",
				url: qlinkerservice + "user/report_hr_mouth_info",
				async: false,
				dataType: "json",
				data: {
					partmentid: settings.partment_uuid
				},
				success: function(data) {
					console.log('异动统计报表：' + JSON.stringify(data));
					if (data.ret == 'success') {
						if (data.item.data.length > 0) {
							for (var i = 0; i < data.item.data.length; i++) {
								monthData.push(data.item.data[i].mouth);
								staffData.push(data.item.data[i].staff_no);
								entryData.push(data.item.data[i].entry_no_direct);
								leaveData.push(data.item.data[i].leave_no_direct);
							}
						}
					} else {
						alert("发生错误！");
					}
				}
			});
		}

		var mul_line_echart = echarts.init(document.getElementById('mul_line_con'));
		var mul_line_option = {
			title: {
				text: '人数/月',
				textStyle: {
					color: '#aeb2b7',
					fontSize: 14,
					fontWeight :{
						lighter:100
					}
				}
			},
			tooltip: {
				trigger: 'axis'
			},
			legend: {
				data: ['入职', '离职', '在职人数'],
				textStyle :{
					color:'#aeb2b7'
				}
			},
			grid: {
                x:50,
				x2: 50,
				y: 50,
				height: 90
			},
			toolbox: {
				show: true,
				//默认值为true，是否在鼠标 hover 的时候显示每个工具 icon 的标题。
				showTitle:false,
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
                        //type: ['line', 'bar', 'stack', 'tiled']
                        //这里是让echart图只显示折线和柱状两种样式
                        type: ['line', 'bar']
					},
					restore: {
						show: true
					}
				}
			},
			calculable: true,
			xAxis: [{
				type: 'category',
				splitLine: false, //是否显示网格线
				boundaryGap: false,
				data: monthData,
				axisTick: {
					alignWithLabel: true,
					show: false, //设置x轴刻度是否显示
				},
				axisLine:{
                lineStyle:{
                    color:'#aeb2b7',//这里是为了突出显示加上的，可以去掉
                    show:false
                }
            },
			}],
			yAxis: [{
				type: 'value',
				splitLine: false, //是否显示网格线
				min: 0,
				splitNumber: 2, //设置坐标轴的分割段数
				axisTick: {
					alignWithLabel: true,
					show: false, //设置x轴刻度是否显示
				},
				axisLine:{
                lineStyle:{
                    color:'#aeb2b7',
                    show:false,
                    width:2
                }
               }
				
			}],
			series: [{
				name: '在职人数',
				type: 'line',
				smooth: true,
				itemStyle: {
					normal: {
						color: '#CCC0E8',
						areaStyle: {
							type: 'default',
							color: '#D7CDEB'
						},
						lineStyle: {
							color: '#CCC0E8'
						}
					}
				},
				data: staffData
			}, {
				name: '离职',
				type: 'line',
				smooth: true,
				itemStyle: {
					normal: {
						color: '#5AB1EF',
						areaStyle: {
							type: 'default',
							color: '#A6D1F0'
						},
						lineStyle: {
							color: '#5AB1EF'
						}
					}
				},
				data: leaveData
			}, {
				name: '入职',
				type: 'line',
				smooth: true,
				itemStyle: {
					normal: {
						color: '#2EC7C9',
						areaStyle: {
							type: 'default',
							color: '#8EDADB'
						},
						lineStyle: {
							color: '#2EC7C9'
						}
					}
				},
				data: entryData
			}]
		};

		mul_line_echart.setOption(mul_line_option);

		$("#dateChange").click(function() {
			var param = {
				partmentid: $("#partmentId").text()
			}
			console.log('异动统计报表参数：' + JSON.stringify(param));
			$.ajax({
				type: "post",
				url: qlinkerservice + "user/report_hr_mouth_info",
				async: false,
				dataType: "json",
				data: param,
				success: function(data) {
					console.log('异动统计报表：' + JSON.stringify(data));
					if (data.ret == 'success') {
						monthData = [], staffData = [], entryData = [], leaveData = [];
						if (data.item.data.length > 0) {
							for (var i = 0; i < data.item.data.length; i++) {
								monthData.push(data.item.data[i].mouth);
								staffData.push(data.item.data[i].staff_no);
								entryData.push(data.item.data[i].entry_no_direct);
								leaveData.push(data.item.data[i].leave_no_direct);
							}
							mul_line_echart.setOption({
						        xAxis: [{
						            data: monthData
						        }],
						        series: [{
						            data: staffData
						        },{
						        	data: leaveData
						        },{
						        	data: entryData
						        }]
							});
						} else {
							mul_line_echart.setOption({
						        xAxis: [{
						            data: []
						        }],
						        series: [{
						            data: []
						        },{
						        	data: []
						        },{
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