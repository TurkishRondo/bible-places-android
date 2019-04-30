package com.turkishrondo.www.bibleplaces.model.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//----------------------------------------------------------------------------------------------------------------------
// UserData
//
// This class contains user data that is preserved throughout the lifecycle of the application.
//----------------------------------------------------------------------------------------------------------------------
class UserData(unselectedTitle: String)
{
    companion object
    {
        const val UNSELECTED_BOOK_INDEX = -1
        const val UNSELECTED_CHAPTER_INDEX = -1
        const val DEFAULT_CHAPTER_INDEX = -1
        const val BLANK_TITLE = ""
    }

    private val mUnselectedTitle = unselectedTitle
    private val mSelectedBookIndex = MutableLiveData<Int>()
    private val mSelectedBookTitle = MutableLiveData<String>()
    private val mSelectedBookAbbrev = MutableLiveData<String>()
    private val mSelectedChapterIndex = MutableLiveData<Int>()
    private val mSelectedChapterTitle = MutableLiveData<String>()
    private val mChapterDataInitialized = MutableLiveData<Boolean>()

    init
    {
        mChapterDataInitialized.value = false
        deselectBook()
    }

    private fun deselectBook()
    {
        mSelectedBookIndex.value = UNSELECTED_BOOK_INDEX
        mSelectedBookTitle.value = mUnselectedTitle
        mSelectedBookAbbrev.value = BLANK_TITLE
        mSelectedChapterIndex.value = UNSELECTED_CHAPTER_INDEX
        mSelectedChapterTitle.value = BLANK_TITLE
    }

    // Selected Book Index
    fun getSelectedBookIndex(): LiveData<Int> = mSelectedBookIndex
    fun setSelectedBookIndex(i: Int)
    {
        // Deselect the book if the input is invalid.
        if ((i < 0) || (i >= BibleBookData.ITEMS.size))
        {
            deselectBook()
        }

        // The input is valid.
        else
        {
            mSelectedBookIndex.value = i
            mSelectedBookTitle.value = BibleBookData.ITEMS[i].mTitle
            mSelectedBookAbbrev.value = BibleBookData.ITEMS[i].mAbbrev
        }
    }

    // Selected Book Title
    fun getSelectedBookTitle(): LiveData<String> = mSelectedBookTitle

    // Selected Book Abbreviation
    fun getSelectedBookAbbrev(): LiveData<String> = mSelectedBookAbbrev

    // Selected Chapter Index
    fun getSelectedChapterIndex(): LiveData<Int> = mSelectedChapterIndex
    fun setSelectedChapterIndex(i: Int)
    {
        if ((mChapterDataInitialized.value == true) && (mSelectedBookIndex.value != UNSELECTED_BOOK_INDEX))
        {
            var index = i

            // Bound invalid input.
            if (i < 0)
            {
                index = 0
            }
            else if (i >= BibleBookData.ITEMS[mSelectedBookIndex.value!!].mChapters!!.size)
            {
                index = BibleBookData.ITEMS[mSelectedBookIndex.value!!].mChapters!!.size - 1
            }

            mSelectedChapterIndex.value = index
            mSelectedChapterTitle.value = BibleBookData.ITEMS[mSelectedBookIndex.value!!].mChapters!![index]
        }
    }

    // Selected Chapter Title
    fun getSelectedChapterTitle(): LiveData<String> = mSelectedChapterTitle

    // Is Chapter Data Initialized
    fun getChapterDataInitialized(): LiveData<Boolean> = mChapterDataInitialized
    fun initializeChapterData()
    {
        mChapterDataInitialized.value = true
        setSelectedChapterIndex(DEFAULT_CHAPTER_INDEX)
    }
}