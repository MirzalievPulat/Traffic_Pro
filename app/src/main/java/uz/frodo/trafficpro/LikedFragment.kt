package uz.frodo.trafficpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.frodo.trafficpro.adapter.RvAdapter
import uz.frodo.trafficpro.databinding.FragmentLikedBinding
import uz.frodo.trafficpro.databinding.ItemBinding
import uz.frodo.trafficpro.db.MyDbHelper
import uz.frodo.trafficpro.models.Sign


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LikedFragment : Fragment(),OnClick {
    lateinit var binding: FragmentLikedBinding
    lateinit var adapter: RvAdapter
    lateinit var myDbHelper: MyDbHelper
    lateinit var list:ArrayList<Sign>
    var pos = -1
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
    ): View {
        myDbHelper = MyDbHelper(requireContext())
        binding = FragmentLikedBinding.inflate(layoutInflater)
        adapter = RvAdapter(this)
        list = myDbHelper.getLikedSigns()
        println("mana liked: $list")
        adapter.submitList(list)
        binding.rvLiked.adapter = adapter

        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            LikedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(sign: Sign) {
        startActivity(Intent(requireContext(),AboutActivity::class.java).putExtra("about",sign))
    }

    override fun onEdit(sign: Sign,position:Int) {
        pos = position
        startActivityForResult(Intent(requireContext(),EditActivity::class.java).putExtra("edit",sign),1)
    }

    override fun onDelete(sign: Sign) {
        list.remove(sign)
        myDbHelper.deleteSign(sign)
        adapter.submitList(list)
        binding.rvLiked.adapter = adapter
    }

    override fun onLike(sign: Sign, b: ItemBinding) {
        list.remove(sign)
        sign.liked = 0
        myDbHelper.updateSign(sign)
        adapter.submitList(list)
        binding.rvLiked.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            val s = data.getParcelableExtra<Sign>("sign")
            myDbHelper.updateSign(s!!)
            list[pos] = s
            adapter.submitList(list)
            binding.rvLiked.adapter = adapter
        }
    }
}