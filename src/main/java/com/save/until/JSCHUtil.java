package com.save.until;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SocketFactory;
/**
 * SSH创建与服务器连接工具类
 * @author 张卓
 * 2017-4-21
 */
public class JSCHUtil {
    final static Logger logger = LoggerFactory.getLogger(JSCHUtil.class);
  private static JSch jsch = new JSch();
  /**
   * 创建Session，并打开Session连接
   * 
   */
  public static Session createSession(String dstIp, int dstPort,
      final String localIp, final int localPort, String userName,
      String password, final int timeOut) throws JSchException {
    //jsch.setKnownHosts("/home/foo/.ssh/known_hosts");
      logger.info("开始连接："+dstIp);
    // 建立一个SSH连接
    Session session = jsch.getSession(userName, dstIp, dstPort);
    session.setPassword(password);
    
    Properties sshConfig = new Properties();
    sshConfig.put("StrictHostKeyChecking", "no");//跳过主机检查
    session.setConfig(sshConfig);
    // 此socket工厂用于创建目标主机的socket，
    // 并创建我们使用的这个socket字节流
    session.setSocketFactory(new SocketFactory() {
      public OutputStream getOutputStream(Socket socket)
          throws IOException {
        return socket.getOutputStream();
      }
      public InputStream getInputStream(Socket socket) throws IOException {
        return socket.getInputStream();
      }
      public Socket createSocket(String host, int port)
          throws IOException, UnknownHostException {
        Socket socket = new Socket();
        if (localIp != null) {
          socket.bind(new InetSocketAddress(InetAddress
              .getByName(localIp), localPort));
        }
        socket.connect(
            new InetSocketAddress(InetAddress.getByName(host), port),
            timeOut);
        return socket;
      }
    });
    session.connect(timeOut);
    return session;
  }
}