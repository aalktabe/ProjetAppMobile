package com.example.app2;

public class Links {
    private String link;
    private String date;
    private int rate;

    public Links(String link, String date, int rate) {
        this.link = link;
        this.date = date;
        this.rate = rate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
