package com.example.appfond;

import java.sql.Date;

    public class Cards {
        public String name_card,date_create,birthday,name_diagnosis,card_comment,id_card,id_user,
                id_diagnosis,name_diag,count_episode_today;

        public Cards() {

        }

        public void setName_card(String name_card) {
            this.name_card = name_card;
        }

        public void setDate_create(String date_create) {
            this.date_create = date_create;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setName_diagnosis(String name_diagnosis) {
            this.name_diagnosis = name_diagnosis;
        }

        public void setCard_comment(String card_comment) {
            this.card_comment = card_comment;
        }

        public void setId_card(String id_card) {
            this.id_card = id_card;
        }

        public void setId_user(String id_user) {
            this.id_user = id_user;
        }

        public void setId_diagnosis(String id_diagnosis) {
            this.id_diagnosis = id_diagnosis;
        }

        public void setName_diag(String name_diag) {
            this.name_diag = name_diag;
        }

        public void setCnt_episode_today(String count_episode_today) {
            this.count_episode_today = count_episode_today;
        }

        public Cards(String name_card, String date_create, String birthday, String name_diagnosis, String card_comment,
                     String id_card, String id_user, String id_diagnosis, String name_diag, String count_episode_today) {
            this.name_card = name_card;
            this.date_create = date_create;
            this.birthday = birthday;
            this.name_diagnosis = name_diagnosis;
            this.card_comment = card_comment;
            this.id_card = id_card;
            this.id_user = id_user;
            this.id_diagnosis = id_diagnosis;
            this.name_diag = name_diag;
            this.count_episode_today = count_episode_today;
        }

        public String getName_card() {
            return name_card;
        }

        public String getDate_create() {
            return date_create;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getName_diagnosis() {
            return name_diagnosis;
        }

        public String getCard_comment() {
            return card_comment;
        }

        public String getId_card() {
            return id_card;
        }

        public String getId_user() {
            return id_user;
        }

        public String getId_diagnosis() {
            return id_diagnosis;
        }

        public String getName_diag() {
            return name_diag;
        }

        public String getCnt_episode_today() {
            return count_episode_today;
        }

    }
