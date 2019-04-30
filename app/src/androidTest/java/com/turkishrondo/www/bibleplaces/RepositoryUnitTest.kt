package com.turkishrondo.www.bibleplaces

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.turkishrondo.www.bibleplaces.model.Repository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

//----------------------------------------------------------------------------------------------------------------------
// RepositoryUnitTest
//
// This unit test class tests Repository in the Model.  Dependent classes/objects that have their own tests may be used
// as part of these tests instead of mocking them.
//----------------------------------------------------------------------------------------------------------------------
@RunWith(AndroidJUnit4::class)
@LargeTest
class RepositoryUnitTest
{
    // TODO: Get this test working again now that the dependencies I was using are deprecated.  Currently, there is an
    // issue with using LiveData, which is used by UserData, which is used by Repository, thus making it dependent upon
    // some framework object with a life cycle, like an Activity.  Previously, I was able to use a test rule to get this
    // working, but that rule appears to be deprecated or missing in the latest version of the
    // androidx.arch.core:core-testing library.

    //@get:Rule
    //val activityRule = ActivityTestRule(RepositoryUnitTest::class.java)

    // FILE DOWNLOADS
    @Test
    fun getAllChaptersKml_ActiveServer()
    {
        // Get the repository.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = Repository.getInstance(appContext)
        val fileLD = repository.getAllChaptersKML()

        // Verify the file was saved.
        Assert.assertEquals(appContext.cacheDir.absolutePath + "/" + ALL_CHAPTERS_FILENAME, fileLD.value?.absoluteFile)
    }
}