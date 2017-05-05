package com.save.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.save.pojo.Cmd;
import com.save.until.MailUtil;
import com.save.until.PropertiesUtil;
import com.save.until.SSHCommUtil;
import com.save.until.WriteUntil;
/**
 * 巡检任务
 * @author zhangzhuo
 *
 */
public class InspAction {
    final static Logger logger = LoggerFactory.getLogger(InspAction.class);
    public static void main(String[] args) {
        InspAction n = new InspAction();
        try {
            n.execute();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("dd");
        }
    }
    /**
     * 执行巡检任务
     * @param args
     */
    public void execute() throws Exception{
        List<Cmd> list = this.handlerData();
        Set<String> mail = new HashSet<String>();
        for (Cmd cmd : list) {
            String ip = cmd.getIp();
            int port = 22;
            String localIp = null;
            int localPort = 0;
            int timeOut = 6000;
            String userName = cmd.getUsername();
            String password = cmd.getPassword();
            String server = cmd.getServer();
            String[] cmds = cmd.getCmds();
            String[] result = null;
            logger.info(ip+"执行巡检任务开始");
            try {
                result = SSHCommUtil.execShellCmdBySSH(ip, port, localIp, localPort, timeOut,
                        userName, password, cmds);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(ip+"巡检，服务器连接不上");
                mail.add(ip+" "+"巡检，服务器连接不上");
            }
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String dateString = formatter.format(date);
            //1、服务存活验证 2、硬盘占用验证 3、巡检结果写入文件
            if (result != null) {
                for (String string : result) {
                    if (string.contains("ps -ef|grep java")||string.contains("ps -ef|grep mongo")||string.contains("ps -ef|grep redis")) {
                        if (!string.contains(server)) {
                            mail.add(ip+" "+server+"服务不存在");
                        }
                    }
                    if (string.contains("df -h")) {
                        String patt = "^[5]\\d{1}\\%|[5-9]\\d{1}\\%|\\d{3,}\\%$";
                        String group = null;
                        Pattern p = Pattern.compile(patt);
                        Matcher m = p.matcher(string);
                        while (m.find()) {
                            group = m.group();
                        }
                        if (!StringUtils.isBlank(group)) {
                            mail.add(ip+" "+"硬盘占用超出预警线");
                        }
                    }
                    WriteUntil.createFile("E:\\save", dateString, "\\"+ip+".txt", string);
                }
                logger.info(ip+"巡检结束");
            }
        }
        //发送故障邮件通知
        if (!mail.isEmpty()||mail.size()!=0) {
            MailUtil.getInstance().sendMail(mail);
        }
    }
    /**
     * 数据处理
     * @return
     */
    private List<Cmd> handlerData(){
        logger.info("开始加载需要巡检的服务器数据");
        Cmd cmd = null;
        List<Cmd> list = new ArrayList<Cmd>(); 
        Map map = PropertiesUtil.getInstance().getAllProperty();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            cmd =new Cmd();
            cmd.setIp(entry.getKey());
            Cmd cmd2 = JSON.parseObject(entry.getValue(), Cmd.class);
            String[] cmds = cmd2.getShell().split(",");
            cmd.setCmds(cmds);
            cmd.setServer(cmd2.getServer());
            cmd.setUsername(cmd2.getUsername());
            cmd.setPassword(cmd2.getPassword());
            list.add(cmd);
        }
        logger.info("数据加载完毕");
        return list;
    }

}
