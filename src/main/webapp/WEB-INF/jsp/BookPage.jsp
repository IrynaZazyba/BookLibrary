<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib uri="customtag" prefix="vh" %>

    <meta charset="UTF-8">
    <title>Book Library</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
          crossorigin="anonymous">
    <link rel="stylesheet" href="/resources/css/style.css"/>
</head>
<body>

<div class="modal fade" id="addNewBorrowRecord" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog"
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
                    <div class="form-group">
                        <div class="form-row">
                            <label for="addBorrowEmail" class="col-sm-5 col-form-label">Reader email
                                address</label>
                            <div class="col">
                                <input type="email" required autocomplete="off" value=""
                                       class="form-control basicAutoComplete"
                                       id="addBorrowEmail">
                                <div class="invalid-feedback">
                                    Please provide valid email.
                                </div>
                            </div>

                        </div>
                    </div>
                    <div class="form-group">

                        <div class="form-row">
                            <label for="addBorrowName" class="col-sm-5 col-form-label">Reader name</label>
                            <div class="col">
                                <input type="text" required id="addBorrowName" class="form-control">
                                <div class="invalid-feedback">
                                    Please provide valid name.
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">

                        <div class="form-row">
                            <label for="addBorrowTimePeriod" class="col-sm-5 col-form-label">Time period</label>
                            <div class="col">
                                <select name="timePeriod" required class="form-control" id="addBorrowTimePeriod">
                                    <option value="ONE">1</option>
                                    <option value="TWO">2</option>
                                    <option value="THREE">3</option>
                                    <option value="SIX">6</option>
                                    <option value="TWELVE">12</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="addBorrowComment" class="col-sm-5 col-form-label">Comment</label>
                            <div class="col">
                                <textarea id="addBorrowComment" class="form-control"></textarea>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Discard</button>
                <button type="button" onclick="createNewBorrowRecord()" class="btn btn-primary">Save</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="editBorrowRecord" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog"
     aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="staticBackdropLabelEditBorrow"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="editBorrowEmail" class="col-sm-5 col-form-label">Reader email address</label>
                            <div class="col">
                                <input type="email" readonly class="form-control email"
                                       id="editBorrowEmail">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="editBorrowName" class="col-sm-5 col-form-label">Reader name</label>
                            <div class="col">
                                <input type="text" readonly id="editBorrowName" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="editBorrowBorrowDate" class="col-sm-5 col-form-label">Borrow date</label>
                            <div class="col">
                                <input type="text" readonly id="editBorrowBorrowDate" class="form-control borrowDate">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="editBorrowTimePeriod" class="col-sm-5 col-form-label">Time period</label>
                            <div class="col">
                                <select disabled class="form-control dueDate" id="editBorrowTimePeriod">
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>6</option>
                                    <option>12</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="editBorrowStatus" class="col-sm-5 col-form-label">Status</label>
                            <div class="col">
                                <select required name="status" class="form-control" id="editBorrowStatus">
                                    <option value=""></option>
                                    <option value="RETURNED">returned</option>
                                    <option value="RETURNED_AND_DAMAGED">returned and damaged</option>
                                    <option value="LOST">lost</option>
                                </select>
                                <div class="invalid-feedback">
                                    Please choose a status.
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-row">
                            <label for="editBorrowComment" class="col-sm-5 col-form-label">Comment</label>
                            <div class="col">
                                <textarea id="editBorrowComment" class="form-control"></textarea>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Discard</button>
                <button type="button" onclick="editBorrowStatus()" class="btn btn-primary">Save</button>
            </div>

        </div>
    </div>
</div>


<div class="modal fade" id="resultNotification" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog"
     aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="staticBackdropLabel">Notification</h5>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" onclick="reloadBookPage()" class="btn btn-primary">Back to page</button>
            </div>
        </div>
    </div>
</div>


<div class="container">

    <jsp:include page="parts/NavigationBar.jsp"/>
    <div id="result"></div>
    <div class="row" style="margin-top: 15px">
        <div class="col-3">
            <form id="cover" enctype="multipart/form-data">
                <div>
                    <label class="downloadImageLabel" for="image_uploads">Choose images to upload cover (PNG,
                        JPG)</label>
                    <input type="file" id="image_uploads" name="image_uploads" accept=".jpg, .png"
                           style="display: none">
                </div>
                <div class="preview">
                    <c:if test="${not empty requestScope.bookPageDto.book.coverPath}">
                        <img alt="cover" style="width:240px;height: 320px"
                             src="${pageContext.request.contextPath}/book/cover?name=${requestScope.bookPageDto.book.coverPath}">
                    </c:if>
                    <c:if test="${empty requestScope.bookPageDto.book.coverPath}">
                        <img alt="cover" style="width:240px;height: 320px" src="/resources/img/book.png">
                    </c:if>

                </div>
            </form>
        </div>
        <div class="col-9">
            <form id="bookInfo">
                <div class="form-group row">
                    <label for="title" class="col-sm-2 col-form-label"><b>title</b></label>
                    <div class="col-sm-10">
                        <input type="text" name="title" class="form-control" required
                               value="${requestScope.bookPageDto.book.title}"
                               id="title">
                    </div>
                </div>
                <div class="form-group row">
                    <label for="author" class="col-sm-2 col-form-label"> <b>author(-s): </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="author" class="form-control"
                               value="<vh:authors authors="${requestScope.bookPageDto.book.author}"/>"
                               id="author" required/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="publisher" class="col-sm-2 col-form-label"><b>publisher: </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="publisher" class="form-control"
                               value="${requestScope.bookPageDto.book.publisher.publisherName}"
                               id="publisher" required/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="publishDate" class="col-sm-2 col-form-label"><b>publish date: </b></label>
                    <div class="col-sm-10">
                        <input type="date" required name="publishDate" class="form-control"
                               value="${requestScope.bookPageDto.book.publishDate}"
                               id="publishDate">
                        <div class="invalid-feedback">
                            Invalid date.
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="genre" class="col-sm-2 col-form-label"><b>genre(-s): </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="genre" class="form-control"
                               value="<vh:genres genres="${requestScope.bookPageDto.book.genres}"/>"
                               id="genre" required/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="pageCount" class="col-sm-2 col-form-label"><b>page count: </b></label>
                    <div class="col-sm-10">
                        <input type="number" min="1" name="pageCount" class="form-control"
                               value="${requestScope.bookPageDto.book.pageCount}"
                               id="pageCount" required/>
                        <div class="invalid-feedback">
                            Invalid data.
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="isbn" class="col-sm-2 col-form-label"><b>ISBN: </b></label>
                    <div class="col-sm-10">
                        <input data-bv-isbn="true" type="text" name="isbn" class="form-control"
                               value="${requestScope.bookPageDto.book.ISBN}"
                               id="isbn" required/>
                        <div class="invalid-feedback">
                            Invalid data.
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="description" class="col-sm-2 col-form-label"><b>description: </b></label>
                    <div class="col-sm-10">
                        <textarea class="form-control" name="description"
                                  id="description">${requestScope.bookPageDto.book.description}</textarea>
                        <div class="invalid-feedback">
                            Length is too long.
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="totalAmount" class="col-sm-2 col-form-label"><b>total amount: </b></label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" required
                               value="${requestScope.bookPageDto.book.totalAmount}" id="totalAmount"/>
                        <input type="hidden" name="totalAmount" class="form-control"
                               value="" id="newTotalAmount"/>
                        <div class="invalid-feedback">
                            Please provide a valid total amount.
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="status" class="col-sm-2 col-form-label"><b>status: </b></label>
                    <div class="col-sm-10">
                        <input disabled type="text" name="status" class="form-control"
                               value="${requestScope.bookPageDto.book.status}"
                               id="status"/>
                    </div>
                </div>
                <input type="hidden" class="form-control"
                       value="${requestScope.bookPageDto.book.inStock}" id="inStock"/>
                <input type="hidden" class="form-control" name="bookId"
                       value="${requestScope.bookPageDto.book.id}" id="bookId"/>
            </form>
        </div>
    </div>

    <h5>Borrow records list</h5>
    <c:choose>

        <c:when test="${requestScope.bookPageDto.book.inStock>0}">
            <button id="addBorrowRecord" type="button" style="margin: 5px 0" class="btn btn-outline-info"
                    onclick="showModalAddBorrow(this)">Add
            </button>
        </c:when>
        <c:otherwise>
            <button id="addBorrowRecord" disabled type="button" style="margin: 5px 0" class="btn btn-outline-info"
                    onclick="showModalAddBorrow(this)">Add
            </button>
        </c:otherwise>
    </c:choose>

    <table id="borrowRecordsList" class="table table-bordered table-sm">
        <thead>
        <tr>
            <th scope="col">reader email address</th>
            <th scope="col">reader name</th>
            <th scope="col">borrow date</th>
            <th scope="col">due date</th>
            <th scope="col">return date</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="borrowRecord" items="${requestScope.bookPageDto.borrowRecords}">
            <tr id="${borrowRecord.id}" data-status="${borrowRecord.status}" data-period="">
                <td class="email"> ${borrowRecord.reader.email}</td>
                <td class="name">
                    <button type="button" onclick="showModalEditBorrow(this)"
                            class="btn btn-link">${borrowRecord.reader.name}</button>
                </td>
                <td class="borrowDate"><vh:local-date-time date="${borrowRecord.borrowDate}"/></td>
                <td class="dueDate"><vh:local-date-time date="${borrowRecord.dueDate}"/></td>
                <td class="returnDate"><c:if test="${not empty borrowRecord.returnDate}">
                    <vh:local-date-time date="${borrowRecord.returnDate}"/>
                </c:if>
                </td>
                <td style="display: none" class="comment">${borrowRecord.comment}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <button type="button" class="btn btn-info add-button" onclick="saveChangesBookPage()">Save</button>
    <a href="${pageContext.request.contextPath}/books">
        <button type="button" class="btn  btn-outline-danger">Discard</button>
    </a>
</div>
<jsp:include page="parts/BootstrapScript.jsp"/>
<script src="https://cdn.jsdelivr.net/gh/xcash/bootstrap-autocomplete@v2.3.7/dist/latest/bootstrap-autocomplete.min.js"></script>
<script src="/resources/js/script.js"></script>
<script src="/resources/js/bookPageScript.js"></script>
</body>
</html>