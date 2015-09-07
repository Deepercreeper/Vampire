package com.deepercreeper.vampireapp.util.view.listeners;

/**
 * A listener for choose difficulty dialogs.
 * 
 * @author Vincent
 */
public interface DifficultyChooseListener
{
	/**
	 * The given difficulty was chosen.
	 * 
	 * @param aDifficulty
	 *            The difficulty.
	 */
	public void difficultyChosen(int aDifficulty);
	
	/**
	 * No difficulty was chosen.
	 */
	public void cancel();
}
