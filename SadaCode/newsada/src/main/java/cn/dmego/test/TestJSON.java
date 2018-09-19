package cn.dmego.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import cn.dmego.pojo.Column;
import cn.dmego.pojo.Query;

import com.alibaba.fastjson.JSON;

public class TestJSON {
	
	/**
	 * @throws IOException
	 */
	@Test
	public void testjson() throws IOException{
		ClassLoader cl = this.getClass().getClassLoader();
        InputStream inputStream = cl.getResourceAsStream("query-res.json");
        String jsontext = IOUtils.toString(inputStream,"utf8");
        List<Query> querylist =JSON.parseArray(jsontext, Query.class);
        for (Query query : querylist) {
        	System.out.println(query);
		}
 
	}
	
	/**
     * Map转成实体对象
     * @param map map实体对象包含属性
     * @param clazz 实体对象类型
     * @return
     */
    public static Object map2Object(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                String flag = (String) map.get(field.getName());
                if(flag != null){
                	if(flag.equals("false") || flag.equals("true")){
                    	System.out.println(Boolean.parseBoolean(flag));
                    	field.set(obj, Boolean.parseBoolean(flag));
                    }else{
                    	field.set(obj, map.get(field.getName()));
                    }
                }                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return obj;
    }
	public void inputstreamtofile(InputStream ins,File file) throws IOException{
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
		os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
		}
	
	/** 
     * 将 List<Map>对象转化为List<JavaBean> 此方法已通过测试
     * @param type 要转化的类型 
     * @param map 
     * @return Object对象
     */  
    public static <T> List<T> convertListMap2ListBean(List<LinkedMap<String, Object>> listcolu, Class T) throws Exception {  
        List<T> beanList = new ArrayList<T>();
        for(int i=0, n=listcolu.size(); i<n; i++){
            Map<String,Object> map = listcolu.get(i);
            T bean = convertMap2Bean(map,T);
            beanList.add(bean);
        }
        return beanList;  
    }
    /** 
     * 将 Map对象转化为JavaBean   此方法已经测试通过
     * @param type 要转化的类型 
     * @param map 
     * @return Object对象
     */  
    public static <T> T convertMap2Bean(Map map, Class T) throws Exception {  
         if(map==null || map.size()==0){
             return null;
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(T);  
        T bean = (T)T.newInstance(); 
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
        for (int i = 0, n = propertyDescriptors.length; i <n ; i++) {  
            PropertyDescriptor descriptor = propertyDescriptors[i];  
            String propertyName = descriptor.getName(); 
            String upperPropertyName = propertyName.toUpperCase();
            if (map.containsKey(upperPropertyName)) { 
                Object value = map.get(upperPropertyName);  
                //这个方法不会报参数类型不匹配的错误。
                BeanUtils.copyProperty(bean, propertyName, value);
				//用这个方法对int等类型会报参数类型不匹配错误，需要我们手动判断类型进行转换，比较麻烦。
				//descriptor.getWriteMethod().invoke(bean, value);
				//用这个方法对时间等类型会报参数类型不匹配错误，也需要我们手动判断类型进行转换，比较麻烦。
				//BeanUtils.setProperty(bean, propertyName, value);
            }  
        }  
        return bean;  
    } 

}