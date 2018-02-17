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