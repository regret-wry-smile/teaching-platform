/*
 * @(#)IOUtils.java		       Project:com.sinaapp.msdxblog.androidkit
 *
 * 此文件代码完全抽取自apache开源项目commons中的commons-io包。
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
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
package com.ejet.core.util.io;

import java.io.Closeable;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * 此文件代码完全抽取自apache开源项目commons中的commons-io包。
 * 
 */
public class IOUtils {
	
	//public static final String TAG = IOUtils.class.getSimpleName();
	
	private IOUtils() {
		
	}
	/**
	 * Unconditionally close a <code>Closeable</code>.
	 * <p>
	 * Equivalent to {@link Closeable#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * Closeable closeable = null;
	 * try {
	 * 	closeable = new FileReader(&quot;foo.txt&quot;);
	 * 	// process closeable
	 * 	closeable.close();
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	IOUtils.closeQuietly(closeable);
	 * }
	 * </pre>
	 * 
	 * @param closeable
	 *            the object to close, may be null or already closed
	 * @since Commons IO 2.0
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Throwable ioe) {
			// ignore
			ioe.printStackTrace();
		}
	}
	
	
	/**
	 * error message
	 * 
	 * @param ex
	 * @return
	 */
	public static String getError(Throwable ex) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			Throwable cause = ex.getCause();
			while (null != cause) {
				cause.printStackTrace(pw);
				cause = cause.getCause();
			}
			sw.append(pw.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeQuietly(sw);
			closeQuietly(pw);
		}
		return sw.toString();
	}
	
	
}
