package com.example.appfond;

public class EpisodeChart {
    public String date, count, id_card;

    public void setDate(String date) {
        this.date = date;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setIdCard(String id_card) {
        this.id_card = id_card;
    }


    public EpisodeChart(String date, String count, String id_card) {

        this.date = date;
        this.count = count;
        this.id_card = id_card;    }



    public String getCount() {
        return count;
    }

    public String getDate() {
        return date;
    }

    public String getIdCard() {
        return id_card;
    }
}
