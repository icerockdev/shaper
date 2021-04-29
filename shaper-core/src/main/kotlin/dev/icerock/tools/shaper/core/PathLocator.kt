/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

object PathLocator {

    private val dirMapList = mutableMapOf<String, String>()

    fun set(path: String, base: String) = dirMapList.put(path, base)

    fun getBaseDir(path: String) = dirMapList[path]

    fun getAbsolutePath(path: String) = getBaseDir(path) + "/" + path

}
