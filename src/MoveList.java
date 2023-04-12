import javax.swing.*;
import java.awt.*;

public class MoveList extends JPanel {
    private JTextArea textArea;
    public MoveList() {
        setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void addTextRow(String text) {
        if (!textArea.getText().isEmpty()) {
            textArea.append("\n");
        }
        textArea.append(text);
    }
    public void deleteTextRow() {
        String currentText = textArea.getText();
        int lastNewLineIndex = currentText.lastIndexOf("\n");

        if (lastNewLineIndex != -1) {
            textArea.setText(currentText.substring(0, lastNewLineIndex));
        } else {
            textArea.setText("");
        }
    }
    public void clearText(){
        textArea.setText("");
    }
}
