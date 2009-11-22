// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.util.Random;

/**
 * Random class with integer seed
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSRandom.java 17245790f2a9
 *          2006/09/12 21:44:14 duns $
 */
public class PSRandom extends Random {

	private long seed;
	private final static long multiplier = 0x5DEECE66DL;
	private final static long addend = 0xBL;
	private final static long mask = (1L << 32) - 1;

	public PSRandom() {
		this((int) System.currentTimeMillis());
	}

	public PSRandom(int seed) {
		setSeed(seed);
	}

	synchronized public void setSeed(int seed) {
		this.seed = (seed ^ multiplier) & mask;
	}

	synchronized protected int next(int bits) {
		seed = (seed * multiplier + addend) & mask;
		return (int) (seed >>> (32 - bits));
	}

	synchronized public int getSeed() {
		return (int) (seed ^ multiplier);
	}
}
