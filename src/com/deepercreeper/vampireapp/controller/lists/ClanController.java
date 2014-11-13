package com.deepercreeper.vampireapp.controller.lists;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineController;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

/**
 * A controller for all clans. Used to handle the creation and initialization of them.<br>
 * 
 * @author Vincent
 */
public class ClanController extends ListControllerImpl<Clan>
{
	/**
	 * Creates a new clan controller out of the given resources.
	 * 
	 * @param aResources
	 *            The resources.
	 * @param aDisciplines
	 *            The discipline controller.
	 */
	public ClanController(final Resources aResources, final DisciplineController aDisciplines)
	{
		final List<Clan> clans = new ArrayList<Clan>();
		for (final String line : aResources.getStringArray(R.array.clans))
		{
			final Clan clan = Clan.create(line, aDisciplines);
			clans.add(clan);
		}
		init(clans);
	}
}
