package uz.frodo.trafficpro

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import uz.frodo.trafficpro.databinding.ActivityAddBinding
import uz.frodo.trafficpro.databinding.DialogLayoutBinding
import uz.frodo.trafficpro.models.AppDatabase
import uz.frodo.trafficpro.models.Dao
import uz.frodo.trafficpro.models.Sign
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddActivity : AppCompatActivity() {
    lateinit var dao: Dao
    lateinit var binding: ActivityAddBinding
    var path:String = ""
    private lateinit var photoURI: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = AppDatabase.getInstance(this).dao()

        binding.addImage.setOnClickListener {
            checkForPermission()
        }

        binding.addButton.setOnClickListener {
            val name = binding.addName.text.toString()
            val about = binding.addAbout.text.toString()
            val type = binding.addSpinner.selectedItem.toString()
            if (name.isNotBlank() && about.isNotBlank() && path.isNotBlank()){
                val sign = Sign(name,about,path,type,0)
                dao.addSign(sign)
                finish()
            }else
                Toast.makeText(applicationContext, "Fill the blank", Toast.LENGTH_SHORT).show()
        }

        binding.addToolbar.setNavigationOnClickListener {
            finish()
        }

    }

    private fun checkForPermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true){
                        println("all p. granted")

                        val dialog = AlertDialog.Builder(this@AddActivity).create()
                        val custom = DialogLayoutBinding.inflate(layoutInflater)
                        dialog.setView(custom.root)
                        custom.dialogCamra.setOnClickListener {
                            val imageFile = createImageFile()
                            photoURI = FileProvider.getUriForFile(this@AddActivity, "uz.frodo.trafficpro.provider", imageFile)
                            getImageFromCamera.launch(photoURI)
                            dialog.dismiss()
                        }
                        custom.dialogGallery.setOnClickListener {
                            getImage.launch("image/*")
                            dialog.dismiss()
                        }
                        dialog.show()

                    }else{
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this@AddActivity,Manifest.permission.CAMERA) &&
                            !ActivityCompat.shouldShowRequestPermissionRationale(this@AddActivity,Manifest.permission.READ_EXTERNAL_STORAGE)){
                            AlertDialog.Builder(this@AddActivity).apply {
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

                override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                    p1?.continuePermissionRequest()
                }
            })
            .check()
    }


    var getImage  = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        uri?:return@registerForActivityResult

        binding.addImage.setImageURI(uri)
        println("Uri: $uri")

        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val inputStream = contentResolver?.openInputStream(uri)
        val file = File(filesDir,"image_$format.png")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        path = file.absolutePath
        Log.d("TTT","path -> $path")
        println("Path: $path")
        val a = Uri.parse(path)
        println("After parse: " + a)///data/user/0/uz.frodo.trafficpro/files/image_20240413_171502.jpg
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val date = SimpleDateFormat("HH:mm:ss_dd-MM-yyyy", Locale.getDefault()).format(Date())

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${date}_", ".jpg", storageDir)
    }

    var getImageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            binding.addImage.setImageURI(photoURI)

            val date = SimpleDateFormat("HH:mm:ss_dd-MM-yyyy", Locale.getDefault()).format(Date())
            val file = File(filesDir, "${date}.jpg")
            val inputStream = contentResolver?.openInputStream(photoURI)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            path = file.absolutePath
            println("first path: $path")
        }
    }
}