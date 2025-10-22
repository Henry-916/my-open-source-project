import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameWindow extends JFrame {
    private final OthelloGame game; // 游戏逻辑核心
    private JPanel boardPanel; // 绘制棋盘的面板

    public GameWindow() {
        game = new OthelloGame();
        initWindow();
    }

    // 初始化游戏窗口
    private void initWindow() {
        setTitle("翻转棋 - 游戏中");
        setSize(500, 550); // 窗口大小（比棋盘大一点，留空间显示信息）
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        // 创建主面板（垂直布局：上方显示信息，下方显示棋盘）
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // 上方信息栏（显示当前玩家）
        JLabel infoLabel = new JLabel("当前：" + game.getCurrentPlayerText(), SwingConstants.CENTER);
        infoLabel.setFont(new Font("宋体", Font.BOLD, 16));
        mainPanel.add(infoLabel, BorderLayout.NORTH);

        // 中间棋盘面板
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g); // 绘制棋盘
            }
        };
        boardPanel.setPreferredSize(new Dimension(500, 500)); // 棋盘大小
        boardPanel.setBackground(new Color(0, 150, 0)); // 棋盘底色（绿色）
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // 给棋盘添加鼠标点击事件（处理落子）
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 计算点击位置对应的棋盘坐标（8×8网格）
                int cellSize = boardPanel.getWidth() / 8;
                int x = e.getX() / cellSize; // 列坐标（0-7）
                int y = e.getY() / cellSize; // 行坐标（0-7）

                // 尝试落子
                boolean success = game.placePiece(x, y);
                if (success) {
                    // 落子成功，更新界面
                    infoLabel.setText("当前：" + game.getCurrentPlayerText());
                    boardPanel.repaint(); // 重绘棋盘
                    if (game.isGameOver()) {
                        // 游戏结束，显示结果
                        String result = game.getGameResult();
                        JOptionPane.showMessageDialog(GameWindow.this, result, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
                        // 结束后可关闭窗口或重置游戏（这里选择关闭）
                        dispose(); // 关闭游戏窗口
                        // 可选：重新显示主界面
                        new SimpleGUI().setVisible(true);
                    }
                } else {
                    // 落子无效，提示
                    JOptionPane.showMessageDialog(GameWindow.this, "此处不能落子！");
                }
            }
        });

        add(mainPanel);
    }

    // 绘制棋盘和棋子
    private void drawBoard(Graphics g) {
        int cellSize = boardPanel.getWidth() / 8;
        // 1. 绘制网格线（黑色）
        g.setColor(Color.BLACK);
        for (int i = 0; i <= 8; i++) {
            // 横线
            g.drawLine(0, i * cellSize, 8 * cellSize, i * cellSize);
            // 竖线
            g.drawLine(i * cellSize, 0, i * cellSize, 8 * cellSize);
        }

        // 2. 绘制棋子
        int[][] board = game.getBoard();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int piece = board[x][y];
                if (piece != 0) {
                    // 黑棋或白棋
                    g.setColor(piece == 1 ? Color.BLACK : Color.WHITE);
                    // 绘制圆形棋子（留2像素边距，更美观）
                    g.fillOval(x * cellSize + 2, y * cellSize + 2,
                            cellSize - 4, cellSize - 4);
                    // 棋子边框（黑色）
                    g.setColor(Color.BLACK);
                    g.drawOval(x * cellSize + 2, y * cellSize + 2,
                            cellSize - 4, cellSize - 4);
                }
            }
        }
    }
}
