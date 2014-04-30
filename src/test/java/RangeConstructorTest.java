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
public class RangeConstructorTest {

	/**
	 * Tests the default constructor.
	 * @throws VersionParserException
	 */
	@Test
	public void defaultConstructor () throws VersionParserException {
		// create version range
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion version2 = new SimpleVersion (2, 0, 0, 0, null);

		// create range
		SimpleVersionRange range = new SimpleVersionRange (version1, false, version2, false);

		// verify getters
		Assert.assertEquals (version1, range.getFloorVersion ());
		Assert.assertEquals (version2, range.getCeilingVersion ());
		Assert.assertFalse (range.isCeilingFuzzy ());
		Assert.assertFalse (range.isFloorVersionFuzzy ());
	}

	/**
	 * Tests the default constructor error detection.
	 * @throws VersionParserException
	 */
	@Test (expected = NullPointerException.class)
	public void defaultConstructorNullError () throws VersionParserException {
		new SimpleVersionRange (((SimpleVersion) null), false, ((SimpleVersion) null), false);
	}

	/**
	 * Tests the default constructor error detection.
	 * @throws VersionParserException
	 */
	@Test (expected = VersionParserException.class)
	public void defaultConstructorRangeError () throws VersionParserException {
		// create version range
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion version2 = new SimpleVersion (2, 0, 0, 0, null);

		// create range
		SimpleVersionRange range = new SimpleVersionRange (version2, false, version1, false);
	}

	/**
	 * Tests the simplified constructor.
	 * @throws VersionParserException
	 */
	@Test
	public void simplified () throws VersionParserException {
		// create version range
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion version2 = new SimpleVersion (2, 0, 0, 0, null);

		// create range
		SimpleVersionRange range = new SimpleVersionRange (version1.toString (), version2.toString ());

		// verify getters
		Assert.assertEquals (version1, range.getFloorVersion ());
		Assert.assertEquals (version2, range.getCeilingVersion ());
		Assert.assertTrue (range.isCeilingFuzzy ());
		Assert.assertTrue (range.isFloorVersionFuzzy ());
	}

	/**
	 * Tests the parser constructor.
	 * @throws VersionParserException
	 */
	@Test
	public void parser () throws VersionParserException {
		// create version range
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion version2 = new SimpleVersion (2, 0, 0, 0, null);

		// create range string
		String rangeString = SimpleVersionRange.FLOOR_SELECTOR_FUZZY + version1.toString () + SimpleVersionRange.RANGE_SEPARATOR + " " + version2.toString () + SimpleVersionRange.CEIL_SELECTOR;

		// parse
		SimpleVersionRange range = new SimpleVersionRange (rangeString);

		// verify
		Assert.assertEquals (version1, range.getFloorVersion ());
		Assert.assertEquals (version2, range.getCeilingVersion ());
		Assert.assertTrue (range.isFloorVersionFuzzy ());
		Assert.assertFalse (range.isCeilingFuzzy ());

		// create range string
		rangeString = version1.toString ();

		// parse
		range = new SimpleVersionRange (rangeString);

		// verify
		Assert.assertEquals (version1, range.getFloorVersion ());
		Assert.assertNull (range.getCeilingVersion ());
		Assert.assertTrue (range.isFloorVersionFuzzy ());
		Assert.assertFalse (range.isCeilingFuzzy ());
		Assert.assertTrue (range.isInRange (version2));
	}

	/**
	 * Tests the parser constructor against the specification.
	 * @throws VersionParserException
	 */
	public void parserSpecification () throws VersionParserException {
		new SimpleVersionRange ("(1.0.0,2.0.0]");
		new SimpleVersionRange ("(1.0.0, 2.1.5");
	}

	/**
	 * Tests the parser error detection.
	 * @throws VersionParserException
	 */
	@Test (expected = VersionParserException.class)
	public void parserEmptyError () throws VersionParserException {
		new SimpleVersionRange ("");
	}

	/**
	 * Tests the parser error detection.
	 * @throws VersionParserException
	 */
	@Test (expected = NullPointerException.class)
	public void parserNullError () throws VersionParserException {
		new SimpleVersionRange (null);
	}

	/**
	 * Tests the parser error detection.
	 * @throws VersionParserException
	 */
	@Test (expected = VersionParserException.class)
	public void parserPrefixError () throws VersionParserException {
		new SimpleVersionRange ("A1.0.0.0,2.0.0.0]");
	}

	/**
	 * Tests the parser error detection.
	 * @throws VersionParserException
	 */
	@Test (expected = VersionParserException.class)
	public void parseRangeError () throws VersionParserException {
		new SimpleVersionRange ("(2.0.0,1.0.0]");
	}

	/**
	 * Tests the parser error detection.
	 * @throws VersionParserException
	 */
	@Test (expected = VersionParserException.class)
	public void parserRangeElementsError () throws VersionParserException {
		new SimpleVersionRange ("(1.0.0.0]");
	}

	/**
	 * Tests the parser error detection.
	 * @throws VersionParserException
	 */
	@Test (expected = VersionParserException.class)
	public void parserSuffixError () throws VersionParserException {
		new SimpleVersionRange ("(1.0.0.0,2.0.0.0A");
	}
}