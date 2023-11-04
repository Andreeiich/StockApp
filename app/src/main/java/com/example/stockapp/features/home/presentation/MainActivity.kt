package com.example.stockapp.features.home.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
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
    val viewModel: StockViewModel by viewModels()

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

            try {
                viewModel.getStocks()
                viewModel.state.collectLatest { data ->
                    data?.let {
                        viewModel.setDataInStocksAdapter(data, adapter)
                    }
                }

            } catch (e: NullPointerException) {
                Toast.makeText(
                    this@MainActivity, getString(R.string.Stocks_unloaded), Toast.LENGTH_LONG
                ).show()
            }
        }


        binding.textInputInner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    viewModel.searchDataStocks(
                        binding.textInputInner.text.toString().lowercase(),
                        adapter
                    )
                } catch (e: NullPointerException) {
                    Toast.makeText(
                        this@MainActivity, getString(R.string.No_name), Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}
