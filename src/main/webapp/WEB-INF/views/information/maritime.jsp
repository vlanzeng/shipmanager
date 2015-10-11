<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript"
	src="${ctx}/static/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="${ctx}/static/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/static/jquery/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/styles/main.css" id="swicth-style" />
</head>

<body>
	<div id="contentWrap">
		<div id="coupon_query_id">
			<table>
				<tr style="height: 40px;">
					<td width="100px">
						<select name="select" id="o_w_status" style="width: 100px">
							<option value="-1" selected="selected">发布日期</option>
							<option value="1">时间倒序</option>
							<option value="2">时间正序</option>
						</select>
					</td>
					<td width="100px">
						<select name="select" id="o_w_status" style="width: 100px">
							<option value="-1" selected="selected">选择城市</option>
							<option value="1">上海</option>
							<option value="2">南京</option>
						</select>
					</td>
					<td width="100px">
						<input type="text" placeholder="发布人手机号"/>
					</td>
					<td width="100px">
               		     <input type="text" placeholder="搜索...."/>
       				 </td>
       				 <td width="100px">
       				    <a href="javascript:void(0);">查询</a>
       				 </td>
       				 <td width="100px"
       				 	 <a href="${ctx}/info/add" class="release-box">发布资讯</a>
       				 </td>
				</tr>
			</table>
		</div>
		<div id="restartDialog" class="easyui-dialog" title="修改资讯" style="width: 600px; height: 500px;">
			<h2 class="info-title">请填写资讯信息：</h2>
			<div class="info-con pdl30">
				<table>
					<tr>
						<td><select name="select" id="o_j_status"
							style="width: 100px">
								<option value="-1" selected="selected">请选择大类型：</option>
								<option value="1">黄沙</option>
								<option value="2">水泥</option>
								<option value="2">石子</option>
								<option value="2">油品</option>
						</select></td>
					</tr>
					<tr>
						<td><select name="select" id="o_j_status"
							style="width: 100px">
								<option value="-1" selected="selected">请选择是否出售/求购：</option>
								<option value="1">出售</option>
								<option value="2">求购</option>
						</select></td>
					</tr>
					<tr>
						<td><select name="select" id="o_j_status"
							style="width: 100px">
								<option value="-1" selected="selected">请选择小类型：</option>
								<option value="1">出售</option>
								<option value="2">求购</option>
						</select></td>
					</tr>
					<tr>
						<td><span class="con-title">请输入价格：</span> <input
							class="input-price box-sizing" type="text" value="3000">&nbsp;&nbsp;元起
						</td>
					</tr>
					<tr>
						<td><span class="con-title">请输入联系人：</span> <input
							class="box-sizing" type="text" placeholder="请输入联系人"></td>
					</tr>
					<tr>
						<td><span class="con-title">请输入电话：</span> <input
							class="box-sizing" type="text" placeholder="请输入联系人电话"></td>
					</tr>
					<tr>
						<td><span class="con-title">请输入地址：</span> <input
							class="box-sizing" type="text" placeholder="请输入地址"></td>
					</tr>
					<tr>
						<td><span class="con-title">请输入标题：</span> <input
							class="box-sizing" type="text" placeholder="请输入标题"></td>
					</tr>
					<tr>
						<td><span class="con-title ver-top">请输入描述：</span> <textarea
								class="box-sizing" name="" id="" cols="30" rows="10"
								placeholder="请输入资讯描述"></textarea></td>
					</tr>
					<tr>
						<td><span class="con-title">上传图片：</span>
							<button class="uploading">上传</button></td>
					</tr>
					<tr>
						 <button class="button">发&nbsp;布</button>
					</tr>
				</table>
			</div>
			</div>
		<div class="pageColumn" style="margin-top: 50px">
			<table id="dg"></table>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$("#restartDialog").dialog('close');
		$('#dg').datagrid({
			url : '${ctx}/m/info/list',
			method : 'GET',
			queryParams : {
				'infoType' : 2
			},
			fitCloumns : true,
			nowrap : true,
			singleSelect : true,
			pagination : true,//分页控件 
			columns : [ [ {
				field : 'id',
				title : 'ID',
				width : 50
			}, {
				field : 'title',
				title : '标题',
				width : fixWidth(0.15),
				align : 'right'
			}, {
				field : 'descri',
				title : '描述',
				width : fixWidth(0.1),
				align : 'right'
			}, {
				field : 'linkMan',
				title : '联系人',
				width : fixWidth(0.08),
				align : 'right'
			}, {
				field : 'photo',
				title : '图片',
				width : fixWidth(0.1),
				align : 'right'
			}, {
				field : 'city',
				title : '城市',
				width : fixWidth(0.1),
				align : 'right'
			}, {
				field : 'phone',
				title : '联系电话',
				width : fixWidth(0.15),
				align : 'right'
			}, {
				field : 'createTime',
				title : '创建时间',
				width : fixWidth(0.15),
				align : 'right'
			}, {
				field : 'op',
				title : '操作',
				width : 155,
				formatter : function(value, rowData, rowIndex) {
					var id = rowData.id;
					var str = "";
					str += ' <a href="#" onclick="del(this)">删除</a>';
					str += ' <a href="#" onclick="update(this)">修改</a>';
					return str;
				}
			} ] ]
		});
	});
	function fixWidth(percent)  
	{  
	    return (document.body.clientWidth-200) * percent ;
	}  
	function create(){
		 $("#restartDialog").dialog('open');
	}
	function update(obj){
		var id = $(obj).parent().parent().parent().children(":first")
		.children().text();
		 $("#restartDialog").dialog('open'); 
	}
	function del(obj) {
		var id = $(obj).parent().parent().parent().children(":first")
				.children().text();
		$
				.get(
						"${ctx}/m/info/delete",
						{
							id : id
						},
						function(data) {
							alert(data.code);
							if (data.code == 200) {
								$('#dg')
										.datagrid(
												{
													url : '${ctx}/m/info/list',
													method : 'GET',
													queryParams : {
														'infoType' : 1
													},
													fitCloumns : true,
													nowrap : true,
													singleSelect : true,
													pagination : true,//分页控件 
													columns : [ [
															{
																field : 'id',
																title : 'ID',
																width : 50
															},
															{
																field : 'title',
																title : '标题',
																width : fixWidth(0.15),
																align : 'right'
															},
															{
																field : 'descri',
																title : '描述',
																width : fixWidth(0.1),
																align : 'right'
															},
															{
																field : 'linkMan',
																title : '联系人',
																width : fixWidth(0.08),
																align : 'right'
															},
															{
																field : 'photo',
																title : '图片',
																width : fixWidth(0.1),
																align : 'right'
															},
															{
																field : 'city',
																title : '城市',
																width : fixWidth(0.1),
																align : 'right'
															},
															{
																field : 'phone',
																title : '联系电话',
																width : fixWidth(0.15),
																align : 'right'
															},
															{
																field : 'createTime',
																title : '创建时间',
																width : fixWidth(0.15),
																align : 'right'
															},
															{
																field : 'op',
																title : '操作',
																width : 155,
																formatter : function(
																		value,
																		rowData,
																		rowIndex) {
																	var id = rowData.id;
																	var str = "";
																	str += ' <a href="#" onclick="del(this)">删除</a>';
																	str += ' <a href="#" onclick="update(this)">修改</a>';
																	return str;
																}
															} ] ]
												});

							}
						}, "json");
	}
	function createFrame(url) {
		var s = '<iframe scrolling="auto" frameborder="0"  src="' + url
				+ '" style="width:100%;height:100%;"></iframe>';
		return s;
	}
</script>
</html>