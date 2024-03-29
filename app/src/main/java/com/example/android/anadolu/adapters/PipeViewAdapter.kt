package com.example.android.anadolu.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.anadolu.R
import com.example.android.anadolu.services.PipeInfo
import com.example.android.anadolu.services.Pipes
import com.github.curioustechizen.ago.RelativeTimeTextView

class PipeViewAdapter(var pipeList: Pipes, var itemClick: PipeClickListener, var roomName: String) : RecyclerView.Adapter<PipeViewAdapter.PipeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PipeViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.pipe_list_item,parent,false)
        return PipeViewHolder(view,itemClick)
    }

    override fun getItemCount(): Int {
        return pipeList._pipes.size
    }

    interface PipeClickListener {
        fun getItem(position: Int)
    }

    override fun onBindViewHolder(holder: PipeViewHolder, position: Int) {
        holder.bindData(pipeList._pipes as ArrayList<PipeInfo>, position, roomName)
    }

    class PipeViewHolder(itemView: View, private var itemClick: PipeClickListener) : RecyclerView.ViewHolder(itemView) {

        val DANGER = 5
        val WARNING = 0

        private val pipeNameView = itemView.findViewById(R.id.pipe_name) as TextView
        private val pipeInfoPanel = itemView.findViewById(R.id.pipe_info_bar) as ConstraintLayout
        private val pipeLastUpdate = itemView.findViewById(R.id.pipe_time) as RelativeTimeTextView
        private val healthIndicator = itemView.findViewById(R.id.health_indicator) as ImageView
        fun bindData(pipeList: ArrayList<PipeInfo>, position: Int, roomName:String){
            val pipeInfo = pipeList[position]
            pipeNameView.text = "Sensor name: " + pipeInfo.pipeName
            pipeLastUpdate.setReferenceTime(pipeList[position].lastUpdated.time)

            when {
                pipeInfo.dangerLevel>=DANGER -> healthIndicator.setBackgroundResource(R.drawable.circle_red)
                pipeInfo.dangerLevel>0 -> healthIndicator.setBackgroundResource(R.drawable.circle_orange)
                else -> healthIndicator.setBackgroundResource(R.drawable.circle_green)

            }


            pipeInfoPanel.setOnClickListener {
                itemClick.getItem(adapterPosition)
                itemView.findNavController().navigate(R.id.PipeFragment, bundleOf(
                    "pipeName" to pipeList[adapterPosition].pipeName,
                    "roomName" to roomName
                ))
            }
        }
    }
}
