package com.example.tabrow

import android.app.Application
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MainApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        val appModule = module {
            viewModel { AViewModel() }
            viewModel { BViewModel() }
        }
        startKoin {
            modules(appModule)
        }
    }
}