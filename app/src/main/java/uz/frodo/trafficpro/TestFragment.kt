package uz.frodo.trafficpro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import uz.frodo.trafficpro.databinding.FragmentTestBinding
import uz.frodo.trafficpro.models.AppDatabase


class TestFragment : Fragment() {
    lateinit var binding: FragmentTestBinding
     private var moreThanFour:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestBinding.inflate(layoutInflater, container, false)
        moreThanFour = AppDatabase.getInstance(requireContext()).dao().getAllSigns().size >= 4

        binding.nomiBoyichaButton.setOnClickListener {
            goTo("Nomi")

        }

        binding.turiBoyichaButton.setOnClickListener {
            goTo("Guruhi")
        }

        binding.natijalarButton.setOnClickListener {
            startActivity(Intent(requireContext(),TestResultActivity::class.java))
        }

        return binding.root
    }

    fun goTo(where: String) {
        if (moreThanFour) {
            val intent = Intent(requireContext(), TestActivity::class.java)
            intent.putExtra("type", where)
            startActivity(intent)
        } else Toast.makeText(requireContext(), "Belgilar kamida 4ta bo'lishi kerak", Toast.LENGTH_SHORT).show()
    }




}