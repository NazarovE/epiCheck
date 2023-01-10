package com.example.appfond;

import java.sql.Date;

public class Episodes {

    public String date, comment, id_card, id_episode;

    public void setIdEpisode(String id_episode) {
        this.id_episode = id_episode;
    }

    public void setDateTime(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setIdCard(String id_card) {
        this.id_card = id_card;
    }


    public Episodes(String date, String comment, String id_card, String id_episode) {
        this.id_episode = id_episode;
        this.id_card = id_card;
        this.comment = comment;
        this.date = date;
    }

    public String getIdEpisode() {
        return id_episode;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getIdCard() {
        return id_card;
    }


}
