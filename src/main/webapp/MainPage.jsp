<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <meta charset="UTF-8">
    <title>Book Library</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/css/style.css"/>
</head>
<body>

<jsp:include page="WEB-INF/jsp/parts/ModalDeleteNotification.jsp"/>
<jsp:include page="WEB-INF/jsp/parts/ModalDeleteAlert.jsp"/>

<div class="container">

    <jsp:include page="WEB-INF/jsp/parts/NavigationBar.jsp"/>

    <div class="row">
        <div class="col-8">
            <a href="${pageContext.request.contextPath}/books/page">
                <button type="button" class="btn  btn-info add-button">Add</button>
            </a>
            <button type="button" onclick="showDeleteAlert()" class="btn  btn-outline-danger">Remove</button>
        </div>
        <div class="col-auto">
            <div class="form-group form-check">
                <c:if test="${requestScope.dto.isAvailableOnly}">
                    <input type="checkbox" class="form-check-input" onclick="doFilter(this)" checked id="filterBook">
                </c:if>
                <c:if test="${not requestScope.dto.isAvailableOnly}">
                    <input type="checkbox" class="form-check-input" onclick="doFilter(this)" id="filterBook">
                </c:if>
                <label class="form-check-label" for="filterBook">Filter out unavailable books</label>
            </div>
        </div>
        <div class="col-1">
            <div class="form_toggle">
                <c:if test="${requestScope.dto.recordsPerPage==10}">
                    <div class="form_toggle-item item-1">
                        <input onclick="getCountRecords(this)" id="count-10" type="radio" name="radio" value="10"
                               checked>
                        <label for="count-10">10</label>
                    </div>
                    <div class="form_toggle-item item-2">
                        <input onclick="getCountRecords(this)" id="count-20" type="radio" name="radio" value="20">
                        <label for="count-20">20</label>
                    </div>
                </c:if>
                <c:if test="${requestScope.dto.recordsPerPage==20}">
                    <div class="form_toggle-item item-1">
                        <input onclick="getCountRecords(this)" id="count-10" type="radio" name="radio" value="10">
                        <label for="count-10">10</label>
                    </div>
                    <div class="form_toggle-item item-2">
                        <input onclick="getCountRecords(this)" id="count-20" type="radio" name="radio" value="20"
                               checked>
                        <label for="count-20">20</label>
                    </div>
                </c:if>

            </div>
        </div>
    </div>

    <jsp:include page="WEB-INF/jsp/parts/MainPageTable.jsp"/>

    <div>
        <nav aria-label="..." class="m-t-27">
            <ul class="pagination pagination-sm pagination_center">
                <c:forEach var="i" begin="1" end="${requestScope.dto.countPages}">
                    <c:if test="${i==requestScope.dto.currentPage}">
                        <li class="page-item page-item-change active" aria-current="page">
                            <span class="page-link">${i}
                                <span class="sr-only">(current)</span>
                            </span>
                        </li>
                    </c:if>
                    <c:if test="${i!=requestScope.dto.currentPage}">
                        <li class="page-item">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/books?currentPage=${i}&isAvailableOnly=${requestScope.dto.isAvailableOnly}&recordsPerPage=${requestScope.dto.recordsPerPage}">
                                    ${i}
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </nav>
    </div>
</div>
<jsp:include page="WEB-INF/jsp/parts/BootstrapScript.jsp"/>
<script src="resources/js/script.js"></script>
</body>
</html>