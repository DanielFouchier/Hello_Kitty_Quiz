package com.example.hellokittyquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.hellokittyquiz.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER_IS_TRUE = "com.example.hellokittyquiz.answer_is_true"
private const val EXTRA_ANSWER_SHOWN = "com.example.hellokittyquiz.answer_shown"


class CheatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCheatBinding
    private var answerIsTrue = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)



        binding.showAnswerButton.setOnClickListener{
            cheatArray[index] = true
            val answerText = when{
                answerIsTrue -> R.string.true_string
                else -> R.string.false_string
            }
            binding.answerText.setText(answerText)
            setAnswerShownResult(true)
        }



    }

    private fun setAnswerShownResult(isAnswerShown: Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
            setResult(Activity.RESULT_OK, data)

     }


    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }

    }
    }
