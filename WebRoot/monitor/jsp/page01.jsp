<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>营销过程监控</title>
		<link rel="stylesheet" href="${ctx}/monitor/themes/gray/easyui.css" />
		<script type="text/javascript" src="${ctx}/monitor/js/jquery.min.js"></script>
		<script type="text/javascript"
			src="${ctx}/monitor/js/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="${ctx}/monitor/js/easyui-lang-zh_CN.js"></script>
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
	padding: 0px 0px 20px 20px;
}

#table_div {
	width: 1200px;
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
</style>
	<body>
		<div id="container_div">
			<div id="condition_div">
				<h3>
					<font color=#5A5AFF>>>营销过程指标查询</font>
				</h3>
				<form id="condition_form">
					<font color=red>*</font>
					<label>
						所属中心
					</label>
					<select name="">
						<option value=""></option>
						<option value="">
							广州
						</option>
						<option value="">
							上海
						</option>
					</select>

					<label>
						数据业务类型
					</label>
					<select name="">
						<option value=""></option>
						<option value="">
							业务类型一
						</option>
						<option value="">
							业务类型二
						</option>
					</select>

					<label>
						组别
					</label>
					<select name="">
						<option value=""></option>
						<option value="">
							组别一
						</option>
						<option value="">
							组别二
						</option>
					</select>

					<label>
						营销员
					</label>
					<select name="">
						<option value=""></option>
						<option value="">
							营销员一
						</option>
						<option value="">
							营销员二
						</option>
					</select>

					<button type="button">
						查询
					</button>
					<button type="button">
						重置
					</button>
					<button type="button">
						导出
					</button>
					<button type="button">
						报表口径说明
					</button>
				</form>
			</div>
			<div id="dataShow_div">
				<h3>
					<font color=#5A5AFF>>>营销过程指标查询</font>
				</h3>
				<div id="table_div">
					<table id="data_table"></table>
				</div>
			</div>
		</div>
	</body>
	<script>
		$(function(){
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
					field : 'billProject',
					title : '开单项目',
					align : 'center',
					sortable : false,
					resizable : false,
					width : 70,
				}, {
					field : 'billNum',
					title : '开单数量',
					align : 'center',
					sortable : false,
					resizable : false,
					width : 80
				}, {
					field : 'sampleNum',
					title : '样本数量',
					align : 'center',
					sortable : false,
					resizable : false,
					width : 50
				}, {
					field : 'projectNum',
					title : '所含单项数量',
					align : 'center',
					sortable : false,
					resizable : false,
					width : 80
				}, {
					field : 'fee',
					title : '标准收费',
					align : 'center',
					sortable : false,
					resizable : false,
					width : 100
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
		});
		</script>
</html>