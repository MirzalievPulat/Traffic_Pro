package uz.frodo.trafficpro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.frodo.trafficpro.adapter.RvAdapter
import uz.frodo.trafficpro.adapter.SearchAdapter
import uz.frodo.trafficpro.databinding.ActivitySearchBinding
import uz.frodo.trafficpro.databinding.ItemBinding
import uz.frodo.trafficpro.databinding.ItemSearchBinding
import uz.frodo.trafficpro.models.AppDatabase
import uz.frodo.trafficpro.models.Dao
import uz.frodo.trafficpro.models.Sign

class SearchActivity : AppCompatActivity(), OnClick {
    lateinit var binding: ActivitySearchBinding
    lateinit var adapter: SearchAdapter
    lateinit var list: List<Sign>
    lateinit var dao:Dao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchView.requestFocus()
        dao = AppDatabase.getInstance(this).dao()


        adapter = SearchAdapter(this)
        list = dao.getAllSigns()
        adapter.differ.submitList(list)
        binding.rvSearch.adapter = adapter


        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = list.filter { item ->
                    item.name!!.contains(newText?:"", ignoreCase = true) || item.about!!.contains(newText?:"", ignoreCase = true)
                }
                println(filteredList)
                adapter.differ.submitList(filteredList)
                return true
            }
        })

    }

    override fun onClick(sign: Sign) {
        val i = Intent(this,AboutActivity::class.java).putExtra("about",sign)
        activityResult.launch(i)
        finish()
    }

    override fun onEdit(sign: Sign, position: Int) {

    }

    override fun onDelete(sign: Sign) {
//
    }

    override fun onLike(sign: Sign, b: ItemBinding?, bS:ItemSearchBinding?) {
        if (sign.liked == 1){
            bS!!.itemLike.setImageResource(R.drawable.bookmark24px)
            sign.liked = 0
            dao.updateSign(sign)
        }else{
            bS!!.itemLike.setImageResource(R.drawable.bookmark_fill24px)
            sign.liked = 1
            dao.updateSign(sign)
        }
    }

    var activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode == Activity.RESULT_OK){
            adapter.notifyDataSetChanged()
        }

    }
}