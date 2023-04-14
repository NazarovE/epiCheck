package com.example.appfond;

public class Teraphy {

    public String id_ter, name_ter, country_ter, doz_ter, date_begin, date_end, active_ter, id_card;

    public Teraphy() {

    }

    public void setId_ter(String id_ter) {
        this.id_ter = id_ter;
    }

    public void setName_ter(String name_ter) {
        this.name_ter = name_ter;
    }

    public void setCountry_ter(String country_ter) {
        this.country_ter = country_ter;
    }

    public void setDoz_ter(String doz_ter) { this.doz_ter = doz_ter;  }

    public void setDate_begin(String date_begin) {
        this.date_begin = date_begin;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public void setActive_ter(String active_ter) {
        this.active_ter = active_ter;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public Teraphy(String id_ter, String name_ter, String country_ter, String doz_ter, String date_begin,
                 String date_end, String active_ter, String id_card) {
        this.id_ter = id_ter;
        this.name_ter = name_ter;
        this.country_ter = country_ter;
        this.doz_ter = doz_ter;
        this.date_begin = date_begin;
        this.date_end = date_end;
        this.active_ter = active_ter;
        this.id_card = id_card;
    }

    public String getId_ter() {
        return id_ter;
    }

    public String getName_ter() {
        return name_ter;
    }

    public String getCountry_ter() {
        return country_ter;
    }

    public String getDoz_ter() {
        return doz_ter;
    }

    public String getDate_begin() {
        return date_begin;
    }

    public String getDate_end() {
        return date_end;
    }

    public String getActive_ter() {
        return active_ter;
    }

    public String getId_card() {
        return id_card;
    }


}
