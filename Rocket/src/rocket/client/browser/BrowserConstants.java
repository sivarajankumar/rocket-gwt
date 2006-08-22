/*
 * Copyright 2006 NSW Police Government Australia
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.client.browser;

import java.util.Date;



public class BrowserConstants {
    public final static String IMAGES = "/images";

    public final static String INTERNET_EXPLORER_USER_AGENT = "MSIE";
    public final static String FIREFOX_USER_AGENT = "Firefox";

    
    // COOKIE CONSTANTS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static char COOKIE_SEPARATOR = ',';
    public final static String COOKIE_SEPARATOR_STRING = "" + COOKIE_SEPARATOR;
    public final static char COOKIE_ATTRIBUTE_SEPARATOR = ';';
    public final static String COOKIE_ATTRIBUTE_SEPARATOR_STRING = "" + COOKIE_ATTRIBUTE_SEPARATOR;
    public final static char COOKIE_ATTRIBUTE_NAME_VALUE_SEPARATOR = '=';

    public final static String COOKIE_COMMENT = "comment";
    public final static String COOKIE_DOMAIN = "domain";
    public final static String COOKIE_EXPIRES = "max-age";
    public final static String COOKIE_PATH = "path";
    public final static String COOKIE_SECURE = "secure";
    public final static String COOKIE_VERSION = "version";
    
	final static String REMOVE_COOKIE_EXPIRES_DATE = new Date( 0 ).toGMTString();
}