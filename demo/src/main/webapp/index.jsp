<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.SQLException" %>
<%
    String name = request.getParameter("name"); // 获取输入的用户名
    String sex = request.getParameter("sex"); // 获取输入的性别
    String age = request.getParameter("age"); // 获取输入的年龄
    if (name != null && sex != null && age != null) { // 判断用户名、性别、年龄是否为空
//        name = new String(name.getBytes("iso-8859-1"), "utf-8"); // 对用户名进行转码
//        sex = new String(sex.getBytes("iso-8859-1"), "utf-8"); // 对性别进行转码
//        age = new String(age.getBytes("iso-8859-1"), "utf-8"); // 对年龄进行转码
        String url = "jdbc:mysql://localhost:3306/STU"; // 数据库连接URL
        String user = "root"; // 数据库用户名
        String passwords = "123456"; // 数据库密码
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载数据库驱动
            Connection connection = DriverManager.getConnection(url, user, passwords); // 获取数据库连接
            String insertQuery = "INSERT INTO Stus (name, sex, age) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);//预先定义一个带有占位符的SQL查询或更新语句，然后在执行之前将实际的参数值绑定到占位符上
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, sex);
            preparedStatement.setString(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
%>
<%--返回的值--%>
<%="name:"+name%>
<%="sex:"+sex%>
<%="age:"+age%>
<%--<%="ok"%>--%>
<%}%>
<%--<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--<head>--%>
<%--    <title>JSP - Hello World</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--<h1><%= "Hello World!" %>--%>
<%--</h1>--%>
<%--<br/>--%>
<%--<a href="exampleServlet">Hello Servlet</a>--%>
<%--</body>--%>
<%--</html>--%>
