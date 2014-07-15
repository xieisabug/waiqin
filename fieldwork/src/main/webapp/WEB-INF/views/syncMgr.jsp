<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <title>湖南航天信息外服人员管理系统</title>
     <link rel="shortcut icon" href="<%=request.getContextPath()%>/static/images/logo.ico">
    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/lib/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/lib/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/app/main.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery.easyui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/easyui-lang-zh_CN.js" charset="utf-8"></script>
    
	<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/common.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/syncMgr.js"></script>
    <script type="text/javascript">
       	var header="<%=request.getContextPath()%>";
       	var currentUserId="${currentUser.id}";
       	var permissions='${rolePermission}';
    </script>
</head>
<body>
<!-- 解决ie渲染慢，直接将页面隐藏，然后加载完了，删除遮盖层 -->
<div id="load">加载中......</div>
<%@ include file="header.jsp"%>

    <div id="center">
     <div id="tab"> 
    	 <!-- 费用项目维护tab项 --> 
    	 <shiro:hasPermission name="expenseItem:list">
	          <div title="费用项目">  
			       <table id="syncTable0">
			           <thead><th>编号</th><th>标题</th></thead>
			           <tbody></tbody>
			       </table>
			       <!--工具栏-->
				    <div id="costTb" class="syncTb">
				     <shiro:hasPermission name="expenseItem:create"> 
				            <button id="costAddBtn" class="addBtn" style="margin-right: 365px">增加</button>
				      </shiro:hasPermission>
				           	 项目名称: <input id="costSearch" type="text" class="searchInput" style="width:120px;margin-right:5px">
				            <button id="costSearchBtn" class="searchBtn">搜索</button>
    				</div>
    				<!--增加界面-->
				    <div id="costAddPanel">
				        <form id="costSubmitForm" method="post" action="<%=request.getContextPath()%>/web/expense-item/${currentUser.id}/create">
				            <ul>
				                <li><label class="labelTitle">ID:</label><input id="costId" type="text" name="itemId" class="inputBox"></li>
				                <li><label class="labelTitle">项目名称:</label><input id="costContent" type="text" name="itemName" class="inputBox"></li>
				                <li class="btnLi">
				                       <input type="submit" value="保存" class="inputBtn">
				                       <input id="costAddReset" type="reset" value="关闭" class="inputBtn">
				            </ul>
				        </form>
				    </div>
	   		 </div> 
	   		 </shiro:hasPermission>
	   		 <!-- 部门维护tab项 --> 
	   		 <shiro:hasPermission name="department:list">
	   		 <div title="部门维护">  
			        <table id="syncTable1">
			           <thead><th>编号</th><th>标题</th></thead>
			           <tbody></tbody>
			       </table>
			       <!--工具栏-->
				    <div id="departmentTb" class="syncTb">
				    <shiro:hasPermission name="department:create">
				            <button id="departmentAddBtn" class="addBtn" style="margin-right: 365px">增加</button>
		            </shiro:hasPermission>
				           	 部门名称: <input id="departmentSearch" type="text" class="searchInput" style="width:120px;margin-right:5px">
				            <button id="departmentSearchBtn" class="searchBtn">搜索</button>
    				</div>
    				<!--增加界面-->
				    <div id="departmentAddPanel">
				        <form id="departmentSubmitForm" method="post" action="<%=request.getContextPath()%>/web/department/${currentUser.id}/create">
				            <ul>
				          	    <li><label class="labelTitle">ID:</label><input id="departmentId" type="text" name="id" class="inputBox"></li>
				                <li><label class="labelTitle">部门代码:</label><input id="departmentCode" type="text" name="departmentCode" class="inputBox"></li>
				                <li><label class="labelTitle">部门名称:</label><input id="departmentName" type="text" name="departmentName" class="inputBox"></li>
				                <li><label class="labelTitle">所属城市:</label><select id="departmentCity" name="city">       
						                       <option value="长沙市">长沙市</option>
						                       <option value="株洲市">株洲市</option>
						                       <option value="湘潭市">湘潭市</option>
						                       <option value="衡阳市">衡阳市</option>
						                       <option value="邵阳市">邵阳市</option>
						                       <option value="常德市">常德市</option>
						                       <option value="张家界">张家界</option>
						                       <option value="益阳市">益阳市</option>
						                       <option value="娄底市">娄底市</option>
						                       <option value="郴州市">郴州市</option>
						                       <option value="永州市">永州市</option>
						                       <option value="怀化市">怀化市</option>
						                       <option value="湘西州">湘西州</option>
						                       <option value="岳阳市">岳阳市</option>
						        	   </select>
						        </li>
				                <li class="btnLi">
				                       <input type="submit" value="保存" class="inputBtn">
				                       <input id="departmentAddReset" type="reset" value="关闭" class="inputBtn">
				            </ul>
				        </form>
				    </div>
	   		 </div>  
	   		 </shiro:hasPermission>
	   		 <!-- 税务分局维护tab项 -->
	   		 <shiro:hasPermission name="revenue:list">
	   		 <div title="分局管理">  
	      	    <table id="syncTable2">
			           <thead><th>编号</th><th>标题</th></thead>
			           <tbody></tbody>
			       </table>
			       <!--工具栏-->
				    <div id="branchTb" class="syncTb">
				     <shiro:hasPermission name="revenue:create">
				            <button id="branchAddBtn" class="addBtn" style="margin-right: 365px">增加</button>
				     </shiro:hasPermission>
				           	 分局名称: <input id="branchSearch" type="text" class="searchInput"  style="width:120px;margin-right:5px">
				            <button id="branchSearchBtn" class="searchBtn">搜索</button>
    				</div>
    				<!--增加界面-->
				    <div id="branchAddPanel">
				        <form id="branchSubmitForm" method="post" action="<%=request.getContextPath()%>/web/revenue/${currentUser.id}/create">
				            <ul>
				                 <li><label class="labelTitle">ID:</label><input id="branchId" type="text" name="id" class="inputBox"></li>
				                <li><label class="labelTitle">部门代码:</label><input id="branchCode" type="text" name="revenueCode" class="inputBox"></li>
				                <li><label class="labelTitle">部门名称:</label><input id="branchName" type="text" name="revenueName" class="inputBox"></li>
				                <li><label class="labelTitle">所属城市:</label><select id="branchCity" name="cityCode">       
					                       <option value="长沙市">长沙市</option>
					                       <option value="株洲市">株洲市</option>
					                       <option value="湘潭市">湘潭市</option>
					                       <option value="衡阳市">衡阳市</option>
					                       <option value="邵阳市">邵阳市</option>
					                       <option value="常德市">常德市</option>
					                       <option value="张家界">张家界</option>
					                       <option value="益阳市">益阳市</option>
					                       <option value="娄底市">娄底市</option>
					                       <option value="郴州市">郴州市</option>
					                       <option value="永州市">永州市</option>
					                       <option value="怀化市">怀化市</option>
					                       <option value="湘西州">湘西州</option>
					                       <option value="岳阳市">岳阳市</option>
						        	   </select>
				               </li>
				                <li class="btnLi">
				                       <input type="submit" value="保存" class="inputBtn">
				                       <input id="branchAddReset" type="reset" value="关闭" class="inputBtn">
				            </ul>
				        </form>
				    </div>
	    	</div>
	    	</shiro:hasPermission>
	    	<!-- 问题类别维护tab项 -->
	    	<shiro:hasPermission name="problemCategory:list">
	    	<div title="问题类别">
	    	 	<table id="syncTable3">
			           <thead><th>编号</th><th>标题</th></thead>
			           <tbody></tbody>
			       </table>
			       <!--工具栏-->
				    <div id="categoryTb" class="syncTb">
				    <shiro:hasPermission name="problemCategory:create">
				            <button id="categoryAddBtn" class="addBtn" style="margin-right: 365px">增加</button>
				    </shiro:hasPermission>
				           	 问题类别: <input id="categorySearch" type="text" class="searchInput" style="width:120px;margin-right:5px">
				            <button id="categorySearchBtn" class="searchBtn">搜索</button>
    				</div>
    				<!--增加界面-->
				    <div id="categoryAddPanel">
				        <form id="categorySubmitForm" method="post" action="<%=request.getContextPath()%>/web/problem-category/${currentUser.id}/create">
				            <ul>
				                <li><label class="labelTitle">ID:</label><input id="categoryId" type="text" name="problemCategoryId" class="inputBox"></li>
				                <li><label class="labelTitle">问题类别名称:</label><input id="categoryContent" type="text" name="problemCategoryName" class="inputBox"></li>
				                <li><label class="labelTitle">关联产品:</label><input id="relatedProduct" name="productId" ></li>
				                <li class="btnLi">
				                       <input type="submit" value="保存" class="inputBtn">
				                       <input id="categoryReset" type="reset" value="关闭" class="inputBtn">
				            </ul>
				        </form>
				    </div>
	    	</div> 
	    	</shiro:hasPermission>
	    	<!-- 问题类型维护tab项 -->
	    	<shiro:hasPermission name="problemType:list">
	    	<div title="问题类型">
	    	 	<table id="syncTable4">
			           <thead><th>编号</th><th>标题</th></thead>
			           <tbody></tbody>
			       </table>
			       <!--工具栏-->
				    <div id="typeTb" class="syncTb">
				    <shiro:hasPermission name="problemType:create">
				            <button id="typeAddBtn" class="addBtn" style="margin-right: 365px">增加</button>
				            </shiro:hasPermission>
				           	 问题类型: <input id="typeSearch" type="text" class="searchInput"  style="width:120px;margin-right:5px">
				            <button id="typeSearchBtn" class="searchBtn">搜索</button>
    				</div>
    				<!--增加界面-->
				    <div id="typeAddPanel">
				        <form id="typeSubmitForm" method="post" action="<%=request.getContextPath()%>/web/problem-type/${currentUser.id}/create">
				            <ul>
				                <li><label class="labelTitle">ID:</label><input id="typeId" type="text" name="problemTypeId" class="inputBox"></li>
				                <li><label class="labelTitle">问题类型名称:</label><input id="typeContent" type="text" name="problemTypeName" class="inputBox"></li>
				                <li><label class="labelTitle">所属问题类别:</label><input id="typeCategory" name="problemCategoryId" ></li>
				                <li class="btnLi">
				                       <input type="submit" value="保存" class="inputBtn">
				                       <input id="typeAddReset" type="reset" value="关闭" class="inputBtn">
				            </ul>
				        </form>
				    </div>
	    	</div>
	    	</shiro:hasPermission>
	    	<!-- 产品分类  -->
	    	<shiro:hasPermission name="product:list">
	    	<div title="产品分类">
	    	 	<table id="syncTable5">
			           <thead><th>编号</th><th>标题</th></thead>
			           <tbody></tbody>
			       </table>
			       
				    <div id="productTb" class="syncTb">
				    	<shiro:hasPermission name="product:create">
				            <button id="productAddBtn" class="addBtn" style="margin-right: 365px">增加</button>
		            	</shiro:hasPermission>
    				</div>
    				
				    <div id="productAddPanel">
				        <form id="productSubmitForm" method="post" action="<%=request.getContextPath()%>/web/product/${currentUser.id}/create">
				            <ul>
				                <li><label class="labelTitle">ID:</label><input id="productId" type="text" name="productId" class="inputBox"></li>
				                <li><label class="labelTitle">产品名称:</label><input id="productName" type="text" name="productName" class="inputBox"></li>
        				        <li class="btnLi">
				                       <input type="submit" value="保存" class="inputBtn">
				                       <input id="productAddReset" type="reset" value="关闭" class="inputBtn">
				            </ul>
				        </form>
				    </div>
	    	</div>
	    	</shiro:hasPermission> 
        </div>
        
        <div class="tip">
            温馨提示:<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此页面功能是实现本平台数据与航信外服平台的数据同步，以便达到数据统一。数据包括外服人员费用项目、客户所属税务分局、部门维护、问题类型、问题类别的增加和删除。如需修改项目，请先删除后添加！ 
        </div>
    </div>
    
   <%@ include file="footer.jsp"%>
</body>
</html>