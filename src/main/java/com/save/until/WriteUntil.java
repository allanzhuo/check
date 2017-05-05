package com.save.until;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.save.action.InspAction;
/**
 * 
 * @author zhangzhuo
 *
 */
public class WriteUntil {
    final static Logger logger = LoggerFactory.getLogger(WriteUntil.class);
    /**
     * 新建文件夹，并创建文件写入数据
     */
    public static void createFile(String basePath,String filePath, String filename, String input) {
        String[] dirs = filePath.split("/");
        String tempPath = basePath;
        for (String dir : dirs) {
            if (null == dir || "".equals(dir)) continue;
            tempPath += "\\" + dir;
        }
        //文件夹判断
        File dir = new File(tempPath);//"d:\\test_dir"
        if (dir.exists()) {
            if (dir.isDirectory()) {
                logger.info("文件夹存在");
            } else {
                logger.info("同名文件存在，无法创建目录");
            }
        } else {
            logger.info("文件夹不存在，开始创建");
            dir.mkdirs();
        }
        //文件判断
        File file = new File(tempPath+filename);//"d:\\test_file.txt"
        if (file.exists()) {
            logger.info(filename+"已存在");
        } else {
            logger.info(filename+"文件不存在，开始创建");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入数据
        //方法一、每次写入覆盖之前的
       /* try {
            FileOutputStream fos = new FileOutputStream(tempPath+filename);
            fos.write(input.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            FileOutputStream fos = new FileOutputStream(tempPath+filename, true);
            fos.write(input.getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
      //测试
/*    public static void main(String[] args) {
        //createFile("E:\\log", "2014/16/2/", "\\2020.txt", "hahha");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(date);
        System.out.println(dateString);
    }*/
}
