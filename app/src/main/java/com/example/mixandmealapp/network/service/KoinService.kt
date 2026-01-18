package com.example.mixandmealapp.network.service

import android.content.Context
import com.example.mixandmealapp.data.TokenRepository
import com.example.mixandmealapp.data.dataStore
import com.example.mixandmealapp.repository.FridgeRepository
import com.example.mixandmealapp.repository.RecipeRepository
import com.example.mixandmealapp.repository.UserRepository
import com.example.mixandmealapp.ui.viewmodel.AccountViewModel
import com.example.mixandmealapp.ui.viewmodel.AllergensViewModel
import com.example.mixandmealapp.ui.viewmodel.AuthViewModel
import com.example.mixandmealapp.ui.viewmodel.FavouritesViewModel
import com.example.mixandmealapp.ui.viewmodel.FridgeViewModel
import com.example.mixandmealapp.ui.viewmodel.HomeViewModel
import com.example.mixandmealapp.ui.viewmodel.MyDietViewModel
import com.example.mixandmealapp.ui.viewmodel.RecipeDetailViewModel
import com.example.mixandmealapp.ui.viewmodel.SearchViewModel
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
    single{ RecipeRepository() }
    single{ FridgeRepository() }
    viewModel{ AuthViewModel(get(), get(), get()) }
    viewModel { FavouritesViewModel(get(),get()) }
    viewModel { RecipeDetailViewModel(get(), get()) }
    single { AccountViewModel(get(), get()) }
    viewModel { FridgeViewModel(get(), get()) }
    single { SearchViewModel(get()) }
    viewModel { AllergensViewModel(get(),get()) }
    viewModel { MyDietViewModel(get(), get()) }
}