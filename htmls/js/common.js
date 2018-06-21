
    $(function() {
           // $("#mySelect").select(); 不传参数可以这样写
           /* $(".mySelect").select({
                width: "3rem",
              	showSearch: true,        //是否启用搜索框
              	listMaxHeight:"1.50rem",     //生成的下拉列表最大高度,
              	searchContent: "关键词搜索" ,  //搜索框的默认提示文字  
              	themeColor:"#e2e2e2",
              	fontColor: "#3e3e3e",        //字体颜色
            });*/
            //可选参数,不填就是默认值
            //width: "180px",            //生成的select框宽度
            //listMaxHeight:"200px",     //生成的下拉列表最大高度
              //themeColor: "#00bb9c",    //主题颜色
             // fontColor: "#000",        //字体颜色
            //fontFamily: "'Helvetica Neue', arial, sans-serif",    //字体种类
            //fontSize:"15px",           //字体大小
             // showSearch: true,        //是否启用搜索框
            //rowColor:"#fff",          //行原本的颜色
            //rowHoverColor: "#0faf03", //移动选择时，每一行的hover底色
            //fontHoverColor: "#fff",   //移动选择时，每一行的字体hover颜色
            //mainContent: "请选择",    //选择显示框的默认文字
            //searchContent: "关键词搜索"   //搜索框的默认提示文字  
            $('.single').multiselect({multiple:false,selectedHtmlValue:'单选'});
        });
