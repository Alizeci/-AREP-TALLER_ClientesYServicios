package co.edu.escuelaing.arep.networking.httpserver;

import java.util.HashMap;

//Copyright (c) 2003-2009, Jodd Team (jodd.org). All Rights Reserved.

/**
* Map file extensions to MIME types. Based on the Apache mime.types file.
* http://www.iana.org/assignments/media-types/
*/
public class MimeType {
	
	public static final String MIME_APPLICATION_JSON = "application/json";
	public static final String MIME_APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String MIME_IMAGE_GIF = "image/gif";
	public static final String MIME_IMAGE_IEF = "image/ief";
	public static final String MIME_IMAGE_JPEG = "image/jpeg";
	public static final String MIME_IMAGE_TIFF = "image/tiff";
	public static final String MIME_IMAGE_PNG = "image/png";
	public static final String MIME_IMAGE_X_ICON = "image/x-icon";
	public static final String MIME_TEXT_HTML = "text/html";
	public static final String MIME_TEXT_CSS = "text/css";
	public static final String MIME_TEXT_JS = "text/javascript";
	
	private static HashMap<String, String> mimeTypeMapping;
	private static HashMap<String, String> extMapping;
	
	static {
		mimeTypeMapping = new HashMap<String, String>(20) {
			private void put1(String key, String value) {
				if (put(key, value) != null) {
					throw new IllegalArgumentException("Duplicated extension: " + key);
				}
			}

			{
				put1("json", MIME_APPLICATION_JSON);
				put1("ico", MIME_IMAGE_X_ICON);
				put1("js", MIME_TEXT_JS);
				put1("html", MIME_TEXT_HTML);
				put1("htm", MIME_TEXT_HTML);
				put1("gif", MIME_IMAGE_GIF);
				put1("jpeg", MIME_IMAGE_JPEG);
				put1("jpg", MIME_IMAGE_JPEG);
				put1("jpe", MIME_IMAGE_JPEG);
				put1("tiff", MIME_IMAGE_TIFF);
				put1("tif", MIME_IMAGE_TIFF);
				put1("png", MIME_IMAGE_PNG);
				put1("css", MIME_TEXT_CSS);
			}
		};
	}
	
	static {
	    extMapping = new HashMap<String, String>(20) {
	        private void put1(String key, String value) {
	            if (put(key, value) != null) {
	                throw new IllegalArgumentException("Duplicated Mimetype: " + key);
	            }
	        }

	        {
	            put1(MIME_APPLICATION_JSON, "json");
	            put1(MIME_TEXT_CSS, "css");
	            put1(MIME_IMAGE_X_ICON, "ico");
	            put1(MIME_IMAGE_IEF, "ief");
	            put1(MIME_TEXT_HTML, "html");
	            put1(MIME_IMAGE_GIF, "gif");
	            put1(MIME_IMAGE_JPEG, "jpg");
	            put1(MIME_IMAGE_TIFF, "tiff");
	            put1(MIME_IMAGE_PNG, "png");
	            put1(MIME_APPLICATION_OCTET_STREAM, "exe");
	        }
	    };
	}

	/**
	 * Registers MIME type for provided extension. Existing extension type will be
	 * overriden.
	 * @param ext - Extension of MimeType file
	 * @param mimeType - Type of content
	 */
	public static void registerMimeType(String ext, String mimeType) {
		mimeTypeMapping.put(ext, mimeType);
	}

	/**
	 * Returns the corresponding MIME type to the given extension. If no MIME type
	 * was found it returns 'application/octet-stream' type.
	 * @param ext - Extension of MimeType file
	 * @return the MimeType content
	 */
	public static String getMimeType(String ext) {
		String newExt = ext.substring(ext.indexOf(".")+1,ext.length());
		
		String mimeType = lookupMimeType(newExt);
		
		if (mimeType == null) {
			mimeType = MIME_APPLICATION_OCTET_STREAM;
		}
		return mimeType;
	}
	
	/**
	 * Simply returns Ext or <code>null</code> if no Mimetype is found.
	 * @param mimeType - Type of content
	 * @return The extension of the mimeType
	 */
	public static String getExt(String mimeType) {
	    return extMapping.get(mimeType.toLowerCase());
	}

	/**
	 * Simply returns MIME type or <code>null</code> if no type is found.
	 * @param ext - Extension of MimeType file
	 * @return the MimeType content
	 */
	public static String lookupMimeType(String ext) {
		return mimeTypeMapping.get(ext.toLowerCase());
	}

}
