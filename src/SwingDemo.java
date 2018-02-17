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