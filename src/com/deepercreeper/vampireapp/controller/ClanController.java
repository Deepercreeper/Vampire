package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.Clan;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineController;

public class ClanController
{
	private static final String			NAME_DELIM	= ":", DISCIPLINES_DELIM = ";", CLAN_DISCIPLIN_DELIM = ",";
	
	private final HashMap<String, Clan>	mClans		= new HashMap<String, Clan>();
	
	private final List<String>			mClanNames	= new ArrayList<String>();
	
	public ClanController(final Resources aResources, final DisciplineController aDisciplines)
	{
		for (final String line : aResources.getStringArray(R.array.clans))
		{
			final String[] clanData = line.split(NAME_DELIM);
			final Clan clan = new Clan(clanData[0]);
			if (clanData.length > 1)
			{
				final String[] clanDisciplines = clanData[1].split(DISCIPLINES_DELIM);
				for (final String clanDiscipline : clanDisciplines[0].split(CLAN_DISCIPLIN_DELIM))
				{
					clan.addDiscipline(aDisciplines.getDisciplines().getItem(clanDiscipline));
				}
				if (clanDisciplines.length > 1)
				{
					clan.setGeneration(Integer.parseInt(clanDisciplines[1]));
				}
			}
			mClans.put(clanData[0], clan);
			mClanNames.add(clanData[0]);
		}
		Collections.sort(mClanNames);
	}
	
	public int indexOf(final Clan aClan)
	{
		return mClanNames.indexOf(aClan.getName());
	}
	
	public Clan get(final int aPos)
	{
		return mClans.get(mClanNames.get(aPos));
	}
	
	public Clan getFirst()
	{
		return mClans.get(mClanNames.get(0));
	}
	
	public Clan getClan(final String aName)
	{
		return mClans.get(aName);
	}
	
	public List<String> getClanNames()
	{
		return mClanNames;
	}
}
