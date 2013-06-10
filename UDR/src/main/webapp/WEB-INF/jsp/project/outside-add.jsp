<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="create-project" text="Create Project" /></title>

        <![if IE]> <link type="text/css" href="../media/css/main-ie.css" rel="stylesheet"  /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>
        <script type="text/javascript" src="../media/js/init-button.js"></script>
    </head>

    <body>
        <div class="wrapper">
            <!-- Header and Menu -->
                <%@include file="../include/header.jsp" %>
                <%@include file="../include/outside-menu.jsp" %>
            <!-- End of Header Menu -->

            <div style="margin:auto; margin-top:10px; width:500px; border:1px solid #CCC; padding:5px">
                <div class="main-form-header"><spring:message code="create-project" text="Create Project"/></div>
                <form:form commandName="projectModel" action="../project/outside-add">
                    <table width="500" border="0" cellpadding="0" cellspacing="4">
                        <tr>
                        <td width="100" align="left">
                            <spring:message code="project-name" text="Project name: " />
                        </td>
                        <td width="400" align="left">
                            <form:input path="name" class="myInput" style="width:100%" type="text" maxlength="64"/>
                        </td>
                        </tr>

                        <tr>
                        <td width="100" align="left" valign="top">
                            <spring:message code="description" text="Description: " />
                        </td>
                        <td width="400" align="left">
                            <form:textarea path="description" id="type" rows="6" style="width:100%" />
                        </td>
                        </tr>

                        <tr>
                            <td width="100" align="left" valign="top">
                            </td>
                            <td width="400" align="right" style="margin-right:-2px; padding-right:0px; float:right;">
                                <button><spring:message code="create-project" text="Create Project"/></button>
                                <button type="button" onclick="location.href='/project/outside-list'">Cancel</button>
                            </td>
                        </tr>
                    </table>
                    <form:errors path="*" cssClass="ui-state-error-text"></form:errors>
                </form:form>
                <!-- End of Create Project Form -->
            </div>
            <!-- End of Form Div -->
         </div>
    </body>
</html>