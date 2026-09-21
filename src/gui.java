import javax.swing.JLabel; 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
//import java.awt.ActionListener;
import java.awt.Color;

class GUI{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(0xFFB6C1));
        mainPanel.setBounds(0,0,  800, 500);
        frame.add(mainPanel);

        JPanel dashboardJPanel = new JPanel();
        dashboardJPanel.setBackground(Color.BLUE);
        dashboardJPanel.setBounds(0, 0, 250, 500);
        mainPanel.add(dashboardJPanel);

        JLabel label = new JLabel();
        label.setText("LOST & FOUND");
        label.setForeground(Color.white);
        label.setVisible(true);
        dashboardJPanel.add(label);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}