package com.gmail.rami.abushaqra79.mapapplication.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {

    private final LatLng position;
    private final String title;
    private final String snippet;
    private final int itemImage;

    public ClusterMarker(LatLng position, String title, String snippet, int itemImage) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.itemImage = itemImage;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }

    public int getItemImage() {
        return itemImage;
    }

    @Nullable
    @Override
    public Float getZIndex() {
        return null;
    }
}
