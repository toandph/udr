<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="add-filter" text="Add Filter" /></title>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />
        <link type="text/css" href="../media/js/DataTables-1.9.1/media/css/demo_table_jui.css" rel="stylesheet" />

        <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

        <![if IE]><link href="../media/css/main-ie.css" rel="stylesheet" type="text/css" /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <link href="../media/js/dynatree/ui.dynatree.css" rel="stylesheet" type="text/css" id="skinSheet"/>
        <link type="text/css" href="../media/css/my-dynatree.css" rel="stylesheet"/>

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>

        <script type="text/javascript" language="javascript" src="../media/js/DataTables-1.9.1/media/js/jquery.dataTables.js"></script>
        <script type="text/javascript" language="javascript" src="../media/js/jquery.layout-latest.min.js"></script>
        <script src="../media/js/dynatree/jquery.cookie.js" type="text/javascript"></script>
        <script src="../media/js/dynatree/jquery.dynatree.js" type="text/javascript"></script>

        <script language="javascript" src="../media/js/init-common-2-columns-layout.js"></script>
        <script language="javascript" src="../media/js/init-button.js"></script>
        <script language="javascript" src="../media/js/init-left-menu.js"></script>

        <script language="javascript">
            $(function() {
                $("input[id^='status']").click(function() {
                    var numCheck = 0;
                    var numTotal = 0;
                    $("input[id^='status']").each(function() {
                        if ($(this).prop('checked')) {
                            numCheck++;
                        }
                        numTotal++;
                    });
                    if (numCheck == numTotal) {
                        $("#select-all-status-checkbox").prop('checked', true);
                        $("#select-all-status-checkbox").trigger('click');
                        $("#select-all-status-checkbox").prop('checked', true);
                    } else if (numCheck == 0) {
                        $("#ignore-status-checkbox").prop('checked', true);
                        $("#ignore-status-checkbox").trigger('click');
                        $("#ignore-status-checkbox").prop('checked', true);
                    } else {
                        $("#ignore-status-checkbox").prop('checked', false);
                        $("#select-all-status-checkbox").prop('checked', false);
                    }
                });

                $("input[id^='state']").click(function() {
                    var numCheck = 0;
                    var numTotal = 0;
                    $("input[id^='state']").each(function() {
                        if ($(this).prop('checked')) {
                            numCheck++;
                        }
                        numTotal++;
                    });
                    if (numCheck == numTotal) {
                        $("#select-all-state-checkbox").prop('checked', true);
                        $("#select-all-state-checkbox").trigger('click');
                        $("#select-all-state-checkbox").prop('checked', true);
                    } else if (numCheck == 0) {
                        $("#ignore-state-checkbox").prop('checked', true);
                        $("#ignore-state-checkbox").trigger('click');
                        $("#ignore-state-checkbox").prop('checked', true);
                    } else {
                        $("#select-all-state-checkbox").prop('checked', false);
                        $("#ignore-state-checkbox").prop('checked', false);
                    }
                });

                $("#ignore-status-checkbox").on('click', function() {
                    if ($(this).prop('checked')) {
                        $("input[id^='status']").attr("disabled", true);
                    } else {
                        $("input[id^='status']:first").prop('checked', true);
                        $("input[id^='status']").removeAttr('disabled');
                    }
                });

                $("#select-all-status-checkbox").on('click', function() {
                    if ($(this).prop('checked')) {
                        $("input[id^='status']").prop('checked', true);
                    } else {
                        $("input[id^='status']").prop('checked', false);
                        $("#ignore-status-checkbox").prop('checked', true);
                        $("#ignore-status-checkbox").trigger('click');
                        $("#ignore-status-checkbox").prop('checked', true);
                    }
                });

                $("#ignore-state-checkbox").on('click', function() {
                    if ($(this).prop('checked')) {
                        $("input[id^='state']").attr("disabled", true);
                    } else {
                        $("input[id^='state']:first").prop('checked', true);
                        $("input[id^='state']").removeAttr('disabled');
                    }
                });

                $("#select-all-state-checkbox").on('click', function() {
                    if ($(this).prop('checked')) {
                        $("input[id^='state']").prop('checked', true);
                    } else {
                        $("input[id^='state']").prop('checked', false);
                        $("#ignore-state-checkbox").prop('checked', true);
                        $("#ignore-state-checkbox").trigger('click');
                        $("#ignore-state-checkbox").prop('checked', true);
                    }
                });

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
                    <div class="main-form">
                        <div class="main-form-header"><spring:message code="create-filter" text="Create Filter" /></div>
                        <form:form commandName="filterModel" action="../filter/add?projectId=${projectId}">
                            <table width="600" border="0" cellpadding="2" cellspacing="4">
                                 <tr>
                                     <td align="left" valign="middle">
                                         <spring:message code="filter-name" text="Filter Name" />: <br/>
                                         <form:input path="filterName" class="myInput" style="width:100%" type="text"/>
                                     </td>
                                 </tr>
                                 <tr>
                                    <td align="left" valign="middle">
                                        <spring:message code="apply-on-build" text="Apply on build ('all' for all builds in project or build number: 1,2,3)" />:
                                        <br/>
                                        <form:input path="onBuild" class="myInput" style="width:100%" type="text"/>
                                    </td>
                                 </tr>
                                 <tr>
                                    <td align="left" valign="middle">
                                        <spring:message code="advance-value" text="Advance value (ex: +label1:value1,value2;label2:value3,value4;)" />
                                        : <br/>
                                        <form:input path="value" class="myInput" style="width:100%" type="text" value=""/>
                                    </td>
                                 </tr>
                                 <tr>
                                    <td>
                                        <!-- Start directory list table -->
                                        <table class="simple-table" width="100%" border="0" cellpadding="0" cellspacing="4">
                                            <tr>
                                                <!-- Select state cell -->
                                                <td align="left" valign="top" width="150">
                                                    <div>
                                                        <input checked="checked" type="checkbox" id="select-all-state-checkbox">
                                                        <b><spring:message code="state-choose" />:</b>
                                                    </div>
                                                    <div>
                                                        <form:checkbox path="state" value="New"/> <spring:message code="new" text="New" /> <br/>
                                                        <form:checkbox path="state" value="Existing"/> <spring:message code="existing" text="Existing" /> <br/>
                                                        <form:checkbox path="state" value="Not in scope"/> <spring:message code="not-in-scope" text="Not in scope" /> <br/>
                                                        <form:checkbox path="state" value="Fixed"/> <spring:message code="fixed" text="Fixed" /> <br/>
                                                        <form:checkbox path="state" value="Recurred"/> <spring:message code="recurred" text="Recurred" /> <br/>
                                                        <form:checkbox path="state" value="Obsolete"/> <spring:message code="obsolete" text="Obsolete" /> <br/>
                                                        <hr align="left" width="60%"/>
                                                        <input type="checkbox" id="ignore-state-checkbox" title="<spring:message code="ignore" />"> <spring:message code="ignore" />
                                                    </div>
                                                </td>
                                                <!-- End of select state cell -->

                                                <!-- Select status cell -->
                                                <td align="left" valign="top" width="150">
                                                    <div>
                                                        <input checked="checked" type="checkbox" id="select-all-status-checkbox">
                                                        <b><spring:message code="status-choose" />:</b>
                                                    </div>
                                                    <div>
                                                        <form:checkbox path="status" value="Analyze"/>  <spring:message code="analyze" text="Analyze" /> <br/>
                                                        <form:checkbox path="status" value="Ignore"/> <spring:message code="ignore" text="Existing" /> <br/>
                                                        <form:checkbox path="status" value="Not a problem"/> <spring:message code="not-a-problem" text="Not a problem" /> <br/>
                                                        <form:checkbox path="status" value="Fix in next release"/> <spring:message code="fix-in-next-release" text="Fix in next release" /> <br/>
                                                        <form:checkbox path="status" value="Fix in later release"/> <spring:message code="fix-in-later-release" text="Fix in later release" /> <br/>
                                                        <form:checkbox path="status" value="Defer"/> <spring:message code="defer" text="Defer" /> <br/>
                                                        <form:checkbox path="status" value="Filter"/> <spring:message code="filter" text="Filter" /> <br/>
                                                        <hr align="left" width="70%"/>
                                                        <input type="checkbox" id="ignore-status-checkbox" title="<spring:message code="ignore" />"> <spring:message code="ignore" />
                                                    </div>
                                                </td>
                                                <!-- End of select status cell -->

                                                <!-- Tree cell -->
                                                <td align="left" valign="top">
                                                    <div>
                                                        <input checked="checked" style="visibility:hidden" type="checkbox" id="dummy-checkbox">
                                                        <b><spring:message code="directory-choose" text="Choose Directory" />:</b>
                                                        <c:if test="${noBuild}"><span class="ui-state-error-text"><spring:message code="no-build" /></span></c:if>
                                                    </div>
                                                    <!-- Tree data -->
                                                    <div id="zipContent">
                                                        <div id="tree">
                                                            <ul>
                                                                <li class='noLink' data="isLazy:true, isFolder:true, addClass: 'ws-wrap', buildPath:''"><span>System Model</span></li>
                                                            </ul>
                                                        </div>

                                                    </div>
                                                    <!-- End of tree data -->
                                                 </td>
                                                <!-- End of tree cell -->
                                            </tr>
                                        </table>
                                        <!-- End of sub table -->
                                        <br/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right" class="special-for-form-submit-button">
                                        <button onclick="location.href='../filter/list'"><spring:message code="create-filter" text="Create Filter" /></button>
                                        <button type="button" onclick="location.href='../filter/list?projectId=${projectId}'"><spring:message code="cancel" text="Cancel" /></button>
                                    </td>
                                </tr>
                            </table>
                            <!-- End of main table -->
                            <form:hidden path="scope" id="scope"/>
                            <form:hidden path="zipContent"/>
                            <form:errors path="*" cssClass="ui-state-error-text"/>
                        </form:form>
                        <!-- End of main form -->
                    </div>
                </div>
                <!-- End of Right side -->

                <div class="ui-layout-west">
                    <div id="accordion">
                        <h3 id="accordionMenu"><strong><spring:message code="filter" text="Filter" /></strong></h3>
                        <div>
                            <div><a href="../filter/list?projectId=${projectId}"><spring:message code="filter-management" text="Filter Management" /></a></div>
                        </div>
                    </div>
                </div>

            </div>
            <!-- End of Layout Content -->
        </div>
    </body>
</html>