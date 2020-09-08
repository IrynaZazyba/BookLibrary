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
<div class="container">

    <jsp:include page="WEB-INF/jsp/parts/NavigationBar.jsp"/>

    <div class="row">
        <div class="col-8">
            <button type="button" class="btn  btn-info add-button">Add</button>
            <button type="button" class="btn  btn-outline-danger">Remove</button>
        </div>
        <div class="col-auto">
            <div class="form-group form-check">
                <c:if test="${requestScope.mainPageDto.isAvailableOnly}">
                    <input type="checkbox" class="form-check-input" onclick="doFilter(this)" checked id="filterBook">
                </c:if>
                <c:if test="${not requestScope.mainPageDto.isAvailableOnly}">
                    <input type="checkbox" class="form-check-input" onclick="doFilter(this)" id="filterBook">
                </c:if>
                <label class="form-check-label" for="filterBook">Filter out unavailable books</label>
            </div>
        </div>
        <div class="col-1">
            <div class="form_toggle">
                <c:if test="${requestScope.mainPageDto.recordsPerPage==10}">
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
                <c:if test="${requestScope.mainPageDto.recordsPerPage==20}">
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
    <table class="table table-bordered mainPageTable">
        <thead>
        <tr>
            <th></th>
            <th scope="col">title</th>
            <th scope="col">author(-s)</th>
            <th scope="col">publish date</th>
            <th scope="col">amount of book</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="num" value="0" scope="page"/>
        <c:forEach var="book" items="${requestScope.mainPageDto.books}">
            <tr>
                <th>
                    <div class="form-group form-check">
                        <input type="checkbox" class="form-check-input" id="delete-${book.id}">
                        <label class="form-check-label" for="delete-${book.id}"></label>
                    </div>
                </th>
                <td><a href="#">${book.title}</a></td>
                <td>
                    <c:forEach var="author" items="${book.author}" varStatus="loop">
                        <c:out value="${author.name}"/>
                        <c:if test="${not loop.last}">,</c:if>
                    </c:forEach>
                </td>
                <td> ${book.publishDate} </td>
                <td>${book.inStock}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div>
        <nav aria-label="..." class="m-t-27">
            <ul class="pagination pagination-sm pagination_center">
                <c:forEach var="i" begin="1" end="${requestScope.mainPageDto.countPages}">
                    <c:if test="${i==requestScope.mainPageDto.currentPage}">

                        <li class="page-item page-item-change active" aria-current="page">
                                                <span class="page-link">${i}
                                                            <span class="sr-only">(current)</span>
                                                            </span>
                        </li>
                    </c:if>


                    <c:if test="${i!=requestScope.mainPageDto.currentPage}">
                        <li class="page-item">
                            <a class="page-link"
                            href="${pageContext.request.contextPath}/books?currentPage=${i}&isAvailableOnly=${requestScope.mainPageDto.isAvailableOnly}&recordsPerPage=${requestScope.mainPageDto.recordsPerPage}">
                                ${i}
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </nav>
    </div>

</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"
        integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI"
        crossorigin="anonymous"></script>
<script src="resources/js/script.js"></script>

</body>
</html>