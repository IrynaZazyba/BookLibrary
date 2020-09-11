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

<div class="container">

    <jsp:include page="parts/NavigationBar.jsp"/>

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
                    <img src="#">
                </div>
            </form>
        </div>
        <div class="col-9">
            <form>
                <div class="form-group row">
                    <label for="title" class="col-sm-2 col-form-label"><b>title</b></label>
                    <div class="col-sm-10">
                        <input type="text" name="title" class="form-control"
                               value="${requestScope.bookPageDto.book.title}"
                               id="title">
                    </div>
                </div>
                <div class="form-group row">
                    <label for="author" class="col-sm-2 col-form-label"> <b>author(-s): </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="author" class="form-control"
                               value="<vh:authors-tag authors="${requestScope.bookPageDto.book.author}"/>"
                               id="author"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="publisher" class="col-sm-2 col-form-label"><b>publisher: </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="publisher" class="form-control"
                               value="${requestScope.bookPageDto.book.publisher.publisherName}"
                               id="publisher"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="publishDate" class="col-sm-2 col-form-label"><b>publish date: </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="publishDate" class="form-control"
                               value="<vh:local-date date="${requestScope.bookPageDto.book.publishDate}"/>"
                               id="publishDate">


                    </div>
                </div>
                <div class="form-group row">
                    <label for="genre" class="col-sm-2 col-form-label"><b>genre(-s): </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="genre" class="form-control"
                               value="<vh:genres-tag genres="${requestScope.bookPageDto.book.genres}"/>"
                               id="genre"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="pageCount" class="col-sm-2 col-form-label"><b>page count: </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="pageCount" class="form-control"
                               value="${requestScope.bookPageDto.book.pageCount}"
                               id="pageCount"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="isbn" class="col-sm-2 col-form-label"><b>ISBN: </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="isbn" class="form-control"
                               value="${requestScope.bookPageDto.book.ISBN}"
                               id="isbn"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="description" class="col-sm-2 col-form-label"><b>description: </b></label>
                    <div class="col-sm-10">
                        <textarea class="form-control" name="description"
                                  id="description">${requestScope.bookPageDto.book.description}"
                        </textarea>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="totalAmount" class="col-sm-2 col-form-label"><b>total amount: </b></label>
                    <div class="col-sm-10">
                        <input type="text" name="totalAmount" class="form-control"
                               value="${requestScope.bookPageDto.book.totalAmount}" id="totalAmount"/>
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
            </form>

        </div>

    </div>


    <h5>Borrow records list</h5>
    <button type="button" style="margin: 5px 0" class="btn btn-outline-info"
            onclick="showModalAddBorrow(this)">Add
    </button>

    <table id="borrowRecordsList" class="table table-bordered table-sm">
        <thead>
        <tr>
            <th scope="col">reader email adress</th>
            <th scope="col">reader name</th>
            <th scope="col">borrow date</th>
            <th scope="col">due date</th>
            <th scope="col">return date</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

    <button type="button" class="btn btn-info add-button">Save</button>
    <button type="button" class="btn  btn-outline-danger">Discard</button>
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
<script src="/resources/js/script.js"></script>

</body>
</html>