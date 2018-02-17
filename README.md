# WebCrawler
Web Crawler to grab txt and image from 今日头条

**声明：浙大java课程小作业
**作者：GeSq

#### 功能描述
爬取今日头条文章的图片和正文文字。仅适用与头条文章版网页，不支持相册版网页。


#### UI界面
![这里写图片描述](http://img.blog.csdn.net/20171110210254030?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzU3MTMwNzQ=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)





网址：输入今日头条文字的url，例如
https://www.toutiao.com/a6426655544824905985/
https://www.toutiao.com/a6426655544824905985/
https://www.toutiao.com/a6485971081961144846/


导出目录：自己填写导出目录。**如果不填，默认是当前目录。**

点击按钮进行爬取。

#### 结果

![这里写图片描述](http://img.blog.csdn.net/20171110210514481?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzU3MTMwNzQ=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
![这里写图片描述](http://img.blog.csdn.net/20171110210656543?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzU3MTMwNzQ=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![这里写图片描述](http://img.blog.csdn.net/20171110204731466?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzU3MTMwNzQ=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



#### 逻辑：
输入url，爬取对应url里的HTML文件，从中筛选出image的url放入List中，然后逐个下载到本地文件夹。爬取对应文本，然后写入本地文档。


#### 代码
Main.java
```
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class Main{
    public static void main(String[] args) {
        SwingDemo demo = new SwingDemo();
    }
}
```
SwingDemo.java
```
import sun.java2d.loops.FillPath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by geshuaiqi on 2017/11/9.
 */
public class SwingDemo extends JFrame
{
    private String mHttpurl; // 网络url
    private String mLocalurl;// 本地url
    private String mFilePath;

    public String getmLocalurl(){
        return  mLocalurl;
    }

    public String getmHttpurl(){
        return mHttpurl;
    }

    public String getmFilePath(){
        return mFilePath;
    }

    public SwingDemo()
    {
        super("头条爬虫助手");
        JFrame frame =new JFrame("头条爬虫助手"); //设置标题
        frame.setSize(300,120); //设置窗口大小
        JPanel panel_up=new JPanel();   // JFrame 里上下两个部分
        JPanel panel_bottom=new JPanel();

        frame.setLayout(new BorderLayout());
        frame.add(panel_up,BorderLayout.CENTER);    // 输入内容居中
        frame.add(panel_bottom,BorderLayout.SOUTH); // 按键在下方


        panel_up.setLayout(new GridLayout(2,2));    // 上方panel 2*2布局

        JLabel HttpLabel =new JLabel("网址");
        JPanel panel_http_content =new JPanel();
        JLabel catalog =new JLabel("导出目录");
        JPanel panel_content =new JPanel();

        panel_up.add(HttpLabel);
        panel_up.add(panel_http_content);
        panel_up.add(catalog);
        panel_up.add(panel_content);


        JTextField LocalUrl=new JTextField(10);     // 本地存储位置输入

        panel_content.setLayout(new GridLayout());
        panel_content.add(LocalUrl);

        JTextField HttpUrl=new JTextField(10);      // 网页http url

        panel_http_content.setLayout(new GridLayout());
        panel_http_content.add(HttpUrl);

        panel_bottom.setLayout(new FlowLayout());           // 按钮布局

        JButton btn_pic = new JButton("导出图片/文字");
        //JButton btn_text = new JButton("导出文本");
        panel_bottom.add(btn_pic);
        //panel_bottom.add(btn_text);

        frame.setVisible(true);

        btn_pic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                mHttpurl = HttpUrl.getText().trim();    // 获取输入内容
                mLocalurl = LocalUrl.getText().trim();

                if(mLocalurl.length() == 0){
                    mLocalurl = "./";
                }

                CreateFile(mLocalurl);

                Crawler my = new Crawler(mHttpurl, mFilePath);

                Thread t = new Thread(my);
                t.start();

            }
        });
//        btn_text.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent arg1) {
//                System.out.println("Text");
//            }
//        });
    }

    // 创建文件夹
    void CreateFile(String FilePath){
        File file = null;
        File textfile = null;
        File picfile = null;
        FilePath = FilePath + "/头条爬虫助手";

        String tmpPath = FilePath;

        file = new File(tmpPath);
        int count = 1;
        while(file.exists()){       // 为避免文件名重复
            tmpPath = FilePath + "_" + Integer.toString(count);
            count++;
            file = new File(tmpPath);
        }
        FilePath = tmpPath;
        mFilePath = FilePath;

        String textdir = FilePath + "/文本库";
        String picdir = FilePath + "/图片库";

        // 创建文件夹
        try {
            file = new File(FilePath);
            if (!file.exists()) {
                System.out.println("成功创建文件夹: "+FilePath);
                file.mkdirs();
            }
            else{
                System.out.println("文件夹已经存在: "+FilePath);
            }
            file = new File(picdir);
            if (!file.exists()) {
                System.out.println("成功创建文件夹: "+picdir);
                file.mkdirs();
            }
            else{
                System.out.println("文件夹已经存在: "+picdir);
            }
            file = new File(textdir);
            if (!file.exists()) {
                System.out.println("成功创建文件夹: "+textdir);
                file.mkdirs();
            }
            else{
                System.out.println("文件夹已经存在: "+textdir);
            }


        } catch (Exception e) {
            System.out.println("创建文件夹失败: "+FilePath);
        } finally {
            file = null;
        }
    }

    public static void main(String[] args)
    {
        SwingDemo t = new SwingDemo();
    }


}
```
Crawler.java
```
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by geshuaiqi on 2017/11/9.
 */
public class Crawler implements Runnable {
    private String mHttpurl;
    private String mLocalurl;
    List<String> mPicUrlList;
    private String Article;

    // 传入网址url，以及本地导出目录。如果不填目录，则默认为当下目录
    Crawler(String url,String address_url){
        mHttpurl = url;
        mLocalurl = address_url;

        GetWebContent Content = new GetWebContent(mHttpurl);
        mPicUrlList = Content.GetPicUrl(); // 从网页中抽取目标图表的url
        for(int i = 0; i< mPicUrlList.size(); i++){
            System.out.println(mPicUrlList.get(i));
        }
        Article = Content.getText();
    }


    // 线程执行文件
    public void run(){
        System.out.println("开始下载图片");

        for(int i=0;i<mPicUrlList.size();i++){
            String pathname = mLocalurl + "/图片库/"+(i+1)+".jpg";
            getPic(mPicUrlList.get(i),pathname);
            System.out.println("已完成:"+(i+1)+".jpg ， 共"+(i+1)+"/"+mPicUrlList.size()+"张");
        }
        System.out.println("全部下载完成，共" + mPicUrlList.size() + "张");

        String textpath = mLocalurl + "/文本库/文本.txt";
        //System.out.println(Article);
        contentToTxt(textpath,Article); // 把文章输入到指定文本文件中
    }

    public static void contentToTxt(String filePath, String content) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath),true));
            writer.write("\n"+content);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // 下载图片

    public void getPic(String strUrl,String pathname) {
        try {
            //构造URL
            URL url = new URL(strUrl);

            //构造连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //这个网站要模拟浏览器才行
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");

            //打开连接
            conn.connect();
            //打开这个网站的输入流
            InputStream inStream = conn.getInputStream();

            //用这个做中转站 ，把图片数据都放在了这里，再调用toByteArray()即可获得数据的byte数组
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            //用这个是很好的，不用一次就把图片读到了文件中
            //要是需要把图片用作其他用途呢？所以直接把图片的数据弄成一个变量，十分有用
            //相当于操作这个变量就能操作图片了

            byte[] buf = new byte[1024];
            //为什么是1024？
            //1024Byte=1KB，分配1KB的缓存
            //这个就是循环读取，是一个临时空间，多大都没关系
            //这没有什么大的关系，你就是用999这样的数字也没有问题，就是每次读取的最大字节数。
            //byte[]的大小，说明你一次操作最大字节是多少
            //虽然读的是9M的文件，其实你的内存只用1M来处理，节省了很多空间．
            //当然，设得小，说明I/O操作会比较频繁，I/O操作耗时比较长，
            //这多少会有点性能上的影响．这看你是想用空间换时间，还是想用时间换空间了．
            //时间慢总比内存溢出程序崩溃强．如果内存足够的话，我会考虑设大点．
            int len = 0;
            //读取图片数据
            while ((len = inStream.read(buf)) != -1) {
                outStream.write(buf, 0, len);
            }
            inStream.close();
            outStream.close();
            //把图片数据填入文件中
            File file = new File(pathname);  // 创建空的图片文件

            FileOutputStream op = new FileOutputStream(file); // 目标为图片的输出流

            op.write(outStream.toByteArray());

            op.close();
        } catch (Exception e) {
            System.out.println("Exception");
        }

    }


}

```
GetWebContent.java
```
/**
 * Created by geshuaiqi on 2017/11/10.
 */
import com.sun.org.apache.xerces.internal.xs.StringList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetWebContent {
    private static String Html;

    GetWebContent(String url){
        Html = getWebCon(url);
    }

    private static String getWebCon(String domain) {
        // System.out.println("开始读取内容...("+domain+")");
        StringBuffer sb = new StringBuffer();
        try {
            java.net.URL url = new java.net.URL(domain);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            //System.out.println(sb.toString());
            in.close();
        } catch (Exception e) { // Report any errors that arise
            sb.append(e.toString());
            System.err.println(e);
            System.err.println("Usage:   java   HttpClient   <URL>   [<filename>]");
        }
        return sb.toString();
    }

    private static String RegexString(String targetStr, String patternStr)
    {
        // 定义一个样式模板，此中使用正则表达式，括号中是要抓的内容
        // 相当于埋好了陷阱匹配的地方就会掉下去
        Pattern pattern = Pattern.compile(patternStr);
        // 定义一个matcher用来做匹配
        Matcher matcher = pattern.matcher(targetStr);
        // 如果找到了
        if (matcher.find())
        {
            // 打印出结果
            return matcher.group(0);
        }
        return "Nothing";
    }

    public static String getText(){
        StringBuffer buffer = new StringBuffer();
        String regex="articleInfo(.*)commentInfo";
        String Article = RegexString(Html,regex);
        String Content = new String();

        String regexStr = "[\u4E00-\u9FA5]*";

        char[] t = Article.toCharArray();

        String text = new String();
        for(int i=0;i<Article.length();i++){
            if( (t[i] >= '\u4E00' && t[i]<='\u9FA5')  || t[i] == '，' || t[i] =='。' || t[i] =='~' || t[i] =='？' || t[i] =='！' || t[i] =='\n' ){
                text += t[i];
                if(t[i] == '。'){
                    text += '\n';
                }
            }
        }
//        Pattern pattern = Pattern.compile(regexStr);
//        // 定义一个matcher用来做匹配
//        Matcher matcher = pattern.matcher(Article);
//        while (matcher.find())
//        {
//           text += matcher.group(0);
//        }


        Content += text;
        return Content;
    }

    /*
        参数：今日头角网址url
        返回：内容图片的url list
     */
    public static List<String> GetPicUrl() {
        //String Html = getWebCon("https://www.toutiao.com/a6486290077252059661/");
        //String Html = getWebCon("https://www.toutiao.com/a6486288385974469134/");
        //String Html = getWebCon(url);

        StringBuffer buffer = new StringBuffer();
        String regex="articleInfo(.*)commentInfo";
        String Article = RegexString(Html,regex);
        Pattern pattern = Pattern.compile("//(.*?)&quot");
        // 定义一个matcher用来做匹配
        Matcher matcher = pattern.matcher(Article);

        List<String> urlList = new ArrayList(); // 图片的url数组
        while (matcher.find()){ // 找到所有相关图片的url，然后几种到list中
            String tmp = "http:" + matcher.group(0).replace("&quot",""); // 数组做一下处理
            urlList.add(tmp);
        }

//        for(int i=0; i<urlList.size(); i++){
//            System.out.println(urlList.get(i));
//        }

        return urlList;

        //System.out.println(Html);

    }
}
```




