package uz.frodo.trafficpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.google.android.material.tabs.TabLayoutMediator
import uz.frodo.trafficpro.adapter.ViewPagerAdapter
import uz.frodo.trafficpro.databinding.FragmentHomeBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: ViewPagerAdapter
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val titleList = arrayListOf<String>("Ogohlantiruvchi", "Imtiyozli", "Ta'qiqlovchi", "Buyuruvchi")

        adapter = ViewPagerAdapter(requireActivity(), titleList)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, p ->
            tab.text = titleList[p]
        }.attach()

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.add_sign -> {
                        val intent = Intent(requireContext(), AddActivity::class.java)
                        activityLauncher.launch(intent)
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }


    val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val fragments = adapter.getFragmentList()
            println(fragments)
            for (i in fragments.indices) {
                println(fragments[i].param1)
                if (fragments[i].param1 != null)
                    fragments[i].updateFragment()
            }
        }
    }

}