package com.itechart.javalab.library.controller.command;

import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.controller.command.ajax.impl.*;
import com.itechart.javalab.library.controller.command.front.Command;
import com.itechart.javalab.library.controller.command.front.impl.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {

    private static volatile CommandProvider instance;
    private final Map<CommandKey, Command> commandRepository = new HashMap<>();
    private final Map<CommandKey, AjaxCommand> ajaxCommandRepository = new HashMap<>();

    private CommandProvider() {
        commandRepository.put(new CommandKey("GET", "/"), new GetBooksCommand());
        commandRepository.put(new CommandKey("GET", "/books"), new GetBooksCommand());
        commandRepository.put(new CommandKey("GET", "/books/search"), new SearchBooksCommand());
        commandRepository.put(new CommandKey("GET", "/books/"), new GetBookCommand());
        commandRepository.put(new CommandKey("GET", "/book/cover"), new GetFileCommand());
        commandRepository.put(new CommandKey("GET", "/books/page"), new GetBookPageCommand());
        ajaxCommandRepository.put(new CommandKey("PUT", "/ajax/book"), new UpdateBookCommand());
        ajaxCommandRepository.put(new CommandKey("PUT", "/ajax/newStatus"), new ReturnBookCommand());
        ajaxCommandRepository.put(new CommandKey("POST", "/ajax/record"), new LendBookCommand());
        ajaxCommandRepository.put(new CommandKey("PUT", "/ajax/oldStatus"),
                new UpdateStatusBorrowRecordCommand());
        ajaxCommandRepository.put(new CommandKey("GET", "/ajax/reader"), new GetReaderCommand());
        ajaxCommandRepository.put(new CommandKey("DELETE", "/ajax/books"), new DeleteBookCommand());
        ajaxCommandRepository.put(new CommandKey("POST", "/ajax/books"), new AddBookCommand());

    }

    public static CommandProvider getInstance() {
        if (instance == null) {
            synchronized (CommandProvider.class) {
                if (instance == null) {
                    instance = new CommandProvider();
                }
            }
        }
        return instance;
    }

    public Command getCommand(String method, String url) {
        Command command;
        if (method != null && url != null) {

            url = parseUrl(url);
            command = commandRepository.get(new CommandKey(method, url));
        } else {
            command = new UnknownCommand();
        }

        if (command == null) {
            command = new UnknownCommand();
        }
        return command;
    }

    public AjaxCommand getAjaxCommand(String method, String url) {
        AjaxCommand ajaxCommand;
        if (method != null && url != null) {

            url = parseUrl(url);
            ajaxCommand = ajaxCommandRepository.get(new CommandKey(method, url));
        } else {
            ajaxCommand = new AjaxUnknownCommand();
        }

        if (ajaxCommand == null) {
            ajaxCommand = new AjaxUnknownCommand();
        }
        return ajaxCommand;
    }

    private String parseUrl(String url) {
        String[] urlParts = url.split("/");
        if (urlParts.length > 0) {
            String s = urlParts[urlParts.length - 1];
            return s.matches("\\d+") ? "/books/" : url;
        } else {
            return url;
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class CommandKey {

        private String method;
        private String url;

    }


}
