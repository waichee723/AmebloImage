package com.waichee.amebloimage

fun String.isAmeblo(): Boolean {
    return this.startsWith("https://ameblo.jp/")
}

fun String.isAmebloEntry(): Boolean {
    return this.contains("entry-")
}