package com.example.appfond;

public class GlobalSettings {
    public String name;
    public String value;

    public void setName(String name) {
            this.name = name;
        }
    public void setValue(String value) {
        this.value = value;
    }

    public GlobalSettings(String name, String value) {
            this.name = name;this.value = value;
        }

    public String getName() {
            return name;
        }
    public String getValue() {
        return value;
    }


}
