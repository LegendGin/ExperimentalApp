package com.onemt.sdk.xposed;

import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.util.Map;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/25 16:10
 * @see
 */
public class MyXposed implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam != null && "com.machinezone.gow".equals(loadPackageParam.processName)) {
            // 查找要Hook的函数(需要打印堆栈调用的目标函数)
            XposedHelpers.findAndHookMethod(
                    "com.machinezone.gow", // 被Hook函数所在的类com.lenovo.anyshare.frv
                    loadPackageParam.classLoader,
                    "a",     // 被Hook函数的名称a
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // Hook函数之前执行的代码

                            //传入参数1
                            //XposedBridge.log("beforeHookedMethod userName:" + param.args[0]);
                        }

                        @Override
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param)
                                throws Throwable {
                            // Hook函数之后执行的代码

                            //函数返回值
                            //XposedBridge.log("afterHookedMethod result:" + param.getResult());

                            // 函数调用完成之后打印堆栈调用的信息
                            // 方法一:
                            Log.i("Dump Stack: ", "---------------start----------------");
                            Throwable ex = new Throwable();
                            StackTraceElement[] stackElements = ex.getStackTrace();
                            if (stackElements != null) {
                                for (int i = 0; i < stackElements.length; i++) {

                                    Log.i("Dump Stack" + i + ": ", stackElements[i].getClassName()
                                            + "----" + stackElements[i].getFileName()
                                            + "----" + stackElements[i].getLineNumber()
                                            + "----" + stackElements[i].getMethodName());
                                }
                            }
                            Log.i("Dump Stack: ", "---------------over----------------");

                            // 方法二:
//                            new Exception().printStackTrace(); // 直接干脆

                            // 方法三:
//                            Thread.dumpStack(); // 直接暴力

                            // 方法四:
                            // 打印调用堆栈: http://blog.csdn.net/jk38687587/article/details/51752436
                            /*RuntimeException e = new RuntimeException("<Start dump Stack !>");
                            e.fillInStackTrace();
                            Log.i("<Dump Stack>:", "++++++++++++", e);*/

                            // 方法五:
                            // Thread类的getAllStackTraces（）方法获取虚拟机中所有线程的StackTraceElement对象，可以查看堆栈
                            /*for (Map.Entry<Thread, StackTraceElement[]> stackTrace : Thread.getAllStackTraces().entrySet()) {
                                Thread thread = (Thread) stackTrace.getKey();
                                StackTraceElement[] stack = (StackTraceElement[]) stackTrace.getValue();

                                // 进行过滤
                                if (thread.equals(Thread.currentThread())) {
                                    continue;
                                }

                                Log.i("[Dump Stack]", "**********Thread name：" + thread.getName() + "**********");
                                int index = 0;
                                for (StackTraceElement stackTraceElement : stack) {

                                    Log.i("[Dump Stack]" + index + ": ", stackTraceElement.getClassName()
                                            + "----" + stackTraceElement.getFileName()
                                            + "----" + stackTraceElement.getLineNumber()
                                            + "----" + stackTraceElement.getMethodName());
                                }
                                // 增加序列号
                                index++;
                            }*/
                            Log.i("[Dump Stack]", "********************* over **********************");
                        }
                    });
        }
    }
}
