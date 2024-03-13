package com.example.cryptoappk.domain

class LoadDataUseCase(private val repository: CoinRepository) {
   suspend operator fun invoke() = repository.loadData()
}
