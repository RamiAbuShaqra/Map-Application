package com.gmail.rami.abushaqra79.mapapplication.model;

import com.google.android.gms.maps.model.LatLng;

public class Item {

    private final int id;
    private final String title;
    private final String snippet;
    private final LatLng position;
    private final int imageResource;

    public Item (int id, String title, String snippet, LatLng position, int imageResource) {
        this.id = id;
        this.title = title;
        this.snippet = snippet;
        this.position = position;
        this.imageResource = imageResource;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public LatLng getPosition() {
        return position;
    }

    public int getImageResource() {
        return imageResource;
    }
}
