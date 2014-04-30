/*
 * Copyright (C) 2014 Evil-Co <http://wwww.evil-co.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evilco.version;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Provides a simple version representation.
 * @auhtor			Johannes Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public class SimpleVersionRange {

	/**
	 * Defines the ceiling version selector.
	 */
	public static final String CEIL_SELECTOR = ")";

	/**
	 * Defines the ceiling (fuzzy) version selector.
	 */
	public static final String CEIL_SELECTOR_FUZZY = "]";

	/**
	 * Defines the floor version selector.
	 */
	public static final String FLOOR_SELECTOR = "(";

	/**
	 * Defines the floor (fuzzy) version selector.
	 */
	public static final String FLOOR_SELECTOR_FUZZY = "[";

	/**
	 * Defines the range separator.
	 */
	public static final String RANGE_SEPARATOR = ",";

	/**
	 * Stores the ceiling version.
	 */
	protected final SimpleVersion ceilVersion;

	/**
	 * Indicates whether the ceiling version is fuzzy (>= ceiling instead of > ceiling).
	 */
	protected final boolean ceilVersionFuzzy;

	/**
	 * Stores the floor version.
	 */
	protected final SimpleVersion floorVersion;

	/**
	 * Indicates whether the floor version is fuzzy (>= floor instead of > floor).
	 */
	protected final boolean floorVersionFuzzy;

	/**
	 * Constructs a new SimpleVersionRange instance.
	 * @param floorVersion The floor version.
	 * @param floorVersionFuzzy True if the floor version is fuzzy (>= floor instead of > floor).
	 * @param ceilVersion The ceiling version.
	 * @param ceilVersionFuzzy True if the ceiling version is fuzzy (>= ceiling instead of > ceiling).
	 */
	public SimpleVersionRange (@Nonnull SimpleVersion floorVersion, boolean floorVersionFuzzy, SimpleVersion ceilVersion, boolean ceilVersionFuzzy) throws VersionParserException {
		Preconditions.checkNotNull (floorVersion, "floorVersion");

		// verify version range
		if (ceilVersion != null && floorVersion.newer (ceilVersion)) throw new VersionParserException ("The floor version is newer than the ceiling version.");

		this.floorVersion = floorVersion;
		this.floorVersionFuzzy = floorVersionFuzzy;
		this.ceilVersion = ceilVersion;
		this.ceilVersionFuzzy = ceilVersionFuzzy;
	}

	/**
	 * Constructs a new SimpleVersionRange instance.
	 * @param floorVersion The floor version.
	 * @param floorVersionFuzzy True if the floor version is fuzzy (>= floor instead of > floor).
	 * @param ceilVersion The ceiling version.
	 * @param ceilVersionFuzzy True if the ceiling version is fuzzy (>= ceiling instead of > ceiling).
	 */
	public SimpleVersionRange (String floorVersion, boolean floorVersionFuzzy, String ceilVersion, boolean ceilVersionFuzzy) throws VersionParserException {
		this (new SimpleVersion (floorVersion), floorVersionFuzzy, new SimpleVersion (ceilVersion), ceilVersionFuzzy);
	}

	/**
	 * Constructs a new SimpleVersionRange instance.
	 * @param floorVersion The floor version.
	 * @param ceilVersion The ceiling version.
	 * @throws VersionParserException
	 */
	public SimpleVersionRange (SimpleVersion floorVersion, SimpleVersion ceilVersion) throws VersionParserException {
		this (floorVersion, true, ceilVersion, true);
	}

	/**
	 * Constructs a new SimpleVersionRange instance.
	 * @param floorVersion The floor version.
	 * @param ceilVersion The ceiling version.
	 */
	public SimpleVersionRange (String floorVersion, String ceilVersion) throws VersionParserException {
		this (floorVersion, true, ceilVersion, true);
	}

	/**
	 * Constructs a new SimpleVersionRange instance.
	 * @param versionRange The version range.
	 * @throws VersionParserException
	 */
	public SimpleVersionRange (@Nonnull String versionRange) throws VersionParserException {
		Preconditions.checkNotNull (versionRange, "versionRange");

		// remove all whitespaces
		versionRange = CharMatcher.WHITESPACE.removeFrom (versionRange);

		// check size
		if (versionRange.isEmpty ()) throw new VersionParserException ("A version range cannot be empty.");

		// split string into two parts
		List<String> versionRangeElements = Splitter.on (RANGE_SEPARATOR).splitToList (versionRange);

		// create simple range
		if (versionRangeElements.size () == 1 && !CharMatcher.anyOf (FLOOR_SELECTOR + FLOOR_SELECTOR_FUZZY + CEIL_SELECTOR + CEIL_SELECTOR_FUZZY).matchesAnyOf (versionRangeElements.get (0))) {
			// store version
			this.floorVersionFuzzy = true;
			this.floorVersion = new SimpleVersion (versionRangeElements.get (0));

			// set ceiling version
			this.ceilVersionFuzzy = false;
			this.ceilVersion = null;

			// done
			return;
		}

		// verify length
		if (versionRangeElements.size () != 2) throw new VersionParserException ("Invalid amount of range elements: " + versionRangeElements.size ());

		// process first element
		String versionFloor = versionRangeElements.get (0);

		// get floor type
		this.floorVersionFuzzy = versionFloor.startsWith (FLOOR_SELECTOR_FUZZY);
		if (!this.floorVersionFuzzy && !versionFloor.startsWith (FLOOR_SELECTOR)) throw new VersionParserException ("Invalid floor prefix found: " + versionFloor.charAt (0));

		// store floor version
		this.floorVersion = new SimpleVersion (versionFloor.substring (1));

		// process second element
		String versionCeil = versionRangeElements.get (1);

		// get ceil type
		this.ceilVersionFuzzy = versionCeil.endsWith (CEIL_SELECTOR_FUZZY);
		if (!this.ceilVersionFuzzy && !versionCeil.endsWith (CEIL_SELECTOR)) throw new VersionParserException ("Invalid ceiling suffix found: " + versionCeil.charAt ((versionCeil.length () - 1)));

		// get ceil version
		this.ceilVersion = new SimpleVersion (versionCeil.substring (0, (versionCeil.length () - 1)));

		// verify range
		if (this.floorVersion.newer (this.ceilVersion)) throw new VersionParserException ("The floor version is newer than the ceiling version.");
	}

	/**
	 * Returns the ceiling (maximal) version.
	 * @return The ceiling version.
	 */
	public SimpleVersion getCeilingVersion () {
		return this.ceilVersion;
	}

	/**
	 * Returns the floor (minimal) version.
	 * @return The floor version.
	 */
	public SimpleVersion getFloorVersion () {
		return this.floorVersion;
	}

	/**
	 * Checks whether the ceiling version is fuzzy (accepts everything below and the actual version).
	 * @return True if the ceiling is fuzzy.
	 */
	public boolean isCeilingFuzzy () {
		return this.ceilVersionFuzzy;
	}

	/**
	 * Checks whether the floor version is fuzzy (accepts everything above and the actual version).
	 * @return
	 */
	public boolean isFloorVersionFuzzy () {
		return this.floorVersionFuzzy;
	}

	/**
	 * Checks whether the specified version is in range.
	 * @param version The version
	 * @return True if the version is in range.
	 */
	public boolean isInRange (@Nonnull SimpleVersion version) {
		Preconditions.checkNotNull (version);

		// support bigger than ranges
		if (this.ceilVersion == null && (this.floorVersionFuzzy && this.floorVersion.equals (version) || this.floorVersion.older (version))) return true;

		// check normal ranges
		return (this.ceilVersionFuzzy && this.ceilVersion.equals (version) || this.floorVersionFuzzy && this.floorVersion.equals (version) || (this.ceilVersion.newer (version) && this.floorVersion.older (version)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj) {
		// handle non range objects
		if (!(obj instanceof SimpleVersionRange)) return super.equals (obj);

		// get range
		SimpleVersionRange range = ((SimpleVersionRange) obj);

		// check
		return (range.getCeilingVersion ().equals (this.getCeilingVersion ()) && range.getFloorVersion ().equals (this.getFloorVersion ()) && range.isCeilingFuzzy () == this.isCeilingFuzzy () && range.isFloorVersionFuzzy () == this.isFloorVersionFuzzy ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString () {
		StringBuilder builder = new StringBuilder ();

		// append floor selector
		builder.append ((this.isFloorVersionFuzzy () ? FLOOR_SELECTOR_FUZZY : FLOOR_SELECTOR));
		builder.append (this.getFloorVersion ().toString ());
		builder.append (RANGE_SEPARATOR);

		// append ceiling selector
		builder.append (this.getCeilingVersion ().toString ());
		builder.append ((this.isCeilingFuzzy () ? CEIL_SELECTOR_FUZZY : CEIL_SELECTOR));

		// return finished selector
		return builder.toString ();
	}
}