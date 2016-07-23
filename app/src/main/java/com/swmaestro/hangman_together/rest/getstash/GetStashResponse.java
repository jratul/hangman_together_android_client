package com.swmaestro.hangman_together.rest.getstash;

import java.util.List;

public class GetStashResponse {
    String message;
    List<String> nickname;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getNickname() {
        return nickname;
    }

    public void setNickname(List<String> nickname) {
        this.nickname = nickname;
    }
}
