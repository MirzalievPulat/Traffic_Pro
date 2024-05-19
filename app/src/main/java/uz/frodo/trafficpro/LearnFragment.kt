package uz.frodo.trafficpro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.frodo.trafficpro.databinding.FragmentLearnBinding


class LearnFragment : Fragment() {
    lateinit var binding: FragmentLearnBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLearnBinding.inflate(layoutInflater)

        binding.cardOgohlantiruvchi.setOnClickListener { goto("Ogohlantituvchi")}
        binding.cardImtiyozli.setOnClickListener { goto("Imtiyozli")}
        binding.cardTaqiqlovchi.setOnClickListener { goto("Ta'qiqlovchi")}
        binding.cardBuyuruvchi.setOnClickListener { goto("Buyuruvchi")}
        binding.cardAxborot.setOnClickListener { goto("Axborot")}
        binding.cardServis.setOnClickListener { goto("Servis")}
        binding.cardQoshimcha.setOnClickListener { goto("Qo'shimcha")}
        binding.cardVaqtincha.setOnClickListener { goto("Vaqtinchalik")}

        return binding.root
    }

    fun goto(where: String) {
        var intent = Intent(requireContext(),SignsActivity::class.java).putExtra("where",where)
        startActivity(intent);
    }

}