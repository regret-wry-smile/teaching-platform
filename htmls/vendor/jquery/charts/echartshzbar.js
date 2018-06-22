/*x薪酬统计水平柱状图*/
	$(function() {
		var hr_column_chart = echarts.init(document.getElementById('data_integrity_con'));
		var hr_column_option = {
			color: ['#ff7e00'],
			tooltip: {},
			yAxis: {
				show: false,
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
				show: false,
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
				name: '资料完整度',
				type: 'bar',
				barWidth: '18',
				label: {
					normal: {
						show: false,
					}
				},
				barGap: '-100%',
				data: [100],
				itemStyle: {
					normal: {
						color: '#eeeeee'
					},
					barBorderRadius: 4 // 统一设置四个角的圆角大小
				}
			}, {
				name: '资料完整度',
				type: 'bar',
				barWidth: '18',
				z: 10,
				data: [70],
				itemStyle: {
					normal: {
						color: '#00a814',
					}
				},
			}]
		};
		hr_column_chart.setOption(hr_column_option);
	});