package com.itechart.javalab.library.controller.command.ajax;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AjaxCommand {

    String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    default String addResponseMessage(HttpServletResponse response, String message, Gson gson) {
        response.setStatus(HttpServletResponse.SC_OK);
        JsonObject responseMessage = new JsonObject();
        JsonElement jsonElement = gson.toJsonTree(message);
        responseMessage.add("message", jsonElement);
        return responseMessage.toString();
    }
}
