package com.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import bean.XmlBeanDefinitionReader;

public class LoadDocument {
	
	private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	
	private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

	protected static  final Log logger = LogFactory.getLog(LoadDocument.class);

	
	public static void main(String[] args) {
		LoadDocument.loadDocument();
	}
	
	public static Document loadDocument() {
		try {
			File f = new File("D:\\temp\\applicationContext.xml");
			InputStream is = new FileInputStream(f);
			InputSource inputSource = new InputSource(is);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// 打开验证  
			factory.setValidating(true);
			// 设置Namespace有效  ,当validating为true时 ，此值需要为true 不然命名空间无法找到xsd文件校验
			factory.setNamespaceAware(true);
			//看名称，就明白为 设置验证的SCHEMA方式为XSD 因为还有其他DTD这种校验模式，所以这里需要指定  
			factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			ResourceLoader rl = new  DefaultResourceLoader();
			EntityResolver entityResolver = new ResourceEntityResolver(rl);
			docBuilder.setEntityResolver(entityResolver);
			ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);
			docBuilder.setErrorHandler(errorHandler);
			Document doc = docBuilder.parse(inputSource);
			BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
			XmlBeanDefinitionReader xr = new XmlBeanDefinitionReader(registry);
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
//			URL url = cl.getResource("applicationContext.xml");
//			String filePath = url.getPath();
			Resource r = new ClassPathResource("applicationContext.xml");
			System.out.println(r.getURI().toString());
			xr.regiterBean(doc, r);
//			doc.get
//			System.out.println(doc.toString());
//			System.out.println(doc.getDocumentElement().getTagName());
//			System.out.println(doc.getDocumentElement().getPrefix());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
