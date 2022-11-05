# HW2 PROBLEM 1

## TEAM
- Cameron Cipriano
- Jae Yoon Chung
- Arnaud Harmange 
- Chelsea Lau

## SOLUTION
 
 Our app has the following features:
 - Latitude reading of current location
 - Longitude reading of current location
 - Current travelling speed
 - Unit toggling
 - Font toggling
 - Indicative colors for speed value
 - Test mode

 Upon first use, the app should ask for user permission to access location. If location is granted, the app will use the Location Manager class to obtain a Location object which contains information such as latitude, longitude, and speed at set intervals of change in time (ms) and distance (m). The TextViews for the read values are updated accordingly.

 There is a Metric switch that can toggle the speed units from mph to km/h, a switch to increase the font size of the speed reading, and a switch to pause the display. The speed value changes color depending on what range the number falls into. Finally, there is a button that when pressed can provide information on how to use the app. 

 There is also a switch that triggers a testing mode that is further described in the TESTING.md. 

 ## INDIVIDUAL ELEMENTS
- Add a pause button that allows the user to pause the display. (Arnaud)
- Change the color of the speed display according to the speed being traveled. (Arnaud)
- Allow the user to change the units in which the speed is being displayed. (Chelsea)
- Allow the user to change the size of the font being used to display the speed. (Jae Yoon)
- Add a help button that, when pressed, displays helpful information on how to use the app. (Cameron)
