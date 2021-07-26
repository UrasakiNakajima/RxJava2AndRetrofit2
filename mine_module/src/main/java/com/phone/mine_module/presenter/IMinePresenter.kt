package com.phone.mine_module.presenter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

interface IMinePresenter {

    fun mineData(fragment: Fragment, bodyParams: Map<String, String>)

    fun mineData(appCompatActivity: AppCompatActivity, bodyParams: Map<String, String>)

    fun userData(appCompatActivity: AppCompatActivity, bodyParams: Map<String, String>)

    fun userData(appCompatActivity: AppCompatActivity, accessToken: String, bodyParams: Map<String, String>)
}