package com.alarm;


import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.RejectedExecutionHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.notify.NotifyMessage;
import com.notify.NotifyService;






/**
 * 每一个key只要连续两次超过阈值的时间间隔小于X秒，就报警
 * 
 * 
 * 
 * (1)slowCall：接口执行时间超过@profiled的timeThreshold就累计一次慢调用(slowCall);
 * (2)slowCall次数累计达到配置的阈值【thresholdCount】时，发送一次告警数据(AlarmData);
 * (3)
 * (4)
 * 
 * 
 * 
 * 
 * @author chenqunhui
 *
 */
public class AlarmTimingManager{
	
	private static Logger logger = Logger.getLogger(AlarmTimingManager.class);
	
	private static final long DEFAULT_ALARM_TIME = 10*60*1000;
	
	//public static final long thresholdCount = 1l;//达到N次慢调用后，就发一次预警数据
	
	private long ticketTime = DEFAULT_ALARM_TIME; //两次信号的最大间隔时间，如超过这个值，则第一次信号将被忽略，单位ms
	
	private int  ticketCount = 3; //连续几次不正常就发短信或者邮件
	
	private int failbackCount = 3;//连续几次正常就发短信通知恢复
	
	private int alermSpaceSecond = 5;//发送告警/恢复短信的时间间隔 【分钟】
		
	//private static LinkedBlockingQueue<AlarmData> _queue =  new LinkedBlockingQueue<AlarmData>(1000);
	
	private NotifyService notifyService;  //通知工具
	
	private String notifyMembers;//预警通知人，逗号隔开
	
	private static ConcurrentHashMap<String,Status> lastAlarmTimeMap = new ConcurrentHashMap<String,Status>();
	
	private static ThreadPoolExecutor timeoutChecker = new ThreadPoolExecutor(10, 20, 60l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20,60l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3000),
			new ThreadFactory() {
				private AtomicLong index = new AtomicLong(0);
				@Override
				public Thread newThread(Runnable arg0) {
					Thread thread = new Thread(arg0);
					thread.setName("AlarmTimer-" + index.incrementAndGet());
					thread.setDaemon(true);
					return thread;
				}
			}, new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable r,
						ThreadPoolExecutor executor) {
					//超出部分丢弃 do nothing
				}

			});
	
	
	/**
	 * 发送信号
	 * @param signal
	 */
	public void sendSignal(Signal signal){
		try{
			final Future<?> futureResult = executor.submit(new SignalJob(signal));
			timeoutChecker.submit(new HandleFutureResult(futureResult));
		}catch(Exception e){
			
		}
		
	}
	
	
	static class HandleFutureResult implements Runnable {
		private Future<?> futureResult;
		
		/**
		 * @param futureResult
		 */
		public HandleFutureResult(Future<?> futureResult) {
			this.futureResult = futureResult;
		}
		
		public void run() {
			try {
				futureResult.get(5000, TimeUnit.SECONDS);
			} catch (Exception e) {
				futureResult.cancel(true);
				logger.warn("future get result failed",e);
			}
		}
	}
	
	
	
	
/*	public  void sendAlarmData(AlarmData data){
		try {
			boolean flag=_queue.offer(data,0l,TimeUnit.MILLISECONDS);
			if(!flag){
				logger.warn("method:"+data.getMethod()+"alarmValue:"+data.getAlarmValue()+"offer failed.");
			}
		} catch (InterruptedException e) {
			// 
		}
	}*/
	
	private void checkProperty(){
		if(null ==notifyService){
			logger.error("No notifyService has been found! please make sure you has set it, otherwise you will never receive the alarm!");
		}
		if(StringUtils.isEmpty(notifyMembers)){
			logger.error("No notifyMembers has been found! please make sure you has set it, otherwise you will never receive the alarm!");
		}
	}
	
	
	
	
	
	public void init(){
		checkProperty();
		/*Thread job = new Thread("take-moniter-data-thread"){
			public void run(){
				while(true){
					try {
						AlarmData data =_queue.take();
						MailAlarmJob alarm  = new MailAlarmJob(data);
						if(logger.isDebugEnabled()){
							logger.debug("Catch the alarm:"+JSON.toJSONString(data));
						}
						executor.execute(alarm);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
		};
		job.setDaemon(true);
		job.start();*/
		logger.info("start to take moniter data.");
	}
	
	
	/**
	 * 处理单次信号
	 * @author chenqunhui
	 *
	 */
	class SignalJob implements Runnable{
		Signal signal;
		public SignalJob(Signal signal){
			this.signal = signal;
		}
		
		@Override
		public void run() {
			Status currentStatus = new Status(signal);
			Status lastStatus = lastAlarmTimeMap.putIfAbsent(signal.getKey(),currentStatus);
			if(null == lastStatus){
				//
				return;
			}else{
				//非首次预警
				synchronized(lastStatus){
					Date currentTime = signal.getTime();
					Date lastTime = lastStatus.getLastTime();
					if(currentTime.before(lastTime)){
						//时间乱序的状态不处理
						return;
					}
					if(currentTime.getTime()-lastTime.getTime()> ticketTime){
						//两次通知的时间超过了统计时间，则直接覆盖
						lastStatus.setLastTime(currentTime);
						lastStatus.setNomal(signal.isNomal);
						lastStatus.setAlramTime(currentTime);
						lastStatus.setCount(1);
						return;
					}
					lastStatus.setLastTime(currentTime);
					if(currentStatus.isNomal()){
						//正常信息
						if(lastStatus.isNomal){
							//原状态也为正常时，累计正常次数即可
							lastStatus.setCount(lastStatus.getCount()+1);
						}else{
							lastStatus.setCount(1);
							lastStatus.setNomal(true);
						}
						if(lastStatus.isAlarming && lastStatus.getCount() >=failbackCount){
							//正在告警中，并且恢复次数>=设定次数时，发送恢复信息
							sendFailback(lastStatus,signal);
						}
					}else{
						if(lastStatus.isNomal){
							//原状态为正常
							lastStatus.setCount(1);
							lastStatus.setNomal(false);
							//首次计数时，开始计时
							lastStatus.setAlramTime(signal.getTime());
						}else{
							if(0==lastStatus.getCount()){
								//首次计数时，开始计时
								lastStatus.setAlramTime(signal.getTime());
							}
							lastStatus.setCount(lastStatus.getCount()+1);
						}
						if(lastStatus.getCount()>= ticketCount){
							//预警达到次数就邮件通知，并清零
							if(!lastStatus.isAlarming || currentTime.getTime()- lastStatus.getAlramTime().getTime()> alermSpaceSecond*60*1000){
								sendAlarm(lastStatus,signal);
							}
						}
					}
				}
				
			}
		}
		
	}
	
	private void sendFailback(Status lastStatus,Signal signal){
		NotifyMessage alarm = new NotifyMessage(NotifyMessage.AlarmType.SMS,"恢复信息",signal.toString(),notifyMembers.split(","));
		notifyService.notifyMessage(alarm);
		lastStatus.setAlarming(false);
	}
	
	private void sendAlarm(Status lastStatus,Signal signal){
		NotifyMessage alarm = new NotifyMessage(NotifyMessage.AlarmType.SMS,"预警信息",signal.toString()+" "+lastStatus.toString(),notifyMembers.split(","));
		notifyService.notifyMessage(alarm);
		lastStatus.isAlarming = true;
		lastStatus.alramTime = new Date();
		lastStatus.count = 0;
	}
	
	
/*     class MailAlarmJob implements Runnable{
    	AlarmData data;
    	public MailAlarmJob(AlarmData data){
    		this.data = data;
    	}
    	
		@Override
		public void run() {
			String key = getKey(data);
			Status currentStatus = new Status(data);
			Status lastStatus = lastAlarmTimeMap.putIfAbsent(key,currentStatus);
			if(null == lastStatus){
				if(!currentStatus.isNomal()){
					logger.warn("First monitor warn:"+JSON.toJSONString(currentStatus));
				}
				return;
			}else{
				//非首次预警
				synchronized(lastStatus){
					Date currentTime = currentStatus.getAlarmTime();
					Date lastTime = lastStatus.getAlarmTime();
					if(currentTime.before(lastTime)){
						//时间乱序的状态不处理
						return;
					}
					lastStatus.setAlarmTime(currentTime);
					if(currentStatus.isNomal()){
						//正常信息
						lastStatus.setCount(0);
						lastStatus.setNomal(true);
					}else{
						//如果是报警信息
						int count  = lastStatus.getCount()+1;
						if(count>= ticketCount){
							//预警达到次数就邮件通知，并清零
							sendMail(lastStatus);
							lastStatus.setCount(0);
						}else{
							lastStatus.setCount(count);
						}
					}
				}
				
			}

		}
		
		private void sendMail(Status lastStatus){
			if(StringUtils.isEmpty(notifyMembers)){
				return;
			}
			try{
				AlarmMessage alarm = new AlarmMessage(notifyMessager.getAlarmType(),"接口慢调用预警",lastStatus.toString(),notifyMembers.split(","));
				notifyMessager.notify(alarm);
			}catch(Exception e){
			}
			
		}
		
		private String getKey(AlarmData data){
			return new StringBuilder().append(data.getHost()).append("_").append(data.getMethod()).toString();
		}
		
    }*/
	
     class Status{
    	private Date lastTime;  //记录上次的时间
    	private boolean isNomal; //状态是否正常
    	private long count = 0;   //持续次数
    	private boolean isAlarming;// 是否已发送过告警
    	private Date alramTime = new Date();
/*    	private String host;     //机器ip
    	private String method;   //方法名称
*/		
		/*public Status(AlarmData data){
			//alarmTime = data.getAlarmDate();
			if(data.getAlarmDate().getTime() - data.getLastAlarmDate().getTime() <= ticketTime){
				isNomal = false;
			}
			count =1;
		}
		
		public Status(){
			
		}*/
		public Status(Signal signal){
			this.lastTime = signal.getTime();
			this.alramTime = signal.getTime();
			isNomal = signal.isNomal;
			count = 1;
		}
		

		public boolean isNomal() {
			return isNomal;
		}

		public void setNomal(boolean isNomal) {
			this.isNomal = isNomal;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}
		
		public String toString(){
			long min = (new Date().getTime()-alramTime.getTime())/60000;
			if(0 == min){
				min = 1;
			}
			return new StringBuilder("在").append(min).append("分钟内累计发生").append(count).append("次").toString();
	    }

		public Date getLastTime() {
			return lastTime;
		}

		public void setLastTime(Date lastTime) {
			this.lastTime = lastTime;
		}


		public boolean isAlarming() {
			return isAlarming;
		}


		public void setAlarming(boolean isAlarming) {
			this.isAlarming = isAlarming;
		}


		public Date getAlramTime() {
			return alramTime;
		}


		public void setAlramTime(Date alramTime) {
			this.alramTime = alramTime;
		}

    }

	public String getNotifyMembers() {
		return notifyMembers;
	}

	public void setNotifyMembers(String notifyMembers) {
		this.notifyMembers = notifyMembers;
	}






	public long getTicketTime() {
		return ticketTime;
	}





	public void setTicketTime(long ticketTime) {
		this.ticketTime = ticketTime;
	}





	public int getTicketCount() {
		return ticketCount;
	}





	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}





	public int getFailbackCount() {
		return failbackCount;
	}





	public void setFailbackCount(int failbackCount) {
		this.failbackCount = failbackCount;
	}





	public int getAlermSpaceSecond() {
		return alermSpaceSecond;
	}





	public void setAlermSpaceSecond(int alermSpaceSecond) {
		this.alermSpaceSecond = alermSpaceSecond;
	}





	public NotifyService getNotifyService() {
		return notifyService;
	}





	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}
	
}