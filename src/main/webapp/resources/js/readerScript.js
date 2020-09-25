"use strict";

const mailPattern = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
const namePattern = /^([a-zA-Z- ]){2,15}$/;


function showModalAddRecord(obj) {
    $('#addRecord').modal('show');
}

const modalForm = document.querySelector("#addRecord form");

function showModalEditRecord(obj) {
    let readerId = obj.getAttribute("id");
    let firstName = obj.querySelector(".firstName").innerHTML;
    let lastName = obj.querySelector(".lastName").innerHTML;
    let email = obj.querySelector(".email").innerHTML;
    let phone = obj.querySelector(".phone").innerHTML;
    let gender = obj.querySelector(".gender").innerHTML;

    modalForm.querySelector("#firstName").value = firstName;
    modalForm.querySelector("#lastName").value = lastName;
    modalForm.querySelector("#email").value = email;
    modalForm.querySelector("#phoneNumber").value = phone;
    modalForm.querySelector("#" + gender).setAttribute("checked", "checked");
    modalForm.querySelector("input[name='readerId']").value = readerId;
    $('#addRecord').modal('show');
}

async function getCountReaderRecords(obj) {
    if (history.pushState) {
        let baseUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        let newUrl;
        newUrl = baseUrl + '?recordsPerPage=' + obj.value;
        history.pushState(null, null, newUrl);
    } else {
        console.warn('History API не поддерживается');
    }
    window.location.reload();
}

async function addNewReader() {

    //validate
    let requestBody = new FormData();
    let reader = createReader(modalForm);
    requestBody.append("dto", JSON.stringify(reader));

    let response = await fetch("/ajax/readers", {
        method: 'POST',
        body: requestBody,
    });

    if (response.ok) {
        $('#addRecord').modal('hide');
    } else {
        modalForm.insertAdjacentHTML('beforebegin', addDangerNotification("Reader wasn't create. Please, try later."));
    }
}

async function editReader() {

    //validate
    let requestBody = new FormData();
    let reader = createReader(modalForm);
    requestBody.append("dto", JSON.stringify(reader));

    let response = await fetch("/ajax/readers", {
        method: 'PUT',
        body: requestBody,
    });

    if (response.ok) {
        $('#addRecord').modal('hide');
    } else {
        modalForm.insertAdjacentHTML('beforebegin', addDangerNotification("Reader wasn't update. Please, try later."));
    }
}

function createReader(form) {
    return {
        id: form.querySelector("input[name='readerId']").value,
        name: form.querySelector("#firstName").value,
        lastName: form.querySelector("#lastName").value,
        email: form.querySelector("#email").value,
        phone: form.querySelector("#phoneNumber").value,
        gender: form.querySelector("input[type='radio']:checked").value,
    };
}

function saveReaderChanges() {
    let id = modalForm.querySelector("input[name='readerId']").value;
    if (id) {
        editReader();
    } else {
        addNewReader();
    }
}

function addDangerNotification(message) {
    return "<div class='alert alert-danger' role='alert'>" + message + "</div>";
}


$('#addRecord').on('hide.bs.modal', function (e) {
    modalForm.querySelectorAll("input.form-control").forEach(e => e.value = "");
    modalForm.querySelectorAll("input.form-check-input").forEach(e => {
        if (e.checked) {
            e.checked=false;
        }
    });
});
