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
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

/**
 * Provides a simple version representation.
 * @auhtor			Johannes Donath <johannesd@evil-co.com>
 * @copyright			Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public class SimpleVersion {

	/**
	 * Defines the version extra bit separator (e.g. version-extra).
	 */
	public static String VERSION_EXTRA_SEPARATOR = "-";

	/**
	 * Defines the version separator (e.g. X.X.X).
	 */
	public static String VERSION_SEPARATOR = ".";

	/**
	 * Defines a list of alpha extra bits.
	 */
	public static List<String> VERSION_EXTRA_ALPHA = (new ImmutableList.Builder<String> ().add ("ALPHA").add ("A")).build ();

	/**
	 * Defines a list of beta extra bits.
	 */
	public static List<String> VERSION_EXTRA_BETA = (new ImmutableList.Builder<String> ().add ("BETA").add ("B")).build ();

	/**
	 * Defines a list of release candidate bits.
	 */
	public static List<String> VERSION_EXTRA_RELEASE_CANDIDATE = (new ImmutableList.Builder<String> ().add ("RC")).build ();

	/**
	 * Defines a list of snapshot extra bits.
	 */
	public static List<String> VERSION_EXTRA_SNAPSHOT = (new ImmutableList.Builder<String> ().add ("SNAPSHOT")).build ();

	/**
	 * Stores the major version bit.
	 */
	protected final int majorBit;

	/**
	 * Stores the minor version bit.
	 */
	protected final int minorBit;

	/**
	 * Stores the maintenance version bit.
	 */
	protected final int maintenanceBit;

	/**
	 * Stores the build version bit.
	 */
	protected final int buildBit;

	/**
	 * Stores the extra version bit.
	 */
	protected final String extraBit;

	/**
	 * Constructs a new SimpleVersion instance.
	 * @param majorBit The major version bit (1.X.X.X-X)
	 * @param minorBit The minor version bit (X.1.X.X-X)
	 * @param maintenanceBit The maintenance version bit (X.X.1.X-X)
	 * @param buildBit The build version bit (X.X.X.1-X)
	 * @param extraBit The extra version bit (X.X.X.X-SNAPSHOT)
	 */
	public SimpleVersion (int majorBit, int minorBit, int maintenanceBit, int buildBit, @Nullable String extraBit) {
		this.majorBit = majorBit;
		this.minorBit = minorBit;
		this.maintenanceBit = maintenanceBit;
		this.buildBit = buildBit;
		this.extraBit = extraBit;
	}

	/**
	 * Constructs a new SimpleVersion instance.
	 * @param majorBit The major version bit (1.X.X-X)
	 */
	public SimpleVersion (int majorBit) {
		this (majorBit, 0, 0, 0, null);
	}

	/**
	 * Constructs a new SimpleVersion instance.
	 * @param majorBit The major version bit (1.X.X-X)
	 * @param minorBit The minor version bit (X.1.X-X)
	 */
	public SimpleVersion (int majorBit, int minorBit) {
		this (majorBit, minorBit, 0, 0, null);
	}

	/**
	 * Constructs a new SimpleVersion instance.
	 * @param majorBit The major version bit (1.X.X-X)
	 * @param minorBit The minor version bit (X.1.X-X)
	 * @param maintenanceBit The maintenance version bit (X.X.1-X)
	 */
	public SimpleVersion (int majorBit, int minorBit, int maintenanceBit) {
		this (majorBit, minorBit, maintenanceBit, 0, null);
	}

	/**
	 * Constructs a new SimpleVersion instance.
	 * @param majorBit The major version bit (1.X.X-X)
	 * @param minorBit The minor version bit (X.1.X-X)
	 * @param maintenanceBit The maintenance version bit (X.X.1-X)
	 * @param buildBit The build version bit (X.X.X.1-X)
	 */
	public SimpleVersion (int majorBit, int minorBit, int maintenanceBit, int buildBit) {
		this (majorBit, minorBit, maintenanceBit, buildBit, null);
	}

	/**
	 * Constructs a new SimpleVersion instance.
	 * @param version A string representation of the version.
	 */
	public SimpleVersion (@Nonnull String version) throws VersionParserException {
		Preconditions.checkNotNull (version, "version");

		// check for whitespaces
		if (CharMatcher.WHITESPACE.matchesAnyOf (version)) throw new VersionParserException ("Versions are not allowed to contain any whitespace characters.");

		// split version
		Iterable<String> versionIterable = Splitter.on (VERSION_SEPARATOR).split (version);

		// check length
		if (!versionIterable.iterator ().hasNext ()) {
			this.majorBit = 0;
			this.minorBit = 0;
			this.maintenanceBit = 0;
			this.buildBit = 0;
			this.extraBit = null;
			return;
		}

		// define current bit
		int currentVersionBit = 0;
		int majorBit = 0;
		int minorBit = 0;
		int maintenanceBit = 0;
		int buildBit = 0;
		String extraBit = null;

		// iterate over all elements
		Iterator<String> it = versionIterable.iterator ();

		// validate length
		if (!it.hasNext ()) throw new VersionParserException ("Supplied an empty version.");

		while (it.hasNext ()) {
			// get next bit
			String versionBit = it.next ();

			// check for last bit
			if (!it.hasNext () && versionBit.contains (VERSION_EXTRA_SEPARATOR)) {
				// split current bit
				List<String> versionBitElements = Splitter.on (VERSION_EXTRA_SEPARATOR).splitToList (versionBit);

				// update versionBit variable
				versionBit = versionBitElements.get (0);

				// update extra bit
				extraBit = "";

				// get iterator
				Iterator<String> extraBitIterator = versionBitElements.iterator ();

				// skip version bit
				extraBitIterator.next ();

				// append all elements
				while (extraBitIterator.hasNext ()) {
					if (!extraBit.isEmpty ()) extraBit += VERSION_EXTRA_SEPARATOR;
					extraBit += extraBitIterator.next ();
				}
			}

			// store bit
			try {
				switch (currentVersionBit) {
					case 0:
						majorBit = Integer.valueOf (versionBit);
						break;
					case 1:
						minorBit = Integer.valueOf (versionBit);
						break;
					case 2:
						maintenanceBit = Integer.valueOf (versionBit);
						break;
					case 3:
						buildBit = Integer.valueOf (versionBit);
						break;
				}
			} catch (NumberFormatException ex) {
				throw new VersionParserException ("Found invalid version number in " + (currentVersionBit == 0 ? "major" : (currentVersionBit == 1 ? "minor" : (currentVersionBit == 2 ? "maintenance" : "build"))) + " version bit: " + ex.getMessage (), ex);
			}

			// verify state (stop processing if we hit the extra bit to support 1.0-EXTRA, 1.0.0-EXTRA, etc.)
			if (extraBit != null) break;

			// update current position
			currentVersionBit++;
		}

		// store elements
		this.majorBit = majorBit;
		this.minorBit = minorBit;
		this.maintenanceBit = maintenanceBit;
		this.buildBit = buildBit;
		this.extraBit = extraBit;
	}

	/**
	 * Copies a SimpleVersion instance.
	 * @param version The version to copy.
	 */
	public SimpleVersion (@Nonnull SimpleVersion version) {
		Preconditions.checkNotNull (version);

		this.majorBit = version.getMajor ();
		this.minorBit = version.getMinor ();
		this.maintenanceBit = version.getMaintenance ();
		this.buildBit = version.getBuild ();
		this.extraBit = version.getExtra ();
	}

	/**
	 * Constructs a new SimpleVersion instance.
	 */
	public SimpleVersion () {
		this (1, 0, 0, 0, null);
	}

	/**
	 * Returns the major version bit.
	 * @return The major version bit (1.X.X-X)
	 */
	public int getMajor () {
		return this.majorBit;
	}

	/**
	 * Returns the minor version bit.
	 * @return The minor version bit (X.1.X-X)
	 */
	public int getMinor () {
		return this.minorBit;
	}

	/**
	 * Returns the maintenance version bit.
	 * @return The maintenance version bit (X.X.1-X)
	 */
	public int getMaintenance () {
		return this.maintenanceBit;
	}

	/**
	 * Returns the build version bit.
	 * @return The build version bit (X.X.X.1-X)
	 */
	public int getBuild () {
		return this.buildBit;
	}

	/**
	 * Returns the extra version bit.
	 * @return The extra version bit (X.X.X-SNAPSHOT)
	 */
	public String getExtra () {
		return this.extraBit;
	}

	/**
	 * Returns a stripped version of the extra bit.
	 * @return A stripped version of extra.
	 */
	protected String getExtraStripped () {
		if (this.getExtra () == null || this.getExtra ().isEmpty () || !this.getExtra ().contains (VERSION_EXTRA_SEPARATOR)) return this.getExtra ();
		return Splitter.on (VERSION_EXTRA_SEPARATOR).splitToList (this.getExtra ()).get (0);
	}

	/**
	 * Returns the unstable version (if any).
	 * @return The unstable version.
	 */
	public int getUnstableVersion () {
		// check for normal builds
		if (!this.isUnstableBuild ()) return -1;

		// check for version bit
		if (this.getExtra () == null || this.getExtra ().isEmpty () || !this.getExtra ().contains (VERSION_EXTRA_SEPARATOR)) return 0;

		// extract version bit
		return Integer.valueOf (Splitter.on (VERSION_EXTRA_SEPARATOR).splitToList (this.getExtra ()).get (1));
	}

	/**
	 * Checks whether the version is an alpha build.
	 * @return True if the version represents an alpha version.
	 */
	public boolean isAlphaBuild () {
		return (this.getExtra () != null && !this.getExtra ().isEmpty () && VERSION_EXTRA_ALPHA.contains (this.getExtraStripped ().toUpperCase ()));
	}

	/**
	 * Checks whether the version is a beta build.
	 * @return True if the version represents a beta version.
	 */
	public boolean isBetaBuild () {
		return (this.getExtra () != null && !this.getExtra ().isEmpty () && this.getExtra () != null && VERSION_EXTRA_BETA.contains (this.getExtraStripped ().toUpperCase ()));
	}

	/**
	 * Checks whether the version is a release candidate build.
	 * @return True if the version represents a release candidate build.
	 */
	public boolean isReleaseCandidateBuild () {
		return (this.getExtra () != null && !this.getExtra ().isEmpty () && VERSION_EXTRA_RELEASE_CANDIDATE.contains (this.getExtraStripped ().toUpperCase ()));
	}

	/**
	 * Checks whether the version is a snapshot build.
	 * @return True if the version represents a snapshot build.
	 */
	public boolean isSnapshotBuild () {
		return (this.getExtra () != null && !this.getExtra ().isEmpty () && VERSION_EXTRA_SNAPSHOT.contains (this.getExtraStripped ().toUpperCase ()));
	}

	/**
	 * Checks whether the version is an unstable build.
	 * @return True if the version represents an unstable build.
	 */
	public boolean isUnstableBuild () {
		return (this.isAlphaBuild () || this.isBetaBuild () || this.isReleaseCandidateBuild () || this.isSnapshotBuild ());
	}

	/**
	 * Checks whether the version is newer than the supplied version.
	 * @param version The version to compare.
	 * @return True if the supplied version is older.
	 */
	public boolean newer (@Nonnull SimpleVersion version) {
		Preconditions.checkNotNull (version, "version");

		// check equality
		if (this.equals (version)) return false;

		// check main bits
		if (version.getMajor () < this.getMajor ()) return true;
		if (version.getMinor () < this.getMinor ()) return true;
		if (version.getMaintenance () < this.getMaintenance ()) return true;
		if (version.getBuild () < this.getBuild ()) return true;

		// check for unstable builds (in general)
		if (version.isUnstableBuild () && !this.isUnstableBuild ()) return true;

		// check unstable versions
		if (version.isAlphaBuild () && !this.isAlphaBuild ()) return true;
		if (version.isBetaBuild () && !this.isAlphaBuild () && !this.isBetaBuild ()) return true;
		if (version.isSnapshotBuild () && !this.isAlphaBuild () && !this.isBetaBuild () && !this.isSnapshotBuild ()) return true;

		// check exact version bit
		if (version.isUnstableBuild () && this.isUnstableBuild () && !version.isSnapshotBuild () && !this.isSnapshotBuild () && (version.getUnstableVersion () < this.getUnstableVersion ())) return true;

		// seems like we're older (or the same version)
		return false;
	}

	/**
	 * Checks whether the version is older than the supplied version.
	 * @param version The version to compare.
	 * @return True if the supplied version is newer.
	 */
	public boolean older (@Nonnull SimpleVersion version) {
		Preconditions.checkNotNull (version, "version");

		return (!this.equals (version) && !this.newer (version));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj) {
		// check for null objects
		if (obj == null) return false;

		// handle non versions
		if (!(obj instanceof SimpleVersion)) return super.equals (obj);

		// get version
		SimpleVersion version = ((SimpleVersion) obj);

		// check version
		if (version.getMajor () != this.getMajor ()) return false;
		if (version.getMinor () != this.getMinor ()) return false;
		if (version.getMaintenance () != this.getMaintenance ()) return false;
		if (version.getBuild () != this.getBuild ()) return false;

		if (version.isUnstableBuild () != this.isUnstableBuild ()) return false;
		if (version.isAlphaBuild () != this.isAlphaBuild ()) return false;
		if (version.isBetaBuild () != this.isBetaBuild ()) return false;
		if (version.isSnapshotBuild () != this.isSnapshotBuild ()) return false;
		if (version.isReleaseCandidateBuild () != this.isReleaseCandidateBuild ()) return false;
		if (!this.isSnapshotBuild () && !version.isSnapshotBuild () && version.getUnstableVersion () != this.getUnstableVersion ()) return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString () {
		return this.toString (true);
	}

	/**
	 * Converts the version as a String.
	 * @param ignoreBuild When set to false, appends the build number even when set to 0.
	 * @return A string representation.
	 */
	public String toString (boolean ignoreBuild) {
		// create string builder
		StringBuilder builder = new StringBuilder ();

		// add major bit
		builder.append (this.getMajor ());

		// add minor bit
		builder.append (VERSION_SEPARATOR);
		builder.append (this.getMinor ());

		// add maintenance bit
		builder.append (VERSION_SEPARATOR);
		builder.append (this.getMaintenance ());

		// add build bit
		if (this.getBuild () > 0 || !ignoreBuild) {
			builder.append (VERSION_SEPARATOR);
			builder.append (this.getBuild ());
		}

		// add extra bit
		if (this.getExtra () != null && !this.getExtra ().isEmpty ()) {
			builder.append (VERSION_EXTRA_SEPARATOR);
			builder.append (this.getExtra ());
		}

		// return finished version string
		return builder.toString ();
	}
}