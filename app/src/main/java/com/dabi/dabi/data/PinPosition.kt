package com.dabi.dabi.data

import android.annotation.SuppressLint
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

data class PinPosition(val x: Double, val y: Double) {
    companion object {
        fun fromList(list: List<Double>): PinPosition {
            if (list.size < 2) throw HandledException.InvalidPin
            return PinPosition(list[0], list[1])
        }
    }
}


class PinPositionAdapter : JsonAdapter<List<PinPosition>>() {

    @SuppressLint("CheckResult")
    @FromJson
    override fun fromJson(reader: JsonReader): List<PinPosition> {
        val pins = mutableListOf<PinPosition>()
        reader.beginObject()
        while (reader.hasNext()) {
            reader.nextName()
            reader.beginArray()
            val list = mutableListOf<Double>()
            while (reader.peek() != JsonReader.Token.END_ARRAY) {
                list.add(reader.nextDouble())
            }
            reader.endArray()
            pins.add(PinPosition.fromList(list))
        }
        reader.endObject()
        return pins
    }

    override fun toJson(writer: JsonWriter, value: List<PinPosition>?) {
        throw NotImplementedError()
    }


}