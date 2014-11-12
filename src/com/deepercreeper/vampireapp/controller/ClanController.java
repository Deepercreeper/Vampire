package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.Clan;
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
	private static final String	NAME_DELIM	= ":", GENERATION_DELIM = ";", CLAN_DISCIPLIN_DELIM = ",";
	
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
			final String[] clanData = line.split(NAME_DELIM);
			final Clan clan = new Clan(clanData[0]);
			if (clanData.length > 1)
			{
				final String[] clanDisciplines = clanData[1].split(GENERATION_DELIM);
				for (final String clanDiscipline : clanDisciplines[0].split(CLAN_DISCIPLIN_DELIM))
				{
					clan.addDiscipline(aDisciplines.getDisciplines().getItem(clanDiscipline));
				}
				if (clanDisciplines.length > 1)
				{
					if ( !clanDisciplines[1].isEmpty())
					{
						clan.setGeneration(Integer.parseInt(clanDisciplines[1]));
					}
					if (clanDisciplines.length > 2)
					{
						for (final String restrictionData : clanDisciplines[2].split(Restriction.RESTRICTIONS_DELIM))
						{
							clan.addRestriction(Restriction.create(restrictionData));
						}
					}
				}
			}
			clans.add(clan);
		}
		init(clans);
	}
}
