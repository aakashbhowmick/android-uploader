package com.example.androidexplore
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import io.tus.android.client.TusPreferencesURLStore
import io.tus.java.client.TusClient
import java.net.URL

class FirstActivity : AppCompatActivity() {

    /** Callback from file picker */
    var fileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri ->
        println("URI received : ${uri}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Register button for choosing file
        var btnFilePick = findViewById<Button>(R.id.btnPickFile)
        btnFilePick.setOnClickListener{
            requestPermission()
            startFilePickActivity()
        }

        // Register button for making get request
        var btnUpload = findViewById<Button>(R.id.btnUpload)
        btnUpload.setOnClickListener{
            makeGetRequest()     // Make a dummy get request
        }

    }

    private fun uploadFile(){
        var client = TusClient()
        client.setUploadCreationURL( URL("http://localhost:5000/upload") )
        var pref = getSharedPreferences("tus", 0)
        client.enableResuming(TusPreferencesURLStore(pref))
    }

    private fun makeGetRequest() {
        val apiInterface = APIClient().getClient()?.create<APIInterface>(APIInterface::class.java)

        /**  GET List Resources */
        val call: Call<APIResponse?>? = apiInterface!!.doGetListResources()

        call?.enqueue(object : Callback<APIResponse?> {
            override fun onResponse(
                call: Call<APIResponse?>?,
                response: Response<APIResponse?>
            ) {
                val resource: APIResponse? = response.body()
                val name = resource?.name
                val age = resource?.age
                Log.i("Works::","Data received : name = $name , age = $age" )
            }

            override fun onFailure(call: Call<APIResponse?>, t: Throwable) {
                Log.e("Error!! ->", t.toString())
            }

        })
    }

    private fun hasWritePermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED


    private fun requestPermission() {
        if(!hasWritePermission()){
            var perms = listOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, perms.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startFilePickActivity() {
        // Open appropriate file picking activity
        fileLauncher.launch("application/*")
    }
}
