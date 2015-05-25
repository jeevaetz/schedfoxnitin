/**
 * Copyright(C) 1999-2000, Sosnoski Software Solutions, Inc. General permission
 * is hereby granted to reuse this software for any purpose, including use
 * in applications developed for distribution and sale, provided this
 * copyright notice and usage policy is retained intact. The software is
 * provided "AS IS", with no warranty of any kind. ALL USE IS STRICTLY AT
 * THE USER'S RISK, AND IN NO CASE WILL SOSNOSKI SOFTWARE SOLUTIONS, INC.
 * BE LIABLE FOR ANY DIRECT OR INDIRECT COSTS OR DAMAGES OF ANY FORM ARISING
 * THROUGH OR FROM THE USE OF THIS SOFTWARE. 
 */

/**
 * Hashtable using <code>String</code> values as keys mapped to primitive
 * <code>int</code> values. This uses the standard hash technique of adding
 * a standard offset to the computed position in the table when a collision
 * occurs, so that several checks may potentially be necessary in order to
 * determine if a particular key value is present in the table (since if the
 * slot calculated by the hash function is occupied by a different entry it's
 * still necessary to check the offset slot for a match, and then the 
 * offset slot from that, until a empty slot is found).<p>
 *
 * Each time the number of entries present in the table grows above the
 * specified fraction full the table is expanded to twice its prior size
 * plus one (to ensure the size is always an odd number). The collision
 * offset is set to half the table size rounded down, ensuring that it
 * will cycle through all slots of the table before returning to the
 * original slot.<p>
 *
 * This implementation does not include support for removing entries from the
 * table. If support for removals is needed it can easily be added, but
 * remember that removing an entry will require following its collision chain
 * until an empty slot is found, recursively moving down any entries found
 * which hash to any of the slots visited on the chain.
 * 
 * @author Dennis M. Sosnoski
 * @version 1.0
 */
package rmischedule.components;

public class StringIntHashtable
{
	/** Default fill fraction allowed before growing table. */
	protected static final double DEFAULT_FILL = 0.3d;
	
	/** Minimum size used for hash table. */
	protected static final int MINIMUM_SIZE = 31;
	
	/** Hash value multiplier to scramble bits before calculating slot. */
	protected static final int KEY_MULTIPLIER = 517;
	
	/** Fill fraction allowed for this hash table. */
	protected double fillFraction;
	
	/** Number of entries present in table. */
	protected int entryCount;
	
	/** Entries allowed before growing table. */
	protected int entryLimit;
	
	/** Offset added (modulo table size) to slot number on collision. */
	protected int hitOffset;
	
	/** Array of key table slots. */
	protected String[] keyTable;
	
	/** Array of value table slots. */
	protected int[] valueTable;

	/**
	 * Constructor with full specification.
	 * 
	 * @param count number of values to assume in initial sizing of table
	 * @param fill fraction full allowed for table before growing
	 */
	
	public StringIntHashtable(int count, double fill) {
		
		// check the passed in fill fraction
		if (fill <= 0.0d || fill >= 1.0d) {
			throw new IllegalArgumentException("fill value out of range");
		}
		fillFraction = fill;
		
		// compute initial table size (ensuring odd)
		int size = Math.max((int) (count / fillFraction), MINIMUM_SIZE);
		size += (size + 1) % 2;
		
		// initialize the table information
		entryLimit = (int) (size * fillFraction);
		hitOffset = size / 2;
		keyTable = new String[size];
		valueTable = new int[size];
	}

	/**
	 * Constructor with only size supplied. Uses default value for fill
	 * fraction.
	 * 
	 * @param count number of values to assume in initial sizing of table
	 */
	
	public StringIntHashtable(int count) {
		this(count, DEFAULT_FILL);
	}

	/**
	 * Default constructor.
	 */
	
	public StringIntHashtable() {
		this(0, DEFAULT_FILL);
	}

	/**
	 * Copy (clone) constructor.
	 * 
	 * @param from instance being copied
	 */
	
	public StringIntHashtable(StringIntHashtable from) {
		fillFraction = from.fillFraction;
		entryCount = from.entryCount;
		entryLimit = from.entryLimit;
		hitOffset = from.hitOffset;
		keyTable = new String[from.keyTable.length];
		System.arraycopy(from.keyTable, 0, keyTable, 0, keyTable.length);
		valueTable = new int[from.valueTable.length];
		System.arraycopy(from.valueTable, 0, valueTable, 0, valueTable.length);
	}

	/**
	 * Get number of items in table.
	 * 
	 * @return item count present
	 */
	
	public int size() {
		return entryCount;
	}

	/**
	 * Find slot number for entry. Starts at the slot found by the hashed
	 * key value. If this slot is already occupied, it adds the collision
	 * offset (modulo the table size) to the slot number and checks that
	 * slot, repeating until an unused slot is found.
	 * 
	 * @param key to be added to table
	 * @param value associated value for key
	 * @return slot at which entry was added
	 */
	
	protected int assignSlot(String key, int value) {
		int offset = (key.hashCode() * KEY_MULTIPLIER &
			Integer.MAX_VALUE) % keyTable.length;
		while (keyTable[offset] != null) {
			offset = (offset + hitOffset) % keyTable.length;
		}
		keyTable[offset] = key;
		valueTable[offset] = value;
		return offset;
	}

	/**
	 * Expand the table. Increases the table size to twice the previous size
	 * plus one, reinserting each entry from the smaller table into the new
	 * one.
	 */
	
	protected void expandTable() {
		
		// initialize for the increased table size
		int size = keyTable.length * 2 + 1;
		entryLimit = (int) (size * fillFraction);
		hitOffset = keyTable.length;
		String[] keys = keyTable;
		keyTable = new String[size];
		int[] values = valueTable;
		valueTable = new int[size];
		
		// reinsert all entries into new table
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			if (key != null) {
				assignSlot(key, values[i]);
			}
		}
	}

	/**
	 * Add an entry to the table. Note that nothing prevents multiple entries
	 * with the same key, but the entry returned by that key will be
	 * undetermined in this case.
	 * 
	 * @param key to be added to table
	 * @param value associated value for key (should be positive for proper
	 * failure indication from <code>get</code> method)
	 */
	
	public void add(String key, int value) {
		if (++entryCount > entryLimit) {
			expandTable();
		}
		assignSlot(key, value);
	}

	/**
	 * Find an entry in the table. The not found case is handled by returning
	 * a negative value, but could alternatively throw an exception (expensive,
	 * if this can occur in the normal case of events), or return an offset with
	 * a separate method to retrieve the value corresponding to that offset, if
	 * negative values need to be supported.
	 * 
	 * @param key for entry to be returned
	 * @return value for key, or <code>-1</code> if none
	 */
	
	public int get(String key) {
		int offset = (key.hashCode() * KEY_MULTIPLIER &
			Integer.MAX_VALUE) % valueTable.length;
		String entry;
		while ((entry = keyTable[offset]) != null) {
			if (key.equals(entry)) {
				return valueTable[offset];
			}
			offset = (offset + hitOffset) % valueTable.length;
		}
		return -1;
	}

	/**
	 * Construct a copy of the table.
	 * 
	 * @return shallow copy of table
	 */
	
	public Object clone() {
		return new StringIntHashtable(this);
	}
}