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
<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/main.css" id="swicth-style"/>
<script type="text/javascript">  
	function fixWidth(percent)  
	{  
	    return (document.body.clientWidth-200) * percent ;
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
    		url:'${ctx}/m/coupon/use/query', 
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
		var name = $("#c_name").val();
		var faceLimit = $("#c_face").val();
		var type = $("#c_type").val();
		//var status = $("#status").val();
		var startTime = $("#c_startTime").val();
		var endTime = $("#c_endTime").val();
		
		$('#dg').datagrid({ url:"${ctx}/m/coupon/use/query",
			queryParams:{'page':1,'rows':15,'name':name,'faceLimit':faceLimit,'type':type,
				'startTime':startTime,'endTime':endTime},
			method:"GET"});
	}
	
	function disOs(){
		var orderId = $("#os_orderId").val();
	    var productName = $("#os_name").val();  
		var osId = $('input[name="os_id_redio"]:checked').val();
		alert(osId);
		if(osId == '' || osId < 0){
			alert("请选择状态。");
			return;
		}
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/order/uos",
		    cache:false,  
		    data: {'orderId':orderId,'osId':osId,'productName':productName} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.code == 200){
		    		$("#restartDialog").dialog('close');
		    		alert("更新成功");
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
		$("#restartDialog").dialog('open');  
	}
	
	function create(){
		var name = $("#coupon_name").val();
		var faceLimit = $("#face_limit").val();
		var type = $("#coupon_type").val();
		var startTime = $('#coupon_startTime').datebox('getValue');   
		var endTime = $('#coupon_endTime').datebox('getValue');   
		alert(startTime);
		if(status < 0){
			alert("请选择状态。");
			return;
		}
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/coupon/add",
		    cache:false,  
		    data: {'name':name,'faceLimit':faceLimit,'type':type,'startTime':startTime,'endTime':endTime} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.code == 200){
		    		$("#restartDialog").dialog('close');
		    		alert("添加成功");
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
		    url: "${ctx}/m/coupon/status",
		    cache:false,  
		    data: {'id':id,'status':status} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.code == 200){
		    		$("#showOsDialog").dialog('close');
		    		alert("更新成功");
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
    	$("#restartDialog").dialog('close');
    	$("#showOsDialog").dialog('close');
    	$('#dg').datagrid({ 
    		url:'${ctx}/m/coupon/use/query', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'id',title:'ID',width:50}, 
    		{field:'userName',title:'用户',width:fixWidth(0.15),align:'right'},
    		{field:'name',title:'优惠券名称',width:fixWidth(0.15),align:'right'},
    		{field:'type',title:'类型',width:fixWidth(0.1),align:'right',formatter:function(value){
				if(value==1){
					return "柴油";
				}else if(value==2){
					return "机油";
				}else{
					return "180";
				}
				
    		}},
    		{field:'status',title:'状态',width:fixWidth(0.08),align:'right',formatter:function(value){
    			if(value==0){
    				return "未使用";
    			}else if(value==1){
    				return "已使用";
    			}else if(value==8){
    				return  "已删除";
    			}else{
    				return  "已过期";
    			}
    		}},
    		{field:'faceValue',title:'面额',width:fixWidth(0.1),align:'right'},
    		{field:'startTime',title:'有效开始时间',width:fixWidth(0.15),align:'right'},
    		{field:'endTime',title:'有效结束时间',width:fixWidth(0.15),align:'right'},
    		{field:'createTime',title:'创建时间',width:fixWidth(0.15),align:'right'}
    /* 		{field:'op',title:'操作',width:155,formatter:function(value,rowData,rowIndex){
    			var id = rowData.id;
    			var status = rowData.status;
    			var str = "";
    			if(status == 1){
    				str += ' <a href="#" onclick="updateStatus(\''+id+'\',0)">失效</a>';
    			}else{
    				str += ' <a href="#" onclick="updateStatus(\''+id+'\',1)">生效</a>';
    			}
    			return str;
    		}}  */
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
						<td width="100px"><span>优惠券名称:</span></td>
						<td width="150px"><input id="c_name" type="text" style="width: 120px"/></td>
						<td width="100px"><span>面额:</span></td>
						<td width="150px"><input id="c_face" type="text" style="width: 120px"/></td>
						<td width="100px"><span>类型:</span></td>
						<td width="150px">
							<select name="type" id="c_type" style="width: 200px">
							    <option value="-1" selected="selected">全部</option>
								<option value="1">柴油  </option>
								<option value="2">机油</option>
								<option value="3">180</option>
							</select>
						</td>
					</tr>
					<tr style="height: 40px;">
						<td width="100px"><span>开始时间:</span></td>
						<td width="150px"><input id="c_startTime" class="easyui-datebox"></input></td>
						<td width="100px"><span>结束时间:</span></td>
						<td width="150px"><input id="c_endTime" class="easyui-datebox"></input></td>
						<td colspan="4" width="100px">&nbsp;</td>
						<td colspan="2" style="text-align: right;"><button type="button" onclick="query()">查询</button></td>
					</tr>
					<tr style="height: 40px;">
				<!-- 	    <td colspan="8" style="text-align: right;"><button type="button" onclick="showCreatePage()">添加</button></td> -->
					    &nbsp;&nbsp;
					</tr>
				</table>
			</div>
		</div>
		<div class="pageColumn" style="margin-top: 50px">
		    <table id="dg"></table>
		</div>
	</div>
	<div id="restartDialog" class="easyui-dialog" title="添加优惠券" style="width: 650px; height: 380px;" >
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 5px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr style="height: 30px">
						<td style="width:30%;">
							优惠券名称：
						</td>
						<td  style="text-align:left;">
							<input id="coupon_name" name="coupon_name"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							限额-面额：
						</td>
						<td  style="text-align:left;">
							<input id="face_limit" name="face_limit"/>(格式：100-30)
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							类型：
						</td>
						<td  style="text-align:left;">
							<select id="coupon_type">
								<option value="1">柴油  </option>
								<option value="2">机油</option>
								<option value="3">180</option>
							</select>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							有效期：
						</td>
						<td  style="text-align:left;">
							<input id="coupon_startTime" class="easyui-datebox"></input>--<input id="coupon_endTime" class="easyui-datebox"></input>
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
	
	<div id="showOsDialog" class="easyui-dialog" title="分配加油站" style="width: 800px; height: 580px;" >
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 5px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr>
						<td  style="text-align:left;">
							<table id="os_dg"></table>
							<input type="hidden" id="os_orderId" name="os_orderId"/>
							<input type="hidden" id="os_name" name="os_name"/>
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