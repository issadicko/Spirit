package Filtering;


import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;
import spirit.entites.Client;

public class CustomComboRenderer extends DefaultListCellRenderer {
    public static final Color background = new Color(225, 240, 255);
    private static final Color defaultBackground = (Color) UIManager.get("List.background");
    private static final Color defaultForeground = (Color) UIManager.get("List.foreground");
    private Supplier<String> highlightTextSupplier ;

    public CustomComboRenderer(Supplier<String> highlightTextSupplier) {
        this.highlightTextSupplier = highlightTextSupplier;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Client emp = (Client) value;
        if (emp == null) {
            return this;
        }
        String text = getClientDisplayText(emp);
        text = HtmlHighlighter.highlightText(text, highlightTextSupplier.get());
        this.setText(text);
        if (!isSelected) {
            this.setBackground(index % 2 == 0 ? background : defaultBackground);
        }
        this.setForeground(defaultForeground);
        return this;
    }

    public static String getClientDisplayText(Client emp) {
        if (emp == null) {
            return "";
        }
        return String.format("%s %s [%s]", emp.getNom(), emp.getPrenom(),emp.getId());
    }
}