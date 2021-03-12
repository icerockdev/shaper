/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */


package dev.icerock.tools.shaper.core

import kotlin.test.Test
import kotlin.test.assertEquals

class StringExtTest {
    @Test
    fun `camel to snake case cases`() {
        assertEquals(
            expected = "test_words_in_camel",
            actual = "testWordsInCamel".camelToSnakeCase()
        )
        assertEquals(
            expected = "test_words_in_camel",
            actual = "TestWordsInCamel".camelToSnakeCase()
        )
    }

    @Test
    fun `snake to upper camel case cases`() {
        assertEquals(
            expected = "TestWordsInCamel",
            actual = "test_words_in_camel".snakeToUpperCamelCase()
        )
    }

    @Test
    fun `snake to lower camel case cases`() {
        assertEquals(
            expected = "testWordsInCamel",
            actual = "test_words_in_camel".snakeToLowerCamelCase()
        )
    }
}