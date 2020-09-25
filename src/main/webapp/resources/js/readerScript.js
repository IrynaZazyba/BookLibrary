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


