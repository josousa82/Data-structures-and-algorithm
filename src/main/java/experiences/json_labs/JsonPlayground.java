/*
 * Copyright (c) 2021 sousaJ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package experiences.json_labs;


import org.apache.commons.text.StringEscapeUtils;

import java.util.ResourceBundle;

/**
 * Created by sousaJ on 23/03/2021
 * in package - experiences.json_labs
 **/
public class JsonPlayground {

    public static void main(String[] args) {

        ResourceBundle bundle = ResourceBundle.getBundle("sql");

        StringEscapeUtils.Builder escapeJson = StringEscapeUtils.builder(StringEscapeUtils.ESCAPE_JSON);
        StringEscapeUtils.Builder unescapeJson = StringEscapeUtils.builder(StringEscapeUtils.UNESCAPE_JSON);

//        escapeJson.escape(bundle.getString("request"));

//        System.out.println("escaped = " +  escapeJson.escape(bundle.getString("requestTemplate")));
        System.out.println("\n\n\n\n\n\nescaped = " +  escapeJson.escape(bundle.getString("status_map")));
        System.out.println("\n\n\n\n\n\nunescaped = " +  unescapeJson.escape(bundle.getString("status_map")));

//        System.out.println(bundle.getString("jsonTest"));


        System.out.println("test".substring(2, 10));
    }
}
