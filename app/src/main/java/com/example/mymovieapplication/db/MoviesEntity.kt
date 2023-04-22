package com.example.mymovieapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymovieapplication.utils.Constants.MOVIES_TABLE

@Entity(tableName = MOVIES_TABLE)
data class MoviesEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var title : String = "",
    var poster : String = "",
    var yearofissue : String = "",
    var raiting : String = "",
    var duration : String = "",
    var country : String = ""
)




