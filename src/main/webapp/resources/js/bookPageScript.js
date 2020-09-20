"use strict";


let serverTotalAmount;
let viewTotalAmount;
let inStock;
let existedBorrowRecords = [];

window.onload = function () {
    serverTotalAmount = document.getElementById("totalAmount").value;
    viewTotalAmount = document.getElementById("totalAmount").value;
    inStock = document.getElementById("inStock").value;

    let recordsRows = document.querySelectorAll("#borrowRecordsList tbody tr");
    recordsRows.forEach(r => {
        let element = r.querySelector(".returnDate");
        if (element.innerHTML.trim() !== "") {
            existedBorrowRecords.push(r.getAttribute("id"));
        }
    });
};


let editedBorrowingRecordsIds = [];
let changedStatusBorrowRecordIds = [];


function editBorrowStatus() {
    let invalidStatusValue = document.querySelector(
        "div[id='editBorrowRecord'] form select[name='status']  + div[class='invalid-feedback']");

    //remove alert message if exists
    if (invalidStatusValue.style.display === "block") {
        invalidStatusValue.style.display = "none";
    }

    let editBorrowForm = document.querySelector("div[id='editBorrowRecord'] form");
    let status = editBorrowForm.status.value;
    if (status === "") {
        //add alert message
        invalidStatusValue.style.display = "block";
    }

    if (status !== "") {
        let editableLineId = document.querySelector("input[name='editableLineId']").value;
        let returnDateTd = document.querySelector("tr[id='" + editableLineId + "'] td[class='returnDate']");

        if (editableLineId > 0 && !existedBorrowRecords.includes(editableLineId)) {
            //add id of edited borrow records to array
            editedBorrowingRecordsIds.push(editableLineId);
        } else {
            //add id borrow records to array where changed status
            changedStatusBorrowRecordIds.push(editableLineId);
        }

        let exStatus = document.querySelector("tbody tr[id='" + editableLineId + "']").getAttribute("data-status");
        bookCountResolver(exStatus, status.toUpperCase());
        updateBookStatus();
        returnDateTd.innerHTML = dateFormatEn(new Date());
        let tr = document.querySelector("tr[id='" + editableLineId + "']");
        tr.setAttribute("data-status", status.toUpperCase());
        $('#editBorrowRecord').modal('hide')

    }
}


function bookCountResolver(exStatus, newStatus) {

    if (exStatus !== "" && exStatus !== newStatus) {

        if (exStatus === "RETURNED" && newStatus !== "RETURNED") {
            inStock--;
            viewTotalAmount--;
        }
        if (exStatus !== "RETURNED" && newStatus === "RETURNED") {
            inStock++;
            viewTotalAmount++;
        }
    } else {
        if (newStatus !== "RETURNED") {
            viewTotalAmount--;
        } else {
            inStock++;
        }
    }
}

function updateBookStatus() {

    let statusInput = document.getElementById("status");

    if (inStock !== 0) {
        document.getElementById("addBorrowRecord").removeAttribute("disabled");
        statusInput.value = `Available (${inStock} out of ${viewTotalAmount})`;
    } else {
        document.getElementById("addBorrowRecord").setAttribute("disabled", "true");
        let earliestDueDate = dateFormatEn(calculateEarliestDueDate());
        statusInput.value = `Unavailable ( expected to become available on ${earliestDueDate} )`;
    }

    let totalAmountInput = document.getElementById("totalAmount");
    totalAmountInput.value = viewTotalAmount;

}

function showModalEditBorrow(obj) {
    $('#editBorrowRecord').modal('show');

    document.getElementById("editBorrowStatus").options[0].selected = true;

    //get the line that called the modal window
    let rowWithData = obj.closest("tr");

    //add hidden input with editableLineId value to modal window
    let editableLineId = rowWithData.id;
    document.querySelector("#editBorrowRecord form").insertAdjacentHTML("afterbegin",
        "<input type='hidden' name='editableLineId' value='" + editableLineId + "'/>");

    //get data from line
    let email = rowWithData.querySelector(".email").innerHTML;
    let borrowDate = rowWithData.querySelector(".borrowDate").innerHTML;
    let dueDate = rowWithData.querySelector(".dueDate").innerHTML;
    let comment = rowWithData.querySelector(".comment").innerHTML;
    let status = rowWithData.getAttribute("data-status");

    //insert data from line to modal window
    let modalEditBorrow = document.getElementById("editBorrowRecord");
    modalEditBorrow.querySelector("#editBorrowEmail").value = email;
    modalEditBorrow.querySelector("#editBorrowName").value = obj.innerHTML;
    modalEditBorrow.querySelector("#editBorrowBorrowDate").value = borrowDate;
    modalEditBorrow.querySelector("#editBorrowBorrowDate").value = borrowDate;
    modalEditBorrow.querySelector("#editBorrowComment").value = comment;
    modalEditBorrow.querySelector("#editBorrowStatus").value = status;

    //calculate time period
    let timePeriod = ((new Date(dueDate) - new Date(borrowDate)) / 86400000);
    let timePeriodOptions = document.getElementById("editBorrowTimePeriod").options;
    Array.from(timePeriodOptions).forEach(option => {
        let temp = option.value;
        if (temp === timePeriod) {
            option.selected = true;
        }
    });
}


function showModalAddBorrow(obj) {
    $('#addNewBorrowRecord').modal('show');
}


// ex. 	August 19, 2020
function dateFormatEn(date) {
    let options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
    };
    return date.toLocaleString("en-US", options)
}

function createNewBorrowRecord() {

    let modalAddNewBorrowRecord = document.getElementById("addNewBorrowRecord");


    if (modalAddNewBorrowRecord.querySelector("#addBorrowEmail").classList.contains("is-invalid")) {
        modalAddNewBorrowRecord.querySelector("#addBorrowEmail").classList.remove("is-invalid")
    }

    if (modalAddNewBorrowRecord.querySelector("#addBorrowName").classList.contains("is-invalid")) {
        modalAddNewBorrowRecord.querySelector("#addBorrowName").classList.remove("is-invalid")
    }

    //get values from inputs
    let email = modalAddNewBorrowRecord.querySelector("#addBorrowEmail").value;
    let name = modalAddNewBorrowRecord.querySelector("#addBorrowName").value;
    let timePeriod = modalAddNewBorrowRecord.querySelector("#addBorrowTimePeriod").value;
    let comment = modalAddNewBorrowRecord.querySelector("#addBorrowComment").value;

    if (validateEmail(email) && validateName(name)) {

        let borrowDate = new Date();
        let dueDate = new Date();
        dueDate.setMonth(borrowDate.getMonth() + +timePeriod);

        //add new row to the borrow records list table
        let tbodyPartOfThTable = document.querySelector("#borrowRecordsList tbody");
        tbodyPartOfThTable.insertAdjacentHTML("beforeend",
            createRowToBorrowRecordsListTable(email, name, borrowDate, dueDate, comment, timePeriod));
        inStock--;
        insertedBorrowRecordId -= 1;

        updateBookStatus();
        $('#addNewBorrowRecord').modal('hide');
        //clear modal window
        modalAddNewBorrowRecord.querySelector("#addBorrowEmail").value = "";
        modalAddNewBorrowRecord.querySelector("#addBorrowName").value = "";
        modalAddNewBorrowRecord.querySelector("#addBorrowComment").value = "";
        modalAddNewBorrowRecord.querySelector("#addBorrowTimePeriod").options[0].selected = true;
    } else {

        if (!validateEmail(email)) {
            modalAddNewBorrowRecord.querySelector("#addBorrowEmail").classList.add("is-invalid");
        }
        if (!validateName(name)) {
            modalAddNewBorrowRecord.querySelector("#addBorrowName").classList.add("is-invalid");
        }
    }
}

let insertedBorrowRecordId = -1;

function createRowToBorrowRecordsListTable(email, name, borrowDare, dueDate, comment, period) {
    let html = "";
    html = "<tr id='" + insertedBorrowRecordId + "' data-period='" + period + "'>"
        + "<td class='email'>" + email + "</td>"
        + "<td class='name'><button type='button' disabled onclick='showModalEditBorrow(this)' class='btn btn-link'>" + name + ""
        + "</button></td>"
        + "<td class='borrowDate'>" + dateFormatEn(borrowDare) + "</td>"
        + "<td class='dueDate'>" + dateFormatEn(dueDate) + "</td>"
        + "<td class='returnDate'></td>"
        + "<td style=\"display:none\" class='comment'>Comment</td>"
        + "</tr>";
    return html;

}


function calculateEarliestDueDate() {
    let earliestReturnDate;

    let borrowRecords = document.querySelectorAll("tbody tr");
    borrowRecords.forEach(elem => {
        let returnDate = elem.querySelector(".returnDate").innerHTML.trim();

        if (!returnDate) {
            let dueDate = Date.parse(elem.querySelector(".dueDate").innerHTML);
            if (!earliestReturnDate) {
                earliestReturnDate = dueDate;
            }

            if (dueDate < earliestReturnDate) {
                earliestReturnDate = dueDate;
            }
        }
    });
    return new Date(earliestReturnDate);
}

let readersJson;

$('.basicAutoComplete').autoComplete({
        resolver: 'custom',
        events: {
            search: async function (qry, callback) {
                // let's do a custom ajax call
                let response = await fetch("/ajax/reader?email=" + qry, {
                    method: 'GET',
                });

                if (response.ok) {
                    let emails = [];

                    let json = await response.json();
                    if (json.hasOwnProperty("readers")) {
                        readersJson = json.readers;
                        json.readers.forEach(elem => emails.push(elem.email));
                    }
                    callback(emails);
                }
            },
        }
    }
);


$('.basicAutoComplete').on('autocomplete.select', function (evt, item) {
    let name;

    Array.from(readersJson).forEach(elem => {

        if (elem.email === item) {
            name = elem.name;
        }
    });
    document.getElementById("addBorrowName").value = name;

});
let navBar = document.querySelector("#result");

function saveChangesBookPage() {

    navBar.innerHTML="";
    document.getElementById("newTotalAmount").value = serverTotalAmount;
    let description=document.getElementById("description");

    if(description.classList.contains("is-invalid")){
        description.classList.remove("is-invalid");
    }

    if (validateTotalAmount()&&validateDescriptionSize(description.value)) {
        updateBookInfo();
    } else {

        if(!validateDescriptionSize(description)){
           description.classList.add("is-invalid");
        }
        let navBar = document.querySelector("result");
        navBar.insertAdjacentHTML('afterbegin', addDangerNotification("Invalid parameters"));
    }

}

async function updateBookInfo() {

    let bookInfoForm = document.getElementById("bookInfo");

    let bookDto = {
        id: bookInfoForm.bookId.value.trim(),
        title: bookInfoForm.title.value.trim(),
        publisher: bookInfoForm.publisher.value.trim(),
        publishDate: bookInfoForm.publishDate.value.trim(),
        author: bookInfoForm.author.value.trim(),
        genre: bookInfoForm.genre.value.trim(),
        pageCount: bookInfoForm.pageCount.value.trim(),
        isbn: bookInfoForm.isbn.value.trim(),
        description: bookInfoForm.description.value.trim(),
        totalAmount: serverTotalAmount,
    };

    let imageForm=document.getElementById("cover");
    let requestBody = new FormData(imageForm);
    requestBody.append("bookDto", JSON.stringify(bookDto));

    let response = await fetch("/ajax/book", {
        method: 'PUT',
        body: requestBody,
    });

    if (response.ok) {
        navBar.insertAdjacentHTML('afterbegin', addSuccessNotification("Book was updated successfully"));
    } else {
        navBar.insertAdjacentHTML('afterbegin', addDangerNotification("Book wasn't updated. Please, try later."));
    }

    if (response.status != null) {
        returnBookToLibrary();
    }

}

async function returnBookToLibrary() {

    let formData = new FormData();
    let editedRecords = [];

    let borrowRecordsForm = document.querySelectorAll("#borrowRecordsList tbody tr");
    borrowRecordsForm.forEach(elem => {

        let id = elem.getAttribute("id");

        if (editedBorrowingRecordsIds.includes(id)) {
            let editedBorrowRecord = createEditBorrowRecord(elem, id);
            editedRecords.push(editedBorrowRecord);
        }
    });

    formData.append("editedRecords", JSON.stringify(editedRecords));

    let response = await fetch("/ajax/newStatus", {
        method: 'PUT',
        body: formData,
    });

    editedBorrowingRecordsIds = [];

    if (response.ok) {
        let json = await response.json();
        if (json !== null && json.hasOwnProperty("message")) {
            navBar.insertAdjacentHTML('afterbegin', addDangerNotification("Not all book was returned. Please, reload page to check."));
        } else {
            navBar.insertAdjacentHTML('afterbegin', addSuccessNotification("Book was successfully returned"));
        }
    } else {
        navBar.insertAdjacentHTML('afterbegin', addDangerNotification("Book wasn't return. Please, try later."));
    }

    if (response.status != null) {
        addNewBorrowRecord();
    }
}

async function addNewBorrowRecord() {

    let formData = new FormData();
    let addedRecords = [];


    let borrowRecordsForm = document.querySelectorAll("#borrowRecordsList tbody tr");
    borrowRecordsForm.forEach(elem => {

        let id = elem.getAttribute("id");
        if (id < 0) {
            let addedBorrowRecord = createAddBorrowRecord(elem);
            addedRecords.push(addedBorrowRecord);
        }
    });

    formData.append("addedRecords", JSON.stringify(addedRecords));

    let response = await fetch("/ajax/record", {
        method: 'POST',
        body: formData,
    });

    if (response.ok) {
        let json = await response.json();
        if (json !== null && json.hasOwnProperty("message")) {
            navBar.insertAdjacentHTML('afterbegin', addDangerNotification("Not all borrow record was added. Please, reload page to check."));
        } else {
            navBar.insertAdjacentHTML('afterbegin', addSuccessNotification("New borrow record was successfully added"));
        }
    } else {
        navBar.insertAdjacentHTML('afterbegin', addDangerNotification("Borrow record wasn't add. Please, try later."));
    }

    if (response.status != null) {
        changeBorrowRecordStatus();
    }

}


async function changeBorrowRecordStatus() {
    let formData = new FormData();
    let updatedRecords = [];

    let borrowRecordsForm = document.querySelectorAll("#borrowRecordsList tbody tr");
    borrowRecordsForm.forEach(elem => {

        let id = elem.getAttribute("id");
        if (changedStatusBorrowRecordIds.includes(id)) {
            let addedBorrowRecord = createEditBorrowRecord(elem, id);
            updatedRecords.push(addedBorrowRecord);
        }
    });

    formData.append("updatedStatus", JSON.stringify(updatedRecords));
    let response = await fetch("/ajax/oldStatus", {
        method: 'PUT',
        body: formData,
    });

    if (response.ok) {
        let json = await response.json();
        if (json !== null && json.hasOwnProperty("message")) {
            navBar.insertAdjacentHTML('afterbegin', addDangerNotification("Not all statuses was updated. Please, reload page to check."));
        } else {
            navBar.insertAdjacentHTML('afterbegin', addSuccessNotification("Borrow record status was successfully changed"));
        }
    } else {
        navBar.insertAdjacentHTML('afterbegin',
            addDangerNotification("Borrow record status was successfully changed. Please, try later."));
    }
}


function addDangerNotification(message) {
    return "<div class='alert alert-danger' role='alert'>" + message + "</div>";
}

function addSuccessNotification(message) {
    return "<div class='alert alert-success' role='alert'>" + message + "</div>";
}


function createAddBorrowRecord(elem) {
    let email = elem.querySelector(".email").innerHTML;
    let name = elem.querySelector(".name button").innerHTML;
    let comment = elem.querySelector(".comment").innerHTML;
    let bookId = document.getElementById("bookId").value;
    let period = elem.getAttribute("data-period");

    return {
        reader: {
            email: email,
            name: name,
        },
        bookId: bookId,
        timePeriod: period,
        comment: comment
    };
}


function createEditBorrowRecord(elem, id) {
    let status = elem.getAttribute("data-status");
    let comment = elem.querySelector(".comment").innerHTML;
    let bookId = document.getElementById("bookId").value;

    return {
        id: id,
        bookId: bookId,
        status: status,
        comment: comment
    };
}

document.getElementById("totalAmount").addEventListener("change", (event) => {

    let totalAmountInput = document.getElementById("totalAmount");
    let newTotalAmount = totalAmountInput.value;

    let isValid = validateTotalAmount();
    if (!isValid) {
        totalAmountInput.classList.add("is-invalid");
    } else {
        if (totalAmountInput.classList.contains("is-invalid")) {
            totalAmountInput.classList.remove("is-invalid");
        }
        let temp = viewTotalAmount - inStock;
        serverTotalAmount = newTotalAmount;
        viewTotalAmount = newTotalAmount;
        inStock = newTotalAmount - temp;

        updateBookStatus();
    }
});


function validateTotalAmount() {
    let totalAmountInput = document.getElementById("totalAmount");
    let newTotalAmount = totalAmountInput.value;
    return newTotalAmount >= (viewTotalAmount - inStock) && newTotalAmount > 0;
}

function validateName(name) {
    let pattern = /^([a-zA-Z- ]){2,25}$/;
    return name.length !== 0 && pattern.test(name);
}

function validateEmail(email) {
    let pattern = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
   // return email.length !== 0 && pattern.test(email);
    return true;
}

function validateDescriptionSize (description) {
    return description.length<350;
}