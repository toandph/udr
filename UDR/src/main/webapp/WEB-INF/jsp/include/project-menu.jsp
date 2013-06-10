<div id="menu">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="simple-table">
        <tr>
            <td>
                <ul class="menu">
                    <a href="../project/outside-list" >
                        <span><spring:message code="menu-project-list" /></span>
                    </a>
                    <li class="last">
                        <a href="../report/chart?projectId=${projectId}">
                            <c:choose>
                                <c:when test="${controller == 'report'}">
                                    <span class="myCurrent"><spring:message code="report" text="Report"/></span>
                                </c:when>
                                <c:otherwise>
                                    <span><spring:message code="report" text="Report"/></span>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                    <li class="last">
                        <a href="../issue/list?projectId=${projectId}">
                            <c:choose>
                                <c:when test="${controller == 'issue'}">
                                    <span class="myCurrent"><spring:message code="issue-management" text="Issue Management"/></span>
                                </c:when>
                                <c:otherwise>
                                    <span><spring:message code="issue-management" text="Issue Management"/></span>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>

                    <li class="last">
                        <a href="../metric/chart?projectId=${projectId}">
                            <c:choose>
                                <c:when test="${controller == 'metric'}">
                                    <span class="myCurrent"><spring:message code="metric-management" text="Metrics"/></span>
                                </c:when>
                                <c:otherwise>
                                    <span><spring:message code="metric-management" text="Metrics"/></span>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>

                    <li class="last">
                        <a href="../filter/list?projectId=${projectId}">
                            <c:choose>
                                <c:when test="${controller == 'filter'}">
                                    <span class="myCurrent"><spring:message code="filter" text="Filter"/></span>
                                </c:when>
                                <c:otherwise>
                                    <span><spring:message code="filter" text="Filter"/></span>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                    <li class="last">
                        <a href="../build/list?projectId=${projectId}">
                            <c:choose>
                                <c:when test="${controller == 'build'}">
                                    <span class="myCurrent"><spring:message code="project-admin" text="Project Admin"/></span>
                                </c:when>
                                <c:otherwise>
                                    <span><spring:message code="project-admin" text="Project Admin"/></span>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                </ul>
            </td>
            <td width="20%" align="right">

                <ul class="menu" style="float:right">
                    <li class="last" style="text-align:right">
                        <a href="javascript:window.open('../issue/help','Help','width=1280,height=800'); void(0)">
                            <c:choose>
                                <c:when test="${(controller == 'issue') && (action == 'help')}">
                                    <span class="myCurrent" ><spring:message code="help"/></span>
                                </c:when>
                                <c:otherwise>
                                    <span><spring:message code="help"/></span>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                    <li class="last" style="text-align:right">
                        <c:if test="${!pageContext.request.userPrincipal.authenticated}">
                            <a href="../user/login?code=0">
                                <c:choose>
                                    <c:when test="${(controller == 'user') && (action == 'login')}">
                                        <span class="myCurrent"><spring:message code="login" text="Login"/></span>
                                    </c:when>
                                    <c:otherwise>
                                        <span><spring:message code="login" text="Login"/></span>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                        </c:if>
                        <c:if test="${pageContext.request.userPrincipal.authenticated}">
                            <a href='<c:url value="/j_spring_security_logout" />'>
                                <span><spring:message code="menu-logout" text="Logout"/></span>
                            </a>
                        </c:if>
                    </li>
                </ul>

            </td>
        </tr>
    </table>
</div>