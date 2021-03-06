package com.tite.system.comfc.utility;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map; 

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * json对象转换工具类
 * 
 * @author guizhou
 * 
 */
public class JsonUtil {
 
	private static final ObjectMapper mapper = new ObjectMapper();


	/**
	 * @param <T>
	 *            泛型声明
	 * @param bean
	 *            类的实例
	 * @return JSON字符串
	 */
	public static <T> String toJson(T bean) {
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator gen = new JsonFactory().createGenerator(sw);
			mapper.writeValue(gen, bean);
			gen.close();
			return sw.toString();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * @param <T>
	 *            泛型声明
	 * @param json
	 *            JSON字符串
	 * @param clzz
	 *            要转换对象的类型
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clzz) {
		T t = null;
		try {
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			t = mapper.readValue(json, clzz);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return t;
	}

	/**
	 * @param json
	 *            JSON字符串,请保持key是加了双引号的
	 * @return Map对象,默认为HashMap
	 */
	public static Map<?, ?> fromJson(String json) {
		try {
			return mapper.readValue(json, HashMap.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
