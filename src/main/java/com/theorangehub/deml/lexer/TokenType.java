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

package com.theorangehub.deml.lexer;

public enum TokenType {
    /*// PAIRS
    LEFT_PAREN,
    RIGHT_PAREN,

    LEFT_BRACKET,
    RIGHT_BRACKET,

    LEFT_BRACE,
    RIGHT_BRACE,

    OPTIONAL_ASSIGN,
    ELVIS,

    // ASSIGNMENT
    ASSIGN,

    // TYPES
    INT,
    NUMBER,
    BOOLEAN,
    FUNCTION,
    STRING,
    ATOM,

    // ARITHMETIC
    PLUS,
    MINUS,
    ASTERISK,
    SLASH,
    BACKSLASH,
    CARET,
    PERCENT,

    // RELATIONAL
    EQUALS,
    NOT_EQUAL,
    GT,
    GTE,
    LT,
    LTE,
    OR,
    AND,

    // BOOLEAN
    AMPERSAND,
    VERTICAL_BAR,

    // MISC
    REST,
    RANGE_TO,
    PIPE_FORWARD,
    PIPE_BACKWARD,
    SHIFT_RIGHT,
    SHIFT_LEFT,
    ARROW,
    TILDE,
    BANG,
    QUESTION,
    COLON,
    COMMA,
    DOT,

    UNDERSCORE,

    // SCRIPT
    MATCH,
    CASE,
    MODULE,
    TYPE,
    IDENTIFIER,
    NULL,
    LET,
    RETURN,
    IS,
    IF,
    ELSE,
    FOR,*/

    /** The Character '='. */
    EQUALS,

    /** The Character '&gt;'. */
    GT,

    /** The Character '&lt;'. */
    LT,

    /** The Character '/'. */
    SLASH,

    TEXT, IDENTIFIER, STRING,

    EOF,
}
