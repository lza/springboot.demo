package org.leon.springboot.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.leon.springboot.demo.controller.rest.params.BaseResult;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leon on 2017/4/21.
 */
public class Utils {
    public static Map<String, Object> getFieldNamesAndValues(final Object obj, boolean publicOnly)
            throws IllegalArgumentException,IllegalAccessException
    {
        Class<? extends Object> c1 = obj.getClass();
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = c1.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            if (publicOnly) {
                if(Modifier.isPublic(fields[i].getModifiers())) {
                    Object value = fields[i].get(obj);
                    map.put(name, value);
                }
            }
            else {
                fields[i].setAccessible(true);
                Object value = fields[i].get(obj);
                map.put(name, value);
            }
        }
        return map;
    }
    public static ServletResponse generateAuthFailResp(ServletResponse response, String status, String msg){
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.reset();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        BaseResult result = new BaseResult();
        result.setStatus(status);
        result.setMsg(msg);
        try {
            resp.getWriter().write(convertObjectToJson(result));
        } catch (Exception e) {
        }
        return resp;
    }

    public static String convertObjectToJson(Object object) {
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
