package com.save.untils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

import com.jcraft.jsch.Session;

/**
 * SSH������
 * @author ��׿
 * 2017-4-21
 */
public class SSHUntil {

    /**
     * Զ�� ִ��������ؽ����ù�� ��ͬ���ģ�ִ����Ż᷵�أ�
     * @param host    ������
     * @param user    �û���
     * @param psw    ����
     * @param port    �˿�
     * @param command    ����
     * @return
     */
    public static String exec(String host,String user,String psw,int port,String command){
        String result="";
        Session session =null;
        ChannelExec openChannel =null;
        try {
            //����Session����
            JSch jsch=new JSch();
            session = jsch.getSession(user, host, port);
            //�����û���Ϣ
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(psw);
            session.connect();
            //����ָ�����͵�ͨ����Channel������
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            int exitStatus = openChannel.getExitStatus();
            System.out.println(exitStatus);
            openChannel.connect();  
            InputStream in = openChannel.getInputStream();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
            //��ȡ�������Ϣ
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                result+= new String(buf.getBytes("gbk"),"UTF-8")+"    <br>\r\n";  
            }  
        } catch (Exception e) {
            result+=e.getMessage();
        }finally{
            if(openChannel!=null&&!openChannel.isClosed()){
                openChannel.disconnect();
            }
            if(session!=null&&session.isConnected()){
                session.disconnect();
            }
        }
        return result;
    }



    public static void main(String args[]){
        String exec = exec("192.168.175.128", "root", "123456", 22, "top");
        System.out.println(exec);    
    }
}