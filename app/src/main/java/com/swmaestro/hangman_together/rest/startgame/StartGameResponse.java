package com.swmaestro.hangman_together.rest.startgame;

public class StartGameResponse {
    String message;
    String gameWord;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGameWord() {
        return gameWord;
    }

    public void setGameWord(String gameWord) {
        this.gameWord = gameWord;
    }
}
