package com.example.reptiles

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.ScriptException
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener
import com.google.gson.Gson
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.MalformedURLException
import java.net.URL
import java.text.DecimalFormat

/**
 * 法术
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/2/21 23:59
 * Description: com.example.reptiles
 */
object MagicManage {
    var updaterDetails: UpdaterDetails? = null
    private lateinit var imgurl: String
    private var httpIconList: MutableList<String>? = null

    val totalList = mutableListOf<UpdaterDetails>()


    private val updateList = mutableListOf<MutableList<String>>()
    lateinit var pageListData: PageListData

    private val iconBean = BaseBean<String>()


    private val oldList = mutableListOf<String>()

    /**
     * 解析无换行、无空格、去*号
     * @param size 网页长度+1
     * @param zhiyuan 资源转成万  网页长度 0开始
     * @param isDowImg 是否下载图片
     */
    fun upData(element: Document, size: Int, zhiyuan: Int, isDowImg: Boolean = false) {
        val updateData = element.getElementsByClass("mdui-m-t-2 update-data-table")[0]
        updateData.getElementsByTag("tr").forEachIndexed { index, it ->
            //清空原来的数据
            val tempList = mutableListOf<String>()

            oldList.clear()
            oldList.addAll(it.text().split(" "))
            oldList.remove("*")
            oldList.remove("(部队)")
            oldList.remove("①")
            oldList.remove("②")
            oldList.remove("③")
            oldList.remove("(英雄)")


            if (oldList.size == size) {
                val format =
                    DecimalFormat("###################.###########").format((oldList[zhiyuan] + oldList[zhiyuan + 1]).toDouble() / 10000)
                val sum = "${format}万"
                repeat(oldList.size) { i ->
                    when {
                        i < zhiyuan -> {
                            tempList.add(oldList[i])
                        }
                        i == zhiyuan -> {
                            tempList.add(sum)

                        }
                        i > zhiyuan + 1 -> {
                            tempList.add(oldList[i])
                        }
                    }
                }
                tempList.remove("")
                updateList.addAll(listOf(tempList))
            } else {
                tempList.addAll(oldList)
                updateList.addAll(listOf(tempList))
            }

        }

        repeat(size - 2) {
            updaterDetails!!.updateBean.updateList.add(mutableListOf())
        }
        var index: Int
        updateList.forEach { list ->
            index = 0
            list.forEachIndexed { i, s ->

                when (i) {
                    0 -> {
                        updaterDetails!!.updateBean.levelList.add(s)
                    }
                    else -> {
                        if (i - 1 == index)
                            updaterDetails!!.updateBean.updateList[index].add(s)
                        index++
                    }
                }
            }
        }
        updaterDetails!!.updateBean.title = updateData.getElementsByClass("subtitle").text()

        totalList.add(updaterDetails!!)
        if (isDowImg)
            downImg()

//        updaterDetails = null
        iconBean.list.clear()
        httpIconList?.clear()
        updateList.clear()
        println(Gson().toJson(updaterDetails))
    }

    /**
     * 解析换行
     * @param size 网页长度+1
     * @param zhiyuan 资源转成万  网页长度 0开始
     * @param isDowImg 是否下载图片
     */
    fun upLineFeed(
        element: Document,
        size: Int,
        lineIndex: Int,
        zhiyuan: Int,
        isDowImg: Boolean = false
    ) {
        val updateData = element.getElementsByClass("mdui-m-t-2 update-data-table")[0]
        updateData.getElementsByTag("tr").forEachIndexed { index, it ->
            //清空原来的数据
            val tempList = mutableListOf<String>()

            oldList.clear()
            oldList.addAll(it.text().split(" "))
            oldList.remove("*")


            if (oldList.size == size) {
                if (oldList[0] == "等级") {
                    repeat(size - 1) { i ->
                        when {
                            i < lineIndex -> {
                                tempList.add(oldList[i])
                            }
                            i == lineIndex -> tempList.add("${oldList[lineIndex]}${oldList[lineIndex + 1]}")
                            i == size - 2 -> tempList.add(oldList[i + 1])
                            i > lineIndex -> {
                                tempList.add("${oldList[i + 1]} ")
                            }
                        }
                    }
                } else {
                    val format =
                        DecimalFormat("###################.###########").format((oldList[zhiyuan] + oldList[zhiyuan + 1]).toDouble() / 10000)
                    val sum = "${format}万"
                    repeat(oldList.size) { i ->
                        when {
                            i < zhiyuan -> {
                                tempList.add(oldList[i])
                            }
                            i == zhiyuan -> {
                                tempList.add(sum)

                            }
                            i > zhiyuan + 1 -> {
                                tempList.add(oldList[i])
                            }
                        }
                    }
                }

                tempList.remove("")
                updateList.addAll(listOf(tempList))
            } else {
                tempList.addAll(oldList)
                updateList.addAll(listOf(tempList))
            }

        }

        repeat(size - 2) {
            updaterDetails!!.updateBean.updateList.add(mutableListOf())
        }
        var index: Int
        updateList.forEach { list ->
            index = 0
            list.forEachIndexed { i, s ->
                when (i) {
                    0 -> {
                        updaterDetails!!.updateBean.levelList.add(s)
                    }
                    else -> {
                        if (i - 1 == index)
                            updaterDetails!!.updateBean.updateList[index].add(s)
                        index++
                    }
                }
            }
        }
        updaterDetails!!.updateBean.title = updateData.getElementsByClass("subtitle").text()

        totalList.add(updaterDetails!!)
        if (isDowImg)
            downImg()

//        updaterDetails = null
        iconBean.list.clear()
        httpIconList?.clear()
        updateList.clear()
    }

    /**
     * 解析无换行、带空格、去*号
     * @param size 网页长度+1
     * @param zhiyuan 资源转成万
     * @param isDowImg 是否下载图片
     */

    fun upKongge(
        element: Document,
        size: Int,
        kongge: Int,
        konggeNum: Int,
        ziyuan: Int,
        isDowImg: Boolean = false
    ) {
        val updateData = element.getElementsByClass("mdui-m-t-2 update-data-table")[0]

        updateData.getElementsByTag("tr").forEachIndexed { index, it ->
            val tempList = mutableListOf<String>()
            oldList.clear()
            oldList.addAll(it.text().split(" "))
            oldList.remove("*")

            if (oldList.size == size) {
                if (oldList[0] == "等级") {
                    repeat(size - 1) { i ->
                        when (konggeNum) {
                            1 -> swkongge1(i, kongge, tempList)
                            2 -> swkongge2(i, kongge, tempList)
                        }

                    }
                }
                tempList.remove("")
                updateList.addAll(listOf(tempList))
            } else if (oldList.size == size - 1) {
                val format =
                    DecimalFormat("###################.###########").format((oldList[ziyuan] + oldList[ziyuan + 1]).toDouble() / 10000)
                val sum = "${format}万"
                repeat(oldList.size - 1) { i ->
                    when {
                        i < ziyuan -> {
                            tempList.addTrim(oldList[i])
                        }
                        i == ziyuan -> {
                            tempList.addTrim(sum)
                        }
                        i == size - 2 -> tempList.addTrim(oldList[i + 1])
                        i > ziyuan -> {
                            tempList.addTrim(oldList[i + 1])
                        }
                    }
                }
                tempList.remove("")
                updateList.addAll(listOf(tempList))
            } else {
                tempList.addAll(oldList)
                updateList.addAll(listOf(tempList))
            }

        }


        repeat(size - konggeNum - 1) {
            updaterDetails!!.updateBean.updateList.add(mutableListOf())
        }
        var index = 0
        updateList.forEach { list ->
            index = 0
            list.forEachIndexed { i, s ->

                when (i) {
                    0 -> {
                        updaterDetails!!.updateBean.levelList.addTrim(s)
                    }
                    else -> {
                        if (i - 1 == index)
                            updaterDetails!!.updateBean.updateList[index].addTrim(s)
                        index++
                    }
                }
            }
        }

        updaterDetails!!.updateBean.title = updateData.getElementsByClass("subtitle").text()

        totalList.add(updaterDetails!!)
        if (isDowImg)
            downImg()


//        updaterDetails = null
        iconBean.list.clear()
        httpIconList?.clear()
        updateList.clear()
    }

    private fun swkongge1(i: Int, kongge: Int, tempList: MutableList<String>) {
        when {
            i < kongge -> {
                tempList.addTrim(oldList[i])
            }
            i == kongge -> tempList.addTrim("${oldList[kongge]}${oldList[kongge + 1]}")
            i > kongge -> {
                tempList.addTrim(oldList[i + 1])
            }
        }
    }

    private fun swkongge2(i: Int, kongge: Int, tempList: MutableList<String>) {
        when {
            i < kongge -> {
                tempList.addTrim(oldList[i])
            }
            i == kongge -> tempList.addTrim("${oldList[kongge]}${oldList[kongge + 1]}")
            i == kongge + 2 -> tempList.addTrim("${oldList[kongge + 2]}${oldList[kongge + 3]}")
            i > kongge + 2 -> tempList.addTrim("${oldList[i + 1]} ")
        }

    }

    /**
     * 下载图片
     */
    private fun downImg() {
        //下载图片
        var url: URL
        iconBean.list.forEach {
            if (it.isNotEmpty()) {
                url = URL(it)
                pageListData.download(it, url.path)

            }
        }

//
        url = URL(imgurl)
        pageListData.download(imgurl, url.path)
    }

    /**
     * 解析基本信息
     */
    fun analysisDetails(url: String): Document {
        val baseUrl = "https://www.cocservice.top$url"
        val element = getJsDocument(baseUrl)
        updaterDetails = UpdaterDetails()

        updaterDetails!!.id = url

        updaterDetails!!.title =
            element.getElementsByClass("mdui-col-xs-12 mdui-col-sm-8 mdui-col-md-9 mdui-typo-title page-title")
                .text()
        /**
         * =====================图片================
         */
        updaterDetails!!.imgTitle = "大图"
        imgurl =
            element.getElementsByClass("mdui-card-media update-big-image-parent")[0].getElementsByAttribute(
                "href"
            ).attr("href")
        val imgurl1 = URL(imgurl)
        updaterDetails!!.img =
            "https://636f-coc-dev-1gfufj8q2024af7f-1302950227.tcb.qcloud.la${imgurl1.path}"
        /**
         * =============历史记录======
         */
        val text1 = element.getElementById("update-history-expand-content")
//        println(element.getElementsByClass("mdui-dialog-title").text())
        element.getElementsByClass("mdui-dialog-content").forEach {
//            println()
            updaterDetails!!.updateLog = it.html().replace("<br>", "").replace("<strong>", "")
                .replace("</strong>", "")

        }

//        print(text1)


        /**
         * ============图标=============
         */
        if (!updaterDetails!!.title.contains("法术")) {
            var maxNum = -1
            element.getElementsByClass("item-level-img").forEach { bean ->
                val text = bean.getElementsByClass("level-image-up").text()
                if (text.contains("-")) {
                    val split = text.split(" - ")
                    maxNum = split[1].toInt() - split[0].toInt() + 1
                    repeat(maxNum) {
                        iconBean.list.addTrim(bean.getElementsByTag("img").attr("data-src"))
                    }
                } else {
                    iconBean.list.addTrim(bean.getElementsByTag("img").attr("data-src"))
                }
            }
        }
        var imgurl2: URL
        httpIconList = mutableListOf()
        iconBean.list.forEach {
            if (it.isNotEmpty()) {
                imgurl2 = URL(it)
                httpIconList?.addTrim("https://636f-coc-dev-1gfufj8q2024af7f-1302950227.tcb.qcloud.la${imgurl2.path}")
            }
        }
        /**
         * ===========基本属性============
         */
        updaterDetails!!.attrBean.title =
            element.getElementsByClass("mdui-col-xs-12")[0]
                .getElementsByClass("subtitle").text()

        element.getElementsByClass("mdui-card update-property-card").forEach {
            val split = it.text().split(" ").toMutableList()
            split.remove("help_outline")
            split.remove("*")
            if (split.size == 2)
                updaterDetails!!.attrBean.list.add(AttrDetails(split[0], split[1]))
        }
        /**
         * ===========注意============
         */
        val a5 = StringBuilder()
        element.getElementsByClass("unit-description").forEach {
            a5.append(it.html().replace("<br>", ""))

        }
        updaterDetails!!.attrBean.careful = a5.toString()


        /**
         * ===========训练时间============
         */

//         updaterDetails!!.trainBean.title =
//            element.getElementsByClass("mdui-col-xs-12 mdui-col-md-4")[0]
//                .getElementsByClass("subtitle").text()
//
//
//        val trainList = mutableListOf<String>()
//        val tab =
//            element.getElementsByClass("mdui-table mdui-table-hoverable update-little-table table-text-center")[0]
//        val split = tab.getElementsByTag("thead")[0].text().split(" ")
//
//
//        trainList.add("${split[1]} ${split[2]}")
//        tab.getElementsByTag("tbody").forEach {
//            it.getElementsByTag("tr").forEach { tr ->
//                val time = tr.text().split(" ")
//                 updaterDetails!!.trainBean.list.add(AttrDetails("${split[1]}:${time[0]}", time[1]))
//            }
//        }
        return element
    }


    /**
     * 获取js树
     */
    fun getJsDocument(url: String): Document {
        val webClient = WebClient(BrowserVersion.CHROME)
        webClient.options.isJavaScriptEnabled = true // 启用JS解释器，默认为true
        webClient.options.isCssEnabled = false // 禁用css支持
        webClient.options.isThrowExceptionOnScriptError = false // js运行错误时，是否抛出异常
        webClient.options.isThrowExceptionOnFailingStatusCode = false
        webClient.options.timeout = 10 * 1000 // 设置连接超时时间
        webClient.javaScriptErrorListener = object : DefaultJavaScriptErrorListener() {
            override fun scriptException(page: HtmlPage?, scriptException: ScriptException?) {
            }

            override fun timeoutError(page: HtmlPage?, allowedTime: Long, executionTime: Long) {
            }

            override fun malformedScriptURL(
                page: HtmlPage?,
                url: String?,
                malformedURLException: MalformedURLException?
            ) {
            }

            override fun loadScriptError(page: HtmlPage?, scriptUrl: URL?, exception: Exception?) {}
            override fun warn(
                message: String?,
                sourceName: String?,
                line: Int,
                lineSource: String?,
                lineOffset: Int
            ) {
            }
        }
        val page: HtmlPage = webClient.getPage(url)
        webClient.waitForBackgroundJavaScript((1000).toLong()) // 等待js后台执行30秒
        val pageAsXml = page.asXml()
        return Jsoup.parse(pageAsXml, url)
    }
}