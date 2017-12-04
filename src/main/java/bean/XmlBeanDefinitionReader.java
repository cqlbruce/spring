package bean;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.beans.factory.parsing.NullSourceExtractor;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.ReaderEventListener;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

public class XmlBeanDefinitionReader {
	
	private final BeanDefinitionRegistry registry;
	private ProblemReporter problemReporter = new FailFastProblemReporter();

	private ReaderEventListener eventListener = new EmptyReaderEventListener();

	private SourceExtractor sourceExtractor = new NullSourceExtractor();
	
	private Class<?> documentReaderClass = DefaultBeanDefinitionDocumentReader.class;

	private NamespaceHandlerResolver namespaceHandlerResolver;
	
	private ClassLoader classLoader ; 

	

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		this.registry = registry ; 
	}
	
	
	public void regiterBean(Document doc , Resource resource) {
		BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
//		int countBefore = getRegistry().getBeanDefinitionCount();
		documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
		
	}
	
	protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
		return BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass(this.documentReaderClass));
	}
	
	public XmlReaderContext createReaderContext(Resource resource) {
		return new XmlReaderContext(resource, this.problemReporter, this.eventListener,
				this.sourceExtractor, new org.springframework.beans.factory.xml.XmlBeanDefinitionReader(this.registry), new DefaultNamespaceHandlerResolver(classLoader));
		
	}
	
	
	public final BeanDefinitionRegistry getRegistry() {
		return this.registry;
	}

}
