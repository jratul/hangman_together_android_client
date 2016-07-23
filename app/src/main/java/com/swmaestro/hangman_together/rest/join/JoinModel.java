package com.swmaestro.hangman_together.rest.join;

public class JoinModel {
    String phoneNum;
    String nickname;
    String lastConnectTime;
    String instanceId;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLastConnectTime() {
        return lastConnectTime;
    }

    public void setLastConnectTime(String lastConnectTime) {
        this.lastConnectTime = lastConnectTime;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
