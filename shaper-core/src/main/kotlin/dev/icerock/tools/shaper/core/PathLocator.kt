package dev.icerock.tools.shaper.core

object PathLocator {

    private val dirMapList = mutableMapOf<String, String>()

    fun set(path: String, base: String) = dirMapList.put(path, base)

    fun getBaseDir(path: String) = dirMapList[path]

    fun getAbsolutePath(path: String) = getBaseDir(path) + "/" + path

}
