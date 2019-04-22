import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class nfplus {
    //private String IMAGE_PATH = "Z:\\nfplus\\";
    //private String XML_PATH = "D:\\xml\\nfplus\\";
    private String IMAGE_PATH = "F:\\myjob\\xx\\";
    private String XML_PATH = "F:\\myjob\\xx\\yy\\";

    private String folderName = "";
    private String docIds = "";
    private HashMap<String, String> chnlMap = new HashMap<String, String>();
    private String channels = "2343,383485\n" +
            "4353,383484\n" +
            "1872,383483\n" +
            "3910,383482\n" +
            "3874,383481\n" +
            "1617,383480\n" +
            "3766,383479\n" +
            "4180,383478\n" +
            "1699,383477\n" +
            "1616,383476\n" +
            "5438,383475\n" +
            "4842,383474\n" +
            "3767,383473\n" +
            "5774,383472\n" +
            "6020,383471\n" +
            "5246,383470\n" +
            "6335,383469\n" +
            "6329,383468\n" +
            "3829,383467\n" +
            "1358,383466\n" +
            "3658,383465\n" +
            "1325,383464\n" +
            "2346,383463\n" +
            "3414,383462\n" +
            "3415,383461\n" +
            "3875,383460\n" +
            "4432,383459\n" +
            "3878,383458\n" +
            "3908,383457\n" +
            "4636,383456\n" +
            "5005,383455\n" +
            "5302,383454\n" +
            "5326,383453\n" +
            "6180,383452";

    /**
     * @description 构造函数
     */
    public nfplus()
    {
        System.out.println("1. 初始化栏目映射关系");
        for ( String channel:channels.split("\n") )
        {
            chnlMap.put(channel.split(",")[0], channel.split(",")[1]);
        }
        System.out.println("2. 创建图片附件目录");
        createFolder();
        docIds = fileRead();
    }


    /**
     * @description 读取文件内容到字符串中
     * @throws Exception
     */
    private String fileRead()
    {
        String str = "";
        try {
            File file = new File("nfplus.txt");//定义一个file对象，用来初始化FileReader
            FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
            BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
            String s = "";
            while ((s = bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
            }
            bReader.close();
            str = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return str;
        }
    }

    /**
     * @description 增量写文件
     */
    private void writeFile(String content)
    {
        FileWriter writer = null;
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter("nfplus.txt", true);
            writer.write(content+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if ( writer != null )
                {
                    writer.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @description 按日期创建目录
     */
    private void createFolder()
    {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        this.folderName = df.format(date);
        String folder = this.IMAGE_PATH + this.folderName;
        File file = new File(folder);
        if ( file.exists() )
        {
            if ( !file.isDirectory() )
            {
                file.mkdir();
            }
        } else {
            file.mkdir();
        }

    }

    /**
     * @description 获取DocumentBuilder对象
     * @return documentBuilder DocumentBuilder
     */
    private DocumentBuilder getDocumentBuilder()
    {
        // 获得DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return db;
    }

    /**
     * @description get rss stream
     * @param apiurl String
     * @return inputStream InputStream
     */
    public InputStream getNfplusRss(String apiurl)
    {
        System.out.println("3. 抓取南方+RSS流");
        InputStream inputStream = null;
        try {
            URL url = new URL(apiurl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return inputStream;
        }
    }

    /**
     * @description 解析rss
     * @param inputStream InputStream
     * @return void
     */
    public void parseRss(InputStream inputStream)
    {
        System.out.println("4. 解析RSS流");
        try {
            DocumentBuilder documentBuilder = this.getDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList articles = document.getElementsByTagName("Article");
            for ( int j = 0; j < articles.getLength(); j++ )
            {
                Element article = (Element) articles.item(j);
                if ( article.hasChildNodes() )
                {
                    HashMap<String, String> hashMap = new HashMap<String, String>();

                    NodeList nodeList = article.getChildNodes();
                    for ( int k = 0; k < nodeList.getLength(); k++ )
                    {
                        if ( nodeList.item(k).getNodeType() == Node.ELEMENT_NODE)
                        {
                            hashMap.put(nodeList.item(k).getNodeName(), nodeList.item(k).getTextContent());
                        }
                    }
                    String article_path = this.XML_PATH + "\\article-" + hashMap.get("DocId") + ".xml";
                    File article_file = new File(article_path);
                    if ( !article_file.exists() && this.docIds.indexOf(hashMap.get("DocId") + "," + hashMap.get("lastModified") + "\n") == -1 ) {
                        if ( this.docIds.indexOf(hashMap.get("DocId") + ",") != -1 ) {
                            hashMap.put("columnId", "0000");
                            hashMap.put("Title", "[已修改] " + hashMap.get("Title"));
                        }
                        this.createRss(article_path, hashMap);
                        this.writeFile(hashMap.get("DocId") + "," + hashMap.get("lastModified"));
                    } else {
                        System.out.println("id为" + hashMap.get("DocId") + "的文档已入库");
                    }
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 创建rss
     * @param filename String
     * @param hashMap HashMap
     * @return void
     */
    public void createRss(String filename, HashMap<String, String> hashMap)
    {
        DocumentBuilder documentBuilder = this.getDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.setXmlStandalone(true);

        Element founderEnpML = document.createElement("FounderEnpML");
        Element version = document.createElement("Version");
        version.setTextContent("2.0.2");

        String contentHtml = hashMap.get("Content");
        contentHtml = this.localizePic(contentHtml);
        contentHtml = this.replaceVideo(contentHtml);
        String titleStr = hashMap.get("Title");

        Element pack = document.createElement("Package");

        Element sourceSystem = document.createElement("SourceSystem");
        CDATASection SourceSystem = document.createCDATASection("");
        sourceSystem.appendChild(SourceSystem);

        Element targetLib = document.createElement("TargetLib");
        targetLib.setTextContent("2");

        Element articleType = document.createElement("ArticleType");
        articleType.setTextContent("1");

        Element targetNode = document.createElement("TargetNode");
        targetNode.setTextContent("");

        Element targetNodeId = document.createElement("TargetNodeId");
        String nodeId = this.getChannelid(hashMap.get("columnId"));
        CDATASection TargetNodeId = document.createCDATASection(nodeId);
        targetNodeId.appendChild(TargetNodeId);

        Element publishState = document.createElement("PublishState");
        publishState.setTextContent("3");

        Element objectType = document.createElement("ObjectType");
        objectType.setTextContent("1");

        Element action = document.createElement("Action");
        action.setTextContent("1");

        // Element userName = document.createElement("UserName");
        // CDATASection UserName = document.createCDATASection("zhangp");
        // userName.appendChild(UserName);

        Element siteId = document.createElement("SiteId");
        siteId.setTextContent("4");


        pack.appendChild(sourceSystem);
        pack.appendChild(targetLib);
        pack.appendChild(articleType);
        pack.appendChild(targetNode);
        pack.appendChild(targetNodeId);
        pack.appendChild(publishState);
        pack.appendChild(objectType);
        pack.appendChild(action);
        // pack.appendChild(userName);
        pack.appendChild(siteId);


        Element article = document.createElement("Article");

        Element docId = document.createElement("DocId");
        CDATASection DocId = document.createCDATASection("0");
        docId.appendChild(DocId);

        Element jabbarMark = document.createElement("JabbarMark");
        CDATASection JabbarMark = document.createCDATASection("");
        jabbarMark.appendChild(JabbarMark);

        Element loginname = document.createElement("Loginname");
        CDATASection Loginname = document.createCDATASection("");
        loginname.appendChild(Loginname);

        Element introtitle = document.createElement("Introtitle");
        CDATASection Introtitle = document.createCDATASection("");
        introtitle.appendChild(Introtitle);

        Element subtitle = document.createElement("Subtitle");
        CDATASection Subtitle = document.createCDATASection("");
        subtitle.appendChild(Subtitle);

        Element abst = document.createElement("Abstract");
        CDATASection Abst = document.createCDATASection(hashMap.get("Abstract"));
        abst.appendChild(Abst);

        Element attr = document.createElement("Attr");
        CDATASection Attr = document.createCDATASection("");
        attr.appendChild(Attr);

        Element wordCount = document.createElement("wordCount");
        CDATASection WordCount = document.createCDATASection(String.valueOf(this.getWordCount(contentHtml)));
        wordCount.appendChild(WordCount);

        Element importance = document.createElement("Importance");
        CDATASection Importance = document.createCDATASection("0");
        importance.appendChild(Importance);

        Element keyword = document.createElement("keyword");
        CDATASection Keyword = document.createCDATASection(hashMap.get("keyword"));
        keyword.appendChild(Keyword);

        Element piccount = document.createElement("piccount");
        CDATASection Piccount = document.createCDATASection(String.valueOf(this.getPicCount(contentHtml)));
        piccount.appendChild(Piccount);

        Element nsdate = document.createElement("Nsdate");
        CDATASection Nsdate = document.createCDATASection(hashMap.get("Nsdate"));
        nsdate.appendChild(Nsdate);

        Element source = document.createElement("Source");
        CDATASection Source = document.createCDATASection(hashMap.get("Source"));
        source.appendChild(Source);

        Element sourceUrl = document.createElement("SourceUrl");
        CDATASection SourceUrl = document.createCDATASection("");
        sourceUrl.appendChild(SourceUrl);

        Element author = document.createElement("Author");
        CDATASection Author = document.createCDATASection(hashMap.get("Author"));
        author.appendChild(Author);

        Element title = document.createElement("Title");
        CDATASection Title = document.createCDATASection(titleStr);
        title.appendChild(Title);

        Element multiattach = document.createElement("Multiattach");
        CDATASection Multiattach = document.createCDATASection("");
        multiattach.appendChild(Multiattach);

        Element nodePath = document.createElement("NodePath");
        CDATASection NodePath = document.createCDATASection("");
        nodePath.appendChild(NodePath);

        Element url = document.createElement("Url");
        CDATASection urll = document.createCDATASection("");
        url.appendChild(urll);

        Element hasTitlePic = document.createElement("hasTitlePic");

        Element content = document.createElement("Content");
        CDATASection Content = document.createCDATASection(contentHtml);
        content.appendChild(Content);

        Element expirationTime = document.createElement("ExpirationTime");
        CDATASection ExpirationTime = document.createCDATASection("");
        expirationTime.appendChild(ExpirationTime);

        Element isTop = document.createElement("IsTop");
        CDATASection IsTop = document.createCDATASection("");
        isTop.appendChild(IsTop);
        Element editor = document.createElement("Editor");
        Element attachement = document.createElement("Attachement");

        article.appendChild(docId);
        article.appendChild(jabbarMark);
        article.appendChild(loginname);
        article.appendChild(introtitle);
        article.appendChild(subtitle);
        article.appendChild(abst);
        article.appendChild(attr);
        article.appendChild(wordCount);
        article.appendChild(importance);
        article.appendChild(keyword);
        article.appendChild(piccount);
        article.appendChild(nsdate);
        article.appendChild(source);
        article.appendChild(sourceUrl);
        article.appendChild(author);
        article.appendChild(title);
        article.appendChild(multiattach);
        article.appendChild(nodePath);
        article.appendChild(url);
        article.appendChild(hasTitlePic);
        article.appendChild(content);
        article.appendChild(expirationTime);
        article.appendChild(isTop);
        article.appendChild(editor);
        article.appendChild(attachement);

        founderEnpML.appendChild(version);
        founderEnpML.appendChild(pack);
        founderEnpML.appendChild(article);

        document.appendChild(founderEnpML);
        TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            System.out.println("6. 生成xml文件, docid 为 " +  hashMap.get("DocId"));
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "Yes");
            transformer.transform(new DOMSource(document), new StreamResult(filename));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 过滤html字符串的标签, 获取文本长度
     * @param htmlStr String
     * @return int
     */
    public int getWordCount(String htmlStr)
    {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        String pureText = htmlStr.trim();
        return pureText.length();
    }

    /**
     * @Description get count of pic
     * @param htmlStr String
     * @return int
     */
    public int getPicCount(String htmlStr)
    {
        String [] aaa = htmlStr.split("<img");
        return aaa.length - 1;
    }

    /**
     * @description localize pic
     * @param htmlStr String
     * @return htmlStr String
     */
    private String localizePic(String htmlStr)
    {
        System.out.println("5. 图片本地化保存");
        Pattern pattern = Pattern.compile("<img[\\s\\S]*?src=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlStr);
        while ( matcher.find() ) {
            String imageUrl = matcher.group(1);
            if ( !"".equals(imageUrl) )
            {
                String imageName = imageUrl.split("/")[imageUrl.split("/").length - 1];
                if ( imageName.indexOf(".") == -1 )
                {
                    String imageSuffix = ".jpg";
                    if ( imageName.indexOf("wx_fmt=gif") != -1 )
                    {
                        imageSuffix = ".gif";
                    } else if ( imageName.indexOf("wx_fmt=png") != -1 ) {
                        imageSuffix = ".png";
                    }
                    imageName = "wx-" + Math.random() + imageSuffix;
                }

                String imagePath = this.IMAGE_PATH + this.folderName +  "\\" + imageName;
                File image_file = new File(imagePath);
                if ( !image_file.exists() ) {
                    System.out.println("正在下载图片: " + imageUrl);
                    this.downloadPicture(imageUrl.replaceAll("https://", "http://"), imagePath);
                }
                htmlStr = htmlStr.replaceAll(imageUrl, "/data/attachement/nfplus/" + this.folderName + "/" + imageName);
            }
        }
        return htmlStr;
    }

    /**
     * @description 视频替换
     * @param htmlStr String
     * @return htmlStr String
     */
    private String replaceVideo(String htmlStr)
    {
        Pattern pattern = Pattern.compile("<video[\\s\\S]*?src=\"(.*?)\"[^>]*?></video>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlStr);
        while ( matcher.find() ) {
            String video_tag = matcher.group(0);
            String video_url = matcher.group(1);
            String new_video_tag = "<div id=\"J_prismPlayer\" style=\"width: 100%;\" class=\"prism-player\">.</div> <script type=\"text/javascript\"> \n" +
                    "<!-- \n" +
                    "function ali_videoplay() {new window.prismplayer({id: \"J_prismPlayer\",autoplay: true,width: \"100%\",source: \""+ video_url +" \n" +
                    "\n" +
                    "\",cover: 'http://www.southcn.com/public/2016/img/videoPoster.jpg \n" +
                    "\n" +
                    "',waterMark:'http://www.southcn.com/public/2016/img/tv/nfvideologo.png|TR|0.15|0.5 \n" +
                    "\n" +
                    "'});};ali_videoplay(); \n" +
                    "--> \n" +
                    "</script>";
            htmlStr = htmlStr.replaceAll(video_tag, new_video_tag);
        }
        return htmlStr;
    }

    /**
     * @description 根据南方+栏目id查找对应的南方网id
     * @param nfplusid
     * @return
     */
    private String getChannelid(String nfplusid)
    {
        if ( nfplusid == "0000" )
            return "383892";
        String channelid = this.chnlMap.get(nfplusid);
        if ( channelid == null )
            return "378292";
        return channelid;
    }

    /**
     * @description      链接url下载图片
     * @param imageUrl  网络图片地址
     * @param  imagePath  图片地址
     */
    public void downloadPicture(String imageUrl, String imagePath) {
        try {
            URL url = new URL(imageUrl);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imagePath));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        nfplus np = new nfplus();
        InputStream inputStream = np.getNfplusRss("http://static.nfapp.southcn.com/southcn/nfplus-article.xml");
        np.parseRss(inputStream);
        System.out.println("7. 结束了");
    }

}
