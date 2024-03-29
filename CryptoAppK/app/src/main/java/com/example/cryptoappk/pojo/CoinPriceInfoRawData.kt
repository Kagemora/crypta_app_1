package com.example.cryptoappk.pojo

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//чтобы не делать для каждой валюты отдельный POJO , мы просто задаем JsonObject и будем парсить вручную
//здесь содержатся все криптовалюты
data class CoinPriceInfoRawData(

    @SerializedName("RAW")
    @Expose
    val coinPriceInfoJsonObject: JsonObject? = null
)