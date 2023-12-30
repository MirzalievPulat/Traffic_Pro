package uz.frodo.trafficpro.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.frodo.trafficpro.models.Sign

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    companion object {
        const val DB_NAME = "Signs_db"
        const val VERSION = 1

        const val TABLE_NAME = "signs"
        const val ID = "id"
        const val NAME = "name"
        const val ABOUT = "about"
        const val IMAGE = "image"
        const val TYPE = "type"
        const val LIKED = "liked"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "create table $TABLE_NAME($ID integer not null primary key autoincrement,$NAME text not null, $ABOUT text " +
                "not null,$IMAGE text not null,$TYPE text not null,$LIKED integer not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_NAME")
        onCreate(db)
    }

    fun addSign(sign: Sign) {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(NAME, sign.name)
            put(ABOUT, sign.about)
            put(IMAGE, sign.image)
            put(TYPE, sign.type)
            put(LIKED, sign.liked)
        }
        db.insert(TABLE_NAME, null, cv)
        db.close()
    }

    fun deleteSign(sign: Sign) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID = ?", arrayOf("${sign.id}"))
        db.close()
    }

    fun updateSign(sign: Sign) {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(NAME, sign.name)
            put(ABOUT, sign.about)
            put(IMAGE, sign.image)
            put(TYPE, sign.type)
            put(LIKED, sign.liked)
        }
        db.update(TABLE_NAME, cv, "$ID = ?", arrayOf("${sign.id}"))
        db.close()
    }

    fun getSignByType(type: String): ArrayList<Sign> {
        val list = ArrayList<Sign>()
        val db = this.readableDatabase
        val columns = arrayOf(ID, NAME, ABOUT, IMAGE, TYPE, LIKED)
        db.use {
            val cursor = db.query(TABLE_NAME, columns, "$TYPE = ?", arrayOf(type), null, null, null)
            cursor.use {
                while (cursor.moveToNext()){
                    list.add(
                        Sign(
                            cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getInt(5)
                        ))
                }
            }
        }
        return list
    }

    fun getLikedSigns():ArrayList<Sign>{
        val list = ArrayList<Sign>()
        val db = this.readableDatabase
        val columns = arrayOf(ID, NAME, ABOUT, IMAGE, TYPE, LIKED)
        db.use {
            val cursor = db.query(TABLE_NAME,columns,"$LIKED = ?", arrayOf("1"),null,null,null)
            cursor.use {
                while (cursor.moveToNext()){
                    list.add(
                        Sign(
                            cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getInt(5)
                        ))
                }
            }
        }
        return list
    }

}