<html lang="en">
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ page isELIgnored="false" %>
    <meta charset="UTF-8">
    <title>Book Library</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
          crossorigin="anonymous">
    <link rel="stylesheet" href="../../resources/css/style.css"/>

</head>
<body>
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
                       value="${requestScope.bookTitle}"/>
            </div>
            <div class="col">
                <input type="text" name="bookAuthor" class="form-control" placeholder="author"
                       value="${requestScope.bookAuthor}"/>
            </div>
            <div class="col">
                <input type="text" name="bookGenre" class="form-control" placeholder="genre"
                       value="${requestScope.bookGenre}"/>
            </div>
            <div class="col">
                <input type="text" name="bookDescription" class="form-control" placeholder="description"
                       value="${requestScope.bookDescription}"/>
            </div>
            <input id="isAvailableOnly" type="hidden" name="isAvailableOnly" value=""/>
            <div class="col">
                <button type="submit" onclick="checkParameter(this)" class="btn btn-success">Search</button>
            </div>
        </div>
    </form>
    <div class="row">
        <div class="col-9">
            <button type="button" class="btn  btn-info add-button">Add</button>
            <c:if test="${empty requestScope.books}">
                <button type="button" id="deleteBookButton" disabled class="btn  btn-outline-danger">Remove</button>
            </c:if>
            <c:if test="${not empty requestScope.books}">
                <button type="button" id="deleteBookButton" class="btn  btn-outline-danger">Remove</button>
            </c:if>
        </div>
        <div class="col-auto">
            <div class="form-group form-check">
                <c:if test="${requestScope.isAvailableOnly}">
                    <input type="checkbox" class="form-check-input" onclick="searchBookWithAvailableFilter(this)"
                           checked id="filterBook">
                </c:if>
                <c:if test="${not requestScope.isAvailableOnly}">
                    <input type="checkbox" class="form-check-input" onclick="searchBookWithAvailableFilter(this)"
                           id="filterBook">
                </c:if>
                <label class="form-check-label" for="filterBook">Filter out unavailable books</label>
            </div>
        </div>
    </div>
    <table id="searchResult" class="table table-bordered mainPageTable">
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
        <c:forEach var="book" items="${requestScope.books}">
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
                <td> ${book.publishDate}</td>
                <td>${book.inStock}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
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
<script src="../../resources/js/script.js"></script>

</body>
</html>