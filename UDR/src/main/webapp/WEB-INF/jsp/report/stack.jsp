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
            var colors = ['green', 'red', 'yellow', 'blue', 'black', 'pink'];
            var buildNames = [];
            var buildIds = [];
            i  = 0;
            $(".build").each(function() {
                buildNames[i] = $(this).attr("bname");
                buildIds[i] = $(this).attr("bid");
                i++;
            });

            i  = 0;
            $(".citem").each(function() {
                var noOfIssues = [];
                a = $(this).attr("cvalue").split("#");
                for (k = 0; k < a.length; k++) {
                    noOfIssues[k] = parseInt(a[k]);
                }
                if (i >= colors.length) {
                    data[i] = {name: $(this).attr("cname"), data: noOfIssues, "original":$(this).attr("original")};
                } else {
                    data[i] = {name: $(this).attr("cname"), color: colors[i], data: noOfIssues, "original":$(this).attr("original")};
                }

                i++;
            });

            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'chart-container',
                    type: 'column',
                    zoomType: 'xy'
                },
                credits: {enabled:false},
                title: {
                    text: $("#chartTitle").html()
                },
                xAxis: {
                    categories: buildNames
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: $("#totalIssue").html()
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                        }
                    }
                },
                legend: {
                    align: 'right',
                    x: -100,
                    verticalAlign: 'top',
                    y: 20,
                    floating: true,
                    backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColorSolid) || 'white',
                    borderColor: '#CCC',
                    borderWidth: 1,
                    shadow: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} ' + $("#issues").html() + ' </b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        animation: false,
                        stacking: 'normal',
                        dataLabels: {
                            enabled: true,
                            color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                        },
                        cursor: 'pointer',
                        point: {
                            events: {
                                click: function() {
                                    var state = this.series.data[this.x].series.options.original.toUpperCase();
                                    var buildid = buildIds[this.x];
                                    location.href = "../issue/list?projectId=${projectId}&buildId="+buildid+"&aSearch=%2Bstate:" + state;
                                }
                            }
                        }
                    }
                },
                series:
                     data

            });

            chart2 = new Highcharts.Chart({
                chart: {
                    renderTo: 'chart2-container',
                    type: 'line',
                    marginRight: 130,
                    marginBottom: 25
                },
                credits: {enabled:false},
                title: {
                    text: $("#chartTitle2").html(),
                    x: -20 //center
                },
                xAxis: {
                    categories: buildNames
                },
                yAxis: {
                    title: {
                        text: $("#totalIssue").html()
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }]
                },
                tooltip: {
                    valueSuffix: ' ' + $("#issues").html()
                },
                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'top',
                    x: -10,
                    y: 100,
                    borderWidth: 0
                },
                plotOptions: {
                    line: {
                        animation:false,
                        point: {
                            events: {
                                click: function() {
                                    var state = this.series.data[this.x].series.options.original.toUpperCase();
                                    var buildid = buildIds[this.x];
                                    location.href = "../issue/list?projectId=${projectId}&buildId="+buildid+"&aSearch=%2Bstate:" + state;
                                }
                            }
                        }
                    }
                },
                series: data
            });

            $("#selectFilter").change(function() {
                filterId = $("#selectFilter").val();
                location.href = "../report/chart?projectId=${projectId}&buildId=${buildId}&type=state-line&filterId=" + filterId;
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
            <div>
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
            <div id="chart-container" style="width:700px"></div>

            <div style="display:none">
                <c:forEach items="${chartInfo.items}" var="data">
                    <c:if test="${noLanguage == true}">
                        <span class="citem" cname = "${data.name}" cvalue = "${data.value}" original="${data.name}"/>
                    </c:if>
                    <c:if test="${noLanguage == false}">
                        <span class="citem" cname = "<spring:message code="${data.name}" />" cvalue = "${data.value}" original="${data.name}"/>
                    </c:if>
                </c:forEach>

                <div class="buildGroup">
                    <c:forEach items="${buildList}" var="data">
                        <span class="build" bname = "${data.name}" bid="${data.id}"/>
                    </c:forEach>
                </div>

                <span id="chartTitle"><spring:message code="${chartTitle}" /></span>
                <span id="chartTitle2"><spring:message code="state-line-chart" /></span>
                <span id="totalIssue"><spring:message code="total-issue" /></span>
                <span id="issues"><spring:message code="number-of-issues" /></span>
                <span id="total"><spring:message code="total" /></span>
            </div>

            <br/>

            <div id="chart2-container" style="width:700px"></div>
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
