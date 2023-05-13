<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<link rel="stylesheet" type="text/css" href="/css/loglayout.css"/>
<script>
    <c:forEach var="history" items="${history}" varStatus="status">
        document.write(createDiv(${status.index})); // 좌하단에 표시되는 로그(div태그)들을 하나씩 생성
        getAddr(${history.h_lat}, ${history.h_long}, '<fmt:formatDate value="${history.tx_time}" pattern="yyyy-MM-dd HH:mm:ss"/>', ${status.index}); // 위치 및 일시 정보를 구하는 메서드
    </c:forEach>
</script>