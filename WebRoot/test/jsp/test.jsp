<%@page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>电营发卡业务审核率统计</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script src="${ctx}/monitor/js/jquery.min.js"></script>
		<script src="${ctx}/monitor/js/echarts.js"></script>
		<script src="${ctx}/monitor/js/step-jquery-dc.js"></script>
	</head>

	<body>
		<table>
		<tr>
		<td>
		手动执行时间：
		<input type="text" name="begintime" id="begintime"  value="" >
		<input type="button"  value="提&nbsp;&nbsp;交" onclick="test()">
		</td>
		</tr>
		</table>
		
		
	</body>
	<script>
	 function RQcheck(RQ) {
        var date = RQ;
        var result = date.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);//验证日期格式为2017-10-10
        if (result == null)
            return false;
	        var d = new Date(result[1], result[3] - 1, result[4]);
	        return (d.getFullYear() == result[1] && (d.getMonth() + 1) == result[3] && d.getDate() == result[4]);
     }
	
	function test(){
	    var temptime=$("#begintime").val();
	    var time=temptime.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3");
        console.log(time);
	    if (!RQcheck(time)) {
                alert("请输入正确的日期格式【20171010】");
                return false;
           }
	    if(window.confirm('数据库中'+time+'已有的记录是否已经删除')){
                 $.ajax( {
					type : 'POST',
					url : "${ctx}/test1/tasktest.action",
					dataType : "json",
					async : true,
					data:{begintime:$("#begintime").val()},
					success : function(obj) {
						 if(obj.msg){  
			                alert("修改成功");  
			                $("#begintime").val("");
			            }else{  
			                alert("修改失败，失败原因【" + obj.msg + "】");  
			            }  
					},
					error:function(obj){  
		               alert(obj.msg);  
		            } 
				});
              }else{
                 return false;
           }
	}
</script>
</html>