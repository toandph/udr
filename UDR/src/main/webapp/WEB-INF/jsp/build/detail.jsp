<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="view-build-info" text="View Build Information"/></title>

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
        <script type="text/javascript">
            $(function() {
                $("#delete-build-button").click(function() {
                    $( "#dialog-confirm" ).dialog({
                        resizable: false,
                        height:150,
                        modal: true,
                        buttons: [ {text:$("#message-1").html(),
                            click:function() {
                                paramString = "projectId=" + $("#projectId").html() + "&buildId=" + $("#buildId").html();
                                location.href = '../build/delete?' + paramString;
                            }
                        },
                            {text:$("#message-2").html(),
                                click:function() {
                                    $( this ).dialog( "close" );
                                }
                            }
                        ]
                    });
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
                    <div class="main-form-header"><spring:message code="view-build-info" text="View Build Information"/></div>
                    <table width="500" border="0" cellpadding="0" cellspacing="4">
                        <tr>
                            <td width="22%" align="left">
                                <spring:message code="build-name" text="Build name"/>:
                            </td>
                            <td width="78%" align="left">
                                <input readonly="true" class="myInput" style="width:100%; background-color:#EEE;" type="text" maxlength="30" value="${detail.buildName}"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" valign="top">
                                <spring:message code="description" text="Description"/>:
                            </td>
                            <td width="78%" align="left">
                                <textarea readonly="true" id="type" rows="6" style="width:100%; background-color:#EEE;" >${detail.description}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <td width="22%" align="left">
                                <spring:message code="created-by" text="Created by"/>:
                            </td>
                            <td width="78%" align="left">
                                <input readonly="true" class="myInput" style="width:100%; background-color:#EEE;" type="text" maxlength="30" value="${detail.createdBy}"/>
                            </td>
                        </tr>
                        <tr>
                            <td width="22%" align="left">
                                <spring:message code="created-date" text="Created date"/>
                            </td>
                            <td width="78%" align="left">
                                <input readonly="true" class="myInput" style="width:100%; background-color:#EEE;" type="text" maxlength="30" value="${detail.createdDate}"/>
                            </td>
                        </tr>
                        <tr>
                            <td width="22%" align="left">
                                <spring:message code="source-type" text="Source type"/>
                            </td>
                            <td width="78%" align="left">
                                <input readonly="true" class="myInput" style="width:100%; background-color:#EEE;" type="text" maxlength="30" value="${detail.sourceType}"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" valign="top"></td>
                            <td width="78%" align="right" class="special-for-form-submit-button">
                                <button onclick="location.href='../build/edit?projectId=${projectId}&buildId=${detail.id}'">
                                    <spring:message code="edit" text="Edit"/>
                                </button>
                                <button id="delete-build-button">
                                    <spring:message code="delete" text="Delete"/>
                                </button>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <div class="ui-layout-west">
                <div id="accordion">
                    <h3 id="accordionMenu"><strong><spring:message code="project-admin" text="Project Admin"/></strong></h3>
                    <div>
                        <div><a href="../build/list?projectId=${projectId}"><spring:message code="build-management" text="Build Management"/></a></div>
                        <div><a href="../build/view-project?projectId=${projectId}"><spring:message code="view-project-info" text="View Project Info"/></a></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- End of Layout Content -->
    </div>

        <!-- Dialog Content -->
        <div id="dialog-confirm" title="Delete build?" style="display:none">
            <p>
                <span class="ui-icon ui-icon-alert"></span>
                <spring:message code="this-build-will-be-permanently-deleted" text = "This build will be permanently deleted. Are you sure?"/>
            </p>
        </div>

        <span style="display:none" id="message-1"><spring:message code="delete-build" text = "Delete Build"/></span>
        <span style="display:none" id="message-2"><spring:message code="cancel" text = "Cancel"/></span>
        <span style="display:none" id="projectId">${projectId}</span>
        <span style="display:none" id="buildId">${buildId}</span>
    </body>
</html>