package com.deepercreeper.vampireapp.controller.lists;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

/**
 * A list controller for all paths.
 * 
 * @author vrl
 */
public class PathCreationController extends ListControllerImpl<Path>
{
	/**
	 * Creates a new path controller.
	 * 
	 * @param aResources
	 *            The resources.
	 */
	public PathCreationController(final Resources aResources)
	{
		final List<Path> paths = new ArrayList<Path>();
		for (final String path : aResources.getStringArray(R.array.paths))
		{
			paths.add(new Path(path));
		}
		init(paths);
	}
}
