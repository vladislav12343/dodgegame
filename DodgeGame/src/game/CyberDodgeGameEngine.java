package game;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// Игровой движок
// Здесь логика игры, поток, мышь и клавиатура
public class CyberDodgeGameEngine implements Runnable,
        MouseMotionListener, KeyListener, GameConstants {

    private CyberDodgeTable table;

    private int playerX = PLAYER_START_X;
    private int score;
    private int lives;

    private boolean gameRunning;
    private boolean paused;
    private boolean playerWon;

    private boolean movingLeft;
    private boolean movingRight;

    private int frameCounter;

    private ArrayList<Point> obstacles = new ArrayList<>();
    private Random random = new Random();

    public CyberDodgeGameEngine(CyberDodgeTable table) {
        this.table = table;

        startNewGame();

        // Запускаем игровой цикл в отдельном потоке
        Thread worker = new Thread(this);
        worker.start();
    }

    // Мышь двигает игрока
    public void mouseMoved(MouseEvent e) {
        movePlayerToMouse(e.getX());
    }

    public void mouseDragged(MouseEvent e) {
        movePlayerToMouse(e.getX());
    }

    // Обработка клавиш
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KEY_NEW_GAME) {
            startNewGame();
        } else if (key == KEY_PAUSE) {
            switchPause();
        } else if (key == KEY_EXIT) {
            System.exit(0);
        } else if (key == KEY_LEFT) {
            movingLeft = true;
        } else if (key == KEY_RIGHT) {
            movingRight = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KEY_LEFT) {
            movingLeft = false;
        } else if (key == KEY_RIGHT) {
            movingRight = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    // Основной игровой цикл
    public void run() {
        while (true) {
            if (gameRunning && !paused) {
                movePlayerByKeyboard();
                moveObstacles();
                createObstacle();
                checkCollisions();
                checkWin();
            }

            updateTable();

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Новая игра
    private void startNewGame() {
        playerX = PLAYER_START_X;
        score = 0;
        lives = START_LIVES;

        gameRunning = true;
        paused = false;
        playerWon = false;

        movingLeft = false;
        movingRight = false;

        frameCounter = 0;
        obstacles.clear();

        updateTable();
    }

    // Пауза
    private void switchPause() {
        if (!gameRunning) {
            return;
        }

        paused = !paused;
    }

    // Движение игрока стрелками
    private void movePlayerByKeyboard() {
        if (movingLeft) {
            playerX -= PLAYER_SPEED;
        }

        if (movingRight) {
            playerX += PLAYER_SPEED;
        }

        keepPlayerInsideTable();
    }

    // Движение игрока мышью
    private void movePlayerToMouse(int mouseX) {
        if (!gameRunning || paused) {
            return;
        }

        playerX = mouseX - PLAYER_WIDTH / 2;

        keepPlayerInsideTable();
    }

    // Не даём игроку выйти за границы окна
    private void keepPlayerInsideTable() {
        if (playerX < 0) {
            playerX = 0;
        }

        if (playerX > TABLE_WIDTH - PLAYER_WIDTH) {
            playerX = TABLE_WIDTH - PLAYER_WIDTH;
        }
    }

    // Двигаем препятствия вниз
    private void moveObstacles() {
        Iterator<Point> iterator = obstacles.iterator();

        while (iterator.hasNext()) {
            Point obstacle = iterator.next();

            obstacle.y += getObstacleSpeed();

            // Если блок прошёл ниже экрана, игрок получает очки
            if (obstacle.y > TABLE_HEIGHT) {
                iterator.remove();
                score += SCORE_FOR_DODGE;
            }
        }
    }

    // Создаём новое препятствие
    private void createObstacle() {
        frameCounter++;

        if (frameCounter <= OBSTACLE_DELAY) {
            return;
        }

        frameCounter = 0;

        int x = random.nextInt(TABLE_WIDTH - OBSTACLE_SIZE + 1);

        obstacles.add(new Point(x, OBSTACLE_START_Y));
    }

    // Скорость препятствий растёт вместе со счётом
    private int getObstacleSpeed() {
        int speed = OBSTACLE_BASE_SPEED + score / 100;

        if (speed > OBSTACLE_MAX_SPEED) {
            speed = OBSTACLE_MAX_SPEED;
        }

        return speed;
    }

    // Проверяем столкновение игрока с блоками
    private void checkCollisions() {
        Iterator<Point> iterator = obstacles.iterator();

        while (iterator.hasNext()) {
            Point obstacle = iterator.next();

            boolean horizontalHit = playerX < obstacle.x + OBSTACLE_SIZE
                    && playerX + PLAYER_WIDTH > obstacle.x;

            boolean verticalHit = PLAYER_Y < obstacle.y + OBSTACLE_SIZE
                    && PLAYER_Y + PLAYER_HEIGHT > obstacle.y;

            // Если есть пересечение по X и Y, значит игрок задел блок
            if (horizontalHit && verticalHit) {
                iterator.remove();
                lives--;

                if (lives <= 0) {
                    gameRunning = false;
                }

                break;
            }
        }
    }

    // Победа после нужного количества очков
    private void checkWin() {
        if (score >= WIN_SCORE) {
            gameRunning = false;
            playerWon = true;
        }
    }

    // Передаём данные в класс отрисовки
    private void updateTable() {
        table.setPlayerX(playerX);
        table.setObstacles(obstacles);
        table.setGameInfo(score, lives, paused, !gameRunning, playerWon);
    }
}