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
    private JTextField reporterNameField;
    private JTextField contactField;

    private JToggleButton lostToggle;
    private JToggleButton foundToggle;
    private JButton submitButton;

    private ItemManager itemManager;

    private static final Color BG_COLOR = new Color(255, 231, 233);

    public ReportItem(ItemManager itemManager) {
        this.itemManager = itemManager;
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
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(255, 238, 224));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // Row 0: Item Name + Category
        itemNameField = new JTextField("eg: Calculator");
        itemNameField.setBorder(BorderFactory.createEtchedBorder());
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5; gbc.insets = new Insets(0, 0, 15, 20);
        form.add(buildFieldBlock("Item Name*", itemNameField), gbc);

        categoryCombo = new JComboBox<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"});
        gbc.gridx = 1; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 15, 0);
        form.add(buildFieldBlock("Category*", categoryCombo), gbc);

        // Row 1: Location Found + Date
        locationField = new JTextField("eg: Library, etc");
        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 15, 20);
        form.add(buildFieldBlock("Location Found*", locationField), gbc);

        dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        gbc.gridx = 1; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 15, 0);
        form.add(buildFieldBlock("Date*", dateSpinner), gbc);

        // Row 2: Reporter name + Contact (NEW)
        reporterNameField = new JTextField();
        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 15, 20);
        form.add(buildFieldBlock("Your Name*", reporterNameField), gbc);

        contactField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 15, 0);
        form.add(buildFieldBlock("Contact (phone/email)*", contactField), gbc);

        // Row 3: Description — spans BOTH columns, so it stretches across the full width
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setText("Any detail (brand, color, etc)");
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 15, 0);
        form.add(buildFieldBlock("Description*", descriptionScroll), gbc);

        // Row 4: Status toggle + Submit — also spans both columns
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

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 0, 0);
        form.add(row4, gbc);

        // Row 5: invisible filler — absorbs any leftover vertical space here,
        // instead of the rows above stretching to fill it.
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        form.add(new JPanel() {{ setOpaque(false); }}, gbc);

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
        // Lets the field stretch to match whatever width GridBagLayout gives
        // this block, while keeping its natural (non-stretched) height.
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));

        block.add(label);
        block.add(Box.createVerticalStrut(5));
        block.add(field);
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

        // NEW: read the reporter details. Not passed to the constructors yet,
        // because Item / LostItem / FoundItem don't have these fields yet.
        String reporterName = reporterNameField.getText();
        String contact = contactField.getText();

        //System.out.println("Submit clicked -> " + name + ", " + category + ", "
        //        + location + ", " + date + ", status=" + status);
        // TD: build a LostItem/FoundItem from these values and hand it to ItemManager
        if(status.equals("LOST")) {
            LostItem lostItem = new LostItem(name, category, location, date, description, reporterName, contact);
            itemManager.addLostItem(lostItem);
        } else {
            FoundItem foundItem = new FoundItem(name, category, location, date, description, reporterName, contact);
            itemManager.addFoundItem(foundItem);
        }
        //System.out.println("Lost items: " + itemManager.lostItem.size());
        //System.out.println("Found items: " + itemManager.foundItem.size());
    }

}