import com.evilco.version.SimpleVersion;
import com.evilco.version.SimpleVersionRange;
import com.evilco.version.VersionParserException;
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
public class MiscTest {

	/**
	 * Tests the export feature.
	 */
	@Test
	public void export () {
		// create versions
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion version2 = new SimpleVersion (2, 0, 0, 0, null);
		SimpleVersion version3 = new SimpleVersion (1, 1, 0, 0, null);
		SimpleVersion version4 = new SimpleVersion (1, 0, 1, 0, null);
		SimpleVersion version5 = new SimpleVersion (1, 0, 0, 1, null);
		SimpleVersion version6 = new SimpleVersion (1, 2, 3, 4, "EXTRA");

		// check
		Assert.assertEquals ("1.0.0", version1.toString ());
		Assert.assertEquals ("2.0.0", version2.toString ());
		Assert.assertEquals ("1.1.0", version3.toString ());
		Assert.assertEquals ("1.0.1", version4.toString ());
		Assert.assertEquals ("1.0.0.1", version5.toString ());
		Assert.assertEquals ("1.2.3.4-EXTRA", version6.toString ());

		Assert.assertEquals ("1.0.0.0", version1.toString (false));
	}

	/**
	 * Tests the range export method.
	 * @throws VersionParserException
	 */
	@Test
	public void exportRange () throws VersionParserException {
		// create ranges
		SimpleVersionRange range1 = new SimpleVersionRange ("1.0.0", "2.0.0");
		SimpleVersionRange range2 = new SimpleVersionRange ("1.0.0", true, "2.0.0", false);
		SimpleVersionRange range3 = new SimpleVersionRange ("1.0.0", false, "2.0.0", true);
		SimpleVersionRange range4 = new SimpleVersionRange ("1.0.0", false, "2.0.0", false);

		// check
		Assert.assertEquals ("[1.0.0,2.0.0]", range1.toString ());
		Assert.assertEquals ("[1.0.0,2.0.0)", range2.toString ());
		Assert.assertEquals ("(1.0.0,2.0.0]", range3.toString ());
		Assert.assertEquals ("(1.0.0,2.0.0)", range4.toString ());
	}

	/**
	 * Tests the isInRange method.
	 * @throws VersionParserException
	 */
	@Test
	public void inRange () throws VersionParserException {
		// create versions
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion version2 = new SimpleVersion (2, 0, 0, 0, null);

		SimpleVersion testVersion1 = new SimpleVersion (1, 2, 0, 0, null);
		SimpleVersion testVersion2 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion testVersion3 = new SimpleVersion (2, 0, 0, 0, null);

		// create range
		SimpleVersionRange range = new SimpleVersionRange (version1, false, version2, true);

		// check
		Assert.assertTrue (range.isInRange (testVersion1));
		Assert.assertFalse (range.isInRange (testVersion2));
		Assert.assertTrue (range.isInRange (testVersion3));
	}

	/**
	 * Tests the isInRange method (with open end ranges).
	 * @throws VersionParserException
	 */
	@Test
	public void inRangeHigher () throws VersionParserException {
		// create versions
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);

		SimpleVersion testVersion1 = new SimpleVersion (5, 0, 0, 0, null);

		// create range
		SimpleVersionRange range = new SimpleVersionRange (version1, true, null, false);

		// check
		Assert.assertTrue (range.isInRange (testVersion1));
		Assert.assertTrue (range.isInRange (version1));
	}
}