package org.exchanger_bot.service.enums;

public enum UserCommand {

    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start"),
    RETRY_EMAIL("/retry_mail");

    private final String cmd;

    UserCommand(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }


    @Override
    public String toString() {
        return cmd;
    }


    public boolean equals(String userCommand){
        return this.toString().equals(userCommand);
    }

}
