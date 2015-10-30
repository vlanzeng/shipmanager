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
<script type="text/javascript" src="${ctx}/My97DatePicker/WdatePicker.js"></script> 
<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/main.css" id="swicth-style"/>
<script type="text/javascript">  
	function fixWidth(percent)  
	{  
	    return (document.body.clientWidth-200) * percent ;
	}  
	
	
	function cancel(){       
		$("#restartDialog").dialog('close');
	}
	
	function cancelOs(){       
		$("#showOsDialog").dialog('close');
	}
	
	function addUserOrderDialog(){
		$("#addUserOrderDialog").dialog('open');
	}
	
	function canceladdUserOrderDialog(){
		$("#addUserOrderDialog").dialog('close');
	}
	
	
	function showCashDetail(id){
		
		
		$.ajax({
			url:'${ctx}/m/order/query/cashorderdetail?id='+id, 
			type: "GET",
			dataType:'json',
			success:function(data){
				$('#dg2').datagrid({ 
		    		method:'GET',
		    		fitCloumns: true , 
		    		nowrap: true , 
		    		singleSelect: true,
		    		pagination:true,//分页控件 
		    		data:data.slice(0,10),
		    		columns:[[ 
		    		{field:'oid',title:'订单号',width:fixWidth(0.1),formatter:function(value,row,index){
		    		return  row.historyOrder.id;
		    		}}, 
		    		{field:'pn',title:'产品名称',width:fixWidth(0.1),align:'right',formatter:function(value,row,index){
		    		if(row.historyOrder.productName)return row.historyOrder.productName;
		    		return "";
		    		}},
		    		{field:'price',title:'单价',width:fixWidth(0.08),align:'right',formatter:function(value,row,index){
		    			return row.historyOrder.price;
		    		}},
		    		{field:'num',title:'数量',width:fixWidth(0.08),align:'right',formatter:function(value,row,index){
		    			return row.historyOrder.num;
		    		}},
		    		{field:'money',title:'总价',width:fixWidth(0.08),align:'right',formatter:function(value,row,index){
		    			return row.historyOrder.money;
		    		}},
		    		{field:'uname',title:'用户名',width:fixWidth(0.08),align:'right',formatter:function(value,row,index){
		    			return row.historyOrder.userName;
		    		}},
		    		{field:'phone',title:'电话',width:fixWidth(0.09),align:'right',formatter:function(value,row,index){
		    			return row.historyOrder.phone;
		    		}}
		    		]] 
		    	});
				
				
				   var pager = $("#dg2").datagrid("getPager");  
		            pager.pagination({  
		                total:data.length,  
		                onSelectPage:function (pageNo, pageSize) {  
		                    var start = (pageNo - 1) * pageSize;  
		                    var end = start + pageSize;  
		                    $("#dg2").datagrid("loadData", data.slice(start, end));  
		                    pager.pagination('refresh', {  
		                        total:data.length,  
		                        pageNumber:pageNo  
		                    });  
		                }  
		            });  
				
			}
			});
		
		
/*     	$('#dg2').datagrid({ 
    		url:'${ctx}/m/order/query/cashorderdetail?id='+id, 
    		method:'GET',
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:true,//分页控件 
    		data:data.slice(0,10),
    		columns:[[ 
    		{field:'oid',title:'订单号',width:fixWidth(0.1),formatter:function(value,row,index){
    		return  row.historyOrder.id;
    		}}, 
    		{field:'pn',title:'产品名称',width:fixWidth(0.15),align:'right',formatter:function(value,row,index){
    		if(row.historyOrder.productName)return row.historyOrder.productName;
    		return "";
    		}},
    		{field:'orderNo',title:'订单编号',width:fixWidth(0.3),align:'right',formatter:function(value,row,index){
    			return row.historyOrder.orderNo;
    		}}
    		]] 
    	}); */
		
	}


    $(document).ready(function(){
    	$("#restartDialog").dialog('close');
    	$("#showOsDialog").dialog('close');
    	$("#addUserOrderDialog").dialog('close');
    	loadCashs();
    });   
    
    
    function  loadCashs(){
    	
    	$('#dg').datagrid({ 
    		url:'${ctx}/m/order/query/cashorder', 
    		method:'GET',
    		queryParams:{'status':-1},
    		fitCloumns: true , 
    		nowrap: true , 
    		singleSelect: true,
    		pagination:false,//分页控件 
    		columns:[[ 
    		{field:'id',title:'订单号',width:fixWidth(0.1)}, 
    		{field:'createTime',title:'创建时间',width:fixWidth(0.15),align:'right',formatter:function(value){
    			var date = new Date(value);
    			 return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
    		}},
    		{field:'orderName',title:'备注名称',width:fixWidth(0.11),align:'right'},
    		{field:'op',title:'操作22',width:fixWidth(0.1),align:'right',formatter:function(value,row,index){
    			return '<a href="#" onclick="showCashDetail(\''+row.id+'\')">详情</a>';
    		}}
    		]] 
    	});
    }
    
    
    function  excelData(){
    	
	var url = "${ctx}/m/order/excel/cashorder";
		
		window.location.href = url;
    }
    
    function delOrder(id){
    	$.ajax({
    	     type: 'POST',
    	     url: '${ctx}/m/order/delOrder', 
    	    data: {"id":id} ,
    	    success: function(result){
				   if(result.status==200){
					   query();
				   }else{
					   alert("系统错误");
				   }
    	    }
    	});
    };
    
    function applyCash(){
    	 var id =  $("#hidden_os_id").val();
    	 if(id){
    	    	$.ajax({
    				url:'${ctx}/m/order/query/apply/cash?id=4', 
    				type: "GET",
    				dataType:'json',
    				success:function(data){
    					$('#dg3').datagrid({ 
    						data:data.slice(0,10),
    			    		method:'GET',
    			    		fitCloumns: true , 
    			    		singleSelect: true,
    			    		pagination:true,//分页控件 
    			    		columns:[[ 
    			    		{field:'id',title:'id',width:fixWidth(0.06)}, 
    			    		{field:'productName',title:'油品',width:fixWidth(0.05),align:'right'},
    			    		{field:'num',title:'数量',width:fixWidth(0.05),align:'right'},
    			    		{field:'status',title:'状态',width:fixWidth(0.06),align:'right'},
    			    		{field:'amount',title:'总价',width:fixWidth(0.05),align:'right'},
    			    		{field:'userName',title:'用户名',width:fixWidth(0.1),align:'right'},
    			    		{field:'osName',title:'加油站',width:fixWidth(0.1),align:'right'},
    			    		{field:'bookTime',title:'预约时间',width:fixWidth(0.1),align:'right'},
    			    		{field:'bookAddr',title:'预约地址',width:fixWidth(0.11),align:'right'},
    			    		{field:'createTime',title:'创建时间',width:fixWidth(0.11),align:'right'}
    			    		]] 
    			    	});
    					
    					   var pager = $("#dg3").datagrid("getPager");  
    			            pager.pagination({  
    			                total:data.length,  
    			                onSelectPage:function (pageNo, pageSize) {  
    			                    var start = (pageNo - 1) * pageSize;  
    			                    var end = start + pageSize;  
    			                    $("#dg3").datagrid("loadData", data.slice(start, end));  
    			                    pager.pagination('refresh', {  
    			                        total:data.length,  
    			                        pageNumber:pageNo  
    			                    });  
    			                }  
    			            });  
    			            
    			        	$("#restartDialog").dialog('open');
    				}
    				});
    	 }else{
    		 alert("系统超时，请重新登录");
    	 }

    	
    };
    
    
    function apply(){
    	jQuery.messager.prompt('提示:','请输入订单名称',function(r){ 
    		if(r){ 
    		   	$.ajax({
    				url:'${ctx}/m/order/apply?orderName='+r, 
    				type: "POST",
    				dataType:'json',
    				success:function(data){
    					if(data && data.status ==200){
    						alert("提现成功");
    						$("#restartDialog").dialog('close');
    						loadCashs();
    					}
    				}	
    				});
    			
    		} 
    		}) 
    	
 
    	
    }
</script>
</head>

<body>
	<div id="contentWrap">
		<div class="" style="">
			<div id="coupon_query_id">
				
				<button onclick="applyCash()">申请提现</button>
				<button onclick="excelData()">导出报表</button>
			</div>
		</div>
		<div class="pageColumn" style="margin-top: 10px">
			<div style="width: 40% ;float: left;">
		    <table id="dg"></table>
			</div>
			<div style="width: 53% ;float: left;">
		    <table id="dg2"></table>
			</div>
			
		</div>
		
	</div>
	<div id="restartDialog" class="easyui-dialog" title="申请提现列表" style="width: 800px; height: 400px;" >
		
		 <table id="dg3"></table>
		 <button onclick="apply()" >确定</button>
		 <button onclick="cancel();">取消</button>
		
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
	
	
	
		<div id="addUserOrderDialog" class="easyui-dialog" title="添加用户订单" style="width: 400px; height: 320px;" >
		<input  type="hidden" id="hidden_os_id" value="${passOsid}" />
		<div style="margin-left: 5px;margin-right: 5px;margin-top: 5px;">			
			<div class="data-tips-info">
				<table style="margin-top: 20px;margin-left:20px;margin-right:20px;vertical-align:middle;" width="90%" border="0" cellpadding="0" cellspacing="1">
					<tr>
						<td style="text-align: center ;">用户电话:</td>
						<td> <input id="order_user_phone"  name="order_user_phone" /></td>	
					</tr>
					<tr style="line-height: 40px;">
					<td style="text-align: center;">产品:</td>	
						<td><select id="order_product_name" style="width: 150px">
								<option value="1">机油 </option>
								<option value="2">柴油</option>
								<option value="3">180</option>
							</select>
						</td>
					</tr>
					<tr style="line-height: 40px;">
					<td style="text-align: center;">加油站:</td>	
						<td><select id="order_station" style="width: 150px">
								<c:forEach items="${oss}" var="item" >
									<option value="${item[0] }">${item[1] } </option>
								</c:forEach>
							</select>
						</td>
					</tr>
		<%-- 				<tr style="line-height: 40px;">
					<td style="text-align: center;">优惠券:</td>	
						<td><select id="order_coupon" style="width: 150px">
								<option value="-1">无</option>
								<c:forEach items="${conpons}" var="item" >
									<option value="${item[0] }">${item[1] } </option>
								</c:forEach>
							</select>
						</td>
					</tr> --%>
							<tr style="line-height: 40px;">
					<td style="text-align: center;">预约时间:</td>	
						<td>
							<input  id="order_book_time"  type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  name="order_book_time" />
						</td>
					</tr>
					<tr style="line-height: 40px;">
					<td style="text-align: center;">单价:</td>	
						<td>
							<input  id="order_price" type="text"   name="order_price" />
						</td>
					</tr>
						<tr style="line-height: 40px;">
					<td style="text-align: center;">数量:</td>	
						<td>
							<input id="order_num" type="text" name="order_num" />
						</td>
					</tr>
					<tr style="line-height: 40px;">
					<td style="text-align: right;">
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'ope-finish'" onclick="addUserOrder()">确定</a>
					</td>
					<td style="text-align: center;">
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'ope-cancel'" onclick="canceladdUserOrderDialog()">取消</a>
					</td>
				</tr>	
				</table>		
			</div> 		
		</div>
	</div>
	
</body>
</html>