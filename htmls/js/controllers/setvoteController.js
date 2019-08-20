//定义模块时引入依赖  
var app=angular.module('app',['ui.bootstrap','toastr']);
app.controller('voteCtrl', function($rootScope,$scope,$modal,toastr) {
	$scope.voteInfo={
		title:"",//主题
		describe:"",//主题描述
		programs:[''],
	}
	$scope.object="";
	//$scope.obejctList=[""];
	$scope.obejctList=[{txt:'',isChecked:false}]

	//添加小对象
	$scope.additem=function(){
		if($scope.obejctList.length<5){
		var item={
			txt:'',
			isChecked:false
		}
		$scope.obejctList.push(item);
		}
	}
	/*$scope.delObject=function(){
		if($scope.obejctList.length>1){
			$scope.obejctList.splice($scope.obejctList.length-1,1);
		}
	}*/
	var obejctindex='';
	$scope.seldelObject=function(obindex,item){
		obejctindex=obindex;
		angular.forEach($scope.obejctList, function(object, index) {
        if(obejctindex != index) {
        	 object.isChecked = false;
       	 }else{
       	 	 object.isChecked = true;
       	 }
		})
	}
	$scope.delObject=function(){
		if($scope.obejctList.length>1){
			$scope.obejctList[obejctindex].isChecked=false;
			$scope.obejctList.splice(obejctindex,1);			
			//$scope.obejctList.splice($scope.obejctList.length-1,1);
		}
	}
	//开始投票到投票统计页面
	$scope.startVote=function(){
	var nary=[];
	nary=angular.copy($scope.obejctList);
	$scope.voteInfo.programs=[];
	var flag = false;
	for(var i=0;i<nary.length;i++){	
		if(flag){
			$scope.voteInfo.programs = [];
			break;
		}
		for(var j=0;j<nary.length;j++){
			if(i != j){
				if (nary[i].txt==nary[j].txt){	
					flag = true;
					toastr.warning(JSON.stringify(nary[j].txt)+"对象重复了")
					break;
				}
			}
		}
		if(!flag){
			$scope.voteInfo.programs.push(nary[i].txt);
		}
	}
	if(!flag){
			var param=$scope.voteInfo;
			$scope.result=JSON.parse(execute_vote("start_vote",JSON.stringify(param)));
			if($scope.result.ret=='success'){		
			window.location.href="../../page/answermoudle/voteCount.html";
			}else{
				toastr.error($scope.result.message);
			}
		}
	}
	//返回设置页面
	$scope.returnPage=function(){
		 window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
	
})
//投票统计控制器
app.controller('quickVoteCountCtrl', function($rootScope,$scope,$modal,toastr) {
	$scope.voteInfo={};
	$scope.isStop=false;
	var _getvoteTitleInfo=function(){
		$scope.result=JSON.parse(execute_vote("get_voteTitleInfo"));
		if($scope.result.ret=='success'&&$scope.result.item){		
			$scope.voteInfo=$scope.result.item;
		}else{
			toastr.error($scope.result.message);			
		}		
	}
	var dom = document.getElementById("coutbar");
	var myChart = echarts.init(dom);
	$scope.voteInfoslist=[];//投票数组
	$scope.titleList=[];//标题数组
	$scope.colors = ['#ffffff','#c4d4ef','#14c629','#f4c96d','#86daf6'];
	$scope.data=[];
	var flag=true;//判断是返回还是停止
	//var mainwd=document.body.clientWidth>920?true:false;
	var _getvote=function(){
		$scope.voteInfoslist=[];
		$scope.data=[];
		$scope.titleList=[];
		$scope.result=JSON.parse(execute_vote("get_vote"));
		//console.log("哈哈哈哈哈哈哈"+JSON.stringify($scope.result))
		if($scope.result.ret=='success'){		
			$scope.voteInfoslist=$scope.result.item;
			//$scope.voteInfoslist=[{"agree":0,"disagree":0,"num":"1","program":"111","waiver":0},{"agree":0,"disagree":0,"num":"2","program":"222","waiver":0},{"agree":0,"disagree":0,"num":"3","program":"333","waiver":0},{"agree":0,"disagree":0,"num":"4","program":"444","waiver":0},{"agree":0,"disagree":0,"num":"5","program":"555","waiver":0}];
			if($scope.voteInfoslist.length>3){
				$scope.width="14%";
			}else{
				$scope.width="20%";
			}
			for(var i=0;i<$scope.voteInfoslist.length;i++){
			$scope.colors.push($scope.colors[i]);		
			$scope.titleList.push($scope.voteInfoslist[i].program);						
			//console.log("头部"+JSON.stringify($scope.voteInfoslist))		
				var item={
					name:$scope.voteInfoslist[i].program,
		            type:'bar',
		            barWidth: $scope.width,
		            itemStyle: {
			    		normal: {
					        label: {
					            show: true,
					            position: 'top',
					            textStyle: {
					                color: "#fff",
				                    fontSize: '26'
					            },
				            }
			       		}
		    		},		    		
		            data:[$scope.voteInfoslist[i].agree,$scope.voteInfoslist[i].disagree,$scope.voteInfoslist[i].waiver],
				}
				$scope.data.push(item);
				console.log("数据"+JSON.stringify($scope.data))
				
			}
			option = {
		    color: $scope.colors,
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data: $scope.titleList,
		        //data:["1","2","3","4"],
		        textStyle:{
		        	fontSize: '20',
		        	color:'#fff'
		        }
		    },
		  	/*toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },*/
		    grid: {
		    	top:'25%',
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            data : ['Agress', 'Disagree', 'Abstain'],
		            axisTick: {
		                alignWithLabel: true
		            },
		            axisLine: {
						lineStyle: {
							color: '#dcdcdc', //这里是为了突出显示加上的，可以去掉
							show: false
						}
					},
					axisLabel: {        
		                show: true,
		                textStyle: {
		                    color: '#fff',
		                    fontSize:'20',
		                }
		            },
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		          	splitLine: false, //是否显示网格线
		          	/*axisTick:{
				        show:false
				    },*/
					axisLine: {
						lineStyle: {
							color: '#dcdcdc', //这里是为了突出显示加上的，可以去掉
							show: false
						}
					},
					axisLabel: {        
		                show: true,
		                textStyle: {
		                    color: '#fff',
		                    fontSize:'26'
		                }
		            },
		        }
		    ],
		    series : $scope.data
/*		    series:[{"name":"1","type":"bar","barWidth":"14%","itemStyle":{"normal":{"label":{"show":true,"position":"top","textStyle":{"color":"#fff","fontSize":"16"}}}},"data":[0,0,0]},{"name":"2","type":"bar","barWidth":"14%","itemStyle":{"normal":{"label":{"show":true,"position":"top","textStyle":{"color":"#fff","fontSize":"16"}}}},"data":[0,0,0]},
		    {"name":"2","type":"bar","barWidth":"14%","itemStyle":{"normal":{"label":{"show":true,"position":"top","textStyle":{"color":"#fff","fontSize":"16"}}}},"data":[0,0,0]},
		    {"name":"3","type":"bar","barWidth":"14%","itemStyle":{"normal":{"label":{"show":true,"position":"top","textStyle":{"color":"#fff","fontSize":"16"}}}},"data":[0,0,0]},
		    {"name":"4","type":"bar","barWidth":"14%","itemStyle":{"normal":{"label":{"show":true,"position":"top","textStyle":{"color":"#fff","fontSize":"16"}}}},"data":[0,0,0]}]*/
			}
			//myChart.clear();
			if(option && typeof option === "object") {
				myChart.setOption(option, true);
			}
		}else{
			//myChart.clear();
			toastr.error($scope.result.message);
		}
	}
	var _init=function(){
		_getvoteTitleInfo();
		_getvote();		
	}();	
	// 获取到的是变更后的页面宽度
		window.onresize = function(){
			myChart.resize();
			mainwd=!mainwd;
		}
	
	//$scope.voteInfoslist=[{"agree":4,"disagree":0,"num":"1","program":"张三","waiver":0},{"agree":2,"disagree":1,"num":"2","program":"李四","waiver":1},{"agree":0,"disagree":0,"num":"3","program":"王五","waiver":0}]
	
	
	//刷新投票统计
	$scope.refresVote=function(){
		_getvote();
	}
	
	var _stopVoteCount=function(){
		$scope.result=JSON.parse(execute_vote("stop_vote"));
		//console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){	
			if(flag==true){
				toastr.success($scope.result.message);
				$scope.isStop=true;
			}else{
				//toastr.success($scope.result.message);
				$scope.objectUrl="../../page/answermoudle/answerCenter.html";
				window.location.href=$scope.objectUrl; 
			}
			
			
		}else{
			toastr.error($scope.result.message);
		}
	}
	
	//停止投票
	$scope.stopVoteCount=function(){
		/*$scope.result=JSON.parse(execute_vote("stop_vote"));
		//console.log(JSON.stringify($scope.result))
		if($scope.result.ret=='success'){	
			toastr.success($scope.result.message);
			$scope.isStop=true;
		}else{
			toastr.error($scope.result.message);
		}*/
		_stopVoteCount();
	}
	//返回设置页面
	$scope.returnPage=function(){
		flag=false;
		_stopVoteCount();
		 //window.location.href="../../page/answermoudle/answerCenter.html"; 
	}
})
