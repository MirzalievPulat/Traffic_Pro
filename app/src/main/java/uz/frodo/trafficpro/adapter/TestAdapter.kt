package uz.frodo.trafficpro.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import uz.frodo.trafficpro.databinding.TestItemBinding
import uz.frodo.trafficpro.models.Test

class TestAdapter() : RecyclerView.Adapter<TestAdapter.VHTest>() {
    class VHTest(val binding: TestItemBinding) : RecyclerView.ViewHolder(binding.root)

    val differCallback = object : DiffUtil.ItemCallback<Test>() {
        override fun areItemsTheSame(oldItem: Test, newItem: Test): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: Test, newItem: Test): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHTest {
        val binding = TestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHTest(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VHTest, position: Int) {
        val item = differ.currentList[position]

        holder.binding.testItemName.text = item.name
        holder.binding.testItemTime.text = item.time
        holder.binding.testItemDandan.text = item.correct
        holder.binding.testItemPer.text = item.percentage.toString()

    }


}