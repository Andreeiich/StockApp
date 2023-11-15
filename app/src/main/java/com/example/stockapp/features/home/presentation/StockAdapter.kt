package com.example.stockapp.features.home.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stockapp.R
import com.example.stockapp.databinding.PopularStocksBinding

class StockAdapter : RecyclerView.Adapter<StockAdapter.StockHolder>() {

    private lateinit var startedData: List<StockData>
    private var stockList: List<StockData> = arrayListOf()

    class StockHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = PopularStocksBinding.bind(item)

        fun bind(stock: StockData, position: Int) = with(binding) {

            val changesPercentage: Double =
                ((stock.currentPrice - (stock.currentPrice + Math.abs(stock.dayDelta))) / (stock.currentPrice + stock.dayDelta)) * 100

            ticker.text = stock.ticker
            companyName.text = stock.companyName
            companyName.isSelected = true

            currentPrice.text = "$".plus(stock.currentPrice.toString())

            val signOfCurrency = if (stock.dayDelta >= 0) "+$" else "-$"
            dayDelta.text = signOfCurrency.plus(Math.abs(stock.dayDelta)).plus(" (")
                .plus(String.format("%.2f", Math.abs(changesPercentage)))
                .plus("%)")

            val backgroundOfStock = popularStock.background
            val colorStockPosition =
                if (position % 2 == 0) R.color.background_search_input else R.color.white

            backgroundOfStock.setTint(
                ContextCompat.getColor(
                    popularStock.context,
                    colorStockPosition
                )
            )

            val changesPercentageColor = if (stock.dayDelta < 0) R.color.red else R.color.green
            dayDelta.setTextColor(ContextCompat.getColor(dayDelta.context, changesPercentageColor))

            Glide.with(this@StockHolder.itemView.context).load(stock.image)
                .error(R.drawable.not_icons).into(logo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_stocks, parent, false)
        return StockHolder(view)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    override fun onBindViewHolder(holder: StockHolder, position: Int) {
        holder.bind(stockList[position], position)

    }

    fun addStock(stock: List<StockData>?) {
        if (stock != null) {
            stockList = stock
        }
        notifyDataSetChanged()
    }

    fun setStartingData(startedStocks: List<StockData>?) {
        if (startedStocks != null) {
            startedData = startedStocks
        }
    }

    fun retrieveStartingData() {
        stockList = startedData
        notifyDataSetChanged()
    }
}
