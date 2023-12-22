package com.example.earninapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.earninapp.databinding.ActivityQuizBinding
import com.example.earninapp.model.Question
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuizActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    var currentChance = 0L
    var currentQuestion = 0
    var score = 0
    private lateinit var questionList: ArrayList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var currentCoins = snapshot.getValue() as Long
                        binding.CoinWithdrawal7.text = currentCoins.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        currentChance = snapshot.value as Long
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        questionList = ArrayList<Question>()

        var image = intent.getIntExtra("categoryimg", 0)
        var catText = intent.getStringExtra("questionType")
        Firebase.firestore.collection("Quizz")
            .document(catText.toString())
            .collection("Questions").get().addOnSuccessListener { questionData ->
                questionList.clear()
                for (data in questionData.documents) {
                    var question: Question? = data.toObject(Question::class.java)
                    questionList.add(question!!)
                }
                if (questionList.size > 0) {
                    binding.question.text = questionList.get(currentQuestion).question
                    binding.optionA.text = questionList.get(currentQuestion).option1
                    binding.optionB.text = questionList.get(currentQuestion).option2
                    binding.optionC.text = questionList.get(currentQuestion).option3
                    binding.optionD.text = questionList.get(currentQuestion).option4
                }
            }
        binding.categoryimg.setImageResource(image)
        binding.CoinWithdrawal6.setOnClickListener {}
        binding.CoinWithdrawal7.setOnClickListener {}
        binding.optionA.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionA.text.toString())
        }
        binding.optionB.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionB.text.toString())
        }
        binding.optionC.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionC.text.toString())
        }
        binding.optionD.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionD.text.toString())
        }
    }

    private fun nextQuestionAndScoreUpdate(s: String) {
        if(s.equals(questionList.get(currentQuestion).ans)){
            score+=10
        }
        currentQuestion++
        if (currentQuestion >= questionList.size) {
            if (score >= (score / (questionList.size * 10)) * 100) {
                binding.win.visibility = View.VISIBLE
                var currentChance= 0L
                Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance+1)
                var isUpdated = false
                if(isUpdated){

                }else {

                }
            } else{
                    binding.loss.visibility = View.VISIBLE
                }
            }
        else {
            binding.question.text = questionList.get(currentQuestion).question
            binding.optionA.text = questionList.get(currentQuestion).option1
            binding.optionB.text = questionList.get(currentQuestion).option2
            binding.optionC.text = questionList.get(currentQuestion).option3
            binding.optionD.text = questionList.get(currentQuestion).option4
        }
    }
}