package uz.frodo.trafficpro

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.frodo.trafficpro.databinding.ActivityAboutBinding
import uz.frodo.trafficpro.models.Sign
import java.io.File

class AboutActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sign = intent.getParcelableExtra<Sign>("about")

        val file = sign!!.image?.let { File(it) }

        binding.aboutImage.setImageURI(Uri.fromFile(file))
        binding.aboutName.text = sign.name
        binding.aboutAbout.text = sign.about

        binding.aboutToolbar.setNavigationOnClickListener {
            finish()
        }

    }
}