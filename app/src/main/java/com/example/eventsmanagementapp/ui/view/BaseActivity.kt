package com.example.eventsmanagementapp.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.eventsmanagementapp.ui.viewmodel.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseActivity : ComponentActivity() {

    // Inject BaseViewModel using Koin
    private val baseViewModel: BaseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                baseViewModel.clearError() // Clear error after showing it
            }
        }
    }
}
