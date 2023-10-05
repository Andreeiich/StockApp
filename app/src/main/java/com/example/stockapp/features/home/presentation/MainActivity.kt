package com.example.stockapp.features.home.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.stockapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.some_title_textview)

        lifecycleScope.launch {
            viewModel.someProccess()
            viewModel.state.collectLatest {data ->
                setText(data.toString())
            }
        }
    }

    fun setText(longString: String) {
        //textView.text = longString
        textView.text = "Abra Cadabra"

    }
}