package uz.frodo.trafficpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.frodo.trafficpro.adapter.RvAdapter
import uz.frodo.trafficpro.adapter.ViewPagerAdapter
import uz.frodo.trafficpro.databinding.FragmentInsideBinding
import uz.frodo.trafficpro.databinding.ItemBinding
import uz.frodo.trafficpro.db.MyDbHelper
import uz.frodo.trafficpro.models.Sign


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class InsideFragment : Fragment(),OnClick {
    lateinit var binding: FragmentInsideBinding
    var adapter =  RvAdapter(this)
    lateinit var myDbHelper : MyDbHelper
    lateinit var list:ArrayList<Sign>
    var pos = -1
    var param1: String? = null
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
        binding = FragmentInsideBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(requireContext())
        list = myDbHelper.getSignByType(param1!!)
        adapter.submitList(list)
        binding.itemRv.adapter = adapter


        return binding.root
    }

    fun updateFragment(){
        list = myDbHelper.getSignByType(param1!!)
        adapter.submitList(list)
        binding.itemRv.adapter = adapter
    }

    companion object {
        fun newInstance(param1: String) =
            InsideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onClick(sign: Sign) {
        startActivity(Intent(requireContext(),AboutActivity::class.java).putExtra("about",sign))
    }

    override fun onEdit(sign: Sign, position: Int) {
        pos = position
        startActivityForResult(Intent(requireContext(),EditActivity::class.java).putExtra("edit",sign),1)
    }

    override fun onDelete(sign: Sign) {
        myDbHelper.deleteSign(sign)
        list.remove(sign)
        adapter.submitList(list)
        binding.itemRv.adapter = adapter
    }

    override fun onLike(sign: Sign, b: ItemBinding) {
        if (sign.liked == 1){
            b.itemLike.setImageResource(R.drawable.not_liked)
            sign.liked = 0
            myDbHelper.updateSign(sign)
        }else{
            b.itemLike.setImageResource(R.drawable.liked)
            sign.liked = 1
            myDbHelper.updateSign(sign)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            val s = data.getParcelableExtra<Sign>("sign")
            myDbHelper.updateSign(s!!)
            list = myDbHelper.getSignByType(param1!!)
            adapter.submitList(list)
            binding.itemRv.adapter = adapter
        }
    }
}