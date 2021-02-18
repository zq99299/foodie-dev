package com.imooc.wx.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import java.io.InputStream;

public class XmlUtil {
//	private static XStream xstream;
//	static {
//		xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
//	}

	/**
	 * 
	 * @Description: xml字符串转换为对象
	 * @param inputXml
	 * @param type
	 * @return
	 * @throws Exception
	 * 
	 * @author imooc
	 * @date 2019年8月31日 下午4:52:13
	 */
	public static Object xml2Object(String inputXml, Class<?> type) throws Exception {
		if (null == inputXml || "".equals(inputXml)) {
			return null;
		}
		XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xstream.alias("xml", type);
		return xstream.fromXML(inputXml);
	}
	
	/**
	 * 
	 * @Description: 从inputStream中读取对象
	 * @param inputStream
	 * @param type
	 * @return
	 * @throws Exception
	 * 
	 * @author imooc
	 * @date 2019年8月31日 下午4:52:29
	 */
	public static Object xml2Object(InputStream inputStream, Class<?> type) throws Exception {
		if (null == inputStream) {
			return null;
		}
		XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xstream.alias("xml", type);
		return xstream.fromXML(inputStream, type);
	}

	/**
	 * 
	 * @Description: 对象转换为xml字符串
	 * @param ro
	 * @param types
	 * @return
	 * @throws Exception
	 * 
	 * @author imooc
	 * @date 2019年8月31日 下午4:52:45
	 */
	public static String object2Xml(Object ro, Class<?> types) throws Exception {
		if (null == ro) {
			return null;
		}
		XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xstream.alias("xml", types);
		return xstream.toXML(ro);
	}
	
}
