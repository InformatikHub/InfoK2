package imp;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

    @Override public void replaceText(int start, int end, String text) {
        if (text.matches("[0-9.]") || text == "") {
            super.replaceText(start, end, text);
        }
    }

    @Override public void replaceSelection(String text) {
        if (text.matches("[0-9.]") || text == "") {
            super.replaceSelection(text);
        }
    }

    public double getDoubleValue() {
        try{
            return Double.parseDouble(this.getText());
        } catch(Exception e) {
            return 0.0;
        }
    }

    public int getIntValue() {
        try{
            return Integer.parseInt(this.getText());
        } catch(Exception e) {
            return 0;
        }
    }

    public void setValue(double v) {
        setText(""+v);
    }

    public void setValue(int v) {
        setText(""+v);
    }

}