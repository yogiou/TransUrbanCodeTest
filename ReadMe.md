ReadMe


Support Android version: 
Android 7 - 11

Support devices: 
Phone only

Support features:
Will fetch the list of starship when launch the app.
Will fetch more items when scroll the recycler view to bottom. 
Click each item will go to the details page. 
Will pop up a snack message if no internet connection. 
Will not call the api to fetch if no internet connection. 
Will show error code snack message if api call failed.
Will show retry button to allow user to retry if api call failed and no items fetched.
Relaunch the app will also retry if no items fetched
User can check/uncheck the checkbox of heart to favourite or unfavourite the starship in list and details screen
Will allow the users to display the list with other sort order like name, model, manufacturer, but only show this spinner option 
if fetched the whole starship list. I believe that the backend return items were sorted by create time or edit time.

Requirements to build the app:
Android Studio Arctic Fox (2020.3.1 Patch 1) Stable. 
Java 1.8 above. 
Please do not use dark mode as it is not supported. 

Instructions to build and launch:
Clone the project from git, then waiting for gradle building successfully, then click Run 'app' button in Android studio. 

