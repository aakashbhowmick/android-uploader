package com.example.androidexplore

import android.util.Log
import io.tus.android.client.TusPreferencesURLStore
import io.tus.java.client.TusClient
import io.tus.java.client.TusExecutor
import io.tus.java.client.TusUpload

class FileUploader(val client:TusClient, val upload:TusUpload) : TusExecutor() {


    override fun makeAttempt() {
        var uploader = client.resumeOrCreateUpload(upload)
        uploader.chunkSize = 1024

        do {
            val totalBytes = upload.size
            val bytesUploaded = uploader.offset
            var progress:Double = bytesUploaded.toDouble() / totalBytes.toDouble()
            Log.i("Upload progress :", progress.toString())
        } while(uploader.uploadChunk() > -1)

        uploader.finish()
    }

}