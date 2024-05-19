package uz.frodo.trafficpro

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import uz.frodo.trafficpro.databinding.ActivityTestBinding
import uz.frodo.trafficpro.models.AppDatabase
import uz.frodo.trafficpro.models.Sign
import uz.frodo.trafficpro.models.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates

class TestActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var binding: ActivityTestBinding
    private var count = 1
    private var score = 0
    var listSize by Delegates.notNull<Int>()
    private lateinit var wholeList:ArrayList<Sign>
    private lateinit var testType: String
    private lateinit var current:Sign
    private lateinit var list:MutableList<Sign>
    private lateinit var allSigns:MutableList<Sign>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dao = AppDatabase.getInstance(this).dao()
        list = AppDatabase.getInstance(this).dao().getAllSigns().toMutableList()
        listSize = list.size
        wholeList = ArrayList(list)

        testType = intent.getStringExtra("type")!!

        if (testType == "Nomi"){
            binding.testToolbar.title = "Nomi bo'yicha test"
            binding.testQuestion.text = "Quyidagi belgining nomi nima?"
            setDataNames()
        }else{
            binding.testToolbar.title = "Guruh bo'yicha test"
            binding.testQuestion.text = "Quyidagi belgi qaysi guruhga kiradi?"
            setDataTypes()
        }

        binding.testToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.testNextBt.setOnClickListener {
            if (list.size == 0){
                val percentage = score*100/listSize
                val time = SimpleDateFormat("HH:mm / dd.MM.yyyy", Locale.getDefault()).format(Date())
                val name = if (testType == "Nomi") "Nomi b." else "Guruhi b."
                val correct = "$score/$listSize"

                val test = Test(name,time,correct,percentage)
                dao.insertTest(test)

                var dialog = AlertDialog.Builder(this)
                dialog.setTitle("$percentage%")
                dialog.setMessage("$listSize tadan $score ta to'g'ri")
                dialog.setCancelable(false)
                dialog.setPositiveButton("OK"){d,w-> setResult(RESULT_OK);finish()}
                dialog.create().show()
            }else{
                binding.testA.isClickable = true
                binding.testA.setBackgroundResource(R.drawable.test_bt)
                binding.testB.isClickable = true
                binding.testB.setBackgroundResource(R.drawable.test_bt)
                binding.testC.isClickable = true
                binding.testC.setBackgroundResource(R.drawable.test_bt)
                binding.testD.isClickable = true
                binding.testD.setBackgroundResource(R.drawable.test_bt)
                if (testType == "Nomi") setDataNames() else setDataTypes()
            }
        }

    }

    private fun setDataTypes() {
        current = list.random()
        list.remove(current)
        val filterdList = arrayListOf("Ogohlantiruvchi","Imtiyozli","Ta'qiqlovchi","Buyuruvchi","Axborot-ishora","Servis",
            "Qo'shimcha","Vaqtinchalik")
        filterdList.remove(current.type)
        val one = filterdList.random()
        filterdList.remove(one)
        val two = filterdList.random()
        filterdList.remove(two)
        val three = filterdList.random()
        val options = arrayListOf(current.type!!,one,two,three)
        options.shuffle()

        binding.testScore.text = "$count/${listSize}"
        binding.testImage.setImageURI(current.image?.toUri())
        binding.testA.text = options[0]
        binding.testB.text = options[1]
        binding.testC.text = options[2]
        binding.testD.text = options[3]

        binding.testNextBt.isClickable = false
        count++
    }

    private fun setDataNames(){
        current = list.random()
        list.remove(current)
        var filtered:ArrayList<Sign> = ArrayList(wholeList.filter { it != current })
        val one = filtered.random()
        filtered.remove(one)
        val two = filtered.random()
        filtered.remove(two)
        val three = filtered.random()
        val options = arrayListOf(current,one,two,three)
        options.shuffle()

        binding.testScore.text = "$count/${listSize}"
        binding.testImage.setImageURI(current.image?.toUri())
        binding.testA.text = options[0].name
        binding.testB.text = options[1].name
        binding.testC.text = options[2].name
        binding.testD.text = options[3].name

        binding.testNextBt.isClickable = false
        count++
    }

    override fun onClick(v: View?) {
        var button = v as Button
        val answer = if (testType == "Nomi") current.name else current.type
        if(button.text.equals(answer)){
            println("yes")
            button.setBackgroundColor(Color.GREEN)
            score++
        }else{
            println("no")
            button.setBackgroundColor(Color.RED)
            if(binding.testA.text.equals(answer)) binding.testA.setBackgroundColor(Color.GREEN)
            else if(binding.testB.text.equals(answer)) binding.testB.setBackgroundColor(Color.GREEN)
            else if(binding.testC.text.equals(answer)) binding.testC.setBackgroundColor(Color.GREEN)
            else if(binding.testD.text.equals(answer)) binding.testD.setBackgroundColor(Color.GREEN)

        }
        binding.testNextBt.isClickable = true

        binding.testA.isClickable = false
        binding.testB.isClickable = false
        binding.testC.isClickable = false
        binding.testD.isClickable = false
    }
}