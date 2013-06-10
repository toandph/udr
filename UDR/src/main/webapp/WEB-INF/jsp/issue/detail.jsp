<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="issue-detail" /></title>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />

        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />

        <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />
        <link rel="stylesheet" href="../media/js/codemirror/addon/dialog/dialog.css">

        <![if IE]><link href="../media/css/main-ie.css" rel="stylesheet" type="text/css" /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <link type="text/css" href="../media/js/codemirror/lib/codemirror.css" rel="stylesheet">
        <link type="text/css" href="../media/js/dynatree/ui.dynatree.css" rel="stylesheet" id="skinSheet"/>
        <link type="text/css" href="../media/css/my-dynatree.css" rel="stylesheet"/>

        <script src="../media/js/jquery-1.8.3.js" type="text/javascript"></script>
        <script src="../media/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script>
        <script src="../media/js/jquery.layout-latest.min.js" type="text/javascript" language="javascript"></script>

        <script src="../media/js/dynatree/jquery.cookie.js" type="text/javascript"></script>
        <script src="../media/js/dynatree/jquery.dynatree.js" type="text/javascript"></script>
        <script src="../media/js/init-button.js" type="text/javascript"></script>
        <script src="../media/js/codemirror/lib/codemirror.js" type="text/javascript" ></script>
        <script src="../media/js/codemirror/mode/clike/clike.js" type="text/javascript"></script>
        <script src="../media/js/codemirror/addon/selection/active-line.js" type="text/javascript"></script>
        <script src="../media/js/init-issue-detail.js" type="text/javascript"></script>

        <script src="../media/js/codemirror/addon/dialog/dialog.js"></script>
        <script src="../media/js/codemirror/addon/search/searchcursor.js"></script>
        <script src="../media/js/codemirror/addon/search/search.js"></script>

        <script>
            $(function() {
                var issueid = $("#issueId").val();
                var projectid = $("#projectId").val();
                var buildid = $("#buildId").val();
                var from = 0;

                var showDate = function () {
                    var now = new Date();
                    var then = now.getFullYear()+'/'+(now.getMonth()+1)+'/'+now.getDay();
                    then += ' ' + now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds();
                    return then;
                }

                var getHistoryBlockHtml = function(id, user, status, date, comment) {
                    var html = "";
                    html += "<div style=\"border:1px #E9E8ED solid; margin-bottom:5px;\">\n" +
                            "<table style=\"border:0px\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                            "<tbody><tr>\n" +
                            "<td align=\"left\" valign=\"top\" class=\"left\"><strong>$dpt_user$</strong><strong></strong></td>\n" +
                            "<td class=\"left\" align=\"right\" valign=\"top\"><strong>$dpt_status$</strong></td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td colspan=\"2\" align=\"left\" valign=\"top\">$dpt_comment$</td>\n" +
                            "</tr>\n" +
                            "<tr>\n";
                    if ($("#permitToRemove").html() == "true") {
                            html += "<td style=\"font-style:normal; font-size:9px\" align=\"left\" valign=\"top\"><a class='delHistoryBtn' historyId='$dpt_id$' href=\"#\">Remove</a></td>\n";
                    } else {
                        html += "<td style=\"font-style:normal; font-size:9px\" align=\"left\" valign=\"top\"></td>\n";
                    }

                    html += "<td style=\"font-style:italic; font-size:9px\" align=\"right\" valign=\"top\">$dpt_date$</td>\n" +
                            "</tr>\n" +
                            "</tbody>" +
                            "</table>\n" +
                            "</div>";
                    html = html.replace("$dpt_user$", user);
                    html = html.replace("$dpt_status$", status);
                    html = html.replace("$dpt_date$", date);
                    html = html.replace("$dpt_comment$", comment);
                    html = html.replace("$dpt_id$", id);
                    return html;
                }

                var loadHistoryDataFunc = function(from, append, animation) {
                    var getListJsonUrl = "../history/get-list-json?issueId=" + issueid + "&projectId=" + projectid + "&buildId=" + buildid + "&from=" + from;
                    $.post(getListJsonUrl, function(data) {
                        var html = "";
                        var obj = data;
                        var count = 0;
                        $(obj.history).each(function(i, history) {
                            html += getHistoryBlockHtml(history.id, history.user, history.status, history.date, history.comment);
                            count++;
                        });

                        if (append) {
                            $("#historyDetail").html($("#historyDetail").html() + html);
                        } else {
                            $("#historyDetail").html(html);
                        }

                        if (count < 5) {
                            $("#moreBtn").hide();
                        } else if (count == 5) {
                            $("#moreBtn").show();
                        }

                        if (animation) {
                            $("#historyDetail div:first-child").hide();
                            $("#historyDetail div:first-child").fadeIn(700);
                        }

                        $(".delHistoryBtn").click(function(e) {
                            e.preventDefault();
                            var delBtn = $(this);
                            $.post("../history/delete-json", {"projectId":projectid, "buildId":buildid, "historyId":$(this).attr("historyId")}, function(data){
                                delBtn.closest("div").fadeOut(500, function() {
                                    $(this).remove();
                                    var size = $("#historyDetail div").length;
                                    if (size < 5) {
                                        loadHistoryDataFunc(0, false);
                                    }
                                });
                                var noOfHistory = parseInt($("#noOfHistory").html());
                                $("#noOfHistory").html(noOfHistory - 1);
                            });
                        });
                    });
                }

                loadHistoryDataFunc(from, false);
                $("#status").val($("#currentStatus").html());

                $("#saveBtn").click(function() {
                    comment = $("#comment").val();
                    if (comment.length >= 2) {
                        var status = $("#status").val();
                        var url = "../history/add-json?issueId=" + issueid + "&projectId=" + projectid + "&buildId=" + buildid + "&status=" + status;
                        $.post(url, {"comment":comment}, function(data){
                            var noOfHistory = parseInt($("#noOfHistory").html());
                            $("#noOfHistory").html(noOfHistory + 1);

                            $("#comment").val("");
                            loadHistoryDataFunc(0, false, true);
                            $("#comment").focus();
                        });
                    } else {
                        $("#comment-limit-dialog").dialog({
                            resizable: false,
                            minHeight:0,
                            modal: true,
                            buttons: [ {text:$("#message-2").html(),
                                    click:function() {
                                        $( this ).dialog( "close" );
                                        $("#comment").focus();
                                    }
                                }
                            ]
                        });
                    }
                });

                $("#moreBtn").click(function(event) {
                    event.preventDefault();
                    from = $("#historyDetail div").length;
                    loadHistoryDataFunc(from, true);
                });

                $("#back-to-list").click(function() {
                    location.href = "../issue/list?projectId=${projectId}&buildId=${build.id}";
                });
                $("#previous").click(function() {
                    location.href = "../issue/detail?projectId=${projectId}&buildId=${build.id}&issueId=${previousId}";
                });
                $("#next").click(function() {
                    location.href = "../issue/detail?projectId=${projectId}&buildId=${build.id}&issueId=${nextId}";
                });

                $("#search-button").click(function() {
                    CodeMirror.commands.find(editor);
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
                <div class="ui-layout-center" style="overflow:hidden">
                    <div style="padding-bottom:5px">
                        <table class="simple-table" width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="left">
                                  <strong><span id="issue-file">${issue.file}</span> (${build.name})</strong>
                                  <c:if test="${error != null}" >
                                      <span id="error-file-span" class="ui-state-error-text">${error}</span>
                                  </c:if>
                                </td>
                                <td width="40%" align="right">
                                    <button id="search-button"><spring:message code="search" /> (Ctrl-F)</button>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <!-- Start of container -->
                    <div id="container" style="min-width: 400px;">
                        <textarea id="code" name="code" rows="10">
                            ${fileContent}
                        </textarea>
                        <span style="display:none" id="error-main-line">${issue.line}</span>
                        <c:forEach var="trace" items="${issue.traceList}">
                            <span class="traceId" style="display:none" file="${trace.file}">${trace.line}</span>
                        </c:forEach>

                    </div>
                    <!-- End of container -->
                </div>

                <div class="ui-layout-west">
                    <div>
                        <table class="simple-table" width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="left">
                                  <button id="back-to-list" style="width:55px">Back to list</button>
                                </td>
                                <td width="40%" align="right" valign="middle">
                                    <c:if test="${previousId == 0}">
                                        <button id="previous" disabled="disabled">Previous</button>
                                    </c:if>
                                    <c:if test="${previousId > 0}">
                                        <button id="previous">Previous</button>
                                    </c:if>

                                    <c:if test="${nextId == 0}">
                                        <button id="next" disabled="disabled">Previous</button>
                                    </c:if>
                                    <c:if test="${nextId > 0}">
                                        <button id="next">Next</button>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div class="message-detail">
                        ${issue.message}
                    </div>

                    <div class="errorDetail">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="left" valign="top" class="left"><spring:message code="ID" /></td>
                                <td width="70%" align="left" valign="top">${issue.id}</td>
                          </tr>
                            <tr>
                                <td align="left" valign="top" class="left"><spring:message code="code" /></td>
                              <td width="70%" align="left" valign="top">${issue.code}</td>
                          </tr>
                            <tr>
                                <td align="left" valign="top" class="left"><spring:message code="name" /></td>
                              <td width="70%" align="left" valign="top">${issue.code}</td>
                          </tr>
                            <tr>
                                <td align="left" valign="top" class="left"><spring:message code="location" /></td>
                              <td width="70%" align="left" valign="top">${issue.file}: ${issue.line}</td>
                          </tr>
                            <tr>
                                <td align="left" valign="top" class="left"><spring:message code="method" /></td>
                                <td width="70%" align="left" valign="top">${issue.method}</td>
                            </tr>
                            <tr>
                                <td align="left" valign="top" class="left"><spring:message code="build" /></td>
                              <td width="70%" align="left" valign="top">${build.name}</td>
                          </tr>
                            <tr>
                                <td align="left" valign="top" class="left"><spring:message code="severity" /></td>
                              <td width="70%" align="left" valign="top">${issue.severity}</td>
                          </tr>
                            <tr>
                                <td align="left" valign="top" class="left"><spring:message code="state" /></td>
                              <td width="70%" align="left" valign="top">${issue.state}</td>
                          </tr>
                            <tr>
                                <td align="left" valign="top" class="left"><spring:message code="status" /></td>
                              <td width="70%" align="left" valign="top">
                                  <select id="status">
                                      <option value="Analyze">  <spring:message code="analyze" text="Analyze" /> </option>
                                      <option value="Ignore">  <spring:message code="ignore" text="Ignore" /> </option>
                                      <option value="Not a problem">  <spring:message code="not-a-problem" text="Not a problem" /> </option>
                                      <option value="Fix in next release">  <spring:message code="fix-in-next-release" text="Fix in next release" /> </option>
                                      <option value="Fix in later release">  <spring:message code="fix-in-later-release" text="Fix in later release" /> </option>
                                      <option value="Defer">  <spring:message code="defer" text="Defer" /> </option>
                                      <option value="Filter">  <spring:message code="filter" text="Filter" /> </option>
                                  </select>
                              </td>
                          </tr>
                        </table>
                    </div>
                    <c:if test="${pageContext.request.userPrincipal.authenticated}">
                        <div class="commentSection" style="margin-top:10px">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td align="left" colspan="2" valign="top" class="left"><spring:message code="comment" /></td>
                                 </tr>
                                <tr>
                                    <td align="left" colspan="2" valign="top" class="left"><textarea id="comment" style="width:100%" rows="4" cols="4"></textarea></td>
                                 </tr>
                                <tr>
                                    <td align="left" valign="top" class="left"></td>
                                    <td width="70%" align="right" valign="top"><input id="saveBtn" type="button" value="<spring:message code="save-changes" />"/></td>

                                </tr>
                            </table>
                        </div>
                    </c:if>

                    <div id="tabs" style="margin-top:10px">
                          <ul>
                              <li><a href="#tabs-1"><spring:message code="TRACEBACK" /></a></li>
                              <li><a href="#tabs-2"><spring:message code="HISTORY" />(<span id="noOfHistory">${noOfHistory}</span>)</a></li>
                          </ul>
                          <div id="tabs-1">
                              <script type="text/javascript">
                                    $(function () {
                                        $("#tree").dynatree({
                                            imagePath:null,
                                            classNames:{
                                                nodeIcon:"nodisplay"
                                            },
                                            onActivate:function (node) {
                                                url = "../issue/file-json?file=" + node.data.file + "&projectId=" + node.data.project + "&buildId=" + node.data.build;
                                                $.post(url, function(data) {
                                                    if ($("#issue-file").html() != node.data.file) {    // Only reload if different file
                                                        if (data == "-1") {
                                                            $("#error-file-span").html("can-not-find-source-file");
                                                            editor.setValue(" ");
                                                            $("#issue-file").html(node.data.file);
                                                        } else {
                                                            $("#error-file-span").html("");
                                                            editor.setValue(data);
                                                            $("#issue-file").html(node.data.file);
                                                        }
                                                    }

                                                    var editors = $('.CodeMirror-scroll');

                                                    $(".traceId").each(function() {
                                                        var minorLine = parseInt($(this).html()) - 1;
                                                        if ($("#issue-file").html() == $(this).attr("file")) {
                                                            editor.removeLineClass(minorLine, "background");
                                                            editor.addLineClass(minorLine, "background", "CodeMirror-minorLine");
                                                        }
                                                    });

                                                    var mainLine = parseInt(node.data.line) - 1;
                                                    editors.scrollTop(0).scrollTop(mainLine * 11 - Math.round(editors.height()/2));
                                                    editor.removeLineClass(mainLine, "background", "CodeMirror-minorLine");
                                                    editor.addLineClass(mainLine, "background", "CodeMirror-majorLine");

                                                });
                                            },
                                            onExpand:function(node) {
                                                $(".dynatree-title").click(function() {
                                                    $(".dynatree-title").attr("style","color:black; display:inline-block");
                                                    $(this).attr("style","color:red; display:inline-block");
                                                });
                                            }
                                        });

                                        $(".dynatree-title").click(function() {
                                            $(".dynatree-title").attr("style","color:black; display:inline-block");
                                            $(this).attr("style","color:red; display:inline-block");
                                        });
                                    });
                              </script>

                              ${traceTree}
                                <!-- end tree data -->
                          </div>
                           <!-- tab1 end here -->

                           <div id="tabs-2">
                               <input type="hidden" id="projectId" value="${projectId}" />
                               <input type="hidden" id="buildId" value="${build.id}" />
                               <input type="hidden" id="issueId" value="${issue.id}" />
                               <input type="hidden" id="userComment" value="${pageContext.request.userPrincipal.name}" />
                               <div class="historyDetail" style="font-size:11px" id="historyDetail"></div>
                               <div align='right'><a href="#" id="moreBtn">More</a></div>
                           </div>
                           <!-- tab2 end here -->
                </div>
                <span id="currentStatus" style="display:none">${issue.citingstatus}</span>
                <span id="permitToRemove" style="display:none">${allow}</span>
            </div>
            <!-- End of Layout Content -->

            <!-- Dialog Content -->
            <div id="comment-limit-dialog" title="<spring:message code="comment-limit-dialog-title"/>" style="display:none">
                <p>
                    <span class="ui-icon ui-icon-alert"></span>
                    <spring:message code="comment-must-be-10-character"/>
                </p>
            </div>
            <span style="display:none" id="message-2">OK</span>
        </div>
    </body>
</html>