package uz.frodo.trafficpro.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.frodo.trafficpro.OnClick
import uz.frodo.trafficpro.R
import uz.frodo.trafficpro.databinding.ItemBinding
import uz.frodo.trafficpro.databinding.ItemSearchBinding
import uz.frodo.trafficpro.models.Sign

class SearchAdapter(val onClick: OnClick): RecyclerView.Adapter<SearchAdapter.VH>() {
    class VH(val binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Sign>() {
        override fun areItemsTheSame(oldItem: Sign, newItem: Sign): Boolean {
            return oldItem.image == newItem.image && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sign, newItem: Sign): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = differ.currentList[position]
        holder.binding.itemName.text = item.name
        holder.binding.itemImage.setImageURI(Uri.parse(item.image))
        if (item.liked == 1){
            holder.binding.itemLike.setImageResource(R.drawable.bookmark_fill24px)
        }else
            holder.binding.itemLike.setImageResource(R.drawable.bookmark24px)

        holder.binding.card.setOnClickListener {
            onClick.onClick(item)
        }

        holder.binding.itemLike.setOnClickListener {
            println("position: $position" )
            onClick.onLike(item,null,holder.binding)
        }
    }


}