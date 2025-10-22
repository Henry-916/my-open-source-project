import javax.swing.*;
import java.awt.*;

public class SimpleGUI extends JFrame {
    public SimpleGUI() {
        // 设置窗口标题、大小、关闭方式
        setTitle("Swing基础");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭窗口时退出程序
        setLocationRelativeTo(null); // 窗口居中

        // 创建面板并添加组件
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout()); // 流式布局
        panel.add(new JLabel("翻转棋游戏"));
        JButton button = new JButton("点击开始");
        button.addActionListener(e -> {
            SimpleGUI.this.setVisible(false);
            // 2. 打开新的游戏窗口
            new GameWindow().setVisible(true);
        });
        panel.add(button);


        // 将面板添加到窗口
        add(panel);

        setVisible(true); // 显示窗口
    }

    public static void main(String[] args) {
        // Swing组件需在事件调度线程中创建（避免线程安全问题）
        SwingUtilities.invokeLater(SimpleGUI::new);
    }
}
