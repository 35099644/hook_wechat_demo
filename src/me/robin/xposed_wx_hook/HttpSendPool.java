package me.robin.xposed_wx_hook;

import de.robv.android.xposed.XposedBridge;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Lubin.Xuan on 2016/3/14.
 */
public class HttpSendPool {
    private static final Queue<String> dataQueue = new LinkedBlockingQueue<String>();

    public static void addDataToQueue(String gzhData) {
        if (null == gzhData || gzhData.trim().length() < 1) {
            return;
        }
        dataQueue.offer(gzhData);

        synchronized (dataQueue) {
            dataQueue.notify();
        }
    }

    static {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String data = dataQueue.poll();
                    if (null == data) {
                        synchronized (dataQueue) {
                            try {
                                dataQueue.wait();
                                continue;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    XposedBridge.log("数据:" + data);
                }
            }
        });
        thread.setDaemon(false);
        thread.setName("数据回传线程");
        thread.start();
    }
}
