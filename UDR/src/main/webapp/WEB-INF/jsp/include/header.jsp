<script>
    function insertParam(key, value)
    {
        key = escape(key); value = escape(value);

        var kvp = document.location.search.substr(1).split('&');

        var i=kvp.length; var x;
        while(i--) {
            x = kvp[i].split('=');
            if (x[0]==key)
            {
                x[1] = value;
                kvp[i] = x.join('=');
                break;
            }
        }
        if(i<0) {
            kvp[kvp.length] = [key,value].join('=');
        }
        document.location.search = kvp.join('&');
    }
</script>

<div id="header">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <a href="../"><img src="../media/images/logo_header.png"  alt="Architect Group UDR" style="border:none"/></a> v.<spring:message code="version" />
            </td>
            <td width="240" align="right">
                <table>
                    <tr>
                        <td valign="middle">
                            <div>
                                <select onchange="insertParam('lang',this.value)">
                                    <option value="en" <c:if test="${pageContext.response.locale == 'en'}">selected </c:if> >English</option>
                                    <option value="ko" <c:if test="${pageContext.response.locale == 'ko'}">selected </c:if>>한국어</option>
                                </select>
                            </div>
                        </td>
                        <c:if test="${pageContext.request.userPrincipal.authenticated}">
                            <td valign="top" width="150">
                                    <div class="simpleDiv">
                                        <strong><spring:message code="user" text="User" /></strong>: ${pageContext.request.userPrincipal.name}<br />

                                        <strong><spring:message code="project" text="Project" /></strong>:
                                        <c:if test="${sessionModel.project == null}"><spring:message code="not-select" text="Not select" /></c:if>
                                        <c:if test="${sessionModel.project != null}">${sessionModel.project.name}</c:if>
                                        <br/>
                                        <strong><spring:message code="build" text="Build" /></strong>:
                                        <c:if test="${sessionModel.build == null}"><spring:message code="not-select" text="Not select" /></c:if>
                                        <c:if test="${sessionModel.build != null}">${sessionModel.build.name}</c:if>
                                    </div>
                            </td>
                        </c:if>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>