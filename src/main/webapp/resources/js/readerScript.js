"use strict";

const emailPattern = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
const namePattern = /^[a-zA-Z- ]{2,25}$/;
const phonePattern = /^[8][0-9]{10}$|^[+][3][7][5][0-9]{9}$/;

function showModalAddRecord(obj) {
    deleteInvalidNotification();
    let alertMessage = document.querySelector("#addRecord .modal-body div.alert");
    if (alertMessage) {
        alertMessage.remove();
    }
    $('#addRecord').modal('show');
}

const modalForm = document.querySelector("#addRecord form");

function showModalEditRecord(obj) {
    deleteInvalidNotification();
    let alertMessage = document.querySelector("#addRecord .modal-body div.alert");
    if (alertMessage != null) {
        alertMessage.remove();
    }
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
    modalForm.querySelector("input[name='readerId']").value = readerId;

    modalForm.querySelectorAll("input.form-check-input").forEach(e => {
        if (e.checked) {
            e.checked = false;
        }
    });
    modalForm.querySelector(".form-check-input[value='" + gender + "']").checked = true;
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

    let requestBody = new FormData();
    let reader = createReader(modalForm);
    requestBody.append("dto", JSON.stringify(reader));

    let response = await fetch("/ajax/readers", {
        method: 'POST',
        body: requestBody,
    });

    if (response.ok) {
        $('#addRecord').modal('hide');
        location.reload();
    } else {
        if (response.status === 400) {
            modalForm.insertAdjacentHTML('beforebegin', addDangerNotification("Please, check reader information."));
        }
        if (response.status === 409) {
            modalForm.insertAdjacentHTML('beforebegin', addDangerNotification("Reader with such email already exists."));
        }
    }
}

async function editReader() {

    let requestBody = new FormData();
    let reader = createReader(modalForm);
    requestBody.append("dto", JSON.stringify(reader));

    let response = await fetch("/ajax/readers", {
        method: 'PUT',
        body: requestBody,
    });

    if (response.ok) {
        $('#addRecord').modal('hide');
        location.reload();
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
    deleteInvalidNotification();
    let alertMessage = document.querySelector("#addRecord .modal-body div.alert");
    if (alertMessage != null) {
        alertMessage.remove();
    }

    if (checkRequiredAttribute() && validateReader()) {
        let id = modalForm.querySelector("input[name='readerId']").value;
        if (id) {
            editReader();
        } else {
            addNewReader();
        }
    }
}

function addDangerNotification(message) {
    return "<div class='alert alert-danger' role='alert'>" + message + "</div>";
}

$('#addRecord').on('hide.bs.modal', function (e) {
    modalForm.querySelectorAll("input.form-control").forEach(e => e.value = "");
});

function validateReader() {
    let result = true;
    let firstName = modalForm.querySelector("#firstName");
    let lastName = modalForm.querySelector("#lastName");
    let email = modalForm.querySelector("#email");
    let phone = modalForm.querySelector("#phoneNumber");

    if (!namePattern.test(firstName.value)) {
        firstName.classList.add("is-invalid");
        result = false;
    }

    if (!namePattern.test(lastName.value)) {
        lastName.classList.add("is-invalid");
        result = false;
    }

    if (!emailPattern.test(email.value)) {
        email.classList.add("is-invalid");
        result = false;
    }

    if (phone.value !== "" && !phonePattern.test(phone.value)) {
        phone.classList.add("is-invalid");
        result = false;
    }
    return result;
}

function checkRequiredAttribute() {
    let result = true;
    modalForm.querySelectorAll("input.form-control").forEach(e => {
        if (e.required && e.value === "") {
            e.classList.add("is-invalid");
            result = false;
        }
    });
    if (modalForm.querySelectorAll("input.form-check-input:checked") == null) {
        result = false;
    }
    return result;
}

function deleteInvalidNotification() {
    modalForm.querySelectorAll("input.form-control").forEach(e => {
        if (e.classList.contains("is-invalid")) {
            e.classList.remove("is-invalid");
        }
    });
}