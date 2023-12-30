package uz.frodo.trafficpro

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import uz.frodo.trafficpro.databinding.ActivityEditBinding
import uz.frodo.trafficpro.databinding.DialogLayoutBinding
import uz.frodo.trafficpro.db.MyDbHelper
import uz.frodo.trafficpro.models.Sign
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    var path = ""
    private lateinit var photoURI: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myDbHelper = MyDbHelper(this)

        val sign = intent.getParcelableExtra<Sign>("edit")

        binding.addImage.setImageURI(Uri.parse(sign!!.image))
        path = sign.image.toString()
        binding.addName.setText(sign.name)
        binding.addAbout.setText(sign.about)
        when(sign.type){
            "Ogohlantiruvchi"-> binding.addSpinner.setSelection(0)
            "Imtiyozli"-> binding.addSpinner.setSelection(1)
            "Ta'qiqlovchi"-> binding.addSpinner.setSelection(2)
            "Buyuruvchi"-> binding.addSpinner.setSelection(3)
        }

        binding.addImage.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val custom = DialogLayoutBinding.inflate(layoutInflater)
            dialog.setView(custom.root)
            custom.dialogCamra.setOnClickListener {
                val imageFile = createImageFile()
                photoURI = FileProvider.getUriForFile(this, "uz.frodo.trafficpro", imageFile)
                getImageFromCamera.launch(photoURI)
                dialog.dismiss()
            }
            custom.dialogGallery.setOnClickListener {
                getImage.launch("image/*")
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.addButton.setOnClickListener {
            val name = binding.addName.text.toString()
            val about = binding.addAbout.text.toString()
            val type = binding.addSpinner.selectedItem.toString()
            println("before adding:$path")
            if (name.isNotBlank() && about.isNotBlank() && path.isNotBlank()){
                val s = Sign(sign.id,name,about,path,type,sign.liked)
                val i = Intent().putExtra("sign",s)
                setResult(RESULT_OK,i)
                finish()
            }else
                Toast.makeText(applicationContext, "Fill the blank", Toast.LENGTH_SHORT).show()
        }

        binding.addToolbar.setNavigationOnClickListener {
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    var getImage  = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        uri?:return@registerForActivityResult
        binding.addImage.setImageURI(uri)

//        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val format = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
        val format2 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YY"))
        val inputStream = contentResolver?.openInputStream(uri)
        val file = File(filesDir,"image_$format.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        path = file.absolutePath
        println("first path: $path")
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