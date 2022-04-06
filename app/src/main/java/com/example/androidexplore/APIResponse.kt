package com.example.androidexplore

import com.google.gson.annotations.SerializedName

class APIResponse {
    @SerializedName("name")
    var name :String? = null

    @SerializedName("age")
    var age:Int? = null
}