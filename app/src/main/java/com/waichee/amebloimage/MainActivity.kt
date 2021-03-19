package com.waichee.amebloimage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.waichee.amebloimage.ui.main.MainFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var intentUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    handleSendText(intent) // Handle text being sent
                }
            }
        }

        if (savedInstanceState == null) {
            val fragment = MainFragment.newInstance()
            val args = Bundle()
            args.putString("intent_url", intentUrl)
            Timber.i(intentUrl)
            fragment.arguments = args
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            intentUrl = it
        }
    }
}