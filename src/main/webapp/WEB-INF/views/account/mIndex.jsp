<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ctx}/static/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/static/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/static/jquery/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/main.css" id="swicth-style"/>
<script type="text/javascript">  
	function fixWidth(percent)  
	{  
	    return (document.body.clientWidth-200) * percent ;
	}  
	
	function deleteuser(id){
		
		if(id.length == 0){
			alert('删除错误');
			return;
		}
		
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/muser/delete",
		    cache:false,  
		    data: {'id':id} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.code == 200){
		    		$("#restartDialog").dialog('close');
		    		alert("更新成功");
		    		query();
		    	}else{
		    		alert(data.msg);  
		    	}
		    },  
		    error : function() {  
		    	alert("操作异常，请稍后再试。");  
		    }  
		});
	}
	
	function modify(id, username){
		$("#mod_name").val(username);
		$("#mod_id").val(id);
		$("#showOsDialog").dialog('open');
	}
	
	function showRestartDialog(id, status){       
        $("#update_orderId").val(id);  
        $("#os_old_status").val(status);  
        $("#restartDialog").dialog('open');  
    }  
	
	function showOsDialog(id, productName){       
        $("#os_orderId").val(id);  
        $("#os_name").val(productName);  
        $("#showOsDialog").dialog('open');  
    	$('#os_dg').datagrid({ 
    		url:'${ctx}/m/os/query', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'id',title:'ID',width:50,formatter:function(value,row,index){
    		    return "<input name='os_id_redio' type='radio' value='"+value+"'>";
    		}}, 
    		{field:'name',title:'加油站',width:200}, 
    		{field:'address',title:'地址',width:320,align:'right'},
    		{field:'cityName',title:'城市',width:100,align:'right'}
    		]] 
    	});
    } 
	
	function cancel(){       
		$("#restartDialog").dialog('close');
	}
	
	function cancelOs(){       
		$("#showOsDialog").dialog('close');
	}
	
	function query(){
		var name = $("#u_name").val();
		var faceLimit = $("#c_face").val();
		var type = $("#c_type").val();
		//var status = $("#status").val();
		var startTime = $("#c_startTime").val();
		var endTime = $("#c_endTime").val();
		var osName = $("#u_osName").val();
		name=encodeURI(name);
		osName=encodeURI(osName);
// 		$('#dg').datagrid({ url:"${ctx}/m/coupon/query",
// 			queryParams:{'page':1,'rows':15,'name':name,'faceLimit':faceLimit,'type':type,
// 				'startTime':startTime,'endTime':endTime},
// 			method:"GET"});
		$('#dg').datagrid('reload',
				{'name':name, 'osName':osName });
	}
	
	function isEmptyStr(str){
		if(typeof str == "undefined" || str==null ||
				str.trim().length == 0)
			return true;
		return false;
	}
	
	function disOs(){
		var mod_name = $("#mod_name").val();
	    var mod_pwd = $("#mod_pwd").val(); 
	    var id = $("#mod_id").val();
		
	    if(isEmptyStr(mod_name) || isEmptyStr(mod_pwd)){
	    	alert("用户名和密码不能为空。。");
	    	return;
	    }

		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/muser/updateMuser",
		    cache:false,  
		    data: {'userName':mod_name,'pwd':mod_pwd,'id':id} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.code == 200){
		    		$("#showOsDialog").dialog('close');
		    		alert("更新成功");
		    		query();
		    	}else{
		    		alert(data.msg);  
		    	}
		    },  
		    error : function() {  
		    	alert("操作异常，请稍后再试。");  
		    }  
		});
	}
	
	function showCreatePage(){
		var role = $("#user_role").val();
		$("#restartDialog").dialog('open');  
		
		if(role == 'admin' || role=='user'){
			$("#os_row").hide()
			return;
		}
// 		$('#os_dg').datagrid({ 
//     		url:'${ctx}/m/os/query', 
//     		method:'GET',
//     		queryParams:{'status':-1},
//     		fitCloumns: true , 
//     		nowrap: true , 
//     		singleSelect: true,
//     		pagination:true,//分页控件 
//     		columns:[[ 
//     		{field:'id',title:'ID',width:50,formatter:function(value,row,index){
//     		    return "<input name='os_id_redio' type='radio' value='"+value+"'>";
//     		}}, 
//     		{field:'name',title:'加油站',width:150}, 
//     		{field:'address',title:'地址',width:200,align:'right'},
//     		{field:'cityName',title:'城市',width:70,align:'right'}
//     		]] 
//     	});
		//$("#os_row").hide();
	}
	
	function create(){
		var userName = $("#user_name").val();
		var pwd = $("#user_pwd").val();
		var role = $("#user_role").val();
		var osId = $('input[name="os_id_redio"]:checked').val();  
		
		if(userName.trim().length==0 || pwd.trim().length==0  || role.trim().length==0){
			alert('请输入用户名、密码');
			return;
		}
		
		if(role !='admin' && role !='user' && osId.trim().length==0){
			alert('请选择加油站');
			return;
		}		
		
		if(status < 0){
			alert("请选择状态。");
			return;
		}
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/muser/add",
		    cache:false,  
		    data: {'userName':userName,'pwd':pwd,'role':role,'osId':osId} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.code == 200){
		    		$("#restartDialog").dialog('close');
		    		alert("添加成功");
		    		query();
		    	}else{
		    		alert(data.msg);  
		    	}
		    },  
		    error : function() {  
		    	alert("操作异常，请稍后再试。");  
		    }  
		});
	}
	
	function updateStatus(id, status){
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/muser/status",
		    cache:false,  
		    data: {'id':id,'status':status} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.code == 200){
		    		$("#showOsDialog").dialog('close');
		    		alert("更新成功");
		    		query();
		    	}else{
		    		alert(data.msg);  
		    	}
		    },  
		    error : function() {  
		    	alert("操作异常，请稍后再试。");  
		    }  
		});
	}

    $(document).ready(function(){
    	
    	$("#user_role").change(function(){
    		if(this.value !='admin' && this.value != 'user'){
    			$("#os_row").show();
    			$('#os_dg').datagrid({ 
    	    		url:'${ctx}/m/os/query', 
    	    		method:'GET',
    	    		queryParams:{'status':-1},
    	    		fitCloumns: true , 
    	    		nowrap: true , 
    	    		singleSelect: true,
    	    		pagination:true,//分页控件 
    	    		columns:[[ 
    	    		{field:'id',title:'ID',width:50,formatter:function(value,row,index){
    	    		    return "<input name='os_id_redio' type='radio' value='"+value+"'>";
    	    		}}, 
    	    		{field:'name',title:'加油站',width:150}, 
    	    		{field:'address',title:'地址',width:200,align:'right'},
    	    		{field:'cityName',title:'城市',width:70,align:'right'}
    	    		]] 
    	    	});
    		}else{
    			$("#os_row").hide();
    		}
    	});
    	
		$('#os_dg').datagrid({ 
    		url:'${ctx}/m/os/query', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'id',title:'ID',width:50,formatter:function(value,row,index){
    		    return "<input name='os_id_redio' type='radio' value='"+value+"'>";
    		}}, 
    		{field:'name',title:'加油站',width:150}, 
    		{field:'address',title:'地址',width:200,align:'right'},
    		{field:'cityName',title:'城市',width:70,align:'right'}
    		]] 
    	});
    	
    	$("#restartDialog").dialog('close');
    	$("#showOsDialog").dialog('close');
    	$('#dg').datagrid({ 
    		url:'${ctx}/m/muser/query', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'id',title:'ID',width:50}, 
    		{field:'userName',title:'用户名',width:fixWidth(0.2),align:'right'},
    		{field:'status',title:'状态',width:fixWidth(0.1),align:'right',formatter:function(value,rowData,rowIndex){
    			return value==1?"正常":"失效";
    		}},
    		{field:'role',title:'角色',width:fixWidth(0.2),align:'right',formatter:function(value,rowData,rowIndex){
    			if('admin'==value){
    				return '系统管理员 ';
    			}else if('cwgly'==value){
    				return '财务管理员';
    			}else if('kfqx'==value){
    				return '客服权限';
    			}else if('jygly'==value){
    				return '进油管理员';
    			}else if('jyzAdmin'==value){
    				return '加油站系统管理员';
    			}else if('jyzcwqx'==value){
    				return '加油站财务权限';
    			}else if('jyzjygqx'==value){
    				return '加油工权限';
    			}else{
    				return '其他';
    			}
    		}},
    		{field:'osName',title:'所属加油站',width:fixWidth(0.28),align:'right'},
    		{field:'createTime',title:'创建时间',width:fixWidth(0.2),align:'right'},
    		{field:'op',title:'操作',width:155,formatter:function(value,rowData,rowIndex){
    			var id = rowData.id;
    			var username = rowData.userName;
    			var status = rowData.status;
    			var str = "";
    			if(status == 1){
    				str += ' <a href="#" onclick="updateStatus(\''+id+'\',0)">失效</a>';
    			}else{
    				str += ' <a href="#" onclick="updateStatus(\''+id+'\',1)">生效</a>';
    			}
    			
    			str += ("|"+' <a href="#" onclick="deleteuser(\''+id+'\')">删除</a>');
    			str += ("|"+' <a href="#" onclick="modify(\''+id+'\',\''+username+'\')">修改</a>');
    			return str;
    		}} 
    		]] 
    	});
    });   
</script>
</head>

<body>
	<div id="contentWrap">
		<div class="" style="">
			<div id="coupon_query_id">
				<table>
					<tr style="height: 40px;">
						<td width="100px"><span>用户名:</span></td>
						<td width="150px"><input id="u_name" type="text" style="width: 120px"/></td>
						<td width="100px"><span>加油站:</span></td>
						<td width="150px"><input id="u_osName" type="text" style="width: 120px"/></td>
					</tr>
					<tr style="height: 40px;">
					    <td colspan="4" style="text-align: right;"><button type="button" onclick="showCreatePage()">添加</button></td>
					    &nbsp;&nbsp;
						<td colspan="4" style="text-align: right;"><button type="button" onclick="query()">查询</button></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="pageColumn" style="margin-top: 50px">
		    <table id="dg"></table>
		</div>
	</div>
	<div id="restartDialog" class="easyui-dialog" title="添加用户" style="width: 800px; height: 400px;" >
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 15px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr style="height: 30px">
						<td style="width:30%;">
							用户名：
						</td>
						<td  style="text-align:left;">
							<input id="user_name" name="user_name"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							密码：
						</td>
						<td  style="text-align:left;">
							<input id="user_pwd" name="user_pwd"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							角色：
						</td>
						<td  style="text-align:left;">
							<select id="user_role">
							    <c:choose>
								   <c:when test="${userRole=='admin'}"> 
								        <option value="admin">系统管理员</option> 
								        <option value="cwgly">财务管理员</option>
										<option value="user">客服权限</option>
										<option value="jygly">进油管理员</option>
										<option value="jyzAdmin">加油站系统管理员</option>      
								   </c:when>
								   <c:otherwise> 
										 <option value="jyzAdmin">加油站系统管理员</option>
										<option value="jyzcwqx">加油站财务权限</option>
										<option value="jyzjygqx">加油工权限</option>
								   </c:otherwise>
								</c:choose>
							</select>
						</td>
					</tr>
					<tr style="height: 30px" id="os_row">
						<td style="width:30%;">
							所属加油站：
						</td>
						<td  style="text-align:left;">
							<table id="os_dg" style="height: 200px"></table>
						</td>
					</tr>
				</table>
				<div style="text-align:right;margin-right:30px;margin-top: 50px">
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'ope-finish'" onclick="create()">确定</a>
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'ope-cancel'" onclick="cancel()">取消</a>
				</div>				
			</div> 		
		</div>
	</div>
	
	<div id="showOsDialog" class="easyui-dialog" title="更改信息" style="width: 800px; height: 400px;" >
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 5px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr style="height: 30px">
						<td style="width:30%;">
							用户名：
						</td>
						<td  style="text-align:left;">
							<input id="mod_name" name="user_name"/>
							<input id="mod_id" name="mod_id" style="display:none"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							密码：
						</td>
						<td  style="text-align:left;">
							<input id="mod_pwd" name="user_pwd"/>
						</td>
					</tr>
				</table>
				<div style="text-align:right;margin-right:30px;margin-top: 50px">
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'ope-finish'" onclick="disOs()">确定</a>
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'ope-cancel'" onclick="cancelOs()">取消</a>
				</div>				
			</div> 		
		</div>
	</div>
</body>
</html>