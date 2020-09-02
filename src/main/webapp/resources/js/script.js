"use strict";

async function doFilter(obj) {

    if (history.pushState) {
        let baseUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        let newUrl;
        newUrl = baseUrl + '?isFiltered=' + obj.checked;

        history.pushState(null, null, newUrl);
    }
    else {
        console.warn('History API не поддерживается');
    }
    window.location.reload();
}