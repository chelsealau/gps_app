package com.example.basicgps;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class LocationListenerTest  {
    @Test
    public void testSomething2() {
        Assert.assertEquals(1 + 3, 4);
    }

    @Test
    public void testGPS() throws NoSuchMethodException {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(android.content.Context.LOCATION_SERVICE);
        locationManager.addTestProvider("Test", false, false, false, false,
                false, false, false, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
        locationManager.setTestProviderEnabled("Test", true);

        Location location = new Location("Test");
        location.setLatitude(10.0);
        location.setLongitude(20.0);
        location.setAccuracy(500);
        location.setTime(System.currentTimeMillis());

        java.lang.reflect.Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
        if (locationJellyBeanFixMethod != null) {
            try {
                locationJellyBeanFixMethod.invoke(location);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }



        locationManager.setTestProviderLocation("Test", location);

        // Check if your listener reacted the right way

        locationManager.removeTestProvider("Test");
    }

}