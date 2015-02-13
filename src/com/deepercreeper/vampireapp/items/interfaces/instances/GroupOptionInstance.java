package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;

public interface GroupOptionInstance extends Comparable<GroupOptionInstance>
{
	public void close();
	
	public LinearLayout getContainer();
	
	public Context getContext();
	
	public ItemGroupInstance getGroup(ItemGroup aGroup);
	
	public GroupOption getGroupOption();
	
	public List<ItemGroupInstance> getGroupsList();
	
	public Element asElement(Document aDoc);
	
	public CharacterInstance getCharacter();
	
	public String getName();
	
	public boolean hasGroup(ItemGroup aGroup);
	
	public boolean hasGroup(ItemGroupInstance aGroup);
	
	public boolean hasGroup(String aName);
	
	public void init();
	
	public boolean isOpen();
	
	public boolean isValueGroupOption();
	
	public void release();
	
	public void resize();
	
	public void setEnabled(boolean aEnabled);
	
	public void toggleGroup();
	
	public void updateGroups();
}
