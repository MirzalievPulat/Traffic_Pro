package uz.frodo.trafficpro.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Test")  // Specify table name
data class Test(
    @ColumnInfo(name = "name")  // Specify column name for each field
    var name: String,
    @PrimaryKey
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "correct")
    var correct: String,
    @ColumnInfo(name = "percentage")
    var percentage: Int
)

