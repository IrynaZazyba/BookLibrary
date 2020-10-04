"use strict";

const card = document.querySelector(".card");
document.addEventListener("DOMContentLoaded", menuLinkHelper);

function menuLinkHelper() {
    let urlParams = window.location.protocol + "//" + window.location.host + window.location.pathname;
    let navbar = document.querySelectorAll(".navbar li a");
    document.querySelector(".navbar li .active").classList.remove('active');
    navbar.forEach(elem => {
        if (elem.href == urlParams) {
            elem.classList.add('active');
        }
    });

    if (window.location.pathname === "/") {
        document.querySelector(".navbar li a").classList.add('active');
    }

    if (localStorage.getItem("libInfoMessage")) {
        card.insertAdjacentHTML('beforebegin', addSuccessNotification(localStorage.getItem("libInfoMessage")));
        localStorage.removeItem("libInfoMessage");
    }
}

async function doFilter(obj) {
    if (history.pushState) {
        let baseUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        let newUrl;
        newUrl = baseUrl + '?isAvailableOnly=' + obj.checked;

        let countRecords = document.querySelector('input[type="radio"]:checked');
        newUrl = newUrl + '&recordsPerPage=' + countRecords.value;

        history.pushState(null, null, newUrl);
    } else {
        console.warn('History API не поддерживается');
    }
    window.location.reload();
}

async function getCountRecords(obj) {
    let filterValue = document.getElementById("filterBook").checked;

    if (history.pushState) {
        let baseUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        let newUrl;
        newUrl = baseUrl + '?isAvailableOnly=' + filterValue;

        newUrl = newUrl + '&recordsPerPage=' + obj.value;
        history.pushState(null, null, newUrl);
    } else {
        console.warn('History API не поддерживается');
    }
    window.location.reload();
}

async function searchBookWithAvailableFilter(obj) {
    document.getElementById("isAvailableOnly").value = obj.checked;
    document.getElementById("currentPage").value = 1;
    document.querySelector("#searchForm button[type='submit']").click();
}

async function checkParameter() {
    let count = 0;
    let elementNodeListOf = document.querySelectorAll("#searchForm input[type='text']");
    elementNodeListOf.forEach(elem => {
        if (elem.value === "") {
            count++;
        }
    });

    if (count === 4) {
        event.preventDefault();
        let navMenu = document.getElementById("alertNoSelectedOptions");
        if (navMenu.style.display === 'none') {
            navMenu.style.display = 'block';
        }
        document.getElementById("deleteBookButton").setAttribute("disabled", "disabled");
    } else {
        document.getElementById("searchForm").submit();
    }
}

let searchInputs = document.querySelectorAll("#searchForm input[type='text']");
searchInputs.forEach(elem => elem.onchange = hidePreviousResult);

async function hidePreviousResult() {
    document.getElementById("searchResult").style.display = 'none';
    document.getElementById("deleteBookButton").setAttribute("disabled", "disabled");
    document.querySelector("nav >ul.pagination").style.display = 'none';
}

async function getCountRecordsSearchPage(obj) {
    document.getElementById("recordsPerPage").value = obj.value;
    document.querySelector("#searchForm button[type='submit']").click();
}

function showDeleteAlert() {
    $('#confirm-delete').modal('show');
}

async function deleteBooks() {
    $('#confirm-delete').modal('hide');
    let deletedBookIds = [];
    const deleteInputs = document.querySelectorAll(".mainPageTable tbody th input[id^='delete']");
    deleteInputs.forEach(input => {
        if (input.checked) {
            deletedBookIds.push(input.value);
        }
    });

    let formData = new FormData;
    formData.append("deletedBooks", JSON.stringify(deletedBookIds));

    let response = await fetch("/ajax/books", {
        method: 'DELETE',
        body: formData,
    });

    const result = document.querySelector("#deleteNotification .modal-body");
    if (response.ok) {
        result.insertAdjacentHTML('afterend', addSuccessNotification("Books was successfully deleted"));
    } else {
        result.insertAdjacentHTML('afterend', addDangerNotification("Please, check borrow list. Not all " +
            "books weren't delete. "));
    }

    $('#deleteNotification').modal('show');
}

function reloadBookPage() {
    document.location.reload();
}

function addDangerNotification(message) {
    return "<div class='alert alert-danger' role='alert'>" + message + "</div>";
}

function addSuccessNotification(message) {
    return "<div class='alert alert-success' role='alert'>" + message + "</div>";
}


document.querySelectorAll("nav .page-link").forEach(elem => {
    elem.addEventListener('click', checkIfNotDeleted)
});

function checkIfNotDeleted(event) {
    let checkedBooks = document.querySelectorAll(".mainPageTable th input[type='checkbox']:checked");
    if (checkedBooks.length !== 0) {
        event.preventDefault();
        document.querySelector("#confirm-pagination a").href = event.currentTarget.href;
        $('#confirm-pagination').modal('show');
    }
}

document.querySelectorAll(".mainPageTable th input[type='checkbox']").forEach(elem => {
    elem.addEventListener('change', activateRemoveButton);
});

function activateRemoveButton(event) {
    let button = document.getElementById("deleteBookButton");

    if (event.currentTarget.checked) {
        if (button.disabled) {
            button.removeAttribute("disabled");
        }
    } else {
        let isAllUnchecked = true;
        document.querySelectorAll(".mainPageTable th input[type='checkbox']").forEach(elem => {
            if (elem.checked) {
                isAllUnchecked = false;
            }
        });

        if (isAllUnchecked) {
            button.setAttribute("disabled", "disabled");
        }
    }
}

async function addLibraryInfo() {

    let bookInfoForm = document.getElementById("libInfo");
    bookInfoForm.querySelectorAll("input.form-control").forEach(elem => {
        if (elem.classList.contains('is-invalid')) {
            elem.classList.remove('is-invalid');
        }
    });

    let validationResult = true;
    bookInfoForm.querySelectorAll("input.form-control[required]").forEach(elem => {
        if (elem.value == "") {
            validationResult = false;
            elem.classList.add('is-invalid');
        }
    });

    if (validationResult) {
        let info = {
            id: bookInfoForm.infoId.value,
            name: bookInfoForm.name.value,
            address: bookInfoForm.address.value,
            signature: bookInfoForm.signature.value,
        };
        let requestBody = new FormData();
        requestBody.append("info", JSON.stringify(info));
        if (info.id) {
            updateLibraryInfo(requestBody);
        } else {
            createLibraryInfo(requestBody);
        }
    }
}

async function createLibraryInfo(body) {

    let response = await fetch("/ajax/library", {
        method: 'POST',
        body: body,
    });

    if (response.ok) {
        localStorage.setItem('libInfoMessage', "Data was saved successfully.");
        location.reload();
    } else {
        card.insertAdjacentHTML('beforebegin', addDangerNotification("Invalid data, please, check provided " +
            "info and try again."))
    }
}

async function updateLibraryInfo(body) {

    let response = await fetch("/ajax/library", {
        method: 'PUT',
        body: body,
    });

    if (response.ok) {
        localStorage.setItem('libInfoMessage', "Data was changed successfully.");
        location.reload();
    } else {
        card.insertAdjacentHTML('beforebegin', addDangerNotification("Invalid data, please, check provided " +
            "info and try again."))
    }
}







