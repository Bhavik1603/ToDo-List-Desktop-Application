package ToDoListDesktopApp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ToDoListDesktopApp extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskTextField;
    private JButton addButton;
    private JButton removeButton;

    private static final String FILE_NAME = "todo_list.txt";

    public ToDoListDesktopApp() {
        super("To-Do List");

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskTextField = new JTextField(20);
        addButton = new JButton("Add Task");
        removeButton = new JButton("Remove Task");

        setupUI();
        loadTasksFromFile();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(taskTextField);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);

        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTask();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addTask() {
        String task = taskTextField.getText().trim();
        if (!task.isEmpty()) {
            taskListModel.addElement(task);
            taskTextField.setText("");
            saveTasksToFile();
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex >= 0) {
            taskListModel.remove(selectedIndex);
            saveTasksToFile();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to remove.", "No Task Selected",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveTasksToFile() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
            for (int i = 0; i < taskListModel.size(); i++) {
                String task = taskListModel.get(i);
                file.writeBytes(task + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromFile() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r")) {
            String line;
            while ((line = file.readLine()) != null) {
                taskListModel.addElement(line);
            }
        } catch (IOException e) {
            // File not found or other I/O error (e.g., first time running the app)
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ToDoListDesktopApp();
            }
        });
    }
}
