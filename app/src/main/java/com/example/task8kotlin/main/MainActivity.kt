package com.example.task8kotlin
import com.example.task8kotlin.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.task8kotlin.databinding.ActivityMainBinding
import com.example.task8kotlin.main.MainViewModel
import com.example.task8kotlin.main.RecyclerAdapter
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit private var viewModel: MainViewModel
    val recyclerAdapter= RecyclerAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding= DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        setContentView(binding.root)
        initialSetup()
        onClickListeners()
    }
    private fun onClickListeners() {
        binding.setAlarm.setOnClickListener(View.OnClickListener {
            onPickDate()
        })
    }
    private fun onPickDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            var datePicker: DatePickerDialog=DatePickerDialog(this,0,DatePickerDialog.OnDateSetListener
            { view, year, month, dayOfMonth ->

            pickTime(year, month, dayOfMonth)

            },2023,12,4)
            datePicker.show()

        } else {
            TODO("VERSION.SDK_INT < N")
        }
    }

    private fun pickTime(year: Int, month: Int, dayOfMonth: Int) {

        var timePicker:TimePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
                Toast.makeText(this, "Time Picked", Toast.LENGTH_LONG).show()
            },2,2,false)
        timePicker.show()
    }


    private fun initialSetup() {
        //action bar and circular progress bar
        var actionbar= supportActionBar
        actionbar!!.setTitle(getString(R.string.actionBarTitle))
        //binding.circularProgresss.visibility= View.VISIBLE
        val colorDrawable = ColorDrawable(resources.getColor(R.color.themeColor))
        actionBar?.setBackgroundDrawable(colorDrawable)

        //view model initialization
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel


       /* //recycler Adapter work
        binding.recyclerView.adapter=recyclerAdapter
        binding.recyclerView.layoutManager= LinearLayoutManager(applicationContext)*/
    }



}