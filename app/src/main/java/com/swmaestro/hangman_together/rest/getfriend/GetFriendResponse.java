package com.swmaestro.hangman_together.rest.getfriend;

import java.util.List;

public class GetFriendResponse {
    String message;
    List<String> friendNickname;
    List<String> hasGivenCandy;

    public List<String> getFriendNickname() {
        return friendNickname;
    }

    public void setFriendNickname(List<String> friendNickname) {
        this.friendNickname = friendNickname;
    }

    public List<String> getHasGivenCandy() {
        return hasGivenCandy;
    }

    public void setHasGivenCandy(List<String> hasGivenCandy) {
        this.hasGivenCandy = hasGivenCandy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
