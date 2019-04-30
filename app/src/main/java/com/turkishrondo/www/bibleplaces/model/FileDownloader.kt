package com.turkishrondo.www.bibleplaces.model

import android.content.ContentValues
import android.os.AsyncTask
import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.util.*

//----------------------------------------------------------------------------------------------------------------------
// FileDownloader
//
// This class downloads a file from the Web Server, then saves it to the specified directory on the device.  This class
// extends Observable and notifies subscribers upon completion.
//----------------------------------------------------------------------------------------------------------------------
class FileDownloader(url: String, cacheDir: String, fileName: String): Observable()
{
    private val mUrl: String = url
    private val mCacheDir: String = cacheDir
    private val mFileName: String = fileName
    private val mWebServerAPI:API_WebServer
    private lateinit var mAsyncFileSave: AsyncFileSave

    init
    {
        // Create the connection to the Web Server.
        val retrofit = Retrofit.Builder().baseUrl(mUrl).build()
        mWebServerAPI = retrofit.create(API_WebServer::class.java)

        // Streaming Download: All Books KML Data
        val call = mWebServerAPI.downloadAllBooks()
        call.enqueue(object : Callback<ResponseBody>
        {
            override fun onResponse(call: retrofit2.Call<ResponseBody>, response: Response<ResponseBody>)
            {
                Log.d(ContentValues.TAG, "Connected to server.")
                Log.d(ContentValues.TAG, mFileName + ": Download succeess? " + response.isSuccessful)
                if (response.isSuccessful == true)
                {
                    // Save the file in the background.
                    mAsyncFileSave = AsyncFileSave(mCacheDir, mFileName)
                    mAsyncFileSave.execute(response.body())
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable)
            {
                Log.d(ContentValues.TAG, "Couldn't connect to server.  (Server offline?)")
            }
        })
    }

    //----------------------------------------------------------------------------------------------
    // INNER CLASS: AsyncFileSave
    //----------------------------------------------------------------------------------------------

    inner class AsyncFileSave(cacheDir: String, fileName: String) : AsyncTask<ResponseBody, Void, Boolean>()
    {
        val mCacheDir = cacheDir
        val mFileName = fileName

        /*override fun onPreExecute()
        {
            super.onPreExecute()
        }*/

        override fun doInBackground(vararg params: ResponseBody): Boolean
        {
            var success = false

            try
            {
                val futureStudioIconFile = File(mCacheDir + File.separator + mFileName)
                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null

                try
                {
                    val fileReader = ByteArray(4096)
                    val fileSize = params[0].contentLength()
                    var fileSizeDownloaded: Long = 0

                    inputStream = params[0].byteStream();
                    outputStream = FileOutputStream(futureStudioIconFile)

                    while (true)
                    {
                        val read = inputStream!!.read(fileReader)

                        if (read == -1)
                        {
                            break
                        }

                        outputStream.write(fileReader, 0, read)
                        fileSizeDownloaded += read.toLong()
                        Log.d(ContentValues.TAG, "file write: $fileSizeDownloaded of $fileSize")
                    }

                    outputStream.flush()

                    success = true
                }
                catch (e: IOException)
                {
                    success = false
                }
                finally
                {
                    if (inputStream != null)
                    {
                        inputStream.close()
                    }

                    if (outputStream != null)
                    {
                        outputStream.close()
                    }
                }
            }
            catch (e: IOException)
            {
                success = false
            }

            Log.d(ContentValues.TAG, mFileName + ": File write succeess? " + success)

            return success
        }

        /*override fun onProgressUpdate(vararg values: Void?)
        {
            super.onProgressUpdate(*values)
        }*/

        override fun onPostExecute(result: Boolean)
        {
            super.onPostExecute(result)
            setChanged()
            notifyObservers(result)
        }
    }
}