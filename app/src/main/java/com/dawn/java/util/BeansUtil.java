package com.dawn.java.util;

import android.os.Build;

import com.dawn.java.ui.MyApplication;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BeansUtil {
    private static boolean getLanguageIsZh() {
        Locale systemLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            systemLocale = MyApplication.mContext.getResources().getConfiguration().getLocales().get(0);
        } else {
            systemLocale = MyApplication.mContext.getResources().getConfiguration().locale;
        }
        return systemLocale.getLanguage().equals("zh");
    }

}
