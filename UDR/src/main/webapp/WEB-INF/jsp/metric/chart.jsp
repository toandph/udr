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

    <link href="../media/js/dynatree/ui.dynatree.css" rel="stylesheet" type="text/css" id="skinSheet"/>
    <link type="text/css" href="../media/css/my-dynatree.css" rel="stylesheet"/>

    <![if IE]> <link type="text/css" href="../media/css/main-ie.css" rel="stylesheet"  /><![endif]>
    <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

    <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>
    <script src="../media/js/dynatree/jquery.cookie.js" type="text/javascript"></script>
    <script src="../media/js/dynatree/jquery.dynatree.js" type="text/javascript"></script>

    <script type="text/javascript" language="javascript" src="../media/js/jquery.layout-latest.min.js"></script>
    <script type="text/javascript" language="javascript" src="../media/js/highchart/js/highcharts.js"></script>
    <script type="text/javascript" language="javascript" src="../media/js/highchart/js/modules/exporting.js"></script>
    <script language="javascript" src="../media/js/init-metrics-layout.js"></script>
    <script language="javascript" src="../media/js/init-button.js"></script>

    <script type="text/javascript" language="javascript">
        var chart;
        $(document).ready(function() {
            $( "#tabs" ).tabs();

            $("#tree").dynatree({
                checkbox:true,
                selectMode:3,
                onSelect: function(select, node) {
                    var selNodes = node.tree.getSelectedNodes(true);
                    var selKeys = $.map(selNodes, function(node){
                        return '*' + node.data.buildPath + '*';
                    });
                    $("#scope").val((selKeys.join(",")));
                },
                minExpandLevel:1,
                imagePath:null,
                classNames:{
                    nodeIcon:"dynatree-icon"
                },
                onExpand:function(node) {
                    $(".dynatree-title").click(function() {
                        $(".dynatree-title").attr("style","color:black; display:inline-block");
                        $(this).attr("style","color:red; display:inline-block");
                    });
                },
                onLazyRead: function(node){
                    node.appendAjax({
                        url: "../filter/zip-json?projectId=${projectId}"
                    });
                }
            });

            $(".dynatree-title").click(function() {
                $(".dynatree-title").attr("style","color:black; display:inline-block");
                $(this).attr("style","color:red; display:inline-block");
            });

            var chart1 = new Highcharts.Chart({
                chart: {
                    renderTo: 'chart-container1',
                    type: 'scatter',
                    zoomType: 'xy'
                },
                credits: {enabled:false},
                title: {
                    text: 'Complexity Scatter Chart'
                },
                subtitle: {
                    text: 'Source: Imagix4D'
                },
                xAxis: {
                    title: {
                        enabled: true,
                        text: 'Line Of Code'
                    },
                    startOnTick: true,
                    endOnTick: true,
                    showLastLabel: true
                },
                yAxis: {
                    title: {
                        text: 'Complexity'
                    }
                },
                legend: {
                    layout: 'vertical',
                    align: 'left',
                    verticalAlign: 'top',
                    x: 100,
                    y: 70,
                    floating: true,
                    backgroundColor: '#FFFFFF',
                    borderWidth: 1
                },
                plotOptions: {
                    scatter: {
                        marker: {
                            radius: 5,
                            states: {
                                hover: {
                                    enabled: true,
                                    lineColor: 'rgb(100,100,100)'
                                }
                            }
                        },
                        states: {
                            hover: {
                                marker: {
                                    enabled: false
                                }
                            }
                        },
                        tooltip: {
                            headerFormat: '<b>{series.name}</b><br>',
                            pointFormat: '{point.x} cm, {point.y} kg'
                        }
                    }
                },
                series: [{
                    name: 'File',
                    color: 'rgba(223, 83, 83, .5)',
                    data: [[161.2, 51.6], [167.5, 59.0], [159.5, 49.2], [157.0, 63.0], [155.8, 53.6],
                        [170.0, 59.0], [159.1, 47.6], [166.0, 69.8], [176.2, 66.8], [160.2, 75.2],
                        [172.5, 55.2], [170.9, 54.2], [172.9, 62.5], [153.4, 42.0], [160.0, 50.0],
                        [147.2, 49.8], [168.2, 49.2], [175.0, 73.2], [157.0, 47.8], [167.6, 68.8],
                        [159.5, 50.6], [175.0, 82.5], [166.8, 57.2], [176.5, 87.8], [170.2, 72.8],
                        [174.0, 54.5], [173.0, 59.8], [179.9, 67.3], [170.5, 67.8], [160.0, 47.0],
                        [154.4, 46.2], [162.0, 55.0], [176.5, 83.0], [160.0, 54.4], [152.0, 45.8],
                        [162.1, 53.6], [170.0, 73.2], [160.2, 52.1], [161.3, 67.9], [166.4, 56.6],
                        [168.9, 62.3], [163.8, 58.5], [167.6, 54.5], [160.0, 50.2], [161.3, 60.3],
                        [167.6, 58.3], [165.1, 56.2], [160.0, 50.2], [170.0, 72.9], [157.5, 59.8],
                        [167.6, 61.0], [160.7, 69.1], [163.2, 55.9], [152.4, 46.5], [157.5, 54.3],
                        [168.3, 54.8], [180.3, 60.7], [165.5, 60.0], [165.0, 62.0], [164.5, 60.3],
                        [156.0, 52.7], [160.0, 74.3], [163.0, 62.0], [165.7, 73.1], [161.0, 80.0],
                        [162.0, 54.7], [166.0, 53.2], [174.0, 75.7], [172.7, 61.1], [167.6, 55.7],
                        [151.1, 48.7], [164.5, 52.3], [163.5, 50.0], [152.0, 59.3], [169.0, 62.5],
                        [164.0, 55.7], [161.2, 54.8], [155.0, 45.9], [170.0, 70.6], [176.2, 67.2],
                        [170.0, 69.4], [162.5, 58.2], [170.3, 64.8], [164.1, 71.6], [169.5, 52.8],
                        [163.2, 59.8], [154.5, 49.0], [159.8, 50.0], [173.2, 69.2], [170.0, 55.9],
                        [161.4, 63.4], [169.0, 58.2], [166.2, 58.6], [159.4, 45.7], [162.5, 52.2]]

                }, {
                    name: 'Function',
                    color: 'rgba(119, 152, 191, .5)',
                    data: [[174.0, 65.6], [175.3, 71.8], [193.5, 80.7], [186.5, 72.6], [187.2, 78.8],
                        [181.5, 74.8], [184.0, 86.4], [184.5, 78.4], [175.0, 62.0], [184.0, 81.6],
                        [180.0, 76.6], [177.8, 83.6], [192.0, 90.0], [176.0, 74.6], [174.0, 71.0],
                        [184.0, 79.6], [192.7, 93.8], [171.5, 70.0], [173.0, 72.4], [176.0, 85.9],
                        [176.0, 78.8], [180.5, 77.8], [172.7, 66.2], [176.0, 86.4], [173.5, 81.8],
                        [178.0, 89.6], [180.3, 82.8], [180.3, 76.4], [164.5, 63.2], [173.0, 60.9],
                        [183.5, 74.8], [175.5, 70.0], [188.0, 72.4], [189.2, 84.1], [172.8, 69.1],
                        [170.0, 59.5], [182.0, 67.2], [170.0, 61.3], [177.8, 68.6], [184.2, 80.1],
                        [186.7, 87.8], [171.4, 84.7], [172.7, 73.4], [175.3, 72.1], [180.3, 82.6],
                        [182.9, 88.7], [188.0, 84.1], [177.2, 94.1], [172.1, 74.9], [167.0, 59.1],
                        [169.5, 75.6], [174.0, 86.2], [172.7, 75.3], [182.2, 87.1], [164.1, 55.2],
                        [163.0, 57.0], [171.5, 61.4], [184.2, 76.8], [174.0, 86.8], [174.0, 72.2],
                        [177.0, 71.6], [186.0, 84.8], [167.0, 68.2], [171.8, 66.1], [182.0, 72.0],
                        [167.0, 64.6], [177.8, 74.8], [164.5, 70.0], [192.0, 101.6], [175.5, 63.2],
                        [171.2, 79.1], [181.6, 78.9], [167.4, 67.7], [181.1, 66.0], [177.0, 68.2],
                        [174.5, 63.9], [177.5, 72.0], [170.5, 56.8], [182.4, 74.5], [197.1, 90.9],
                        [180.1, 93.0], [175.5, 80.9], [180.6, 72.7], [184.4, 68.0], [175.5, 70.9],
                        [180.6, 72.5], [177.0, 72.5], [177.1, 83.4], [181.6, 75.5], [176.5, 73.0]]
                }, {
                    name: 'Directory',
                    color: 'rgba(0, 255, 0, .5)',
                    data: [[177.0, 65.6], [177, 11], [177, 44], [123, 33], [122, 222],
                        [177, 33], [177, 44], [177.5, 78.4], [175.0, 62.0], [184.0, 81.6]]
                }, {
                    name: 'Class',
                    color: 'rgba(0, 0, 0, .5)',
                    data: []
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
            <table width="100%">
                <tr>
                    <td width="250" valign="top">
                        Show maximum:
                        <select>
                            <option>50</option>
                        </select>
                        of:
                        <select>
                            <option>Line Of Code</option>
                        </select>
                        <br/>
                        Metric x :
                        <select>
                            <option>Line Of Code</option>
                        </select>

                        <br/>

                        Metric y :
                        <select>
                            <option>Cyclo Complexity</option>
                        </select>

                        <br/> <br/>
                        <button>View</button>
                    </td>
                    <td align="left" width="700">
                        <div id="chart-container1" style="width:600px"></div>
                    </td>
                    <td align="left" valign="top">
                        <div>
                            <input checked="checked" style="visibility:hidden" type="checkbox" id="dummy-checkbox">
                            <b>Scope:</b>
                        </div>
                        <!-- Tree data -->
                        <div id="zipContent" style="overflow:auto; height:300px; width:300px">
                            <div id="tree">
                                <ul>
                                    <li class='noLink' data="isLazy:true, isFolder:true, addClass: 'ws-wrap', buildPath:''"><span>System Model</span></li>
                                </ul>
                            </div>

                        </div>
                        <!-- End of tree data -->
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <hr style="color:#DDD"/>
                        <table width="100%">
                            <tr>
                                <td valign="top" width="250">
                                    <div style="line-height:17px">
                                        <b>Metrics</b> <br/>
                                        Decision Depth: 100 <br/>
                                        FncKnots: 22 <br/>
                                        Fan In: 23 <br/>
                                        Transitive Fan In: 43  <br/>
                                        Fan out: 323   <br/>
                                        Transitive Fan out: 3234    <br/>
                                        Intelligent Content: 322   <br/>
                                        Mental Effort: 3243  <br/>
                                        Program Difficulty: 23432  <br/>
                                        Program Volume: 22   <br/>
                                        Cyclomatic Complexity: 11  <br/>
                                        Decision Density: 12   <br/>
                                        Essential Complexity: 12  <br/>
                                        Essential Density: 44   <br/>
                                        Parameters: 33 <br/>
                                        Returns:  11   <br/>
                                        Variables in Function: 23   <br/>
                                        Coverage: 23     <br/>
                                        Frequency: 33    <br/>
                                        Time: 222     <br/>
                                        Lines in Function: 33  <br/>
                                        Lines of Source Code: 43    <br/>
                                        Global Vars Used: 43   <br/>
                                        Static Vars Used: 33  <br/>
                                    </div>
                                </td>

                                <td valign="top" width="250">
                                    <div style="line-height:17px">
                                        <b>Direct Relationship:</b> <br/>
                                        Caller: <br/>
                                        <ul>
                                            <li>fnDoing(int a, int b)</li>
                                            <li>fnDoing(int a, int b)</li>
                                            <li>fnDoing(int a, int b)</li>
                                        </ul>

                                        Callee: <br/>
                                        <ul>
                                            <li>fnDoing(int a, int c)</li>
                                            <li>fnDoing(int a, int c)</li>
                                            <li>fnDoing(int a, int c)</li>
                                        </ul>
                                    </div>
                                </td>
                                <td valign="top" width="250">
                                    <div style="line-height:17px;">
                                        <b>Search:</b><br/>
                                        Input: <input type="text" /> <button>Go</button><br/>
                                        <div style="margin-top:10px">
                                            <b>Result:</b><br/>
                                        </div>
                                    </div>
                                </td>


                                <td valign="top">
                                    <div style="line-height:17px">
                                        <b>Entity Information</b> <br/>
                                        Decision Depth: 100 <br/>
                                        FncKnots: 22 <br/>
                                        Fan In: 23 <br/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
        <div class="ui-layout-west">

                       <div style="line-height:17px;">
                            <b>Project Information:</b><br/>
                           Project Size, KBytes: 1234 <br/>
                           Lines in Project: 3434 <br/>
                           Lines of Source Code: 1234 <br/>

                       </div>
        </div>
    </div>


    <!-- End of Layout Content -->
</div>
</body>
</html>
