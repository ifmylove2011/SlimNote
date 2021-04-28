package com.xter.slimnotek.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

/**
 * @Author XTER
 * @Date 2021/4/28 9:30
 * @Description
 */
fun AppCompatActivity.getNavBackStackEntryCount():Int{
    val navHostFragment = this.supportFragmentManager.fragments.first() as NavHostFragment
    return navHostFragment.childFragmentManager.backStackEntryCount
}