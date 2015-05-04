package com.deepercreeper.vampireapp.character.instance;

/**
 * Represents the modes a character can be inside.
 * 
 * @author vrl
 */
public enum Mode
{
	/**
	 * Default state. Nothing special.
	 */
	DEFAULT,
	
	/**
	 * The character is sleeping. No actions possible.
	 */
	SLEEPING,
	
	/**
	 * The character has been hurt so much, that he can't move anymore.
	 */
	KO
}
