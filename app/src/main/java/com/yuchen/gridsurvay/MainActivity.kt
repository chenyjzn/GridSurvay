package com.yuchen.gridsurvay

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.yuchen.gridsurvay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.addItemDecoration(
            GridItemDecoration(
                binding.recyclerView.context,
                horizontalDividerInDp = 10,
                horizontalDividerColor = Color.BLUE,
                verticalDividerInDp = 5,
                verticalDividerColor = Color.GREEN,
                frameInDp = 8,
                frameColor = Color.RED,
                cornerRadiusInDp = 20,
                titleBackGroundColor = Color.BLACK,
                gridBackGroundColorList = listOf(Color.LTGRAY, Color.TRANSPARENT)
            )
        )
        val adapter = GridTestAdapter()
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        setContentView(binding.root)
    }
}