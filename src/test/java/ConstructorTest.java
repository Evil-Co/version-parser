import com.evilco.version.SimpleVersion;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
@RunWith (MockitoJUnitRunner.class)
public class ConstructorTest {

	/**
	 * Tests the default constructor.
	 */
	@Test
	public void defaultConstructor () {
		// create version instance
		SimpleVersion version = new SimpleVersion (1, 2, 3, 4, "EXTRA");

		// verify contents
		Assert.assertEquals (1, version.getMajor ());
		Assert.assertEquals (2, version.getMinor ());
		Assert.assertEquals (3, version.getMaintenance ());
		Assert.assertEquals (4, version.getBuild ());
		Assert.assertEquals ("EXTRA", version.getExtra ());
	}

	/**
	 * Tests the empty SimpleVersion constructor.
	 */
	@Test
	public void empty () {
		// create version instance
		SimpleVersion version = new SimpleVersion ();

		// verify contents
		Assert.assertEquals (1, version.getMajor ());
		Assert.assertEquals (0, version.getMinor ());
		Assert.assertEquals (0, version.getMaintenance ());
		Assert.assertEquals (0, version.getBuild ());
		Assert.assertEquals (null, version.getExtra ());
	}

	/**
	 * Tests the version parser constructor.
	 */
	@Test
	public void parser () {
		// create test parameters
		int major = 2;
		int minor = 3;
		int maintenance = 4;
		int build = 5;
		String extra = "SNAPSHOT-TEST";

		// build complete test string
		String version = major + SimpleVersion.VERSION_SEPARATOR + minor + SimpleVersion.VERSION_SEPARATOR + maintenance + SimpleVersion.VERSION_SEPARATOR + build + SimpleVersion.VERSION_EXTRA_SEPARATOR + extra;

		// create version instance
		SimpleVersion parsedVersion = new SimpleVersion (version);

		// verify instance
		Assert.assertEquals (major, parsedVersion.getMajor ());
		Assert.assertEquals (minor, parsedVersion.getMinor ());
		Assert.assertEquals (maintenance, parsedVersion.getMaintenance ());
		Assert.assertEquals (build, parsedVersion.getBuild ());
		Assert.assertEquals (extra, parsedVersion.getExtra ());
		Assert.assertTrue (parsedVersion.isUnstableBuild ());
		Assert.assertFalse (parsedVersion.isAlphaBuild ());
		Assert.assertFalse (parsedVersion.isBetaBuild ());
		Assert.assertTrue (parsedVersion.isSnapshotBuild ());
		Assert.assertFalse (parsedVersion.isReleaseCandidateBuild ());

		// build complete test string
		version = major + SimpleVersion.VERSION_SEPARATOR + minor;

		// create version instance
		parsedVersion = new SimpleVersion (version);

		// verify instance
		Assert.assertEquals (major, parsedVersion.getMajor ());
		Assert.assertEquals (minor, parsedVersion.getMinor ());
		Assert.assertEquals (0, parsedVersion.getMaintenance ());
		Assert.assertEquals (0, parsedVersion.getBuild ());
		Assert.assertFalse (parsedVersion.isUnstableBuild ());
		Assert.assertFalse (parsedVersion.isAlphaBuild ());
		Assert.assertFalse (parsedVersion.isBetaBuild ());
		Assert.assertFalse (parsedVersion.isSnapshotBuild ());
		Assert.assertFalse (parsedVersion.isReleaseCandidateBuild ());

		// build complete test string
		version = major + SimpleVersion.VERSION_SEPARATOR + minor + SimpleVersion.VERSION_SEPARATOR + maintenance;

		// create version instance
		parsedVersion = new SimpleVersion (version);

		// verify instance
		Assert.assertEquals (major, parsedVersion.getMajor ());
		Assert.assertEquals (minor, parsedVersion.getMinor ());
		Assert.assertEquals (maintenance, parsedVersion.getMaintenance ());
		Assert.assertEquals (0, parsedVersion.getBuild ());
		Assert.assertFalse (parsedVersion.isUnstableBuild ());
		Assert.assertFalse (parsedVersion.isAlphaBuild ());
		Assert.assertFalse (parsedVersion.isBetaBuild ());
		Assert.assertFalse (parsedVersion.isSnapshotBuild ());
		Assert.assertFalse (parsedVersion.isReleaseCandidateBuild ());

		// build complete test string
		version = major + SimpleVersion.VERSION_SEPARATOR + minor + SimpleVersion.VERSION_SEPARATOR + maintenance + SimpleVersion.VERSION_SEPARATOR + build;

		// create version instance
		parsedVersion = new SimpleVersion (version);

		// verify instance
		Assert.assertEquals (major, parsedVersion.getMajor ());
		Assert.assertEquals (minor, parsedVersion.getMinor ());
		Assert.assertEquals (maintenance, parsedVersion.getMaintenance ());
		Assert.assertEquals (build, parsedVersion.getBuild ());
		Assert.assertFalse (parsedVersion.isUnstableBuild ());
		Assert.assertFalse (parsedVersion.isAlphaBuild ());
		Assert.assertFalse (parsedVersion.isBetaBuild ());
		Assert.assertFalse (parsedVersion.isSnapshotBuild ());
		Assert.assertFalse (parsedVersion.isReleaseCandidateBuild ());
	}

	/**
	 * Tests the parser parameter verification.
	 */
	@Test (expected = NullPointerException.class)
	public void parserNull () {
		new SimpleVersion (((String) null));
	}
}