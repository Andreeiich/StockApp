package com.example.stockapp.features.home.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.marginTop
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockapp.R
import com.example.stockapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CustomViewListener {

    @Inject
    lateinit var adapter: StockAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: StockViewModel by viewModels()
    private var searchJob: Job? = null
    private lateinit var searchCustomPopularRequests: ViewGroup
    private lateinit var searchCustomSearched: ViewGroup
    private val handler = Handler(Looper.getMainLooper())
    private var changedSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchListener = findViewById<SearchCustomView>(R.id.SearchCustomSearched)
        searchListener.viewListener = this
        val popularStockListener = findViewById<SearchCustomView>(R.id.SearchCustomPopularRequests)
        popularStockListener.viewListener = this

        binding.apply {
            recView.layoutManager = LinearLayoutManager(this@MainActivity)
            recView.adapter = adapter
        }
        searchCustomPopularRequests =
            findViewById<SearchCustomView>(R.id.SearchCustomPopularRequests)
        searchCustomSearched = findViewById<SearchCustomView>(R.id.SearchCustomSearched)
        startVisibility()

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
                    searchJob?.cancel()// для отмены, если строка пустая стала, т.е. когда пользователь очистил строку ввода
                    getStartStocks()
                } else {
                    requestVisibility()
                    getStocks(s.toString())
                }
            }
        })

        binding.textInputInner.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!searchCustomPopularRequests.children.any()) {//Проверка, чтобы не добавить повторно элементы, если пользователь вернется к поиску снова
                fillRequests()
            }
            if (changedSearch) {
                searchCustomSearched.removeAllViews()
                searchCustomPopularRequests.removeAllViews()
                fillRequests()
                changedSearch = false
            }
            focusOnSearchVisibility()
        })

    }

    private fun getStocks(symbols: String) {
        searchJob?.cancel()// для отмены, если строка ввода изменилась

        try {
            if (!symbols.equals("")) {

                searchJob = lifecycleScope.launch {
                    delay(400)
                    viewModel.searchDataStocks(// ищет по символам, не дожидаясь окончательного ввода
                        symbols
                    )
                    viewModel.changeListRequestsOfUser(symbols)
                    changedSearch = true
                    viewModel.searched.collectLatest { data ->
                        data?.let {
                            adapter.addStock(data)
                        } ?: Toast.makeText(
                            this@MainActivity, getString(R.string.No_name), Toast.LENGTH_LONG
                        ).show()
                        handler.post {
                            binding.recView.visibility = View.VISIBLE
                            binding.showMore.visibility = View.VISIBLE
                            binding.stocksForSearch.visibility = View.VISIBLE
                            binding.line.visibility = View.VISIBLE
                        } // если не отправлять на главный поток,
                        // сначала будет отображаться основной список акций, а после тот, который найден
                    }
                }
            }

        } catch (e: NullPointerException) {
            Toast.makeText(
                this@MainActivity, getString(R.string.No_name), Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun getStartStocks() {
        adapter.retrieveStartingData()
        startVisibility()
    }

    private fun fillRequests() {

        var popularRequests: List<SearchData>? = arrayListOf()
        var hadRequest: List<SearchData>? = arrayListOf()
        lifecycleScope.launch {
            viewModel.getRequestsOfUser()
            hadRequest = viewModel.request.value
            viewModel.getPopularRequests()
            popularRequests = viewModel.popularRequests.value
        }

        showHadRequests(hadRequest)
        showPopularRequests(popularRequests)

    }

    private fun showPopularRequests(list: List<SearchData>?) {

        for (i in 0 until (list?.size ?: 0)) {

            val textView = layoutInflater.inflate(R.layout.search_stock, null)
            val item = textView.findViewById<TextView>(R.id.searchStockItem)
            item.text = list?.get(i).toString()

            val layoutParams = SearchCustomView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(4, 4, 4, 4)
            textView.layoutParams = layoutParams
            searchCustomPopularRequests.addView(textView)
        }
    }

    private fun showHadRequests(list: List<SearchData>?) {

        for (i in 0 until (list?.size ?: 0)) {
            val textView = layoutInflater.inflate(R.layout.search_stock, null)
            val item = textView.findViewById<TextView>(R.id.searchStockItem)
            item.text = list?.get(i).toString()
            val layoutParams = SearchCustomView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(4, 4, 4, 4)
            textView.layoutParams = layoutParams
            searchCustomSearched.addView(textView)
        }
    }

    private fun startVisibility() {
        binding.favourite.visibility = View.VISIBLE
        binding.stocks.visibility = View.VISIBLE
        binding.recView.visibility = View.VISIBLE
        binding.popularRequests.visibility = View.GONE
        binding.SearchCustomPopularRequests.visibility = View.GONE
        binding.YouHaveSearched.visibility = View.GONE
        binding.SearchCustomSearched.visibility = View.GONE
        binding.line.visibility = View.GONE
        binding.stocksForSearch.visibility = View.GONE
        binding.showMore.visibility = View.GONE
    }

    private fun focusOnSearchVisibility() {
        binding.favourite.visibility = View.INVISIBLE
        binding.stocks.visibility = View.INVISIBLE
        binding.recView.visibility = View.INVISIBLE
        binding.popularRequests.visibility = View.VISIBLE
        binding.SearchCustomPopularRequests.visibility = View.VISIBLE
        binding.YouHaveSearched.visibility = View.VISIBLE
        binding.SearchCustomSearched.visibility = View.VISIBLE
    }

    private fun requestVisibility() {
        binding.favourite.visibility = View.INVISIBLE
        binding.stocks.visibility = View.INVISIBLE
        binding.recView.visibility = View.INVISIBLE
        binding.popularRequests.visibility = View.INVISIBLE
        binding.SearchCustomPopularRequests.visibility = View.INVISIBLE
        binding.YouHaveSearched.visibility = View.INVISIBLE
        binding.SearchCustomSearched.visibility = View.INVISIBLE
    }

    private fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }

    override fun transferInfo(info: String) {
        binding.textInputInner.setText(info)
    }

}

