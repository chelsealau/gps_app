# HW2 PROBLEM 0

## TEAM
- Cameron Cipriano
- Jae Yoon Chung
- Arnaud Harmange 
- Chelsea Lau

## SOLUTION
 
 Our app has the following features:
**Problem Zero:**
 - Latitude reading of current location
 - Longitude reading of current location
 - Current travelling speed
 - Unit toggling - change between Km/h and mph 
 - Font toggling - change size of current speed indicator font
 - Indicative colors for speed value
 - Test mode
 - Pause button that allows the user to pause all activity on the display

**New for Problem One:**
 - Reset button- resets all accumulated metrics to zero
 - Height of phone (current altitude of the device)
 - Distance traveled by the device since the last reset or app restart
 - Time since app start (or last reset or app restart)
 - Total moving time (total time device has been in motion since last reset or app restart)
 - indicators (arrows) showing if a value is increasing or decreasing, in real time (displays magnitude of change to the right of the indicator)
 - Unit toggling
   - Time Units: Seconds, Minutes, Hours, Days
   - Distance and Altitude Units: Meters, Kilometers, Miles, Feet
   - Speed Units: Meters per second, Kilometers per hour, Miles per hour
 - Uses an independent thread to fetch location values before updating UI fields on UI(main) thread
 - A high scores page that is visible after resetting the app and then tapping the "check high scores" button
   - Displays maximum Speed achieved, the Max distance traveled, and the Maximum time for which the app was left running

**New for Problem Two:**
-Accessible map implemented using OpenStreetMap that shows the user's travels (NOTE: Path is generated from all location points collected BEFORE pressing the _MAP_ button)


 Upon first use, the app should ask for user permission to access location. If location is granted, the app will use the Location Manager class to obtain a Location object which contains information such as latitude, longitude, and speed at set intervals of change in time (ms) and distance (m). The TextViews for the read values are updated accordingly.
 There is also a switch that triggers a testing mode that is further described in the TESTING.md. 

Once the app starts, the time Elapsed timer begins, showing a real time metric for how long the app has been running. This is reset to zero every time the app is restarted or the reset button is hit.
The Latitude and longitude are initialized with values as soon as the GPS receives location data. Once the device begins to move, the Latitude and Longitude will change, and next to them, arrows will appear,
indicating if those values are increasing (up arrow) or decreasing (down arrow). the magnitude of the change indicated by the arrow is displayed to the right of the arrow. The speed metric changes in real time,
and changes color as speed changes**.The Altitude metric -assuming the phone hardware supports it - will display current altitude of the phone. Note that the emulator does not provide altitude data, but says it 
can provide data, so this will always display zero altitude in the emulator. The distance traveled metric calculates how far the phone has traveled since the phone started moving. The total moving time is 
how much time the phone has been moving for. this pauses any time the phone stops moving, and picks back up once the phone starts moving again. Note that all of these metrics have units
that can be selected by the list of unit options below the displayed metrics. 
The map is a world map that plots a polynomial line defined by each different latitude and longitude the user has visited. The line will only reflect the points visited before the button was clicked, so if the user wants to add new points they must return to the home page and travel more before regenerating the map. To access the historical averages page, the user can press the _AVERAGE_ button. Instantaneous acceleration provides a value for the change in speed at that exact moment. _EMAIL DATA_ will create a PDF attachment containing average values for the provided metrics and send to the provided email address.

**'**SPEED COLOR HAS BEEN MODIFIED IN PROBLEM 2 TO REFLECT WHETHER THE VALUE IS GREATER OR LESS THAN HISTORICAL AVERAGE** 

 ## INDIVIDUAL ELEMENTS (Problem Zero)
- Add a pause button that allows the user to pause the display. (Arnaud)
- Change the color of the speed display according to the speed being traveled. (Arnaud)
- Allow the user to change the units in which the speed is being displayed. (Chelsea)
- Allow the user to change the size of the font being used to display the speed. (Jae Yoon)
- Add a help button that, when pressed, displays helpful information on how to use the app. (Cameron)

## INDIVIDUAL ELEMENTS (Problem One)
- Reset button - Cameron Cipriano
- Qualitative Indicators - Chelsea Lau
- High Scores Page - Jae Yoon Chung
- Total Moving Time - Arnaud Harmange

## INDIVIDUAL ELEMENTS (Problem Two)
- Speed colors according to historical average - Cameron Cipriano
- Instantaneous acceleration - Chelsea Lau
- Historical averages page - Jae Yoon Chung
- Export feature - Arnaud Harmange
