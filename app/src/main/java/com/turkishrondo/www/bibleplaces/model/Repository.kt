package com.turkishrondo.www.bibleplaces.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Context
import com.turkishrondo.www.bibleplaces.ALL_CHAPTERS_FILENAME
import com.turkishrondo.www.bibleplaces.BASE_URL
import com.turkishrondo.www.bibleplaces.R
import com.turkishrondo.www.bibleplaces.SingletonHolder
import com.turkishrondo.www.bibleplaces.model.data.BibleBookData
import com.turkishrondo.www.bibleplaces.model.data.UserData
import java.io.*
import java.util.*

//----------------------------------------------------------------------------------------------------------------------
// Repository
//
// This singleton class is the single access point for the ViewModel into the Model.  It provides access to application
// data, including downloaded data, which is observed with a FileDownloader.
//----------------------------------------------------------------------------------------------------------------------
class Repository private constructor(context: Context): Observer
{
    // This used to implement a singleton that requires a context argument.  Note that the context is NOT held on to,
    // and is only used for initialization purposes.
    companion object : SingletonHolder<Repository, Context>(::Repository)

    // Downloaded files are saved to the cache directory path.
    private val mCacheDir: String = context.cacheDir.path

    // User Data
    private val mUserData = UserData(context.resources.getString(R.string.app_name))

    // This is used to download the all-chapters.kml file from the server and provide it to the ViewModel in a
    // LiveData<File>.  FileDownloader will notify me when the download process is complete.
    private val mAllChaptersDownloadComplete = MutableLiveData<Boolean>()
    private val mAllChaptersFileDownloader = FileDownloader(BASE_URL, mCacheDir, ALL_CHAPTERS_FILENAME)
    private val mAllChaptersKML = MutableLiveData<File>()

    init
    {
        // All Chapters Download
        mAllChaptersFileDownloader.addObserver(this)
        mAllChaptersDownloadComplete.value = false
    }

    //----------------------------------------------------------------------------------------------
    // OUTPUT TO VIEW MODEL
    //----------------------------------------------------------------------------------------------

    // Downloads
    fun getAllChaptersKML(): LiveData<File>
    {
        // Determine if the download is newly complete.
        if ((mAllChaptersDownloadComplete.value == true) && (mAllChaptersKML.value == null))
        {
            val file = File(mCacheDir, ALL_CHAPTERS_FILENAME)
            if (file.exists() == true)
            {
                mAllChaptersKML.value = file
            }
        }
        return mAllChaptersKML
    }
    fun getDownloadComplete(): LiveData<Boolean> = mAllChaptersDownloadComplete

    // Bible Book Data
    fun getBibleBookItems(): List<BibleBookData.BookItem> = BibleBookData.ITEMS

    // User Data
    fun getChapterDataInitialized(): LiveData<Boolean> = mUserData.getChapterDataInitialized()
    fun getSelectedBookIndex(): LiveData<Int> = mUserData.getSelectedBookIndex()
    fun getSelectedBookTitle(): LiveData<String> = mUserData.getSelectedBookTitle()
    fun getSelectedBookAbbrev(): LiveData<String> = mUserData.getSelectedBookAbbrev()
    fun getSelectedChapterIndex(): LiveData<Int> = mUserData.getSelectedChapterIndex()
    fun getSelectedChapterTitle(): LiveData<String> = mUserData.getSelectedChapterTitle()

    //----------------------------------------------------------------------------------------------
    // INPUT FROM VIEW MODEL
    //----------------------------------------------------------------------------------------------

    // Bible Book Data
    // The hash parameter is like this: HashMap<Book Id, List of Chapter names>
    fun setBibleBookChapterData(hash: HashMap<Int, ArrayList<String>>)
    {
        // This is only initialized once per the lifecycle of the application.
        if (mUserData.getChapterDataInitialized().value == false)
        {
            BibleBookData.initializeChapterData(hash)
            mUserData.initializeChapterData()
        }
    }

    // User Data
    fun setSelectedBookIndex(i: Int) = mUserData.setSelectedBookIndex(i)
    fun setSelectedChapterIndex(i: Int) = mUserData.setSelectedChapterIndex(i)

    //----------------------------------------------------------------------------------------------
    // INTERFACE IMPLEMENTATION
    //----------------------------------------------------------------------------------------------

    // Get notified of FileDownloader completion.
    override fun update(o: Observable, arg: Any)
    {
        mAllChaptersDownloadComplete.value = arg as Boolean
    }
}