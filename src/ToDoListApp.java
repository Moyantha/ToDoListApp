import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class ToDoListApp extends JFrame {

    private ArrayList<Task> tasks = new ArrayList<>();

    private JTextField taskField;
    private JSpinner hourSpinner, minuteSpinner;
    private JComboBox<String> priorityBox;

    // Models and lists for each priority
    private DefaultListModel<Task> highModel = new DefaultListModel<>();
    private DefaultListModel<Task> mediumModel = new DefaultListModel<>();
    private DefaultListModel<Task> lowModel = new DefaultListModel<>();

    private JList<Task> highList = new JList<>(highModel);
    private JList<Task> mediumList = new JList<>(mediumModel);
    private JList<Task> lowList = new JList<>(lowModel);

    public ToDoListApp() {
        setTitle("Daily To-Do");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ---------- TOP PANEL ----------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        taskField = new JTextField(15);
        hourSpinner = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        priorityBox = new JComboBox<>(new String[]{"HIGH", "MEDIUM", "LOW"});

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addTask());

        topPanel.add(new JLabel("Task:"));
        topPanel.add(taskField);
        topPanel.add(new JLabel("Time:"));
        topPanel.add(hourSpinner);
        topPanel.add(new JLabel(":"));
        topPanel.add(minuteSpinner);
        topPanel.add(priorityBox);
        topPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);

        // ---------- CENTER PANEL: three lists stacked vertically ----------
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        centerPanel.add(wrapListWithTitle(highList, "HIGH Priority"));
        centerPanel.add(wrapListWithTitle(mediumList, "MEDIUM Priority"));
        centerPanel.add(wrapListWithTitle(lowList, "LOW Priority"));

        add(centerPanel, BorderLayout.CENTER);

        setupList(highList, highModel);
        setupList(mediumList, mediumModel);
        setupList(lowList, lowModel);

        setVisible(true);
    }

    private JPanel wrapListWithTitle(JList<Task> list, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private void setupList(JList<Task> list, DefaultListModel<Task> model) {
        list.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        list.setCellRenderer(new TaskCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index != -1) {
                    Task task = model.get(index);
                    Rectangle rect = list.getCellBounds(index, index);
                    int checkboxWidth = 20;

                    if (e.getX() - rect.x < checkboxWidth) {
                        task.toggleCompleted();
                        list.repaint(rect);
                    }
                }
            }
        });
    }

    private void addTask() {
        String title = taskField.getText().trim();
        int hour = (int) hourSpinner.getValue();
        int minute = (int) minuteSpinner.getValue();
        String priority = (String) priorityBox.getSelectedItem();

        if (title.isEmpty()) return;

        LocalTime time = LocalTime.of(hour, minute);
        Task task = new Task(title, time, priority);
        tasks.add(task);

        switch (priority) {
            case "HIGH":
                highModel.addElement(task);
                break;
            case "MEDIUM":
                mediumModel.addElement(task);
                break;
            case "LOW":
                lowModel.addElement(task);
                break;
        }

        taskField.setText("");
    }

    private class TaskCellRenderer extends JPanel implements ListCellRenderer<Task> {
        private JCheckBox checkBox = new JCheckBox();
        private JLabel label = new JLabel();

        public TaskCellRenderer() {
            setLayout(new BorderLayout());
            add(checkBox, BorderLayout.WEST);
            add(label, BorderLayout.CENTER);
            checkBox.setEnabled(false);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            checkBox.setSelected(task.isCompleted());

            String text = String.format("%s  [%s]  %s",
                    task.getTime().toString(),
                    task.getPriority(),
                    task.getTitle()
            );

            label.setText(text);
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
            label.setForeground(Color.BLACK);

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setOpaque(true);
            return this;
        }
    }

    public static void main(String[] args) {
        new ToDoListApp();
    }
}


