package com.example.earninapp.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earninapp.Withdrawal
import com.example.earninapp.databinding.FragmentSpinBinding
import com.example.earninapp.model.HistoryModelClass
import com.example.earninapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.Random

class SpinFragment : Fragment() {
    private lateinit var binding: FragmentSpinBinding
    private lateinit var timer: CountDownTimer
    private var itemTitles = arrayOf("100", "Try Again", "500", "Try Again", "200", "Try Again")
    var currentChance = 0L
    var currentCoins = 0L
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSpinBinding.inflate(inflater, container, false)
        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue<User>()

                        binding.Name.text = user?.name
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        currentChance = snapshot.getValue() as Long
                        binding.spinchance.text = (snapshot.getValue() as Long).toString()
                    }
                    else{
                        var temp = 0
                        binding.spinchance.text = temp.toString()
                        binding.Spin.isEnabled = false
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        currentCoins = snapshot.getValue() as Long
                        binding.CoinWithdrawal3.text = currentCoins.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return binding.root
    }
    private fun showResult(itemTitle: String, spin: Int){
        if(spin % 2 == 0){
            var winCoin = itemTitle.toInt()
            Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid).setValue(winCoin + currentCoins)
            var historyModelClass = HistoryModelClass(System.currentTimeMillis().toString(), winCoin.toString(),false)
            Firebase.database.reference.child("PlayerCoinHistory").child(Firebase.auth.currentUser!!.uid)
                .push()
                .setValue(historyModelClass)
            binding.CoinWithdrawal3.text = (winCoin + currentCoins).toString()
        }
        Toast.makeText(requireContext(), itemTitle, Toast.LENGTH_SHORT).show()
        currentChance = currentChance - 1
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance)
        binding.Spin.isEnabled = true // Enable the button again
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.CoinWithdrawal2.setOnClickListener{
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
        binding.CoinWithdrawal3.setOnClickListener{
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }

        binding.Spin.setOnClickListener{
            binding.Spin.isEnabled = false // Disable the button while wheel is Spinning
            if(currentChance > 0){
                val spin = Random().nextInt(6) // Generate a random value between 0 and 5
                val degrees = 60f * spin // Calculate the rotation degree based on the random value

                timer = object : CountDownTimer(5000, 50){
                    var rotation = 0f
                    override fun onTick(millisUntilFinished: Long) {
                        rotation += 5f // rotate the wheel
                        if(rotation >= degrees){
                            rotation = degrees
                            timer.cancel()
                            showResult(itemTitles[spin], spin)
                        }
                        binding.Wheel.rotation = rotation
                    }
                    override fun onFinish() {}
                }.start()
            }else{
                Toast.makeText(activity, "Out of spin chance",Toast.LENGTH_SHORT).show()
            }

        }
    }
}
