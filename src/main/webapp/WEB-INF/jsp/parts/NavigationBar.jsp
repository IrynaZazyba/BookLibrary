<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>

<nav class="navbar navbar-expand-sm navbar-dark bg-dark nav-bar">
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link active" href="${pageContext.request.contextPath}/books">Main Page</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/books/search">Search Page</a>
            </li>
        </ul>
    </div>
</nav>