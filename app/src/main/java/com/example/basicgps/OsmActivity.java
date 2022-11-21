package com.example.basicgps;

import java.util.ArrayList;
import java.util.List;

//import org.osmdroid.DefaultResourceProxyImpl;
//import org.osmdroid.ResourceProxy;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
//import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class OsmActivity extends AppCompatActivity {

    private MapView myOpenMapView;
    private MapController myMapController;
//    protected LocationManager locationManager;
    private static final String TAG = "OsmActivity";
    private double m_latitude = 42.3505d;
    private double m_longitude = -71.1054d;
    private GeoPoint start = new GeoPoint(m_latitude, m_longitude);
    private ArrayList<MyLocationNewOverlay> mOverlayItemArray = new ArrayList<MyLocationNewOverlay>();
    MyLocationNewOverlay mLocationOverlay;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.access_map);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            m_latitude = extras.getDouble("Latitude");
            m_longitude = extras.getDouble("Longitude");
        }


        myOpenMapView = (MapView)findViewById(R.id.map);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setBuiltInZoomControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(12);
        myOpenMapView.setExpectedCenter(start);


        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),myOpenMapView);
        mLocationOverlay.enableMyLocation();
        mOverlayItemArray.add(mLocationOverlay);
        String arrayString = mOverlayItemArray.toString();
        Log.d(TAG, "onCreate: " + arrayString);

        for (MyLocationNewOverlay location : mOverlayItemArray) {
            myOpenMapView.getOverlays().add(location);
        }

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
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        myOpenMapView.onResume();
    }

}