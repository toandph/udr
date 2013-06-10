<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>UDR - <spring:message code="issue-list" /></title>

    <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
    <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />

    <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
    <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

    <![if IE]> <link href="../media/css/main-ie.css" rel="stylesheet" type="text/css" /><![endif]>
    <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

    <link type="text/css" href="../media/css/my-issue-list.css" rel="stylesheet" />

    <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>
</head>

<body>
    <div class="wrapper">
        <!-- Header and Menu -->
        <%@include file="../include/header.jsp" %>
        <%@include file="../include/project-menu.jsp" %>
        <!-- End of Header Menu -->

        <div class="ui-layout-center">
            <spring:message code="there-is-no-build-in-this-project" text = "There is no build in this project" />.
            <a href="../build/add?projectId=${projectId}"><spring:message code="click-here-to-insert-a-build" text = "Click here to insert a build" /></a> .
        </div>
    </div>
</body>
</html>