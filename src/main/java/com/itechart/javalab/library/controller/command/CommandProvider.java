package com.itechart.javalab.library.controller.command;

import com.itechart.javalab.library.controller.command.impl.AddBookCommand;
import com.itechart.javalab.library.controller.command.impl.EditBookCommand;
import com.itechart.javalab.library.controller.command.impl.GetBooksCommand;
import com.itechart.javalab.library.controller.command.impl.UnknownCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandProvider {

    private static volatile CommandProvider instance;
    private Map<CommandKey, Command> commandRepository = new HashMap<>();

    private CommandProvider() {
        commandRepository.put(new CommandKey("GET", "/books"), new GetBooksCommand());
        commandRepository.put(new CommandKey("PUT", "/books"), new AddBookCommand());
        commandRepository.put(new CommandKey("POST", "/books"), new EditBookCommand());
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
            command = commandRepository.get(new CommandKey(method, url));
        } else {
            command = new UnknownCommand();
        }

        if (command == null) {
            command = new UnknownCommand();
        }

        return command;
    }


    class CommandKey {

        private String method;
        private String url;

        CommandKey(String method, String url) {
            this.method = method;
            this.url = url;
        }


        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CommandKey that = (CommandKey) o;
            return Objects.equals(method, that.method) &&
                    Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, url);
        }


    }


}
