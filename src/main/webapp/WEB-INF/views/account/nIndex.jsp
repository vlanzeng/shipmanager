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
		var phone = $("#u_phone").val();
		name=encodeURI(name);
		phone=encodeURI(phone);
// 		$('#dg').datagrid({ url:"${ctx}/m/coupon/query",
// 			queryParams:{'page':1,'rows':15,'name':name,'faceLimit':faceLimit,'type':type,
// 				'startTime':startTime,'endTime':endTime},
// 			method:"GET"});
		$('#dg').datagrid('reload',
				{'name':name, 'phone':phone });
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
	}
	
	function isEmptyStr(str){
		if(typeof str == "undefined" || str==null ||
				str.trim().length == 0)
			return true;
		return false;
	}
	
	function create(){
		var userName = $("#user_name").val();
		var phone = $("#phone").val();
		var pwd = $("#user_pwd").val();
		var c_name = $("#c_name").val();
		var c_no = $("#c_no").val();
		
		if( isEmptyStr(userName) || isEmptyStr(phone) ||
				isEmptyStr(pwd)){
			alert('请输入昵称、手机号和密码');
			return;
		}
		
		var param = {};
		param['userName'] = userName;
		param['phone'] = phone;
		param['pwd'] = pwd;
		
		if(!isEmptyStr(c_name)){
			param['c_name'] = c_name;
		}
		
		if(!isEmptyStr(c_no)){
			param['c_no'] = c_no;
		}
		
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/muser/nadd",
		    cache:false,  
		    data: param ,
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
    		url:'${ctx}/m/muser/nquery', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'userName',title:'昵称',width:fixWidth(0.2),align:'right'},
    		{field:'phone',title:'手机号',width:fixWidth(0.15),align:'right'},
    		{field:'shipname',title:'船舶名',width:fixWidth(0.2),align:'right'},
    		{field:'shipno',title:'船舶编号',width:fixWidth(0.15),align:'right'},
    		{field:'createtime',title:'用户创建时间',width:fixWidth(0.15),align:'right'},
    		{field:'rechargeamount',title:'充值总额',width:fixWidth(0.10),align:'right',formatter:function(value,rowData,rowIndex){
    			if(typeof(value)=='undefined' || value==null) return;
    			return "<a href='javascript:detail_recharge("+rowData.phone+");'>"+value.toFixed(2)+"</a>";
    		}},
    		{field:'fee',title:'支出总额',width:fixWidth(0.10),align:'right',formatter:function(value,rowData,rowIndex){
    			if(typeof(value)=='undefined' || value==null) return;
    			return "<a href='javascript:detail_order("+rowData.phone+");'>"+value.toFixed(2)+"</a>";
    		}},
    		{field:'left',title:'余额',width:fixWidth(0.10),align:'right' }
    		]] 
    	});
    });   
    
    function detail_recharge(phone){
    	$("#div_dg").hide();
    	$("#div_recharge").show();
    	
		$('#dg_recharge').datagrid({ 
			url:"${ctx}/m/order/rQuery",
			queryParams:{'page':1,'rows':15,'phone':phone},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件     		
    		columns:[[ 
    		{field:'id',title:'ID',width:fixWidth(0.06)}, 
    		{field:'phone',title:'手机号',width:fixWidth(0.18)}, 
    		{field:'username',title:'昵称',width:fixWidth(0.18)}, 
    		{field:'shipname',title:'船舶名',width:fixWidth(0.18)}, 
    		{field:'amount',title:'金额',width:fixWidth(0.1),align:'right'},
    		{field:'createTime',title:'充值时间',width:fixWidth(0.20),align:'right'}
    		]] ,
			method:"GET"});
    }
    
    function detail_order(phone){
		
    	$("#div_dg").hide();
    	$("#div_order").show();
		$('#dg_order').datagrid({ url:"${ctx}/m/order/query",
			queryParams:{'page':1,'rows':15,'userName':phone},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		    		{field:'orderNo',title:'订单号',width:fixWidth(0.16)}, 
    		    		{field:'productName',title:'油品',width:fixWidth(0.04),align:'right'},
    		    		{field:'num',title:'数量',width:fixWidth(0.03),align:'right'},
    		    		{field:'status',title:'状态',width:fixWidth(0.06),align:'right'},
    		    		{field:'amount',title:'总价',width:fixWidth(0.10),align:'right'},
    		    		{field:'userName',title:'昵称',width:fixWidth(0.15),align:'right'},
    		    		{field:'osName',title:'加油站',width:fixWidth(0.15),align:'right'},
    		    		{field:'bookTime',title:'预约时间',width:fixWidth(0.1),align:'right'},
    		    		{field:'bookAddr',title:'预约地址',width:fixWidth(0.15),align:'right'},
    		    		{field:'createTime',title:'创建时间',width:fixWidth(0.12),align:'right'}
    		    		]],
			method:"GET"});
    }
   
    function back_order(){
    	$("#div_order").hide();
    	$("#div_dg").show();
    }
    function back_recharge(){
    	$("#div_recharge").hide();
    	$("#div_dg").show();
    }
</script>
</head>

<body>
	<div id="contentWrap">
		<div class="" style="">
			<div id="coupon_query_id">
				<table>
					<tr style="height: 40px;">
						<td width="100px"><span>昵称:</span></td>
						<td width="150px"><input id="u_name" type="text" style="width: 120px"/></td>
						<td width="100px"><span>手机号:</span></td>
						<td width="150px"><input id="u_phone" type="text" style="width: 120px"/></td>
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
			<div id="div_dg"><a href="${ctx}/m/muser/exportNuser">导出</a>
		    <table id="dg"></table></div>
		    <div style="display:none"  id="div_order"><a href="javascript:back_order();">返回</a><table id="dg_order"></table></div>
		    <div  style="display:none" id="div_recharge"><a href="javascript:back_recharge();">返回</a><table id="dg_recharge"></table></div>
		</div>
	</div>
	<div id="restartDialog" class="easyui-dialog" title="添加用户" style="width: 800px; height: 400px;" >
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 5px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr style="height: 30px">
						<td style="width:30%;">
							昵称：
						</td>
						<td  style="text-align:left;">
							<input id="user_name" name="user_name"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							手机号：
						</td>
						<td  style="text-align:left;">
							<input id="phone" name="phone"/>
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
							船舶名：
						</td>
						<td  style="text-align:left;">
							<input id="c_name" name="c_name"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							船舶编号：
						</td>
						<td  style="text-align:left;">
							<input id="c_no" name="c_no"/>
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