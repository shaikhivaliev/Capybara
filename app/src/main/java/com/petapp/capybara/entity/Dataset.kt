package com.petapp.capybara.entity

import com.google.gson.annotations.SerializedName

data class Dataset(
    @SerializedName("IdentificationNumber") val identificationNumber: String,
    @SerializedName("CategoryCaption") val categoryCaption: String,
    @SerializedName("DepartmentCaption") val departmentCaption: String
)