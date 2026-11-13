package ies.sequeros.psp.airfigther.engine;

import ies.sequeros.psp.airfigther.GameBoard;

public class PhysicsEngine extends Thread {
    private static final int TARGET_UPS = 60;
    private static final long OPTIMAL_TIME = 1000000000L / TARGET_UPS; // nanosegundos por tick

    private final GameBoard gameBoard;
    private final GameContext gameContext;

    public PhysicsEngine(GameBoard gameBoard, GameContext gameContext) {
        super("PhysicsEngine");
        this.gameBoard = gameBoard;
        this.gameContext = gameContext;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        long lastUpdateTime = System.nanoTime();
        while (this.gameContext.getStatus() != GameStatus.GAME_OVER) {
            long now = System.nanoTime();
            long delta = now - lastUpdateTime;
            lastUpdateTime = now;

            this.gameBoard.processInput();

            this.gameBoard.update(delta);

            long processingTime = System.nanoTime() - now;
            long sleepTime = (OPTIMAL_TIME - processingTime) / 1000000L; // a milisegundos
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}