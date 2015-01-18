package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances;

import java.util.List;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.restrictions.InstanceRestrictionable;

public interface ItemGroupInstance extends InstanceRestrictionable
{
	public ItemControllerInstance getItemController();
	
	public List<ItemInstance> getItemsList();
	
	public int indexOfItem(ItemInstance aItem);
}
