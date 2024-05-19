package uz.frodo.trafficpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.frodo.trafficpro.adapter.TestAdapter
import uz.frodo.trafficpro.databinding.ActivityTestResultBinding
import uz.frodo.trafficpro.models.AppDatabase

class TestResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dao = AppDatabase.getInstance(this).dao()

        val adapter = TestAdapter()
        binding.testRv.adapter = adapter
        adapter.differ.submitList(dao.getAllTest())

        binding.testResultToolbar.setNavigationOnClickListener {
            finish()
        }

    }
}