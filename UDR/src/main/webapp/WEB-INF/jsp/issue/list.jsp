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
        <link type="text/css" href="../media/js/DataTables-1.9.1/media/css/demo_table_jui.css" rel="stylesheet" />

        <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

        <![if IE]> <link href="../media/css/main-ie.css" rel="stylesheet" type="text/css" /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <link type="text/css" href="../media/css/my-issue-list.css" rel="stylesheet" />

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>

        <script type="text/javascript" src="../media/js/DataTables-1.9.1/media/js/jquery.dataTables.js"></script>
        <script type="text/javascript" src="../media/js/DataTables-1.9.1/media/js/TableTools.js"></script>
        <script type="text/javascript" src="../media/js/jquery.layout-latest.min.js"></script>
        <script type="text/javascript" src="../media/js/init-button.js"></script>

        <script type="text/javascript" language="javascript">
            $(function() {
                $("#loadIcon").hide();
                var oldSearch = $("#aSearch").html();

                var formatRow = function (format, data, nRow) {
                    str = format;
                    var str;

                    for (var i = 0; i < data.length - 1; i++) {
                        str = str.replace("dpt_"+i, data[i]);
                    }
                    str = str.replace("dpt_8", data[0]);
                    str = str.replace("dpt_9", data[0]);
                    str = str.replace("dpt_a", data[8]);
                    str = str.replace("dpt_b", data[0]);
                    str = str.replace("dpt_c", data[0]);
                    str = str.replace("dpt_d", data[0]);

                    // Format the Row //
                    var oCell = document.createElement("TD");
                    oCell.colSpan = data.length;
                    oCell.innerHTML = str;
                    for (i = 0; i < data.length; i++) {
                        try {
                            nRow.deleteCell(0);
                        } catch (err) {

                        }
                    }
                    nRow.appendChild(oCell);
                };

                var oTable = $('#issue-table').dataTable( {
                        "bFilter":true,
                        "bInfo":true,
                        "sDom": 'T<"clear">lfrtip',
                        "bServerSide": true,
                        "bPaginate": true,
                        "bLengthChange": true,
                        "iDisplayLength":20,
                        "bProcessing": true,
                        "bSort":false,
                        "sAjaxSource": "../issue/list-json?projectId=${projectId}&buildId=${buildId}&filterId=${filterId}",
                        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                                                formatRow("<td colspan='8'><div class='issueListRow'> #<span class='issueId'>dpt_0</span>: <a href='../issue/detail?projectId=" + $("#projectId").html() + "&buildId=" + $("#buildId").html() + "&issueId=dpt_8'>dpt_7</a><br/> <span>dpt_6</span> <br/>" + $("#message-code").html() + ": <a href='#' id='issueid_dpt_9'>dpt_1</a> | " + $("#message-state").html() + ": <a href='#' id='state_issueid_dpt_b'>dpt_2</a> | " + $("#message-status").html() + ": <a href='#' id='status_issueid_dpt_c'>dpt_a</a> | " + $("#message-severity").html() + ": <a href='#' id='severity_issueid_dpt_d'>dpt_3</a> | " + $("#message-display").html() + ": dpt_4 | " + $("#message-method").html() + ": dpt_5 </div></td>",aData, nRow);
                                            }
                        ,
                        "fnDrawCallback": function( oSettings) {
                                                $("#info").html($("#issue-table_info").html());

                                                $("a[id^='issueid_']").click(function(e) {
                                                    e.preventDefault();
                                                    $("#input-search").val('+code:' + $(this).html());
                                                    oTable.fnFilter($("#input-search").val());
                                                    oldSearch = $("#input-search").val();
                                                })

                                                $("a[id^='state_issueid_']").click(function(e) {
                                                    e.preventDefault();
                                                    $("#input-search").val('+state:' + $(this).html());
                                                    oTable.fnFilter($("#input-search").val());
                                                    oldSearch = $("#input-search").val();
                                                })

                                                $("a[id^='status_issueid_']").click(function(e) {
                                                    e.preventDefault();
                                                    $("#input-search").val('+status:' + $(this).html());
                                                    oTable.fnFilter($("#input-search").val());
                                                    oldSearch = $("#input-search").val();
                                                })

                                                $("a[id^='severity_issueid_']").click(function(e) {
                                                    e.preventDefault();
                                                    $("#input-search").val('+severity:' + $(this).html());
                                                    oTable.fnFilter($("#input-search").val());
                                                    oldSearch = $("#input-search").val();
                                                })
                                           },
                        "oLanguage": {
                            "sEmptyTable":     $("#sZeroRecords").html(),
                            "sInfo":           $("#sInfo").html(),
                            "sInfoEmpty":      $("#sInfoEmpty").html(),
                            "sInfoFiltered":   $("#sInfoFiltered").html(),
                            "sLoadingRecords": $("#sLoadingRecords").html(),
                            "sProcessing":     $("#sProcessing").html(),
                            "oAria": {
                                "sSortAscending":  $("#sSortAscending").html(),
                                "sSortDescending": $("#sSortDescending").html()
                            },
                            "sZeroRecords": $("#sZeroRecords").html()
                        }
                                      });

                // Initialize the oTable with search filter
                $("#input-search").val($("#aSearch").html());
                if ($("#aSearch").html().length > 0) {
                    oTable.fnFilter($("#input-search").val());
                }

                $('#issue-table tbody').on( 'click', 'tr', function () {
                    $(this).toggleClass('row_selected');
                });

                $("#edit-selected-btn").click(function(e) {
                    e.preventDefault();
                    $( "#edit-dialog" ).dialog({
                        resizable: false,
                        height:200,
                        modal: true,
                        buttons: [ {text:$("#message-1").html(),
                            click:function() {
                                var paramString = "";
                                $(".row_selected .issueId").each(function() {
                                    id = $(this).html();
                                    paramString += "issueId=" + id + '&';
                                })

                                var status = $("#status").val();
                                var comment = $("#comment").val();

                                $( this ).dialog( "close" );
                                $("#loadIcon").show();
                                url = '../history/edit?projectId=' + $("#projectId").html() + '&buildId=' + $("#buildId").html() + '&status=' + status + '&comment=' + comment + '&' + paramString;
                                location.href = url;
                            }
                        },
                            {text:$("#message-2").html(),
                                click:function() {
                                    $( this ).dialog( "close" );
                                }
                            }
                        ]
                    });
                });

                $("#edit-all-btn").click(function(e) {
                    e.preventDefault();
                    $( "#edit-dialog" ).dialog({
                        resizable: false,
                        height:200,
                        modal: true,
                        buttons: [ {text:$("#message-1").html(),
                            click:function() {
                                var paramString = "";
                                var status = $("#status").val();
                                var comment = $("#comment").val();

                                $( this ).dialog( "close" );
                                $("#loadIcon").show();
                                url = '../history/edit-all?projectId=' + $("#projectId").html() + '&buildId=' + $("#buildId").html() + '&status=' + status + '&comment=' + comment + '&' + paramString;
                                location.href = url;
                            }
                        },
                            {text:$("#message-2").html(),
                                click:function() {
                                    $( this ).dialog( "close" );
                                }
                            }
                        ]
                    });
                });

                $("#search-btn").click(function() {
                    oTable.fnFilter($("#input-search").val());
                    oldSearch = $("#input-search").val();
                });

                $("#seek-first").click(function() {
                    oTable.fnPageChange( 'first' );
                });

                $("#seek-end").click(function() {
                    oTable.fnPageChange( 'last' );
                });

                $("#previous").click(function() {
                    oTable.fnPageChange( 'previous' );
                });

                $("#next").click(function() {
                    oTable.fnPageChange( 'next' );
                });

                $("#page-length").change(function() {
                    var oSettings = oTable.fnSettings();
                    oSettings._iDisplayLength = parseInt($("#page-length").val());
                    oTable.fnDraw();
                });

                var buildId, filterId;
                $("#selectBuild").change(function() {
                    buildId = $("#selectBuild").val();
                    location.href = "../issue/list?projectId=${projectId}&buildId=" + buildId + "&";
                });

                $("#selectFilter").change(function() {
                    filterId = $("#selectFilter").val();
                    location.href = "../issue/list?projectId=${projectId}&buildId=${buildId}&filterId=" + filterId;
                });

                $("#xmlButton").click(function(e) {
                    var sSearch = $("#input-search").val();
                    url = "../issue/download?type=xml&projectId=" + $("#projectId").html() + "&buildId=" + $("#buildId").html() + "&filterId=" + $("#filterId").html() + "&sSearch=" + oldSearch;
                    location.href = url;
                });

                $("#textButton").click(function(e) {
                    var sSearch = $("#input-search").val();
                    url = "../issue/download?type=text&projectId=" + $("#projectId").html() + "&buildId=" + $("#buildId").html() + "&filterId=" + $("#filterId").html() + "&sSearch=" + oldSearch;
                    location.href = url;
                });

                $("#csvButton").click(function(e) {
                    var sSearch = $("#input-search").val();
                    url = "../issue/download?type=csv&projectId=" + $("#projectId").html() + "&buildId=" + $("#buildId").html() + "&filterId=" + $("#filterId").html() + "&sSearch=" + oldSearch;
                    location.href = url;
                });

                $("#htmlButton").click(function(e) {
                    var sSearch = $("#input-search").val();
                    url = "../issue/download?type=html&projectId=" + $("#projectId").html() + "&buildId=" + $("#buildId").html() + "&filterId=" + $("#filterId").html() + "&sSearch=" + oldSearch;
                    location.href = url;
                });

            });
        </script>

        <style>
            .dataTables_filter, .dataTables_info, #issue-table_paginate, #issue-table_length {
                display: none;
            }
        </style>
    </head>

    <body>
        <div class="wrapper">
            <!-- Header and Menu -->
            <%@include file="../include/header.jsp" %>
            <%@include file="../include/project-menu.jsp" %>
            <!-- End of Header Menu -->

            <div style="margin-bottom:5px">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="left">
                            <input id="input-search" class="myInput" type="text" />
                            <button id="search-btn"><spring:message code="search" /></button>
                            <button onclick="javascript:window.open('../issue/help#issue','Help','width=1280,height=800');"><spring:message code="help" /></button>
                        </td>
                        <td width="40%" align="right" valign="middle">
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
                                <a href="../filter/add?projectId=${projectId}"><spring:message code="create-filter" /></a>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>

            <!-- Start div of list div (include control) -->
            <div style="border:1px solid #E9E9E9">
                <!-- Start of control div -->
                <div style="background:#E9E9E9">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td align="left">
                                <div style="padding-left:5px;">
                                    <spring:message code="show" />:
                                    <select id="page-length">
                                        <option value="20">20</option>
                                        <option value="50">50</option>
                                        <option value="70">70</option>
                                        <option value="100">100</option>
                                    </select>
                                    &nbsp;&nbsp;
                                    <a href="#" id="xmlButton" style="color:#903">xml</a>
                                    <a href="#" id="csvButton" style="color:#060">csv</a>
                                    <a href="#" id="textButton" style="color:#009">text</a>
                                    <a href="#" id="htmlButton" style="color:black">html</a>
                                    &nbsp;&nbsp;
                                    <a href="#" id="edit-selected-btn"><spring:message code="edit-selected" /></a>
                                    <a href="#" id="edit-all-btn"><spring:message code="edit-all" /></a>
                                    &nbsp; <img width="10" height="10" src="../media/images/ajax-loader.gif" id="loadIcon" style="margin-bottom:.25em; vertical-align:middle;">
                                </div>
                            </td>
                            <td width="40%" align="right" valign="middle">
                                <button id="seek-first"><spring:message code="seek-first" /></button>
                                <button id="previous"><spring:message code="previous" /></button>
                                <span id="info"></span>
                                <button id="next"><spring:message code="next" /></button>
                                <button id="seek-end"><spring:message code="seek-end" /></button>
                            </td>
                            </tr>
                    </table>
                </div>
                <!-- End of control div -->

                <!-- Start of table list -->
                <table id="issue-table" class="display" width="100%" cellpadding="0" cellspacing="0" border="0">
                    <thead>
                        <tr>
                            <th align="left">Id</th>
                            <th>Code</th>
                            <th>State</th>
                            <th>Severity</th>
                            <th>Display</th>
                            <th>Method</th>
                            <th>Filename</th>
                            <th>Message</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
                <!-- End of table list -->
                <span style="display:none" id="message-1"><spring:message code="edit" text = "Edit"/></span>
                <span style="display:none" id="message-2"><spring:message code="cancel" text = "Cancel"/></span>
                <span style="display:none" id="message-code"><spring:message code="code" text = "Code"/></span>
                <span style="display:none" id="message-state"><spring:message code="state" text = "State"/></span>
                <span style="display:none" id="message-severity"><spring:message code="severity" text = "Severity"/></span>
                <span style="display:none" id="message-display"><spring:message code="display" text = "Display"/></span>
                <span style="display:none" id="message-method"><spring:message code="method" text = "Method"/></span>
                <span style="display:none" id="message-status"><spring:message code="status" text = "Status"/></span>
                <span style="display:none" id="projectId">${projectId}</span>
                <span style="display:none" id="buildId">${buildId}</span>
                <span style="display:none" id="filterId">${filterId}</span>
                <span style="display:none" id="aSearch">${aSearch}</span>

                <span style="display:none" id="sZeroRecords"><spring:message code="no-records-to-display" text = "No records to display"/></span>
                <span style="display:none" id="sEmptyTable"><spring:message code="no-data-available-in-table"/></span>
                <span style="display:none" id="sInfo"><spring:message code="showing-from-to-of-total-entries"/></span>
                <span style="display:none" id="sInfoEmpty"><spring:message code="showing-from-to-of-total-entries"/></span>
                <span style="display:none" id="sInfoFiltered"><spring:message code="filter-from-total-entries"/></span>
                <span style="display:none" id="sLoadingRecords"><spring:message code="loading"/></span>
                <span style="display:none" id="sProcessing"><spring:message code="processing"/></span>
                <span style="display:none" id="sSortDescending"><spring:message code="activate-to-sort-column-descending"/></span>
                <span style="display:none" id="sSortAscending"><spring:message code="activate-to-sort-column-ascending"/></span>
            </div>
            <!-- End of list div (include control) -->
        </div>

        <!-- Dialog Content -->
        <div id="edit-dialog" title="<spring:message code="edit-dialog" />" style="display:none">
            <table width="100%">
                <tr>
                    <td width="50"><spring:message code="status" />: </td>
                    <td>
                        <select id="status">
                            <option value="Analyze">  <spring:message code="analyze" text="Analyze" /> </option>
                            <option value="Ignore">  <spring:message code="ignore" text="Ignore" /> </option>
                            <option value="Not a problem">  <spring:message code="not-a-problem" text="Not a problem" /> </option>
                            <option value="Fix in next release">  <spring:message code="fix-in-next-release" text="Fix in next release" /> </option>
                            <option value="Fix in later release">  <spring:message code="fix-in-later-release" text="Fix in later release" /> </option>
                            <option value="Defer">  <spring:message code="defer" text="Defer" /> </option>
                            <option value="Filter">  <spring:message code="filter" text="Filter" /> </option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><spring:message code="comment" />: </td>
                    <td>
                        <textarea id="comment" style="width:100%" rows="4" cols="4"></textarea>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>