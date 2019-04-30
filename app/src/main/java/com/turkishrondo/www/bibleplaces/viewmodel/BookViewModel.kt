package com.turkishrondo.www.bibleplaces.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.turkishrondo.www.bibleplaces.model.Repository
import com.turkishrondo.www.bibleplaces.model.data.BibleBookData
import java.io.File

//----------------------------------------------------------------------------------------------------------------------
// BookViewModel
//
// This ViewModel supports BookListActivity and BookDetailActivity by providing Bible book display information and
// forwarding UI calls from the View to the Repository.  The reason why this class serves multiple activities, instead
// of one ViewModel per activity, is because the activities implement a master-detail flow, and to have a ViewModel for
// each activity would be redundant.
//----------------------------------------------------------------------------------------------------------------------
class BookViewModel(app: Application) : AndroidViewModel(app)
{
    //----------------------------------------------------------------------------------------------
    // OUTPUT TO VIEW
    //----------------------------------------------------------------------------------------------

    // DATA BINDING: In order for the view to be refreshed upon changes to the associated data, the following must be
    // done in the ViewModel:
    //
    // 1. The ViewModel method that is data-bound in a View resource file must return a LiveData.  This affects the
    //    auto-generated code that ties the data-binding together (ex: ActivityBookListBindingImpl).
    //
    // 2. The value of the LiveData that is returned by the data-bound method mentioned above must be updated outside of
    //    that method.  In fact, it is the change of the LiveData that triggers process that results in the data-bound
    //    method being called.

    fun getDownloadComplete() : LiveData<Boolean> = getRepository().getDownloadComplete()
    fun getSelectedBookTitle(): LiveData<String> = getRepository().getSelectedBookTitle()
    fun getSelectedBookAbbrev(): LiveData<String> = getRepository().getSelectedBookAbbrev()
    fun getSelectedChapterIndex(): LiveData<Int> = getRepository().getSelectedChapterIndex()
    fun getSelectedChapterTitle(): LiveData<String> = getRepository().getSelectedChapterTitle()
    fun getBookItems(): List<BibleBookData.BookItem> = getRepository().getBibleBookItems()

    //----------------------------------------------------------------------------------------------
    // INPUT FROM VIEW
    //----------------------------------------------------------------------------------------------

    // View Code Input
    fun onSelectBook(i: Int)
    {
        getRepository().setSelectedBookIndex(i)
        getRepository().setSelectedChapterIndex(0)
    }
    fun onUnselectBook() = getRepository().setSelectedBookIndex(-1)

    // UI Events
    fun onClickPrevFAB() = getRepository().setSelectedChapterIndex(getRepository().getSelectedChapterIndex().value!! - 1)
    fun onClickNextFAB() = getRepository().setSelectedChapterIndex(getRepository().getSelectedChapterIndex().value!! + 1)

    //----------------------------------------------------------------------------------------------
    // KML DATA
    //----------------------------------------------------------------------------------------------

    fun getAllChaptersKML() : LiveData<File> = getRepository().getAllChaptersKML()
    fun isChapterDataInitialized(): LiveData<Boolean> = getRepository().getChapterDataInitialized()
    fun setChapterData(hash: HashMap<Int, ArrayList<String>>) = getRepository().setBibleBookChapterData(hash)

    //----------------------------------------------------------------------------------------------
    // FACTORY
    //----------------------------------------------------------------------------------------------

    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory()
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T
        {
            return BookViewModel(application) as T
        }
    }

    //----------------------------------------------------------------------------------------------
    // PRIVATE
    //----------------------------------------------------------------------------------------------

    private fun getRepository(): Repository
    {
        return Repository.getInstance(getApplication<Application>().applicationContext)
    }
}