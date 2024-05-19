package uz.frodo.trafficpro

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.karumi.dexter.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import uz.frodo.trafficpro.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//this activity is only for example, it doesn't have a job in this app

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var im = 0
    private lateinit var photoURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()

        binding.fromCamera.setOnClickListener {
            val imageFile = createImageFile()
            photoURI = FileProvider.getUriForFile(this, "uz.frodo.trafficpro", imageFile)
            getImageFromCamera.launch(photoURI)
        }

        binding.fromStorage.setOnClickListener {
            getImageFromStorage.launch("image/*")
        }

        binding.clear.setOnClickListener {
            if (filesDir.isDirectory) {
                filesDir.listFiles()?.forEach {
                    it.delete()
                }
            }
        }


    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val date = SimpleDateFormat("HH:mm:ss_dd-MM-yyyy", Locale.getDefault()).format(Date())

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${date}_", ".jpg", storageDir)
    }

    var getImageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            binding.imagePlace.setImageURI(photoURI)

            val date = SimpleDateFormat("HH:mm:ss_dd-MM-yyyy", Locale.getDefault()).format(Date())
            val file = File(filesDir, "${date}.jpg")
            val inputStream = contentResolver?.openInputStream(photoURI)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
        }
    }

    var getImageFromStorage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@registerForActivityResult
        binding.imagePlace.setImageURI(uri)

        val inputStream = contentResolver?.openInputStream(uri)
        val file = File(filesDir, "image${im++}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

    }

    private fun checkPermissions() {
        Dexter.withContext(this@MainActivity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {

                        } else {
                            var settings = false
                            for (permission in report.deniedPermissionResponses) {
                                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                        this@MainActivity,
                                        permission.permissionName
                                    )
                                ) {
                                    settings = true
                                }
                            }

                            if (settings) {
                                AlertDialog.Builder(this@MainActivity).apply {
                                    setCancelable(false)
                                    setMessage("Please go to settings and give permissions")
                                    setPositiveButton("OK") { dialog, which ->
                                        val intent = Intent(
                                            Intent.ACTION_OPEN_DOCUMENT, Uri.fromParts(
                                                "package",
                                                packageName, null
                                            )
                                        )
                                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                        startActivity(intent)
                                        dialog.dismiss()
                                    }
                                }.show()
                            }

                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Log.d("TAG", "onCreate: ${it.name}")
            }
            .check()
    }
}