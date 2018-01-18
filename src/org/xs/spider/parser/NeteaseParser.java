package org.xs.spider.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class NeteaseParser extends BasicParser {
    /**
     * 假设现在抓取news.163.com的新闻时，不能够准确定位，
     * 于是重写一个叫NeteaseParser的类，继承自BasicParser，
     * 重写BasicParser中定位正文的方法
     */

    public NeteaseParser(){
        //将自己注册到ParserLocator中
        ParserLocator.getInstance().register("news.163.com", this);
    }

    @Override
    public Element excavateContent(Document document) {
        return document.select("div.post_text").get(0);
    	//return document.getElementById("keyword");
    }
}
