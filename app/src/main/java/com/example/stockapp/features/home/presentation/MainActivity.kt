package com.example.stockapp.features.home.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockapp.R
import com.example.stockapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val adapter = StockAdapter()
    val viewModel: MainViewModel by viewModels()

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            recView.layoutManager = LinearLayoutManager(this@MainActivity)
            recView.adapter = adapter
        }

        lifecycleScope.launch {
            viewModel.getData()
            viewModel.state.collectLatest { data ->
                data?.let {
                    viewModel.setDataInStocksAdapter(data, adapter)
                }
            }
        }
    }

}
