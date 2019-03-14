package com.example.monster.airgesture.phase;


public enum Gesture {
    HENG("—"),
    SHU("|"),
    ZUO_XIE("/"),
    YOU_XIE("\\"),
    ZUO_HU("⊂"),
    YOU_HU("⊃");

    private String mStoke;

    Gesture(String stoke) {
        this.mStoke = stoke;
    }

    public String getStoke() {
        return mStoke;
    }
}
