<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>UDR - View Chart</title>

    <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
    <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />

    <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
    <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

    <![if IE]> <link type="text/css" href="../media/css/main-ie.css" rel="stylesheet"  /><![endif]>
    <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

    <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>

    <script type="text/javascript" language="javascript" src="../media/js/jquery.layout-latest.min.js"></script>
    <script type="text/javascript" language="javascript" src="../media/js/highchart/js/highcharts.js"></script>
    <script type="text/javascript" language="javascript" src="../media/js/highchart/js/modules/exporting.js"></script>

    <script language="javascript" src="../media/js/init-chart-layout.js"></script>
    <script language="javascript" src="../media/js/init-left-menu.js"></script>
    <script language="javascript" src="../media/js/init-button.js"></script>
</head>

<body>
<div class="wrapper">
    <!-- Header and Menu -->
    <%@include file="../include/header.jsp" %>
    <%@include file="../include/project-menu.jsp" %>
    <!-- End of Header Menu -->

    <!-- Layout Content -->
    <div id="myPane">
        <div class="ui-layout-center">
            <spring:message code="there-is-no-build-in-this-project" text = "There is no build in this project" />.
            <a href="../build/add?projectId=${projectId}"><spring:message code="click-here-to-insert-a-build" text = "Click here to insert a build" /></a> .
        </div>

        <div class="ui-layout-west">
            <div id="accordion">
                <h3 id="accordionMenu"><strong><spring:message code="report-pie-chart" text="Report Pie Chart"/></strong></h3>
                <div>
                    <div><a href="../report/chart?projectId=${projectId}&buildId=${buildId}&type=state-pie"><spring:message code="state-pie-chart" text="State Pie Chart"/></a></div>
                    <div><a href="../report/chart?projectId=${projectId}&buildId=${buildId}&type=severity-pie"><spring:message code="severity-level-pie-chart" text="Severity Level Pie Chart"/></a></div>
                    <div><a href="../report/chart?projectId=${projectId}&buildId=${buildId}&type=state-line"><spring:message code="state-line-chart" text="State Line Chart"/></a></div>
                    <div><a href="../report/chart?projectId=${projectId}&buildId=${buildId}&type=error-pie"><spring:message code="error-code-pie-chart" text="Error Code Pie Chart"/></a></div>
                </div>
            </div>
        </div>
    </div>
    <!-- End of Layout Content -->
</div>
</body>
</html>