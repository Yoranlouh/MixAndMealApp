package com.example.mixandmealapp.network.service

import android.content.Context
import com.example.mixandmealapp.data.TokenRepository
import com.example.mixandmealapp.data.dataStore
import com.example.mixandmealapp.repository.UserRepository
import com.example.mixandmealapp.ui.viewmodel.AccountViewModel
import com.example.mixandmealapp.ui.viewmodel.AuthViewModel
import com.example.mixandmealapp.ui.viewmodel.HomeViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    single{get<Context>().dataStore}
    single{ TokenRepository(get()) }
    single{ UserRepository() }
    single{ HomeViewModel(get(), get()) }
    viewModel{ AuthViewModel(get(), get(), get()) }
    viewModel{ AccountViewModel(get(), get()) }
}