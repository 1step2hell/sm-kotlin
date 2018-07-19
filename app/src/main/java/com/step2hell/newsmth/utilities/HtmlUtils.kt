package com.step2hell.newsmth.utilities

import java.net.URL
import java.nio.charset.Charset
import java.util.regex.Pattern

object HtmlUtils {

    @Throws(Exception::class)
    fun getHTML(url: String): String {
        val inputStream = URL(url).openConnection().getInputStream()
        val buf = ByteArray(1024)
        val sb = StringBuilder()
        while (inputStream.read(buf, 0, buf.size) > 0) {
            sb.append(String(buf, Charset.forName("GBK")))
        }
        inputStream.close()
        return sb.toString()
    }

    fun getSubSample(soap: String, rgex: String): String {
        val pattern = Pattern.compile(rgex)
        val m = pattern.matcher(soap)
        while (m.find()) {
            return m.group(1)
        }
        return ""
    }

}