package test.event1;

import org.springframework.context.ApplicationEvent;


public class ContentEvent extends ApplicationEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContentEvent(final String content){
		super(content);
	}
	
}
