package game;

import java.awt.Color;
import java.awt.event.KeyEvent;

// Все основные настройки игры хранятся здесь
public interface GameConstants {
    String GAME_TITLE = "Cyber Dodge";

    // Размер игрового окна
    int TABLE_WIDTH = 600;
    int TABLE_HEIGHT = 650;

    // Настройки игрока
    int PLAYER_WIDTH = 70;
    int PLAYER_HEIGHT = 25;
    int PLAYER_START_X = (TABLE_WIDTH - PLAYER_WIDTH) / 2;
    int PLAYER_Y = TABLE_HEIGHT - PLAYER_HEIGHT - 50;
    int PLAYER_SPEED = 12;

    // Настройки препятствий
    int OBSTACLE_SIZE = 30;
    int OBSTACLE_START_Y = -OBSTACLE_SIZE;
    int OBSTACLE_BASE_SPEED = 5;
    int OBSTACLE_MAX_SPEED = 10;
    int OBSTACLE_DELAY = 15;

    // Игровые параметры
    int START_LIVES = 3;
    int SCORE_FOR_DODGE = 10;
    int WIN_SCORE = 500;

    // Скорость игрового цикла
    int SLEEP_TIME = 20;

    // Клавиши управления
    int KEY_LEFT = KeyEvent.VK_LEFT;
    int KEY_RIGHT = KeyEvent.VK_RIGHT;
    int KEY_NEW_GAME = KeyEvent.VK_N;
    int KEY_PAUSE = KeyEvent.VK_P;
    int KEY_EXIT = KeyEvent.VK_Q;

    // Настройки сетки на фоне
    int GRID_STEP = 40;

    // Цвета фона
    Color BG_TOP = new Color(25, 25, 55);
    Color BG_BOTTOM = new Color(10, 50, 90);
    Color GRID_COLOR = new Color(0, 255, 255, 40);

    // Цвета объектов
    Color PLAYER_COLOR = new Color(0, 255, 255);
    Color OBSTACLE_COLOR = new Color(255, 70, 130);

    // Цвета текста и сообщений
    Color TEXT_COLOR = Color.WHITE;
    Color PANEL_COLOR = new Color(0, 0, 0, 130);
    Color OVERLAY_COLOR = new Color(0, 0, 0, 180);
    Color WIN_COLOR = new Color(100, 255, 170);
    Color GAME_OVER_COLOR = new Color(255, 80, 120);

    // Подсказка по управлению
    String HELP_TEXT = "Mouse / ← → — движение | N — новая игра | P — пауза | Q — выход";
}