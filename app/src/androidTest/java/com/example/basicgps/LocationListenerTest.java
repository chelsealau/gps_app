package com.example.basicgps;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;

@RunWith(AndroidJUnit4.class)
public class LocationListenerTest  {
//    @Test
//    public void testSomething2() {
//        Assert.assertEquals(1 + 3, 4);
//    }

    @Rule
    public ActivityScenarioRule<Group3> activityRule = new ActivityScenarioRule<>(Group3.class);

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
        onView(withId(R.id.tv_lat)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_lon)).check(matches(isDisplayed()));

        locationManager.removeTestProvider("Test");
    }

    @Test
    public void resetResetsAllValues() {
        onView(withId(R.id.reset_button)).perform(click());
        onView(withId(R.id.tv_lat)).check(matches(withText("0.0")));
        onView(withId(R.id.tv_lon)).check(matches(withText("0.0")));
        onView(withId(R.id.tv_speed)).check(matches(withText("0.0")));
        onView(withId(R.id.tv_alt)).check(matches(withText("0.0")));
        onView(withId(R.id.tv_distance)).check(matches(withText("0.0")));
    }

//    @Test
    public void unitTimeChanges() {
        // seconds
        onView(withId(R.id.chbx_seconds)).perform(click());
        onView(withId(R.id.tv_time)).check(matches(withSubstring("seconds")));

        // minutes
        onView(withId(R.id.chkbx_minutes)).perform(click());
        onView(withId(R.id.tv_time)).check(matches(withSubstring("minutes")));

        // hours
        onView(withId(R.id.chkbx_hours)).perform(click());
        onView(withId(R.id.tv_time)).check(matches(withSubstring("hours")));

        // days
        onView(withId(R.id.chkbx_days)).perform(click());
        onView(withId(R.id.tv_time)).check(matches(withSubstring("days")));
    }

//    @Test
    public void unitSpeedChanges() {
        // start the test
        onView(withId(R.id.sw_test)).perform(click());
        // meters per second
        onView(withId(R.id.chkbx_meterPerSec)).perform(click());
        onView(withId(R.id.tv_speed)).check(matches(withSubstring("m/sec")));
        // kilometers per hour
        onView(withId(R.id.chkbx_kmh)).perform(click());
        onView(withId(R.id.tv_speed)).check(matches(withSubstring("km/h")));
        // miles per hour
        onView(withId(R.id.chkbx_mph)).perform(click());
        onView(withId(R.id.tv_speed)).check(matches(withSubstring("mph")));
        // minutes per mile
        onView(withId(R.id.chkbx_minPermile)).perform(click());
        onView(withId(R.id.tv_speed)).check(matches(withSubstring("min/mile")));
    }

}