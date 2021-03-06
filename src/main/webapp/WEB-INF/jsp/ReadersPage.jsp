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
<div class="modal fade" id="addRecord" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog"
     aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="staticBackdropLabelAddNewBorrow"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">

                <form>
                    <input type="hidden" name="editableLineId" value="">
                    <div class="form-group">
                        <div class="form-row">
                            <label for="firstName" class="col-sm-5 col-form-label">First name</label>
                            <div class="col">
                                <input type="text" id="firstName" required class="form-control"/>
                                <div class="invalid-feedback">
                                    Please provide valid first name.
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="lastName" class="col-sm-5 col-form-label">Last name</label>
                            <div class="col">
                                <input type="text" required id="lastName" class="form-control"/>
                                <div class="invalid-feedback">
                                    Please provide valid last name.
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="email" class="col-sm-5 col-form-label">Email address</label>
                            <div class="col">
                                <input type="email" required id="email" class="form-control"/>
                                <div class="invalid-feedback">
                                    Please provide valid email.
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="phoneNumber" class="col-sm-5 col-form-label">Phone</label>
                            <div class="col">
                                <input type="tel"  id="phoneNumber" class="form-control">
                                <div class="invalid-feedback">
                                    Please provide valid phone.
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <label class="col-sm-5 col-form-label">Gender</label>
                        <div class="col">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="gender" id="male"
                                       value="MALE">
                                <label class="form-check-label" for="male">
                                    male </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="gender" id="female"
                                       value="FEMALE">
                                <label class="form-check-label" for="female">
                                    female
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input"  checked type="radio" name="gender" id="other"
                                       value="OTHER">
                                <label class="form-check-label" for="other">
                                    other
                                </label>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" class="form-control" name="readerId" value="">
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Discard</button>
                <button type="button" onclick="saveReaderChanges()" class="btn btn-info">Save</button>
            </div>
        </div>
    </div>
</div>

<div class="container">

    <jsp:include page="parts/NavigationBar.jsp"/>

    <div class="row">
        <div class="col-11">
            <button type="button" onclick="showModalAddRecord(this)" class="btn  btn-info add-button">Add</button>
        </div>
        <div class="col-auto">
            <div class="form_toggle">
                <c:if test="${requestScope.dto.recordsPerPage==10}">
                    <div class="form_toggle-item item-1">
                        <input onclick="getCountReaderRecords(this)" id="count-10" type="radio" name="radio" value="10"
                               checked>
                        <label for="count-10">10</label>
                    </div>
                    <div class="form_toggle-item item-2">
                        <input onclick="getCountReaderRecords(this)" id="count-20" type="radio" name="radio" value="20">
                        <label for="count-20">20</label>
                    </div>
                </c:if>
                <c:if test="${requestScope.dto.recordsPerPage==20}">
                    <div class="form_toggle-item item-1">
                        <input onclick="getCountReaderRecords(this)" id="count-10" type="radio" name="radio" value="10">
                        <label for="count-10">10</label>
                    </div>
                    <div class="form_toggle-item item-2">
                        <input onclick="getCountReaderRecords(this)" id="count-20" type="radio" name="radio" value="20"
                               checked>
                        <label for="count-20">20</label>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <table class="table table-bordered readersTable">
        <thead>
        <tr>
            <th scope="col">name</th>
            <th scope="col">email address</th>
            <th scope="col">date of registration</th>
            <th scope="col">phone number</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="reader" items="${requestScope.dto.readers}">
            <tr onclick="showModalEditRecord(this)" id="${reader.id}">
                <td style="display: none" class="firstName">${reader.name}</td>
                <td style="display: none" class="lastName">${reader.lastName}</td>
                <td class="name">${reader.name} ${reader.lastName}</td>
                <td class="email">${reader.email}</td>
                <td class="regDate">${reader.registrationDate}</td>
                <td class="phone">${reader.phone}</td>
                <td style="display: none" class="gender">${reader.gender}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

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
                               href="${pageContext.request.contextPath}/readers?currentPage=${i}&recordsPerPage=${requestScope.dto.recordsPerPage}">
                                    ${i}
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </nav>
    </div>
</div>
<jsp:include page="parts/BootstrapScript.jsp"/>
<script src="resources/js/script.js" charset="utf-8"></script>
<script src="resources/js/readerScript.js" charset="utf-8"></script>
</body>
</html>