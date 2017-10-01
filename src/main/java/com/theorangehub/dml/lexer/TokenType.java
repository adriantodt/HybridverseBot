/*
 *  Copyright 2017 An Tran and Adrian Todt
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.theorangehub.dml.lexer;

public enum TokenType {

    /** The Character '='. */
    EQUALS,

    /** The Character '&gt;'. */
    GT,

    /** The Character '&lt;'. */
    LT,

    /** The Character '/'. */
    SLASH,

    /**
     * Text outside the tags
     */
    TEXT,

    /**
     * Tag identifier
     */
    IDENTIFIER,

    /**
     * Strings (values) of the identifiers inside a tag
     */
    STRING,

    /**
     * End of File
     */
    EOF,
}
