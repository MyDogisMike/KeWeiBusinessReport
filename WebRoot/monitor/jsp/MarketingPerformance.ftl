<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>营销绩效监控</title>
		<link rel="stylesheet" href="${request.contextPath}/monitor/themes/gray/easyui.css" />
		<script type="text/javascript" src="${request.contextPath}/monitor/js/jquery.min.js"></script>
		<script type="text/javascript"
			src="${request.contextPath}/monitor/js/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="${request.contextPath}/monitor/js/easyui-lang-zh_CN.js"></script>
	</head>
	<style>
#condition_form select {
	width: 10%;
	margin-right: 10px;
}

#condition_div {
	border: solid 1px aqua;
	padding: 0px 0px 60px 20px;
}

#dataShow_div {
	border: solid 1px aqua;
	padding: 0px 0px 20px 0px;
}

#table_div {
	width: 100%;
}

.color{
	background-color: #E6E7E7;
}

.datagrid-header-inner,.datagrid-header-rownumber {
	background-color: #00CCFF;
}

td.datagrid-header-over:hover,.datagrid-header-rownumber:hover {
	background-color: #00CCFF;
}

.datagrid-header-row td {
	border-color: black;
	border-style: solid;
}

.datagrid-wrap {
	border-color: black;
}

button {
	margin-left: 20px;
}
</style>
	<body>
	<input type="hidden" value="${roleInfo}" id="roleInfo" />
	<input type="hidden" value="${isGroup}" id="isGroup" />
		<div id="container_div">
			<div id="condition_div">
				<h3>
					<font color=#5A5AFF>>>营销绩效指标查询</font>
				</h3>
				<form id="condition_form" method="post">
					<font color=red>*</font>
					<label>
						所属中心
					</label>
					<select required="true" name="" id="center" class="easyui-combobox" editable="false">
						<option value=""></option>
						
					</select>

					<label>
						数据业务类型
					</label>
					<select name="" id="ywType" class="easyui-combobox" editable="false">
						
					</select>

					<label>
						组别
					</label>
					<select name="" id="group" class="easyui-combobox" editable="false">
						<option value=""></option>
						
					</select>

					<label>
						营销员
					</label>
					<select class="easyui-combobox" name="" id="user">
						<option value=""></option>
					</select>

					<button type="button" id="searchBtn">
						查询
					</button>
					<button type="button" id="resetBtn">
						重置
					</button>
					<button type="button" id="exportBtn">
						导出
					</button>
					<button type="button" id="dialogBtn">
						报表口径说明
					</button>
				</form>
			</div>
			<div id="dataShow_div">
				<h3>
					<font color=#5A5AFF>>>营销绩效指标</font>
				</h3>
				<div id="table_div">
					<table id="data_table"></table>
				</div>
			</div>
		</div>
		
		<div style="display: none;">
			<div id="explainDialog">
				<table>
					<tr>
						<th>名称</th><th>定义</th><th>备注</th>
					</tr>
					<tr>
						<td>所属中心 </td><td>营销员所在的中心名称</td><td>营销员所在的中心名称</td>
					</tr>
					<tr class="color">
						<td>数据业务类型</td><td>派发数据所属业务类型</td><td>数据业务类型</td>
					</tr>
					<tr>
						<td>组别 </td><td>营销员所在的组别名称</td><td>营销员所在的组别名称</td>
					</tr>
					<tr class="color">
						<td>营销员工号</td><td>营销员工号</td><td>营销员工号</td>
					</tr>
					<tr>
						<td>营销员姓名</td><td>营销员姓名</td><td>营销员姓名</td>
					</tr>
					<tr class="color">
						<td>批核收入</td><td>业务工单批核时间在查询周期内批核的收入（含主营+交叉），如涉及批核结果数据流转则将金额统计入数据同步当天</td><td>∑线上对应期数已批核成交金额*期数*基准费率  </td>
					</tr>
					<tr>
						<td>主营成功受理量</td><td>业务工单保存时间在查询周期内成功受理的数据量</td><td>主营成功受理量</td>
					</tr>
					<tr class="color">
						<td>主营成功批核金额</td><td>业务工单批核时间在查询周期内成功批核的金额，如涉及批核结果数据流转则将金额统计入数据同步当天</td><td>主营成功批核金额</td>
					</tr>
					<tr>
						<td>18（24、36）期批核金额</td><td>业务工单批核时间在查询周期内成功批核18（24、36）期的金额，如涉及批核结果数据流转则将金额统计入数据同步当天</td><td>18（24、36）期批核金额</td>
					</tr>
					<tr class="color">
						<td>交叉EPP批核金额</td><td>业务工单批核时间在查询周期内交叉账单成功批核的金额，如涉及批核结果数据流转则将金额统计入数据同步当天</td><td>交叉EPP批核金额</td>
					</tr>
					<tr>
						<td>交叉EPPC批核金额</td><td>业务工单批核时间在查询周期内交叉EPPC成功批核的金额，如涉及批核结果数据流转则将金额统计入数据同步当天</td><td>交叉EPPC批核金额</td>
					</tr>
					<tr class="color">
						<td>交叉大额EPPC批核金额</td><td>业务工单批核时间在查询周期内交叉大额EPPC成功批核的金额，如涉及批核结果数据流转则将金额统计入数据同步当天</td><td>交叉大额EPPC批核金额</td>
					</tr>
					<tr>
						<td>自动绑定EPP量</td><td>自动绑定EPP业务登记时间在查询周期内的登记量</td><td>自动绑定EPP量</td>
					</tr>
					<tr class="color">
						<td>自动绑定账单分期量</td><td>自动绑定账单分期业务登记时间在查询周期内的登记量</td><td>自动绑定账单分期量</td>
					</tr>
					<tr>
						<td>接通通话总时长</td><td>通话结束时间在查询周期内外呼接通后的总通话时长（不含振铃、不含话后满意度时长）</td><td>接通通话总时长</td>
					</tr>
				</table>
			</div>
		</div>
	</body>
	<script>
		$(function(){
			$.post("../dataview/getCenterList.action",{},function(data){
				var htmlStr = "<option value=''></option>";

				for(var i = 0; i < data.length; i++){
					htmlStr += "<option value='" + data[i].id + "'>" + data[i].text + "</option>";
				}

				$("#center").empty();
				$("#center").html(htmlStr);
				$("#center").combobox({
					onChange:function(newValue){	//newValue新改变的值
						$.ajax({
					         type : "post",  
					         url : "../dataview/getGroupList.action",  
					         data : "centerId=" + newValue,  
					         async : false,  
					         success : function(data){
					         	var htmlStr = "<option value=''></option>";
			
								for(var i = 0; i < data.length; i++){
									htmlStr += "<option value='" + data[i].id + "'>" + data[i].text + "</option>";
								}
				
								$("#group").empty();
								$("#group").html(htmlStr);
								$("#group").combobox({
									onChange:function(newValue){	//newValue新改变的值
										$.post("../dataview/getUserList.action",{groupId:newValue},function(data){
											var htmlStr = "<option value=''></option>";
				
											for(var i = 0; i < data.length; i++){
												htmlStr += "<option value='" + data[i] + "'>" + data[i] + "</option>";
											}
							
											$("#user").empty();
											$("#user").html(htmlStr);
											$("#user").combobox();
											$(".combobox-item").css("height", "20px");
										});
									}
								});
								$(".combobox-item").css("height", "20px");
					         }  
					     }); 
			        }
			        
				});
				$(".combobox-item").css("height", "20px");
				//如果是小组长，则只能查询该小组的信息
				var roleInfo = $("#roleInfo").val();
				var isGroup = $("#isGroup").val();
				if(isGroup == "yes"){
					var arr = roleInfo.split("_");
					if(arr.length > 1 && arr[0] != "" && arr[1] != ""){
						$('#center').combobox('select',arr[0]);
						$("#center").combobox('readonly',true);
						$("#center").next().find("input").css("background-color", "#E6E7E7");
						$('#group').combobox('select',arr[1]);
						$("#group").combobox('readonly',true);
						$("#group").next().find("input").css("background-color", "#E6E7E7");
						$("#exportBtn").hide();
					}
				}
			});
			
			var defultlist = [
					{ "id": "", "text": ""},
					{ "id": "EPP", "text": "EPP"},
					{ "id": "账单分期", "text": "账单分期"},
					{ "id": "大额EPPC", "text": "大额EPPC"},
					{ "id": "EPPC", "text": "EPPC"},
					{ "id": "备用金", "text": "备用金"},
				];
				
			$('#ywType').combobox({
				url: null,
		        valueField: 'id',
		        textField: 'text',
		        editable: false,
		        data: defultlist
			});  
			
			$(".combobox-item").css("height", "20px");

			$("select").combobox({
				onHidePanel: function() {  
			            var valueField = $(this).combobox("options").valueField;  
			            var val = $(this).combobox("getValue");  //当前combobox的值  
			            var allData = $(this).combobox("getData");   //获取combobox所有数据  
			            var result = true;      //为true说明输入的值在下拉框数据中不存在  
			            for (var i = 0; i < allData.length; i++) {  
			                if (val == allData[i][valueField]) {  
			                    result = false;  
			                    break;  
			                }  
			            }  
			            if (result) {  
			                $(this).combobox("clear");  
			            }  
			      
			        },
			        onShowPanel: function () {
			        	//alert("test");
			            var v = $(this).combobox('panel')[0].childElementCount;
			            if (v <= 10) {
			                $(this).combobox('panel').height("auto");
			            } else {
			                $(this).combobox('panel').height(200);
			            }
			        }
			});
			$(".combobox-item").css("height", "20px");
		
			$('#data_table').datagrid({
				height : "400px",
				//width: "700px",
				url : '',
				method : 'POST',
				striped : true,
				nowrap : true,
				rownumbers : false,
				// singleSelect : true,
				showHeader : true,
				showFooter : false,
				loadMsg : '努力展开中...',
				fitColumns : true,
				scrollbarSize:0,
				checkOnSelect : true,
				onClickRow : function(rowIndex, rowData) {
					$(this).datagrid('unselectRow', rowIndex);
				},
				columns : [ [ {
					field : 'center',
					title : '所属中心',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 70,
				}, {
					field : 'ywType',
					title : '数据业务类型',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 90
				}, {
					field : 'group',
					title : '组别',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 35
				}, {
					field : 'userName',
					title : '营销员工号',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				}, {
					field : 'userRealName',
					title : '营销员姓名',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 70
				}, {
					field : 'approveMoney',
					title : '批核收入',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 60
				}, {
					field : 'mainAcceptNum',
					title : '主营成功受理量',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'mainApproveMoney',
					title : '主营成功批核金额',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'approveMoney18',
					title : '18期批核金额',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'approveMoney24',
					title : '24期批核金额',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'approveMoney36',
					title : '36期批核金额',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'crossEPPApproveMoney',
					title : '交叉EPP批核金额',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'crossBillApproveMoney',
					title : '交叉账单批核金额',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'crossEPPCApproveMoney',
					title : '交叉EPPC批核金额',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'crossBigEPPCApproveMoney',
					title : '交叉大额EPPC批核金额',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'autoBindEPPNum',
					title : '自动绑定EPP量',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'autoBindBillNum',
					title : '自动绑定账单分期量',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				},{
					field : 'communicateTotleTime',
					title : '接通通话总时长',
					align : 'center',
					sortable : false,
					resizable : true,
					width : 80
				}
				] ],
				pagination : true,
				pageSize : 10,
				pageList : [ 10, 15, 20 ],
				pageNumber : 1,
				pagePosition : 'bottom',
				remoteSort : false,
		
				onClickRow : function(rowIndex, rowData) {
		
				}
			});
			
			//报表口径说明弹窗
			$("#explainDialog").dialog({
				title: "业绩监控报表口径说明",
				top: 50,
				width: 1080,
				height: 'auto',
				resizable: true,
				modal: true,
				closed: true,
				//href: '/YKPathology/EntryManagement',
				buttons:[{
					text:'关闭',
					//iconCls:'icon-sqy-cancel',
					handler:function(){$('#explainDialog').dialog('close');}
				}]
			});
			
			var t;	//定时器变量
			//判断后台是否有线程空余，如果有则将按钮变为可点击状态，停止定时器
			function isLeisure(){
				$.post("../dataview/isLeisure.action",{},function(data){
		    		if(data == true){
		    			$("#searchBtn").attr('disabled',false);
				    	$("#exportBtn").attr('disabled',false);
				    	window.clearInterval(t); 
		    		}
		    	});
			}
			
			$("#searchBtn").click(function(){
				if($('#center').combobox('getValue')==''){
					$.messager.alert('提示','请选择所属中心');
					return;
				}
				$("#searchBtn").attr('disabled',true);
				$("#exportBtn").attr('disabled',true);
			//alert(window.location.href);
				$('#data_table').datagrid({
				    url:'../dataview/getMarketingPerformance.action',
				    queryParams:{
				       center:$('#center').combobox('getValue'),
				        group:$('#group').combobox('getValue'),
				        ywType:$('#ywType').combobox('getValue'),
				        userName:$('#user').combobox('getValue')
				    },onLoadSuccess: function(data){
			            $("#searchBtn").attr('disabled',false);
				    	$("#exportBtn").attr('disabled',false);
				    	//将easyUIdatagrid工具栏下显示多少到多少条数据隐藏
				    	$('.pagination-info').hide(); 
				    	$('.pagination-page-list').hide();
			        },
				    onLoadError:function(data){	//数据加载失败函数，将按钮变为不可点击，防止重复提交
				    	$.messager.alert('提示','现在查询的人数过多，请稍后再试');
				    	$("#searchBtn").attr('disabled',true);
				    	$("#exportBtn").attr('disabled',true);
				    	//启动定时器，每一秒去查看是否有线程空余
				    	t = window.setInterval(isLeisure,1000); 
				    	
				    }
				});

			});
			
			$("#resetBtn").click(function(){
				
				$('#ywType').combobox('clear');
				$('#user').combobox('clear');//清空选中项
				if(!$("#exportBtn").is(":hidden")){
					$('#center').combobox('select','');
					$('#group').combobox('select','');
					$('#user').combobox('loadData', {});//清空option选项  
				}
				$('#data_table').datagrid('loadData',{total:0,rows:[]})
			});
			
			$("#dialogBtn").click(function(){
				$('#explainDialog').dialog('open');
			});
			
			//导出excel表格按钮的点击事件
			$('#exportBtn').click(function(){
				if($('#center').combobox('getValue')==''){
					$.messager.alert('提示','请选择所属中心');
					return;
				}
		        $('#condition_form').form({
		            url : '../dataview/exportMarketingPerformanceExcel.action',
		            dataType: 'text',
		            queryParams:{
				        center:$('#center').combobox('getValue'),
				        group:$('#group').combobox('getValue'),
				        ywType:$('#ywType').combobox('getValue'),
				        userName:$('#user').combobox('getValue')
				    },success:function(data){
		                if("wait" == data){
		                	$.messager.alert('提示','现在查询的人数过多，请稍后再试');
					    	$("#searchBtn").attr('disabled',true);
					    	$("#exportBtn").attr('disabled',true);
					    	//启动定时器，每一秒去查看是否有线程空余
					    	t = window.setInterval(isLeisure,1000); 
		                }
		            },error:function(data){
		                $.messager.alert('提示','导出数据失败');
		            }
		        });
		        $('#condition_form').submit();
		        $.messager.alert('提示','导出报表需要几分钟，请稍候，请勿重复点击');
			});
			
		});
	</script>
</html>