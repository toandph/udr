<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="menu-project-list" text="Project List"/></title>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />
        <link type="text/css" href="../media/js/DataTables-1.9.1/media/css/demo_table_jui.css" rel="stylesheet" />

        <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

        <![if IE]><link href="../media/css/main-ie.css" rel="stylesheet" type="text/css" /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>

        <script type="text/javascript" language="javascript" src="../media/js/DataTables-1.9.1/media/js/jquery.dataTables.js"></script>
        <script type="text/javascript" language="javascript" src="../media/js/jquery.layout-latest.min.js"></script>
        <script type="text/javascript" language="javascript" src="../media/js/jquery.textchange.min.js"></script>

        <script language="javascript" src="../media/js/init-common-2-columns-layout.js"></script>
        <script language="javascript" src="../media/js/init-button.js"></script>
        <script language="javascript" src="../media/js/init-left-menu.js"></script>


        <script type="text/javascript" language="javascript">
            $(function() {
                var oTable = $('#project-table').dataTable( {
                    "bFilter": true,
                    "bInfo": false,
                    "bPaginate": false,
                    "bLengthChange": false,
                    "bJQueryUI": true,
                    "bAutoWidth": false,
                    "aaSorting": [[ 1, "asc" ]],
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
                    },
                    "aoColumnDefs": [
                                      { "bSortable": false, "aTargets": [ 0 ] }
                                    ]
                } );

                $('.myInput').bind('textchange', function (event, previousText) {
                    oTable.fnFilter($('.myInput').val());
                })

                $("#delete-project-button").click(function() {
                    var paramString = "";
                    $(".check").each(function(){
                        if ($(this).prop('checked')) {
                            paramString += 'projectId=' + $(this).attr('pid') + '&';
                        }
                    });

                    if (paramString.length > 0) {
                         $( "#dialog-confirm" ).dialog({
                              resizable: false,
                              minHeight:0,
                              modal: true,
                              buttons: [ {text:$("#message-1").html(),
                                            click:function() {
                                                $( this ).dialog( "close" );
                                                location.href = '../project/delete?' + paramString;
                                            }
                                        },
                                         {text:$("#message-2").html(),
                                            click:function() {
                                                $( this ).dialog( "close" );
                                            }
                                          }
                                       ]
                         });
                    }
                });

                $("#checkAll").click(function(){
                    $('.check').prop('checked', $("#checkAll").prop('checked'));
                })
            });
        </script>
    </head>

    <body>
        <div class="wrapper">
            <!-- Header and Menu -->
            <%@include file="../include/header.jsp" %>
            <%@include file="../include/outside-menu.jsp" %>
            <!-- End of Header Menu -->

            <!-- Layout Content -->
            <div id="myPane">
                <div class="ui-layout-center">
                    <div style="margin-bottom:5px">
                        <table class="simple-table" width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td align="left">
                                      <button onclick="location.href='../project/add'"><spring:message code="create-project" text="Create Project"/></button>
                                      <button id="delete-project-button"><spring:message code="delete-project" text="Delete Project"/></button>
                                    </td>
                                    <td width="40%" align="right">
                                        <spring:message code="filter" text="Filter"/>: <input class="myInput" type="text" maxlength="30"/>
                                    </td>
                                </tr>
                            </table>
                      </div>

                    <table id="project-table" class="display" width="100%" cellpadding="0" cellspacing="0" border="0">
                        <thead>
                            <tr>
                                <th align="left" width="5" id="checkboxHeader"><input id="checkAll" type="checkbox" /></th>
                                <th align="left"><spring:message code="table-project-name" text="Project Name" /></th>
                                <th><spring:message code="table-new-issues" text="New Issues" /></th>
                                <th><spring:message code="table-open-issues" text="Open Issues" /></th>
                                <th><spring:message code="table-fixed-issues" text="Fixed Issues" /></th>
                                <th><spring:message code="table-total-size" text="Size" /></th>
                                <th><spring:message code="table-last-build" text="Last Build" /></th>
                                <th><spring:message code="table-source-type" text="Source Type" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${projectList}" var="project" varStatus="status">
                                <tr>
                                    <td align="left"><input class="check" pid="${project.id}" id="check${status.count}" type="checkbox" /></td>
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
                </div>

                <div class="ui-layout-west">
                    <div id="accordion">
                          <h3 id="accordionMenu"><strong><spring:message code="menu-udr-admin" text = "UDR Admin"/></strong></h3>
                          <div>
                                <div><a href="../project/list"><spring:message code="project-management" text = "Project Management"/></a></div>
                                <div><a href="../user/list"><spring:message code="user-management" text = "User Management"/></a></div>
                                <div><a href="../license/detail"><spring:message code="license-detail" text = "License Detail"/></a></div>
                          </div>
                    </div>
                </div>
            </div>
            <!-- End of Layout Content -->
        </div>

        <!-- Dialog Content -->
        <div id="dialog-confirm" title="<spring:message code="delete-project"/> ?" style="display:none">
          <p>
              <span class="ui-icon ui-icon-alert"></span>
              <spring:message code="these-item-will-be-delete-are-you-sure" text="These items will be deleted. Are you sure?"/>
          </p>
        </div>
        <span style="display:none" id="message-1"><spring:message code="delete-projects" text = "Delete Projects"/></span>
        <span style="display:none" id="message-2"><spring:message code="cancel" text = "Cancel"/></span>
        <span style="display:none" id="sZeroRecords"><spring:message code="no-records-to-display" text = "No records to display"/></span>
        <span style="display:none" id="sEmptyTable"><spring:message code="no-data-available-in-table"/></span>
        <span style="display:none" id="sInfo"><spring:message code="showing-from-to-of-total-entries"/></span>
        <span style="display:none" id="sInfoEmpty"><spring:message code="showing-from-to-of-total-entries"/></span>
        <span style="display:none" id="sInfoFiltered"><spring:message code="filter-from-total-entries"/></span>
        <span style="display:none" id="sLoadingRecords"><spring:message code="loading"/></span>
        <span style="display:none" id="sProcessing"><spring:message code="processing"/></span>
        <span style="display:none" id="sSortDescending"><spring:message code="activate-to-sort-column-descending"/></span>
        <span style="display:none" id="sSortAscending"><spring:message code="activate-to-sort-column-ascending"/></span>
    </body>
</html>