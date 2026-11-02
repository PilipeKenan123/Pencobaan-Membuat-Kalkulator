package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * MODEL: berisi seluruh logika kalkulator.
 * Tidak ada kode JavaFX di sini, jadi mudah diuji dan dipakai ulang.
 */
public class CalculatorModel {

    private static final String ERROR = "Error";

    private String current = "0";    // angka yang sedang tampil / diketik
    private double accumulator = 0;  // angka sebelumnya
    private String operator = null;  // + - × ÷
    private boolean startNew = true; // true = ketikan berikutnya mulai angka baru

    /** Pintu masuk tunggal: terima teks tombol, lalu jalankan aksinya. */
    public void press(String key) {
        switch (key) {
            case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> inputDigit(key);
            case "." -> inputDot();
            case "+", "-", "×", "÷" -> inputOperator(key);
            case "=" -> equals();
            case "C" -> clear();
            case "±" -> negate();
            case "%" -> percent();
            case "⌫" -> backspace();
            default -> { }
        }
    }

    /** Teks yang siap ditampilkan (ada pemisah ribuan, desimal pakai koma). */
    public String getDisplay() {
        if (current.equals(ERROR)) return ERROR;
        boolean neg = current.startsWith("-");
        String body = neg ? current.substring(1) : current;
        String[] parts = body.split("\\.", -1);
        String intPart = parts[0].replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".");
        return (neg ? "-" : "") + intPart + (parts.length > 1 ? "," + parts[1] : "");
    }

    // ====== AKSI ======

    private void inputDigit(String d) {
        if (current.equals(ERROR)) clear();
        if (startNew) {
            current = d;
            startNew = false;
        } else if (current.replace("-", "").replace(".", "").length() < 12) {
            current = current.equals("0") ? d : current + d;
        }
    }

    private void inputDot() {
        if (current.equals(ERROR)) clear();
        if (startNew) {
            current = "0.";
            startNew = false;
        } else if (!current.contains(".")) {
            current += ".";
        }
    }

    private void inputOperator(String op) {
        if (current.equals(ERROR)) return;
        if (operator != null && !startNew) {
            compute(); // rantai: 2 + 3 + ... langsung dihitung
        } else {
            accumulator = parse(current);
        }
        operator = op;
        startNew = true;
    }

    private void equals() {
        if (operator == null || current.equals(ERROR)) return;
        compute();
        operator = null;
        startNew = true;
    }

    private void compute() {
        double b = parse(current);
        double result = accumulator;
        switch (operator) {
            case "+" -> result = accumulator + b;
            case "-" -> result = accumulator - b;
            case "×" -> result = accumulator * b;
            case "÷" -> {
                if (b == 0) {
                    current = ERROR;
                    operator = null;
                    startNew = true;
                    return;
                }
                result = accumulator / b;
            }
            default -> { }
        }
        accumulator = result;
        current = format(result);
    }

    private void clear() {
        current = "0";
        accumulator = 0;
        operator = null;
        startNew = true;
    }

    private void negate() {
        if (current.equals(ERROR) || current.equals("0")) return;
        current = current.startsWith("-") ? current.substring(1) : "-" + current;
    }

    private void percent() {
        if (current.equals(ERROR)) return;
        current = format(parse(current) / 100);
        startNew = true;
    }

    private void backspace() {
        if (startNew || current.equals(ERROR)) return;
        boolean tooShort = current.length() <= 1
                || (current.length() == 2 && current.startsWith("-"));
        current = tooShort ? "0" : current.substring(0, current.length() - 1);
    }

    // ====== HELPER ======

    private double parse(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Hilangkan ".0" dan batasi desimal supaya tidak muncul 0.30000000000000004 */
    private String format(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) return ERROR;
        BigDecimal bd = BigDecimal.valueOf(v).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros();
        String s = bd.toPlainString();
        return s.equals("-0") ? "0" : s;
    }
}
