import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class ReportItem extends javax.swing.JPanel {

    private JTextField itemNameField;
    private JComboBox<String> categoryCombo;
    private JTextField locationField;
    private JSpinner dateSpinner;
    private JTextArea descriptionArea;

    private JToggleButton lostToggle;
    private JToggleButton foundToggle;
    private JButton submitButton;

    private static final Color BG_COLOR = new Color(255, 231, 233);

    public ReportItem() {
        initComponents();
    }

    private void initComponents() {
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_COLOR);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel(" Report Item");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel subtitleLabel = new JLabel("Fill in the details below to report a lost or found item");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));

        titleBlock.add(titleLabel);
        titleBlock.add(subtitleLabel);

        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(Color.orange);
        JLabel profileLabel = new JLabel("Dhyay", loadIcon("Profile.png"), JLabel.CENTER);
        profileLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        profilePanel.add(profileLabel);

        header.add(titleBlock, BorderLayout.WEST);
        header.add(profilePanel, BorderLayout.EAST);
        return header;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(new Color(255, 238, 224));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Row 1: Item Name + Category
        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 0));
        row1.setBackground(form.getBackground());

        itemNameField = new JTextField("eg: Calculator");
        itemNameField.setBorder(BorderFactory.createEtchedBorder());
        row1.add(buildFieldBlock("Item Name*", itemNameField));

        categoryCombo = new JComboBox<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"});
        row1.add(buildFieldBlock("Category*", categoryCombo));

        // Without this, BoxLayout stretches this row to fill any leftover
        // vertical space in the window, and GridLayout then stretches every
        // field inside it to match — same underlying issue as the description
        // box earlier, just the opposite direction (too tall instead of too narrow).
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, row1.getPreferredSize().height));

        // Row 2: Location Found + Date
        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));
        row2.setBackground(form.getBackground());

        locationField = new JTextField("eg: Library, etc");
        row2.add(buildFieldBlock("Location Found*", locationField));

        dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        row2.add(buildFieldBlock("Date*", dateSpinner));

        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, row2.getPreferredSize().height));

        // Row 3: Description
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setText("Any detail (brand, color, etc)");
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        // Without this, BoxLayout uses the scroll pane's default maximum size
        // (basically its preferred size) and refuses to stretch it horizontally.
        descriptionScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        JPanel row3 = buildFieldBlock("Description*", descriptionScroll);

        // Row 4: Status toggle + Submit
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        row4.setBackground(form.getBackground());

        lostToggle = new JToggleButton("LOST", loadIcon("LStatus.png"));
        lostToggle.setForeground(new Color(153, 0, 0));
        lostToggle.setBackground(new Color(255, 204, 204));

        foundToggle = new JToggleButton("FOUND", loadIcon("FStatus.png"));
        foundToggle.setForeground(new Color(0, 102, 0));
        foundToggle.setBackground(new Color(204, 255, 204));

        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(lostToggle);
        statusGroup.add(foundToggle);
        lostToggle.setSelected(true);

        submitButton = new JButton("SUBMIT REPORT");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(Color.orange);
        submitButton.addActionListener(this::submitClicked);

        row4.add(new JLabel("Item Status*"));
        row4.add(lostToggle);
        row4.add(foundToggle);
        row4.add(submitButton);

        form.add(row1);
        form.add(Box.createVerticalStrut(15));
        form.add(row2);
        form.add(Box.createVerticalStrut(15));
        form.add(row3);
        form.add(Box.createVerticalStrut(15));
        form.add(row4);
        // Absorbs any extra vertical space at the bottom instead of letting
        // BoxLayout spread it out between the rows above.
        form.add(Box.createVerticalGlue());

        return form;
    }

    private JPanel buildFieldBlock(String labelText, JComponent field) {
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setBackground(new Color(255, 238, 224));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        block.add(label);
        block.add(Box.createVerticalStrut(5));
        block.add(field);

        // Let this block stretch to fill the form's width, capped at its own
        // natural height — same BoxLayout gotcha as above.
        block.setMaximumSize(new Dimension(Integer.MAX_VALUE, block.getPreferredSize().height));
        return block;
    }

    private ImageIcon loadIcon(String fileName) {
        java.net.URL url = getClass().getResource("/Images/" + fileName);
        if (url == null) {
            System.out.println("Warning: could not find icon " + fileName);
            return null;
        }
        return new ImageIcon(url);
    }

    private void submitClicked(ActionEvent evt) {
        String name = itemNameField.getText();
        String category = (String) categoryCombo.getSelectedItem();
        String location = locationField.getText();
        Date date = (Date) dateSpinner.getValue();
        String description = descriptionArea.getText();
        String status = lostToggle.isSelected() ? "LOST" : "FOUND";

        System.out.println("Submit clicked -> " + name + ", " + category + ", "
                + location + ", " + date + ", status=" + status);
        // TODO: build a LostItem/FoundItem from these values and hand it to ItemManager
    }
}