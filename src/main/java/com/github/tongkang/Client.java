package com.github.tongkang;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("输入你的昵称");
        Scanner userInput = new Scanner(System.in);
        String name = userInput.nextLine();

        Socket socket = new Socket("127.0.0.1", 8080);

        Util.writeMessage(socket, name);

        System.out.println("连接成功！");

        new Thread(() -> readFromServer(socket)).start();

        System.out.println("输入你要发送的聊天消息");
        System.out.println("id:message,例如：1:hello代表向id为1的用户发送hello消息");
        System.out.println("id=0,代表向所有人发消息，例如id为0：代表向所有在线用户发送hello消息");
        System.out.println("---------------------------------------------------------------------");
        while (true) {

            String line = userInput.nextLine();

            if (!line.contains(":")) {
                System.out.println("输入id给之不对");
            } else {
                int colonIndex = line.indexOf(':');
                int id = Integer.parseInt(line.substring(0, colonIndex));
                String message = line.substring(colonIndex + 1);

                String json = JSON.toJSONString(new Message(id, message));
                Util.writeMessage(socket, json);
            }
        }

    }

    private static void readFromServer(Socket socket) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
