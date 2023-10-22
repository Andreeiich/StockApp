package com.example.stockapp.features.home.presentation

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.databinding.PopularStocksBinding
import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO
import com.bumptech.glide.Glide
import com.example.stockapp.R

class StockAdapter : RecyclerView.Adapter<StockAdapter.StockHolder>() {

    val stockListName: MutableList<StockNameDTO> = arrayListOf()
    var stockList: MutableList<StockDTO> = arrayListOf()

    class StockHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = PopularStocksBinding.bind(item)

        @SuppressLint("ResourceAsColor")
        fun bind(stockName: StockNameDTO, stock: StockDTO, position: Int) = with(binding) {

            ticker.text = stock.symbol
            companyName.text = stock.companyName
            currentPrice.text = "$".plus(stock.price.toString())

            if (stock.changes >= 0) {
                dayDelta.text = "+$".plus(stock.changes).plus(" (")
                    .plus(String.format("%.2f", stockName.changesPercentage))
                    .plus("%)")
            } else {
                dayDelta.text = "-$".plus(Math.abs(stock.changes)).plus(" (")
                    .plus(String.format("%.2f", stockName.changesPercentage))
                    .plus("%)")
            }

            val defaultColor = popularStock.background
            val conditionColor =
                if (position % 2 == 0) R.color.background_search_input else R.color.white
            defaultColor.setTint(ContextCompat.getColor(popularStock.context, conditionColor))

            val changesPercentageColor = if (stock.changes < 0) R.color.red else R.color.green
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
        holder.bind(stockListName[position], stockList[position], position)
    }

    fun addStock(stockName: List<StockNameDTO>, stock: MutableList<StockDTO>) {
        stockList.addAll(stock)
        stockListName.addAll(stockName)
        notifyDataSetChanged()
    }

}