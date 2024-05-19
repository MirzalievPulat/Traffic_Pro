package uz.frodo.trafficpro

import android.content.Context
import android.content.SharedPreferences

object MySharedPref {
    private lateinit var shPref: SharedPreferences

    fun init(context: Context){
        shPref = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
    }
    private inline fun SharedPreferences.edit(operation:(SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var name_max:String?
        get() {
            return shPref.getString("name_max","0.0")
        }
        set(value) {
            shPref.edit{
                it.putString("name_max",value)
            }
        }

    var name_max_time: String?
        get() {
            return shPref.getString("name_max_time","-")
        }
        set(value) {
            shPref.edit{
                it.putString("name_max_time",value)
            }
        }


    var type_max: String?
        get() {
            return shPref.getString("type_max","0.0")
        }
        set(value) {
            shPref.edit{
                it.putString("type_max",value)
            }
        }

    var type_max_time: String?
        get() {
            return shPref.getString("type_max_time","-")
        }
        set(value) {
            shPref.edit{
                it.putString("type_max_time",value)
            }
        }
}