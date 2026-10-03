import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Sidebar navigation + CardLayout content area for VIVIDA.
 * Missing icons or a page that fails to build will NOT crash the app.
 */
public class SidebarPanel extends JPanel {

    // Card names
    //Here we are creating an secret code type of thing which will be used to open the page. 
    // This is necessary because we are using CardLayout which needs a name to open the page.
    // It also requires less memory as opening it directly.
    // Java is smart and does most of the linking work , and for it to trace we have specified it in addPage
    private static final String DASHBOARD    = "DASHBOARD";
    private static final String REPORT_ITEM  = "REPORT_ITEM";
    private static final String BROWSE_ITEM  = "BROWSE_ITEM";
    private static final String CLAIMED_ITEM = "CLAIMED_ITEM";

    //private ItemManager itemManager ; //= new ItemManager();

    // Look & feel
    private static final Color SIDEBAR_BG   = new Color(102, 215, 33);
    private static final Color HOVER_BG     = new Color(130, 235, 65);
    private static final Color ACTIVE_BG    = new Color(76, 175, 20);
    private static final Color CONTENT_BG   = new Color(240, 240, 240);
    private static final int   ICON_SIZE    = 20;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentArea = new JPanel(cardLayout);
    private final Map<String, JLabel> menuItems = new LinkedHashMap<>();
    private String activeCard;
    private ItemManager itemManager;
    private BrowseItem browseItem;
    private DashboardPanel dashboardPanel;


    public SidebarPanel(ItemManager itemManager) {
        this.itemManager = itemManager;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ---------- Content area ----------
        contentArea.setBackground(CONTENT_BG);
        //addPage means add a new page to the content area.
        //DASHBOARD is like the name of the page which we have defined above, here it means that open this page
        //DashboardPanel is the class name which is getting open 
        //:: new means that create that thing when it is needed 
        addPage(DASHBOARD, () -> {
            dashboardPanel = new DashboardPanel(itemManager);
            return dashboardPanel;
        });
        addPage(REPORT_ITEM, () -> new ReportItem(itemManager));
        addPage(BROWSE_ITEM, () -> {
            browseItem = new BrowseItem(itemManager);
            return browseItem;
        });
        addPage(CLAIMED_ITEM, () -> new ClaimedItem(itemManager));

        // ---------- Sidebar ----------
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        sidebar.setPreferredSize(new Dimension(180, 0));

        // Title
        JLabel titleLabel = new JLabel("VIVIDA", loadIcon("Location.png"), SwingConstants.LEFT);
        titleLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(20));

        // Menu items
        addMenuItem(sidebar, "Dashboard",   "Dashboard.png",  DASHBOARD);
        addMenuItem(sidebar, "Report Item", "ReportItem.png", REPORT_ITEM);
        addMenuItem(sidebar, "Browse Item", "Browse.png",     BROWSE_ITEM);
        addMenuItem(sidebar, "Claimed",     "Claimed.png",    CLAIMED_ITEM);

        // Push footer to the bottom
        sidebar.add(Box.createVerticalGlue());

        // Footer
        sidebar.add(createFooterLabel("Lost Things", new Color(51, 51, 51)));
        sidebar.add(createFooterLabel("Find their way back", new Color(0, 51, 51)));

        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);

        // Start on Report Item
        showCard(REPORT_ITEM);

    }

    // =========================
    // PAGES
    // =========================

    /** Builds a page safely; if it throws, a readable error panel is shown instead of crashing. */
    private void addPage(String name, Supplier<? extends Component> factory) {
        Component page;
        try {
            // page is the place where java stores newly made screens 
            // factory is the variable which is used to create the page when it is needed (DASHBOARD, etc.)
            // .get() is the function which is used to create the page when it is needed (DASHBOARD, etc.)
            page = factory.get();
        } catch (Throwable t) {
            t.printStackTrace();
            page = createErrorPage(name, t);
        }
        contentArea.add(page, name);
    }

    //This function is used to create a error page when the page is not loaded properly or if there is an error in the page.
    private JPanel createErrorPage(String name, Throwable t) {
        //This thing creates a panel which is created in case if the panel required is not loded ,to prevent an error from hapenning
        JPanel panel = new JPanel(new GridBagLayout());
        // Background se set to match the color of the content area so that it doesn't look like aomething wrong
        panel.setBackground(CONTENT_BG);
        JLabel msg = new JLabel(
                "<html><div style='text-align:center'>Could not load page: " + name
                + "<br>" + t.getClass().getSimpleName() + "</div></html>");
        msg.setForeground(Color.RED.darker());// This just makes the warning message of dark red color
        panel.add(msg);
        return panel;
    }

    // =========================
    // MENU
    // =========================

    private void addMenuItem(JPanel sidebar, String text, String iconFile, String card) {
        JLabel label = new JLabel(text, loadIcon(iconFile), SwingConstants.LEFT);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setOpaque(true);
        label.setBackground(SIDEBAR_BG);
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));

        label.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { showCard(card); }
            @Override public void mouseEntered(MouseEvent e) {
                if (!card.equals(activeCard)) label.setBackground(HOVER_BG);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!card.equals(activeCard)) label.setBackground(SIDEBAR_BG);
            }
        });

        menuItems.put(card, label);
        sidebar.add(label);
        sidebar.add(Box.createVerticalStrut(10));
    }

    private JLabel createFooterLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void showCard(String card) {
        if (card.equals(DASHBOARD) && dashboardPanel != null) {
            dashboardPanel.updateCounts();
        }
    
        if (card.equals(BROWSE_ITEM) && browseItem != null) {
            browseItem.displayItems();
        }

        cardLayout.show(contentArea, card);
        activeCard = card;

        menuItems.forEach((name, label) ->
            label.setBackground(
                name.equals(card) ? ACTIVE_BG : SIDEBAR_BG
            ));
    }

    // =========================
    // ICONS
    // =========================

    /** Loads /Images/<fileName> scaled to ICON_SIZE; returns null (no icon) if missing/broken. */
    private ImageIcon loadIcon(String fileName) {
        try {
            java.net.URL url = getClass().getResource("/Images/" + fileName);
            if (url == null) {
                System.err.println("Warning: could not find icon " + fileName);
                return null;
            }
            ImageIcon icon = new ImageIcon(url);
            if (icon.getIconWidth() <= 0) return null;
            Image scaled = icon.getImage()
                    .getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("Warning: failed to load icon " + fileName + ": " + e);
            return null;
        }
    }

    // =========================
    // OPTIONAL: quick test launcher
    // =========================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("VIVIDA");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new SidebarPanel(new ItemManager()));
            frame.setSize(950, 600);
            frame.setLocationRelativeTo(null);  
            frame.setVisible(true);
        });
    }
}