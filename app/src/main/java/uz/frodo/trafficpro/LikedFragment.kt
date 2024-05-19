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
import uz.frodo.trafficpro.databinding.FragmentLikedBinding
import uz.frodo.trafficpro.databinding.ItemBinding
import uz.frodo.trafficpro.databinding.ItemSearchBinding
import uz.frodo.trafficpro.models.AppDatabase
import uz.frodo.trafficpro.models.Dao
import uz.frodo.trafficpro.models.Sign


                                            // this fragment has deleting problem
class LikedFragment : Fragment(),OnClick {
    lateinit var binding: FragmentLikedBinding
    lateinit var adapter: RvAdapter
    lateinit var dao: Dao
    var disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikedBinding.inflate(layoutInflater)
        dao = AppDatabase.getInstance(requireContext()).dao()

        val adapter = RvAdapter(this)
        binding.rvLiked.adapter = adapter

        disposable.add(dao.getLikedSigns()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { it ->
                    adapter.differ.submitList(it)
                }

            ))

        return binding.root
    }



    override fun onClick(sign: Sign) {
        startActivity(Intent(requireContext(),AboutActivity::class.java).putExtra("about",sign))
    }

    override fun onEdit(sign: Sign,position:Int) {
        startActivity(Intent(requireContext(),EditActivity::class.java).putExtra("edit",sign))
    }

    override fun onDelete(sign: Sign) {
        dao.deleteSign(sign)
    }

    override fun onLike(sign: Sign, b: ItemBinding?,bS:ItemSearchBinding?) {
        println(sign)
        sign.liked = 0
        dao.updateSign(sign)

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}