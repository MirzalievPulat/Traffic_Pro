package uz.frodo.trafficpro.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.frodo.trafficpro.OnClick
import uz.frodo.trafficpro.R
import uz.frodo.trafficpro.databinding.ItemBinding
import uz.frodo.trafficpro.models.Sign

// this adapter has a position problem
class RvAdapter(val onClick: OnClick) : RecyclerView.Adapter<RvAdapter.VH>() {
    class VH(val binding: ItemBinding) : ViewHolder(binding.root)

    val differCallback = object : DiffUtil.ItemCallback<Sign>() {
        override fun areItemsTheSame(oldItem: Sign, newItem: Sign): Boolean {
            return oldItem.image == newItem.image && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sign, newItem: Sign): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = differ.currentList[position]

        holder.binding.itemName.text = item.name
        holder.binding.itemImage.setImageURI(Uri.parse(item.image))
        if (item.liked == 1) {
            holder.binding.itemLike.setImageResource(R.drawable.bookmark_fill24px)
        } else
            holder.binding.itemLike.setImageResource(R.drawable.bookmark24px)

        holder.binding.card.setOnClickListener {
            onClick.onClick(item)
        }
        holder.binding.itemEdit.setOnClickListener {
            onClick.onEdit(item, position)
        }
        holder.binding.itemDelete.setOnClickListener {
            println("rv ichi detele")
            onClick.onDelete(item)
        }
        holder.binding.itemLike.setOnClickListener {
            println("position: $position")
            onClick.onLike(item, holder.binding)
        }
    }


}