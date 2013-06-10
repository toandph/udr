<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="filter-management" text="Filter Management" /></title>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />
        <link type="text/css" href="../media/js/DataTables-1.9.1/media/css/demo_table_jui.css" rel="stylesheet" />

        <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

        <![if IE]> <link type="text/css" href="../media/css/main-ie.css" rel="stylesheet" /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>

        <script type="text/javascript" language="javascript" src="../media/js/DataTables-1.9.1/media/js/jquery.dataTables.js"></script>
        <script type="text/javascript" language="javascript" src="../media/js/jquery.layout-latest.min.js"></script>

        <script language="javascript" src="../media/js/init-common-2-columns-layout.js"></script>
        <script language="javascript" src="../media/js/init-button.js"></script>
        <script language="javascript" src="../media/js/init-left-menu.js"></script>

        <script type="text/javascript" language="javascript">
            $(function() {
                var oTable = $('#filter-table').dataTable( {
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
                        },
                        "aaSorting": [[ 1, "asc" ]],
                        "aoColumnDefs": [
                                          { "bSortable": false, "aTargets": [ 0 ] }
                                        ]
                    } );

                $('.myInput').bind('textchange', function (event, previousText) {
                    oTable.fnFilter($('.myInput').val());
                });

               $("#delete-filter-button").click(function() {
                   var paramString = "";
                   $(".check").each(function(){
                       if ($(this).prop('checked')) {
                           paramString += 'filterId=' + $(this).attr('pid') + '&';
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
                                      location.href = '../filter/delete?' + paramString;
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
            <%@include file="../include/project-menu.jsp" %>
            <!-- End of Header Menu -->

            <!-- Layout Content -->
            <div id="myPane">
                <div class="ui-layout-center">
                    <div style="margin-bottom:5px">
                        <table class="simple-table" width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                <td align="left">
                                 <button onclick="location.href='../filter/add?projectId=${projectId}'"><spring:message code="create-filter" text="Create Filter" /></button>
                                 <button id="delete-filter-button"><spring:message code="delete-filter" text="Delete Filter" /></button>
                                </td>
                                <td width="40%" align="right">
                                     <button onclick="javascript:window.open('/issue/help#create-filter','Help','width=1280,height=800');"><spring:message code="filter-guide" text="Filter Guide" /></button>
                                </td>
                                </tr>
                            </table>
                      </div>

                    <table id="filter-table" class="display" width="100%" cellpadding="0" cellspacing="0" border="0">
                        <thead>
                        <tr>
                            <th align="left" width="5" id="checkboxHeader"><input id="checkAll" type="checkbox" /></th>
                            <th align="left"><spring:message code="filter-name" text="Filter Name" /></th>
                            <th align="center"><spring:message code="build" text="Build" /></th>
                            <th align="left"><spring:message code="advance-value-brief" text="Advance Value" /></th>
                            <th align="left"><spring:message code="state" text="State" /></th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${filterList}" var="filter" varStatus="status">
                                <tr>
                                    <td align="left"><input class = "check" type="checkbox" pid="${filter.id}" id="check${status.count}" /></td>
                                    <td align="left">${filter.name}</td>
                                    <td align="center">${filter.build}</td>
                                    <td align="left">${filter.value}</td>
                                    <td align="left">${filter.state}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                     </table>
                </div>

                <div class="ui-layout-west">
                    <div id="accordion">
                        <h3 id="accordionMenu"><strong><spring:message code="filter" text="Filter"/></strong></h3>
                        <div>
                            <div><a href="../filter/list?projectId=${projectId}"><spring:message code="filter-management" text="Filter Management" /></a></div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- End of Layout Content -->
        </div>

        <!-- Dialog Content -->
        <div id="dialog-confirm" title="<spring:message code="delete-build" text = "Delete Build"/>?" style="display:none">
              <span class="ui-icon ui-icon-alert"></span>
              <spring:message code="these-filters-will-be-permanently-deleted" text = "These filters will be permanently deleted. Are you sure?"/>
        </div>

        <span style="display:none" id="message-1"><spring:message code="delete-filter" text = "Delete Filter"/></span>
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