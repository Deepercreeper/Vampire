package com.deepercreeper.vampireapp.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Saveable
{
	public Element asElement(Document aDoc);
}
