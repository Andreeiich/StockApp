package com.example.stockapp.features.home.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockapp.R
import com.example.stockapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: StockAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: StockViewModel by viewModels()
    private var searchJob: Job? = null
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

        binding.textInputInner.addTextChangedListener(object : TextWatcher {

            /**Этот метод вызывается, чтобы уведомить вас о том, что в течение s символы счетчика,
             * начинающиеся с начала, будут заменены новым текстом длиной после.
             * Попытка внести изменения в s из этого обратного вызова является ошибкой.*/
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                if (after == 0 && count == 1) {
                    binding.textInputFrame.startIconDrawable = (getDrawable(R.drawable.search))
                    binding.textInputInner.hint = getString(R.string.find)
                    binding.textInputInner.clearFocus()
                    getStartStocks()
                }
            }

            /**Этот метод вызывается, чтобы уведомить вас о том, что count в s количество символов, начинающихся с начала start,
             *  только что заменило старый текст before, который раньше имел длину.
             * Попытка внести изменения в s из этого обратного вызова является ошибкой.*/

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textInputFrame.startIconDrawable =
                    (getDrawable(R.drawable.back))
                binding.textInputInner.hint = ""
                getStocks(s.toString())
            }

            /**Этот метод вызывается, чтобы уведомить вас о том, что где-то внутри s текст был изменен.
             * Разрешено вносить дальнейшие изменения в s из этого обратного вызова, но будьте осторожны, чтобы не попасть в бесконечный цикл,
             * поскольку любые внесенные вами изменения приведут к повторному рекурсивному вызову этого метода.
             * (Вам не сообщают, где произошло изменение, потому что другие методы afterTextChanged(), возможно, уже внесли другие изменения и
             * сделали смещения недействительными. Но если вам нужно знать здесь, вы можете использовать Spannable.setSpan в onTextChanged,
             * чтобы отметить свое место, а затем посмотреть отсюда, где закончился пролет.*/
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 0) {
                    binding.textInputFrame.startIconDrawable = (getDrawable(R.drawable.search))
                    binding.textInputInner.hint = getString(R.string.find)
                    binding.textInputInner.clearFocus()
                    getStartStocks()
                }
            }
        })
    }

    fun getStocks(symbols: String) {
        searchJob?.cancel()

        try {
            if (!symbols.equals("")) {
                searchJob = lifecycleScope.launch {

                    viewModel.searchDataStocks(// ищет по символам, не дожидаясь окончательного ввода
                        symbols
                    )
                    viewModel.searched.collectLatest { data ->
                        data?.let {
                            adapter.addStock(data)
                        } ?:Toast.makeText(
                            this@MainActivity, getString(R.string.No_name), Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        } catch (e: NullPointerException) {
            Toast.makeText(
                this@MainActivity, getString(R.string.No_name), Toast.LENGTH_LONG
            ).show()
        }
    }

    fun getStartStocks() {
        adapter.retrieveStartingData()
    }

    fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }

}

