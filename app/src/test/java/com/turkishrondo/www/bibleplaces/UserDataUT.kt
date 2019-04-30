package com.turkishrondo.www.bibleplaces

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.turkishrondo.www.bibleplaces.model.data.BibleBookData
import com.turkishrondo.www.bibleplaces.model.data.UserData
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule

//----------------------------------------------------------------------------------------------------------------------
// UserDataUT
//
// This unit test class tests UserData in the Model.  Dependent classes/objects that have their own tests may be used as
// part of these tests instead of mocking them.
//----------------------------------------------------------------------------------------------------------------------
class UserDataUT
{
    @Rule
    @JvmField
    val mRule = InstantTaskExecutorRule()

    // SELECTED BOOK
    @Test
    fun selectedBook_validSelection_returnsValid()
    {
        val userData = UserData("Application Name")

        for (i in BibleBookData.ITEMS.indices)
        {
            userData.setSelectedBookIndex(i)
            assertEquals(i, userData.getSelectedBookIndex().value)
            assertEquals(BibleBookData.ITEMS[i].mTitle, userData.getSelectedBookTitle().value)
            assertEquals(BibleBookData.ITEMS[i].mAbbrev, userData.getSelectedBookAbbrev().value)
        }
    }
    @Test
    fun selectedBook_invalidSelection_returnsDefault()
    {
        val userData = UserData("Application Name")

        userData.setSelectedBookIndex(-99999)
        assertEquals(UserData.UNSELECTED_BOOK_INDEX, userData.getSelectedBookIndex().value)
        assertEquals("Application Name", userData.getSelectedBookTitle().value)
        assertEquals(UserData.BLANK_TITLE, userData.getSelectedBookAbbrev().value)
        assertEquals(UserData.DEFAULT_CHAPTER_INDEX, userData.getSelectedChapterIndex().value)
        assertEquals(UserData.BLANK_TITLE, userData.getSelectedChapterTitle().value)

        userData.setSelectedBookIndex(99999)
        assertEquals(UserData.UNSELECTED_BOOK_INDEX, userData.getSelectedBookIndex().value)
        assertEquals("Application Name", userData.getSelectedBookTitle().value)
        assertEquals(UserData.BLANK_TITLE, userData.getSelectedBookAbbrev().value)
        assertEquals(UserData.DEFAULT_CHAPTER_INDEX, userData.getSelectedChapterIndex().value)
        assertEquals(UserData.BLANK_TITLE, userData.getSelectedChapterTitle().value)
    }

    // SELECTED CHAPTER
    @Test
    fun selectedChapter_initialized_returnsValid()
    {
        val userData = UserData("Application Name")
        assertEquals(false, userData.getChapterDataInitialized().value)
        BibleBookData.initializeChapterData(ModelTestHelper.createChapterTitles())
        userData.initializeChapterData()
        assertEquals(true, userData.getChapterDataInitialized().value)
    }
    @Test
    fun selectedChapter_validSelection_returnsValid()
    {
        val userData = UserData("Application Name")
        BibleBookData.initializeChapterData(ModelTestHelper.createChapterTitles())
        userData.initializeChapterData()

        for (bookItem in BibleBookData.ITEMS)
        {
            userData.setSelectedBookIndex(bookItem.mId)

            for (i in bookItem.mChapters!!.indices)
            {
                userData.setSelectedChapterIndex(i)
                assertEquals(i, userData.getSelectedChapterIndex().value)
                assertEquals(bookItem.mChapters!![i], userData.getSelectedChapterTitle().value)
            }
        }
    }
    @Test
    fun selectedChapter_invalidSelection_returnsDefault()
    {
        val userData = UserData("Application Name")
        BibleBookData.initializeChapterData(ModelTestHelper.createChapterTitles())

        for (bookItem in BibleBookData.ITEMS)
        {
            userData.setSelectedBookIndex(bookItem.mId)

            userData.setSelectedChapterIndex(-99999)
            assertEquals(UserData.DEFAULT_CHAPTER_INDEX, userData.getSelectedChapterIndex().value)
            assertEquals(UserData.BLANK_TITLE, userData.getSelectedChapterTitle().value)

            userData.setSelectedChapterIndex(99999)
            assertEquals(UserData.DEFAULT_CHAPTER_INDEX, userData.getSelectedChapterIndex().value)
            assertEquals(UserData.BLANK_TITLE, userData.getSelectedChapterTitle().value)
        }
    }
}
