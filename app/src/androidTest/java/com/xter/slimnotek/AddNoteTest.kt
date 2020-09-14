package com.xter.slimnotek

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.xter.slimnotek.data.NoteLocalSource
import com.xter.slimnotek.data.NoteSource
import com.xter.slimnotek.data.NoteSourceManager
import com.xter.slimnotek.data.db.DataLoader
import com.xter.slimnotek.ui.note.AddNoteFragment
import com.xter.slimnotek.ui.note.AddNoteFragmentDirections
import com.xter.slimnotek.ui.note.NoteFragmentDirections
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class AddNoteTest {

    private lateinit var noteSource: NoteSource

    @Before
    fun initRepository() {
//        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val context: Context = mock(Context::class.java)
        noteSource = DataLoader.provideNoteSource(context)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.xter.slimnotek", appContext.packageName)
    }

    @Test
    fun addNote() {
        launchFragmentInContainer<AddNoteFragment>(null, R.style.AppTheme)
        Espresso.onView(withId(R.id.ed_add_note_title)).perform(ViewActions.replaceText("我是一个标题"))
        Espresso.onView(withId(R.id.ed_add_note_content)).perform(ViewActions.replaceText("我是一个内容"))
        Espresso.onView(withId(R.id.fab_save_note)).perform(ViewActions.click())

        val navController = mock(NavController::class.java)
        Mockito.verify(navController).navigate(
            AddNoteFragmentDirections.addToNotes()
        )
    }
}
