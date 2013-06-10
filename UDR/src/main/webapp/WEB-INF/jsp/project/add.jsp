<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="create-project" text="Create Project" /></title>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

        <![if IE]> <link href="../media/css/main-ie.css" rel="stylesheet" type="text/css" /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>

        <script type="text/javascript" language="javascript" src="../media/js/jquery.layout-latest.min.js"></script>

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
                    <div class="main-form">
                        <div class="main-form-header"><spring:message code="create-project" text="Create Project"/></div>
                        <form:form commandName="projectModel" action="../project/add">
                            <table width="500" border="0" cellpadding="0" cellspacing="4">
                                <tr>
                                    <td width="22%" align="left">
                                        <spring:message code="project-name" text="Project name: " />
                                    </td>
                                    <td width="78%" align="left">
                                        <form:input path="name" class="myInput" style="width:100%" type="text" maxlength="64"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" valign="top">
                                        <spring:message code="description" text="Description: " />
                                    </td>
                                    <td width="78%" align="left">
                                        <form:textarea path="description" id="type" rows="6" style="width:100%" />
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" valign="top"></td>
                                    <td width="78%" align="right" class="special-for-form-submit-button">
                                        <button><spring:message code="create-project" text="Create Project"/></button>
                                        <button type="button" onclick="location.href='../project/list'"><spring:message code="cancel" text="Cancel"/></button>
                                    </td>
                                </tr>
                            </table>
                            <form:errors path="*" cssClass="ui-state-error-text"></form:errors>
                        </form:form>
                    </div>
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
    </body>
</html>