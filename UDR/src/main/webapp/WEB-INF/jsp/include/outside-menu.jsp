<div id="menu">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="simple-table">
        <tr>
            <td>
                <ul class="menu">
                    <a href="../project/outside-list">
                        <c:choose>
                            <c:when test="${controller == 'project' && (action == 'outside-list' || action == 'outside-add')}">
                                <span class="myCurrent"><spring:message code="menu-project-list" text="Project List" /></span>
                            </c:when>
                            <c:otherwise>
                                <span><spring:message code="menu-project-list" text="Project List" /></span>
                            </c:otherwise>
                        </c:choose>
                    </a>
                    <li class="last">
                        <a href="../project/list">
                            <c:choose>
                                <c:when test="${(controller == 'project' || controller == 'user' || controller == 'license') && (action == 'list' || action == 'add' || action == 'edit' || action == 'detail')}">
                                    <span class="myCurrent"><spring:message code="menu-udr-admin" text="UDR Admin" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span><spring:message code="menu-udr-admin" text="UDR Admin" /></span>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                </ul>
            </td>
            <td width="10%">
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