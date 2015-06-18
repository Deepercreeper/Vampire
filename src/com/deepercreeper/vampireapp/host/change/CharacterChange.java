package com.deepercreeper.vampireapp.host.change;

import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.util.Saveable;

/**
 * Each action, that is done and has an effect to the other side of the connection is serialized as a change.
 * 
 * @author vrl
 */
public interface CharacterChange extends Saveable
{
	/**
	 * The change is done to the character.
	 * 
	 * @param aCharacter
	 *            The character, that is changed.
	 */
	public void applyChange(CharacterInstance aCharacter);
	
	/**
	 * @return the change type.
	 */
	public String getType();
}
