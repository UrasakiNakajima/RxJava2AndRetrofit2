package com.mobile.mine_module.presenter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

interface IMinePresenter {

    fun mineData(fragment: Fragment, bodyParams: Map<String, String>)

    fun mineDetails(bodyParams: Map<String, String>)

    fun userData(appCompatActivity: AppCompatActivity, bodyParams: Map<String, String>)
}