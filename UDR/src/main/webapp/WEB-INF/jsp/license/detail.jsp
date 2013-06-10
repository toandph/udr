<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>UDR - <spring:message code="license-detail" text="License Detail"/></title>

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
            <div>
                <div style="margin-bottom:3px; font-weight:bold">Information: </div>
                <table>
                    <tr>
                        <td>HostID: </td> <td><b>${currenthost}</b></td>
                    </tr>
                    <tr>
                        <td>Current Version: </td> <td><b><spring:message code="version" /></b></td>
                    </tr>
                </table>
            </div>
            <div>-----------------------------------</div>
            <div style="margin-bottom:3px; font-weight:bold">Current license: </div>
            <table>
                <tr>
                    <td>Key: </td>
                    <td>
                        <c:if test="${key == 'KEY_VALID'}">
                            <b><span style="color:green">${key}</span></b>
                        </c:if>

                        <c:if test="${key == 'KEY_INVALID'  || key == 'KEY_EXPIRED'}">
                            <b><span style="color:red">${key}</span></b> (Please check the license key correctly)
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Name: </td> <td><b>${name}</b></td>
                </tr>
                <tr>
                    <td>Host: </td> <td><b>${host}</b></td>
                </tr>
                <tr>
                    <td>Version: </td> <td><b>${version}</b></td>
                </tr>
                <tr>
                    <td>Expired on: </td>
                    <td>

                        <c:if test="${key == 'KEY_VALID' }">
                            <b><span style="color:green">${date}</span></b>
                        </c:if>

                        <c:if test="${key == 'KEY_INVALID' || key == 'KEY_EXPIRED'}">
                            <b><span style="color:red;">${date}</span></b>
                        </c:if>
                    </td>
                </tr>
            </table>
            <div>------------------------------------</div>
            <!--
            <div  style="margin-bottom:3px; font-weight:bold">Add / Renew License</div>
            -->
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