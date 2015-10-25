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
	
	function showRestartDialog(id, status){       
        $("#restartDialog").dialog('open');  
    }  
	
	function showOsDialog(id, status){       
        $("#update_orderId").val(id);  
        $("#update_old_status").val(status);  
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
    		    return "<input name='os_id_redio' type='radio'>";
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
		var phone = $("#q_user_phone").val();
		var startTime = $("#startTime").datebox('getValue');
		var endTime = $("#endTime").datebox('getValue');
		var amountStart = $("#c_charge_start").val();
		var amountEnd = $("#c_charge_end").val();
		var cname = $("#c_name").val();		
		
		if(!isMoney(amountStart) || !isMoney(amountEnd)){
			alert('请输入正确的金额');
			return;
		}
		$("#dgsumdiv").hide();
		$("#dgdiv").show();
		$('#dg').datagrid({ url:"${ctx}/m/order/rQuery",
			queryParams:{'page':1,'rows':15,'phone':phone,
				'startTime':startTime,'endTime':endTime,
				'amountStart':amountStart,
				'amountEnd':amountEnd,
				'cname':cname},
			method:"GET"});
	}
	
	function querySum(){
		var phone = $("#q_user_phone").val();
		var startTime = $("#startTime").datebox('getValue');
		var endTime = $("#endTime").datebox('getValue');
		var amountStart = $("#c_charge_start").val();
		var amountEnd = $("#c_charge_end").val();
		var cname = $("#c_name").val();		
		
		if(!isMoney(amountStart) || !isMoney(amountEnd)){
			alert('请输入正确的金额');
			return;
		}
		
		$("#dgdiv").hide();
		$("#dgsumdiv").show();
		
		$('#dgsum').datagrid({ url:"${ctx}/m/order/rQuerySum",
			queryParams:{'page':1,'rows':15,'phone':phone,
				'startTime':startTime,'endTime':endTime,
				'amountStart':amountStart,
				'amountEnd':amountEnd,
				'cname':cname},
			method:"GET"});
	}
	
	function isMoney(num){
		if(num.trim().length == 0){
			return true;
		}
		if(/^[0-9]+(\.[0-9])?[0-9]*$/.test(num)){
			if(num == 0){
				return false;
			}
			return true;
		}
		return false;
	}
	
	function addRecharge(){
		var phone = $("#user_phone").val();
		var amount = $("#user_amount").val();
		
		if( !isMoney(amount)){
			alert("请输入正确的金额");
			return false;
		}
		
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/order/radd",
		    cache:false,  
		    data: {'phone':phone,'amount':amount} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.code == 200){
		    		$("#restartDialog").dialog('close');
		    		alert("充值成功");

					$("#dgsumdiv").hide();
					$("#dgdiv").show();
					$('#dg').datagrid({ url:"${ctx}/m/order/rQuery",
						queryParams:{'page':1,'rows':15},
						method:"GET"});
		    	}else{
		    		alert(data.msg);  
		    	}
		    },  
		    error : function() {  
		    	alert("操作异常，请稍后再试。");  
		    }  
		});
	}
	
	function updateStatus(){
		var orderId = $("#update_orderId").val();
		var status = $("#update_order_status").val();
		var oldStatus = $("#update_old_status").val();
		if(status < 0){
			alert("请选择状态。");
			return;
		}
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/order/uStatus",
		    cache:false,  
		    data: {'orderId':orderId,'status':status,'oldStatus':oldStatus} ,
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

    $(document).ready(function(){
    	$("#restartDialog").dialog('close');
    	$("#showOsDialog").dialog('close');
    	$('#dg').datagrid({ 
    		url:'${ctx}/m/order/rQuery', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'id',title:'ID',width:fixWidth(0.06)}, 
    		{field:'phone',title:'手机号',width:fixWidth(0.18)}, 
    		{field:'username',title:'用户名',width:fixWidth(0.18)}, 
    		{field:'shipname',title:'船舶名',width:fixWidth(0.18)}, 
    		{field:'amount',title:'金额',width:fixWidth(0.1),align:'right'},
    		{field:'createTime',title:'充值时间',width:fixWidth(0.20),align:'right'}
    		]] 
    	});
    	
    	$("#dgsum").datagrid({ 
    		url:'${ctx}/m/order/rQuery', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
//     		{field:'id',title:'ID',width:fixWidth(0.06)}, 
    		{field:'phone',title:'手机号',width:fixWidth(0.20)}, 
    		{field:'username',title:'用户名',width:fixWidth(0.20)}, 
    		{field:'shipname',title:'船舶名',width:fixWidth(0.20)}, 
    		{field:'amount',title:'金额',width:fixWidth(0.20),align:'right'}
//     		{field:'createTime',title:'充值时间',width:fixWidth(0.12),align:'right'}
    		]] 
    	});
    	
    	$("#dgsumdiv").hide();
    });   
</script>
</head>

<body>
	<div id="contentWrap">
		<div class="" style="">
			<div id="coupon_query_id">
				<table>
					<tr style="height: 40px;">
						<td width="100px"><span>手机号:</span></td>
						<td width="150px"><input id="q_user_phone" type="text" style="width: 120px"/></td>
						<td width="100px"><span>开始时间:</span></td>
						<td width="150px"><input id="startTime" class="easyui-datebox"></input></td>
						<td width="100px"><span>结束时间:</span></td>
						<td width="150px"><input id="endTime" class="easyui-datebox"></input></td>
						<td colspan="4" width="100px">&nbsp;</td>
					</tr>
					<tr style="height: 40px;">
						<td width="100px"><span>充值金额范围:</span></td>
						<td width="150px"><input id="c_charge_start" type="text" style="width: 120px"/></td>
						<td width="100px"><span>--</span></td>
						<td width="150px"><input id="c_charge_end" type="text" style="width: 120px" ></input></td>
						<td width="100px"><span>船舶名称:</span></td>
						<td width="150px"><input id="c_name" type="text" style="width: 120px"></input></td>
						<td colspan="4" width="100px">&nbsp;</td>
					</tr>
					<tr style="height: 40px;">
					</tr>
					<tr style="height: 40px;">
					    <td colspan="8" style="text-align: right;"><button type="button" onclick="showRestartDialog()">充值</button></td>
						<td colspan="8" style="text-align: right;"><button type="button" onclick="query()">查询</button></td>
						<td colspan="8" style="text-align: right;"><button type="button" onclick="querySum()">统计查询</button></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="pageColumn" style="margin-top: 50px" id='dgdiv'>
		    <table id="dg"></table>
		</div>
		<div class="pageColumn" style="margin-top: 50px" id='dgsumdiv'>
		    <table id="dgsum"></table>
		</div>
	</div>
	<div id="restartDialog" class="easyui-dialog" title="用户充值" style="width: 500px; height: 250px;" >
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 5px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr style="height: 30px">
						<td style="width:30%;">
							手机号：
						</td>
						<td  style="text-align:left;">
							<input id="user_phone" name="user_phone"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							金额：
						</td>
						<td  style="text-align:left;">
							<input id="user_amount" name="user_amonut"/>
						</td>
					</tr>
				</table>
				<div style="text-align:right;margin-right:30px;margin-top: 50px">
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'ope-finish'" onclick="addRecharge()">确定</a>
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
							<input type="hidden" id="os_orderId" name="update_orderId"/>
							<input type="hidden" id="os_old_status" name="update_old_status"/>
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