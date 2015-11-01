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
	function submitfile(){
		var name=$("#add_os_name").val();
		var addr=$("#add_os_addr").val();
		var phone=$("#add_os_phone").val();
		var man = $("#man").val();
		var jian = $("#jian").val();
		
		if(name.trim().length==0 || addr.trim().length==0 || phone.trim().length==0){
			alert('请填写名称、地址和电话');
			return;
		}
		
		if( ( man.trim().length==0 && jian.trim().length!=0 ) ||
				(man.trim().length!=0 && jian.trim().length==0)){
			alert('请输入完整的满减金额');
		}
		
		$("#testform").submit();
	}
	function fixWidth(percent)  
	{  
	    return (document.body.clientWidth-200) * percent ;
	}  
	
	function showRestartDialog(id, status){       
        $("#update_orderId").val(id);  
        $("#os_old_status").val(status);  
        //$("#restartDialog").dialog('open');  
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
		//$("#restartDialog").dialog('close');
		$("#restartDialog").hide();
		$("#pageColumn").show();
	}
	
	function cancelOs(){       
		$("#showOsDialog").dialog('close');
	}
	
	function query(){
		var name = $("#c_name").val();
		var faceLimit = $("#c_face").val();
		var cityName= $("#c_city").find("option:selected").text();
// 		var type = $("#c_type").val();
// 		//var status = $("#status").val();
// 		var startTime = $("#c_startTime").datebox('getValue');
// 		var endTime = $("#c_endTime").datebox('getValue');
		name=encodeURI(name);
		cityName=encodeURI(cityName);
// 		$('#dg').datagrid({ url:"${ctx}/m/os/query",
// 			queryParams:{'page':1,'rows':15,'name':name,'faceLimit':faceLimit,'type':type,'cityName':cityName,
// 				'startTime':startTime,'endTime':endTime},
// 			method:"GET"});
		$('#dg').datagrid('reload',
				{'page':1,'rows':15,'osName':name, 'cityName':cityName,'status':-1 });
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
		//$("#restartDialog").dialog('open');  
		$("#pageColumn").hide();
		$("#restartDialog").show();
	}
	
	function create(){
		//$('#file_upload').uploadify("upload");
//         	var name = $("#add_os_name").val();
//       	var addr = $("#add_os_addr").val();
//       	var phone = $("#add_os_phone").val();
//       	var latitude = $("#add_os_latitude").val();
//       	var langitude = $("#add_os_longitude").val();
//       	var cityId = $("#add_os_city").val();
//       	name=encodeURI(name);
//       	addr=encodeURI(addr);
		//$("#addform")[0].action = "${ctx}/m/os/addnima?add_os_name="+name+"&add_os_addr="+addr+"&add_os_phone="+phone+"&add_os_latitude="+latitude+"&add_os_longitude="+langitude+"&add_os_city="+cityId;
		//$("#addform")[0].action = "${ctx}/m/os/add";
		$("#addform").submit();
	}
	
	function updateStatus(id, status){
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/os/updateStatus",
		    cache:false,  
		    data: {'osId':id,'status':status} ,
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
	
	function deleteStatus(id, status){
		$.ajax({
		    type:'POST',
		    url: "${ctx}/m/os/deleteOS",
		    cache:false,  
		    data: {'osId':id} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.code == 200){
		    		$("#showOsDialog").dialog('close');
		    		alert("删除成功");
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
	
	function queryComment( osid ){
		$("#pageColumn").hide();
		$("#comments").show();
		
    	$('#dg_comment').datagrid({ 
    		url:'${ctx}/m/os/queryComment', 
    		method:'GET',
    		queryParams:{'status':-1, 'osid':osid },
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'userName',title:'用户名',width:fixWidth(0.15),align:'right'},
    		{field:'osName',title:'加油站',width:fixWidth(0.2),align:'right'},
    		{field:'comment',title:'评论',width:fixWidth(0.7),align:'right'}
    		]] 
    	});
	}
	
	function commentBack(){
		$("#comments").hide();
		$("#pageColumn").show();
	}
	

    $(document).ready(function(){
    	//$("#restartDialog").dialog('close');
    	$("#restartDialog").hide();
    	$("#showOsDialog").dialog('close');
    	$("#comments").hide();
    	
    	$('#dg').datagrid({ 
    		url:'${ctx}/m/os/query', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		columns:[[     		          
    		{field:'id',title:'ID',width:50}, 
    		{field:'picUrl',title:'图片',width:fixWidth(0.15),align:'right',formatter:function(value,rowData,rowIndex){
    			return '<img src="'+value+'" style="width: 100px;height: 100px"/>';
    		}},
    		{field:'name',title:'名称',width:fixWidth(0.15),align:'right'},
    		{field:'address',title:'地址',width:fixWidth(0.2),align:'right'},
    		{field:'phone',title:'电话',width:fixWidth(0.1),align:'right'},
    		{field:'cityName',title:'城市',width:fixWidth(0.05),align:'right'},
    		{field:'status',title:'状态',width:fixWidth(0.05),align:'right'},
    		{field:'createTime',title:'创建时间',width:fixWidth(0.15),align:'right'},
    		{field:'latitude',title:'经度',width:fixWidth(0.10),align:'right'},
    		{field:'longitude',title:'纬度',width:fixWidth(0.10),align:'right'},
    		{field:'derate',title:'优惠券',width:fixWidth(0.10),align:'right'},
    		{field:'op',title:'操作',width:185,formatter:function(value,rowData,rowIndex){
    			var id = rowData.id;
    			var status = rowData.status;
    			var str = "";
    			str += ' <a href="#" onclick="deleteStatus(\''+id+'\',0)">删除</a>';
    			if(status == 1){
    				str += ' <a href="#" onclick="updateStatus(\''+id+'\',0)">失效</a>';
    			}else{
    				str += ' <a href="#" onclick="updateStatus(\''+id+'\',1)">生效</a>';
    			}
    			str += ' <a href="#" onclick="queryComment(\''+id+'\',1)">评论列表</a>';
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
						<td width="100px"><span>名称:</span></td>
						<td width="150px"><input id="c_name" type="text" style="width: 120px"/></td>
						<td width="100px"><span>城市:</span></td>
						<td width="150px">
						<select id="c_city" name="c_city" style="width: 150px">
								<option value="1">上海  </option>
								<option value="2">南京</option>
								<option value="3">仪征</option>
								<option value="4">镇江</option>
								<option value="5">靖江</option>
								<option value="6">张家界</option>
								<option value="7">江阴</option>
								<option value="8">南通</option>
								<option value="9">常熟</option>
								<option value="10">太仓</option>
								<option value="11">高岗</option>
							</select>
						</td>
					</tr>
<!-- 					<tr style="height: 40px;"> -->
<!-- 						<td width="100px"><span>开始时间:</span></td> -->
<!-- 						<td width="150px"><input id="c_startTime" class="easyui-datebox"></input></td> -->
<!-- 						<td width="100px"><span>结束时间:</span></td> -->
<!-- 						<td width="150px"><input id="c_endTime" class="easyui-datebox"></input></td> -->
<!-- 						<td colspan="4" width="100px">&nbsp;</td> -->
<!-- 					</tr> -->
					<tr style="height: 40px;">
					    <td colspan="8" style="text-align: right;"><button type="button" onclick="showCreatePage()">添加</button></td>
						<td colspan="8" style="text-align: right;"><button type="button" onclick="query()">查询</button></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="pageColumn" id="pageColumn" style="margin-top: 50px">
		    <table id="dg"></table>
		</div>
		
		<div class="comments" id="comments" style="margin-top: 50px">
			<a href="javascript:commentBack();">返回</a>
		    <table id="dg_comment"></table>
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
	
	
	<div id="restartDialog"  title="添加加油站" style="width: 650px; height: 380px; border: 1px; text-align: center; margin:auto;" >
	<form id="testform" action="${ctx}/m/os/add" enctype="multipart/form-data" method="post">
			
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr style="height: 30px">
						<td style="width:30%;">
							加油站名称：
						</td>
						<td  style="text-align:left;">
							<input id="add_os_name" name="add_os_name"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							加油站地址：
						</td>
						<td  style="text-align:left;">
							<input id="add_os_addr" name="add_os_addr"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							联系电话：
						</td>
						<td  style="text-align:left;">
							<input id="add_os_phone" name="add_os_phone"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							经度：
						</td>
						<td  style="text-align:left;">
							<input id="add_os_latitude" name="add_os_latitude"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							纬度：
						</td>
						<td  style="text-align:left;">
							<input id="add_os_longitude" name="add_os_longitude"/>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							所属城市：
						</td>
						<td  style="text-align:left;">
							<select id="add_os_city" name="add_os_city" style="width: 150px">
								<option value="1">上海  </option>
								<option value="2">南京</option>
								<option value="3">仪征</option>
								<option value="4">镇江</option>
								<option value="5">靖江</option>
								<option value="6">张家界</option>
								<option value="7">江阴</option>
								<option value="8">南通</option>
								<option value="9">常熟</option>
								<option value="10">太仓</option>
								<option value="11">高岗</option>
							</select>
						</td>
					</tr>
					<tr style="height: 30px">
						<td style="width:30%;">
							加油站图片：
						</td>
						<td  style="text-align:left;">
							<input id="file_upload" name="file" type="file" />
							<div id="some_file_queue" style="display: none;"></div>
						</td>
					</tr>
					<tr style="height: 30px">
						<td  colspan="2">
							满<input id='man'  name='man' type="text"></input>减<input id='jian'   name='jian' type="text"></input>
						</td>
					</tr>
				</table>
<!-- 		<input type="file" name="file" id="testfile">上传我的文件</input> -->
		<input type="button" onclick="submitfile();" value="确定"></input>
		<input type="button" onclick="cancel();" value="退出"></input>
	</form>
	</div>
</body>
</html>