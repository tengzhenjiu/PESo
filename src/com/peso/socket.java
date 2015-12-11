package com.peso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class socket {
	private static final int PORT = 6000;
	private String ip = "192.168.1.105";
	private static BufferedWriter writer;
	private InetSocketAddress isa = null;

	// 链接服务器
	public Socket connecttoserver() throws UnknownHostException,
			IOException {

		return RequestSocket(ip, PORT);
	}

	private Socket RequestSocket(String host, int port)
			throws UnknownHostException, IOException {

		Socket consocket = new Socket();
		// 创建套接字地址，其中 IP 地址为通配符地址，端口号为指定值。
		// 有效端口值介于 0 和 65535 之间。端口号 zero 允许系统在 bind 操作中挑选暂时的端口。
		isa = new InetSocketAddress(host, port);
		// 建立服务器
		consocket.connect(isa);
		// 建立一个远程链接
		if (!consocket.isConnected()) {
			Log.e("error", "服务器连接失败");
		} else {
			Log.i("Tag", "服务器连接成功");
		}
		return consocket;
	}

	// 向服务器发送消息
	public void SendMsg(Socket Socket, String msg) throws IOException {
		writer = new BufferedWriter(new OutputStreamWriter(
				Socket.getOutputStream()));
		Log.i("msg", msg.replace("\n", "") + "/n");//
		writer.write(msg.replace("\n", " ") + "\n");
		Log.i("writer", writer.toString());
		writer.flush();
	}

	public void CloseSocket(Socket socket) throws IOException {
		writer.close();
		socket.close();
		System.out.println("客户端一线程已关闭..");
	}

	// 接受服务器消息
	public String ReceiveMsg(Socket Socket) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(Socket.getInputStream()));
		String line;
		String txt = "";
		while ((line = reader.readLine()) != null) {
			return line;
		}
		Log.i("Tag", "接收到的消息" + txt);
		return txt;
	}
}
