package com.example.cryptoappk.data.mapper

import com.example.cryptoappk.data.database.CoinInfoDbModel
import com.example.cryptoappk.data.network.model.CoinInfoDto
import com.example.cryptoappk.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoappk.data.network.model.CoinNameListDto
import com.example.cryptoappk.domain.CoinInfo
import com.google.gson.Gson
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CoinMapper {
    fun mapDtoToDbModel(dto: CoinInfoDto): CoinInfoDbModel = CoinInfoDbModel(
        fromSymbol = dto.fromSymbol,
        toSymbol = dto.toSymbol,
        price = dto.price,
        lastUpdate = dto.lastUpdate,
        highDay = dto.highDay,
        lowDay = dto.lowDay,
        lastMarket = dto.lastMarket,
        imageUrl = BASE_IMAGE_URL + dto.imageUrl
    )

    fun mapDbModelToEntity(dbModel: CoinInfoDbModel): CoinInfo = CoinInfo(
        fromSymbol = dbModel.fromSymbol,
        toSymbol = dbModel.toSymbol,
        price = dbModel.price,
        lastUpdate = convertTimestampToTime(dbModel.lastUpdate),
        highDay = dbModel.highDay,
        lowDay = dbModel.lowDay,
        lastMarket = dbModel.lastMarket,
        imageUrl = dbModel.imageUrl
    )

    fun mapJsonContainerToListCoinInfo(jsonContainerDto: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = jsonContainerDto.json ?: return result//получаем объект Raw
        val coinKeySet = jsonObject.keySet()//берем у  нашего Raw все ключи это BTC.ETH и т.д
        for (coinKey in coinKeySet) {//перебираем ключи EHT,BTC,TON И Т.Д
            val currencyJson =
                jsonObject.getAsJsonObject(coinKey)//допустим у btc получаем вложенные ключи USD,EUR,RUB
            val currencyKeySet = currencyJson.keySet()//получаем массив этих ключей
            for (currencyKey in currencyKeySet) {//тут идет цикл по EUR,RUB,USD
                val priceInfo =
                    Gson().fromJson(//создаем из EUR,USD,RUB объект CoinPriceInfo и заполняем его
                        currencyJson.getAsJsonObject(currencyKey),
                        CoinInfoDto::class.java
                    )
                result.add(priceInfo)//добавляем объект в массив result
            }
        }
        return result
    }

    private fun convertTimestampToTime(timestamp: Long?): String {
        if (timestamp == null) return ""
        val stamp =
            Timestamp(timestamp * 1000)//Timestamp принимает милисекунды, а с сервера приходят секунды
        val date = Date(stamp.time)
        val pattern = "HH:mm:ss"//hh с маленькой буквы 12 часовой формат
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    fun mapNamesListToString(namesListDto: CoinNameListDto): String {
        return namesListDto.names?.map { it.coinNameDto?.name }?.joinToString(",") ?: ""
    }
    companion object{
        const val BASE_IMAGE_URL = "https://cryptocompare.com"
    }


}