/** 
 * Project Name:advertise-common <br>
 * File Name:ProbabilityUtils.java <br>
 * Copyright (c) 2017, babytree-inc.com All Rights Reserved. 
 */
package com.ch.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

/**
 * 基于随机数的概率计算工具类
 * @date 2017年7月13日 下午2:04:50
 */
public class ProbabilityUtils {
	
	/**
	 * 根据传入的概率，判断当前这次是否命中
	 * 该方法是基于随机数的
	 * 
	 * @param probability
	 * @return
	 */
	public static boolean hit(double probability){
		if(probability==0) {
			return false;
		}
		if(probability==1){
			return true;
		}
		int a  = (int)(probability*10000);
		int rnd = RandomUtils.nextInt(10000);
		if(rnd<=a){
			return true;
		}
		return false;
	} 
	
	/**
	 * 根据传入的集合中指定的某事件出现概率权重计算出此轮出现的事件代表的key
	 * 
	 * @param collections &lt;代表某一事件的key, key对应的事件出现的概率权重&gt;
	 * @param rnd 若该值为小于0，则重新生成随机数，重新根据概率计算命中元素，若该值大于等于0则直接根据所在范围命中指定元素
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T hitWithoutHitNum(Map<T, Integer> collections, int rnd){
		if(collections == null || collections.size()<1){
			return null;
		}
		if(collections.size()<2){
			return (T) collections.keySet().toArray()[0];
		}
		Set<Map.Entry<T, Integer>> entries = collections.entrySet();
		int sumWeight = 0;
		for(Map.Entry<T, Integer> entry : entries){
			sumWeight += entry.getValue();
			entry.setValue(sumWeight);
		}
		if(rnd<0){
			rnd = RandomUtils.nextInt(sumWeight);
		}
		T hitEle = search(new ArrayList<Map.Entry<T, Integer>>(entries) , rnd);
		
		return hitEle;
	}
	
	/**
	 * 根据传入的集合中指定的某事件出现概率权重计算出此轮出现的事件代表的key
	 * 
	 * @param collections &lt;代表某一事件的key, key对应的事件出现的概率权重&gt;
	 * @param rnd 若该值为小于0，则重新生成随机数，重新根据概率计算命中元素，若该值大于等于0则直接根据所在范围命中指定元素
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Hit> T hit(Map<T, Integer> collections, int rnd){
		if(collections == null || collections.size()<1){
			return null;
		}
		if(collections.size()<2){
			return (T) collections.keySet().toArray()[0];
		}
		Set<Map.Entry<T, Integer>> entries = collections.entrySet();
		int sumWeight = 0;
		for(Map.Entry<T, Integer> entry : entries){
			sumWeight += entry.getValue();
			entry.setValue(sumWeight);
		}
		if(rnd<0){
			rnd = RandomUtils.nextInt(sumWeight);
		}
		T hitEle = search(new ArrayList<Map.Entry<T, Integer>>(entries) , rnd);
		hitEle.setHitNum(rnd);
		
		return hitEle;
	}
	
	/**
	 * 在区间列表中查找rnd落在的区间范围(左包含)
	 * @param entries
	 * @param rnd
	 * @return
	 */
	private static <T> T search(List<Map.Entry<T, Integer>> entries, int rnd){
		int mid = entries.size()/2;
		if(mid < 1){
			return entries.get(0).getKey();
		}
		Map.Entry<T, Integer> curEntry = entries.get(mid);
		Map.Entry<T, Integer> preEntry = entries.get(mid-1);
		Integer curV = curEntry.getValue();
		Integer preV = preEntry.getValue();
		if(preV<=rnd && rnd<curV){
			return curEntry.getKey();
		}
		if(rnd>=curV){
			return search(entries.subList(mid, entries.size()), rnd);
		}
		if(rnd<preV){
			return search(entries.subList(0, mid), rnd);
		}
		return null;
	}
	/**
	 * 用于验证测试界面指定固定流量获取数据
	 * @param <T>
	 * @param collections   --广告集合
	 * @param fetchCount	--	抓取数量
	 * @param fixRatio  --指定的流量参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> multiHit(Map<T, Integer> collections, int fetchCount,String fixRatio){
		if(collections == null || collections.size()<1 || fetchCount<1){
			return null;//EmptyObject.EMPTY_LIST;
		}
		if(collections.size()<=fetchCount){
			return new ArrayList<T>(collections.keySet());
		}
		List<T> hitUnit = new ArrayList<T>(fetchCount);
		List<T> allUnit = new LinkedList<T>();
		Map<T, List<T>> entityUnitMap = new HashMap<T, List<T>>(collections.size());
		
		for(Map.Entry<T, Integer> entry : collections.entrySet()){
			T entryKey = entry.getKey();
			int entryValue = entry.getValue();
			List<T> entityUnit = new ArrayList<T>(entryValue);
			for(int i=0;i<entryValue;i++){
				entityUnit.add(entryKey);
				allUnit.add(entryKey);
			}
			entityUnitMap.put(entryKey, entityUnit);			
		}
		int rand = Integer.parseInt(fixRatio);
		while(fetchCount-->0){
			T hit = allUnit.get(rand);
			hitUnit.add(hit);
			allUnit.removeAll(entityUnitMap.get(hit));
		}
		
		return hitUnit;
	}
	/**
	 * 根据传入的集合以及需要按照条件随机命中的个数，返回fetchCount个实体key
	 * 
	 * @param collections  &lt;key, key对应的实体的出现概率(推荐按照百分比算)&gt;
	 * @param fetchCount  每次需要取得的命中实体个数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> multiHit(Map<T, Integer> collections, int fetchCount){
		if(collections == null || collections.size()<1 || fetchCount<1){
			return null;
		}
		if(collections.size()<=fetchCount){
			return new ArrayList<T>(collections.keySet());
		}
		List<T> hitUnit = new ArrayList<T>(fetchCount);
		List<T> allUnit = new LinkedList<T>();
		Map<T, List<T>> entityUnitMap = new HashMap<T, List<T>>(collections.size());
		
		for(Map.Entry<T, Integer> entry : collections.entrySet()){
			T entryKey = entry.getKey();
			int entryValue = entry.getValue();
			List<T> entityUnit = new ArrayList<T>(entryValue);
			for(int i=0;i<entryValue;i++){
				entityUnit.add(entryKey);
				allUnit.add(entryKey);
			}
			entityUnitMap.put(entryKey, entityUnit);
		}
		while(fetchCount-->0){
			int rand = RandomUtils.nextInt(allUnit.size());
			T hit = allUnit.get(rand);
			hitUnit.add(hit);
			allUnit.removeAll(entityUnitMap.get(hit));
		}
		
		return hitUnit;
	}
	
	/*public static void main(String[] args){
		List<Prd> prdList = new ArrayList<Prd>(50);
		prdList.add(new Prd(1L, new BigDecimal("1.1")));
		prdList.add(new Prd(2L, new BigDecimal("1.11")));
		prdList.add(new Prd(3L, new BigDecimal("1.4")));
		prdList.add(new Prd(4L, new BigDecimal("1.8")));
		prdList.add(new Prd(5L, new BigDecimal("1.9")));
		prdList.add(new Prd(6L, new BigDecimal("2.2")));
		prdList.add(new Prd(7L, new BigDecimal("2.3")));
		prdList.add(new Prd(8L, new BigDecimal("2.4")));
		prdList.add(new Prd(9L, new BigDecimal("2.9")));
		prdList.add(new Prd(10L, new BigDecimal("3.2")));
		
		Map<Long, Integer> collections = new HashMap<Long, Integer>();
		BigDecimal sum = BigDecimal.ZERO;
		for(Prd p : prdList){
			sum = sum.add(p.getPrice());
		}
		BigDecimal lastNum = BigDecimal.ZERO;
		int delta = 1;
		for(Prd p : prdList){
			BigDecimal curNum = p.getPrice();
			if(curNum.compareTo(lastNum)!=0){
				delta += 1;
			}
			lastNum = curNum;
			collections.put(p.getId(), 
					p.getPrice().divide(sum, 3, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).intValue() + delta);
		}
		
		Map<Long, Integer> countMap = new HashMap<Long, Integer>();
		
		int loopCount = 10000; 
		long s = System.currentTimeMillis();
		while(loopCount-->0){
			List<Long> hitUnits = hit(collections, 3);
			for(Long id : hitUnits){
				Integer i = countMap.get(id);
				if(i==null){
					countMap.put(id, 1);
				}else{
					countMap.put(id, i+1);
				}
			}
		}
		long e = System.currentTimeMillis();
		System.out.println("use time : " + (e-s));
		for (Map.Entry<Long, Integer> entry : countMap.entrySet()) {
            System.out.println(entry.getKey() + ", count=" + entry.getValue());
        }
	}
	
	static class Prd {
		private long id;
		private BigDecimal price;
		
		public Prd(long id, BigDecimal price) {
			super();
			this.id = id;
			this.price = price;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
	}*/
	
	public static interface Hit{
		void setHitNum(int hit);
	}
}
