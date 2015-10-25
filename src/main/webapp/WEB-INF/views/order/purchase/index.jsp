<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
        $("#update_orderId").val(id);  
        $("#os_old_status").val(status);  
        $("#restartDialog").dialog('open');  
    }  
	
	function showOsDialog(id, productName){       
        $("#os_orderId").val(id);  
        $("#os_name").val(productName);  
        $("#showOsDialog").dialog('open');  
    	$('#os_dg').datagrid({ 
    		url:'${ctx}/m/os/query/purchase', 
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
		var orderNo = $("#order_no").val();
		var osName = $("#o_s_name").val();
		var owStatus = $("#o_w_status").val();
		var priceRegion = $("#o_price_region").val();
		var startTime = $('#o_startTime').datebox('getValue');   
		var endTime = $('#o_endTime').datebox('getValue');  
		
		osName=encodeURI(osName);
		
		$('#dg').datagrid({ url:"${ctx}/m/order/query/purchase",
			queryParams:{'page':1,'rows':15,'orderNo':orderNo,'osName':osName,
				'owStatus':owStatus,'startTime':startTime,'endTime':endTime,'priceRegion':priceRegion},
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
    		url:'${ctx}/m/order/query/purchase', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'orderNo',title:'订单号',width:300}, 
    	/* 	{field:'productName',title:'油品',width:200,align:'right'}, */
    		{field:'num',title:'数量',width:fixWidth(0.1),align:'right'},
    		{field:'status',title:'状态',width:fixWidth(0.06),align:'right'},
    		{field:'amount',title:'总价',width:fixWidth(0.1),align:'right'},
    		{field:'userName',title:'用户名',width:fixWidth(0.20),align:'right'},
    		{field:'osName',title:'加油站',width:fixWidth(0.20),align:'right'},
    	/* 	{field:'bookTime',title:'预约时间',width:fixWidth(0.1),align:'right'},
    		{field:'bookAddr',title:'预约地址',width:fixWidth(0.15),align:'right'}, */
    		{field:'createTime',title:'创建时间',width:200,align:'right'}
/*     		{field:'op',title:'操作',width:155,formatter:function(value,rowData,rowIndex){
    			var id = rowData.id;
    			var status = rowData.status;
    			var productName = rowData.productName;
    			var str = "";
    			str += '<a href="#" onclick="showRestartDialog(\''+id+'\','+status+')">设置状态</a>';
    			if(status == 11){
    				str += ' | <a href="#" onclick="showOsDialog(\''+id+'\',\''+productName+'\')">分配加油站</a>';
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
			<div id="coupon_query_id" style="position: relative;">
				<table>
					<tr style="height: 40px;">
						<td width="100px"><span>订单号:</span></td>
						<td width="150px"><input id="order_no" type="text" style="width: 120px"/></td>
							<!-- <td width="100px"><span>用户名/手机号:</span></td> -->
						<!-- <td width="150px"><input id="user_name" type="text" style="width: 120px"/></td> -->
					<!-- 	<td width="100px"><span>加油站:</span></td>
						<td width="150px"><input id="o_s_name" type="text" style="width: 120px"/></td> -->
					<!-- 	<td width="100px"><span>地区:</span></td>
						<td width="150px"><input id="o_area" type="text" style="width: 120px"/></td> -->
						<td width="100px"><span>订单状态:</span></td>
						<td width="100px">
							<select name="select" id="o_w_status" style="width: 100px">
							    <option value="-1" selected="selected">全部</option>
							    <option value="1">未付款</option>
								<option value="2">已付款</option>
								<option value="3">已交货</option>
							</select>
						</td>
						<td width="100px"><span>开始时间:</span></td>
						<td width="150px"><input id="o_startTime" class="easyui-datebox"></input></td>
						<td width="100px"><span>结束时间:</span></td>
						<td width="150px"><input id="o_endTime" class="easyui-datebox"></input></td>
						<td colspan="4" width="100px">&nbsp;</td>
					</tr>
						<tr style="width: 40px;">
						<td><span>价格区间:</span></td>
						<td>
							<select name="select" id="o_price_region" style="width: 125px;">
							    <option value="-1" selected="selected">全部</option>
							    <option value="4">0-59万</option>
								<option value="5">59-100万</option>
								<option value="6">100万以上</option>
							</select>
							</td>
							
								   <shiro:hasAnyRoles name="jyzAdmin,jyzcwqx">
							<td width="100px"><span>加油站:</span></td>
						<td width="150px"><input id="o_s_name" type="text" style="width: 120px"/></td> 
							</shiro:hasAnyRoles>
							
						</tr>
						
				</table>
					<div style="width: 80px;height: 25px;position: absolute;right: 50px;top: 50px;">
					<button type="button" onclick="query()">查询</button>
					</div>
			</div>
		</div>
		<div class="pageColumn" style="margin-top: 30px">
		    <table id="dg"></table>
		</div>
	</div>
	<div id="restartDialog" class="easyui-dialog" title="更新状态" style="width: 400px; height: 180px;" >
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 5px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr>
						<td style="width:30%;">
							选择状态：
						</td>
						<td  style="text-align:left;">
							<select id="update_order_status" style="width: 200px">
							    <option value="-1">请选择</option>
								<option value="0">等待付款中</option>
								<option value="1">付款成功</option>
								<option value="2">付款失败</option>
								<option value="3">过期</option>
								<option value="4">撤销成功</option>
								<option value="5">退款中</option>
								<option value="6">退款成功</option>
								<option value="7">退款失败</option>
								<option value="8">部分退款成功</option>
								<option value="11">新建预约订单</option>
								<option value="12">后台加油站已确定-等待付款中</option>
								<option value="99">删除</option>
							</select>
							<input type="hidden" id="update_orderId" name="update_orderId"/>
							<input type="hidden" id="update_old_status" name="update_old_status"/>
						</td>
					</tr>
				</table>
				<div style="text-align:right;margin-right:30px;margin-top: 50px">
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'ope-finish'" onclick="updateStatus()">确定</a>
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