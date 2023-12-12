package me.grgamer2626.utils;

public interface Identifiable<T extends Number> {
	
	/**
	 * Return identification number of the object.
	 *
	 * @return id number of the object
	 */
	T getId();
}
