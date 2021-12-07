package com.example.reptiles

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.ScriptException
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.MalformedURLException
import java.net.URL
import java.text.DecimalFormat

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/2/8 8:37
 * Description: com.example.reptiles
 */
object DataManage {

    var updaterDetails: UpdaterDetails? = null
    private lateinit var imgurl: String
    private var httpIconList: MutableList<String>? = null

    val totalList = mutableListOf<UpdaterDetails>()


    private val updateList = mutableListOf<MutableList<String>>()
    lateinit var pageListData: PageListData

    private val iconBean = BaseBean<String>()


    private val oldList = mutableListOf<String>()

    private val numValueList: MutableList<String> = mutableListOf()
    private val numKeyList: MutableList<String> = mutableListOf()


    /**
     * 解析无换行、无空格、去*号
     * @param size 网页长度+1
     * @param zhiyuan 资源转成万  网页长度 0开始
     * @param isDowImg 是否下载图片
     */
    fun upData(element: Document, size: Int, zhiyuan: Int, isDowImg: Boolean = true) {
        val updateData = element.getElementsByClass("mdui-m-t-2 update-data-table")[0]
        updateData.getElementsByTag("tr").forEachIndexed { index, it ->
            //清空原来的数据
            val tempList = mutableListOf<String>()

            oldList.clear()
            oldList.addAll(it.text().split(" "))

            oldList.remove("*")
            //攻城气球
            oldList.remove("(每秒)")
            oldList.remove("(每秒)")
            oldList.remove("(每次)")
            oldList.remove("(每次)")
            //城墙
            oldList.remove("每秒治疗量")
            oldList.remove("每秒治疗量")
            oldList.remove("每次治疗量")
            oldList.remove("每次治疗量")
            oldList.remove("小石头人数量")
            oldList.remove("死亡冰冻时间")
            oldList.remove("死亡冰冻时间")
            oldList.remove("所需金币")
            oldList.remove("(主动伤害)")
            oldList.remove("所需圣水")
            oldList.remove("附加伤害")
            oldList.remove("数量")
            oldList.remove("法术空间")
            oldList.remove("得的经验")
            oldList.remove("(非技能状态)")
            oldList.remove("(技能状态)")
            oldList.remove("纳的兵种数量")
//            oldList.remove("每次伤害")
            oldList.remove("炸墙伤害")
            oldList.remove("(每次)")
            oldList.remove("伤害增加幅度")
            oldList.remove("的临时生命值")
            oldList.remove("间隔时间")
            oldList.remove("①")
            oldList.remove("②")
            oldList.remove("的蝙蝠数量")
            oldList.remove("冷却时间")
            oldList.remove("的骷髅数量")
            oldList.remove("的每秒伤害")
            oldList.remove("狂暴状态")
            oldList.remove("狂暴状态")
            oldList.remove("(对建筑)")
            oldList.remove("(对建筑)")
            oldList.remove("(对城墙)")
            oldList.remove("(对城墙)")
            oldList.remove("大本等级")
            oldList.remove("炸墙时的")
            oldList.remove("第一个炸弹的")

            if (index == 0) {
                tempList.add("图示")
            } else {
                tempList.add("${httpIconList?.get(index - 1)}")
            }

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
                        updaterDetails!!.updateBean.iconList.add(s)
                    }
                    1 -> {
                        updaterDetails!!.updateBean.levelList.add(s)
                    }
                    else -> {

                        if (i - 2 == index)
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

        if (numKeyList.isNotEmpty()) {

            val list = mutableListOf<String>()

            updaterDetails!!.updateBean.let {
                it.updateList.get(it.updateList.size - 1).forEach {
                    var level = 0
                    if (it != "所需大本等级"&&it!="得的经验所需大本等级"&&it!="解锁的兵种")
                        level = it.toInt()
                    numKeyList.forEachIndexed { index, key ->
                        if (key == "大本等级") {

                        } else if (key.contains("-")) {
                            val splists = key.split("-")
                            val a = splists[0].toInt()
                            val b = splists[1].toInt()
                            if (level in a..b) {
                                list.add(numValueList.get(index))
                            }
                        } else {
                            if (level == key.toInt()) {
                                list.add(numValueList.get(index))
                            }
                        }
                    }
                }
            }
            list.add(0, "建筑数量")
            updaterDetails!!.updateBean.updateList.addAll(listOf(list))
            numKeyList.clear()
            numValueList.clear()

//            print(list)
        }

//        updaterDetails = null
        iconBean.list.clear()
        httpIconList?.clear()
        updateList.clear()
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

            //空气炮
            oldList.remove("每次攻击的")
            //炸弹塔
            oldList.remove("被摧毁后的")
            //天鹰火炮
            oldList.remove("中心区域")
            oldList.remove("外围区域")
            oldList.remove("10秒内")
            oldList.remove("触发所需")
            oldList.remove("的每次伤害")
            oldList.remove("的每次伤害")
            //巨型特斯拉电磁塔
            oldList.remove("每个目标的")
            oldList.remove("每个目标的")
            //巨型地狱之塔
            oldList.remove("被摧毁后的")
            oldList.remove("移动速度")
            oldList.remove("攻击速度")
//            oldList.remove("被摧毁后的")


            //隐形弹簧
            oldList.remove("可被弹飞")
            //大本营
            oldList.remove("储量")
            oldList.remove("(不含陷阱)")

            //金矿
            oldList.remove("所需时间")
            oldList.remove("志时的储量")
            oldList.remove("提速所需宝石")
            oldList.remove("所需宝石")

            oldList.remove("部队人口")
            oldList.remove("法术空间")
            oldList.remove("攻城机器数量")
            oldList.remove("冰冻法术")
            oldList.remove("城机器数量")
            oldList.remove("攻城机器")
            oldList.remove("石所需时间")
            oldList.remove("的时间")
            oldList.remove("时间比例")
            oldList.remove("冰冻时间")
            oldList.remove("的修复值")


            if (index == 0) {
                tempList.addTrim("图示")
            } else {
                tempList.addTrim("${httpIconList?.get(index - 1)}")
            }

            if (oldList.size == size) {
                if (oldList[0] == "等级"||oldList[0] == "大本等级") {
                    repeat(size - 1) { i ->
                        when {
                            i < lineIndex -> {
                                tempList.addTrim(oldList[i])
                            }
                            i == lineIndex -> tempList.addTrim("${oldList[lineIndex]}${oldList[lineIndex + 1]}")
                            i == size - 2 -> tempList.addTrim(oldList[i + 1])
                            i > lineIndex -> {
                                tempList.addTrim("${oldList[i + 1]} ")
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
                                tempList.addTrim(oldList[i])
                            }
                            i == zhiyuan -> {
                                tempList.addTrim(sum)

                            }
                            i > zhiyuan + 1 -> {
                                tempList.addTrim(oldList[i])
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
                        updaterDetails!!.updateBean.iconList.addTrim(s)
                    }
                    1 -> {
                        updaterDetails!!.updateBean.levelList.addTrim(s)
                    }
                    else -> {

                        if (i - 2 == index)
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

        if (numKeyList.isNotEmpty()) {

            val list = mutableListOf<String>()

            updaterDetails!!.updateBean.let {
                it.updateList.get(it.updateList.size - 2).forEach {
                    var level = 0
                    if (it != "所需大本等级"&&it!="得的经验所需大本等级"&&it!="解锁的兵种")
                        level = it.toInt()
                    numKeyList.forEachIndexed { index, key ->
                        if (key == "大本等级") {

                        } else if (key.contains("-")) {
                            val splists = key.split("-")
                            val a = splists[0].toInt()
                            val b = splists[1].toInt()
                            if (level in a..b) {
                                list.add(numValueList.get(index))
                            }
                        } else {
                            if (level == key.toInt()) {
                                list.add(numValueList.get(index))
                            }
                        }
                    }
                }
            }
            list.add(0, "建筑数量")
            updaterDetails!!.updateBean.updateList.addAll(listOf(list))
            numKeyList.clear()
            numValueList.clear()

//            print(list)
        }

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
            if (index == 0)
                tempList.addTrim("图示 ")
            else {
                tempList.addTrim("${httpIconList?.get(index - 1)} ")
            }

            if (oldList.size == size) {
                if (oldList[0] == "等级") {
                    repeat(size - 1) { i ->
                        when (konggeNum) {
                            1 -> swkongge1(i, kongge, tempList)
                            2 -> swkongge2(i, kongge, tempList)
                        }

                    }
                } else {
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
                }
                tempList.remove("")
                updateList.add(tempList)
            } else {
                updateList.addAll(listOf(oldList))
            }

        }


        repeat(size - 2) {
            updaterDetails!!.updateBean.updateList.add(mutableListOf())
        }
        var index = 0
        updateList.forEach { list ->
            index = 0
            list.forEachIndexed { i, s ->

                when (i) {
                    0 -> {
                        updaterDetails!!.updateBean.iconList.addTrim(s)
                    }
                    1 -> {
                        updaterDetails!!.updateBean.levelList.addTrim(s)
                    }
                    else -> {

                        if (i - 2 == index)
                            updaterDetails!!.updateBean.updateList[index].addTrim(s)
//                        update.addTrim(s)
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
                tempList.addTrim("${oldList[i]} ")
            }
            i == kongge -> tempList.addTrim("${oldList[kongge]}${oldList[kongge + 1]} ")
            i == kongge + 2 -> tempList.addTrim("${oldList[kongge + 2]}${oldList[kongge + 3]} ")
            i > kongge + 2 -> tempList.addTrim("${oldList[i + 1]} ")
        }

    }

    /**
     * 下载图片
     */
    fun downImg() {
        //下载图片
        var url: URL
//        iconBean.list.forEach {
//            if (it.isNotEmpty()) {
//
//                val replace = it.replace("w_100", "w_100")
//                url = URL(it)
////                    url = URL(replace)
//                pageListData.download(replace,url.path )
//
//            }
//        }

//
        url = URL(imgurl)
        pageListData.download(imgurl, url.path)
    }

    /**
     * 解析基本信息
     */
    fun analysisDetails(url: String, type: Int = 0): Document {
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
            "https://636f-coc-dev-7gcl1tmm4893a4e6-1306113230.tcb.qcloud.la${imgurl1.path}"
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
         * ============图标=============  法术工厂需要去掉！
         */
        if (!updaterDetails!!.title.contains("法术")) {
            var maxNum = -1
            element.getElementsByClass("item-level-img").forEach { bean ->
                val text = bean.getElementsByClass("level-image-up").text()
                if (text.contains("-")) {
                    val split = text.split(" - ")
                    maxNum = split[1].toInt() - split[0].toInt() + 1
                    repeat(maxNum) {
                        iconBean.list.add(bean.getElementsByTag("img").attr("data-src"))
                    }
                } else {
                    iconBean.list.add(bean.getElementsByTag("img").attr("data-src"))
                }
            }
        }
        var imgurl2: URL
        httpIconList = mutableListOf()
        iconBean.list.forEach {
            if (it.isNotEmpty()) {
                imgurl2 = URL(it)
                httpIconList?.add("https://636f-coc-dev-7gcl1tmm4893a4e6-1306113230.tcb.qcloud.la${imgurl2.path}")
            }
        }
        /**
         * ===========基本属性============
         */
        if (type == 3) {
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
        } else {
            updaterDetails!!.attrBean.title =
                element.getElementsByClass("mdui-col-xs-12 mdui-col-md-8")[0]
                    .getElementsByClass("subtitle").text()

            element.getElementsByClass("mdui-card update-property-card").forEach {
                val split = it.text().split(" ").toMutableList()
                split.remove("help_outline")
                split.remove("*")
                if (split.size == 2)
                    updaterDetails!!.attrBean.list.add(AttrDetails(split[0], split[1]))
            }
        }


        /**
         * ===========注意============
         */
        val a5 = StringBuilder()
        element.getElementsByClass("unit-description").forEach {
            a5.append(
                it.html().replace("<br>", "")
                    .replace("<strong>", "")
                    .replace("</strong>", "")
            )

        }
        updaterDetails!!.attrBean.careful = a5.toString()


        /**
         *  建筑数量
         */
//        val a = element.getElementsByClass("building-num-parent").get(0)
//            .getElementsByClass("building-num-key").text().replace(" - ", "-").split(" ")
//        val b = element.getElementsByClass("building-num-parent").get(0)
//            .getElementsByClass("building-num-value").text().split(" ")
//        numKeyList.addAll(a)
//        numValueList.addAll(b)

        /**
         * ===========训练时间============
         */

        if (type == 3) return element

        updaterDetails!!.trainBean.title =
            element.getElementsByClass("mdui-col-xs-12 mdui-col-md-4")?.get(0)
                ?.getElementsByClass("subtitle")?.text() ?: ""


        val trainList = mutableListOf<String>()
        val tab =
            element.getElementsByClass("mdui-table mdui-table-hoverable update-little-table table-text-center")
                ?.get(0)
        val split = tab?.getElementsByTag("thead")?.get(0)?.text()?.split(" ")


        trainList.add("${split?.get(1)} ${split?.get(2)}")
        tab?.getElementsByTag("tbody")?.forEach {
            it.getElementsByTag("tr").forEach { tr ->
                val time = tr.text().split(" ")
                updaterDetails!!.trainBean.list.add(
                    AttrDetails(
                        "${split?.get(1)}:${time[0]}",
                        time[1]
                    )
                )
            }
        }


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

fun MutableList<String>.addTrim(string: String) {
    this.add(string.trim())
}