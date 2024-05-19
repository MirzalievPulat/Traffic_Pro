package uz.frodo.trafficpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.frodo.trafficpro.databinding.ActivitySignsBinding

class SignsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when(intent.getStringExtra("where")){
            "Ogohlantituvchi"->{
                binding.signsToolbar.title = "Ogohlantiruvchi belgilar"
                binding.allSignsImage.setImageResource(R.drawable.ogohlantiruvchi)
            }
            "Imtiyozli"->{
                binding.signsToolbar.title = "Imtiyozli belgilar"
                binding.allSignsImage.setImageResource(R.drawable.imtiyozli)
            }
            "Ta'qiqlovchi"->{
                binding.signsToolbar.title = "Ta'qiqlovchi belgilar"
                binding.allSignsImage.setImageResource(R.drawable.taqiqlovchi)
            }
            "Buyuruvchi"->{
                binding.signsToolbar.title = "Buyuruvchi belgilar"
                binding.allSignsImage.setImageResource(R.drawable.buyuruvchi)
            }
            "Axborot"->{
                binding.signsToolbar.title = "Axborot-ishora belgilar"
                binding.allSignsImage.setImageResource(R.drawable.axborot_ishora)
            }
            "Servis"->{
                binding.signsToolbar.title = "Servis belgilar"
                binding.allSignsImage.setImageResource(R.drawable.servis_belgilar)
            }
            "Qo'shimcha"->{
                binding.signsToolbar.title = "Qo'shimcha axborot belgilar"
                binding.allSignsImage.setImageResource(R.drawable.qoshimcha_axborot)
            }
            "Vaqtinchalik" -> {
                binding.signsToolbar.title = "Vaqtinchalik belgilar"
                binding.allSignsImage.setImageResource(R.drawable.vaqtinchalik_belgilar)
            }


        }

        binding.signsToolbar.setNavigationOnClickListener {
            finish()
        }

    }
}