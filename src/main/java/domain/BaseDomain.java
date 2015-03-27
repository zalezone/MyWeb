package domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

//实现序列化接口，以便JVM可以序列化PO实例
public class BaseDomain implements Serializable {
	// 统一的etoString()方法因为所有实体的toString()方法 都用的是简单的"+"，因为每"＋" 一个就会 new 一个 String
	// 对象，这样如果系统内存小的话会暴内存（前提系统实体比较多）。使用ToStringBuilder就可以避免暴内存这种问题的
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
}
