package com.group1.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CartUtils {
    companion object {
        fun getCart(userId: String): DatabaseReference {
            return FirebaseDatabase.getInstance().getReference("cart")
                .child(userId)
        }
    }
}