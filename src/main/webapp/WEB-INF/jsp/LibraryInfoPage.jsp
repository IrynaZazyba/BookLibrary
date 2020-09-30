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
<div class="container">

    <jsp:include page="parts/NavigationBar.jsp"/>
    <div class="card bg-light mb-3">
        <div class="card-header"><h5 class="card-title">Please, provide info to construct automatic emails to
            readers from the library.</h5></div>
        <div class="card-body">
            <p class="card-text">
            <form id="libInfo" class="form-horizontal" role="form" method="GET">
                <div class="form-row">
                    <div class="col">
                        <input type="text" name="address" class="form-control" placeholder="address"
                               value="${requestScope.emailInfo.address}"/>
                    </div>
                    <div class="col">
                        <input type="text" name="name" class="form-control" placeholder="name"
                               value="${requestScope.emailInfo.name}"/>
                    </div>
                    <div class="col">
                        <input type="text" name="signature" class="form-control" placeholder="signature"
                               value="${requestScope.emailInfo.signature}"/>
                    </div>
                    <div class="col">
                        <button type="submit" class="btn btn-info">Save</button>
                    </div>
                </div>
            </form>
            </p>
        </div>
    </div>
    <jsp:include page="parts/BootstrapScript.jsp"/>
    <script src="/resources/js/script.js"></script>
</body>
</html>