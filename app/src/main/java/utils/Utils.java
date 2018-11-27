package utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.example.yivintest.YivinTest;

import io.rong.calllib.RongCallSession;

public class Utils {
    private static Utils utils;

    private Utils() {

    }

    public static Utils getInstence() {
        if (utils == null) {
            utils = new Utils();
        }
        return utils;
    }


    //今日头条适配方案，在 Activity#onCreate 方法中调用下；
    private static float sNoncompatDesity;
    private static float sNoncompatScaledDensity;

    private static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application, @NonNull int designDpi,@NonNull int reference) {
        //获取到手机真实的分辨率
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        //字体sp大小适配
        if (sNoncompatDesity == 0) {
            sNoncompatDesity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            //监听字体大小修改
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }
                @Override
                public void onLowMemory() {
                }
            });
        }
        //designDpi为设计图的标准，单位为dp，设计图按照宽度优先设计；如果高度优先则换成heightPixels
        // 1为宽维度，2为高维度
        float targetDensity = 0;
        if(reference == 1){
            targetDensity = appDisplayMetrics.widthPixels / designDpi;
        }else if(reference == 2){
            targetDensity = appDisplayMetrics.heightPixels / designDpi;
        }
        final float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDesity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;

    }
}
