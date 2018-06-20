app.directive('smartZoom', function() {
	return {
		restrict: 'A',
		scope: {

		},

		link: function(scope, element, attrs) {
			$('#imageFullScreen').smartZoom({
				'containerClass': 'zoomableContainer',
				'maxScale': 5,
				'adjustOnResize': true
			});

			$('#topPositionMap,#leftPositionMap,#rightPositionMap,#bottomPositionMap').bind("click", moveButtonClickHandler);
			$('#zoomInButton,#zoomOutButton').bind("click", zoomButtonClickHandler);
			$('.dot').hover(function(event) {
				var point = getMousePos(event);
				$('#tip').css({
					'top': point.y,
					'left': point.x
				});
				console.log(getMousePos(event));
				$('#tip').show();
			}, function() {
				$('#tip').hide();
			})

			function zoomButtonClickHandler(e) {
				var scaleToAdd = 0.8;
				if(e.target.id == 'zoomOutButton')
					scaleToAdd = -scaleToAdd;
				$('#imageFullScreen').smartZoom('zoom', scaleToAdd);
			}

			function moveButtonClickHandler(e) {
				var pixelsToMoveOnX = 0;
				var pixelsToMoveOnY = 0;

				switch(e.target.id) {
					case "leftPositionMap":
						pixelsToMoveOnX = 50;
						break;
					case "rightPositionMap":
						pixelsToMoveOnX = -50;
						break;
					case "topPositionMap":
						pixelsToMoveOnY = 50;
						break;
					case "bottomPositionMap":
						pixelsToMoveOnY = -50;
						break;
				}
				$('#imageFullScreen').smartZoom('pan', pixelsToMoveOnX, pixelsToMoveOnY);
			}

			function getMousePos(event) {
				var e = event || window.event;
				var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
				var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
				var x = e.pageX || e.clientX + scrollX;
				var y = e.pageY || e.clientY + scrollY;
				//alert('x: ' + x + '\ny: ' + y);
				return {
					'x': x,
					'y': y
				};
			}

			function drag() {
				var obj = $('.dot');
				obj.bind('mousedown', start);
				var scale = 1;

				function start(e) {
					/*var ol = obj.offset().left;*/
					var ol = obj.css('left').replace('px', '');
					console.log('实际' + ol);
					/*var ot = obj.offset().top;*/
					var ot = obj.css('top').replace('px', '');
					var transformMatrix = $('#imageFullScreen').css('transform');
					if(transformMatrix && transformMatrix != "" && transformMatrix.search('matrix') != -1) { // get the target css matrix tranform 

						var arrValues;
						if(transformMatrix.search('matrix3d') != -1) { // check the matrix type
							arrValues = transformMatrix.replace('matrix3d(', '').replace(')', '').split(',');
							scale = arrValues[0]; // get target current scale
						} else {
							arrValues = transformMatrix.replace('matrix(', '').replace(')', '').split(',');
							scale = arrValues[3]; // get target current scale

						}
						console.log('放大倍数' + scale);
					}
					deltaX = e.pageX / scale - ol;
					deltaY = e.pageY / scale - ot;
					$(document).bind({
						'mousemove': move,
						'mouseup': stop
					});

					return false;
				}

				function move(e) {
					obj.css({
						"left": (e.pageX / scale - deltaX),
						"top": (e.pageY / scale - deltaY)
					});
					//console.log("left:" + (e.pageX - deltaX))
					return false;
				}

				function stop() {
					$(document).unbind({
						'mousemove': move,
						'mouseup': stop
					});
				}
			}
			drag();
			scope.$on('$destroy', function() {
				$('#imageFullScreen').smartZoom('destroy');
			})

		}
	}
});
/*上传附件*/
app.directive('uploadFile', function() {
	return {
		restrict: 'A',
		isshow: '=',
		templateUrl: 'js/directives/tpl/uploadfile.html',
		link: function(scope, element, attrs) {}
	}
});
/*上传文件校验*/
app.directive('validFile', function() {
	return {
		require: 'ngModel',
		link: function(scope, el, attrs, ngModel) {
			//change event is fired when file is selected
			el.bind('change', function() {
				scope.$apply(function() {
					ngModel.$setViewValue(el.val());
					ngModel.$render();
				});
			});
		}
	}
});
app.directive('infiniteScroll', [
	'$rootScope', '$window', '$timeout',
	function($rootScope, $window, $timeout) {
		return {
			link: function(scope, elem, attrs) {

				/*var gethandler=function(){
					if($window.height()>elem.height()){
						handler();
					}
				}
				gethandler();*/
				var checkWhenEnabled, handler, scrollDistance, scrollEnabled;
				console.log(attrs.infiniteScrollContent)
				if(attrs.infiniteScrollContent) {
					$window = angular.element(attrs.infiniteScrollContent);

				} else {
					$window = angular.element($window);
				}

				scrollDistance = 0;
				if(attrs.infiniteScrollDistance != null) {
					scope.$watch(attrs.infiniteScrollDistance, function(value) {
						return scrollDistance = parseInt(value, 10);
					});
				}
				scrollEnabled = true;
				checkWhenEnabled = false;
				if(attrs.infiniteScrollDisabled != null) {
					scope.$watch(attrs.infiniteScrollDisabled, function(value) {
						scrollEnabled = !value;
						if(scrollEnabled && checkWhenEnabled) {
							checkWhenEnabled = false;
							return handler();
						}
					});
				}
				handler = function() {
					var elementBottom, remaining, shouldScroll, windowBottom;
					windowBottom = $window.height() + $window.scrollTop();
					//					elementBottom = elem.offset().top + elem.height();
					elementBottom = elem.height();
					/*console.log(elem.offset().top);
					console.log('elementBottom', elementBottom);
					console.log('windowBottom', windowBottom);*/
					remaining = elementBottom - windowBottom;
					shouldScroll = remaining <= $window.height() * scrollDistance;
					if(shouldScroll && scrollEnabled) {
						if($rootScope.$$phase) {
							return scope.$eval(attrs.infiniteScroll);
						} else {
							return scope.$apply(attrs.infiniteScroll);
						}
					} else if(shouldScroll) {
						return checkWhenEnabled = true;
					}
				};
				var gethandler = function() {
					//console.log(JSON.stringify($window.height()))
					if($window.height() > elem.height()) {
						handler();
					}
				}
				gethandler();
				$window.on('scroll', handler);
				scope.$on('$destroy', function() {
					return $window.off('scroll', handler);
				});
				return $timeout((function() {
					if(attrs.infiniteScrollImmediateCheck) {
						if(scope.$eval(attrs.infiniteScrollImmediateCheck)) {
							return handler();
						}
					} else {
						return handler();
					}
				}), 0);

				/*$(window).on("resize", function(){
					alert(elem.height()-$window.height());
				});*/

			}
		};
	}
]);
app.directive('gotoTop', function() {
	return {
		restrict: 'AE',
		replace: true,
		scope: {
			minHeight: '@?'

		},
		template: '<a href="javascript:;" style="position:fixed; bottom:0;right:0;display:none">返回顶部</a>',
		link: function(scope, elem, attrs) {
			if(attrs.scrollContent) {
				$window = angular.element(attrs.scrollContent);

			} else {
				$window = angular.element('html,body');
			}

			elem.click(function() {
					/*	jQuery('html,body').animate({
							scrollTop: 0
						}, 700);*/
					$window.animate({
						scrollTop: 0
					}, 700);
				})
				.hover(function() {
					jQuery(this).addClass("hover");
				}, function() {
					jQuery(this).removeClass("hover");
				});

			scope.minHeight = scope.minHeight ? scope.minHeight : 600;
			/*jQuery(window).scroll(function() {
				var s = jQuery(window).scrollTop();
				if(s > scope.minHeight) {
					jQuery("#gotoTop").fadeIn(100);
				} else {
					jQuery("#gotoTop").fadeOut(200);
				};
			});*/

			$window.scroll(function() {
				var s = $window.scrollTop();

				if(s > scope.minHeight) {
					jQuery("#gotoTop").fadeIn(100);
				} else {
					jQuery("#gotoTop").fadeOut(200);
				};
			});
		}
	};
});
app.directive('daterangepicker', function(JQ_CONFIG, uiLoad) {
	return {
		scope: {
			formate: '@',
			starTime: '=',
			endTime: '=',
			isNorange: '=?',
			minDate: '=?',
			maxDate: '=?'

		},
		restrict: 'AE',
		require: "ngModel",
		link: function(scope, elem, attrs, ngModelCtr) {
			var $me = angular.element(elem);

			//alert(attrs.ngModel);
			var setting = {
				format: 'YYYY/MM/DD',
				/*startDate: moment().subtract('days', 29),
				endDate: moment(),*/

			}

			var ranges = {
					'ranges': {

						'今天': [moment(), moment()],

						'明天': [moment().subtract('days', 1), moment().subtract('days', 1)],

						'最近7天': [moment().subtract('days', 6), moment()],

						'最近30天': [moment().subtract('days', 29), moment()],

						'本月': [moment().startOf('month'), moment().endOf('month')],

						'上月': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]

					}
				}
				/*	timePickerSpecial: false,上午下午
							timePicker: true,
							timePickerIncrement: 5,
							timePicker12Hour: false,日历加时间的时候设置*/
				/*singleDatePicker: true,//使用单个日历*/

			var param = {
				startDate: scope.starTime || moment().subtract('days', 29),
				endDate: moment()
			}
			var option = angular.extend({}, setting, param);
			if(!scope.isNorange) {
				option = angular.extend({}, option, ranges);

			}
			uiLoad.load(JQ_CONFIG['daterangepicker']).then(function() {
				$me.daterangepicker(option, function(start, end, label) {
					scope.starTime = start.format(scope.formate);
					scope.endTime = end.format(scope.formate);
					$scope.$apply();
					//console.log(start.toISOString(), end.toISOString(), label);

				});

			}).catch(function() {

			});
			
			scope.$on('$destroy', function() {
				$me.data('daterangepicker').remove();
			});

			scope.$watch('minDate+maxDate', function(newvalue, oldvalue) {
				if(newvalue != oldvalue) {
					option = angular.extend({}, option, {
						'minDate': scope.minDate,
						'maxDate': scope.maxDate
					});
					/*if(new Date(scope.maxDate).getTime()>)*/
					if(moment(option.minDate).isAfter(scope.startDate)) {
						option = angular.extend({}, option, {
							'startDate': scope.minDate
						});
					}
					if(moment(option.maxDate).isBefore(scope.startDate)) {
						option = angular.extend({}, option, {
							'startDate': scope.minDate
						});
					}
					if(moment(option.maxDate).isBefore(scope.endDate)) {
						option = angular.extend({}, option, {
							'endDate': scope.maxDate
						});
					}
					if(moment(option.minDate).isAfter(scope.endDate)) {
						option = angular.extend({}, option, {
							'endDate': scope.maxDate
						});
					}
					/*return false;*/
					if($me.data('daterangepicker')) {
						$me.data('daterangepicker').setOptions(option, function(start, end, label) {
							//	alert(start.format(this.format));
							scope.starTime = start.format(scope.formate);
							scope.endTime = end.format(scope.formate);
							//console.log(start.toISOString(), end.toISOString(), label);

						});
					}
				}
			}, true);

			/*scope.$watch('starTime+endTime', function(newvalue, oldvalue) {
			if(newvalue != oldvalue){
				option = angular.extend({}, option, {
						'startDate': scope.starTime,
						'endDate': scope.endTime
					});
						alert(JSON.stringify(option))
				if($me.data('daterangepicker')) {
					$me.data('daterangepicker').setOptions(option, function(start, end, label) {
						scope.starTime = start.format(scope.formate);
						scope.endTime = end.format(scope.formate);
					});
				}}
			}, true)*/
		}
	}
});
app.directive('datetimepicker', function(JQ_CONFIG, uiLoad) {
	return {
		restrict: 'AE',
		link: function(scope, elem, attrs) {
			var $me = angular.element(elem);
			var linkOptions = {};
			if(attrs.uiOptions) {
				linkOptions = scope.$eval(attrs.uiOptions);
			}
			var opt = {
				format: 'yyyy-mm-dd',
				autoclose: true,
				forceParse: false,
				minView: 2,
				startView: 2,
				language: 'zh-CN'
			};
			var opt2 = {};
			if(attrs.timetype == 'time') {
				opt2 = {
					format: 'hh:ii',
					startView: 1,
					minView: 0,
				}
			} else if(attrs.timetype == 'seconds') {
				opt2 = {
					format: 'hh:ii:ss',
					startView: 1,
					minView: 0,
					showSecond: 1,
					minuteStep: 1,
				}
			} else if(attrs.timetype == 'month') {
				opt2 = {
					format: 'yyyy-mm',
					startView: 3, //这里就设置了默认视图为年视图
					minView: 3, //设置最小视图为年视图
				}
			} else if(attrs.timetype == 'hours') {
				opt2 = {
					format: 'hh:00',
					startView: 1,
					minView: 1,
				}
			}

			var option = angular.extend({}, opt, opt2);
			uiLoad.load(JQ_CONFIG['datetimepicker']).then(function() {
				$me.datetimepicker(option);
			}).catch(function() {

			});
		}
	}
});
/*监测数据--概况下的具体类别-接口*/
app.directive('showDetails', function() {
	return {
		restrict: 'AE',
		scope: {
			tempUrl: '@',
			options: '='
		},
		controller: function($scope, $rootScope, $http, $modal, toastr, $state, monitorDataService, projectService, reportService) {
			$rootScope.projectId = localStorage.getItem("projectId"); /*取出本地存储的项目id*/
			//var opt = $scope.options;
			//基坑下拉框的选项
			var _foundationPit = function() {
				var projectInfoId = $rootScope.projectId;
				var param = {
					"projectInfoId": projectInfoId
				};
				projectService.searchFoundationPitInfo({
					'json': JSON.stringify(param)
				}).then(function(response) {
					$scope.dict1s = response.item;
					$scope.dict1 = $scope.dict1s[0];
					if($scope.options) {
						$scope.foundationPitIds = $scope.options.foundationPitId;
						//console.log("查询基坑概述："+JSON.stringify($scope.dict1s));
						_periodSelect();
					}
				});
			}
			_foundationPit();

			//期数下拉框的选项
			var _periodSelect = function() {
				var param = {
					foundationPitId: $scope.foundationPitIds
				}
				reportService.searchPeriod({
					'json': JSON.stringify(param)
				}).then(function(response) {
					$scope.periods = response.item;
					if($scope.options) {
						$scope.selperiod = $scope.options.periods;
					}
					$rootScope.searchData();

				});
			};
			/*根据基坑切换期数*/
			$scope.changeFoundations = function(foundationPitId) {
				$scope.foundationPitIds = foundationPitId;
				_periodSelect();

			}
			$scope.changePeriods = function(period) {
				$scope.selperiod = period;
				$scope.options.periods = $scope.selperiod;
				$rootScope.searchData();
			}
			$scope.monitorProjectInfo = [];
			$scope.monitorManager = '';
			$scope.monitorProjectInfoObj = [];
			$scope.monitorProjectInfo = [];
			$scope.monitorUserId = '';
			$scope.calculateUserId = '';
			$scope.reviewUserId = '';
			//查询人员
			var _searchUserInfo = function() {
					var objectparam = {};
					var param = {
						json: JSON.stringify(objectparam)
					}
					projectService.searchUserInfo(param).then(function(response) {
						if(response.ret == 'success') {
							$scope.personList = response.item;
							/*if($scope.monitorManager.monitorUserId){
								$scope.surveyorUser=$scope.monitorManager.monitorUserId;
								$scope.calculateUser=$scope.monitorManager.calculateUserId;
								$scope.reviewUser=$scope.monitorManager.reviewUserId;									
							}*/

							for(var i = 0; i < $scope.personList.length; i++) {
								for(var j = 0; j < $scope.personList[i].list.length; j++) {
									if($scope.monitorUserId == $scope.personList[i].list[j].id) {
										$scope.surveyorUser = angular.copy(JSON.stringify($scope.personList[i].list[j]));
									}
									if($scope.calculateUserId == $scope.personList[i].list[j].id) {
										$scope.calculateUser = angular.copy(JSON.stringify($scope.personList[i].list[j]));
									}
									if($scope.reviewUserId == $scope.personList[i].list[j].id) {
										$scope.reviewUser = angular.copy(JSON.stringify($scope.personList[i].list[j]));
									}
									if($scope.patrolUserId == $scope.personList[i].list[j].id) {
										$scope.patrolUserUser = angular.copy(JSON.stringify($scope.personList[i].list[j]));
									}
								}

							}
						}
					})
				}
				//_searchUserInfo();
				//查询概况人员监测员、计算人员、复核人员
			$scope.value = '';
			var _searchUserInfo1 = function() {
					$scope.roleUserList = [];
					var item = {
						roleTypeCode: $scope.value
					}
					$scope.roleUserList.push(item)
					var objectparam = {
						data: $scope.roleUserList
					}
					var param = {
						json: JSON.stringify(objectparam)
					}
					monitorDataService.searchUserByRoleTypeCode(param).then(function(response) {
						if(response.ret == 'success') {
							$scope.personList = response.item;
						}
					})
				}
				//查询监测人员
			var _getmonitorUser = function() {
					$scope.value = 'monitor';
					_searchUserInfo1();
				}
				//查询计算人员
			var _getcalculateUser = function() {
					$scope.value = 'calculate';
					_searchUserInfo1();
				}
				//查询复核人员
			var _getreviewUser = function() {
				$scope.value = 'review';
				_searchUserInfo1();
			}
			var _init = function() {
				_getmonitorUser();
				_getcalculateUser();
				_getreviewUser();
			}();
			var _searchData = function() {
					var param = {
						/*foundationPitId: opt.foundationPitId,
						periods: opt.periods,*/
						foundationPitId: $scope.foundationPitIds,
						periods: $scope.selperiod,
						monitorProjectId: $scope.options.monitorProjectId
					}
					switch($scope.options.type) {
						case 'AxialType':
							{ //轴力类
								monitorDataService.searchAxialForceData({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorProjectInfo = data.item;
									}
								});
								//monitorDataService.searchAxialForceDataManager({
								monitorDataService.searchAllTypeDataManager({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorManager = data.item;
										$scope.monitorManager.surveyorUserUrl = $scope.monitorManager.surveyorUserUrl;
										$scope.monitorManager.calculateUserUrl = $scope.monitorManager.calculateUserUrl;
										$scope.monitorManager.reviewUserUrl = $scope.monitorManager.reviewUserUrl;
										$scope.monitorUserId = $scope.monitorManager.monitorUserId;
										$scope.calculateUserId = $scope.monitorManager.calculateUserId;
										$scope.reviewUserId = $scope.monitorManager.reviewUserId;
										$scope.patrolUserId = $scope.monitorManager.patrolUserId;
										$scope.patrolUserUrl = $scope.monitorManager.patrolElecronicSignature;
										_searchUserInfo1();
									}
								});
								break;
							}
						case 'DisplacementType':
							{ //位移类
								monitorDataService.searchDisplacementData({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorProjectInfoObj = data.item;
										$scope.monitorProjectInfo = $scope.monitorProjectInfoObj.data; /*位移类的数据--数组*/
										$scope.monitorProjectInfoType = $scope.monitorProjectInfoObj.type;
										/*根据返回的数据type  判断显示角度法盘坐盘右   还是位移法x y*/
										if($scope.monitorProjectInfoType == 'x' || $scope.monitorProjectInfoType == 'y') {
											$scope.horizontalState = true;
											$scope.angleState = false;
										}
										if($scope.monitorProjectInfoType == 'angle') {
											$scope.angleState = true;
											$scope.horizontalState = false;
										}
									}
								})
								//monitorDataService.searchDisplacementDataManager({
								monitorDataService.searchAllTypeDataManager({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorManager = data.item;
										$scope.monitorManager.surveyorUserUrl = $scope.monitorManager.surveyorUserUrl;
										$scope.monitorManager.calculateUserUrl = $scope.monitorManager.calculateUserUrl;
										$scope.monitorManager.reviewUserUrl = $scope.monitorManager.reviewUserUrl;
										$scope.monitorUserId = $scope.monitorManager.monitorUserId;
										$scope.calculateUserId = $scope.monitorManager.calculateUserId;
										$scope.reviewUserId = $scope.monitorManager.reviewUserId;
										$scope.patrolUserId = $scope.monitorManager.patrolUserId;
										$scope.patrolUserUrl = $scope.monitorManager.patrolElecronicSignature;
										_searchUserInfo1();
									}
								});
								break;
							}
						case 'InclinometerType':
							{ //测斜类
								monitorDataService.searchInclinometerData({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorProjectInfo = data.item;
									}
								})
								//monitorDataService.searchInclinometerDataManager({
								monitorDataService.searchAllTypeDataManager({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorManager = data.item;
										$scope.monitorManager.surveyorUserUrl = $scope.monitorManager.surveyorUserUrl;
										$scope.monitorManager.calculateUserUrl = $scope.monitorManager.calculateUserUrl;
										$scope.monitorManager.reviewUserUrl = $scope.monitorManager.reviewUserUrl;
										$scope.monitorUserId = $scope.monitorManager.monitorUserId;
										$scope.calculateUserId = $scope.monitorManager.calculateUserId;
										$scope.reviewUserId = $scope.monitorManager.reviewUserId;
										$scope.patrolUserId = $scope.monitorManager.patrolUserId;
										$scope.patrolUserUrl = $scope.monitorManager.patrolElecronicSignature;
										_searchUserInfo1();
									}
								});
								break;
							}
						case 'SettlementType':
							{ //沉降类 
								monitorDataService.searchSettlementData({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorProjectInfo = data.item;
									}
								})
								//monitorDataService.searchSettlementDataManager({
								monitorDataService.searchAllTypeDataManager({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorManager = data.item;
										$scope.monitorManager.surveyorUserUrl = $scope.monitorManager.surveyorUserUrl;
										$scope.monitorManager.calculateUserUrl = $scope.monitorManager.calculateUserUrl;
										$scope.monitorManager.reviewUserUrl = $scope.monitorManager.reviewUserUrl;
										$scope.monitorUserId = $scope.monitorManager.monitorUserId;
										$scope.calculateUserId = $scope.monitorManager.calculateUserId;
										$scope.reviewUserId = $scope.monitorManager.reviewUserId;
										$scope.patrolUserId = $scope.monitorManager.patrolUserId;
										$scope.patrolUserUrl = $scope.monitorManager.patrolElecronicSignature;
										_searchUserInfo1();
									}
								});
								break;
							}
						case 'WaterType':
							{ //水位类 
								monitorDataService.searchWaterLevelData({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorProjectInfo = data.item;
									}
								})
								//monitorDataService.searchWaterLevelDataManager({
								monitorDataService.searchAllTypeDataManager({
									'json': JSON.stringify(param)
								}).then(function(data) {
									if(data.ret == 'success') {
										$scope.monitorManager = data.item;
										$scope.monitorManager.surveyorUserUrl = $scope.monitorManager.surveyorUserUrl;
										$scope.monitorManager.calculateUserUrl = $scope.monitorManager.calculateUserUrl;
										$scope.monitorManager.reviewUserUrl = $scope.monitorManager.reviewUserUrl;
										$scope.monitorUserId = $scope.monitorManager.monitorUserId;
										$scope.calculateUserId = $scope.monitorManager.calculateUserId;
										$scope.reviewUserId = $scope.monitorManager.reviewUserId;
										$scope.patrolUserId = $scope.monitorManager.patrolUserId;
										$scope.patrolUserUrl = $scope.monitorManager.patrolElecronicSignature;
										_searchUserInfo1();
									}
								});
								break;
							}
						case 'PatrolType':
							{ //巡视检查
								$state.go('index.monitorData.inspect', {
									foundationPitId: $scope.foundationPitId,
									period: $scope.period,
									monitorProjectId: $scope.options.monitorProjectId
								});
								break;
							}
					}
				}
				/*_searchData();*/
			$rootScope.searchData = function() {
					/*$scope.monitorProjectInfo = [];
					$scope.monitorManager = '';
					$scope.monitorProjectInfoObj = [];
					$scope.monitorProjectInfo = [];*/
					_searchData();
				}
				/*监听期数改变*/
			$scope.$watch('options', function(newvalue, oldvalue) {
				//var opt = $scope.options;
				if($scope.options.periods && $scope.options.periods != oldvalue) {
					$scope.selperiod = $scope.options.periods;
					$scope.foundationPitIds = $scope.options.foundationPitId;
					$rootScope.searchData();
				}

			}, true)

			$scope.personList = []; //人员列表				
			var date = moment().format('YYYY-MM-DD'); //当前日期
			var time = moment().format('HH:mm:ss'); //当前时间
			$scope.changecalculate = function(calculateUser) {
				if(calculateUser) {
					var jsonparam = {
						calculateUserId: JSON.parse(calculateUser).id,
						foundationPitId: $scope.foundationPitIds,
						date: date,
						periods: $scope.selperiod,
						time: time,
						monitorProjectId: $scope.options.monitorProjectId //监测项目id
					}
					var param = {
						json: JSON.stringify(jsonparam)
					}
					monitorDataService.updateDataManager(param).then(function(response) {
						if(response.ret == 'success') {
							$rootScope.searchData();
							$scope.monitorManager.calculateUserUrl = JSON.parse(calculateUser).imageUrl;
							$scope.calculateelectronicSignatureUrl = JSON.parse(calculateUser).electronicSignatureUrl;
						}
					})
				}
			}
			$scope.changereview = function(reviewUser) {
				if(reviewUser) {
					var jsonparam = {
						reviewUserId: JSON.parse(reviewUser).id,
						foundationPitId: $scope.foundationPitIds,
						date: date,
						periods: $scope.selperiod,
						time: time,
						monitorProjectId: $scope.options.monitorProjectId //监测项目id
					}
					var param = {
						json: JSON.stringify(jsonparam)
					}
					monitorDataService.updateDataManager(param).then(function(response) {
						if(response.ret == 'success') {
							$rootScope.searchData();
							$scope.monitorManager.reviewUserUrl = JSON.parse(reviewUser).imageUrl;
							$scope.reviewelectronicSignatureUrl = JSON.parse(reviewUser).electronicSignatureUrl;
						}
					})
				}
			}
			$scope.changesurvey = function(surveyorUser) {
				if(surveyorUser) {
					var jsonparam = {
						monitorUserId: JSON.parse(surveyorUser).id,
						foundationPitId: $scope.foundationPitIds,
						date: date,
						periods: $scope.selperiod,
						time: time,
						monitorProjectId: $scope.options.monitorProjectId //监测项目id
					}
					var param = {
						json: JSON.stringify(jsonparam)
					}
					monitorDataService.updateDataManager(param).then(function(response) {
						if(response.ret == 'success') {
							$scope.monitorManager.surveyorUserUrl = JSON.parse(surveyorUser).imageUrl;
							$scope.surveyelectronicSignatureUrl = JSON.parse(surveyorUser).electronicSignatureUrl;
							$rootScope.searchData();

						}
					})
				}
			}
			$scope.exportTemplates = function() {
					var exportparam = {
						foundationPitId: $scope.foundationPitIds,
						periods: $scope.selperiod,
					}
					var param = {
						json: JSON.stringify(exportparam)
					}
					reportService.exportDataToTemplate(param).then(function(response) {
						if(response.ret == 'success') {
							//成功之后重新载入模板页面（备注:这里是详情页面，按小类型查看）
							if($scope.options.type) {
								switch($scope.options.type) {
									case 'AxialType':
										{ //轴力类
											$scope.temp = 'tpl/monitorDataSurveyAxial.html';
											break;
										}
									case 'DisplacementType':
										{ //位移类
											$scope.temp = 'tpl/monitorDataSurveyDisplacement.html';
											break;
										}
									case 'InclinometerType':
										{ //测斜类
											$scope.temp = 'tpl/monitorDataSurveyInclinometer.html';
											break;
										}
									case 'SettlementType':
										{ //沉降类 
											$scope.temp = 'tpl/monitorDataSurveySettlement.html';
											break;
										}
									case 'WaterType':
										{ //水位类
											$scope.temp = 'tpl/monitorDataSurveySettlement.html';
											break;
										}
									case 'PatrolType':
										{ //巡视检查
											$state.go('index.monitorData.inspect', {
												foundationPitId: $scope.foundationPitId,
												period: $scope.period
											});
											break;
										}
									case 'monitorAnalysis':
										{ //监测分析
											$state.go('index.monitorData.analyse', {
												foundationPitId: $scope.foundationPitId,
												period: $scope.period
											});
											break;
										}
								}
								$scope.isShowdetails = true;
							}
						}
					})
				}
				/*新增测点编号信息*/
			$scope.add = function(monitorProject) {
					var modalInstance = $modal.open({
						templateUrl: 'tpl/monitorDataEditPoint.html',
						controller: 'monitorDataAddPointCtr', // 在publicCtr里面
						size: '400',
						backdrop: false,
						resolve: {
							items: function() {
								return monitorProject; //向模态框控制器中传值  
							}
						}
					});
					modalInstance.result.then(function(result) {
						if(result == 'success') {
							_searchData();
						}

					}, function() {

					});
				}
				/*编辑测点编号信息*/
			$scope.edit = function(monitorProject) {
					var modalInstance = $modal.open({
						templateUrl: 'tpl/monitorDataEditPoint.html',
						controller: 'monitorDataEditPointCtr', // 在publicCtr里面
						size: '400',
						backdrop: false,
						resolve: {
							items: function() {
								return monitorProject; //向模态框控制器中传值  
							}
						}
					});
					modalInstance.result.then(function(result) {
						if(result == 'success') {
							_searchData();
						}

					}, function() {

					});
				}
				/*删除测点编号信息*/
			$scope.delete = function(pointId) {
				var param = {
					id: pointId
				}
				monitorDataService.deletePoint({
					'json': JSON.stringify(param)
				}).then(function(data) {
					if(data.ret == 'success') {
						_searchData();
					}
				})
			}
		},
		link: function(scope, elem, attrs) {

		},
		template: "<div class=\"skipcss\" style=\"position: relative;\"><div class=\"searcher clearfix\" style=\"position: absolute;top: -48px;\">\n\t\t<select ng-model=\"foundationPitIds\" ng-options=\"dict1.id as dict1.name for dict1 in dict1s\" ng-change=\"changeFoundations(foundationPitIds)\"><option value=\"\" disabled=\"true\">\u8BF7\u9009\u62E9\u57FA\u5751</option></select>\n\t\t<select ng-model=\"selperiod\" ng-options=\"'\u7B2C'+period+'\u671F' for period in periods\" ng-change=\"changePeriods(selperiod)\"><option value=\"\" disabled=\"true\">\u8BF7\u9009\u62E9\u671F\u6570</option></select>\n\t\t<span class=\"count ml75 mr20 pointer pull-right\" style=\"display: inline-block;width: 130px;height: 26px;border-radius: 10px;background: #0078ff;color: #fff;text-align: center;line-height: 26px;\" ng-if=\"isShowdetails&&(submitAuditbtnlimit=='submitAudit')\" ng-click=\"exportTemplates()\">\u751F\u6210PDF</span>\t\t\n\t\t</div><ng-include src=\"tempUrl\"></ng-include></div>"
	}
});
/*无数据时列表显示样式*/
app.directive('noData', function($templateCache, $compile, $http, $sce, $templateRequest) {
	return {
		scope: {
			isShow: "=",
			getData: '=',
			isHide: "&"
		}, // use a new isolated scope
		restrict: 'AE',
		/*replace: 'true',*/
		/*transclude: true,*/
		/*controller:'ResetPwdCtrl',*/
		templateUrl: 'tpl/nodata.html',

		link: function(scope, el, attrs) {
			scope.isShow = false;
			//alert(JSON.stringify(scope.isShow))
			if(scope.getData == 0) {
				scope.isShow = true;
			} else if(scope.getData > 0) {
				scope.isShow = false;
				$('.nodata').remove();
			}
		}
	}
});
app.directive('selectMultiple', function($rootScope) {
	return {
		scope: {
			myselList: "=",
			myList: "=",
			mySelect: "="
		}, // use a new isolated scope
		restrict: 'AE',
		require: "ngModel",
		link: function(scope, element, attrs, ngModel) {
			scope.myselList = [];
			//alert(JSON.stringify(scope.myList))
			var str = '';
			for(var i = 0; i < scope.myList.length; i++) {
				str += '<optgroup  label="' + scope.myList[i].type + '" >';
				alert(JSON.stringify(scope.myList))
				if(scope.myList[i].list) {
					scope.myselList = scope.myList[i].list;
					for(var j = 0; j < scope.myselList.length; j++) {
						str += '<option value="' + scope.myselList[j].id + '">' + scope.myselList[j].name + '</option>';
					}
				}

			}
			element.append(str);
			//			element.html(str);
			element.chosen();
			element.trigger("chosen:updated");
			//			element.val(scope.myselList);
			//			

		}
	}
});
app.directive('swiper', function(monitorDataService) {
	return {
		scope: {
			pagenum: "=", //两两图表线的监测项目个数
			itemlist: "=", //异常图表数组
			exceptionReportList: "=", //异常报表信息,是个对象
			config: "=", //富文本编辑器配置
			foundationPitId: "=", //选择的基坑id
			period: "=", //选择的期数,
			selstartDate: "=", //选择的开始时间
			selendDate: "=", //选择的结束时间
			listMonitorProjectIdList: "=", //监测项目id,是个数组
			updateExceptionReportData: "&", //更新异常数据方法
			companyInfo:"="

		},
		restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment  
		/*replace: true,
		transclude: true,*/
		templateUrl: 'swiper.html',
		link: function(scope, el, attrs) {
			var indexsilde = 0;
			var silderlist = 0;
			silderlist = el.find('.swiper-slide').length;
			silderlist = silderlist + scope.pagenum;
			/*更新异常数据分析*/
			var _updateExceptionReportData = function() {
				var param = { /*基坑*/
					foundationPitId: scope.foundationPitId,
					/*期数*/
					periods: scope.period,
					/*监测项目id*/
					listMonitorProjectId: scope.listMonitorProjectIdList,
					/*开始时间*/
					startDate: moment(scope.selstartDate).format('YYYY-MM-DD'),
					/*结束时间*/
					endDate: moment(scope.selendDate).format('YYYY-MM-DD'),

				}
				var option = angular.extend({}, scope.exceptionReportList, param);
				if(scope.exceptionReportList.lock == false) {
					monitorDataService.updateExceptionReport({
						'json': JSON.stringify(option)
					}).then(function(response) {
						/*alert(JSON.stringify(response));	*/
					})
				}

			}
			scope.updateExceptionReportData = function() {
				_updateExceptionReportData();
			}

			scope.next = function() {

				if(indexsilde <= (silderlist - 2)) {
					indexsilde++;
					var w = el.find('.swiper-container').width() * indexsilde;
					var es = el.find('.swiper-wrapper')[0].style;
					el.find('.swiper-container').animate({
						'scrollLeft': w
					}, 500);

					//					es.webkitTransform = es.MsTransform = es.msTransform = es.MozTransform = es.OTransform = es.transform = "translate3d(" + -w + "px,0,0)"
				}
			}
			scope.prev = function() {

				if(indexsilde >= 1) {
					indexsilde--;
					var w = el.find('.swiper-container').width() * indexsilde;
					var es = el.find('.swiper-wrapper')[0].style;
					el.find('.swiper-container').animate({
						'scrollLeft': w
					}, 500);
					//					es.webkitTransform = es.MsTransform = es.msTransform = es.MozTransform = es.OTransform = es.transform = "translate3d(" + -w + "px,0,0)"
				}
			}
			scope.$watch('pagenum', function(newvalue, oldvalue) {
				if(newvalue != oldvalue) {
					silderlist = silderlist + scope.pagenum;

				}
			}, true)

		}
	}

});

//监测结果点的折线图
app.directive('pointLinechart', function() {
	return {
		scope: {
			id: "@",
			option: "="
		},
		restrict: 'E', // E = Element, A = Attribute, C = Class, M = Comment  
		//		template: '<div><div style="height:300px"></div><div style="height:300px"></div></div>',
		template: '<div style="height:1000px"></div>',
		replace: true,
		link: function($scope, iElm, iAttrs, controller) {
			var myChart;
			var list = {};
			/*结果表x轴*/
			var xdata = [];
			/*变化表x轴*/
			var xdata2 = [];
			/*结果表数据*/
			var mydata = [];
			/*变化表数据*/
			var mydataRate = [];
			/*变化表线段名称*/
			var mydataRatename = [];
			/*结果表线段名称*/
			var mydataname = [];
			/*标题*/
			var mydatatitle = [];
			$scope.$watch('option', function(oldvalue, newvalue) {
				var list = {};
				if($scope.option) {
					list = $scope.option;
					if(list.allData.dataTotalVariation) {
						for(var i = 0; i < list.allData.dataTotalVariation.length; i++) {
							var dataTotalVariation = list.allData.dataTotalVariation[i];
							var itemlist = [];
							for(var j = 0; j < dataTotalVariation.mapTotalVariationPoint.length; j++) {
								if(i == 0) {
									xdata.push(dataTotalVariation.mapTotalVariationPoint[j].dateMonitor);
								}
								itemlist.push(dataTotalVariation.mapTotalVariationPoint[j].totalVariation);
							}
							mydata.push(itemlist);

							mydataname.push(dataTotalVariation.mapTotalVariationPoint[0].pointNumber)
						}
					}
					if(list.allData.dataRate) {
						for(var i = 0; i < list.allData.dataRate.length; i++) {
							var dataRate2 = list.allData.dataRate[i];
							//				console.log(JSON.stringify(dataRate2));
							var itemlist = [];
							for(var j = 0; j < dataRate2.mapRatePoint.length; j++) {
								if(i == 0) {
									xdata2.push(dataRate2.mapRatePoint[j].dateMonitor);
								}
								itemlist.push(dataRate2.mapRatePoint[j].rate);
							}
							mydataRate.push(itemlist);
							mydataRatename.push(dataRate2.mapRatePoint[0].pointNumber);
						}
					}

					var itmetitle = {
						text: list.allData.nameTotalVariation,
						left: 'center',

					}
					var itmetitle2 = {
						text: list.allData.nameRate,
						left: 'center',
						top: '50%',

					}
					mydatatitle.push(itmetitle);
					mydatatitle.push(itmetitle2);
					var series = [];
					for(var i = 0; i < mydata.length; i++) {
						var item = {
							name: mydataname[i],
							type: 'line',
							showSymbol: false,
							hoverAnimation: false,
							data: mydata[i],
							smooth: true
						}

						series.push(item);

					}
					if(mydataRate) {

					}
					for(var i = 0; i < mydataRate.length; i++) {
						console.log(JSON.stringify(mydataRate))
						var item2 = {
							name: mydataRatename[i],
							type: 'line',
							xAxisIndex: 1,
							yAxisIndex: 1,
							symbolSize: 8,
							showSymbol: false,
							hoverAnimation: false,
							data: mydataRate[i],
							smooth: true
						}
						series.push(item2);
					}
					var legendSelected = {

					};
					/*
								if(mydataname.length > 10) {
									for(var i = 10; i < mydataname.length; i++) {
										legendSelected[mydataname[i]] = false;
									}
								}*/
					option = {
						title: mydatatitle,
						tooltip: {
							trigger: 'axis',

						},
						grid: [{
							left: 50,
							right: 150,
							height: '35%'
						}, {
							left: 50,
							right: 150,
							top: '55%',
							height: '35%'
						}],
						legend: [{
							data: mydataname,
							selected: legendSelected,
							type: 'scroll',
							orient: 'vertical',
							right: 10,
							top: 20,
							bottom: '55%'
						}, {
							gridIndex: 1,
							data: mydataRatename,
							selected: legendSelected,
							type: 'scroll',
							orient: 'vertical',
							right: 10,
							top: '55%',
							bottom: 20,
						}],
						xAxis: [{
							type: 'category',
							data: xdata,

							boundaryGap: false,
						}, {
							type: 'category',
							gridIndex: 1,
							data: xdata2,
							splitLine: {
								show: false
							},
							boundaryGap: false,
						}],
						yAxis: [{
							type: 'value',
							boundaryGap: [0, '100%'],
							splitLine: {
								show: false
							}
						}, {
							gridIndex: 1,
							type: 'value',
							boundaryGap: [0, '100%'],
							splitLine: {
								show: false
							}
						}],
						series: series
					};

				}
				myChart = echarts.init(iElm[0], 'macarons');
				myChart.setOption(option);
			}, true)

			//myChart.clear();												
		}
	}
});
app.directive('selectMultiples', function($rootScope, JQ_CONFIG, uiLoad, $timeout) {
	return {
		restrict: 'A',
		require: "ngModel",
		link: function(scope, element, attrs, ngModel) {
			var inputinid = scope.$eval(attrs.inputInId);
			uiLoad.load(JQ_CONFIG['chosen']).then(function() {
				$timeout(function() {
					element.chosen();
					if(inputinid) {
						if(inputinid.length > 0) {

							element.find('option').each(function(i, item) {
								for(var i = 0; i < inputinid.length; i++) {
									if($(item).attr('value') == inputinid[i])
										$(item).attr({
											'selected': 'selected'
										});
								}
							})
							element.trigger('chosen:updated');
						}
					}

				}, 0)

			}).catch(function() {

			});

		}
	}
});
app.directive('select2', function(select2Query) {
	return {
		restrict: 'A',
		scope: {
			config: '=',
			ngModel: '=',
			select2Model: '='
		},
		link: function(scope, element, attrs) {
			// 初始化
			var tagName = element[0].tagName,
				config = {
					allowClear: true,
					multiple: !!attrs.multiple,
					placeholder: attrs.placeholder || ' ' // 修复不出现删除按钮的情况
				};

			// 生成select
			if(tagName === 'SELECT') {
				// 初始化
				var $element = $(element);
				delete config.multiple;

				$element
					.prepend('<option value=""></option>')
					.val('')
					.select2(config);

				// model - view
				scope.$watch('ngModel', function(newVal) {
					setTimeout(function() {
						$element.find('[value^="?"]').remove(); // 清除错误的数据
						$element.select2('val', newVal);
					}, 0);
				}, true);
				return false;
			}

			// 处理input
			if(tagName === 'INPUT') {
				// 初始化
				var $element = $(element);

				// 获取内置配置
				if(attrs.query) {
					scope.config = select2Query[attrs.query]();
				}

				// 动态生成select2
				scope.$watch('config', function() {
					angular.extend(config, scope.config);
					$element.select2('destroy').select2(config);
				}, true);

				// view - model
				$element.on('change', function() {
					scope.$apply(function() {
						scope.select2Model = $element.select2('data');
					});
				});

				// model - view
				scope.$watch('select2Model', function(newVal) {
					$element.select2('data', newVal);
				}, true);

				// model - view
				scope.$watch('ngModel', function(newVal) {
					// 跳过ajax方式以及多选情况
					if(config.ajax || config.multiple) {
						return false
					}

					$element.select2('val', newVal);
				}, true);
			}
		}
	}
});

/**
 * select2 内置查询功能
 */
app.factory('select2Query', function($timeout) {
	return {
		testAJAX: function() {
			var config = {
				minimumInputLength: 1,
				ajax: {
					url: "http://api.rottentomatoes.com/api/public/v1.0/movies.json",
					dataType: 'jsonp',
					data: function(term) {
						return {
							q: term,
							page_limit: 10,
							apikey: "ju6z9mjyajq2djue3gbvv26t"
						};
					},
					results: function(data, page) {
						return {
							results: data.movies
						};
					}
				},
				formatResult: function(data) {
					return data.title;
				},
				formatSelection: function(data) {
					return data.title;
				}
			};

			return config;
		}
	}
});
app.directive('subNav', function() {
	return {
		restrict: 'A',
		link: function(scope, element, attrs) {

			element.on('click', '.nav-item a', function() {
				if($(this).next().length > 0) {
					if($(this).next().css('display') == "none") {
						//展开未展开
						$('.nav-item').children('ul').slideUp(300);
						$(this).next('ul').slideDown(300);
						$(this).parent('li').addClass('nav-show').siblings('li').removeClass('nav-show');
						/*	$(this).parent('li').nextUntil('.nav-item a').css("display", "none");*/
					} else {
						//收缩已展开
						$(this).next('ul').slideUp(300);
						$('.nav-item.nav-show').removeClass('nav-show');
						/*$(this).parent('li').nextUntil('.nav-item a').css("display", "block");*/
					}
				}
			})

		}
	}
});
//树形结构  
app.directive('zTree', function() {
	return {
		scope: {
			id: "@",
			setting: "=setting",
			option: "=",
			backdata: "=",
			seldata: "=",
			isChecked: "=",
			ok: "&",
			isCheckedAll: "&"

		},
		restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment  
		require: '?ngModel',
		link: function($scope, element, attrs, ngModel, ngModelController) {
			$scope.setting = {
				async: {
					enable: true,

				},
				view: {
					showIcon: false,
					showLine: true,
					txtSelectedEnable: true,
					showTitle: true,
					selectedMulti: true,
					dblClickExpand: true,

				},
				check: {
					enable: true, //true / false 分别表示 显示 / 不显示 复选框或单选框
					autoCheckTrigger: true,
					nocheckInherit: true,
					checkTypeFlag: true
				},
				data: {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "pid",
						rootPId: 0,
					},
					key: {
						url: "",
					}
				},
				callback: {
					/*onCheck: onCheck,
					onClick: zTreeOnClick,
					onAsyncSuccess: onAsyncSuccess*/
				}
			}
			var isInitTree = false;
			$scope.$watch('option', function(newvalue, oldvalue) {
				if(newvalue.length > 0 && (newvalue != oldvalue)) {
					$.fn.zTree.init($("#treeDemo"), $scope.setting, $scope.option);
					isInitTree = true;
				}
			}, true);
			$scope.$watch('backdata', function(newvalue, oldvalue) {
				if((newvalue != oldvalue) && isInitTree) {
					$.fn.zTree.init($("#treeDemo"), $scope.setting, $scope.option);
					var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
					if(zTreeObj.checkAllNodes) {
						zTreeObj.checkAllNodes(false); //将所有节点不选中
						zTreeObj.expandAll(false); //关闭所有节点
						if(($scope.backdata.length == $scope.option.length) && $scope.option.length > 0) {
							$scope.isChecked = true;
						} else {
							$scope.isChecked = false;
						}
					}
					for(var i = 0; i < $scope.option.length; i++) {
						for(var j = 0; j < $scope.backdata.length; j++) {
							if($scope.backdata[j].moduleId) {
								if($scope.backdata[j].moduleId == $scope.option[i].id) {
									var checkedNode = zTreeObj.getNodeByParam("id", $scope.backdata[j].moduleId);
									//判断是否选中

									zTreeObj.checkNode(checkedNode, true, false);
									zTreeObj.selectNode(checkedNode, true); //指定选中ID的节点  
									zTreeObj.expandNode(checkedNode, true); //展开选中的	
									zTreeObj.updateNode(checkedNode, false); //更新接点，且不联动操作
								}
							}

						}
					}
				}

			}, true);

		}
	}
});

app.directive('zsTree', function($timeout) {
	return {
		scope: {
			id: "@",
			setting: "=setting",
			option: "=",
			backdata: "=",
			seldata: "=",
			isChecked: "=isChecked",
			ok: "&",
			isCheckedAll: "&",

		},
		restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment  
		require: '?ngModel',
		link: function($scope, element, attrs, ngModel, ngModelController) {
			$scope.setting = {
				async: {
					enable: true,

				},
				view: {
					showIcon: false,
					showLine: true,
					txtSelectedEnable: true,
					showTitle: true,
					selectedMulti: true,
					dblClickExpand: true,
				},
				check: {
					enable: true, //true / false 分别表示 显示 / 不显示 复选框或单选框
					autoCheckTrigger: true,
					nocheckInherit: true,
					checkTypeFlag: true
				},
				data: {
					simpleData: {
						enable: true,
						idKey: "syslevelId",
						pIdKey: "syslevelPid",
						rootPId: 0,
					},
					key: {
						url: "",
						name: "syslevelName"
					}
				},
				callback: {
					/*onCheck: onCheck,
					onClick: zTreeOnClick,
					onAsyncSuccess: onAsyncSuccess*/
				}
			}
			var isInitTree = false;
			$scope.$watch('option', function(newvalue, oldvalue) {
				if(newvalue.length > 0 && (newvalue != oldvalue)) {
					$.fn.zTree.init($("#treeDemo"), $scope.setting, $scope.option);
					isInitTree = true;
				}

			}, true);
			$scope.$watch('backdata', function(newvalue, oldvalue) {
				if((newvalue != oldvalue) && isInitTree) {
					$.fn.zTree.init($("#treeDemo"), $scope.setting, $scope.option);
					var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
					if(zTreeObj.checkAllNodes) {
						zTreeObj.checkAllNodes(false); //将所有节点不选中
						zTreeObj.expandAll(false); //关闭所有节点
						if(($scope.backdata.length == $scope.option.length) && $scope.option.length > 0) {
							$scope.isChecked = true;
						} else {
							$scope.isChecked = false;
						}
					}

					for(var i = 0; i < $scope.option.length; i++) {
						for(var j = 0; j < $scope.backdata.length; j++) {
							if($scope.backdata[j].syslevelId) {
								if($scope.backdata[j].syslevelId == $scope.option[i].syslevelId) {
									var checkedNode = zTreeObj.getNodeByParam("syslevelId", $scope.backdata[j].syslevelId);
									//判断是否选中
									zTreeObj.checkNode(checkedNode, true, false);
									zTreeObj.selectNode(checkedNode, true); //指定选中ID的节点  
									zTreeObj.expandNode(checkedNode, true); //展开选中的	
									zTreeObj.updateNode(checkedNode, false); //更新接点，且不联动操作

								}
							}

						}
					}
				}

			}, true);
		}
	}
});