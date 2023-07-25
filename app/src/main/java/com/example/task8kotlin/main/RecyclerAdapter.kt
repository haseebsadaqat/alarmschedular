package com.example.task8kotlin.main
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.task7kotlin.utils.Constants
import com.example.task8kotlin.databinding.DetailrecyclerviewBinding
import com.example.task8kotlin.roomdb.AppRepository
import com.example.task8kotlin.roomdb.DataModel
import com.example.task8kotlin.roomdb.TimeStringDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Collections

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    var alarmList = ArrayList<DataModel>()
    var application:Application?=null
    lateinit var updateButtonListener: OnClickListener
    interface OnClickListener{
    fun getUpdateClick(dataModel: DataModel)
    }
    fun setUpdateClick(listener : OnClickListener){
        updateButtonListener=listener
    }
    fun setUsersList(ldtDataModellist: ArrayList<DataModel>,application: Application) {
        alarmList = ldtDataModellist
        this.application=application
        notifyDataSetChanged()
    }
    inner class MyViewHolder(val binding: DetailrecyclerviewBinding,listener: OnClickListener): RecyclerView.ViewHolder(binding.root){
    init {
        //setting callback for updateAlarm Click
        binding.updateAlarm.setOnClickListener(object : OnClickListener, View.OnClickListener {
        override fun getUpdateClick(dataModel: DataModel) {
        }
        override fun onClick(v: View?) {
            listener.getUpdateClick(alarmList[adapterPosition])
        }
    })
    }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding=DetailrecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding,updateButtonListener)
    }
    override fun getItemCount(): Int {
        return alarmList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.detailRecyclerText = ""+alarmList[position].time.toString().substring(11,16) +
                " | "+alarmList[position].time.toString().substring(8,10)+ "-" +
                (alarmList[position].time.toString().substring(5,7).toInt()+1) +
                "-"+ alarmList[position].time.toString().substring(2,4)
        holder.binding.delAlarm.setOnClickListener(View.OnClickListener {
            if(alarmList[position].delAction==true){
            var appRepository=AppRepository(application ?: return@OnClickListener  )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                      if(alarmList.size!=1){
                        if(!alarmList.isNullOrEmpty()){
                            GlobalScope.launch(Dispatchers.IO) {
                              appRepository.deleteByTimeValue(
                                  LocalDateTime.parse(alarmList[position].time.toString())
                                      .toString()
                              )
                          }
                          notifyDataSetChanged()
                          notifyItemRangeChanged(position,itemCount)
                        }else{
                            Toast.makeText(it.context,Constants.nothingLeftOver,Toast.LENGTH_LONG).show()
                        }
                        //if last elemet remain in list then it executes
                      }else if(alarmList.size==1){
                          try {
                              GlobalScope.launch(Dispatchers.IO) {
                                  appRepository.deleteAllEntries()
                              }
                              alarmList.removeAt(0)
                              notifyItemRemoved(0)
                          }catch(e: java.lang.Exception){
                              Toast.makeText(it.context,e.message,Toast.LENGTH_LONG).show()
                          }
                      }
            }
            }else{
                Toast.makeText(it.context,Constants.failedKey,Toast.LENGTH_LONG).show()
            }
        })
    }
}