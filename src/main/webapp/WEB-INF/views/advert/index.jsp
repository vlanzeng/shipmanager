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
    		url:'${ctx}/advert/query', 
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
// 		$('#dg').datagrid({ url:"${ctx}/advert/query",
// 			queryParams:{'page':1,'rows':15},
// 			method:"GET"});
		doQuery( $("#type").val() );
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
	
	function isEmptyStr( str ){
		if(str == null)
			return true;
		return /^\s*$/.test(str);
	}
	function create(){
		var url = $("#a_url").val();
		var purl = $("#a_purl").val();
		var type = $("#a_type").val();
		var title = $('#a_title').val();
		var os = $("#a_os").combobox('getValue');
		
		if(  isEmptyStr( url ) || isEmptyStr(purl) || isEmptyStr(type) || isEmptyStr(title)){
			alert("请录入链接地址、图片地址、标题和广告类型");
			return;
		}
		$.ajax({
		    type:'POST',
		    url: "${ctx}/advert/add",
		    cache:false,  
		    data: {'url':url,'purl':purl,'type':type,'title':title,'os':os} ,
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

	function doQuery( type ){
		var param = {'page':1,'rows':15};
		
		if( typeof type!='undefined' && type != null && type.trim().length>0){
			param['type'] = type;
		}
		
	   	$('#dg').datagrid({ 
    		url:'${ctx}/advert/query', 
    		method:'GET',
    		queryParams:param,
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'id',title:'ID',width:50}, 
    		{field:'url',title:'链接',width:fixWidth(0.3),align:'right'},
    		{field:'purl',title:'图片',width:fixWidth(0.1),align:'right',formatter:function(value){
					return "  <img  src="+value+"  width=80px height=80px />  ";				
    		}},
    		{field:'title',title:'描述',width:fixWidth(0.25),align:'right'},
    		{field:'type',title:'类型',width:fixWidth(0.15),align:'right'}
   
    		]] 
    	});
	}
	
    $(document).ready(function(){
		$.ajax({
		    type:'GET',
		    url:'${ctx}/m/os/query?status=-1&page=1&rows=10000000',
		    cache:false,  
		    dataType: 'json',
		    success: function(vdata){
		    	$("#a_os").combobox({
		    		data:vdata.rows,
		    		valueField:'id',
		    		textField:'name'
		    	});

		    	$("#restartDialog").dialog('close');
		    	$("#showOsDialog").dialog('close');
		    },  
		    error : function() {  
		    	alert("操作异常，请稍后再试。");  
		    }  
		});
 		doQuery();
    });   
</script>
</head>

<body>
	<div id="contentWrap">
		<div class="" style="">
			<div id="coupon_query_id">
			<table>
					<tr style="height: 40px;">
						<td width="100px"><span>类型:</span></td>
						<td width="150px">
							<select id="type">
								<option value="" selected="selected">--</option>
								<option value="1" >加油站</option>
								<option value="2">新闻网页</option>
								<option value="3">加油站发放优惠券</option>
							</select>
						</td>
						<td colspan="4" width="100px">&nbsp;</td>
					    <td colspan="2" style="text-align: right;"><button type="button" onclick="showCreatePage()">添加</button></td>
						<td colspan="2" style="text-align: center;"><button type="button" onclick="query()">查询</button></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="pageColumn" style="margin-top: 20px">
		    <table id="dg"></table>
		</div>
	</div>
	<div id="restartDialog" class="easyui-dialog" title="添加广告" style="width: 650px; height: 380px;" >
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 5px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr style="height: 30px">
						<td style="width:30%;">
						 	链接地址：
						</td>
						<td  style="text-align:left;">
							<input id="a_url"   type="text"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							图片地址：
						</td>
						<td  style="text-align:left;">
							<input id="a_purl"   type="text"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							标题：
						</td>
						<td  style="text-align:left;">
							<input id="a_title"  type="text" />
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							广告类型：
						</td>
						<td  style="text-align:left;">
							<select id="a_type">
								<option value="" selected="selected">--</option>
								<option value="1" >加油站</option>
								<option value="2">新闻网页</option>
								<option value="3">加油站发放优惠券</option>
							</select>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							加油站：
						</td>
						<td  style="text-align:left;">
							<input id="a_os" type="text" ></input>
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