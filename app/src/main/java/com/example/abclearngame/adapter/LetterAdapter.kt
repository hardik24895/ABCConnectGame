package com.example.abclearngame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abclearngame.R
import com.example.abclearngame.databinding.RowLetterBinding
import com.example.abclearngame.utils.AppUtils
import com.example.abclearngame.utils.AppUtils.getCapitalLatterPath

class LetterAdapter(
    private val list : List<String>,
    private val context :Context,
    private val listener : onLetterClick

) : RecyclerView.Adapter<LetterAdapter.ItemViewHolder>() {

    lateinit var binding: RowLetterBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       binding = RowLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return  ItemViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
          return list.size
    }

    interface onLetterClick{
        fun LetterClick(data: String, pos:Int)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = list[position]
        holder.onBind(data, position, listener, context)
    }

    class ItemViewHolder(itemView: RowLetterBinding) : RecyclerView.ViewHolder(itemView.root) {
        private var binding = itemView

        fun onBind(data: String, position: Int, listener: onLetterClick, context: Context){
            val path= getCapitalLatterPath(data)
            val bm  = AppUtils.getBitmapByAssetName(path, context = context)
            Glide.with(context).load(bm).into(binding.imgLetter)
           // binding.imgLetter.setImageBitmap(bm)

            binding.imgLetter.setOnClickListener {
                listener.LetterClick(data, position)
            }
        }

    }
}