<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>航运宝</title>
<script type="text/javascript" src="${ctx}/static/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/static/jquery/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/main.css" id="swicth-style">
<style type="text/css">
body {
	font: 12px/20px "微软雅黑", "宋体", Arial, sans-serif, Verdana, Tahoma;
	padding: 0;
	margin: 0;
}

.layout-split-proxy-h {
	position: absolute;
	width: 2px;
	background: #888;
	font-size: 1px;
	cursor: e-resize;
	display: none;
	z-index: 5;
}

.layout-split-north {
	border-bottom: 5px solid #efefef;
}

.layout-split-south {
	border-top: 5px solid #efefef;
}

.layout-split-east {
	border-left: 0px solid #efefef;
}

.layout-split-west {
	border-right: 0px solid #efefef;
}

a:link {
	text-decoration: none;
}

a:visited {
	text-decoration: none;
}

a:hover {
	text-decoration: underline;
}

a:active {
	text-decoration: none;
}

.cs-north {
	height: 95px;
}

.cs-north-bg {
	width: 100%;
	height: 80px;
	background: url(themes/gray/images/header_bg.png) repeat-x;
}

.cs-north-logo {
	height: 40px;
	margin: 15px 0px 0px 5px;
	display: inline-block;
	color: #000000;
	font-size: 22px;
	font-weight: bold;
	text-decoration: none
}

.cs-west {
	width: 200px;
	padding: 0px;
}

.cs-navi-tab {
	padding: 5px;
}

.cs-tab-menu {
	width: 120px;
}

.cs-home-remark {
	padding: 10px;
}

.wrapper {
	float: right;
	height: 30px;
	margin-left: 10px;
}

.ui-skin-nav {
	float: right;
	padding: 0;
	margin-right: 10px;
	list-style: none outside none;
	height: 30px;
}

.ui-skin-nav .li-skinitem {
	float: left;
	font-size: 12px;
	line-height: 30px;
	margin-left: 10px;
	text-align: center;
}

.ui-skin-nav .li-skinitem span {
	cursor: pointer;
	width: 10px;
	height: 10px;
	display: inline-block;
}

.ui-skin-nav .li-skinitem span.cs-skin-on {
	border: 1px solid #FFFFFF;
}

.ui-skin-nav .li-skinitem span.gray {
	background-color: gray;
}

.ui-skin-nav .li-skinitem span.default {
	background-color: blue;
}

.ui-skin-nav .li-skinitem span.bootstrap {
	background-color: #D7EBF9;
}

.ui-skin-nav .li-skinitem span.black {
	background-color: black;
}

.ui-skin-nav .li-skinitem span.metro {
	background-color: #FFE57E;
}
</style>
<script type="text/javascript">
	function addTab(title, url) {
		if ($('#tabs').tabs('exists', title)) {
			$('#tabs').tabs('select', title);//选中并刷新
			var currTab = $('#tabs').tabs('getSelected');
			var url = $(currTab.panel('options').content).attr('src');
			if (url != undefined && currTab.panel('options').title != 'Home') {
				$('#tabs').tabs('update', {
					tab : currTab,
					options : {
						content : createFrame(url)
					}
				})
			}
		} else {
			var content = createFrame(url);
			$('#tabs').tabs('add', {
				title : title,
				content : content,
				closable : true
			});
		}
		tabClose();
	}
	function createFrame(url) {
		var s = '<iframe scrolling="auto" frameborder="0"  src="' + url
				+ '" style="width:100%;height:100%;"></iframe>';
		return s;
	}

	function tabClose() {
		/*双击关闭TAB选项卡*/
		$(".tabs-inner").dblclick(function() {
			var subtitle = $(this).children(".tabs-closable").text();
			$('#tabs').tabs('close', subtitle);
		})
		/*为选项卡绑定右键*/
		$(".tabs-inner").bind('contextmenu', function(e) {
			$('#mm').menu('show', {
				left : e.pageX,
				top : e.pageY
			});

			var subtitle = $(this).children(".tabs-closable").text();

			$('#mm').data("currtab", subtitle);
			$('#tabs').tabs('select', subtitle);
			return false;
		});
	}
	//绑定右键菜单事件
	function tabCloseEven() {
		//刷新
		$('#mm-tabupdate').click(function() {
			var currTab = $('#tabs').tabs('getSelected');
			var url = $(currTab.panel('options').content).attr('src');
			if (url != undefined && currTab.panel('options').title != 'Home') {
				$('#tabs').tabs('update', {
					tab : currTab,
					options : {
						content : createFrame(url)
					}
				})
			}
		})
		//关闭当前
		$('#mm-tabclose').click(function() {
			var currtab_title = $('#mm').data("currtab");
			$('#tabs').tabs('close', currtab_title);
		})
		//全部关闭
		$('#mm-tabcloseall').click(function() {
			$('.tabs-inner span').each(function(i, n) {
				var t = $(n).text();
				if (t != 'Home') {
					$('#tabs').tabs('close', t);
				}
			});
		});
		//关闭除当前之外的TAB
		$('#mm-tabcloseother').click(function() {
			var prevall = $('.tabs-selected').prevAll();
			var nextall = $('.tabs-selected').nextAll();
			if (prevall.length > 0) {
				prevall.each(function(i, n) {
					var t = $('a:eq(0) span', $(n)).text();
					if (t != 'Home') {
						$('#tabs').tabs('close', t);
					}
				});
			}
			if (nextall.length > 0) {
				nextall.each(function(i, n) {
					var t = $('a:eq(0) span', $(n)).text();
					if (t != 'Home') {
						$('#tabs').tabs('close', t);
					}
				});
			}
			return false;
		});
		//关闭当前右侧的TAB
		$('#mm-tabcloseright').click(function() {
			var nextall = $('.tabs-selected').nextAll();
			if (nextall.length == 0) {
				//msgShow('系统提示','后边没有啦~~','error');
				alert('后边没有啦~~');
				return false;
			}
			nextall.each(function(i, n) {
				var t = $('a:eq(0) span', $(n)).text();
				$('#tabs').tabs('close', t);
			});
			return false;
		});
		//关闭当前左侧的TAB
		$('#mm-tabcloseleft').click(function() {
			var prevall = $('.tabs-selected').prevAll();
			if (prevall.length == 0) {
				alert('到头了，前边没有啦~~');
				return false;
			}
			prevall.each(function(i, n) {
				var t = $('a:eq(0) span', $(n)).text();
				$('#tabs').tabs('close', t);
			});
			return false;
		});

		//退出
		$("#mm-exit").click(function() {
			$('#mm').menu('hide');
		})
	}

	$(function() {
		tabCloseEven();

		$('.cs-navi-tab').click(function() {
			var $this = $(this);
			var href = $this.attr('src');
			var title = $this.text();
			addTab(title, href);
		});

		var skins = $('.li-skinitem span').click(
				function() {
					var $this = $(this);
					if ($this.hasClass('cs-skin-on'))
						return;
					skins.removeClass('cs-skin-on');
					$this.addClass('cs-skin-on');
					var skin = $this.attr('rel');
					setCookie('cs-skin', skin);
					skin == 'dark-hive' ? $('.cs-north-logo').css('color',
							'#FFFFFF') : $('.cs-north-logo').css('color',
							'#000000');
				});

		if (getCookie('cs-skin')) {
			var skin = getCookie('cs-skin');
			$this = $('.li-skinitem span[rel=' + skin + ']');
			$this.addClass('cs-skin-on');
			skin == 'dark-hive' ? $('.cs-north-logo').css('color', '#FFFFFF')
					: $('.cs-north-logo').css('color', '#000000');
		}
	});

	function setCookie(name, value) {//两个参数，一个是cookie的名子，一个是值
		var Days = 30; //此 cookie 将被保存 30 天
		var exp = new Date(); //new Date("December 31, 9998");
		exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
		document.cookie = name + "=" + escape(value) + ";expires="
				+ exp.toGMTString();
	}

	function getCookie(name) {//取cookies函数        
		var arr = document.cookie.match(new RegExp("(^| )" + name
				+ "=([^;]*)(;|$)"));
		if (arr != null)
			return unescape(arr[2]);
		return null;
	}
</script>
</head>
<body class="easyui-layout">
	<div region="north" border="true" class="cs-north">
		<div class="cs-north-bg">
			<div class="cs-north-logo">航运宝管理系统</div>
			<div style="float: right;margin-top: 50px;margin-right: 100px">
			登录用户：xxx  &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;  <a href="${ctx}/logout">退出</a>
		</div>
		</div>
	</div>
	<div region="west" border="true" split="true" title=""
		class="cs-west">
		<div class="easyui-accordion" fit="true" border="false">
			<div title="订单管理">
				<a href="javascript:void(0);" src="${ctx}/m/order/index"
					class="cs-navi-tab">订单查询</a>
				</p>
				<a href="javascript:void(0);" src="${ctx}/m/order/index"
					class="cs-navi-tab">生成客户订单</a>
				</p>
			</div>
			<div title="财务明细">
				<a href="javascript:void(0);" src="${ctx}/m/order/index"
					class="cs-navi-tab">加油收入</a>
				</p>
				<a href="javascript:void(0);" src="${ctx}/m/order/index"
					class="cs-navi-tab">进油支出</a>
				</p>
				<a href="javascript:void(0);" src="${ctx}/m/order/mIndex"
					class="cs-navi-tab">提现申请</a>
				</p>
			</div>
			<div title="预约销码">
				<a href="javascript:void(0);" src="${ctx}/m/order/index"
					class="cs-navi-tab">预约销码</a>
				</p>
				<a href="javascript:void(0);" src="${ctx}/m/order/index"
					class="cs-navi-tab">生成客户订单</a>
				</p>
			</div>
			<div title="油库进油">
				<p><a href="javascript:void(0);" src="${ctx}/m/os/jyzOIndex" class="cs-navi-tab">加油站订单查询</a></p>
				<p><a href="javascript:void(0);" src="${ctx}/m/os/adminOIndex" class="cs-navi-tab">admin订单查询</a></p>
			</div>
			<div title="优惠券管理">
				<a href="javascript:void(0);" src="${ctx}/m/coupon/index"
					class="cs-navi-tab">优惠券列表</a>
				</p>
				<a href="javascript:void(0);" src="${ctx}/m/coupon/uIndex"
					class="cs-navi-tab">优惠券使用列表</a>
				</p>
			</div>
			<div title="加油站管理">
				<a href="javascript:void(0);" src="${ctx}/m/os/index"
					class="cs-navi-tab">加油站列表</a>
				</p>
			</div>
			<div title="资讯管理">
				<a href="javascript:void(0);" src="${ctx}/m/info/index" class="cs-navi-tab">大宗商品</a>
				</p>
				<a href="javascript:void(0);" src="${ctx}/admin/user/list" class="cs-navi-tab">船舶服务</a>
				</p>
				<a href="javascript:void(0);" src="${ctx}/admin/user/list" class="cs-navi-tab">配货信息</a>
				</p>
				<a href="javascript:void(0);" src="${ctx}/admin/user/list" class="cs-navi-tab">海事服务</a>
				</p>
			</div>
			<div title="基本信息管理">
				<a href="javascript:void(0);" src="${ctx}/admin/user/list" class="cs-navi-tab">用户列表</a>
				</p>
			</div>
			<div title="充值管理">
				<p><a href="javascript:void(0);" src="${ctx}/m/order/rIndex" class="cs-navi-tab">充值管理</a></p>
			</div>
			<div title="广告管理">
				<p><a href="javascript:void(0);" src="${ctx}/admin/user/list" class="cs-navi-tab">广告管理</a></p>
			</div>
			<div title="系统管理">
			    <a href="javascript:void(0);" src="${ctx}/m/muser/index"
					class="cs-navi-tab">后台用户管理</a>
				</p>
			</div>
		</div>
	</div>
	<div id="mainPanle" region="center" border="true" border="false">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<div title="Home">
				<div class="cs-home-remark">
					航运宝管理系统
				</div>
			</div>
		</div>
	</div>

	<div id="mm" class="easyui-menu cs-tab-menu">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseother">关闭其他</div>
		<div id="mm-tabcloseall">关闭全部</div>
	</div>
</body>
<script type="text/javascript">

</script>
</html>
