package com.example.cryptoappk.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cryptoappk.data.repository.CoinRepositoryImpl
import com.example.cryptoappk.domain.GetCoinInfoListUseCase
import com.example.cryptoappk.domain.GetCoinInfoUseCase
import com.example.cryptoappk.domain.LoadDataUseCase

class CoinViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CoinRepositoryImpl(application)

    private val getCoinInfoListUseCase = GetCoinInfoListUseCase(repository)
    private val getCoinInfoUseCase = GetCoinInfoUseCase(repository)
    private val loadDataUseCase = LoadDataUseCase(repository)

    val coinInfoList = getCoinInfoListUseCase()

    fun getDetailInfo(fSym: String) = getCoinInfoUseCase(fSym)

    init {
        loadDataUseCase()
    }
}