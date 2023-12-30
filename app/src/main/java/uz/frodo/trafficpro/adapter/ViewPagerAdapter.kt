package uz.frodo.trafficpro.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import uz.frodo.trafficpro.AboutActivity
import uz.frodo.trafficpro.InsideFragment
import uz.frodo.trafficpro.OnClick
import uz.frodo.trafficpro.R
import uz.frodo.trafficpro.db.MyDbHelper
import uz.frodo.trafficpro.models.Sign

class ViewPagerAdapter(fr:FragmentActivity,var title:ArrayList<String>):FragmentStateAdapter(fr) {
    var fragments = ArrayList<InsideFragment>()

    override fun getItemCount(): Int {
        return title.size
    }

    override fun createFragment(position: Int): Fragment {
        val insideFragment = InsideFragment.newInstance(title[position])
        fragments.add(insideFragment)
        return insideFragment
    }

    fun getFragmentList():ArrayList<InsideFragment>{
        return fragments
    }
}