package com.example.basicgps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basicgps.database.GPSDatabase;
import com.example.basicgps.database.entities.Metric;

public class OsmActivity extends AppCompatActivity {

    private MapView myOpenMapView;
    private MapController myMapController;

    private static final String TAG = "OsmActivity";

    private ArrayList<GeoPoint> m_geopoints;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.access_map);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.m_geopoints = extras.getParcelableArrayList("Geopoints");
        }


        myOpenMapView = (MapView) findViewById(R.id.map);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setBuiltInZoomControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(12);
        GeoPoint start = m_geopoints.get(0);
        myOpenMapView.setExpectedCenter(start);


        Polyline line = new Polyline();
        line.setPoints(m_geopoints);
        myOpenMapView.getOverlays().add(line);
        myOpenMapView.invalidate();

    }

}