# BiblePlaces Overview
BiblePlaces is a simple Android application that displays the locations cited throughout the Bible.  Locations are displayed as markers on a Google map and are grouped by each chapter of each book of the Bible.  The app is intended to be used as a visual aid while reading the Bible.

<img src="https://github.com/TurkishRondo/bible-places-android/blob/master/screenshots/BookList.png" alt="drawing" width="200"/> <img src="https://github.com/TurkishRondo/bible-places-android/blob/master/screenshots/BookDetail.png" alt="drawing" width="200"/>

BiblePlaces was created as a means to gain exposure to:
- Developing an Android application with Kotlin and featuring File Downloads, Data-Binding, and the Android Jetpack/Architecture Components to help implement an MVVM architecture.
- Developing a file server using ASP.NET Core.
- GitHub.

The Android implementation is located in the current repository, and the file server implementation is located in [this](https://github.com/TurkishRondo/bible-places-server) repository.

# Implementation
The ASP.NET Core file server hosts the .kml file (Google Earth data) needed by the Android app, which contains the information to display the biblical locations of each chapter of each book (given any known locations are cited).  At startup, the Android application downloads the .kml file from the server using functionality from the Retrofit2 library, and displaying a load screen until the download is complete and the file is saved.  The books of the Bible are displayed and selected via a RecyclerView on one activity, and when selected, a new Activity containing a Map View is displayed with the loaded .kml data.

In order to access Google Map information, the project must have the Google Play services SDK installed and there must be a valid "API key" for the application.  If you are running this application or creating one for yourself, follow the instructions to Google's [Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk/start) guide.  Obtaining the API key is Step 4.

Further, the library version of the play-services-map specified in the Gradle file seems to matter.  As of April 29, 2019, the latest version results in a "<Your App> is having trouble with Google Play services.  Please try again." error when running the app.  Version 16.0.0, however, does work for this project.  See the gradle file [here](https://github.com/TurkishRondo/bible-places-android/blob/master/app/build.gradle).

# Running the ASP.NET Core File Server Locally
The file server was designed to be run on a local network, and in order to access the server, the platform's firewall must allow access.  On a Windows platform, the URL:port specified [here](https://github.com/TurkishRondo/bible-places-server/blob/master/BiblePlaces/Program.cs) and [here](https://github.com/TurkishRondo/bible-places-android/blob/master/app/src/main/java/com/truthbetolddesigns/www/bibleplaces/Globals.kt) can be opened by:

1. Open a command prompt and type "ipconfig".  The URL you should use is the IPv4 address shown.
2. Navigate to Windows Security -> Firewall -> Advanced Settings, then create an Outbound Rule with a TCP protocol on whichever port you'd like to use.  (I chose 5001.)

# Credits
Special thanks to [OpenBible.info](https://www.openbible.info/geo/) for the .kml data.
