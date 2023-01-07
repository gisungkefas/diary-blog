package com.kefas.diaryblog.model.role;

public enum Role {

    ADMIN("Admin"),
    USER("User");

    private String display;

    Role(String display) {
        this.display = display;
    }
}
