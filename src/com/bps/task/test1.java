package com.bps.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

/**
 * @author HeJin
 *
 */
public class test1 {
	
	// 模拟了100米赛跑，10名选手已经准备就绪，只等裁判一声令下。当所有人都到达终点时，比赛结束。
	   public static void main(String[] args) {
		   Map<String, String> usersMap= new HashMap<String, String>();
		   String redisValue1="{\"dy0021\":\"6\", \"dy0021\":\"7\"}";
		   String redisValue2="{dy0021='M', dy0021='F'}";
			if(redisValue1!=null){
				usersMap= JSONObject.fromObject(redisValue2);
			}
	   }
	   /**
		   * 多线程代码块
	   public void runable() throws InterruptedException{
		   Runnable myRunnable1 =new MyRunnable1(); // 创建一个Runnable实现类的对象
		   Runnable myRunnable2 =new MyRunnable2(); // 创建一个Runnable实现类的对象
		   Runnable myRunnable3 =new MyRunnable3(); // 创建一个Runnable实现类的对象
           Thread thread1 = new Thread(myRunnable1); // 将myRunnable作为Thread target创建新的线程
           Thread thread2 = new Thread(myRunnable2);
           Thread thread3 = new Thread(myRunnable3);
           thread1.start(); // 调用start()方法使得线程进入就绪状态
           thread2.start();
           thread3.start();
           Thread.sleep(10);  
          System.out.println("9999999999999999999999999999999999999999999999999999");
	   }
	   */
	   /**
		 * 部门路径拆分组、中心、大区代码块
	   private void groupBillingDataByExcpBatchCode(List<String> strlist){
		   ArrayList<String> newList = new ArrayList<String>();
	   		 for(String s: strlist){
	   			 System.out.println(s);
	   		     if(Collections.frequency(newList, s) < 1) newList.add(s);
	   		 }
	   		 System.out.println("newList.size():"+newList.size());
	   		 for(String s: newList){
	   			 System.err.println(s);
	   		 }
	   		 ArrayList<String> four_pathList = new ArrayList<String>();
	   		 Map<String, String> four_map=new HashMap<String, String>();
	   		 ArrayList<String> three_pathList = new ArrayList<String>();
	   		Map<String, String> three_map=new HashMap<String, String>();
	   		 ArrayList<String> two_pathList = new ArrayList<String>();
	   		Map<String, String> two_map=new HashMap<String, String>();
	   		 for(String s: strlist){
	   			 String[] split=s.split("_");
	   			if(split.length==4){
	   				String qu_path=split[0]+"_"+split[1]+"_"+split[2]+"_"+split[3]+"_";
	   				 if(Collections.frequency(four_pathList, qu_path) < 1) four_pathList.add(qu_path);
	   			}
	   			if(split.length==3){
	   				String qu_path=split[0]+"_"+split[1]+"_"+split[2]+"_";
	   				 if(Collections.frequency(three_pathList, qu_path) < 1) three_pathList.add(qu_path);
	   			}
	   			if(split.length==2){
	   				String qu_path=split[0]+"_"+split[1]+"_";
	   				 if(Collections.frequency(two_pathList, qu_path) < 1) two_pathList.add(qu_path);
	   			}
	   		 }
	   }
	   */
	   class MyRunnable1 implements Runnable {
		      private int i = 0;
		  
		      @Override
		      public void run() {
		          for (i = 0; i < 5; i++) {
		              System.out.println(Thread.currentThread().getName() + " 1111111111" + i);
		          }
		      }
		 }
	   class MyRunnable2 implements Runnable {
		      private int i = 0;
		  
		      @Override
		      public void run() {
		          for (i = 0; i < 5000; i++) {
		              System.out.println(Thread.currentThread().getName() + "22222222222222 " + i);
		          }
		      }
		 }
	   class MyRunnable3 implements Runnable {
		      private int i = 0;
		  
		      @Override
		      public void run() {
		          for (i = 0; i < 5; i++) {
		              System.out.println(Thread.currentThread().getName() + "333333333333 " + i);
		          }
		      }
		 }
}
