package com.sagara.myapplication

import com.google.firebase.database.PropertyName

data class Product(
    @get:PropertyName("Id") @set:PropertyName("Id") var id: String = "",
    @get:PropertyName("Name") @set:PropertyName("Name") var name: String = "",
    @get:PropertyName("Price") @set:PropertyName("Price") var price: Double = 0.0,
    @get:PropertyName("Description") @set:PropertyName("Description") var description: String = "",
    @get:PropertyName("Image") @set:PropertyName("Image") var image: String = "",
    @get:PropertyName("Category") @set:PropertyName("Category") var category: String = "",
    @get:PropertyName("Tag") @set:PropertyName("Tag") var tag: String = ""
)
