<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="user-management" text="User Management" /></title>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />
        <link type="text/css" href="../media/js/DataTables-1.9.1/media/css/demo_table_jui.css" rel="stylesheet" />

        <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

        <![if IE]> <link type="text/css" href="../media/css/main-ie.css" rel="stylesheet"  /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>
        <script type="text/javascript" src="../media/js/jquery.textchange.min.js"></script>

        <script type="text/javascript" language="javascript" src="../media/js/DataTables-1.9.1/media/js/jquery.dataTables.js"></script>
        <script type="text/javascript" language="javascript" src="../media/js/jquery.layout-latest.min.js"></script>

        <script language="javascript" src="../media/js/init-common-2-columns-layout.js"></script>
        <script language="javascript" src="../media/js/init-button.js"></script>
        <script language="javascript" src="../media/js/init-left-menu.js"></script>

        <script>
            $(function() {
                var oTable = $('#user-list-table').dataTable({
                    "bFilter": true,
                    "bInfo": false,
                    "bPaginate": false,
                    "bLengthChange": false,
                    "bJQueryUI": true,
                    "bAutoWidth": false,
                    "aaSorting": [
                        [ 1, "asc" ]
                    ],
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
                });

                $('.myInput').bind('textchange', function (event, previousText) {
                    oTable.fnFilter($('.myInput').val());
                });

                $("#delete-user-button").click(function() {
                    var paramString = "";
                    $(".check").each(function(){
                        if ($(this).prop('checked')) {
                            paramString += 'userId=' + $(this).attr('pid') + '&';
                        }
                    });

                    if (paramString.length > 0) {
                        $( "#dialog-confirm" ).dialog({
                            resizable: false,
                            height:150,
                            modal: true,
                            buttons: [ {text:$("#message-1").html(),
                                click:function() {
                                    $( this ).dialog( "close" );
                                    location.href = '../user/delete?' + paramString;
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
                <!-- Right Side -->
                <div class="ui-layout-center">
                    <div style="margin-bottom:5px">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="simple-table">
                            <tr>
                                <td align="left">
                                    <button onclick="location.href='../user/add'"><spring:message code="create-user" text="Create User" /></button>
                                    <button id="delete-user-button"><spring:message code="delete-user" text="Delete User" /></button>
                                </td>
                                <td width="40%" align="right">
                                    <label for="filterInput"><spring:message code="filter" text="Filter" />: </label>
                                    <input class="myInput" id="filterInput" type="text" maxlength="30"/>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <table id="user-list-table" class="display" width="100%" cellpadding="0" cellspacing="0" border="0">
                        <thead>
                        <tr>
                            <th align="left" width="5" id="checkboxHeader"><input id="checkAll" type="checkbox"/></th>
                            <th align="left" width="200"><spring:message code="username" text="Username" /></th>
                            <th align="left" width="300"><spring:message code="email" text="Email" /></th>
                            <th align="left"><spring:message code="role" text="Role" /></th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${userList}" var="user" varStatus="status">
                                <tr>
                                    <td align="left"><input class = "check" type="checkbox" pid="${user.id}" id="check${status.count}"/></td>
                                    <td align="left"><a href="../user/detail?id=${user.username}">${user.username}</a></td>
                                    <td align="left">${user.email}</td>
                                    <td align="left">${user.roleInString}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                     </table>
                </div>
                <!-- End of Right Side -->

                <!-- Left Side -->
                <div class="ui-layout-west">
                    <div id="accordion">
                          <h3 id="accordionMenu"><strong><spring:message code="menu-udr-admin" text="UDR Admin" /></strong></h3>
                          <div>
                                <div><a href="../project/list"><spring:message code="project-management" text="Project Management" /></a></div>
                                <div><a href="../user/list"><spring:message code="user-management" text="User Management" /></a></div>
                              <div><a href="../license/detail"><spring:message code="license-detail" text = "License Detail"/></a></div>
                          </div>
                    </div>
                </div>
                <!-- End of Left Side -->
            </div>
            <!-- End of Layout Content -->
        </div>

        <!-- Dialog Content -->
        <div id="dialog-confirm" title="<spring:message code="delete-user" text="Delete User"/> ?" style="display:none">
          <p>
              <span class="ui-icon ui-icon-alert"></span>
              <spring:message code="these-users-will-be-permanently-deleted" text = "These users will be deleted. Are you sure?"/>
          </p>
        </div>

        <span style="display:none" id="message-1"><spring:message code="delete-user" text = "Delete User"/></span>
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