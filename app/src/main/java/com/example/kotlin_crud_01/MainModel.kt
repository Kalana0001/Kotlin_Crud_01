package com.example.kotlin_crud_01

data class MainModel(
    var name: String = "",
    var price: Int = 0,
    var phone: Int = 0,
    var turl: String = ""
) {

    constructor() : this("", 0, 0, "")
}
