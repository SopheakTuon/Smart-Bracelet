//package com.example.android.bluetoothlegatt.ble_service;
//
///**
// * @author Sopheak Tuon
// * @created on 04-Oct-17
// */
//
//import android.os.AsyncTask;
//
//import com.blueware.agent.android.api.v2.TraceFieldInterface;
//import com.blueware.agent.android.tracing.TraceMachine;
//import com.blueware.agent.android.tracing.TracingInactiveException;
//
//import java.util.concurrent.Executor;
//
//public class AsyncTaskInstrumentation {
//    @ReplaceCallSite
//    public static final <Params, Progress, Result> AsyncTask execute(AsyncTask<Params, Progress, Result> asyncTask, Params... paramsArr) {
//        try {
//            ((TraceFieldInterface) asyncTask)._nr_setTrace(TraceMachine.getCurrentTrace());
//        } catch (TracingInactiveException e) {
//        } catch (NoSuchFieldError e2) {
//        }
//        return asyncTask.execute(paramsArr);
//    }
//
//    @ReplaceCallSite
//    public static final <Params, Progress, Result> AsyncTask executeOnExecutor(AsyncTask<Params, Progress, Result> asyncTask, Executor executor, Params... paramsArr) {
//        try {
//            ((TraceFieldInterface) asyncTask)._nr_setTrace(TraceMachine.getCurrentTrace());
//        } catch (TracingInactiveException e) {
//        } catch (NoSuchFieldError e2) {
//        }
//        return asyncTask.executeOnExecutor(executor, paramsArr);
//    }
//}
