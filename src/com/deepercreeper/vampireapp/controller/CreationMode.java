package com.deepercreeper.vampireapp.controller;

/**
 * The creation of a character has several states.<br>
 * Each is declared by one CreationMode.
 * 
 * @author Vincent
 */
public enum CreationMode
{
	/**
	 * This is the first creation mode. Here are the start points and other main options registered.
	 */
	CREATION,
	
	/**
	 * Here are the bonus points set, that can be given to each character.
	 */
	FREE_POINTS,
	
	/**
	 * This is the normal game mode. At this time the user needs experience points to increase anything.
	 */
	NORMAL;
}
