package com.bps.annotation;

import java.lang.reflect.Field;

import com.bps.dal.object.Entity;




public class GetAnnotation {
	
	 
	 public static void main(String[] args) throws Exception{
		Entity test = new Entity();
		Class<Entity> ga = Entity.class;
		Field[] fields = ga.getDeclaredFields();
	    
	    
		for (int i = 0; i < fields.length; i++) {
			DBField annotation = fields[i].getAnnotation(DBField.class);
			if(annotation!=null){
				System.out.println("fieldName="+annotation.field()+"\ttype="+annotation.type());
			}
		}
	    //获取方法上的所有注解
	    
		
	}

}
