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

<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/uploadify.css" id="swicth-style"/>
<script type="text/javascript" src="${ctx}/static/jquery/swfobject.js"></script>
<script type="text/javascript" src="${ctx}/static/jquery/jquery.uploadify.js"></script>
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
    		url:'${ctx}/m/coupon/query', 
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
		var orderNo = $("#oo_orderNo").val();
		var status = $("#oo_status").val();
		var startTime = $("#oo_startTime").val();
		var endTime = $("#oo_endTime").val();
		name=encodeURI(name);
		$('#dg').datagrid({ url:"${ctx}/m/os/jyzOQuery",
			queryParams:{'page':1,'rows':15,'orderNo':orderNo,'status':status,
				'startTime':startTime,'endTime':endTime},
			method:"GET"});
	}
	
	function create(){  
	    var oilId = $("#add_oo_type").val();
	    var num = $("#add_oo_num").val();  
	    var status = $("#add_oo_status").val();
	    var price = $("#add_oo_price").val(); 
	    alert(oilId);
	    alert(num);
	    alert(status);
	    alert(price);
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/os/addOsOil",
		    cache:false,  
		    data: {'oilId':oilId,'num':num,'status':status,'price':price} ,
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
    		url:'${ctx}/m/os/jyzOQuery', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[  
    		{field:'id',title:'ID',width:50}, 
    		{field:'orderNo',title:'订单号',width:fixWidth(0.2),align:'right'},
    		{field:'oilName',title:'油品',width:fixWidth(0.05),align:'right'},
    		{field:'num',title:'数量',width:fixWidth(0.08),align:'right'},
    		{field:'status',title:'状态',width:fixWidth(0.06),align:'right',formatter:function(value,rowData,rowIndex){
    			if(value == 1){
    				return '未支付';
    			}if(value == 2){
    				return '已支付';
    			}else{
    				return '已交货';
    			}
    		}},
		    {field:'price',title:'单价(元)',width:fixWidth(0.06),align:'right'},
    		{field:'amount',title:'总价(元)',width:fixWidth(0.1),align:'right'},
		    {field:'userName',title:'用户名',width:fixWidth(0.1),align:'right'},
		    {field:'osName',title:'加油站',width:fixWidth(0.15),align:'right'},
    		{field:'createTime',title:'创建时间',width:fixWidth(0.15),align:'right'},
    		{field:'op',title:'操作',width:185,formatter:function(value,rowData,rowIndex){
    			return '';
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
						<td width="100px"><span>订单号:</span></td>
						<td width="150px"><input id="oo_orderNo" type="text" style="width: 120px"/></td>
						<td width="100px"><span>状态:</span></td>
						<td width="150px">							
						   <select id="oo_status" style="width: 150px">
						        <option value="-1">全部</option>
								<option value="1">未支付</option>
								<option value="2">已支付</option>
								<option value="3">已交货</option>
							</select></td>
					</tr>
					<tr style="height: 40px;">
						<td width="100px"><span>开始时间:</span></td>
						<td width="150px"><input id="oo_startTime" class="easyui-datebox"></input></td>
						<td width="100px"><span>结束时间:</span></td>
						<td width="150px"><input id="oo_endTime" class="easyui-datebox"></input></td>
						<td colspan="4" width="100px">&nbsp;</td>
					</tr>
					<tr style="height: 40px;">
					    <td colspan="8" style="text-align: right;"><button type="button" onclick="showCreatePage()">添加</button></td>
						<td colspan="8" style="text-align: right;"><button type="button" onclick="query()">查询</button></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="pageColumn" style="margin-top: 50px">
		    <table id="dg"></table>
		</div>
	</div>
	<div id="restartDialog" class="easyui-dialog" title="添加进油订单" style="width: 650px; height: 350px;" >
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 5px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr style="height: 30px">
						<td style="width:30%;">
							油型：
						</td>
						<td  style="text-align:left;">
							<select id="add_oo_type" style="width: 150px">
								<option value="1">机油 </option>
								<option value="2">柴油</option>
								<option value="3">180</option>
							</select>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							容量(升)：
						</td>
						<td  style="text-align:left;">
							<input id="add_oo_num" name="add_os_addr"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							状态：
						</td>
						<td  style="text-align:left;">
							<select id="add_oo_status" style="width: 150px">
								<option value="1">未支付</option>
								<option value="2">已支付</option>
								<option value="3">已交货</option>
							</select>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							价格：
						</td>
						<td  style="text-align:left;">
							<input id="add_oo_price" name="add_os_latitude"/>
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