<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>UDR - <spring:message code="view-pie-chart" text="View Pie Chart" /></title>

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

    <script type="text/javascript" language="javascript">
        var chart;

        $(document).ready(function() {
            // Chart get data
            var data = [];
            var colors = ['green', 'red', 'blue', 'yellow', 'black', 'pink'];
            i  = 0;
            $(".citem").each(function() {
                if (i >= colors.length) {
                    data[i] = {name: $(this).attr("cname"), y: parseInt($(this).attr("cvalue")), original:$(this).attr("original")}
                } else {
                    data[i] = {name: $(this).attr("cname"), color: colors[i], y: parseInt($(this).attr("cvalue")), original:$(this).attr("original")}
                }
                i++;
            });

            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'chart-container',
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    zoomType: 'xy'
                },
                credits: {enabled:false},
                title: {
                    text: $("#chartTitle").html()
                },
                tooltip: {
                    pointFormat: '{series.name}: <strong>{point.percentage}%</strong>',
                    percentageDecimals: 1
                },
                plotOptions: {
                    pie: {
                        animation: false,
                        allowPointSelect: false,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            color: '#000000',
                            connectorColor: '#000000',
                            formatter: function() {
                                return '<strong>'+ this.point.name +'</strong>: '+ this.y + ' ' + $("#lblIssues").html();
                            }
                        },
                        showInLegend: true
                    }
                },
                series: [{
                    type: 'pie',
                    name: 'no.',
                    data: data,
                    point:{
                        events:{
                            click: function (event) {
                                var state = this.original.toUpperCase();
                                var type = $("#chartTypeSearch").html();
                                location.href = "../issue/list?projectId=${projectId}&aSearch=%2B" + type + ":" + state;
                            }
                        }
                    }
                }]
            });

            var buildId, filterId;
            $("#selectBuild").change(function() {
                buildId = $("#selectBuild").val();
                location.href = "../report/chart?projectId=${projectId}&buildId=" + buildId + "&type=${chartInfo.type}";
            });

            $("#selectFilter").change(function() {
                filterId = $("#selectFilter").val();
                location.href = "../report/chart?projectId=${projectId}&buildId=${buildId}&type=${chartInfo.type}&filterId=" + filterId;
            });
        });
    </script>
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
            <div><spring:message code="build" />:
                <select id="selectBuild">
                    <c:forEach items="${buildList}" var="build" varStatus="status">
                        <c:if test="${build.id == buildId}" >
                            <option selected value="${build.id}">${build.name}</option>
                        </c:if>
                        <c:if test="${build.id != buildId}" >
                            <option value="${build.id}">${build.name}</option>
                        </c:if>
                    </c:forEach>
                </select>

                <spring:message code="filter" />:
                <select id="selectFilter">
                    <option selected value="-1"><spring:message code="no-filter" /></option>
                    <c:forEach items="${filterList}" var="filter" varStatus="status">
                        <c:if test="${filter.id == filterId}" >
                            <option selected value="${filter.id}">${filter.name}</option>
                        </c:if>
                        <c:if test="${filter.id != filterId}" >
                            <option value="${filter.id}">${filter.name}</option>
                        </c:if>
                    </c:forEach>
                </select>
                <a href="/filter/add?projectId=${projectId}"><spring:message code="create-filter" /></a>
            </div>
            <div id="chart-container" ></div>
            <div style="display:none">

                <c:forEach items="${chartInfo.items}" var="data">
                    <c:if test="${noLanguage == true}">
                        <span class="citem" original= "${data.name}" cname = "${data.name}" cvalue = "${data.value}"/>
                    </c:if>
                    <c:if test="${noLanguage == false}">
                        <span class="citem" original= "${data.name}" cname = "<spring:message code="${data.name}" />" cvalue = "${data.value}"/>
                    </c:if>
                </c:forEach>

                <span id="chartTitle"><spring:message code="${chartTitle}" /></span>
                <span id="lblIssues"><spring:message code="number-of-issues" /></span>
                <span id="chartTypeSearch">${chartTypeSearch}</span>


            </div>
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
