package cn.dmego.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class MapObjectUtil {
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
}
