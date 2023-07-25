package com.example.task8kotlin.main
import android.annotation.SuppressLint
import com.example.task8kotlin.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task8kotlin.databinding.ActivityMainBinding
import com.example.task8kotlin.roomdb.DataModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList
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
        observeFromRoom()
    }
    private fun observeFromRoom() {
     viewModel.liveDataRoom.observe(this, Observer {
        var ldtDataModellist = ArrayList<DataModel>()
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
             if(!it.isNullOrEmpty()){
             for(i in it){

             ldtDataModellist.add(DataModel(id = i.id, time = LocalDateTime.parse(i.time), delAction = i.delAction))
                    }
                 viewModel.dataModellist= ldtDataModellist
                 recyclerAdapter.setUsersList(ldtDataModellist,application)
                 recyclerAdapter.notifyItemInserted(it.size-1)
             }else{
                 Toast.makeText(applicationContext,getString(R.string.nothingFound),Toast.LENGTH_LONG).show()
             }
         }
     })
    }
    private fun onClickListeners() {
        //on add alarm button
        binding.setAlarm.setOnClickListener(View.OnClickListener {
            onPickDate()
        })
        //on deleteAll alarm button
        binding.deleteAll.setOnClickListener(View.OnClickListener {
            viewModel.deleteAllEntries()
            recyclerAdapter.setUsersList(ArrayList<DataModel>(),application)
        })
//Callback Listener from adapter
recyclerAdapter.setUpdateClick(object : RecyclerAdapter.OnClickListener {
    override fun getUpdateClick(dataModel: DataModel) {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            if(dataModel.delAction==true){
                //updates date and time of selected object
                 onUpdateDate(dataModel)
            }else{
        Toast.makeText(applicationContext,getString(R.string.cannotUpdate),Toast.LENGTH_LONG).show()
            }
        }
    }
})
    }
   fun onPickDate() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            var datePicker = DatePickerDialog(this,0, DatePickerDialog.OnDateSetListener
            { view, year, month, dayOfMonth ->

                pickTime(year, month, dayOfMonth)

            },LocalDateTime.now().toLocalDate().year,LocalDateTime.now().toLocalDate().monthValue-1,
                LocalDateTime.now().toLocalDate().dayOfMonth)
            datePicker.show()
        } else {
        }

    }
    @SuppressLint("NewApi")
      fun pickTime(year: Int, month: Int, dayOfMonth: Int) {
        var timePicker = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val calender = Calendar.getInstance()
            viewModel.tenMinutesThreshold = LocalDateTime.of(
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH),
                calender.get(Calendar.HOUR_OF_DAY),
                calender.get(Calendar.MINUTE)
            ).plusMinutes(10)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Ten Minutes Threshold Validation
                if (LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute)
                        .isAfter(viewModel.tenMinutesThreshold)
                ) {
                    viewModel.dataModelObject = DataModel(
                        time = LocalDateTime.of(
                            year,
                            month,
                            dayOfMonth,
                            hourOfDay,
                            minute
                        ), delAction = false
                    )
                    viewModel.saveDataModelToRoom(viewModel.dataModelObject)
                    viewModel.alarmschedular.scheduleAlarm(viewModel.dataModelObject)
                    Toast.makeText(this, getString(R.string.alarmScheduled), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.failedScheduled),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        },LocalDateTime.now().toLocalTime().hour,LocalDateTime.now().toLocalTime().minute,false)
        timePicker.show()
    }
    private fun initialSetup() {
        //action bar and circular progress bar
        var actionbar= supportActionBar
        actionbar?.setTitle(getString(R.string.actionBarTitle))
        actionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.themeColor)))
        //view model initialization
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel
        //recycler Adapter work
        binding.recyclerView.adapter=recyclerAdapter
        binding.recyclerView.layoutManager= LinearLayoutManager(applicationContext)
    }
    private fun onUpdateDate(dataModel: DataModel) {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            var datePicker = DatePickerDialog(this,0, DatePickerDialog.OnDateSetListener
            { view, year, month, dayOfMonth ->

                onUpdateTime(year, month, dayOfMonth,dataModel)

            },LocalDateTime.now().toLocalDate().year,LocalDateTime.now().toLocalDate().monthValue-1,
                LocalDateTime.now().toLocalDate().dayOfMonth)
            datePicker.show()
        }else{
        }
    }
    private fun onUpdateTime(year: Int, month: Int, dayOfMonth: Int, dataModel: DataModel) {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
        var timePicker = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val calender = Calendar.getInstance()
            var tenMinutesThreshold = LocalDateTime.of(
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH),
                calender.get(Calendar.HOUR_OF_DAY),
                calender.get(
                    Calendar.MINUTE
                )
            ).plusMinutes(10)
            // Ten Minutes Threshold Validation
            if (LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute)
                    .isAfter(tenMinutesThreshold)
            ) {
                //delete and update selected object
                viewModel.deleteByTimeValue(LocalDateTime.parse(dataModel.time.toString()).toString())
                recyclerAdapter.notifyDataSetChanged()

                viewModel.dataModelObject = DataModel(
                    time = LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute),
                    delAction = false
                )
                viewModel.saveDataModelToRoom(viewModel.dataModelObject)
                viewModel.alarmschedular.scheduleAlarm(viewModel.dataModelObject)
                Toast.makeText(this,R.string.alarmScheduled, Toast.LENGTH_LONG).show()
            } else {

                Toast.makeText(
                    applicationContext,
                    R.string.failedScheduled,
                    Toast.LENGTH_LONG
                ).show()
            }
        },LocalDateTime.now().toLocalTime().hour,LocalDateTime.now().toLocalTime().minute,false)
    timePicker.show()

        }else{ }
    }
}