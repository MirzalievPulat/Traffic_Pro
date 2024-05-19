package uz.frodo.trafficpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.frodo.trafficpro.adapter.RvAdapter
import uz.frodo.trafficpro.adapter.ViewPagerAdapter
import uz.frodo.trafficpro.databinding.FragmentInsideBinding
import uz.frodo.trafficpro.databinding.ItemBinding
import uz.frodo.trafficpro.databinding.ItemSearchBinding
import uz.frodo.trafficpro.models.AppDatabase
import uz.frodo.trafficpro.models.Dao
import uz.frodo.trafficpro.models.Sign


private const val ARG_PARAM1 = "param1"


class InsideFragment : Fragment(),OnClick {
    lateinit var binding: FragmentInsideBinding
    var adapter =  RvAdapter(this)
    lateinit var dao : Dao
    var param1: String? = null
    var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInsideBinding.inflate(layoutInflater)
        dao = AppDatabase.getInstance(requireContext()).dao()
        binding.itemRv.adapter = adapter

        disposable.add(dao.getSignByType(param1!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    adapter.differ.submitList(it)
                }
            ))


        return binding.root
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
        var intent = Intent(requireContext(),EditActivity::class.java).putExtra("edit",sign)
        startActivity(intent)
    }

    override fun onDelete(sign: Sign) {
        dao.deleteSign(sign)
    }

    override fun onLike(sign: Sign, b: ItemBinding?, bS:ItemSearchBinding?) {
        if (sign.liked == 1){
            b!!.itemLike.setImageResource(R.drawable.bookmark24px)
            sign.liked = 0
            dao.updateSign(sign)
        }else{
            b!!.itemLike.setImageResource(R.drawable.bookmark_fill24px)
            sign.liked = 1
            dao.updateSign(sign)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}