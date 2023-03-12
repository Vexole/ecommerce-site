package com.group1.model

class Product (var modelId: String,
               var modelName: String,
               var modelDescription: String,
               var modelStatus: String,
               var price: Double,
               var imageName: String,
               var operatingSys: String,
               var brand: String,
               var color: String,
               var capacity: String) {
    constructor() : this("", "", "", "", 0.00, "", "", "", "", "")
}