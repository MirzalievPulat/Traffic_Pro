package uz.frodo.trafficpro

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.frodo.trafficpro.databinding.ActivityImageBinding
import uz.frodo.trafficpro.models.Sign

class ImageActivity : AppCompatActivity() {
    lateinit var binding: ActivityImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sign = intent.getParcelableExtra<Sign>("sign")

        binding.imageToolbar.title = sign!!.name
        binding.zoomImage.setImageURI(Uri.parse(sign.image))



        binding.imageToolbar.setNavigationOnClickListener {
            finish()
        }

    }
}