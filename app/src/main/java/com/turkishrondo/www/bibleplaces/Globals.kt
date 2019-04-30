package com.turkishrondo.www.bibleplaces

//----------------------------------------------------------------------------------------------------------------------
// Globals
//
// This file contains project-level constants.
//----------------------------------------------------------------------------------------------------------------------

// This is the URL:port of the web server.  If you're running the webserver on a Windows PC on your local area network:
// 1. Find it's IPv4 address on the network by opening a command prompt and typing "ipconfig".
// 2. You may also have to create an "Outbound Rule" for your firewall.  To do so, navigate to Windows Security ->
//    Firewall -> Advanced Settings, then create an Outbound Rule with a TCP protocol on port 5001.
const val BASE_URL = "http://192.168.1.252:5001/"

// Names of files download from the server.
const val ALL_CHAPTERS_FILENAME = "all-chapters.kml"