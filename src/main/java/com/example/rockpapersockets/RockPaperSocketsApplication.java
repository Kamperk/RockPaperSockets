package com.example.rockpapersockets;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RockPaperSocketsApplication {

    private static final int PORT = 23;
    private static final Queue<Player> playerQueue = new ConcurrentLinkedQueue<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Player newPlayer = new Player(clientSocket);
                System.out.println(newPlayer.getName() + " подключился к игре.");
                playerQueue.add(newPlayer);

                if (playerQueue.size() >= 2) {
                    Player playerOne = playerQueue.poll();
                    Player playerTwo = playerQueue.poll();

                    GameSession gameSession = new GameSession(playerOne, playerTwo, executor);
                    executor.execute(gameSession);
                }
            }
        } finally {
            executor.shutdown();
        }
    }
}
