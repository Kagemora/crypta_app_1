package com.example.cryptoappk

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cryptoappk.api.ApiFactory
import com.example.cryptoappk.database.AppDatabase
import com.example.cryptoappk.pojo.CoinPriceInfo
import com.example.cryptoappk.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()
    fun getDetailInfo(fSym: String): LiveData<CoinPriceInfo> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    init {//при создании активити и создании вьюмодели в активити код в init выполнится автоматически
        loadData()
    }

    private fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 50)
            .map { it.data?.map { it.coinInfo?.name }?.joinToString(",").toString() }
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            .map { getPriceListFromRawData(it) }
            .delaySubscription(10, TimeUnit.SECONDS)
            .repeat()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)
                Log.d("TEST_OF_LOADING", "Success: $it")
            }, {
                Log.d("TEST_OF_LOADING", "Failure:  ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    private fun getPriceListFromRawData(
        coinPriceInfoRawData: CoinPriceInfoRawData
    ): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return result//получаем объект Raw
        val coinKeySet = jsonObject.keySet()//берем у  нашего Raw все ключи это BTC.ETH и т.д
        for (coinKey in coinKeySet) {//перебираем ключи EHT,BTC,TON И Т.Д
            val currencyJson = jsonObject.getAsJsonObject(coinKey)//допустим у btc получаем вложенные ключи USD,EUR,RUB
            val currencyKeySet = currencyJson.keySet()//получаем массив этих ключей
            for (currencyKey in currencyKeySet) {//тут идет цикл по EUR,RUB,USD
                val priceInfo = Gson().fromJson(//создаем из EUR,USD,RUB объект CoinPriceInfo и заполняем его
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)//добавляем объект в массив result
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}