"use strict";

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

async  function getCountRecordsSearchPage(obj){
    document.getElementById("recordsPerPage").value = obj.value;
    document.querySelector("#searchForm button[type='submit']").click();
}


let input = document.getElementById('image_uploads');
let preview = document.querySelector('.preview');
input.style.display = "none";
input.addEventListener('change', updateImageDisplay);

function updateImageDisplay() {
    while (preview.firstChild) {
        preview.removeChild(preview.firstChild);
    }

    let curFiles = input.files;
    let image = document.createElement('img');
    image.style.width = "200px";
    image.style.height = "200px";
    if (curFiles[0] == null) {
        image.src = "img/book.png";
    } else {
        image.src = window.URL.createObjectURL(curFiles[0]);
    }
    preview.appendChild(image);
}


async function deleteBooks() {
    let deletedBookIds=[];
    const deleteInputs = document.querySelectorAll(".mainPageTable tbody th input[id^='delete']");
    deleteInputs.forEach(input=>{
        if(input.checked){
            deletedBookIds.push(input.value);
        }
    });

    let formData=new FormData;
    formData.append("deletedBooks", JSON.stringify(deletedBookIds));

    let response = await fetch("/ajax/books", {
        method: 'DELETE',
        body: formData,
    });

    const navBar = document.querySelector("nav");
    if (response.ok) {
        navBar.insertAdjacentHTML('afterend', addSuccessNotification("Books was successfully deleted"));
    } else {
        navBar.insertAdjacentHTML('afterend', addDangerNotification("Books  weren't delete. Please, check borrow list."));
    }
}

function addDangerNotification(message) {
    return "<div class='alert alert-danger' role='alert'>" + message + "</div>";
}

function addSuccessNotification(message) {
    return "<div class='alert alert-success' role='alert'>" + message + "</div>";
}
