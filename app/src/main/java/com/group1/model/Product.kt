package com.group1.model

class Product (var productId: String,
               var productName: String,
               var productDescription: String,
               var productStatus: String,
               var price: Double,
               var imageName: String,
               var material: String,
               var manufacturer: String,
               var brand: String,
               var department: String,
               var minQtyPurchase: Int,
               var washingInstruction: String) {
    constructor() : this("", "", "", "", 0.00, "", "", "", "", "", 0, "")
}