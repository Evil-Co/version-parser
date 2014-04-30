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
public class VersionUnstableTest {

	/**
	 * Tests snapshot detection.
	 */
	@Test
	public void snapshot () {
		// create version
		SimpleVersion version = new SimpleVersion (1, 0, 0, 0, "SNAPSHOT");

		// check detection
		Assert.assertTrue (version.isSnapshotBuild ());
		Assert.assertFalse (version.isAlphaBuild ());
		Assert.assertFalse (version.isBetaBuild ());
		Assert.assertFalse (version.isReleaseCandidateBuild ());
		Assert.assertTrue (version.isUnstableBuild ());
	}

	/**
	 * Tests alpha detection.
	 */
	@Test
	public void alpha () {
		// create version
		SimpleVersion version = new SimpleVersion (1, 0, 0, 0, "ALPHA-1");

		// check detection
		Assert.assertTrue (version.isAlphaBuild ());
		Assert.assertFalse (version.isSnapshotBuild ());
		Assert.assertFalse (version.isBetaBuild ());
		Assert.assertFalse (version.isReleaseCandidateBuild ());
		Assert.assertTrue (version.isUnstableBuild ());

		// create version
		version = new SimpleVersion (1, 0, 0, 0, "A-1");

		// check detection
		Assert.assertTrue (version.isAlphaBuild ());
		Assert.assertFalse (version.isSnapshotBuild ());
		Assert.assertFalse (version.isBetaBuild ());
		Assert.assertFalse (version.isReleaseCandidateBuild ());
		Assert.assertTrue (version.isUnstableBuild ());
	}

	/**
	 * Tests beta detection.
	 */
	@Test
	public void beta () {
		// create version
		SimpleVersion version = new SimpleVersion (1, 0, 0, 0, "BETA-1");

		// check detection
		Assert.assertTrue (version.isBetaBuild ());
		Assert.assertFalse (version.isSnapshotBuild ());
		Assert.assertFalse (version.isAlphaBuild ());
		Assert.assertFalse (version.isReleaseCandidateBuild ());
		Assert.assertTrue (version.isUnstableBuild ());

		// create version
		version = new SimpleVersion (1, 0, 0, 0, "B-1");

		// check detection
		Assert.assertTrue (version.isBetaBuild ());
		Assert.assertFalse (version.isSnapshotBuild ());
		Assert.assertFalse (version.isAlphaBuild ());
		Assert.assertFalse (version.isReleaseCandidateBuild ());
		Assert.assertTrue (version.isUnstableBuild ());
	}

	/**
	 * Tests release candidate detection.
	 */
	@Test
	public void releaseCandidate () {
		// create version
		SimpleVersion version = new SimpleVersion (1, 0, 0, 0, "RC-1");

		// check detection
		Assert.assertTrue (version.isReleaseCandidateBuild ());
		Assert.assertFalse (version.isSnapshotBuild ());
		Assert.assertFalse (version.isAlphaBuild ());
		Assert.assertFalse (version.isBetaBuild ());
		Assert.assertTrue (version.isUnstableBuild ());
	}
}