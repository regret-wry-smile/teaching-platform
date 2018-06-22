// config
/*配置按需加载的控制器，服务等是。AngularJS一旦初始化，之后加载的controller／directive／filter／sercive是不会自动注册到模块上的*/
var app =  
angular.module('app')
  .config(
    [        '$controllerProvider', '$compileProvider', '$filterProvider', '$provide', '$httpProvider',
    function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide,  $httpProvider) {
        $httpProvider.interceptors.push('UserInterceptor');
        // lazy controller, directive and service
        app.controller = $controllerProvider.register;
        app.directive  = $compileProvider.directive;
        app.filter     = $filterProvider.register;
        app.factory    = $provide.factory;
        app.service    = $provide.service;
        app.constant   = $provide.constant;
        app.value      = $provide.value;
    }
  ])