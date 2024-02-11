package com.example.rockpapersockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {

    private final String name;
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public Player(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out.println("Введите ваше имя:");
        this.name = in.readLine();
    }

    public String getName() {
        return name;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public String getChoice() throws IOException {
        out.println("Выберите камень(rock), ножницы(scissors) или бумага(paper):");
        return in.readLine();
    }
}
