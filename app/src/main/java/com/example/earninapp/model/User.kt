package com.example.earninapp.model

class User {
     var name = ""
     var age = 0
     var email = ""
     var passworD = ""
   constructor()
    constructor(name: String, age: Int, email: String, passworD: String) {
        this.name = name
        this.age = age
        this.email = email
        this.passworD = passworD
    }
}