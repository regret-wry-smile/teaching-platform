angular.module('someApp', []).controller('UserCtr',function($scope){
	$scope.projectType = get_project_type(); //软件类型,S为学生端T为老师端
	$scope.linkstatusList = [];
	$scope.isImport=false;
	$scope.isBind=false;
	
	//显示loading
	$scope.showLoading=function(){
			$('body').loading({
				loadingWidth:240,
				title:'请稍等!',
				name:'test',
				discription:message,
				direction:'column',
				type:'origin',
				// originBg:'#71EA71',
				originDivWidth:40,
				originDivHeight:40,
				originWidth:6,
				originHeight:6,
				smallLoading:false,
				loadingMaskBg:'rgba(0,0,0,0.2)'
			});
			/*setTimeout(function(){
				removeLoading('test');
			},3000);*/
	}
	//移除loading
	$scope.removeLoading=function(){
		removeLoading('test');
	}

	
	/*人员列表控制器*/
	//获取学生信息
	_GETSTUDENTINFO = function(){
		var studentInfos = JSON.parse(get_student_info());
		if(studentInfos.ret == 'success'){
			$scope.studentInfos = studentInfos.item;
		}else{
			alert("获取学生信息失败!");
		}
	}
	_GETSTUDENTINFO();
	
	//获取客户端连接信息
	_GETCLIENTINFO = function(){
		$scope.linkstatusList = JSON.parse(get_online_info())
	}
	
	if($scope.projectType==true){
		_GETCLIENTINFO();
	}
	//返回
	$scope.returnPre = function(){
		to_returnPre();
	}	
	
	//提示框
	$scope.getTip =  function () {
        var dblChoseAlert = simpleAlert({
            "title":"提示:",
            "content":message,
        })
    }
	
	//导入
	$scope.startImport = function(fileName) {
			$scope.isImport=true;
			$scope.isBind=false;
			$scope.isshow=true;
			var dblChoseAlert = simpleAlert({
            "title":"上传文件",
            "content":'<input type="file" id="uploadFile" /><p id="url"></p>',
            "buttons":{
                "确定":function () {
					var filepath = document.querySelector('#uploadFile').value;
					var extStart = filepath.lastIndexOf(".");
					var ext = filepath.substring(extStart, filepath.length).toUpperCase();
					if(ext != ".XLS" && ext != ".XLSX") {
						alert("只能导入.XLSX、.XLS类型文件");
						return ;
					}else{
						start_import(filepath);
						dblChoseAlert.close();
					}
                },
                "取消":function () {
                    dblChoseAlert.close();
                }
            }
        })
	}
	//开始绑定
	$scope.startbind=function(){
		if($scope.isBind==false){
			$scope.isBind=true;
			$scope.isImport=false;
		}
		save_nobing_student(JSON.stringify($scope.studentInfos));
		start_bind();
	}
	//解除绑定
	$scope.startunbind=function(){
		$scope.isBind=false;
	}
	//关闭
	$scope.closeShell = function(){
		close_shell();
	}
	
	//刷新页面
	$scope.refresh = function(){
		if($scope.projectType==true){
			_GETCLIENTINFO();
		}else{
			_GETSTUDENTINFO();
		}
		
	}
	
	
})