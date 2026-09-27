import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// NOTE: To temporarily test this GUI, replace Main.java's contents with:
//
// import javax.swing.*;
//
// public class Main {
//     public static void main(String[] args) {
//         JFrame frame = new JFrame("LostFound");
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setSize(700, 500);
//
//         JDesktopPane desktop = new JDesktopPane();
//         frame.setContentPane(desktop);
//
//         SidebarPanel sidebar = new SidebarPanel();
//         sidebar.setVisible(true);
//         desktop.add(sidebar);
//
//         frame.setVisible(true);
//     }
// }

public class SidebarPanel extends JPanel {

    // Menu labels
    private JLabel titleLabel;
    private JLabel dashboardLabel;
    private JLabel reportItemLabel;
    private JLabel browseItemLabel;
    private JLabel claimedLabel;

    // Bottom labels
    private JLabel lostThingsLabel;
    private JLabel findWayBackLabel;

    // This is where different "pages" show up, swapped via CardLayout
    private JPanel contentArea;
    private CardLayout cardLayout;

    // Card name constants — used both when adding pages and when switching to them
    private static final String DASHBOARD = "DASHBOARD";
    private static final String REPORT_ITEM = "REPORT_ITEM";

    public SidebarPanel() {
        initComponents();
    }

    private void initComponents() {
        // No setSize() needed anymore — this panel will be told its size
        // by whatever container it's placed inside (the JFrame, in our case).

        // --- Sidebar setup ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(102, 215, 33));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        sidebar.setPreferredSize(new Dimension(165, 0));

        // Title
        titleLabel = new JLabel("VIVIDA", loadIcon("Location.png"), JLabel.LEFT);
        titleLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                titleLabelClicked(evt);
            }
        });
        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(20));

        // Menu items
        dashboardLabel = createMenuItem("Dashboard", "Dashboard.png");
        dashboardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dashboardClicked(evt);
            }
        });
        sidebar.add(dashboardLabel);
        sidebar.add(Box.createVerticalStrut(18));

        reportItemLabel = createMenuItem("Report Item", "ReportItem.png");
        reportItemLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                reportItemClicked(evt);
            }
        });
        sidebar.add(reportItemLabel);
        sidebar.add(Box.createVerticalStrut(18));

        browseItemLabel = createMenuItem("Browse Item", "Browse.png");
        browseItemLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                browseItemClicked(evt);
            }
        });
        sidebar.add(browseItemLabel);
        sidebar.add(Box.createVerticalStrut(18));

        claimedLabel = createMenuItem("Claimed", "Claimed.png");
        claimedLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                claimedClicked(evt);
            }
        });
        sidebar.add(claimedLabel);

        // Pushes everything below this down to the bottom of the sidebar
        sidebar.add(Box.createVerticalGlue());

        lostThingsLabel = new JLabel("Lost Things");
        lostThingsLabel.setForeground(new Color(51, 51, 51));
        lostThingsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lostThingsLabel);

        findWayBackLabel = new JLabel("Find there way back");
        findWayBackLabel.setForeground(new Color(0, 51, 51));
        findWayBackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(findWayBackLabel);

        // --- Content area (right side, swaps pages via CardLayout) ---
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(new Color(240, 240, 240));

        // Add each page here, giving it a name to switch to later.
        // Add the remaining two (Browse Item, Claimed) once those panels exist.
        contentArea.add(new DashboardPanel(), DASHBOARD);
        contentArea.add(new ReportItem(), REPORT_ITEM);

        // --- Put it together ---
        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
    }

    private JLabel createMenuItem(String text, String iconFileName) {
        JLabel label = new JLabel(text, loadIcon(iconFileName), JLabel.LEFT);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // Loads an icon from the "Images" folder under src.
    // If you rename/move the folder, update the path here only.
    private ImageIcon loadIcon(String fileName) {
        java.net.URL url = getClass().getResource("/Images/" + fileName);
        if (url == null) {
            System.out.println("Warning: could not find icon " + fileName);
            return null;
        }
        return new ImageIcon(url);
    }

    // --- Click handlers: fill these in as you build each page ---

    private void titleLabelClicked(MouseEvent evt) {
        // TODO
    }

    private void dashboardClicked(MouseEvent evt) {
        cardLayout.show(contentArea, DASHBOARD);
    }

    private void reportItemClicked(MouseEvent evt) {
        cardLayout.show(contentArea, REPORT_ITEM);
    }

    private void browseItemClicked(MouseEvent evt) {
        System.out.println("Browse Item clicked");
        // TODO: swap contentArea to the Browse Item page
    }

    private void claimedClicked(MouseEvent evt) {
        System.out.println("Claimed clicked");
        // TODO: swap contentArea to the Claimed page
    }
}