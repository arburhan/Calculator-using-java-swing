import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Calculator extends JFrame {
    Container panel;
    JButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnClr, btnBspc, btnMod, btnsQrt, btnAdd,
            btnSub, btnMul, btnDiv, btnPnt, btnEql;
    JTextField display;

    public Calculator() {
        setTitle("AR Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 450);
        // setResizable(false);
        panel = getContentPane();
        panel.setLayout(null);

        btnClr = new JButton("Clear");
        btnClr.setBounds(10, 50, 70, 50);
        btnClr.setBackground(Color.RED);
        panel.add(btnClr);

        btnBspc = new JButton("->Back");
        btnBspc.setBounds(280, 50, 70, 50);
        btnBspc.setBackground(Color.YELLOW);
        panel.add(btnBspc);

        btn1 = new JButton("1");
        btn1.setBounds(10, 110, 70, 50);
        panel.add(btn1);

        btn2 = new JButton("2");
        btn2.setBounds(100, 110, 70, 50);
        panel.add(btn2);

        btn3 = new JButton("3");
        btn3.setBounds(190, 110, 70, 50);
        panel.add(btn3);

        btnAdd = new JButton("+");
        btnAdd.setBounds(280, 110, 70, 50);
        panel.add(btnAdd);

        btn4 = new JButton("4");
        btn4.setBounds(10, 170, 70, 50);
        panel.add(btn4);

        btn5 = new JButton("5");
        btn5.setBounds(100, 170, 70, 50);
        panel.add(btn5);

        btn6 = new JButton("6");
        btn6.setBounds(190, 170, 70, 50);
        panel.add(btn6);

        btnSub = new JButton("-");
        btnSub.setBounds(280, 170, 70, 50);
        panel.add(btnSub);

        btn7 = new JButton("7");
        btn7.setBounds(10, 230, 70, 50);
        panel.add(btn7);

        btn8 = new JButton("8");
        btn8.setBounds(100, 230, 70, 50);
        panel.add(btn8);

        btn9 = new JButton("9");
        btn9.setBounds(190, 230, 70, 50);
        panel.add(btn9);

        btnMul = new JButton("x");
        btnMul.setBounds(280, 230, 70, 50);
        panel.add(btnMul);

        btn0 = new JButton("0");
        btn0.setBounds(10, 290, 70, 50);
        panel.add(btn0);

        btnMod = new JButton("mod");
        btnMod.setBounds(100, 290, 70, 50);
        panel.add(btnMod);

        btnsQrt = new JButton("sqrt");
        btnsQrt.setBounds(190, 290, 70, 50);
        panel.add(btnsQrt);

        btnDiv = new JButton("/");
        btnDiv.setBounds(280, 290, 70, 50);
        panel.add(btnDiv);

        btnPnt = new JButton(".");
        btnPnt.setBounds(10, 350, 70, 50);
        panel.add(btnPnt);

        btnEql = new JButton("=");
        btnEql.setBounds(100, 350, 250, 50);
        btnEql.setBackground(Color.GREEN);
        panel.add(btnEql);

        setVisible(true);

        display = new JTextField();
        display.setBounds(10, 10, 340, 30);
        display.setEditable(false);
        panel.add(display);

        ActionListener buttonClickListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if ("Clear".equals(command)) {
                    display.setText("");
                } else if ("->Back".equals(command)) {
                    String currentText = display.getText();
                    if (currentText.length() > 0) {
                        display.setText(currentText.substring(0, currentText.length() - 1));
                    }
                } else if ("=".equals(command)) {
                    try {
                        double result = evaluateExpression(display.getText());
                        display.setText(String.valueOf(result));
                    } catch (ArithmeticException ae) {
                        display.setText("Error");
                        JOptionPane.showMessageDialog(panel, ae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(null, "Null Error",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(null,
                                "ArrayIndexOutOfBounds", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        display.setText("Invalid Input");
                        JOptionPane.showMessageDialog(panel, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    display.setText(display.getText() + command);
                }
            }
        };

        btn0.addActionListener(buttonClickListener);
        btn1.addActionListener(buttonClickListener);
        btn2.addActionListener(buttonClickListener);
        btn3.addActionListener(buttonClickListener);
        btn4.addActionListener(buttonClickListener);
        btn5.addActionListener(buttonClickListener);
        btn6.addActionListener(buttonClickListener);
        btn7.addActionListener(buttonClickListener);
        btn8.addActionListener(buttonClickListener);
        btn9.addActionListener(buttonClickListener);
        btnAdd.addActionListener(buttonClickListener);
        btnSub.addActionListener(buttonClickListener);
        btnMul.addActionListener(buttonClickListener);
        btnDiv.addActionListener(buttonClickListener);
        btnMod.addActionListener(buttonClickListener);
        btnsQrt.addActionListener(buttonClickListener);
        btnPnt.addActionListener(buttonClickListener);
        btnClr.addActionListener(buttonClickListener);
        btnBspc.addActionListener(buttonClickListener);
        btnEql.addActionListener(buttonClickListener);
    }

    private double evaluateExpression(String expression) {
        // Handle sqrt separately as it is a unary operator
        if (expression.contains("sqrt")) {
            String[] parts = expression.split("sqrt");
            double num = Double.parseDouble(parts[1]);
            return Math.sqrt(num);
        }

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();
        StringBuilder numberBuffer = new StringBuilder();

        for (char ch : expression.toCharArray()) {
            if (Character.isDigit(ch) || ch == '.') {
                numberBuffer.append(ch);
            } else {
                if (numberBuffer.length() > 0) {
                    numbers.push(Double.parseDouble(numberBuffer.toString()));
                    numberBuffer.setLength(0);
                }

                while (!operations.isEmpty() && precedence(ch) <= precedence(operations.peek())) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = operations.pop();
                    numbers.push(applyOperation(a, b, op));
                }

                operations.push(ch);
            }
        }

        if (numberBuffer.length() > 0) {
            numbers.push(Double.parseDouble(numberBuffer.toString()));
        }

        while (!operations.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            char op = operations.pop();
            numbers.push(applyOperation(a, b, op));
        }

        return numbers.pop();
    }

    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case 'x':
            case '/':
            case 'm':
                return 2;
        }
        return -1;
    }

    private double applyOperation(double a, double b, char op) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case 'x':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Can't divide by zero");
                }
                return a / b;
            case 'm':
                return a % b;
        }
        return 0;
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);

    }
}
