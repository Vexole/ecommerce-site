package com.group1.model

class LineItem (var itemId: String, var price: Double, var qty: Int) {
    constructor() : this("", 0.00, 0)
}