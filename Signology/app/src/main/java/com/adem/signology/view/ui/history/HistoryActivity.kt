package com.adem.signology.view.ui.history

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adem.signology.R
import com.adem.signology.data.Result
import com.adem.signology.data.remote.response.HistoryItem
import com.adem.signology.view.ViewModelFactory


class HistoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        recyclerView = findViewById(R.id.rv_history)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.getHistory().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    updateRecyclerView(result.data.history)
                }

                is Result.Error -> {
                    var errorMessage = result.error.trim()
                    if (errorMessage == "HTTP 404"){
                        errorMessage = "History not found"
                    }
                    showAlertDialog(errorMessage)
                }

                else -> {}
            }
        }
    }

    private fun updateRecyclerView(historyList: List<HistoryItem?>?) {
        historyAdapter = HistoryAdapter(this, historyList)
        recyclerView.adapter = historyAdapter
    }


    private fun showAlertDialog(message:String) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
        })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}