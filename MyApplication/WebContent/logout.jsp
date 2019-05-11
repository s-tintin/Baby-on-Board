<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Baby On Board | Logout</title>
</head>
<body>
<script type="text/javascript">localStorage.clear();</script>
<%
	session.invalidate();
	response.sendRedirect(request.getContextPath()+"/index");
%>


</body>
</html>