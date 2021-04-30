package com.mobile.mine_module.presenter

import androidx.fragment.app.Fragment

interface IMinePresenter {

    fun mineData(fragment: Fragment, bodyParams: Map<String, String>)

    fun mineDetails(bodyParams: Map<String, String>)

}