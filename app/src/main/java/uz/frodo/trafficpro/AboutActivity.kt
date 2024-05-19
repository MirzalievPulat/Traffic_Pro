package uz.frodo.trafficpro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.MenuProvider
import com.karumi.dexter.BuildConfig
import uz.frodo.trafficpro.databinding.ActivityAboutBinding
import uz.frodo.trafficpro.models.AppDatabase
import uz.frodo.trafficpro.models.Dao
import uz.frodo.trafficpro.models.Sign
import java.io.File
import java.util.Locale

class AboutActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutBinding
    lateinit var dao: Dao
    lateinit var sign: Sign
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = AppDatabase.getInstance(this).dao()
        setSupportActionBar(binding.aboutToolbar)

        sign = intent.getParcelableExtra<Sign>("about")!!

//        val file = sign!!.image?.let { File(it) }

        binding.aboutImage.setImageURI(Uri.parse(sign.image))
        binding.aboutName.text = sign.name
        binding.aboutAbout.text = sign.about

        if (sign.liked == 1) binding.fab.setImageResource(R.drawable.bookmark_white_fill24px)

        binding.fab.setOnClickListener {
            if (sign.liked == 1) {
                binding.fab.setImageResource(R.drawable.bookmark_white24px)
                sign.liked = 0
                dao.updateSign(sign)
            } else {
                binding.fab.setImageResource(R.drawable.bookmark_white_fill24px)
                sign.liked = 1
                dao.updateSign(sign)
            }
        }

        binding.aboutImage.setOnClickListener {
            startActivity(Intent(this, ImageActivity::class.java).putExtra("sign", sign))
        }

        binding.aboutToolbar.setNavigationOnClickListener {
            finish()
        }


        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.sign_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {

//                    R.id.share_sign -> {
//                        val imageUri = sign.image!!.toUri()
//                        val shareIntent: Intent = Intent().apply {
//                            action = Intent.ACTION_SEND
//                            type = "image/png"
//                            type = "text/plain"
//                            putExtra(Intent.EXTRA_SUBJECT, sign.name?.uppercase())
//                            putExtra(Intent.EXTRA_TEXT, sign.name?.uppercase() + "\n" + sign.about)
//                            putExtra(Intent.EXTRA_STREAM, imageUri)
//                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                        }
//                        startActivity(Intent.createChooser(shareIntent, null))
//                        println("success")
//                    }

                    R.id.edit_sign -> {
                        var intent = Intent(this@AboutActivity, EditActivity::class.java).putExtra("edit", sign)
                        activityResult.launch(intent)
                    }

                    R.id.delete_sign -> {
                        dao.deleteSign(sign)
                        finish()
                    }

                }
                return true
            }
        })

    }

    var activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var s = result.data!!.getParcelableExtra<Sign>("sign")
            binding.aboutImage.setImageURI(Uri.parse(s!!.image))
            binding.aboutName.text = s.name
            binding.aboutAbout.text = s.about
            sign = s
        }

    }
}