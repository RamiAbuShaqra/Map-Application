package com.gmail.rami.abushaqra79.mapapplication;

import static com.gmail.rami.abushaqra79.mapapplication.Constants.ERROR_DIALOG_REQUEST;
import static com.gmail.rami.abushaqra79.mapapplication.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.gmail.rami.abushaqra79.mapapplication.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.gmail.rami.abushaqra79.mapapplication.model.ClusterMarker;
import com.gmail.rami.abushaqra79.mapapplication.model.Item;
import com.gmail.rami.abushaqra79.mapapplication.util.ClusterRenderer;
import com.gmail.rami.abushaqra79.mapapplication.view_model.MapViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.gmail.rami.abushaqra79.mapapplication.databinding.ActivityMapsBinding;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        ItemAdapter.ListItemClickListener {

    private ActivityMapsBinding binding;
    private List<Item> mItems;
    private ItemAdapter mItemAdapter;
    private MapViewModel mViewModel;
    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private static LatLngBounds mMapBounds;
    private ClusterManager mClusterManager;
    private ClusterRenderer mClusterRenderer;
    private final ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mItems = addItemsToList();

        mItemAdapter = new ItemAdapter(mItems, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mItemAdapter);

        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mViewModel.getItemList().observe(this, items -> {
            mItemAdapter.setItemList(items);
        });
    }

    private List<Item> addItemsToList() {
        List<Item> list = new ArrayList<>();

        list.add(new Item(1, "Person # 1", "This is first person",
                new LatLng(20.962906, 45.009431), R.drawable.person_one));
        list.add(new Item(2, "Person # 2", "This is second person",
                new LatLng(35.033200, 25.905755), R.drawable.person_two));
        list.add(new Item(3, "Person # 3", "This is third person",
                new LatLng(29.933014, 25.937007), R.drawable.person_three));
        list.add(new Item(4, "Person # 4", "This is fourth person",
                new LatLng(31.949005, 35.785801), R.drawable.person_four));
        list.add(new Item(5, "Person # 5", "This is fifth person",
                new LatLng(31.923263, 50.023870), R.drawable.person_five));

        return list;
    }

    private boolean checkMapServices() {
        if (isServicesOK()) {
            return isMapsEnabled();
        }
        return false;
    }

    private boolean isServicesOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (available == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
            if (dialog != null) {
                dialog.show();
            }
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }

        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PERMISSIONS_REQUEST_ENABLE_GPS) {
            if (!mLocationPermissionGranted) {
                getLocationPermission();
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMapBounds = new LatLngBounds(new LatLng(32.0, 35.7),
                new LatLng(32.0, 36.0));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBounds, 3, 3, 0));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(4.0f));

        showItemImagesOnMap();
    }

    private void showItemImagesOnMap() {
        if (mMap != null) {
            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<>(this, mMap);
            }
            if (mClusterRenderer == null) {
                mClusterRenderer =  new ClusterRenderer(this, mMap, mClusterManager);
            }
        }

        for (Item item : mItems) {
            ClusterMarker clusterMarker = new ClusterMarker(item.getPosition(), item.getTitle(),
                    item.getSnippet(), item.getImageResource());

            mClusterManager.addItem(clusterMarker);
            mClusterMarkers.add(clusterMarker);
        }

        mClusterManager.cluster();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Item item = mItems.get(clickedItemIndex);
        LatLng position = item.getPosition();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(position), 600, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkMapServices()) {
            if (!mLocationPermissionGranted) {
                getLocationPermission();
            }
        }
    }
}
