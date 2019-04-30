package com.turkishrondo.www.bibleplaces.view

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.kml.KmlLayer
import com.turkishrondo.www.bibleplaces.R
import com.turkishrondo.www.bibleplaces.viewmodel.BookViewModel
import kotlinx.android.synthetic.main.book_detail.*
import java.io.File

//----------------------------------------------------------------------------------------------------------------------
// BookDetailFragment
//
// This fragment represents a single Book detail screen and is either contained in a BookListActivity in two-pane mode
// or a BookDetailActivity in one-pane mode.
//----------------------------------------------------------------------------------------------------------------------
class BookDetailFragment : Fragment(), OnMapReadyCallback
{
    //private var mBinding: BookDetailBinding? = null
    private var mMap: GoogleMap? = null
    private var mKmlLayer: KmlLayer? = null
    private var mMarkers = ArrayList<Marker>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.book_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null)
        {
            val factory = BookViewModel.Factory(activity!!.application)
            val viewModel = ViewModelProviders.of(this, factory).get(BookViewModel::class.java)

            map.onCreate(savedInstanceState)
            map.getMapAsync(this)

            // Set FAB onClickListers.
            fab_prev.setOnClickListener(View.OnClickListener { v ->
                viewModel.onClickPrevFAB()
            })
            fab_next.setOnClickListener(View.OnClickListener { v ->
                viewModel.onClickNextFAB()
            })

            // DATA BINDING
            //mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.book_detail, container, true)
            //mBinding!!.lifecycleOwner = this
            //mBinding!!.viewModel = viewModel
            //mBinding!!.map.onCreate(savedInstanceState)
            //mBinding!!.map.getMapAsync(this)

            // VIEW MODEL CONFIGURATION
            // Observe ALL_CHAPTERS_FILENAME file changes.
            viewModel.getAllChaptersKML().observe(this, object : Observer<File>
            {
                override fun onChanged(file: File?)
                {
                    if (isReadyToLoadKmlLayer(file) == true)
                    {
                        loadKmlLayer(file, viewModel)
                    }
                }
            })

            // Observe chapter-selection changes.
            viewModel.getSelectedChapterIndex().observe(this, object : Observer<Int>
            {
                override fun onChanged(index: Int?)
                {
                    onChapterChange(viewModel);
                }
            })
        }
    }

    // This callback is triggered when the map is ready to be used.
    override fun onMapReady(googleMap: GoogleMap)
    {
        mMap = googleMap

        // This will prevent the "directions" and "google map" buttons from coming up when the user touches a marker.
        mMap!!.uiSettings.setMapToolbarEnabled(false)

        // Center and zoom on Jerusalem.
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(31.771959, 35.217018), 4.5f))
        mMap!!.mapType = GoogleMap.MAP_TYPE_HYBRID

        // Return out of the function if activity is null for some reason, otherwise continue to load the KML data.
        val app:Application = activity?.application ?: return
        val factory = BookViewModel.Factory(app)
        val viewModel = ViewModelProviders.of(this, factory).get(BookViewModel::class.java)
        val file = viewModel.getAllChaptersKML().value
        if (isReadyToLoadKmlLayer(file) == true)
        {
            loadKmlLayer(file, viewModel)
        }
    }

    private fun loadKmlLayer(file: File?, viewModel: BookViewModel)
    {
        // TODO: Creating the KmlLayer and adding it to the map takes a very long time (approx 5s in debug mode), and I
        // think it has to be done on the main thread.  An async load with the progress bar displaying in the mean time
        // won't work, and the existing progress bar isn't really doing anything since this load is blocking up the main
        // thread.  If there's any room for improvement in this project, this is it!  One idea is to break the
        // all-chapters.kml file into multiple files per book.  Before doing that, I'd like to know exactly why it takes
        // so long to load.  Is there something I can do to all-chapters.kml to make it load faster?  Can the file be
        // saved as a different format and loaded faster that way?
        //
        // NOTE: You *must* add the layer to the map *before* you start looking at the layer, or else android will
        // crash.
        mKmlLayer = KmlLayer(mMap, file?.inputStream(), context?.applicationContext)
        mKmlLayer!!.addLayerToMap();

        // Parse the KML file for the chapter titles if it hasn't already been done.
        if (viewModel.isChapterDataInitialized().value == false)
        {
            viewModel.setChapterData(getChaptersFromKML(mKmlLayer!!))
        }

        // Load locations.
        onChapterChange(viewModel);

        // Show the map view and hide the progress view.
        map.visibility = View.VISIBLE
        progress_bar_map_loading.visibility = View.INVISIBLE
    }

    private fun onChapterChange(viewModel: BookViewModel)
    {
        if (mKmlLayer == null)
        {
            return
        }

        // Remove any existing markers.
        if (mMarkers.isNotEmpty() == true)
        {
            for (marker in mMarkers)
            {
                marker.remove()
            }
            mMarkers.clear()
        }

        // TODO: Search through the .kml file recussively.
        // I happen to know that the .kml file is laid out like shown below.  Ideally, we'd search through the file
        // recursively, although, if android was nice enough to us, they may have embedded that functionality into
        // the KmlLayer or KmlContainer.  Regardless, we're brute-forcing it for now.  Anyhow, here's what the file
        // structure looks like.  Oh yeah, apparently "places" in the file corresponds to "containers" in the code and
        // not "placemarks":
        //
        // Places
        // - Temporary Places
        //   - All Bible Places (Books)
        //     - Gen
        //     - Ex
        //     - ...
        //     - Rev
        if (mKmlLayer!!.hasContainers() == true)
        {
            // Container Loop - Look for the container that has the Biblical books.  (The one with nested containers.)
            for (container in mKmlLayer!!.containers)
            {
                if (container.hasContainers() == true)
                {
                    // Selected Book - Look for the selected book.
                    for (subcontainer in container.containers)
                    {
                        val selected_abbrev = viewModel.getSelectedBookAbbrev().value
                        val property_name = subcontainer.getProperty("name")

                        if ((subcontainer.hasProperty("name") == true) && (property_name.equals(selected_abbrev) == true))
                        {
                            // Chapter Loop - Look for the selected chapter.
                            for ((index, chapter) in subcontainer.containers.withIndex())
                            {
                                if (index == viewModel.getSelectedChapterIndex().value)
                                {
                                    // Add Markers
                                    var latLong: LatLng
                                    for (placemark in chapter.placemarks)
                                    {
                                        // Add marker and save to a list so we can remove it later.
                                        latLong = placemark.geometry.geometryObject as LatLng
                                        val name = placemark.getProperty("name")
                                        val description: String = placemark.getProperty("description")
                                        val marker = mMap!!.addMarker(MarkerOptions().position(latLong).title(name).snippet(description))
                                        mMarkers.add(marker)
                                    }

                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO: Parse the KML data in the model.
    // KmlLayer needs to be in the View domain because it depends upon GoogleMap, which depends upon "MapFragment" and
    // other things that appear to be grounded in the Android view realm.  That means that, as is, KmlLayer cannot be
    // used in the Model domain to create a Book and Chapter hash map, unless I can figure out a way to give it a dummy
    // GoogleMap, and the hash map must be created in the View domain.  Perhaps there is a way to parse the .kml file
    // without using a KmlLayer, in which case, the book/chapter data could be built up in the Model domain.
    private fun getChaptersFromKML(layer: KmlLayer): HashMap<Int, ArrayList<String>>
    {
        // Chapter Abbreviation, Chapter
        val hash = HashMap<Int, ArrayList<String>>()

        if (layer.hasContainers() == true)
        {
            // Container Loop - Look for the container that has the Biblical books.  (The one with nested containers.)
            for (container in layer.containers)
            {
                if (container.hasContainers() == true)
                {
                    // Book Loop - Look for each book of the Bible.
                    container.containers.forEachIndexed{ i, subcontainer ->
                        if (subcontainer.hasProperty("name") == true)
                        {
                            val list = ArrayList<String>()

                            // Chapter Loop - Add the chapter name to the list.
                            for (chapter in subcontainer.containers)
                            {
                                list.add(chapter.getProperty("name"))
                            }

                            // Add the book's chapters to the hash.
                            hash.set(i, list)
                        }
                    }
                }
            }
        }

        return hash
    }

    private fun isReadyToLoadKmlLayer(file: File?): Boolean
    {
        return ((mMap != null) && (mKmlLayer == null) && (file != null))
    }

    // MapView is high maintenance, but at least it's straight-forward...
    // https://developers.google.com/maps/documentation/android-sdk/map#mapview
    override fun onStart()
    {
        super.onStart() // Call 1st.
        //mBinding?.map?.onStart()
        map.onStart()
    }
    override fun onResume()
    {
        super.onResume() // Call 1st.
        //mBinding?.map?.onResume()
        map.onResume()
    }
    override fun onPause()
    {
        //mBinding?.map?.onPause()
        map.onPause()
        super.onPause() // Call last.
    }
    override fun onSaveInstanceState(outState: Bundle)
    {
        //mBinding?.map?.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState) // Call ???.
    }

    override fun onDestroyView()
    {
        super.onDestroyView() // Call last.
    }

    override fun onDestroy()
    {
        mKmlLayer?.removeLayerFromMap()
        mMap?.clear()
        if (mMarkers.isNotEmpty() == true)
        {
            for (marker in mMarkers)
            {
                marker.remove()
            }
            mMarkers.clear()
        }
        //mBinding?.map?.onDestroy()
        //map.onDestroy() // Crashes if uncommented because map is already null.

        mKmlLayer = null
        mMap = null
        //mBinding = null

        super.onDestroy() // Call last.
    }
    override fun onLowMemory()
    {
        //mBinding!!.map.onLowMemory()
        map.onLowMemory()
        super.onLowMemory() // Call ???.
    }
}