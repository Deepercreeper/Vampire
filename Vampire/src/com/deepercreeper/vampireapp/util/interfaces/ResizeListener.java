package com.deepercreeper.vampireapp.util.interfaces;

/**
 * Some controllers need to update the size of their parents.
 * 
 * @author Vincent
 */
public interface ResizeListener
{
	/**
	 * Some child views were resized.
	 */
	public void resize();
}
