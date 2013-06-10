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
            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'chart-container',
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                credits: {enabled:false},
                title: {
                    text: 'State Pie Chart'
                },
                tooltip: {
                    pointFormat: '{series.name}: <strong>{point.percentage}%</strong>',
                    percentageDecimals: 1
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            color: '#000000',
                            connectorColor: '#000000',
                            formatter: function() {
                                return '<strong>'+ this.point.name +'</strong>: '+ this.percentage +' %';
                            }
                        },
                        showInLegend: true
                    }
                },
                series: [{
                    type: 'pie',
                    animation: false,
                    name: 'Browser share',
                    data: [
                        ['Firefox',   45.0],
                        ['IE',       26.8],
                        {
                            name: 'Chrome',
                            y: 12.8,
                            sliced: true,
                            selected: true
                        },
                        ['Safari',    8.5],
                        ['Opera',     6.2],
                        ['Others',   0.7]
                    ],
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
            <div>Build:
                <select>
                    <option value="1">Build 1</option>
                </select>
                Filter:
                <select>
                    <option value="1">Filter 1</option>
                </select>
            </div>
            <div id="chart-container" ></div>
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