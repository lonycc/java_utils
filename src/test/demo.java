package test;
import java.io.IOException;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xs.spider.parser.*;


public class demo {
	
	public static void main(String[] args)
	{
		new NeteaseParser();
		String url = "http://politics.people.com.cn/n1/2018/0118/c1001-29771303.html";
		Parser parser = ParserLocator.getInstance().getParser(url);
		
		//直接用基础解析器
		//BasicParser parser = new BasicParser();
		
		try {
			Document document = Jsoup.connect(url).get();
			String content = parser.getContent(document);
			System.out.println(content);
			String text = parser.getContentText(document);
			Element ele = parser.getContentEle(document);
			System.out.print(text + "----------------\n" + ele);
			HashMap<String, String> hashMap = parser.getDetailsMap(document);
			System.out.println(hashMap.get("title"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
