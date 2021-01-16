package com.tristeza.kafka.collector.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * $FastJsonConvertUtil
 *
 * @author hezhuo.bai
 * @since 2019年1月15日 下午4:53:28
 */
public class FastJsonConvertUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FastJsonConvertUtil.class);

    private static final SerializerFeature[] featuresWithNullValue = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty};

    /**
     * <B>方法名称：</B>将JSON字符串转换为实体对象<BR>
     * <B>概要说明：</B>将JSON字符串转换为实体对象<BR>
     *
     * @param data  JSON字符串
     * @param clzss 转换对象
     * @return T
     * @author hezhuo.bai
     * @since 2019年1月15日 下午4:53:49
     */
    public static <T> T convertJSONToObject(String data, Class<T> clzss) {
        try {
            T t = JSON.parseObject(data, clzss);
            return t;
        } catch (Exception e) {
            LOGGER.error("convertJSONToObject Exception", e);
            return null;
        }
    }

    /**
     * <B>方法名称：</B>将JSONObject对象转换为实体对象<BR>
     * <B>概要说明：</B>将JSONObject对象转换为实体对象<BR>
     *
     * @param data  JSONObject对象
     * @param clzss 转换对象
     * @return T
     * @author hezhuo.bai
     * @since 2019年1月15日 下午4:54:32
     */
    public static <T> T convertJSONToObject(JSONObject data, Class<T> clzss) {
        try {
            T t = JSONObject.toJavaObject(data, clzss);
            return t;
        } catch (Exception e) {
            LOGGER.error("convertJSONToObject Exception", e);
            return null;
        }
    }

    /**
     * <B>方法名称：</B>将JSON字符串数组转为List集合对象<BR>
     * <B>概要说明：</B>将JSON字符串数组转为List集合对象<BR>
     *
     * @param data  JSON字符串数组
     * @param clzss 转换对象
     * @return List<T>集合对象
     * @author hezhuo.bai
     * @since 2019年1月15日 下午4:54:50
     */
    public static <T> List<T> convertJSONToArray(String data, Class<T> clzss) {
        try {
            List<T> t = JSON.parseArray(data, clzss);
            return t;
        } catch (Exception e) {
            LOGGER.error("convertJSONToArray Exception", e);
            return null;
        }
    }

    /**
     * <B>方法名称：</B>将List<JSONObject>转为List集合对象<BR>
     * <B>概要说明：</B>将List<JSONObject>转为List集合对象<BR>
     *
     * @param data  List<JSONObject>
     * @param clzss 转换对象
     * @return List<T>集合对象
     * @author hezhuo.bai
     * @since 2019年1月15日 下午4:55:11
     */
    public static <T> List<T> convertJSONToArray(List<JSONObject> data, Class<T> clzss) {
        try {
            List<T> t = new ArrayList<T>();
            for (JSONObject jsonObject : data) {
                t.add(convertJSONToObject(jsonObject, clzss));
            }
            return t;
        } catch (Exception e) {
            LOGGER.error("convertJSONToArray Exception", e);
            return null;
        }
    }

    /**
     * <B>方法名称：</B>将对象转为JSON字符串<BR>
     * <B>概要说明：</B>将对象转为JSON字符串<BR>
     *
     * @param obj 任意对象
     * @return JSON字符串
     * @author hezhuo.bai
     * @since 2019年1月15日 下午4:55:41
     */
    public static String convertObjectToJSON(Object obj) {
        try {
            String text = JSON.toJSONString(obj);
            return text;
        } catch (Exception e) {
            LOGGER.error("convertObjectToJSON Exception", e);
            return null;
        }
    }

    /**
     * <B>方法名称：</B>将对象转为JSONObject对象<BR>
     * <B>概要说明：</B>将对象转为JSONObject对象<BR>
     *
     * @param obj 任意对象
     * @return JSONObject对象
     * @author hezhuo.bai
     * @since 2019年1月15日 下午4:55:55
     */
    public static JSONObject convertObjectToJSONObject(Object obj) {
        try {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(obj);
            return jsonObject;
        } catch (Exception e) {
            LOGGER.error("convertObjectToJSONObject Exception", e);
            return null;
        }
    }

    public static String convertObjectToJSONWithNullValue(Object obj) {
        try {
            String text = JSON.toJSONString(obj, featuresWithNullValue);
            return text;
        } catch (Exception e) {
            LOGGER.error("convertObjectToJSONWithNullValue Exception", e);
            return null;
        }
    }
}
