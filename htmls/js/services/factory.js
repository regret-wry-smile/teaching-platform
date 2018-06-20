app.factory('Path', function() {
	return {
		//backendUrl: "http://192.168.10.181:8080/fpm/",
		backendUrl: "http://localhost:8080/fpm/",
		backendUrl: "http://localhost/fpm/",
		/*backendUrl: "/fpm/"*/
	}
}).factory('UserInterceptor', ["$window", "$q", "$rootScope", "Path", function($window, $q, $rootScope, Path) {
	var numLoadings = 0;
	$rootScope.loading = false;
	return {
		request: function(config) {
			numLoadings++;
			$rootScope.loading = true;
			return config;
		},
		response: function(response) {
			//00：失败且不弹出，
			//01：失败且弹出，
			//10：成功且不弹出，
			//11：成功且弹出
			$rootScope.tip = response.data;
			//alert($rootScope.tip);

			if(response.data.ret != "success") {
				//console.log(JSON.stringify(response.data.reason));
			}
			if(--numLoadings == 0) {
				$rootScope.loading = false;
			}

			return response;
		},
		responseError: function(response) {

			if(--numLoadings == 0) {
				$rootScope.loading = false;
			}
			var status = response.status;
			//var reason = response.data.reason;
			var errorCode = response.errorCode;
			if(status == "600") {
				$window.location.href = Path.backendUrl + '/index.html#/login';

			}
			if(errorCode == '01') {
				/*toastr.error(response.reason,'');*/
			} else if(errorCode == '11') {
				/*toastr.success(response.reason,'');*/
			}
			return $q.reject(response);
		}
	};
}]).factory('dataFactory', function($http, $q, Path) {
		return {
			getList: function(endpoint, method, params) {
				var headers = {
					'Content-Type': 'application/json',
					/*	'X-Requested-With': 'XMLHttpRequest',
						'Cache-Control': 'no-cache',
						'Pragma': 'no-cache'*/
				};
				if(params){
					params=angular.fromJson(params);
//					var newparams=JSON.parse(params.json);	
					var newparams=params.json;	
				}
				
				var defer = $q.defer();
				if(params) {
					$http({
						url: endpoint,
						method: method,
						headers: headers,
						data: newparams,
						dataType: "json",
						timeout: 300000
					}).success(function(data) {
						defer.resolve(data);
					}).
					error(function(data, status, headers, config) {
						defer.reject(data);
					});
				} else {
					$http({
						url: endpoint,
						method: method,
						headers: headers,

					}).success(function(data) {
						defer.resolve(data);
					}).
					error(function(data, status, headers, config) {
						defer.reject(data);
					});
				}
				return defer.promise;
			}
		}
	})
.factory('systemService', function($http, $q, Path,dataFactory) {
	//			登陆的服务
	return {
		doLogin: function(param) {
			var url = Path.backendUrl + 'user/login';
			var d = $q.defer();
			dataFactory.getList(url, 'post', param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		/*查询个人信息暂未用*/
		searchUser: function() {
			var url = Path.backendUrl + 'user/searchUser';
			var d = $q.defer();
			dataFactory.getList(url, 'post').then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		//修改密码
		updatePassword: function(param) {
			var url = Path.backendUrl + 'user/updatePassword';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		
		getCode: function(param) {
			var url = Path.backendUrl + 'user/sendCode';
			var d = $q.defer();
			dataFactory.getList(url, 'post', param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		doForgetpwd: function(param) {
			var url = Path.backendUrl + 'user/forgetpwd';
			var d = $q.defer();
			dataFactory.getList(url, 'post', param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		logOut: function(param) {
			var url = Path.backendUrl + 'user/logOut';
			var d = $q.defer();
			dataFactory.getList(url, 'post', param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		/*查询所有系统名称*/
		searchAllCompany: function() {
			var url = Path.backendUrl + 'system/company/searchAllCompany';
			var d = $q.defer();
			dataFactory.getList(url, 'post').then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		/*修改系统信息*/
		updateCompany: function(param) {
			var url = Path.backendUrl + 'system/company/updateCompany';
			var d = $q.defer();
			dataFactory.getList(url, 'post', param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		insertCompany: function(param) {
			var url = Path.backendUrl + 'system/company/insertCompany';
			var d = $q.defer();
			dataFactory.getList(url, 'post', param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		deleteCompany: function(param) {
			var url = Path.backendUrl + 'system/company/deleteCompany';
			var d = $q.defer();
			dataFactory.getList(url, 'post', param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}

			)
			return d.promise;
		},
		
		
		}
}).factory('localsService', ['$window', function($window) {
	//		登陆后本地保存的信息
	return {
		set: function(key, value) {
			$window.localStorage[key] = value;
		},
		get: function(key, defaultValue) {
			return $window.localStorage[key] || defaultValue;
		},
		setObject: function(key, value) {
			$window.localStorage[key] = JSON.stringify(value);
		},
		getObject: function(key) {
			return JSON.parse($window.localStorage[key] || '{}');
		}
	}
}]).factory('projectService', function($http, $q, Path,dataFactory) {
	/***工程信息******/
	return {
		//查询项目类型及数量
		searchCountProjectInfoByStatus: function() {
			var url = Path.backendUrl + 'project/searchCountProjectInfoByStatus';
			var d = $q.defer();
			dataFactory.getList(url, 'post').then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//系统概况中项目信息下的下拉框返回
		searchDictTree: function() {
			var url = Path.backendUrl + 'system/dict/searchDictTree';
			var d = $q.defer();
			dataFactory.getList(url, 'post').then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询某个项目下的设备关联的监测类型
		selectProjectMonitorType: function() {
			var url = Path.backendUrl + 'system/dictType/selectProjectMonitorType';
			var d = $q.defer();
			dataFactory.getList(url, 'post').then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//初始值录入单位
		searchUnitByTypeDict: function(param) {
			var url = Path.backendUrl + 'project/searchUnitByTypeDict';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//查询项目统计
		searchCountProjectInfoByProvince: function(param) {
			var url = Path.backendUrl + 'project/searchCountProjectInfoByProvince';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查看工程信息项目概况
		searchProjectInfo: function(param) {
			var url = Path.backendUrl + 'project/searchProjectInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//编辑项目概况
		updateProjectInfo: function(param) {
			var url = Path.backendUrl + 'project/updateProjectInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增项目概况
		insertProjectInfo: function(param) {
			var url = Path.backendUrl + 'project/insertProjectInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除项目概况
		deleteProjectInfo: function(param) {
			var url = Path.backendUrl + 'project/deleteProjectInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		searchmutiProjectInfo: function(param) {
			var url = Path.backendUrl + 'project/searchProjectInfoByCon';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查看基坑工程概况
		searchFoundationPitInfo: function(param) {
			var url = Path.backendUrl + 'project/searchFoundationPitInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//编辑基坑工程概况
		updateFoundationPitInfo: function(param) {
			var url = Path.backendUrl + 'project/updateFoundationPitInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增基坑工程概况
		insertFoundationPit: function(param) {
			var url = Path.backendUrl + 'project/insertFoundationPit';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除基坑工程概况
		deleteFoundationPit: function(param) {
			var url = Path.backendUrl + 'project/deleteFoundationPit';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查看水文地质
		searchHydrogeology: function(param) {
			var url = Path.backendUrl + 'project/searchHydrogeology';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//编辑水文地质
		updateHydrogeology: function(param) {
			var url = Path.backendUrl + 'project/updateHydrogeology';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增水文地质
		insertHydrogeology: function(param) {
			var url = Path.backendUrl + 'project/insertHydrogeology';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除水文地质
		deleteHydrogeology: function(param) {
			var url = Path.backendUrl + 'project/deleteHydrogeology';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查看设备
		searchEquipment: function(param) {
			var url = Path.backendUrl + 'project/searchEquipment';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询某一设备使用记录
		searchEquUseRecord: function(param) {
			var url = Path.backendUrl + 'project/searchEquUseRecord';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询已有设备回填减少用户输入		
		searchEquipmentByNumber: function(param) {
			var url = Path.backendUrl + 'project/searchEquipmentByNumber';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//数据上传查看设备
		searchAllEquipment: function(param) {
			var url = Path.backendUrl + 'project/searchAllEquipment';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//编辑设备
		updateEquipment: function(param) {
			var url = Path.backendUrl + 'project/updateEquipment';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增设备
		insertEquipment: function(param) {
			var url = Path.backendUrl + 'project/insertEquipment';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除设备
		deleteEquipment: function(param) {
			var url = Path.backendUrl + 'project/deleteEquipment';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询监测项目
		searchMonitorProject: function(param) {
			var url = Path.backendUrl + 'project/searchMonitorProject';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询监测项目的大类型导航
		searchMonitorProjectBig: function(param) {
			var url = Path.backendUrl + 'project/searchMonitorProjectBig';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//修改监测项目
		updateMonitorProject: function(param) {
			var url = Path.backendUrl + 'project/updateMonitorProject';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增监测项目
		insertMonitorProject: function(param) {
			var url = Path.backendUrl + 'project/insertMonitorProject';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//添加监测项目时需要检查该监测项目的名称不能重复
		searchMonitorProjecByName: function(param) {
			var url = Path.backendUrl + 'project/searchMonitorProjecByName';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//删除监测项目
		deleteMonitorProject: function(param) {
			var url = Path.backendUrl + 'project/deleteMonitorProject';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*查询监测频率*/
		searchMonitorFrequency: function(param) {
			var url = Path.backendUrl + 'project/searchMonitorProjectFrequency';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//通过监测类型查询单位
		searchUnitByTypeDict: function(param) {
			var url = Path.backendUrl + 'project/searchUnitByTypeDict';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//查询人员信息
		searchUserInfo: function(param) {
			var url = Path.backendUrl + 'project/searchUserInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//根据电话查询用户信息
		searchByPhoneNumberUserInfo: function(param) {
			var url = Path.backendUrl + 'project/searchByPhoneNumber';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//修改人员信息
		updateUserInfo: function(param) {
			var url = Path.backendUrl + 'project/updateUserInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增人员信息
		insertUserInfo: function(param) {
			var url = Path.backendUrl + 'project/insertUserInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除人员信息
		deleteUserInfo: function(param) {
			var url = Path.backendUrl + 'project/deleteUserInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//根据电话查用户登陆信息
		searchUserByPhone: function(param) {
			var url = Path.backendUrl + 'system/searchUserByPhone';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//根据邮箱查人员信息
		searchUserByEmail: function(param) {
			var url = Path.backendUrl + 'system/searchUserByEmail';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//查看参建单位
		searchBluidGrade: function(param) {
			var url = Path.backendUrl + 'project/searchParticipateUnit';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},

		//新增参建单位
		insertParticipateUnit: function(param) {
			var url = Path.backendUrl + 'project/insertParticipateUnit';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*******工程信息-预警消警*******/
		//根据基坑查已经锁定的监测报告的期数
		searchPeriodsLock: function(param) {
			var url = Path.backendUrl + 'report/searchPeriodsLock';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//预警管理概况查询监测项目
		searchWarningMonitorProject: function(param) {
			var url = Path.backendUrl + 'warning/searchWarningMonitorProject';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//预警管理概况查询所有异常数据
		searchWarningExceptionData: function(param) {
			var url = Path.backendUrl + 'warning/searchWarningExceptionData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询预警消警结论
		searchWarningConclusion: function(param) {
			var url = Path.backendUrl + 'warning/searchWarningConclusion';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增和编辑预警消警结论
		updateWarningConclusion: function(param) {
			var url = Path.backendUrl + 'warning/updateWarningConclusion';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}
		
	}
}).factory('dictService', function($http, $q, Path,dataFactory) {
	/*******系统-数据字典***********/
	return {
		//查询字典分类  
		searchDictTypeById: function(param) {
			var url = Path.backendUrl + 'system/dictType/searchDictTypeById';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增字典分类
		insertDictType: function(param) {
			var url = Path.backendUrl + 'system/dictType/insertDictType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除字典分类
		deleteDictType: function(param) {
			var url = Path.backendUrl + 'system/dictType/deleteDictType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//更新字典分类
		updateDictType: function(param) {
			var url = Path.backendUrl + 'system/dictType/updateDictType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询字典表
		searchDictsByDict: function(param) {
			var url = Path.backendUrl + 'system/dict/searchDictsByDict';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//保存字典表  
		insertDict: function(param) {
			var url = Path.backendUrl + 'system/dict/insertDict';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//修改字典表  
		updateDict: function(param) {
			var url = Path.backendUrl + 'system/dict/updateDict';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除字典表  
		deleteDict: function(param) {
			var url = Path.backendUrl + 'system/dict/deleteDict';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}, //查询操作记录
		findOutlineRecord: function(param) {
			var url = Path.backendUrl + 'system/outlineRecord/findOutlineRecord';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//根据类型查询数据字典
		searchTypedatadict: function(param) {
			var url = Path.backendUrl + 'system/dictType/searchDictTypeByType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询计算规则类型
		searchCalculateruleType: function(param) {
			var url = Path.backendUrl + 'system/calculateruleType/searchCalculateruleType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}, //增加计算规则类型
		insertCalculateruleType: function(param) {
			var url = Path.backendUrl + 'system/calculateruleType/insertCalculateruleType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}, //删除计算规则类型
		deleteCalculateruleType: function(param) {
			var url = Path.backendUrl + 'system/calculateruleType/deleteCalculateruleType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}, //更新计算规则类型
		updateCalculateruleType: function(param) {
			var url = Path.backendUrl + 'system/calculateruleType/updateCalculateruleType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}, //查询计算规则内容
		searchCalculateRule: function(param) {
			var url = Path.backendUrl + 'system/calculateRule/searchCalculateRule';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询计算规则的最高的计算公式
		searchCalculateRuleTop: function(param) {
			var url = Path.backendUrl + 'system/searchCalculateRuleTop';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//增加计算规则内容
		insertCalculateRule: function(param) {
			var url = Path.backendUrl + 'system/calculateRule/insertCalculateRule';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}, //删除计算规则内容
		deleteCalculateRule: function(param) {
			var url = Path.backendUrl + 'system/calculateRule/deleteCalculateRule';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}, //更新计算规则内容
		updateCalculateRule: function(param) {
			var url = Path.backendUrl + 'system/calculateRule/updateCalculateRule';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询字典中的监测项目类型  
		searchMonitorDict: function() {
			var url = Path.backendUrl + 'system/dict/searchMonitorDict';
			var d = $q.defer();
			dataFactory.getList(url, 'post').then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//按精度截取常量位数
		AccuracyDeal: function(param) {
			var url = Path.backendUrl + 'system/searchConstantAccuracyDeal';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},

		//查询报表模板
		searchReportsModel: function(param) {
			var url = Path.backendUrl + 'system/reportsModel/searchReportsModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//更新报表模板
		updateReportsModel: function(param) {
			var url = Path.backendUrl + 'system/reportsModel/updateReportsModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//删除报表模板
		deleteReportsModelsystem: function(param) {
			var url = Path.backendUrl + 'system/reportsModel/deleteReportsModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//分析报表模板的sheel页
		analysisReportsModel: function(param) {
			var url = Path.backendUrl + 'system/reportsModel/analysisReportsModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//批量保存每个 sheet 对应的监测项目关系
		batchInsertReportsModel: function(param) {
			var url = Path.backendUrl + 'system/reportsModel/batchInsertReportsModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//系统--增加报表模板
		insertReportsModel: function(param) {
			var url = Path.backendUrl + 'system/reportsModel/insertReportsModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询统一参数返回
		searchAllParam: function(param) {
			var url = Path.backendUrl + 'monitorData/searchAllParam';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询接口常量
		searchConstant: function(param) {
			var url = Path.backendUrl + 'system/searchConstant';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询参数列表
		searchparam: function(param) {
			var url = Path.backendUrl + 'system/searchCalculateRuleByType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},

	}

}).factory('monitorDataService', function($http, $q, Path,dataFactory) {
	/*******监测数据***********/
	return {
		//查询点信息  
		searchPoint: function(param) {
			var url = Path.backendUrl + 'monitorData/searchPoint';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}, //更新点信息
		updatePoint: function(param) {
			var url = Path.backendUrl + 'monitorData/updatePoint';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增点信息 ( 报表导入的时候最后确定保存的接口 )
		insertPoint: function(param) {
			var url = Path.backendUrl + 'monitorData/insertPoint';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//添加点信息(单个添加)
		addPoint: function(param) {
			var url = Path.backendUrl + 'monitorData/addPoint';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除点信息  
		deletePoint: function(param) {
			var url = Path.backendUrl + 'monitorData/deletePoint';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询轴力类下面的还没有关联的钢筋计
		searchSteelBarByNotAssociate: function() {
			var url = Path.backendUrl + 'monitorData/searchSteelBarByNotAssociate';
			var d = $q.defer();
			dataFactory.getList(url, 'post').then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询初始值录入处的轴力类下的钢筋参数和混凝土参数
		searchSteelParam: function(param) {
			var url = Path.backendUrl + 'monitorData/searchSteelParam';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//编辑初始值录入处的钢筋参数和混凝土参数
		updateSteelParam: function(param) {
			var url = Path.backendUrl + 'monitorData/updateSteelParam';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下具体的类别数据
		searchAllDataByType: function(param) {
			var url = Path.backendUrl + 'monitorData/searchAllDataByType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的轴力类数据
		searchAxialForceData: function(param) {
			var url = Path.backendUrl + 'monitorData/searchAxialForceData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的轴力类表头负责人数据
		searchAxialForceDataManager: function(param) {
			var url = Path.backendUrl + 'monitorData/searchAxialForceDataManager';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的位移类数据
		searchDisplacementData: function(param) {
			var url = Path.backendUrl + 'monitorData/searchDisplacementData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的位移类表头负责人数据
		searchDisplacementDataManager: function(param) {
			var url = Path.backendUrl + 'monitorData/searchDisplacementDataManager';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的测斜类数据
		searchInclinometerData: function(param) {
			var url = Path.backendUrl + 'monitorData/searchInclinometerData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的测斜类表头负责人数据
		searchInclinometerDataManager: function(param) {
			var url = Path.backendUrl + 'monitorData/searchInclinometerDataManager';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的沉降类数据
		searchSettlementData: function(param) {
			var url = Path.backendUrl + 'monitorData/searchSettlementData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的沉降类表头负责人数据
		searchSettlementDataManager: function(param) {
			var url = Path.backendUrl + 'monitorData/searchSettlementDataManager';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的水位类数据
		searchWaterLevelData: function(param) {
			var url = Path.backendUrl + 'monitorData/searchWaterLevelData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询概况下的水位类表头负责人数据
		searchWaterLevelDataManager: function(param) {
			var url = Path.backendUrl + 'monitorData/searchWaterLevelDataManager';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询巡视检查具体数据
		searchPatrolContent: function(param) {
			var url = Path.backendUrl + 'monitorData/searchPatrolContent';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询巡视检查的表头
		searchDataManagerPatrol: function(param) {
			var url = Path.backendUrl + 'monitorData/searchDataManagerPatrol';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//修改巡视检查下面的结果
		updatePatrolContent: function(param) {
			var url = Path.backendUrl + 'monitorData/updatePatrolContent';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//修改巡视检查的表头负责人
		updatePatrolDataManager: function(param) {
			var url = Path.backendUrl + 'monitorData/updatePatrolDataManager';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询所有类型的表头
		searchAllTypeDataManager: function(param) {
			var url = Path.backendUrl + 'monitorData/searchAllTypeDataManager';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//通过角色类型查询人员
		searchUserByRoleTypeCode: function(param) {
			var url = Path.backendUrl + 'system/searchUserByRoleTypeCode';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//编辑表头负责人
		updateDataManager: function(param) {
			var url = Path.backendUrl + 'monitorData/updateDataManager';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
	
		//新增巡视检查  
		insertPatrolResult: function(param) {
			var url = Path.backendUrl + 'monitorData/insertPatrolResult';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*查询异常数据分析中的监测项目*/
		searchExceptionReportMonitorProject: function(param) {
			var url = Path.backendUrl + 'monitorData/searchExceptionReportMonitorProject';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询异常数据分析的期数
		searchPeriods: function(param) {
			var url = Path.backendUrl + 'exceptionReport/searchPeriods';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//查询异常数据分析  
		searchExceptionReport: function(param) {
			var url = Path.backendUrl + 'monitorData/searchExceptionReportData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}, //更新异常数据分析
		updateExceptionReport: function(param) {
			var url = Path.backendUrl + 'monitorData/updateExceptionReportData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询异常数据分析报告中的异常数据
		searchExceptionAllData: function(param) {
			var url = Path.backendUrl + 'monitorData/searchExceptionAllData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增异常数据分析  
		insertExceptionReport: function(param) {
			var url = Path.backendUrl + 'monitorData/insertExceptionReport';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*//删除异常数据分析  
		deleteExceptionReport: function(param) {
			var d = $q.defer();
			$http({
					url: Path.backendUrl + '/monitorData/deleteExceptionReport',
					method: 'post',
					data: $.param(param)
				})
				.then(function(response) {
					d.resolve(response.data);
				}, function err(reason) {
					d.reject(reason);
				});
			return d.promise;
		},*/
		//新增监测报告  
		//		insertMonitorReport: function(param) {
		//			var d = $q.defer();
		//			$http({
		//					url: Path.backendUrl + 'monitorData/insertMonitorReport',
		//					method: 'post',
		//					data: $.param(param)
		//				})
		//				.then(function(response) {
		//					d.resolve(response.data);
		//				}, function err(reason) {
		//					d.reject(reason);
		//				});
		//			return d.promise;
		//		},
		//新增监测数据
		insertMonitorData: function(param) {
			var url = Path.backendUrl + 'monitorData/insertMonitorData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除监测数据
		deleteMonitorData: function(param) {
			var url = Path.backendUrl + 'monitorData/deleteMonitorData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//更新监测数据
		updateMonitorData: function(param) {
			var url = Path.backendUrl + 'monitorData/updateMonitorData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询监测数据
		searchMonitorData: function(param) {
			var url = Path.backendUrl + 'monitorData/searchMonitorData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//修改监测分析
		updateMonitorAnalysis: function(param) {
			var url = Path.backendUrl + 'monitorData/updateMonitorAnalysis';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增监测分析
		insertMonitorAnalysis: function(param) {
			var url = Path.backendUrl + 'monitorData/insertMonitorAnalysis';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询监测分析
		searchMonitorAnalysis: function(param) {
			var url = Path.backendUrl + 'monitorData/searchMonitorAnalysis';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//本期工作量统计
		pointCount: function(param) {
			var url = Path.backendUrl + 'monitorData/pointCount';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//监测成果统计
		pointCompare: function(param) {
			var url = Path.backendUrl + 'monitorData/pointCompare';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询监测数据--概况
		searchMonitorGeneralSituation: function(param) {
			var url = Path.backendUrl + 'monitorData/searchMonitorGeneralSituation';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//监测数据数据计算 
		calculateTotalData: function(param) {
			var url = Path.backendUrl + 'monitorData/calculateTotalData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//数据预警计算接口 
		calculateTotalDataWarning: function(param) {
			var url = Path.backendUrl + 'monitorData/calculateTotalDataWarning';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//根据基坑查最新一期的期数
		searchPeriodsByFou: function(param) {
			var url = Path.backendUrl + 'report/searchPeriodsByFou';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//上传数据
		uploadData: function(param) {
			var url = Path.backendUrl + 'monitorData/uploadData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//监测数据生成pdf
		createExceptionReortPdf: function(param) {
			var url = Path.backendUrl + 'monitorData/createExceptionReortPdf';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//上传数据预测时间
		predictDate: function(param) {
			var url = Path.backendUrl + 'monitorData/predictDate';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}
		
	}
}).factory('reportService', function($http, $q, Path,dataFactory) {
	/*******监测报告***********/
	return {
		//查看异常数据分析报告流程
		searchExceptionReportApprove: function(param) {
			var url = Path.backendUrl + 'report/searchExceptionReportApprove';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		// 查看监测报告审批流程
		searchReportApprove: function(param) {
			var url = Path.backendUrl + 'report/searchReportApprove';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查当前登录人员的审批按钮
		searchifApprove: function(param) {
			var url = Path.backendUrl + 'system/ifApprove';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//根据报告人员--权限查人员
		searchUserByModule: function(param) {
			var url = Path.backendUrl + 'system/searchUserByModule';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//监测报告/异常数据分析报告审批流程总接口
		reportApproveProcess: function(param) {
			var url = Path.backendUrl + 'report/reportApproveProcess';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		searchMonitorReportByFou: function(param) {
			var url = Path.backendUrl + 'report/searchMonitorReportByFou';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		// 查看期数
		searchPeriod: function(param) {
			var url = Path.backendUrl + 'report/searchPeriods';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		// 生成期数
		creatPeriods: function(param) {
			var url = Path.backendUrl + 'report/creatPeriods';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		// 生成期数
		creatExceptionPeriods: function(param) {
			var url = Path.backendUrl + 'report/creatExceptionPeriods';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//提交监测报告并模板导出数据
		exportDataToTemplate: function(param) {
			var url = Path.backendUrl + 'report/exportDataToTemplate';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		/*新增监测报告*/
		insertMonitorReport: function(param) {
			var url = Path.backendUrl + 'report/insertMonitorReport';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*新增监测报告*/
		insertMonitorReports: function(param) {
			var url = Path.backendUrl + 'report/insertMonitorReportDetails';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*编辑监测报告*/
		updateMonitorReports: function(param) {
			var url = Path.backendUrl + 'report/updateMonitorReportDetails';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*删除监测报告*/
		delMonitorReports: function(param) {
			var url = Path.backendUrl + 'report/deleteMonitorReportDetails';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//锁定监测报告
		unclockMonitorReport: function(param) {
			var url = Path.backendUrl + 'report/updateMonitorReportLock';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*根据监测报告id查询基坑期数和基坑-监测报告概况跳转到预览*/
		searchMonitorReportinfo: function(param) {
			var url = Path.backendUrl + 'report/searchMonitorReportById';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},

		/*查询监测报告*/
		searchMonitorReport: function(param) {
			var url = Path.backendUrl + 'report/searchMonitorReport';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},

		/*预览--查询报告详情*/
		searchMonitorReportDetails: function(param) {
			var url = Path.backendUrl + 'report/searchMonitorReportDetails';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},

		/*查询项目模板*/
		searchReportModel: function(param) {
			var url = Path.backendUrl + 'report/searchReportModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*新增项目模板*/
		addReportModel: function(param) {
			var url = Path.backendUrl + 'report/addReportModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*删除项目模板*/
		deleteReportModel: function(param) {
			var url = Path.backendUrl + 'report/deleteReportModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*编辑项目模板*/
		updateReportModel: function(param) {
			var url = Path.backendUrl + 'report/updateReportModel';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*新建异常分析--监测报告下的*/
		insertExceptionReport: function(param) {
			var url = Path.backendUrl + 'exceptionReport/insertExceptionReport';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}
	}
}).factory('drawingService', function($http, $q, Path,dataFactory) {
	/*******图纸管理***********/
	return {
		//查询图纸类型个数
		searchCountByType: function(param) {
			var url = Path.backendUrl + 'drawing/searchCountByType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询图纸
		searchDrawing: function(param) {
			var url = Path.backendUrl + 'drawing/searchDrawing';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增图纸
		insertDrawing: function(param) {
			var url = Path.backendUrl + 'drawing/insertDrawing';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除图纸
		deleteDrawing: function(param) {
			var url = Path.backendUrl + 'drawing/deleteDrawing';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//更新图纸
		updateDrawing: function(param) {
			var url = Path.backendUrl + 'drawing/updateDrawing';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}

	}
}).factory('earlyManagementService', function($http, $q, Path,dataFactory) {
	/*******预警管理***********/
	return {
		/*查询预警监测项目类型*/
		searchWarningMonitorProject:function(param){
			var url = Path.backendUrl + 'warning/searchWarningMonitorProject';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*预警管理概况查询所有异常数据*/
		searchWarningExceptionData:function(param){
			var url = Path.backendUrl + 'warning/searchWarningExceptionData';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*************预警管理-预警查询*************/
		//查询测点状态
		searchStateColor:function(param){
			var url = Path.backendUrl + 'warning/searchStateColor';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//根据开始和结束时间选择返回的期数
		searchPeriodsByStartAndEndDate:function(param){
			var url = Path.backendUrl + 'warning/searchPeriodsByStartAndEndDate';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//根据期数查询监测项目树
		searchWarningMonitorProjectByPeriods:function(param){
			var url = Path.backendUrl + 'warning/searchWarningMonitorProjectByPeriods';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//根据各种条件查询异常数据
		searchExceptionDataByCondition:function(param){
			var url = Path.backendUrl + 'warning/searchExceptionDataByCondition';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增预警指标值
		insertInitialWarning: function(param) {
			var url = Path.backendUrl + 'warning/insertInitialWarning';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查询预警指标值
		findInitialWarning: function(param) {
			var url = Path.backendUrl + 'warning/findInitialWarning';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//通过基坑id查询预警指标值
		findInitialWarningByFoundationPitId: function(param) {
			var url = Path.backendUrl + 'warning/findInitialWarningByFoundationPitId';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除预警指标值
		deleteInitialWarning: function(param) {
			var url = Path.backendUrl + 'warning/deleteInitialWarning';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//编辑预警指标值
		updateInitialWarning: function(param) {
			var url = Path.backendUrl + 'warning/updateInitialWarning';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//查预警等级记录
		findWarningRecord: function(param) {
			var url = Path.backendUrl + 'warning/findWarningRecord';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
	}
}).factory('uploadfileService', function($http, $q, Path,dataFactory) {
	/***上传文件******/
	return {
		/*共用上传文件*/
		updloadFile: function(param) {
			var d = $q.defer();
			$http({
					url: Path.backendUrl + 'upload',
					method: 'post',
					data: param,
					headers: {
						'Content-Type': undefined
					},
					transformRequest: angular.identity
				})
				.then(function(response) {
					d.resolve(response.data);
				}, function err(reason) {
					d.reject(reason);
				});
			return d.promise;
		},
		/*上传图纸可压缩*/
		uploadDrawing: function(param) {
			var d = $q.defer();
			$http({
					url: Path.backendUrl + 'uploadDrawing',
					method: 'post',
					data: param,
					headers: {
						'Content-Type': undefined
					},
					transformRequest: angular.identity
				})
				.then(function(response) {
					d.resolve(response.data);
				}, function err(reason) {
					d.reject(reason);
				});
			return d.promise;
		},
		/*上传图片不压缩*/
		uploadPicture: function(param) {
			var d = $q.defer();
			$http({
					url: Path.backendUrl + 'uploadImage',
					method: 'post',
					data: param,
					headers: {
						'Content-Type': undefined
					},
					transformRequest: angular.identity
				})
				.then(function(response) {
					d.resolve(response.data);
				}, function err(reason) {
					d.reject(reason);
				});
			return d.promise;
		},
		/*初始值上传的预览接口*/
		previewInitExcelAndFields: function(param) {
			var url = Path.backendUrl + 'previewInitExcelAndFields';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*数据上传的预览接口*/
		previewRealExcelAndFields: function(param) {
			var url = Path.backendUrl + 'previewRealExcelAndFields';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}
	}
}).factory('userInfoService', function($http, $q, Path,dataFactory) {
	/*******项目-用户***********/
	return {
		//查询人员信息
		searchUserInfo: function(param) {
			var url = Path.backendUrl + 'project/searchUserInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增用户
		insertUserInfo: function(param) {
			var url = Path.backendUrl + 'project/insertUserInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除用户
		deleteUserInfo: function(param) {
			var url = Path.backendUrl + 'project/deleteUserInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//编辑用户
		updateUserInfo: function(param) {
			var url = Path.backendUrl + 'project/updateUserInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}
	}
}).factory('SysuserInfoService', function($http, $q, Path,dataFactory) {
	/*******系统-用户***********/
	return {
		//查询用户权限
		getUsermodelbuildtree: function(param) {
			var url = Path.backendUrl + 'sys_user_rights/get_user_model_build_tree';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
		//查询人员信息
		searchsysUserInfo: function(param) {
			var url = Path.backendUrl + 'system/searchUser';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//新增用户
		insertsysUserInfo: function(param) {
			var url = Path.backendUrl + 'system/insertUser';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//删除用户
		deletesysUserInfo: function(param) {
			var url = Path.backendUrl + 'system/deleteUser';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//编辑用户
		updatesysUserInfo: function(param) {
			var url = Path.backendUrl + 'system/updateUser';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		//校验
		checkvaliate: function(param) {
			var url = Path.backendUrl + 'system/checkUserLoginInfo';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		querysysUserrole: function(param) {
			var url = Path.backendUrl + 'sys_user_role/query';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		
	}
}).factory('ProvinceService', function($http, $q, Path,dataFactory) {
	/*******查询省***********/
	return {
		searchProvince: function() {
			var url = Path.backendUrl + 'system/searchProvince';
			var d = $q.defer();
			dataFactory.getList(url, 'post').then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*根据省的编号查市信息*/
		searchCity: function(param) {
			var url = Path.backendUrl + 'system/searchCity';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*根据省的编号查区信息*/
		searchArea: function(param) {
			var url = Path.backendUrl + 'system/searchArea';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},

		/*添加一个市*/
		insertCity: function(param) {
			var url = Path.backendUrl + 'system/insertCity';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*编辑一个市*/
		updateCity: function(param) {
			var url = Path.backendUrl + 'system/updateCity';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*删除一个市*/
		deleteCity: function(param) {
			var url = Path.backendUrl + 'system/deleteCity';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*添加一个区*/
		insertArea: function(param) {
			var url = Path.backendUrl + 'system/insertArea';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*编辑一个区*/
		updateArea: function(param) {
			var url = Path.backendUrl + 'system/updateArea';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*删除一个区*/
		deleteArea: function(param) {
			var url = Path.backendUrl + 'system/deleteArea';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}

	}

}).factory('ServerService', function($http, $q, Path,dataFactory) {
	/*******系统-服务***********/
	return {
		/*查询服务器*/
		searchServer: function(param) {
			var url = Path.backendUrl + 'system/server/searchAllServer';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			var d = $q.defer();
			return d.promise;
		},
		/*根据类型查询服务器*/
		searchTypeServer: function(param) {
			var url = Path.backendUrl + 'system/server/searchServerByDictType';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*新增服务器*/
		insertServer: function(param) {
			var url = Path.backendUrl + 'system/server/insertServer';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*编辑服务器*/
		updateServer: function(param) {
			var url = Path.backendUrl + 'system/server/updateServer';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		/*删除服务器*/
		deleteServer: function(param) {
			var url = Path.backendUrl + 'system/server/deleteServer';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}
	}
})
.factory('ProjectReportRuleService', function($http, $q, Path,dataFactory) {
	return {
		searchProjectReportRule: function(param) {
			var url = Path.backendUrl + 'system/projectReportRule/searchProjectReportRule';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		},
		searchProjectReportRuleByProjectId: function(param) {
			var url = Path.backendUrl + 'system/projectReportRule/searchProjectReportRuleByProjectId';
			var d = $q.defer();
			dataFactory.getList(url, 'post',param).then(function(da) {
					d.resolve(da);
				}, function err(reason) {
					d.reject(reason);
				}
			)
			return d.promise;
		}
		
	}
}).factory('limitService', function($http, $q, Path, dataFactory) {
		/*权限设置*/
		return {
			/*********角色类型***********/
			/*查询角色类型分页*/
			getRoletype: function(param) {
				var url = Path.backendUrl + 'sys_role_type/query_by_page';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}
			)
				return d.promise;
			},
			/*查询角色类型*/
			queryRoletype: function(param) {
				var url = Path.backendUrl + 'sys_role_type/query';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},

			/*新增角色类型*/
			addRoletype: function(param) {
				var url = Path.backendUrl + 'sys_role_type/add';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			delRoletype: function(param) {
				var url = Path.backendUrl + 'sys_role_type/delete';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			updateRoletype: function(param) {
				var url = Path.backendUrl + 'sys_role_type/update';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			
			
			/*********角色管理***********/
			/*查询角色不分页*/
			queryRole: function(param) {
				var url = Path.backendUrl + 'sys_role/query';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			/*查询角色分页*/
			querypageRole: function(param) {
				var url = Path.backendUrl + 'sys_role/query_by_page';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			/*新增角色*/
			addRole: function(param) {
				var url = Path.backendUrl + 'sys_role/add';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			/*编辑角色*/
			updateRole: function(param) {
				var url = Path.backendUrl + 'sys_role/update';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			/*删除角色*/
			deleteRole: function(param) {
				var url = Path.backendUrl + 'sys_role/delete';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},

			/*********角色数据权限***********/
			//获取所有数据权限（不分页）		
			getsyslevelztree: function(param) {
				var url = Path.backendUrl + 'sys_syslevel/query';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//查询角色数据权限
			getsysleveltree: function(param) {
				var url = Path.backendUrl + 'sys_role_syslevel/query';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//设置角色数据权限
			setsysleveltree: function(param) {
				var url = Path.backendUrl + 'sys_role_syslevel/set_role_syslevels';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//复制角色：查看对应的数据权限
			getrolesyslevels: function(param) {
				var url = Path.backendUrl + 'sys_role_syslevel/get_role_syslevels';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			
			/*********角色功能菜单权限***********/
			//获取菜单树
			getmoduletree: function(param) {
				var url = Path.backendUrl + 'sys_module/get_module_tree';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			getmoduleztreeurl: function() {
				var url = Path.backendUrl + 'sys_module/get_module_ztree_url';
				var d = $q.defer();
				dataFactory.getList(url, 'post').then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},

			//查询角色对应的功能菜单权限		
			queryrolemodules: function(param) {
				var url = Path.backendUrl + 'sys_role_module/query';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			
			//复制角色：查看对应的菜单权限
			getrolemodules: function(param) {
				var url = Path.backendUrl + 'sys_role_module/get_role_modules';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//设置角色数据权限
			setrolemodules: function(param) {
				var url = Path.backendUrl + 'sys_role_module/set_role_modules';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//查询用户对应的角色
			querysystemuserbypage: function(param) {
				var url = Path.backendUrl + 'system/query_by_page_account';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//用户启用停用
			updateloginstate: function(param) {
				var url = Path.backendUrl + 'system/update_login_state';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			
			//查询用户菜单权限
			queryusermodule: function(param) {
				var url = Path.backendUrl + 'sys_user_rights/get_user_modules';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//设置用户菜单权限
			setusermodules: function(param) {
				var url = Path.backendUrl + 'sys_user_rights/set_user_modules';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//查询用户数据权限
			getuserdata: function(param) {
				var url = Path.backendUrl + 'sys_user_rights/get_user_syslevels';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//设置用户数据权限
			setuserdata: function(param) {
				var url = Path.backendUrl + 'sys_user_rights/set_user_syslevels';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//查询用户角色组关联
			getuserrolesrelate: function(param) {
				var url = Path.backendUrl + 'sys_user_role/get_user_roles';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},
			//用户角色组关联设置
			setuserrolesrelate: function(param) {
				var url = Path.backendUrl + 'sys_user_role/set_user_roles';
				var d = $q.defer();
				dataFactory.getList(url, 'post', param).then(function(da) {
						d.resolve(da);
					}, function err(reason) {
						d.reject(reason);
					}

				)

				return d.promise;
			},

		}
	})
/*app.factory('Reddit', function($http) {
	var num = 0;
	var Reddit = function() {
		this.items = [];
		this.busy = false;
		this.after = '';
	};

	Reddit.prototype.nextPage = function() {

		if(this.busy) return;
		this.busy = true;

		var url = "https://api.reddit.com/hot?after=" + this.after + "&jsonp=JSON_CALLBACK";
		$http.jsonp(url).success(function(data) {
			var items = data.data.children;
			for(var i = 0; i < items.length; i++) {
				this.items.push(items[i].data);
			}

			this.after = "t3_" + this.items[this.items.length - 1].id;
			this.busy = false;
		}.bind(this));
	};

	return Reddit;
});*/