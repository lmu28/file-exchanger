package org.exchanger_bot.service.enums;

public enum LinkType {

    GET_PHOTO("get-photo"),
    GET_DOC("get-doc");


    private String link;

    LinkType(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }
}
