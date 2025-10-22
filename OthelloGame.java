import java.util.ArrayList;
import java.util.List;

public class OthelloGame {
    // 棋盘：0=空，1=黑棋，-1=白棋（黑棋先行）
    private final int[][] board;
    private int currentPlayer; // 当前玩家：1（黑）/-1（白）

    public OthelloGame() {
        board = new int[8][8];
        // 初始化中心4颗棋子
        board[3][3] = 1;
        board[3][4] = -1;
        board[4][3] = -1;
        board[4][4] = 1;
        currentPlayer = 1; // 黑棋先行
    }

    // 获取棋盘（供界面绘制使用）
    public int[][] getBoard() {
        return board;
    }

    // 获取当前玩家（供界面显示使用）
    public String getCurrentPlayerText() {
        return currentPlayer == 1 ? "黑棋回合" : "白棋回合";
    }

    // 核心：判断落子是否合法，并返回可翻转的棋子位置
    private List<int[]> isValidMove(int x, int y) {
        List<int[]> flipPositions = new ArrayList<>();
        // 检查位置是否合法（在棋盘内且为空）
        if (x < 0 || x >= 8 || y < 0 || y >= 8 || board[x][y] != 0) {
            return flipPositions;
        }

        // 8个方向的偏移量（上、下、左、右、4个对角线）
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1},  {1, 0}, {1, 1}};

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int nx = x + dx; // 下一个坐标
            int ny = y + dy;
            List<int[]> temp = new ArrayList<>();

            // 沿当前方向查找：必须先遇到对方棋子
            while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && board[nx][ny] == -currentPlayer) {
                temp.add(new int[]{nx, ny}); // 记录对方棋子
                nx += dx; // 继续沿该方向移动
                ny += dy;
            }

            // 如果对方棋子后面是自己的棋子，则中间的可翻转
            if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && board[nx][ny] == currentPlayer && !temp.isEmpty()) {
                flipPositions.addAll(temp);
            }
        }
        return flipPositions;
    }

    // 落子并翻转棋子（返回是否落子成功）
    public boolean placePiece(int x, int y) {
        List<int[]> flipPositions = isValidMove(x, y);
        if (flipPositions.isEmpty()) {
            return false; // 落子无效
        }

        // 放置当前玩家的棋子
        board[x][y] = currentPlayer;
        // 翻转对方棋子
        for (int[] pos : flipPositions) {
            board[pos[0]][pos[1]] = currentPlayer;
        }

        // 切换玩家
        currentPlayer = -currentPlayer;
        return true;

    }
    public boolean isGameOver() {
        // 检查当前玩家是否有合法落子
        boolean currentHasMove = hasValidMove(currentPlayer);
        return !currentHasMove; // 当前玩家无合法落子，游戏结束
    }

    /**
     * 检查指定玩家是否有合法落子
     */
    private boolean hasValidMove(int player) {
        // 遍历棋盘所有空位，检查是否有合法落子
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] == 0) { // 空位
                    // 临时切换到该玩家，检查是否能落子
                    int originalPlayer = currentPlayer;
                    currentPlayer = player;
                    boolean isValid = !isValidMove(x, y).isEmpty();
                    currentPlayer = originalPlayer; // 恢复原玩家

                    if (isValid) {
                        return true; // 找到合法落子
                    }
                }
            }
        }
        return false; // 无合法落子
    }

    /**
     * 计算黑棋数量
     */
    public int getBlackCount() {
        int count = 0;
        for (int[] row : board) {
            for (int piece : row) {
                if (piece == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 计算白棋数量
     */
    public int getWhiteCount() {
        int count = 0;
        for (int[] row : board) {
            for (int piece : row) {
                if (piece == -1) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 获取游戏结果文本（胜负或平局）
     */
    public String getGameResult() {
        int black = getBlackCount();
        int white = getWhiteCount();
        if (black > white) {
            return "黑棋获胜！\n黑棋：" + black + " 子，白棋：" + white + " 子";
        } else if (white > black) {
            return "白棋获胜！\n白棋：" + white + " 子，黑棋：" + black + " 子";
        } else {
            return "平局！\n双方各" + black + " 子";
        }
    }
}
