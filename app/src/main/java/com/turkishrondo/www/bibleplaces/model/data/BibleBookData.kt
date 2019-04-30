package com.turkishrondo.www.bibleplaces.model.data

import java.util.ArrayList
import java.util.HashMap

//----------------------------------------------------------------------------------------------------------------------
// BibleBookData
//
// This object contains data of the books of the Bible that is used by the application.  The titles and abbreviations of
// the books are hard-coded, but the associated list of chapter titles is initially null, then parsed from the .kml data
// once the BookDetailFragment's GoogleMap is ready.  See the note in BookDetailFragment.getChaptersFromKML() for more
// information about parsing the .kml for the chapters, and see initializeChapterData() below for setting the chapter
// data.
//----------------------------------------------------------------------------------------------------------------------
object BibleBookData
{
    // Store the data in an ArrayList and a HashMap for convenient access.
    val ITEMS: MutableList<BookItem> = ArrayList()
    val ITEM_MAP: MutableMap<Int, BookItem> = HashMap()

    init
    {
        var bookNum = 0

        // Add books of the Bible.
        addItem(BookItem(bookNum++, "Genesis", "Gen", null))
        addItem(BookItem(bookNum++, "Exodus", "Ex", null))
        addItem(BookItem(bookNum++, "Leviticus", "Lev", null))
        addItem(BookItem(bookNum++, "Numbers", "Num", null))
        addItem(BookItem(bookNum++, "Deuteronomy", "Deut", null))
        addItem(BookItem(bookNum++, "Joshua", "Josh", null))
        addItem(BookItem(bookNum++, "Judges", "Judg", null))
        addItem(BookItem(bookNum++, "Ruth", "Ruth", null))
        addItem(BookItem(bookNum++, "1 Samuel", "1 Sam", null))
        addItem(BookItem(bookNum++, "2 Samuel", "2 Sam", null))
        addItem(BookItem(bookNum++, "1 Kings", "1 Kgs", null))
        addItem(BookItem(bookNum++, "2 Kings", "2 Kgs", null))
        addItem(BookItem(bookNum++, "1 Chronicles", "1 Chr", null))
        addItem(BookItem(bookNum++, "2 Chronicles", "2 Chr", null))
        addItem(BookItem(bookNum++, "Ezra", "Ezra", null))
        addItem(BookItem(bookNum++, "Nehemiah", "Neh", null))
        addItem(BookItem(bookNum++, "Esther", "Est", null))
        addItem(BookItem(bookNum++, "Job", "Job", null))
        addItem(BookItem(bookNum++, "Psalms", "Ps", null))
        //addItem(BookItem(bookNum++, "Proverbs", "Prov", null))
        addItem(BookItem(bookNum++, "Ecclesiastes", "Eccl", null))
        addItem(BookItem(bookNum++, "Song of Solomon", "Sng", null))
        addItem(BookItem(bookNum++, "Isaiah", "Isa", null))
        addItem(BookItem(bookNum++, "Jeremiah", "Jer", null))
        addItem(BookItem(bookNum++, "Lamentations", "Lam", null))
        addItem(BookItem(bookNum++, "Ezekiel", "Ezek", null))
        addItem(BookItem(bookNum++, "Daniel", "Dan", null))
        addItem(BookItem(bookNum++, "Hosea", "Hos", null))
        addItem(BookItem(bookNum++, "Joel", "Joel", null))
        addItem(BookItem(bookNum++, "Amos", "Amos", null))
        addItem(BookItem(bookNum++, "Obadiah", "Obad", null))
        addItem(BookItem(bookNum++, "Jonah", "Jonah", null))
        addItem(BookItem(bookNum++, "Micah", "Mic", null))
        addItem(BookItem(bookNum++, "Nahum", "Nahum", null))
        addItem(BookItem(bookNum++, "Habakkuk", "Hab", null))
        addItem(BookItem(bookNum++, "Zephaniah", "Zeph", null))
        addItem(BookItem(bookNum++, "Haggai", "Hag", null))
        addItem(BookItem(bookNum++, "Zechariah", "Zech", null))
        addItem(BookItem(bookNum++, "Malachi", "Mal", null))
        addItem(BookItem(bookNum++, "Matthew", "Matt", null))
        addItem(BookItem(bookNum++, "Mark", "Mark", null))
        addItem(BookItem(bookNum++, "Luke", "Luke", null))
        addItem(BookItem(bookNum++, "John", "John", null))
        addItem(BookItem(bookNum++, "Acts", "Acts", null))
        addItem(BookItem(bookNum++, "Romans", "Rom", null))
        addItem(BookItem(bookNum++, "1 Corinthians", "1 Cor", null))
        addItem(BookItem(bookNum++, "2 Corinthians", "2 Cor", null))
        addItem(BookItem(bookNum++, "Galatians", "Gal", null))
        addItem(BookItem(bookNum++, "Ephesians", "Eph", null))
        addItem(BookItem(bookNum++, "Philippians", "Phil", null))
        addItem(BookItem(bookNum++, "Colossians", "Col", null))
        addItem(BookItem(bookNum++, "1 Thessalonians", "1 Thes", null))
        //addItem(BookItem(bookNum++, "2 Thessalonians", "2 Thes", null))
        addItem(BookItem(bookNum++, "1 Timothy", "1 Tim", null))
        addItem(BookItem(bookNum++, "2 Timothy", "2 Tim", null))
        addItem(BookItem(bookNum++, "Titus", "Titus", null))
        //addItem(BookItem(bookNum++, "Philemon", "Philemon", null))
        addItem(BookItem(bookNum++, "Hebrews", "Heb", null))
        //addItem(BookItem(bookNum++, "James", "James", null))
        addItem(BookItem(bookNum++, "1 Peter", "1 Pet", null))
        addItem(BookItem(bookNum++, "2 Peter", "2 Pet", null))
        //addItem(BookItem(bookNum++, "1 John", "1 John", null))
        //addItem(BookItem(bookNum++, "2 John", "2 John", null))
        //addItem(BookItem(bookNum++, "3 John", "3 John", null))
        addItem(BookItem(bookNum++, "Jude", "Jude", null))
        addItem(BookItem(bookNum, "Revelation", "Rev", null))
    }

    private fun addItem(item: BookItem)
    {
        ITEMS.add(item)
        ITEM_MAP.put(item.mId, item)
    }

    // Initialize the chapter data once the .kml data has been parsed.  The hash parameter is like this:
    // HashMap<Book Id, List of Chapter names>
    fun initializeChapterData(hash: HashMap<Int, ArrayList<String>>)
    {
        for (item in ITEM_MAP)
        {
            item.value.mChapters = hash[item.key]
        }
    }

    data class BookItem(val mId: Int, val mTitle: String, val mAbbrev: String, var mChapters: ArrayList<String>?)
    {
        override fun toString(): String = mTitle
    }
}