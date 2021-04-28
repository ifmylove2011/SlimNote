package com.xter.slimnotek

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import com.xter.slimnotek.util.L
import com.xter.slimnotek.data.NoteSource
import com.xter.slimnotek.data.db.DataLoader

class NoteApp : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        L.DEBUG = BuildConfig.LOG
    }

    val noteSource: NoteSource
        get() = DataLoader.provideNoteSource(this)
}