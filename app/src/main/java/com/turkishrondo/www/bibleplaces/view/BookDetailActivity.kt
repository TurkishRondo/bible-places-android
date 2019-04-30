package com.turkishrondo.www.bibleplaces.view

import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.turkishrondo.www.bibleplaces.R
import com.turkishrondo.www.bibleplaces.databinding.ActivityBookDetailBinding
import com.turkishrondo.www.bibleplaces.viewmodel.BookViewModel
import kotlinx.android.synthetic.main.activity_book_detail.*

//----------------------------------------------------------------------------------------------------------------------
// BookDetailActivity
//
// This activity represents a single Book detail screen in a "master-detail flow." This activity is only used on narrow
// width devices/ orientations.  On wide devices/orientations, Book details are presented side-by-side with a list of
// items in a BookListActivity.
//----------------------------------------------------------------------------------------------------------------------
class BookDetailActivity : AppCompatActivity()
{
    lateinit var mViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // DATA BINDING: To bind data using a ViewModel, we must perform the following here.  Note that the variables
        // set in the <data> tags of the activity layout file (activity_book_detail.xml) can be found in the auto-
        // generated binding class, ActivityBookDetailBinding.  See the related section in the developer guide at "Use
        // ViewModel to manage UI-related data" here:
        //
        // https://developer.android.com/topic/libraries/data-binding/architecture
        val factory = BookViewModel.Factory(application)
        mViewModel = ViewModelProviders.of(this, factory).get(BookViewModel::class.java)
        val binding: ActivityBookDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_detail)
        binding.lifecycleOwner = this
        binding.viewModel = mViewModel

        // Set the up button.
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // DATA BINDING: Even though data-binding is set up in the activity_book_detail resource file and is working as
        // expected, android insists on overwriting the toolbar title from the data binding with the application name,
        // unless I explicitly set the title here.  To observe, set a breakpoint in Toolbar.setTitle() and comment out
        // the line below.  I suspect there is a "proper" way to avoid the issue, but I don't know what it is yet.
        supportActionBar?.setTitle(mViewModel.getSelectedBookTitle().value)

        if (savedInstanceState == null)
        {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            val fragment = BookDetailFragment()
            supportFragmentManager.beginTransaction().add(R.id.book_detail_container, fragment).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId)
        {
            android.R.id.home -> {
                navigateUpTo(Intent(this, BookListActivity::class.java))
                mViewModel.onUnselectBook()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}