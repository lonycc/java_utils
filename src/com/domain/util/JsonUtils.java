package com.domain.util;

import java.io.IOException;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @description Utils - JSON
 * @version 1.0
 */
public final class JsonUtils
{

	/** ObjectMapper */
	private static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * @description 不可实例化
	 */
	private JsonUtils() {}

	/**
	 * @description 将对象转换为JSON字符串
	 * @param value Object 对象
	 * @return String
	 */
	public static String toJson(Object value)
	{
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description 根据路径获取json的值
	 * @param json JSON字符串
	 * @param path 路径
	 * @return JSOn字符串
	 */
	public static String getPathValue(String json, String path)
	{
		try {
			JsonNode node = mapper.readTree(json);
			for (String name : path.split(","))
			{
				if ( name.startsWith("[") && name.startsWith("]") )
				{
					int index = Integer.parseInt(name.substring(1, name.length() - 2));
					node = node.get(index);
				} else {
					node = node.get(name);
				}
			}
			return node.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description 将JSON字符串转换为对象
	 * @param json JSON字符串
	 * @param valueType 对象类型
	 * @return 对象
	 */
	public static <T> T toObject(String json, Class<T> valueType)
	{
		try {
			return mapper.readValue(json, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description 将JSON字符串转换为对象
	 * @param json  JSON字符串
	 * @param typeReference 对象类型
	 * @return 对象
	 */
	public static <T> T toObject(String json, TypeReference<?> typeReference)
	{
		try {
			return mapper.readValue(json, typeReference);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description 将JSON字符串转换为对象
	 * @param json  JSON字符串
	 * @param javaType 对象类型
	 * @return 对象
	 */
	public static <T> T toObject(String json, JavaType javaType) {
		try {
			return mapper.readValue(json, javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description 将对象转换为JSON流
	 * @param writer writer
	 * @param value  对象
	 */
	public static void writeValue(Writer writer, Object value) {
		try {
			mapper.writeValue(writer, value);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}