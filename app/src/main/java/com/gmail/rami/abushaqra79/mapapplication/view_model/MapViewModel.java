package com.gmail.rami.abushaqra79.mapapplication.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.rami.abushaqra79.mapapplication.model.Item;

import java.util.List;

public class MapViewModel extends ViewModel {

    private final LiveData<List<Item>> itemList = new MutableLiveData<>();

    public LiveData<List<Item>> getItemList() {
        return itemList;
    }
}
