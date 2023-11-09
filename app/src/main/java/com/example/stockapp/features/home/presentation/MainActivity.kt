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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: StockAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: StockViewModel by viewModels()

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
                        adapter.addStock(data)
                        adapter.setStartingData(data)
                    }
                }
            } catch (e: NullPointerException) {
                Toast.makeText(
                    this@MainActivity, getString(R.string.Stocks_unloaded), Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.textInputInner.setOnClickListener() {
            binding.textInputFrame.startIconDrawable = (getDrawable(R.drawable.back))
            binding.textInputInner.setHint("")
        }

        binding.textInputInner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    binding.textInputFrame.startIconDrawable = (getDrawable(R.drawable.back))
                    viewModel.searchDataStocks(
                        binding.textInputInner.text.toString().lowercase()
                    )
                } catch (e: NullPointerException) {
                    Toast.makeText(
                        this@MainActivity, getString(R.string.No_name), Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.textInputInner.text?.equals("") == true || binding.textInputInner.text.isNullOrEmpty()) {
                    binding.textInputFrame.startIconDrawable = (getDrawable(R.drawable.search))
                    binding.textInputInner.hint
                    binding.textInputInner.clearFocus()
                }
            }
        })
    }
}
