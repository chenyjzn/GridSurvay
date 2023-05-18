package com.yuchen.gridsurvay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.yuchen.gridsurvay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.addItemDecoration(GridItemDecoration(3, 2))
        val adapter = GridTestAdapter()
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        setContentView(binding.root)
    }
}