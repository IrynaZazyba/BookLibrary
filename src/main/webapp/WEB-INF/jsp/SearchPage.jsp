<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <meta charset="UTF-8">
    <title>Book Library</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
          crossorigin="anonymous">
    <link rel="stylesheet" href="/resources/css/style.css"/>
</head>
<body>
<jsp:include page="parts/ModalDeleteNotification.jsp"/>
<jsp:include page="parts/ModalDeleteAlert.jsp"/>
<jsp:include page="parts/ModalConfirmPagination.jsp"/>

<div class="container">

    <jsp:include page="parts/NavigationBar.jsp"/>
    <div class='alert alert-info' style="display: none" id="alertNoSelectedOptions" role='alert'>Please select a search
        option
    </div>
    <form id="searchForm" action="${pageContext.request.contextPath}/books/search" class="form-horizontal"
          role="form" method="GET">
        <div class="form-row">
            <div class="col">
                <input type="text" name="bookTitle" class="form-control" placeholder="title"
                       value="${requestScope.dto.bookTitle}"/>
            </div>
            <div class="col">
                <input type="text" name="bookAuthor" class="form-control" placeholder="author"
                       value="${requestScope.dto.bookAuthor}"/>
            </div>
            <div class="col">
                <input type="text" name="bookGenre" class="form-control" placeholder="genre"
                       value="${requestScope.dto.bookGenre}"/>
            </div>
            <div class="col">
                <input type="text" name="bookDescription" class="form-control" placeholder="description"
                       value="${requestScope.dto.bookDescription}"/>
            </div>
            <input id="isAvailableOnly" type="hidden" name="isAvailableOnly"
                   value="${requestScope.dto.isAvailableOnly}"/>
            <input id="recordsPerPage" type="hidden" name="recordsPerPage"
                   value="${requestScope.dto.recordsPerPage}"/>
            <div class="col">
                <button type="submit" onclick="checkParameter(this)" class="btn btn-info">Search</button>
            </div>
        </div>
    </form>
    <div class="row">
        <div class="col-8">
            <a href="${pageContext.request.contextPath}/books/page">
                <button type="button" class="btn  btn-info add-button">Add</button>
            </a>
            <button type="button" id="deleteBookButton" onclick="showDeleteAlert()" disabled
                    class="btn danger-button-theme btn-outline-danger">Remove
            </button>

        </div>
        <div class="col-auto">
            <div class="form-group form-check">
                <c:if test="${requestScope.dto.isAvailableOnly}">
                    <input type="checkbox" class="form-check-input" onclick="searchBookWithAvailableFilter(this)"
                           checked id="filterBook">
                </c:if>
                <c:if test="${!requestScope.dto.isAvailableOnly}">
                    <input type="checkbox" class="form-check-input" onclick="searchBookWithAvailableFilter(this)"
                           id="filterBook">
                </c:if>
                <label class="form-check-label" for="filterBook">Filter out unavailable books</label>
            </div>
        </div>
        <div class="col-1">
            <div class="form_toggle">
                <c:if test="${requestScope.dto.recordsPerPage==10||requestScope.dto.recordsPerPage==null}">
                    <div class="form_toggle-item item-1">
                        <input onclick="getCountRecordsSearchPage(this)" id="count-10" type="radio" name="radio"
                               value="10"
                               checked/>
                        <label for="count-10">10</label>
                    </div>
                    <div class="form_toggle-item item-2">
                        <input onclick="getCountRecordsSearchPage(this)" id="count-20" type="radio" name="radio"
                               value="20"/>
                        <label for="count-20">20</label>
                    </div>
                </c:if>
                <c:if test="${requestScope.dto.recordsPerPage==20}">
                    <div class="form_toggle-item item-1">
                        <input onclick="getCountRecordsSearchPage(this)" id="count-10" type="radio" name="radio"
                               value="10"/>
                        <label for="count-10">10</label>
                    </div>
                    <div class="form_toggle-item item-2">
                        <input onclick="getCountRecordsSearchPage(this)" id="count-20" type="radio" name="radio"
                               value="20"
                               checked/>
                        <label for="count-20">20</label>
                    </div>
                </c:if>

            </div>
        </div>

    </div>
    <jsp:include page="parts/MainPageTable.jsp"/>

    <nav aria-label="..." class="m-t-27">
        <ul class="pagination pagination-sm pagination_center">
            <c:forEach var="i" begin="1" end="${requestScope.dto.countPages}">
                <c:if test="${i==requestScope.dto.currentPage}">
                    <li id="${i}" class="page-item page-item-change active" aria-current="page">
                        <span class="page-link">${i}
                            <span class="sr-only">(current)</span>
                        </span>
                    </li>
                </c:if>
                <c:if test="${i!=requestScope.dto.currentPage}">
                    <li class="page-item">
                        <a class="page-link" id="${i}"
                           href="${pageContext.request.contextPath}/books/search?currentPage=${i}&isAvailableOnly=${requestScope.dto.isAvailableOnly}&recordsPerPage=${requestScope.dto.recordsPerPage}&bookTitle=${requestScope.dto.bookTitle}&bookAuthor=${requestScope.dto.bookAuthor}&bookGenre=${requestScope.dto.bookGenre}&bookDescription=${requestScope.dto.bookDescription}">
                                ${i}
                        </a>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </nav>
</div>
<jsp:include page="parts/BootstrapScript.jsp"/>
<script src="/resources/js/script.js"></script>
</body>
</html>