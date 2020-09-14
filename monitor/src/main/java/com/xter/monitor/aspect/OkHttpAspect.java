package com.xter.monitor.aspect;

import android.util.Log;

import com.xter.monitor.L;
import com.xter.monitor.annonation.OkHttpTrace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Locale;

/**
 * @Author XTER
 * @Date 2020/9/2 17:05
 * @Description Http切面实体类
 */
@Aspect
public class OkHttpAspect {

    @Around("execution(* com.xter.slimnotek.ui.note.remove())")
    public void timeTrace(ProceedingJoinPoint proceedingJoinPoint) {
        long currentTime = System.currentTimeMillis();
        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long delay = System.currentTimeMillis() - currentTime;
        System.out.println("+++++++ " + delay + "ms +++++++");
    }
}
