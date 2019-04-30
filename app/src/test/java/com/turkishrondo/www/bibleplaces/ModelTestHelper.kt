package com.turkishrondo.www.bibleplaces

import com.turkishrondo.www.bibleplaces.model.data.BibleBookData

object ModelTestHelper
{
    fun createChapterTitles(): HashMap<Int, ArrayList<String>>
    {
        // Initialize a hash of dummy chapter names in BibleBookData.
        val hash = HashMap<Int, ArrayList<String>>()
        for (i in BibleBookData.ITEMS.indices)
        {
            val chapters = ArrayList<String>()
            for (j in 1..5)
            {
                chapters.add("Book" + i + "Chapter" + j)
            }
            hash.set(i, chapters)
        }
        return hash
    }
}