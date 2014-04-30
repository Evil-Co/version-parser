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
public class ComparisonTest {

	/**
	 * Tests whether versions detect equality properly.
	 */
	@Test
	public void equals () {
		// create versions
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion version2 = new SimpleVersion (1, 0, 0, 0, "TEST");
		SimpleVersion version3 = new SimpleVersion (2, 0, 0, 0, null);

		// compare
		Assert.assertTrue (version1.equals (version2));
		Assert.assertFalse (version1.equals (version3));
	}

	/**
	 * Tests whether versions detect newer versions properly.
	 */
	@Test
	public void newer () {
		// create versions
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion version2 = new SimpleVersion (1, 0, 0, 0, "ALPHA-1");
		SimpleVersion version3 = new SimpleVersion (1, 0, 0, 0, "BETA-1");
		SimpleVersion version4 = new SimpleVersion (1, 0, 0, 0, "SNAPSHOT");
		SimpleVersion version5 = new SimpleVersion (1, 0, 0, 0, "RC-1");
		SimpleVersion version6 = new SimpleVersion (1, 0, 0, 0, "RC-2");
		SimpleVersion version7 = new SimpleVersion (2, 0, 0, 0, null);

		// compare
		Assert.assertTrue (version1.newer (version2));
		Assert.assertTrue (version1.newer (version3));
		Assert.assertTrue (version1.newer (version4));
		Assert.assertTrue (version1.newer (version5));
		Assert.assertTrue (version1.newer (version6));
		Assert.assertFalse (version1.newer (version7));

		Assert.assertTrue (version3.newer (version2));
		Assert.assertTrue (version4.newer (version3));
		Assert.assertTrue (version5.newer (version4));
		Assert.assertTrue (version6.newer (version5));

		Assert.assertFalse (version1.newer (version1));
	}

	/**
	 * Tests whether versions detect older versions properly.
	 */
	@Test
	public void older () {
		// create versions
		SimpleVersion version1 = new SimpleVersion (1, 0, 0, 0, null);
		SimpleVersion version2 = new SimpleVersion (1, 0, 0, 0, "ALPHA-1");
		SimpleVersion version3 = new SimpleVersion (1, 0, 0, 0, "BETA-1");
		SimpleVersion version4 = new SimpleVersion (1, 0, 0, 0, "SNAPSHOT");
		SimpleVersion version5 = new SimpleVersion (1, 0, 0, 0, "RC-1");
		SimpleVersion version6 = new SimpleVersion (1, 0, 0, 0, "RC-2");
		SimpleVersion version7 = new SimpleVersion (2, 0, 0, 0, null);

		// compare
		Assert.assertTrue (version2.older (version1));
		Assert.assertTrue (version3.older (version1));
		Assert.assertTrue (version4.older (version1));
		Assert.assertTrue (version5.older (version1));
		Assert.assertTrue (version6.older (version1));
		Assert.assertFalse (version7.older (version1));

		Assert.assertTrue (version5.older (version6));
		Assert.assertTrue (version4.older (version5));
		Assert.assertTrue (version3.older (version4));
		Assert.assertTrue (version2.older (version3));

		Assert.assertFalse (version1.older (version1));
	}
}