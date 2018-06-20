'use strict';

/**
 * Config for the router
 */
app.run(
	['$rootScope', '$state', '$stateParams',
		function($rootScope, $state, $stateParams) {
			$rootScope.$state = $state;
			$rootScope.$stateParams = $stateParams;
			$rootScope.pageSize=5;
			$rootScope.$on("$stateChangeSuccess", function(event, toState, toParams, fromState, fromParams) {
				// to be used for back button //won't work when page is reloaded.
				$rootScope.previousState_name = fromState.name;
				$rootScope.previousState_params = fromParams;
				$rootScope.toState_name = toState.name
			});
			//back button function called from back button's ng-click="back()"
			$rootScope.back = function() {
				$state.go($rootScope.previousState_name, $rootScope.previousState_params);
			};
			$rootScope.isHasProj = false;
			$rootScope.Debugger = {
				switch: true,
				log: function(message) {
					try {
						if($rootScope.Debugger.switch) {
							console.log(message);
						}
					} catch(exception) {
						return 'Error:the function  log is not exist.';
					}
				},
				table: function(table) {
					try {
						if($rootScope.Debugger.switch) {
							console.table(table);
						}
					} catch(exception) {
						return 'Error:the function  log is not exist.';
					}
				}
			};

		}
	]
).config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$httpProvider.interceptors.push('UserInterceptor');
	$httpProvider.defaults.headers.post = {
		'Content-Type': 'application/x-www-form-urlencoded'
	}
	$urlRouterProvider.otherwise('/login');
	//$urlRouterProvider.otherwise('/index');
	 /*主页面*/
		.state('setting', {
			url: '/setting',
			views: {
				'': {
					templateUrl: 'set.html',
//					controller: 'reimburseCtrl',
					resolve: {
						deps: ['$ocLazyLoad',
							function($ocLazyLoad) {
								return $ocLazyLoad.load('js/controllers/reimburseController.js').then(function() {
									return $ocLazyLoad.load('treeControl').then(function() {
										return $ocLazyLoad.load(['multi-select-tree', 'js/directives/silder.js']);
									})
								})
							}
						]
					}

				}
			}
		}) /*报销-申请记录*/
		/*.state('oa.reimbursemoudle.reimbursemanage', {
			url: '/reimbursemanage/:state',
			views: {
				'': {
					templateUrl: 'page/oa/reimburse/reimbursemanage.html',
					controller: 'reimbursemanageCtrl',
					resolve: {
						deps: ['$ocLazyLoad',
							function($ocLazyLoad) {
								return $ocLazyLoad.load('treeControl').then(
									function() {
										return $ocLazyLoad.load('multi-select-tree')
									}
								)
							}
						]
					}
				}
			}

		})*/
});