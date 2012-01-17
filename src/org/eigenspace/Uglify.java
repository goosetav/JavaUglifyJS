package org.eigenspace;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 * Run UglifyJS using Rhino
 * 
 * @author Erik Gustavson
 * @since January 2012
 * 
 * @credit: Mihai Bazon (mishoo) for Uglify - https://github.com/mishoo/UglifyJS 
 * @credit: Mozilla for the Rhino runtime - http://www.mozilla.org/rhino 
 * @credit: Alex Objelean (alexo) for wroj4j which inspired this project and showing how to shim CommonJS to fix module loading - https://github.com/alexo/wro4j
 * @credit: Steven Haines for a nice tutorial on how to use Rhino - http://www.informit.com/guides/content.aspx?g=java&seqNum=562
 * 
 * (C) Erik Gustavson, 2012
 * 
 * License: Apache License, Version 2.0.
 * http://www.apache.org/licenses/LICENSE-2.0.html
 */
public class Uglify {

	/**
	 * Convenience method to call minify a single file
	 * 
	 * @param uncompressedJavascript
	 *            The js you want to compress
	 * @return a String that represents js compressed with Uglify
	 * 
	 * @throws Exception
	 *             general exception if something goes horribly wrong
	 */
	public String minify(String uncompressedJavascript) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add(uncompressedJavascript);

		return minify(list).get(0);
	}

	/**
	 * Minify
	 * 
	 * @param uncompressedJavascripts
	 * @return
	 * @throws Exception
	 */
	public List<String> minify(List<String> uncompressedJavascripts) throws Exception {
		// Create and enter a Context. A Context stores information about the
		// execution environment of a script.
		Context cx = Context.enter();
		List<String> results = new ArrayList<String>(uncompressedJavascripts.size());

		try {
			// Initialize the context
			Scriptable scope = cx.initStandardObjects();

			// Add all the libraries we need
			List<String> libs = new ArrayList<String>();

			libs.add("shim.js");
			libs.add("lib/parse-js.js");
			libs.add("lib/process.js");
			libs.add("uglify-js.js");

			for (String library : libs) {
				try {
					InputStream in = getClass().getResourceAsStream(library);
					cx.evaluateReader(scope, new InputStreamReader(in), library, 1, null);
				} catch (Exception e) {
					throw new Exception("Problem loading library: " + library, e);
				}
			}

			// get a handle to our compression function
			Function compress = (Function) scope.get("uglify", scope);

			for (String uncompressedJavascript : uncompressedJavascripts) {

				// put our uncompressed javascript as an input
				Object args[] = { uncompressedJavascript, null };

				// run it
				Object result = compress.call(cx, scope, scope, args);
				results.add(Context.toString(result));
			}

			return results;

		} finally {
			Context.exit();
		}
	}
}