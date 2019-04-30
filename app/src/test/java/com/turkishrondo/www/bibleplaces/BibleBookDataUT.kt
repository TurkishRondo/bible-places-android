package com.turkishrondo.www.bibleplaces

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.turkishrondo.www.bibleplaces.model.data.BibleBookData
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

//----------------------------------------------------------------------------------------------------------------------
// BibleBookDataUT
//
// This unit test class tests BibleBookData in the Model.  Dependent classes/objects that have their own tests may be
// used as part of these tests instead of mocking them.
//----------------------------------------------------------------------------------------------------------------------
class BibleBookDataUT
{
    @Rule
    @JvmField
    val mRule = InstantTaskExecutorRule()

    // CHAPTER INITIALIZATION
    @Test
    fun chapterInitialization()
    {
        for (bookItem in BibleBookData.ITEMS)
        {
            assertEquals(null, bookItem.mChapters)
        }

        val hash = ModelTestHelper.createChapterTitles()
        BibleBookData.initializeChapterData(hash)

        for (bookItem in BibleBookData.ITEMS)
        {
            for (i in bookItem.mChapters!!.indices)
            {
                assertEquals(hash[bookItem.mId]?.get(i), bookItem.mChapters?.get(i))
            }
        }
    }
}