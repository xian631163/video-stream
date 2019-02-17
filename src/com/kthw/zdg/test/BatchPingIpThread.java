package com.kthw.zdg.test;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created
 */
class BatchPingIpThread {
    private Queue<String> allIp; // �?验证的IP
    private int threadNum = 5; // 线程�?
    private static String ipsOK = ""; // 可以ping通的IP
    private static String ipsNO = ""; // 不能ping通的IP

    public static String getIpsNO() {
        return ipsNO;
    }

    public static void setIpsNO(String ipsNO) {
        BatchPingIpThread.ipsNO = ipsNO;
    }

    
    public static String getIpsOK() {
        return ipsOK;
    }

    
    public static void setIpsOK(String ipsOK) {
        BatchPingIpThread.ipsOK = ipsOK;
    }


    public BatchPingIpThread(Queue<String> allIp, int threadNum) {
        this.allIp = allIp;
        this.threadNum = threadNum;
    }


        public void startPing() {
        // 创建�?个线程池，多个线程同步执�?
            ExecutorService executor = Executors.newFixedThreadPool(threadNum);
            for (int i = 0; i < threadNum; i++) {
            executor.execute(new PingRunner());
            }
            
            executor.shutdown();
            try {
                while (!executor.isTerminated()) {
                Thread.sleep(100);
                }
            } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        private class PingRunner implements Runnable {
        private String taskIp = null;
        
        
        @Override
        public void run() {
            try {
                while ((taskIp = getIp()) != null) {
                    InetAddress addr = InetAddress.getByName(taskIp);
                    if (addr.isReachable(5000)) {
                        ipsOK += taskIp + ",";
                    } else {
                        ipsNO += taskIp + ",";
                    }
                   }
                } catch (SocketException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }


    public String getIp() {
        String ip = null;
        synchronized (allIp) {
            ip = allIp.poll();
        }
        return ip;
        }
        }


        public static void main(String[] args) {
            
            Queue<String> allIp = new LinkedList<String>();
            allIp.offer("192.168.1.2");
            allIp.offer("192.168.1.1");
            allIp.offer("192.168.1.3");
            allIp.offer("192.168.1.4");
            allIp.offer("192.168.1.5");
            allIp.offer("192.168.1.6");
            allIp.offer("192.168.0.200");
            allIp.offer("192.168.31.220");
            allIp.offer("192.168.104.71");
            allIp.offer("121.11.80.79");
            
            BatchPingIpThread batchPingIpThread = new BatchPingIpThread(allIp, 10);
            batchPingIpThread.startPing();
            System.out.println("ipsOK:" + batchPingIpThread.getIpsOK());
            System.out.print("ipsNO:" + batchPingIpThread.getIpsNO());
      }
}
