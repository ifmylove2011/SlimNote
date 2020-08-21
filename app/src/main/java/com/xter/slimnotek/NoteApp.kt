package com.xter.slimnotek

import android.app.Application
import android.os.Build
import com.xter.slimnotek.util.L
import com.xter.slimnotek.data.NoteSource
import com.xter.slimnotek.data.db.DataLoader

class NoteApp : Application() {

    override fun onCreate() {
        super.onCreate()
        L.DEBUG = BuildConfig.LOG
    }

    val noteSource: NoteSource
        get() = DataLoader.provideNoteSource(this)
}