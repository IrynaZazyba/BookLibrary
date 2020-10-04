<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <c:forEach var="book" items="${requestScope.dto.books}">
        <tr>
            <th>
                <div class="form-group form-check">
                    <input type="checkbox" value="${book.id}" class="form-check-input" id="delete-${book.id}">
                    <label class="form-check-label" for="delete-${book.id}"></label>
                </div>
            </th>
            <td><a href="${pageContext.request.contextPath}/books/${book.id}">${book.title}</a></td>
            <td>
                <c:forEach var="author" items="${book.author}" varStatus="loop">
                    <c:out value="${author.name}"/>
                    <c:if test="${not loop.last}">,</c:if>
                </c:forEach>
            </td>
            <td>${book.publishDate}</td>
            <td>${book.inStock}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
