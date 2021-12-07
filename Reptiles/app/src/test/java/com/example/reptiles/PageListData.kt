package com.example.reptiles

import com.google.gson.Gson
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2020/12/29 23:50
 * Description: com.example.reptiles
 */
class PageListData {

    val dataList = mutableListOf<TabBean>()

    private var updateDocument: Document? = null

    private lateinit var tabList: List<String>

    fun startPage() {
        analysisTab()
        analysisTitle("update-tab-home")
        analysisTitle("update-tab-builderBase")
        analysisTitle("update-tab-superTroops")
        analysisTitle("update-tab-tempUnits")
//        analysisTitle("update-tab-thLevel")


//        var url: URL
//        dataList.forEach {
//            it.list.forEach { aa ->
//                aa.updaterList.forEachIndexed { index, cardBean ->
//                    val replace = cardBean.img.replace("w_100", "w_200")
//                    url = URL(replace)
//
//                    download(replace, url.path)
//
//
////                    download(replace, cardBean.urlAddress)
//                    cardBean.img =
//                        "https://636f-coc-dev-7gcl1tmm4893a4e6-1306113230.tcb.qcloud.la${url.path}"
//                }
//
//            }
//        }
//        print(Gson().toJson(dataList))
//        print(i)
    }

    /**
     * 解析TAB
     */
    fun analysisTab() {
        getUpdateDocument()
            .getElementsByClass("mdui-tab mdui-tab-scrollable unit-type-tab")
            .forEach {
                tabList = it.text().split(" ").drop(1)
            }
    }

    /**
     * ===================================第一个界面======================
     */
    private fun analysisTitle(type: String) {
//        if (!updaterMap.containsKey(type)) {
//            updaterMap[type] = mutableListOf()
//        }
        val tabBean = TabBean()
        tabBean.name = when (type) {
            "update-tab-home" -> {
                tabBean.isActive = true
                "家乡"
            }
            "update-tab-builderBase" -> "夜世界"
            "update-tab-superTroops" -> "超级兵种"
            "update-tab-tempUnits" -> "临时兵种"
            else -> ""

        }
//        tabBean.name = type
        var updaterListBean: UpdaterListBean?
        val tabHome = getUpdateDocument().getElementById(type)
        val subtitle = tabHome.getElementsByClass("subtitle")
        val units = tabHome.getElementsByClass("units")
        repeat(units.size) { index ->
            updaterListBean = UpdaterListBean()
            if (!subtitle.isEmpty()) {
//                println(subtitle[index].text())
                updaterListBean!!.title = subtitle[index].text()
            }
            units[index].getElementsByTag("a").forEach { s ->
                val href = s.getElementsByAttribute("href")

                val img = s.getElementsByTag("img")

                val name = s.getElementsByClass("update-card-text")
                for (i in img.indices) {
                    img[i].dataset().values.forEach {
//                        print(href.attr("href"))
                        updaterListBean!!.updaterList.add(
                            CardBean(
                                name[i].text(),
                                href.attr("href"),
                                it
                            )
                        )
                    }
                }
            }
            tabBean.list.add(updaterListBean!!)

//            updaterMap[type]?.add(updaterListBean!!)
//            println("=================================")
        }
        dataList.add(tabBean)
    }


    private fun getUpdateDocument(): Document {
        if (updateDocument == null) {
            updateDocument = Jsoup.connect("https://www.cocservice.top/update/").get()
        }
        return updateDocument!!
    }

    /**
     * 获取本地图标
     */

    private val arrays = mutableListOf<String>()
    fun getFile(path: String, deep: Int): MutableList<String> {
        // 获得指定文件对象
        val file = File(path)
        // 获得该文件夹内的所有文件
        val array = file.listFiles()
        for (i in array.indices) {
            if (array[i].isFile) {
                for (j in 0 until deep)  //输出前置空格
                    print(" ")
                // 只输出文件名字
                arrays.add(array[i].name)
//                println(array[i].name)
                // 输出当前文件的完整路径
                // System.out.println("#####" + array[i]);
                // 同样输出当前文件的完整路径   大家可以去掉注释 测试一下
                // System.out.println(array[i].getPath());
            } else if (array[i].isDirectory) {
                for (j in 0 until deep)  //输出前置空格
                    print(" ")
//                println(array[i].name)
                arrays.add(array[i].name)
                //System.out.println(array[i].getPath());
                //文件夹需要调用递归 ，深度+1
                getFile(array[i].path, deep + 1)
            }
        }
        return arrays
    }

    /**
     * 下载图标
     */
    @Throws(Exception::class)
    fun download(urlString: String?, i: String) {
        // 构造URL
        val url = URL(urlString)
        // 打开连接
        val con: URLConnection = url.openConnection()
        con.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36)"
        );
        // 输入流
        val `is`: InputStream = con.getInputStream()
        // 1K的数据缓冲
        val bs = ByteArray(1024)
        // 读取到的数据长度
        var len: Int
        // 输出的文件流
        val filename = "F:\\图片下载/$i" //下载路径及下载图片名称
        val file = File(filename)
        val os = FileOutputStream(file, true)
        // 开始读取
        while (`is`.read(bs).also { len = it } != -1) {
            os.write(bs, 0, len)
        }
        println(i)
        // 完毕，关闭所有链接
        os.close()
        `is`.close()
    }

}