package game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import java.util.ArrayList;

// Класс отрисовки игры
// Здесь рисуется фон, игрок, препятствия, счёт и сообщения
public class CyberDodgeTable extends JPanel implements GameConstants {
    private CyberDodgeGameEngine engine;

    private int playerX = PLAYER_START_X;
    private int score = 0;
    private int lives = START_LIVES;

    private boolean paused = false;
    private boolean gameFinished = false;
    private boolean playerWon = false;

    private ArrayList<Point> obstacles = new ArrayList<>();

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(TABLE_WIDTH, TABLE_HEIGHT);
    }

    public CyberDodgeTable() {
        engine = new CyberDodgeGameEngine(this);

        addMouseMotionListener(engine);
        addKeyListener(engine);

        setFocusable(true);
    }

    // Добавляем игровую панель в окно
    public void addPanelToFrame(Container container) {
        container.add(this);
    }

    // Главный метод отрисовки
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        prepareGraphics(g2d);
        drawBackground(g2d);
        drawObstacles(g2d);
        drawPlayer(g2d);
        drawText(g2d);
        drawHelpPanel(g2d);

        if (paused) {
            drawMessage(g2d, "PAUSED", TEXT_COLOR);
        }

        if (gameFinished) {
            if (playerWon) {
                drawMessage(g2d, "YOU WIN!", WIN_COLOR);
            } else {
                drawMessage(g2d, "GAME OVER", GAME_OVER_COLOR);
            }
        }

        requestFocusInWindow();
    }

    // Включаем сглаживание
    private void prepareGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    // Рисуем фон и сетку
    private void drawBackground(Graphics2D g2d) {
        GradientPaint gradient = new GradientPaint(
                0,
                0,
                BG_TOP,
                0,
                TABLE_HEIGHT,
                BG_BOTTOM
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, TABLE_WIDTH, TABLE_HEIGHT);

        g2d.setColor(GRID_COLOR);

        for (int x = 0; x < TABLE_WIDTH; x += GRID_STEP) {
            g2d.drawLine(x, 0, x, TABLE_HEIGHT);
        }

        for (int y = 0; y < TABLE_HEIGHT; y += GRID_STEP) {
            g2d.drawLine(0, y, TABLE_WIDTH, y);
        }
    }

    // Рисуем игрока
    private void drawPlayer(Graphics2D g2d) {
        g2d.setColor(PLAYER_COLOR);

        g2d.fillRoundRect(
                playerX,
                PLAYER_Y,
                PLAYER_WIDTH,
                PLAYER_HEIGHT,
                15,
                15
        );
    }

    // Рисуем препятствия
    private void drawObstacles(Graphics2D g2d) {
        g2d.setColor(OBSTACLE_COLOR);

        for (Point obstacle : obstacles) {
            g2d.fillRoundRect(
                    obstacle.x,
                    obstacle.y,
                    OBSTACLE_SIZE,
                    OBSTACLE_SIZE,
                    10,
                    10
            );
        }
    }

    // Рисуем счёт и жизни
    private void drawText(Graphics2D g2d) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, 26));

        g2d.drawString("Score: " + score, 25, 45);
        g2d.drawString("Lives: " + lives, 25, 80);
    }

    // Рисуем нижнюю панель управления
    private void drawHelpPanel(Graphics2D g2d) {
        g2d.setColor(PANEL_COLOR);
        g2d.fillRect(0, TABLE_HEIGHT - 40, TABLE_WIDTH, 40);

        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        FontMetrics metrics = g2d.getFontMetrics();
        int textX = (TABLE_WIDTH - metrics.stringWidth(HELP_TEXT)) / 2;

        g2d.drawString(HELP_TEXT, textX, TABLE_HEIGHT - 16);
    }

    // Рисуем сообщение по центру экрана
    private void drawMessage(Graphics2D g2d, String text, java.awt.Color color) {
        g2d.setColor(OVERLAY_COLOR);
        g2d.fillRect(0, 0, TABLE_WIDTH, TABLE_HEIGHT);

        g2d.setColor(color);
        g2d.setFont(new Font("Arial", Font.BOLD, 55));

        FontMetrics metrics = g2d.getFontMetrics();

        int x = (TABLE_WIDTH - metrics.stringWidth(text)) / 2;
        int y = TABLE_HEIGHT / 2;

        g2d.drawString(text, x, y);

        g2d.setFont(new Font("Arial", Font.PLAIN, 22));
        g2d.setColor(TEXT_COLOR);

        String hint = "Score: " + score + " | N – новая игра";
        FontMetrics hintMetrics = g2d.getFontMetrics();

        g2d.drawString(
                hint,
                (TABLE_WIDTH - hintMetrics.stringWidth(hint)) / 2,
                y + 45
        );
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
        repaint();
    }

    public void setObstacles(ArrayList<Point> obstacles) {
        this.obstacles = new ArrayList<>(obstacles);
        repaint();
    }

    // Получаем данные игры от движка
    public void setGameInfo(
            int score,
            int lives,
            boolean paused,
            boolean gameFinished,
            boolean playerWon
    ) {
        this.score = score;
        this.lives = lives;
        this.paused = paused;
        this.gameFinished = gameFinished;
        this.playerWon = playerWon;

        repaint();
    }

    // Запуск игры
    public static void main(String[] args) {
        JFrame frame = new JFrame(GAME_TITLE);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CyberDodgeTable table = new CyberDodgeTable();
        table.addPanelToFrame(frame.getContentPane());

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        table.requestFocusInWindow();
    }
}