<%@ page contentType="text/html;charset=UTF-8" language="java" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="menu-project-list" text="Project List"/></title>

        <link type="text/css" href="../media/css/main.css" rel="stylesheet" />
        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />
        <link type="text/css" href="../media/js/DataTables-1.9.1/media/css/demo_table_jui.css" rel="stylesheet" />

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>
        <script type="text/javascript" src="../media/js/DataTables-1.9.1/media/js/jquery.dataTables.js"></script>

        <script type="text/javascript" src="../media/js/init-button.js"></script>
        <script type="text/javascript" src="../media/js/jquery.textchange.min.js"></script>

        <script>
            $(function() {
                var oTable = $('#project-table').dataTable( {
                        "bFilter": true,
                        "bInfo": false,
                        "bPaginate": false,
                        "bLengthChange": false,
                        "bJQueryUI": true,
                        "bAutoWidth": false,
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
                    } );

                $('.myInput').bind('textchange', function (event, previousText) {
                    oTable.fnFilter($('.myInput').val());
                })
            });
        </script>
    </head>

    <body>
        <div class="wrapper">
             <%@include file="../include/header.jsp" %>
             <%@include file="../include/outside-menu.jsp" %>

              <div style="margin-bottom:5px">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="simple-table">
                        <tr>
                        <td align="left">
                          <button onclick="location.href='../project/outside-add'"><spring:message code="create-project" text="Create Project" /></button>
                        </td>
                        <td width="40%" align="right">
                            <spring:message code="filter" text="Filter" />: <input class="myInput" type="text" maxlength="30"/>
                        </td>
                        </tr>
                    </table>
              </div>

              <table id="project-table" width="100%" cellpadding="0" cellspacing="0" border="0" class="display">
                    <thead>
                        <tr>
                            <th align="left"><spring:message code="table-project-name" text="Project Name" /></th>
                            <th><spring:message code="table-new-issues" text="New Issues" /></th>
                            <th><spring:message code="table-open-issues" text="Open Issues" /></th>
                            <th><spring:message code="table-fixed-issues" text="Fixed Issues" /></th>
                            <th><spring:message code="table-total-size" text="Total Size" /></th>
                            <th><spring:message code="table-last-build" text="Last Build" /></th>
                            <th><spring:message code="table-source-type" text="Source Type" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${projectList}" var="project">
                            <tr>
                                <td align="left"><a href="../report/chart?projectId=${project.id}">${project.name}</a></td>
                                <td align="center"><a href="../issue/list?projectId=${project.id}&aSearch=%2Bstate:NEW">${project.lastBuild.numberOfNewIssue}</a></td>
                                <td align="center"><a href="../issue/list?projectId=${project.id}&aSearch=%2Bstate:EXISTING,NEW,REOCCURED">${project.lastBuild.numberOfOpenIssue}</a></td>
                                <td align="center"><a href="../issue/list?projectId=${project.id}&aSearch=%2Bstate:FIXED">${project.lastBuild.numberOfFixedIssue}</a></td>
                                <td align="center"><fmt:formatNumber type="number" maxFractionDigits="0" value="${project.lastBuild.size / 1000}" /> KB</td>
                                <td align="center"><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${project.lastBuild.endTime}" /></td>
                                <td align="center">${project.lastBuild.type}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
              </table>
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
    </body>
</html>