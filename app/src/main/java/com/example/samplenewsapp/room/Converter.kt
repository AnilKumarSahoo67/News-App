package com.example.samplenewsapp.room

import androidx.room.TypeConverter
import javax.xml.transform.Source

class Converter {
    @TypeConverter
    fun fromSource(source: com.example.samplenewsapp.model.Source) : String{
        return source.name
    }

    @TypeConverter
    fun toSource(name : String) : com.example.samplenewsapp.model.Source{
        return com.example.samplenewsapp.model.Source(name,name)
    }
}