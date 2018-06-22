/*人力资源工资报表水平柱状图*/
$(function() {		
		var hr_column_chart = echarts.init(document.getElementById('hr-salary-column'));
		var hr_column_option = {
			color: ['#ff7e00'],
			tooltip: {},
			//去四周空白
			grid: {
				x: 5,
				x2: 20,
				y: 7,
				height: 110
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