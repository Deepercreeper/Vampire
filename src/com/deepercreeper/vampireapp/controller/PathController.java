package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

public class PathController extends ListControllerImpl<Path>
{
	public PathController(final Resources aResources)
	{
		final List<Path> paths = new ArrayList<Path>();
		for (final String path : aResources.getStringArray(R.array.paths))
		{
			paths.add(new Path(path));
		}
		init(paths);
	}
}
