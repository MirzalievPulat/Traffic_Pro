package uz.frodo.trafficpro.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Flowable


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSign(sign: Sign)


    @Update
    fun updateSign(sign: Sign)

    @Delete
    fun deleteSign(sign: Sign)

    @Query("select * from Sign")
    fun getAllSigns(): List<Sign>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTest(test: Test)

    @Query("select * from Test ORDER BY percentage DESC")
    fun getAllTest(): List<Test>
    @Query("select * from Sign where type=:type")
    fun getSignByType(type:String): Flowable<List<Sign>>

    @Query("select * from Sign where liked=1")
    fun getLikedSigns(): Flowable<List<Sign>>

}


