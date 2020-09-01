package com.xter.slimnotek.util

import android.content.Context
import android.graphics.Typeface

/**
 * @Author XTER
 * @Date 2020/8/25 10:14
 * @Description
 */
object FontLoader {

    private var minijianzhunyuan: Typeface? = null

    fun getMNJZY(context: Context): Typeface? {
        if (minijianzhunyuan == null) {
            minijianzhunyuan = Typeface.createFromAsset(context.assets, "fonts/MiNiJianZhunYuan.ttf")
        }
        return minijianzhunyuan
    }

}

