package com.commons.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


 

public class ToolUtil {
	private static Log log = LogFactory.getLog(ToolUtil.class);
	
	/**
	 * 
	 * @param array
	 * @return
	 */
	public static List<Object> ArrayToList(Object[] array){
		List<Object> list = new ArrayList<Object>();
		try {
			if(array!=null&&array.length>0){
				 for(int i=0;i<array.length;i++){
					 list.add(array[i]);
				 }
			}
		} catch (Exception e) {
			log.error("",e);
			list = null;
		}
		return list ;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Object[] ListToArray(List<Object> list){
		Object[] arry = {};
		try {
			if(list!=null&&list.size()>0){
				for(int i=0 ;i<list.size();i++){
					 arry[i]=list.get(i);
				}
			}
		} catch (Exception e) {
			log.error("",e);
			list = null;
		}
		return arry ;
	}
	
	/**
	 * 
	 * @param array
	 * @param flag
	 * @return
	 */
	public static String ArrayStringToString(Object[] array,String flag){
		StringBuffer result = new StringBuffer();
		try {
			if(array!=null&&array.length>0){
				 for(int i=0;i<array.length;i++){
					 if(i+1!=array.length){
						 result.append(array[i]).append(flag);
					 }else{
						 result.append(array[i]);
					 }
				 }
			}
		} catch (Exception e) {
			log.error("",e);
			result = null;
		}
		return result.toString() ;
	}
	
	/**
	 * 
	 * @param list
	 * @param flag
	 * @return
	 */
	public static String ListStringToString(List<String> list,String flag){
		StringBuffer result = new StringBuffer();
		try {
			if(list!=null&&list.size()>0){
				 
				 for(int i=0;i<list.size();i++){
					 if(i+1==list.size()){
						 result.append(list.get(i));
						 
					 }else {
						 result.append(list.get(i)).append(flag);
						
					}
					 
				 }
			}
		} catch (Exception e) {
			log.error("",e);
			result = null;
		}
		return result.toString() ;
	}
	
	
	
	/**
	 * 
	 * @param strs
	 * @param flag
	 * @return
	 */
	public static String[] StringToArryString(String strs,String flag){
		List<String> list =StringToListString(strs,flag);
		try {
			 if(list!=null){
				 int size = list.size();
				 String[] arg = new String[size];
				 for(int i =0;i<size;i++){
					 arg[i]=list.get(i);
				 }
				 return arg;
			 }
			 
		} catch (Exception e) {
			log.error("",e);
		}
		return  null;
	}
	
	public static String  getClassPath(Class<?> clazz,String key){
		String user_dir = System.getProperty("user.dir");
		String tmp_path = user_dir+"/config" ;
		File file = new File(tmp_path);
		if(!file.exists()){
			tmp_path = user_dir+"/src/main/resources/"+key ;
		}else{
			URL url = clazz.getResource("/");
			if(url!=null){
				file = new File(url.getPath());
				tmp_path = file.getPath()+"/"+key;
				File tmp_file = new File(tmp_path);
				if(tmp_file.exists()){
					tmp_path = file.getPath()+"/"+key;
				}else {
					tmp_path = file.getParent()+"/"+key;
				}
				
			}else{
				tmp_path = user_dir +"/"+key;
			}
			 
		}
		return tmp_path;
	}
	
	/**
	 * 
	 * @param strs
	 * @param flag
	 * @return
	 */
	public static List<String> StringToListString(String strs,String flag){
		List<String> result = new ArrayList<String>();
		try {
			String[] tmp = {};
			if(strs!=null&&!"".equals(strs.trim())){
				tmp = strs.split(flag);
			}
			if(tmp!=null&&tmp.length>0){
				for(int i=0;i<tmp.length;i++){
					if(!"".equals(tmp[i].trim())){
						result.add(tmp[i].trim());	
					}
				}
			}
		} catch (Exception e) {
			log.error("",e);
			result = null;
		}
		return result ;
	}
	
	
	public static  void main(String[] args){
		getClassPath(ToolUtil.class,"");
/*		String str ="1,2 , 3, ,4, 5 ,,,,6" ;
		
		List<String> list =StringToListString(str,",");
		System.out.println(ListStringToString(list,","));
		
	    String[] array = StringToArryString(str,",");
		System.err.println(ArrayStringToString(array,","));*/
		
	}
}
