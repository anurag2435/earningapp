package com.example.earninapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.earninapp.R
import com.example.earninapp.databinding.FragmentProfileBinding
import com.example.earninapp.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    var isExpand = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.imageButton.setOnClickListener {
            if (isExpand) {
                binding.expandableconstraintlayout.visibility = View.VISIBLE
                binding.imageButton.setImageResource(R.drawable.arrowup)
            } else {
                binding.expandableconstraintlayout.visibility = View.GONE
                binding.imageButton.setImageResource(R.drawable.downarrow)
            }
            isExpand = !isExpand
            Firebase.database.reference.child("Users")
                .child(Firebase.auth.currentUser!!.uid)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var user = snapshot.getValue<User>()

                                binding.Name.text = user?.name
                                binding.nameUp.text = user?.name
                                binding.Passsword.text = user?.passworD
                                binding.Email.text = user?.email
                                binding.Age.text = user?.age.toString()
                            }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    }
                )
        }
            // Inflate the layout for this fragment
            return binding.root
        }

        companion object {

        }
    }
