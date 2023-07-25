package com.example.abclearngame.tracing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.eisuchi.extention.goToActivityBundle
import com.eisuchi.extention.setToFullScreen
import com.example.abclearngame.adapter.LetterAdapter
import com.example.abclearngame.databinding.ActivityLettersBinding
import com.example.abclearngame.tracing.TracingActivity
import com.example.abclearngame.utils.Constant.LETTERS
import com.example.abclearngame.utils.Constant.LETTER_POSITION
import com.example.abclearngame.utils.Constant.LettersList

class LettersListActivity :AppCompatActivity() , LetterAdapter.onLetterClick {

    private lateinit var binding: ActivityLettersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLettersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToFullScreen(this)

        setupRecyclerView(list = LettersList)
        binding.imgClose.setOnClickListener {
            finish()
            Animatoo.animateFade(this)
        }
    }

    private fun setupRecyclerView(list: List<String>) {
        val layoutManager = GridLayoutManager(this,7)
        binding.rvLetter.layoutManager = layoutManager
       val adapter = LetterAdapter(list = list, this,this)
        binding.rvLetter.adapter = adapter

    }

    override fun LetterClick(data: String, position:Int) {
        val bundle = Bundle()
        bundle.putString(LETTERS, data)
        bundle.putInt(LETTER_POSITION, position)
       goToActivityBundle<TracingActivity>(bundle)
    }
}