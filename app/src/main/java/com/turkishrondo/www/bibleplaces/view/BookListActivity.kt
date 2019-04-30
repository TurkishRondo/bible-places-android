package com.turkishrondo.www.bibleplaces.view

import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.turkishrondo.www.bibleplaces.R
import com.turkishrondo.www.bibleplaces.databinding.*
import com.turkishrondo.www.bibleplaces.model.data.BibleBookData
import com.turkishrondo.www.bibleplaces.viewmodel.BookViewModel
import kotlinx.android.synthetic.main.book_list_content.view.*

//----------------------------------------------------------------------------------------------------------------------
// BookListActivity
//
// This activity represents a list of books of the Bible in a "master-detail flow."  This activity has different
// presentations for narrow devices/ orientations and wide devices/orientations. On "narrows," the activity presents a
// list of the books, which when touched, lead to a BookDetailActivity. On "wides," the activity presents the list of
// books on the left side and the book's details on the right side.
//----------------------------------------------------------------------------------------------------------------------
class BookListActivity : AppCompatActivity()
{
    lateinit var mViewModel: BookViewModel

    // This indicates if the activity is in two-pane mode.
    private var mTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // DATA BINDING: Get the View Model and set up data-binding.  Be sure to set the lifecycle owner and ViewModel.
        val factory = BookViewModel.Factory(this.application)
        mViewModel = ViewModelProviders.of(this, factory).get(BookViewModel::class.java)
        val activityBinding: ActivityBookListBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_list)
        activityBinding.lifecycleOwner = this
        activityBinding.viewModel = mViewModel

        val parent = activityBinding.frameLayout
        val bookListBinding: BookListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.book_list, parent, true)
        bookListBinding.lifecycleOwner = this
        bookListBinding.viewModel = mViewModel

        // Two-Pane
        /*if (book_detail_container != null)
        {
            mTwoPane = true
        }*/

        // Recycler View
        bookListBinding.bookList.adapter = SimpleItemRecyclerViewAdapter(this, mViewModel.getBookItems(), mTwoPane)

        // Inform the ViewModel that we don't have any book selected.
        mViewModel.onUnselectBook()
    }

    //---------------------------------------------------------------------------------------------
    // SimpleItemRecyclerViewAdapter
    //---------------------------------------------------------------------------------------------
    class SimpleItemRecyclerViewAdapter(private val parentActivity: BookListActivity,
                                        private val values: List<BibleBookData.BookItem>, private val twoPane: Boolean):
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>()
    {
        private val onClickListener: View.OnClickListener

        init
        {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as BibleBookData.BookItem
                if (twoPane)
                {
                    val fragment = BookDetailFragment()
                    parentActivity.supportFragmentManager.beginTransaction().replace(R.id.book_detail_container, fragment).commit()
                }
                else
                {
                    val intent = Intent(v.context, BookDetailActivity::class.java)
                    v.context.startActivity(intent)
                }

                parentActivity.mViewModel.onSelectBook(item.mId)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val item = values[position]
            holder.contentView.text = item.mTitle

            with(holder.itemView)
            {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
        {
            val contentView: TextView = view.content
        }
    }
}