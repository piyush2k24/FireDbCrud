package com.piyush2k24.firedbcrud.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.piyush2k24.firedbcrud.Adapter.UserAdapter
import com.piyush2k24.firedbcrud.Holder.UserHolder
import com.piyush2k24.firedbcrud.databinding.ActivityUsersListBinding

class UsersList : AppCompatActivity() {
    private lateinit var binding: ActivityUsersListBinding
    private lateinit var UserList:ArrayList<UserHolder>
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUsersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.RecyclerUserDb.layoutManager=LinearLayoutManager(this)
        binding.RecyclerUserDb.hasFixedSize()
        UserList= arrayListOf<UserHolder>()
        Viewers()
    }

    private fun Viewers(){
        databaseReference=FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(Users in snapshot.children){
                        val UsersData=Users.getValue(UserHolder::class.java)
                        UserList.add(UsersData!!)
                    }
                    val mAdapter= UserAdapter(UserList)
                    binding.RecyclerUserDb.adapter=mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.toString())
            }

        })
    }

    private fun showToast(str : String){
        Toast.makeText(applicationContext,str,Toast.LENGTH_SHORT).show()
    }
}