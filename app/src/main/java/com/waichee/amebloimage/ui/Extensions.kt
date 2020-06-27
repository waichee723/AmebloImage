package com.waichee.amebloimage.ui

fun String.isAmeblo(): Boolean {
    return this.startsWith("https://ameblo.jp/")
}

fun String.isAmebloEntry(): Boolean {
    return this.contains("entry-")
}