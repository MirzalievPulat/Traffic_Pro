package uz.frodo.trafficpro.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.frodo.trafficpro.OnClick
import uz.frodo.trafficpro.R
import uz.frodo.trafficpro.databinding.ItemBinding
import uz.frodo.trafficpro.models.Sign

class RvAdapter(val onClick: OnClick):ListAdapter<Sign,RvAdapter.VH>(MyDiffUtil()) {
    lateinit var h:VH
    class VH(val binding: ItemBinding):ViewHolder(binding.root)

    class MyDiffUtil:DiffUtil.ItemCallback<Sign>() {
        override fun areItemsTheSame(oldItem: Sign, newItem: Sign): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sign, newItem: Sign): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        h = holder
        holder.binding.itemName.text = getItem(position).name
        holder.binding.itemImage.setImageURI(Uri.parse(getItem(position).image))
        if (getItem(position).liked == 1){
            holder.binding.itemLike.setImageResource(R.drawable.liked)
        }else
            holder.binding.itemLike.setImageResource(R.drawable.not_liked)

        holder.binding.card.setOnClickListener {
            onClick.onClick(getItem(position))
        }
        holder.binding.itemEdit.setOnClickListener {
            onClick.onEdit(getItem(position),position)
        }
        holder.binding.itemDelete.setOnClickListener {
            println("rv ichi detele")
            onClick.onDelete(getItem(position))
        }
        holder.binding.itemLike.setOnClickListener {
            onClick.onLike(getItem(position),holder.binding)
        }
    }

//    fun setLiked(){
//        h.binding.itemLike.setImageResource(R.drawable.liked)
//    }
//    fun setNotLiked(){
//        h.binding.itemLike.setImageResource(R.drawable.not_liked)
//    }
}