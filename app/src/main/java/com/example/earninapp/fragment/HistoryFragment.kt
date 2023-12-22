package com.example.earninapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earninapp.R
import com.example.earninapp.Withdrawal
import com.example.earninapp.adaptor.HistoryAdaptor
import com.example.earninapp.databinding.FragmentHistoryBinding
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
import java.util.Collections


class HistoryFragment : Fragment() {
    val binding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }
    lateinit var adaptor: HistoryAdaptor
private var listHistory  = ArrayList<HistoryModelClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.reference.child("PlayerCoinHistory").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var listHistory1  = ArrayList<HistoryModelClass>()
                    for(datasnapshot in snapshot.children){
                        var data = datasnapshot.getValue(HistoryModelClass::class.java)
                        listHistory1.add(data!!)
                    }
                    Collections.reverse(listHistory1)
                    listHistory.addAll(listHistory1)
                    adaptor.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
//                     TODO("Not yet implemented")
                }

            })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.CoinWithdrawal6.setOnClickListener{
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
        binding.CoinWithdrawal7.setOnClickListener{
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
        binding.HistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adaptor = HistoryAdaptor(listHistory)
        binding.HistoryRecyclerView.adapter = adaptor
        binding.HistoryRecyclerView.setHasFixedSize(true)
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
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
    }
}