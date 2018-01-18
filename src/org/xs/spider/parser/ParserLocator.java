package org.xs.spider.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by tony on 20161219.
 * 使用对象定位器模式管理各类Parser
 */
public class ParserLocator
{

    private static ParserLocator locator = new ParserLocator();
    private HashMap<String, Parser> parserHashMap = new HashMap<String, Parser>();

    BasicParser basicParser = new BasicParser();

    public Parser getParser(String url)
    {
        String host = getHost(url);
        if ( host != null && parserHashMap.containsKey(host) )
        {
            return parserHashMap.get(host);
        }
        return basicParser;
    }

    public void register(String host, Parser parser)
    {
        parserHashMap.put(host, parser);
    }

    public void unregister(String host)
    {
        parserHashMap.remove(host);
    }

    public static ParserLocator getInstance()
    {
        return locator;
    }

    private ParserLocator() {}


    private static String getHost(String url)
    {
        try {
            URL u = new URL(url);
            return u.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
