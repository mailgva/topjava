<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
    var i18n = [];
    $(function() {
        var dataPage = $('div[data-page]:first');

        if (dataPage !== undefined) {
            if (dataPage.attr("data-page") === "meals") {
                i18n["addTitle"] = '<spring:message code="meal.add"/>';
                i18n["editTitle"] = '<spring:message code="meal.edit"/>';
            } else {
                i18n["addTitle"] = '<spring:message code="user.add"/>';
                i18n["editTitle"] = '<spring:message code="user.edit"/>';
            }
        }
    });

    <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus"}%>'>
        i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
</script>