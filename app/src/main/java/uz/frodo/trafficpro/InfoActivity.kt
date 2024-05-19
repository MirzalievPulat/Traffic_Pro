package uz.frodo.trafficpro

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.frodo.trafficpro.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.telegram.setOnClickListener {
            goToUrl("https://t.me/traffic_pro_official")
        }

        binding.youtube.setOnClickListener {
            goToUrl("https://www.youtube.com/@Traffic_Pro")
        }

        binding.gmail.setOnClickListener {
            goToUrl("https://mail.google.com/mail/u/0/#inbox")
        }

    }
    private fun goToUrl(s: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(s)))
    }
}