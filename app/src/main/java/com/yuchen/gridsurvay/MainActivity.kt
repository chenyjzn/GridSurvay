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
        GridItemDecorationBuilder(binding.recyclerView.context)
            .verticalDivider(1, 0xffffffff.toInt())
            .frame(1, 0xffe6e6e6.toInt())
            .cornerRadius(4)
            .titleBackGroundColor(0xff333333.toInt())
            .gridBackGroundColorList(listOf(0xfff3f3f3.toInt(), 0xffffffff.toInt()))
            .build()
            .into(binding.recyclerView)
//        binding.recyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val adapter = GridTestAdapter()
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        setContentView(binding.root)
    }
}