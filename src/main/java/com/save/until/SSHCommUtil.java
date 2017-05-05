package com.save.until;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
/**
 * 执行Shell工具类
 * @author zhangzhuo
 *
 */
public class SSHCommUtil {
    final static Logger logger = LoggerFactory.getLogger(SSHCommUtil.class);
    /**
     * SHH连接Linux Shell，返回结果 
     */
    public static String[] execShellCmdBySSH(String dstIp, int dstport,
            String localIp, int localPort, int timeOut, String userName,
            String password, String... cmds) throws Exception {
        Session session = null;
        Channel channel = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            session = JSCHUtil.createSession(dstIp, dstport, localIp,
                    localPort, userName, password, timeOut);
            logger.info("开始创建channel通道！");
            //创建一个channel类型的通道
            channel = session.openChannel("shell");
            // Enable agent-forwarding.
            // ((ChannelShell)channel).setAgentForwarding(true);
            // Choose the pty-type "vt102".
            // ((ChannelShell)channel).setPtyType("vt102");
            // Set environment variable "LANG" as "ja_JP.eucJP".
            // ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
            channel.connect();
            is = channel.getInputStream();
            os = channel.getOutputStream();
            String[] result = new String[cmds.length];
            for (int i = 0; i < cmds.length; i++) {
                result[i] = sendCommand(is, os, cmds[i]);
            }
            return result;
        } catch (JSchException e) {
            if (e.getMessage().contains("Auth fail")) {
                logger.error(dstIp+"服务器验证失败");
                throw new Exception("Auth error");
            } else {
                logger.error(dstIp+"服务器连接失败");
                throw new Exception("Connect error");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
            try {
                os.close();
            } catch (IOException e) {
            }
            channel.disconnect();
            session.disconnect();
        }
    }

    /**
     *执行Shell脚本并返回结果
     * 
     */
    private static String sendCommand(InputStream is, OutputStream os,
            String cmd) throws IOException {
        logger.info("开始执行脚本！");
        os.write(cmd.getBytes());
        os.flush();
        StringBuffer sb = new StringBuffer();
        int beat = 0;
        while (true) {
            if (beat > 3) {
                break;
            }
            if (is.available() > 0) {
                byte[] b = new byte[is.available()];
                is.read(b);
                sb.append(new String(b));
                beat = 0;
            } else {
                if (sb.length() > 0) {
                    beat++;
                }
                try {
                    Thread.sleep(sb.toString().trim().length() == 0 ? 1000
                            : 300);
                } catch (InterruptedException e) {
                }
            }
        }
        return sb.toString();
    }
   
}