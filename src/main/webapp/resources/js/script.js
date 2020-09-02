"use strict";

async function doFilter(obj) {

    if (history.pushState) {
        let baseUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        let newUrl;
        newUrl = baseUrl + '?isFiltered=' + obj.checked;

        let countRecords = document.querySelector('input[type="radio"]:checked');
        newUrl = newUrl + '&recordsPerPage='+countRecords.value;

        history.pushState(null, null, newUrl);
    }
    else {
        console.warn('History API не поддерживается');
    }
    window.location.reload();
}

async function getCountRecords(obj) {
    let filterValue = document.getElementById("filterBook").checked;

    if (history.pushState) {
        let baseUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        let newUrl;
        newUrl = baseUrl + '?isFiltered=' + filterValue;

        newUrl = newUrl + '&recordsPerPage=' + obj.value;
        history.pushState(null, null, newUrl);
    }
    else {
        console.warn('History API не поддерживается');
    }
    window.location.reload();

}