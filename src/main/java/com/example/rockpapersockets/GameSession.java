package com.example.rockpapersockets;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class GameSession implements Runnable {

    private final Player playerOne;
    private final Player playerTwo;
    private final ExecutorService executor;

    public GameSession(Player playerOne, Player playerTwo, ExecutorService executor) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.executor = executor;
    }


    @Override
    public void run() {
        try {
            String choiceOne, choiceTwo, result;
            do {
                Future<String> futureChoiceOne = executor.submit(playerOne::getChoice);
                Future<String> futureChoiceTwo = executor.submit(playerTwo::getChoice);
                choiceOne = futureChoiceOne.get();
                choiceTwo = futureChoiceTwo.get();

                playerOne.getOut().println("Ваш выбор: " + choiceOne + ". Выбор оппонента: " + choiceTwo);
                playerTwo.getOut().println("Ваш выбор: " + choiceTwo + ". Выбор оппонента: " + choiceOne);

                result = determineWinner(choiceOne, choiceTwo);

                playerOne.getOut().println(result);
                playerTwo.getOut().println(result);


            } while (result.contains("Игра начинается заново"));

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            try {
                playerOne.close();
                playerTwo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String determineWinner(String choiceOne, String choiceTwo) {
        if (!isValidChoice(choiceOne) || !isValidChoice(choiceTwo)) {
            return "Один из игроков сделал некорректный выбор. Игра начинается заново.";
        }

        if (choiceOne.equals(choiceTwo)) {
            return "Ничья! Игра начинается заново.";
        }

        return switch (choiceOne) {
            case "rock" -> (choiceTwo.equals("scissors")) ? "Победил " + playerOne.getName() + "!" : "Победил " + playerTwo.getName() + "!";
            case "scissors" -> (choiceTwo.equals("paper")) ? "Победил " + playerOne.getName() + "!" : "Победил " + playerTwo.getName() + "!";
            case "paper" -> (choiceTwo.equals("rock")) ? "Победил " + playerOne.getName() + "!" : "Победил " + playerTwo.getName() + "!";
            default -> "";
        };
    }

    private boolean isValidChoice(String choice) {
        return choice.equals("rock") || choice.equals("scissors") || choice.equals("paper");
    }

}
