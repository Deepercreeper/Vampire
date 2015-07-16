package com.deepercreeper.vampireapp.util.interfaces;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Every class that is able to be saved into a XML file needs to implement this interface.
 * 
 * @author vrl
 */
public interface Saveable
{
	/**
	 * Parses this into a document element.
	 * 
	 * @param aDoc
	 *            The document.
	 * @return the element that contains all data for this instance.
	 */
	public Element asElement(Document aDoc);
}
