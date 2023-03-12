package com.group1.model

class Cart(
    var userId: String,
    var lineItemList: List<LineItem>,
    var total: Double,
    var status: String
) {
    constructor() : this("", mutableListOf(), 0.00, "")
}