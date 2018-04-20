package test;
import java.io.IOException;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xs.spider.parser.*;

public class demo {
	
    public static int convertPDF2SWF(String sourcePath, String destPath) throws IOException {
        // 源文件不存在则返回
        File source = new File(sourcePath);
        if (!source.exists())
            return 0;

        // 调用pdf2swf命令进行转换
        String command = "D:\\SWFTools\\pdf2swf.exe" + " " + sourcePath + " -o " + destPath + " -f -T 9";
        System.out.println(command);
        Process pro = Runtime.getRuntime().exec(command);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        while (bufferedReader.readLine() != null) ;
        try {
            pro.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pro.exitValue();
    }
	
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
