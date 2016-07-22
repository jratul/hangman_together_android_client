package com.swmaestro.hangman_together.rest.home;

import java.util.List;

public class HomeResponse {
    String message;
    String myscore;
    String mycandy;
    List<String> rankNickname;
    List<String> rankScore;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMyscore() {
        return myscore;
    }

    public void setMyscore(String myscore) {
        this.myscore = myscore;
    }

    public String getMycandy() {
        return mycandy;
    }

    public void setMycandy(String mycandy) {
        this.mycandy = mycandy;
    }

    public List<String> getRankNickname() {
        return rankNickname;
    }

    public void setRankNickname(List<String> rankNickname) {
        this.rankNickname = rankNickname;
    }

    public List<String> getRankScore() {
        return rankScore;
    }

    public void setRankScore(List<String> rankScore) {
        this.rankScore = rankScore;
    }
}
