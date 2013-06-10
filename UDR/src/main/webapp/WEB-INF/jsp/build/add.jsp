<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="add-build" text="Add Build" /></title>

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

        <script language="javascript">
            $(function() {
                $("#loadIcon").hide();

                $( "#create-build-button" ).click(function() {
                    $("#loadIcon").show();
                    $("#create-build-form").submit();

                });

                $( "#cancel-button" ).click(function() {
                    location.href = "../build/list?projectId=${projectId}";
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
            <div id="myPane" style="height:auto; width:100%">
                <div class="ui-layout-center">
                    <div class="main-form">
                        <div class="main-form-header"><spring:message code="create-build" text="Create build"/> &nbsp; <img width="10" height="10" src="../media/images/ajax-loader.gif" id="loadIcon" style="margin-bottom:.25em; vertical-align:middle;"></div>
                        <form:form id="create-build-form" action="../build/add?projectId=${projectId}" commandName="buildUploadModel" enctype="multipart/form-data">
                            <table width="389" border="0" cellpadding="2" cellspacing="4">
                                <tr>
                                    <td width="37%" align="left" valign="middle">
                                        <spring:message code="build-name" text="Build name: " />
                                    </td>
                                    <td width="63%" align="left">
                                        <form:input path="buildName" class="myInput" style="width:100%" name="buildName" id="buildName" type="text" maxlength="30"/>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="left" valign="middle">
                                        <spring:message code="xml-file-select" text="XML file select: " />
                                    </td>
                                    <td width="63%" align="left">
                                        <form:input path="xmlFile" type="file" id="xmlFile" />
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" valign="middle">
                                        <spring:message code="source-zip-file-select" text="Source zip file select: " />
                                    </td>
                                    <td align="left">
                                        <form:input path="sourceFile" type="file" id="sourceFile" />
                                    </td>
                                </tr>

                                <tr>
                                    <td align="left" valign="middle">
                                        <spring:message code="source-type" text="Source type: " />
                                    </td>
                                    <td align="left">
                                        <c:if test="${buildListSize == 0}">
                                            <form:select id="sourceType" path="sourceType">
                                                <c:forEach var="enum" items="${sourceTypeList}">
                                                    <form:option value="${enum}"/>
                                                </c:forEach>
                                            </form:select>
                                        </c:if>
                                        <c:if test="${buildListSize > 0}">
                                            <form:input path="sourceType" type="text" readonly="true"/>
                                        </c:if>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="left" valign="middle">
                                        <spring:message code="imagix-xml-file-select" text="Imagix4D xml select: " />
                                    </td>
                                    <td align="left">
                                        <form:input path="imagixFile" type="file" id="imagixFile" />
                                    </td>
                                </tr>

                                <tr>
                                    <td colspan="2" width="63%" align="right" class="special-for-form-submit-button">
                                        <button type="button" id="create-build-button">
                                            <spring:message code="create-build" text="Create build"/>
                                        </button>
                                        <button type="button" id="cancel-button">
                                            <spring:message code="cancel" text="Cancel"/>
                                        </button>
                                    </td>
                                </tr>
                            </table>
                            <form:hidden path="projectId" />
                            <form:errors path="*" cssClass="ui-state-error-text" />
                        </form:form>
                    </div>
                </div>

                <div class="ui-layout-west">
                    <div id="accordion">
                          <h3 id="accordionMenu"><strong><spring:message code="project-admin" text="Project Admin"/></strong></h3>
                          <div>
                              <div><a href="../build/list?projectId=${projectId}"><spring:message code="build-management" text="Build Management"/></a></div>
                              <div><a href="../build/view-project"><spring:message code="view-project-info" text="View Project Info"/></a></div>
                          </div>
                    </div>
                </div>
            </div>
            <!-- End of Layout Content -->
        </div>
    </body>
</html>