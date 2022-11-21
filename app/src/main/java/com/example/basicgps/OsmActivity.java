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
//    protected LocationManager locationManager;
    private static final String TAG = "OsmActivity";
//    private double m_latitude = 37.390488;
//    private double m_longitude = -122.06385;
//    private GeoPoint start = new GeoPoint(m_latitude, m_longitude);
//    private ArrayList<MyLocationNewOverlay> mOverlayItemArray = new ArrayList<MyLocationNewOverlay>();
//    MyLocationNewOverlay mLocationOverlay;

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
//            m_latitude = extras.getDouble("Latitude");
//            m_longitude = extras.getDouble("Longitude");
            this.m_geopoints = extras.getParcelableArrayList("Geopoints");
        }


        myOpenMapView = (MapView) findViewById(R.id.map);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setBuiltInZoomControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(12);
        GeoPoint start = m_geopoints.get(0);
        myOpenMapView.setExpectedCenter(start);


        Polyline line = new Polyline();   //see note below!
        line.setPoints(m_geopoints);
        Color color = new Color();
//        line.getOutlinePaint().setColor((int)color.green());
        myOpenMapView.getOverlays().add(line);

//        for (int i=0; i < 5; i++) {
//            Marker startMarker = new Marker(myOpenMapView);
//            startMarker.setPosition(m_geopoints.get(i));
//            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            myOpenMapView.getOverlays().add(startMarker);
//        }
        myOpenMapView.invalidate();

//        GeoPoint point = new GeoPoint(m_latitude, m_longitude);
//        Marker startMarker = new Marker(myOpenMapView);
//        startMarker.setPosition(point);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        myOpenMapView.getOverlays().add(startMarker);
//        myOpenMapView.invalidate();
//        GeoPoint newPoint = new GeoPoint(42.350876, -71.106918);
//        Marker startMarker2 = new Marker(myOpenMapView);
//        startMarker2.setPosition(newPoint);
//        myOpenMapView.getOverlays().add(startMarker2);
//        Log.d(TAG, "overlays: " + myOpenMapView.getOverlays().toString());


//        @Override
//        protected void onResume() {
//            // TODO Auto-generated method stub
//            super.onResume();
//            myOpenMapView.onResume();
//        }

//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                List<GeoPoint> geoPoints = new ArrayList<>();
//
//                //// get all values from database
//                List<Metric> metrics = GPSDatabase.getInstance(getApplicationContext()).metricDAO().getAllMetrics();
//                List<Double> long_list = metrics.stream().map(Metric::getLongitude).collect(Collectors.toList());
//                List<Double> lat_list = metrics.stream().map(Metric::getLatitude).collect(Collectors.toList());
//
//                //// add geopoints to array using fetched long and lat values
//
////                geoPoints.add(start);
////                geoPoints.add(new GeoPoint(39.6920d, -119.2851d));
//                for (int i=0; i < 15; i++) {
//                    geoPoints.add(new GeoPoint(long_list.get(i), lat_list.get(i)));
//                }
//
//                GeoPoint startPoint = new GeoPoint(39.6920d, -119.2851d);
//                Marker startMarker = new Marker(myOpenMapView);
//                startMarker.setPosition(startPoint);
//                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
////                myOpenMapView.getOverlays().add(startMarker);
//
////                String pointsString = geoPoints.toString();
////                Log.d(TAG, "GEOPOINTS LIST: " + pointsString);
////                // create polyline using values
////                Polyline line = new Polyline();   //see note below!
////                line.setPoints(geoPoints);
////                String linePoints = line.getPoints().toString();
////                Log.d(TAG, "GEOPOINTS LIST FROM LINE: " + linePoints);
////                line.getOutlinePaint().setStrokeWidth(25f);
////                Color color = new Color();
////                line.getOutlinePaint().setColor((int)color.blue());
////                Boolean vis = line.isVisible();
////                Log.d(TAG, "VISIBILITY CHECK: " + vis);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        myOpenMapView.getOverlays().add(line);
////                        myOpenMapView.getOverlays().add(startMarker);
//                        for (int i=0; i < geoPoints.size(); i++) {
////                            geoPoints.add(new GeoPoint(long_list.get(i), lat_list.get(i)));
//                            Marker startMarker = new Marker(myOpenMapView);
//                            startMarker.setPosition(geoPoints.get(i));
//                            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//                            myOpenMapView.getOverlays().add(new Marker(myOpenMapView));
//                            myOpenMapView.invalidate();
//                        }
//
//                    }
//                });
//            }
//        });
    }


//        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),myOpenMapView);
//        mLocationOverlay.enableMyLocation();
//        mOverlayItemArray.add(mLocationOverlay);
//        String arrayString = mOverlayItemArray.toString();
//        Log.d(TAG, "onCreate: " + arrayString);
//
//        for (MyLocationNewOverlay location : mOverlayItemArray) {
//            myOpenMapView.getOverlays().add(location);
//        }

        //--- Create Overlay
//        overlayItemArray = new ArrayList<OverlayItem>();
//        overlayItemArray.add(new OverlayItem("TRAVEL MAP", "Your travelled locations are marked on the map", new GeoPoint(m_latitude, m_longitude)));
//
//        List<GeoPoint> geoPoints = new ArrayList<>();
//        //add your points here
//        geoPoints.add(new GeoPoint(m_latitude, m_longitude));
//        Polyline line = new Polyline();   //see note below!
//        line.setPoints(geoPoints);
////        line.setOnClickListener(new Polyline.OnClickListener() {
////            @Override
////            public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
////                Toast.makeText(mapView.getContext(), "polyline with " + polyline.getPoints().size() + "pts was tapped", Toast.LENGTH_LONG).show();
////                return false;
////            }
////        });
//        myOpenMapView.getOverlayManager().add(line);


//        DefaultResourceProxyImpl defaultResourceProxyImpl
//                = new DefaultResourceProxyImpl(this);
//        MyItemizedIconOverlay myItemizedIconOverlay
//                = new MyItemizedIconOverlay(
//                overlayItemArray, null, defaultResourceProxyImpl);
//        myOpenMapView.getOverlays().add(myItemizedIconOverlay);
        //---


//        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //for demo, getLastKnownLocation from GPS only, not from NETWORK
//        Location lastLocation
//                = locationManager.getLastKnownLocation(
//                LocationManager.GPS_PROVIDER);
//        if(lastLocation != null){
//            updateLoc(lastLocation);
//        }

        //Add Scale Bar
//        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(this.myOpenMapView);
//        myOpenMapView.getOverlays().add(myScaleBarOverlay);
//    }


//    }
}