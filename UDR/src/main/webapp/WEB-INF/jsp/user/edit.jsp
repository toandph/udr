<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="edit-user" text="Edit User"/></title>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />

        <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

        <![if IE]><link href="../media/css/main-ie.css" rel="stylesheet" type="text/css" /><![endif]>
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
                <!-- Right Side -->
                <div class="ui-layout-center">
                    <div class="main-form">
                        <div class="main-form-header"><spring:message code="edit-user" text="Edit User"/></div>
                        <form:form commandName="userModel" action="../user/edit?userId=${userModel.username}">
                            <table width="357" border="0" cellpadding="0" cellspacing="4">
                                <tr>
                                    <td width="32%" align="left" valign="middle">
                                        <spring:message code="username" text="Username"/>:
                                    </td>
                                    <td width="68%" align="left">
                                        <form:input path="username" class="myInput" style="width:100%" type="text" maxlength="30"/>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="left" valign="middle">
                                        <spring:message code="password" text="Password"/>:
                                    </td>
                                    <td align="left">
                                        <form:input path="password" class="myInput" style="width:100%" type="password" maxlength="30"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" valign="middle">
                                        <spring:message code="confirm-password" text="Confirm Password"/>:
                                    </td>
                                    <td align="left">
                                        <form:input path="confirmPassword" class="myInput" style="width:100%" type="password" maxlength="30"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" valign="middle">
                                        <spring:message code="email" text="Email"/>:
                                    </td>
                                    <td width="68%" align="left">
                                        <form:input path="email" class="myInput" style="width:100%" type="text" maxlength="30"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" valign="middle"><spring:message code="role" text="Role"/>:</td>
                                    <td align="left">
                                        <form:select path = "role">
                                            <form:option value="1024">Admin</form:option>
                                            <form:option value="4">User</form:option>
                                        </form:select>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="left" valign="top"></td>
                                    <td width="68%" align="right" class="special-for-form-submit-button">
                                        <button onclick="location.href='../user/detail'"><spring:message code="save" text="Save"/></button>
                                        <button onclick="location.href='../user/detail'"><spring:message code="cancel" text="Cancel"/></button>
                                    </td>
                                </tr>
                            </table>
                            <form:errors path="*" cssClass="ui-state-error-text"/>
                            <form:hidden path="id" />
                        </form:form>
                    </div>
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
    </body>
</html>