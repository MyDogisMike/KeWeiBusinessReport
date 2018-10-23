package com.bps.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.Jedis;

/**
 * 封装redis 缓存服务器服务接口
 * @author HeJin
 *
 */
public class RedisUtil {
	public static String  M_MANAGER_FILE="M_MANAGER_FILE";// :管理员层次男声录音文件信息
	public static String  F_MANAGER_FILE="F_MANAGER_FILE";// :管理员层次女声录音文件信息
	public static String  M_GROUP_FILE="M_GROUP_FILE";// :业务组层次女声录音文件信息
	public static String  F_GROUP_FILE="F_GROUP_FILE";// :业务组层次女声录音文件信息
	public static String  AGENT_FILE="AGENT_FILE";// :坐席自制录音文件信息
	
	public static String BPS_CENTER="BPS_CENTER";	//保存所有中心信息
	public static String BPS_GROUP="BPS_GROUP";	//保存各中心下的小组信息信息
	public static String BPS_RATE="BPS_RATE";	//保存各各业务的费率
	 
	//操作redis客户端
    public static Jedis jedis;
    @Autowired
    @Qualifier("jedisConnectionFactory")
    private JedisConnectionFactory jedisConnectionFactory;
    /**
     * 获取一个jedis 客户端
     * @return
     */
    public Jedis getJedis(){
        if(jedis == null){
            return jedisConnectionFactory.getShardInfo().createResource();
        }
        return jedis;
    }
  /**
  * @author andalee 20170407 把list对象序列化，再存入redis
  * 存储record_id 龙行有，但ipcc 还没存入的 invokeId集合
  * @param key
  * @return
  */
 public  void setList(String key,List<String> value){  
	 this.getJedis().set(key.getBytes(), serialize(value));
 }
/**
* @author andalee 20170407 根据key取到数据流，然后转换成list，再存入redis
* 获取 record_id 龙行有，但ipcc 还没存入的 invokeId集合
* @param key
* @return
*/
public List<String> getList(String key){
  byte[] in =  this.getJedis().get(key.getBytes());  
  List<String> value =(List<String>) deserialize(in);
  return value;
}
  public static byte[] serialize(Object value) {  
  if (value == null) {  
      throw new NullPointerException("Can't serialize null");  
  }  
  byte[] rv=null;  
  ByteArrayOutputStream bos = null;  
  ObjectOutputStream os = null;  
  try {  
      bos = new ByteArrayOutputStream();  
      os = new ObjectOutputStream(bos);  
      os.writeObject(value);  
      os.close();  
      bos.close();  
      rv = bos.toByteArray();  
  } catch (Exception e) {  
      throw new IllegalArgumentException("Non-serializable object", e);  
  } finally { 
  	try {
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  return rv;  
} 
public static Object deserialize(byte[] in) {  
	            Object rv=null;  
	            ByteArrayInputStream bis = null;  
	            ObjectInputStream is = null;  
	            try {  
	                if(in != null) {  
	                    bis=new ByteArrayInputStream(in);  
	                    is=new ObjectInputStream(bis);  
	                    rv=is.readObject();  
	                    is.close();  
	                    bis.close();  
	                }  
	            } catch (Exception e) {  
	            	e.printStackTrace();
	            } finally {  
	            	try {
						bis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }  
	            return rv;  
	        } 
//  /**
//  * 添加key value
//  * @param key
//  * @param value
//  */
// public void set(String key,String value){
//     this.getJedis().set(key, value);
// }
// /**添加key value (字节)(序列化)
//  * @param key
//  * @param value
//  */
// public void set(byte [] key,byte [] value){
//     this.getJedis().set(key, value);
// }
// 
// /**添加key  hashMap 
//  * @param key
//  * @param value
//  */
// public  void setHashMap(String key,HashMap<String, String> value){  
// 	 this.getJedis().hmset(key, value);
// }
///**添加hash值
//* @param key
//* @param value
//*/
//public  void hset(String key,String dbkey ,String value){  
//	 this.getJedis().hset(key,dbkey, value);
//}
//
//
// /**
//  * 获取redis value (String)
//  * @param key
//  * @return
//  */
// public String get(String key){
//     String value = this.getJedis().get(key);
//     return value;
// }
// /**
//  * 获取redis value (byte [] )(反序列化)
//  * @param key
//  * @return
//  */
// public byte[] get(byte [] key){
//     return this.getJedis().get(key);
// }
// /**
//  * @author andalee 20170407 
//  * 获取redis value (String)
//  * @param key
//  * @return
//  */
// public HashMap<String, String>  getHashMap(String key){
//// 	//迭代redis的key取出所有key和值，再重新装入HashMap
// 	HashMap value=new HashMap();
//	     Iterator<String> iter=this.getJedis().hkeys(key).iterator();  
//	     while (iter.hasNext()){  
//	         String map_key = iter.next();  
//	         value.put(map_key, this.getJedis().hmget(key,map_key));  
//	     } 
//// 	byte[] in = get(key.getBytes());  
//// 	HashMap<String,Object> value =(HashMap<String,Object>) deserialize(in);
// 	
//     return value;
// }
// /**
//  * @author andalee 20170407 
//  * 获取redis value (String)
//  * @param key
//  * @return
//  */
// public String hget(String key,String map_key){
// 	
//     return  this.getJedis().hget(key, map_key);
// }   
//    /**
//     * 通过key删除（字节）
//     * @param key
//     */
//    public void del(byte [] key){
//        this.getJedis().del(key);
//    }
//    /**
//     * 通过key删除
//     * @param key
//     */
//    public void del(String key){
//        this.getJedis().del(key);
//    }
//
//    /**
//     * 添加key value 并且设置存活时间(byte)
//     * @param key
//     * @param value
//     * @param liveTime
//     */
//    public void set(byte [] key,byte [] value,int liveTime){
//        this.set(key, value);
//        this.getJedis().expire(key, liveTime);
//    }
//    /**
//     * 添加key value 并且设置存活时间
//     * @param key
//     * @param value
//     * @param liveTime
//     */
//    public void set(String key,String value,int liveTime){
//        this.set(key, value);
//        this.getJedis().expire(key, liveTime);
//    }
//    /**
//     * 通过正则匹配keys
//     * @param pattern
//     * @return
//     */
//    public Set<String> keys(String pattern){
//        return this.getJedis().keys(pattern);
//    }
//
//    /**
//     * 检查key是否已经存在
//     * @param key
//     * @return
//     */
//    public boolean exists(String key){
//        return this.getJedis().exists(key);
//    }
//    /**
//     * 清空redis 所有数据
//     * @return
//     */
//    public String flushDB(){
//        return this.getJedis().flushDB();
//    }
//    /**
//     * 查看redis里有多少数据
//     */
//    public long dbSize(){
//        return this.getJedis().dbSize();
//    }
//    /**
//     * 检查是否连接成功
//     * @return
//     */
//    public String ping(){
//        return this.getJedis().ping();
//    }
//   
//    private RedisUtil (){
//
//    }

}
