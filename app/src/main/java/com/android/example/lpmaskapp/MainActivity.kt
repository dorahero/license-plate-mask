/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.lpmaskapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.example.lpmaskapp.utils.FLAGS_FULLSCREEN
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
private const val IMMERSIVE_FLAG_TIMEOUT = 500L
const val MINIMUM_CONFIDENCE_TF_OD_API = 0.4f



/**
 * Main entry point into our app. This app follows the single-activity pattern, and all
 * functionality is implemented in the form of fragments.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var container: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        System.out.println(this.assets)
        container = findViewById(R.id.fragment_container)
    }

    override fun onResume() {
        super.onResume()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
        container.postDelayed({
            container.systemUiVisibility = FLAGS_FULLSCREEN
        }, IMMERSIVE_FLAG_TIMEOUT)
    }

    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {

//            KeyEvent.KEYCODE_VOLUME_UP -> {
////                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
////                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//                true
//            }
//
//            KeyEvent.KEYCODE_VOLUME_DOWN -> {
////                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
////                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//                true
//            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

//    private val tflite by lazy {
//        Interpreter(
//                FileUtil.loadMappedFile(this, MainActivity.MODEL_PATH),
//                Interpreter.Options().addDelegate(NnApiDelegate()))
//    }
//
//    private val detector by lazy {
//        ObjectDetectionHelper(
//                tflite,
//                FileUtil.loadLabels(this, MainActivity.LABELS_PATH)
//        )
//    }
    companion object {

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
                File(baseFolder, SimpleDateFormat(format, Locale.TAIWAN)
                        .format(System.currentTimeMillis()) + extension)

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }

    }
}
