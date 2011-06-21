package com.cari.voip.keyboard.soft.views.zest;

import java.util.Comparator;

import org.eclipse.zest.layouts.LayoutEntity;

public class SwitchUserComparatorbyFirst<T> implements Comparator<T> {

	public int compare(T o1, T o2){
		if(o2 instanceof LayoutEntity){
			LayoutEntity l2 = (LayoutEntity)o2;
			if(o1 instanceof Comparable){
				return ((Comparable)o1).compareTo(l2.getGraphData());
			}
		}
		
		return 0;
	}
}
