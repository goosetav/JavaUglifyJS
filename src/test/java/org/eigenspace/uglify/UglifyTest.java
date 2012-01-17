/**
 * 
 */
package org.eigenspace.uglify;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

/**
 * @author erik
 * 
 */
public class UglifyTest {

	/**
	 * Test method for {@link org.eigenspace.Uglify#minify(java.lang.String)}.
	 */
	@Test
	public void testCompressString() {

		String s = "function longname(longarg) { return longarg }";

		Uglify u = new Uglify();

		try {
			String r = u.minify(s);
			assertTrue(r != null);
			assertTrue(r.length() < s.length());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void testCompressWithBigFile() {

		Uglify u = new Uglify();
		
		try {
			InputStream in = getClass().getResourceAsStream("/jquery-1.7.1.js");
			
			String s = new Scanner(in).useDelimiter("\\A").next();
			
			System.out.println("Original: " + s.length());
			
			String r = u.minify(s);
			
			assertTrue(r != null);
			assertTrue(r.length() < s.length());
			
			System.out.println("Minified: " + r.length());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
}
