package com.yuchen.gridsurvay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuchen.gridsurvay.databinding.HolderTestBinding

class GridTestAdapter : RecyclerView.Adapter<GridTestAdapter.TestHolder>() {
    val list: List<String> =
        listOf(
            "a", "b", "c",
            "100", "1000", "10000",
            "100", "1000", "10000",
            "100", "1000", "1000000000000000000000000000000000000000",
            "100", "1000", "10000",
            "100", "1000", "10000",
            "100", "1000", "10000",
            "100", "1000", "10000",
            "100", "1000", "10000"
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridTestAdapter.TestHolder {
        return TestHolder(
            HolderTestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GridTestAdapter.TestHolder, position: Int) {
        holder.bind(position)
    }

    inner class TestHolder(private val binding: HolderTestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.holderTextView.text = list[position]
        }
    }
}