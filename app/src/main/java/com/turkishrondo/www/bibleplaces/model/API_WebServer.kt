package com.turkishrondo.www.bibleplaces.model

import com.turkishrondo.www.bibleplaces.ALL_CHAPTERS_FILENAME
import com.turkishrondo.www.bibleplaces.BASE_URL
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Streaming

//----------------------------------------------------------------------------------------------------------------------
// API_WebServer
//
// This interface makes use of the Retrofit2 library and is used to download files from the web server.
//----------------------------------------------------------------------------------------------------------------------
interface API_WebServer
{
    // This KML file is over 1MB and could potentially get bigger, so we'll stream it.
    @Streaming
    @GET(BASE_URL + ALL_CHAPTERS_FILENAME)
    // Use ResponseBody so Retrofit2 will *not* attempt to parse and convert the file into some other data.
    fun downloadAllBooks(): Call<ResponseBody>
}