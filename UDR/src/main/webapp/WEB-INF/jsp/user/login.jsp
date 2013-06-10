<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="login" text="Login" /></title>

        <![if IE]><link href="../media/css/main-ie.css" rel="stylesheet" type="text/css" /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>

        <script language="javascript" src="../media/js/init-button.js"></script>

        <script language="javascript">
            $(function() {
                $( "#login-button" ).click(function() {
                    $("#create-login-form").submit();
                });
            });
        </script>
    </head>

    <body>
        <div class="wrapper">
            <%@include file="../include/header.jsp" %>
            <%@include file="../include/outside-menu.jsp" %>

            <!-- Login Div -->
            <div style="margin:auto; margin-top:10px; border:1px solid #CCC; width:250px; padding:5px">
                <div style="font-weight:bold; padding:1px;"><spring:message code="login" /></div>
                <!-- Start Form -->
                <form name='f' action='<c:out value="../j_spring_security_check" />'  method="POST">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="33%" align="left">
                                <spring:message code="username" text="Username: " />
                            </td>
                            <td width="67%" align="left">
                                <input name="j_username" class="myInput" style="width:100%" type="text" maxlength="30"/>
                            </td>
                        </tr>

                        <tr>
                            <td align="left" valign="top">
                                <spring:message code="password" text="Password: " />
                            </td>
                            <td width="67%" align="left">
                                <input name="j_password" class="myInput" style="width:100%" type="password" maxlength="30"/>
                            </td>
                        </tr>

                        <tr>
                            <td align="left" valign="top"></td>
                            <td width="67%" align="right" class="special-for-form-submit-button">
                                <button type="submit" id="login-button">
                                    <spring:message code="login" text="Login" />
                                </button>
                            </td>
                        </tr>
                    </table>
                    <c:if test='${message != ""}'>
                        <span class="ui-state-error-text"><spring:message code="${message}" /></span>
                    </c:if>
                </form>

                <!-- End of Form -->
            </div>
            <!-- End of Login Div -->
        </div>
    </body>
</html>