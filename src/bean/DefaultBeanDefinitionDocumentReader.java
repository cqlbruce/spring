package bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DefaultBeanDefinitionDocumentReader  implements BeanDefinitionDocumentReader {

	protected final Log logger = LogFactory.getLog(DefaultBeanDefinitionDocumentReader.class);

	
	public static final String BEAN_ELEMENT = BeanDefinitionParserDelegate.BEAN_ELEMENT;
	private BeanDefinitionParserDelegate delegate ; 
	private XmlReaderContext readerContext;
	public static final String NESTED_BEANS_ELEMENT = "beans";
	public static final String ALIAS_ELEMENT = "alias";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String ALIAS_ATTRIBUTE = "alias";
	public static final String IMPORT_ELEMENT = "import";
	public static final String RESOURCE_ATTRIBUTE = "resource";
	public static final String PROFILE_ATTRIBUTE = "profile";
	
	
	@Override
	public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
			throws BeanDefinitionStoreException {
		this.readerContext = readerContext;
		Element root = doc.getDocumentElement();
		doRegisterBeanDefinitions(root);
	}
	
	protected void doRegisterBeanDefinitions(Element root) {
		BeanDefinitionParserDelegate parent = this.delegate;
		this.delegate = createDelegate(getReaderContext(), root, parent);
		
//		preProcessXml(root);
		parseBeanDefinitions(root, this.delegate);
//		postProcessXml(root);
		this.delegate = parent;
	}
	
	protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
		if (delegate.isDefaultNamespace(root)) {
			NodeList nl = root.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node instanceof Element) {
					Element ele = (Element) node;
					if (delegate.isDefaultNamespace(ele)) {
						parseDefaultElement(ele, delegate);
					}
					else {
						delegate.parseCustomElement(ele);
					}
				}
			}
		}else {
			delegate.parseCustomElement(root);
		}
	}
	
	
	private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
		if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
//			importBeanDefinitionResource(ele);
		}
		else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
//			processAliasRegistration(ele);
		}
		else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
			processBeanDefinition(ele, delegate);
		}
		else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
			// recurse
//			doRegisterBeanDefinitions(ele);
		}
	}
	
	
	protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
		BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
		if (bdHolder != null) {
			bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
			try {
				// Register the final decorated instance.
				BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error("Failed to register bean definition with name '" +
						bdHolder.getBeanName() + "'", ele, ex);
			}
			// Send registration event.
			getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
		}
	}
	
	
	
	protected BeanDefinitionParserDelegate createDelegate(
			XmlReaderContext readerContext, Element root, BeanDefinitionParserDelegate parentDelegate) {

		BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
		delegate.initDefaults(root, parentDelegate);
		return delegate;
	}
	
	
	
	protected final XmlReaderContext getReaderContext() {
		return this.readerContext;
	}
	
	protected void preProcessXml(Element root) {
	}
	
	protected void postProcessXml(Element root) {
	}
	
}
