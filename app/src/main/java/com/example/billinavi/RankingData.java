package com.example.billinavi;

public class RankingData {
    private String ID, winRate;
    private int numId, changeId;

    public String getUserID() {
        return ID;
    }

    public void setID(String id) {
        this.ID = id;
    }

    public String getRate() {
        return winRate;
    }

    public void setRate(String rate) {
        this.winRate = rate;
    }

    public int getNumId() {
        return numId;
    }

    public void setNumId(int numId) {
        this.numId = numId;
    }

    public int getChangeId() {
        return changeId;
    }

    public void setChangeId(int changeId) {
        this.changeId = changeId;
    }

}
