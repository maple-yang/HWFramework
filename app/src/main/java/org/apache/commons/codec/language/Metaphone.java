package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

@Deprecated
public class Metaphone implements StringEncoder {
    private String frontv;
    private int maxCodeLen;
    private String varson;
    private String vowels;

    public Metaphone() {
        this.vowels = "AEIOU";
        this.frontv = "EIY";
        this.varson = "CSPTG";
        this.maxCodeLen = 4;
    }

    public String metaphone(String txt) {
        if (txt == null || txt.length() == 0) {
            return "";
        }
        if (txt.length() == 1) {
            return txt.toUpperCase();
        }
        char[] inwd = txt.toUpperCase().toCharArray();
        StringBuffer local = new StringBuffer(40);
        StringBuffer code = new StringBuffer(10);
        switch (inwd[0]) {
            case 'A':
                if (inwd[1] != 'E') {
                    local.append(inwd);
                    break;
                }
                local.append(inwd, 1, inwd.length - 1);
                break;
            case 'G':
            case 'K':
            case 'P':
                if (inwd[1] != 'N') {
                    local.append(inwd);
                    break;
                }
                local.append(inwd, 1, inwd.length - 1);
                break;
            case 'W':
                if (inwd[1] != 'R') {
                    if (inwd[1] != 'H') {
                        local.append(inwd);
                        break;
                    }
                    local.append(inwd, 1, inwd.length - 1);
                    local.setCharAt(0, 'W');
                    break;
                }
                local.append(inwd, 1, inwd.length - 1);
                break;
            case 'X':
                inwd[0] = 'S';
                local.append(inwd);
                break;
            default:
                local.append(inwd);
                break;
        }
        int wdsz = local.length();
        int n = 0;
        while (code.length() < getMaxCodeLen() && n < wdsz) {
            char symb = local.charAt(n);
            if (symb == 'C' || !isPreviousChar(local, n, symb)) {
                switch (symb) {
                    case 'A':
                    case 'E':
                    case 'I':
                    case 'O':
                    case 'U':
                        if (n == 0) {
                            code.append(symb);
                            break;
                        }
                        break;
                    case 'B':
                        if (!(isPreviousChar(local, n, 'M') && isLastChar(wdsz, n))) {
                            code.append(symb);
                            break;
                        }
                    case 'C':
                        if (!isPreviousChar(local, n, 'S') || isLastChar(wdsz, n) || this.frontv.indexOf(local.charAt(n + 1)) < 0) {
                            if (!regionMatch(local, n, "CIA")) {
                                if (isLastChar(wdsz, n) || this.frontv.indexOf(local.charAt(n + 1)) < 0) {
                                    if (!isPreviousChar(local, n, 'S') || !isNextChar(local, n, 'H')) {
                                        if (isNextChar(local, n, 'H')) {
                                            if (n != 0 || wdsz < 3 || !isVowel(local, 2)) {
                                                code.append('X');
                                                break;
                                            }
                                            code.append('K');
                                            break;
                                        }
                                        code.append('K');
                                        break;
                                    }
                                    code.append('K');
                                    break;
                                }
                                code.append('S');
                                break;
                            }
                            code.append('X');
                            break;
                        }
                        break;
                        break;
                    case 'D':
                        if (!isLastChar(wdsz, n + 1) && isNextChar(local, n, 'G') && this.frontv.indexOf(local.charAt(n + 2)) >= 0) {
                            code.append('J');
                            n += 2;
                            break;
                        }
                        code.append('T');
                        break;
                        break;
                    case 'F':
                    case 'J':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'R':
                        code.append(symb);
                        break;
                    case 'G':
                        if (!(isLastChar(wdsz, n + 1) && isNextChar(local, n, 'H')) && ((isLastChar(wdsz, n + 1) || !isNextChar(local, n, 'H') || isVowel(local, n + 2)) && (n <= 0 || !(regionMatch(local, n, "GN") || regionMatch(local, n, "GNED"))))) {
                            boolean hard;
                            if (isPreviousChar(local, n, 'G')) {
                                hard = true;
                            } else {
                                hard = false;
                            }
                            if (!isLastChar(wdsz, n) && this.frontv.indexOf(local.charAt(n + 1)) >= 0 && !hard) {
                                code.append('J');
                                break;
                            }
                            code.append('K');
                            break;
                        }
                        break;
                    case 'H':
                        if (!isLastChar(wdsz, n) && ((n <= 0 || this.varson.indexOf(local.charAt(n - 1)) < 0) && isVowel(local, n + 1))) {
                            code.append('H');
                            break;
                        }
                    case 'K':
                        if (n > 0) {
                            if (!isPreviousChar(local, n, 'C')) {
                                code.append(symb);
                                break;
                            }
                        }
                        code.append(symb);
                        break;
                        break;
                    case 'P':
                        if (!isNextChar(local, n, 'H')) {
                            code.append(symb);
                            break;
                        }
                        code.append('F');
                        break;
                    case 'Q':
                        code.append('K');
                        break;
                    case 'S':
                        if (!regionMatch(local, n, "SH") && !regionMatch(local, n, "SIO") && !regionMatch(local, n, "SIA")) {
                            code.append('S');
                            break;
                        }
                        code.append('X');
                        break;
                        break;
                    case 'T':
                        if (!regionMatch(local, n, "TIA") && !regionMatch(local, n, "TIO")) {
                            if (!regionMatch(local, n, "TCH")) {
                                if (!regionMatch(local, n, "TH")) {
                                    code.append('T');
                                    break;
                                }
                                code.append('0');
                                break;
                            }
                        }
                        code.append('X');
                        break;
                        break;
                    case 'V':
                        code.append('F');
                        break;
                    case 'W':
                    case 'Y':
                        if (!isLastChar(wdsz, n) && isVowel(local, n + 1)) {
                            code.append(symb);
                            break;
                        }
                    case 'X':
                        code.append('K');
                        code.append('S');
                        break;
                    case 'Z':
                        code.append('S');
                        break;
                }
                n++;
            } else {
                n++;
            }
            if (code.length() > getMaxCodeLen()) {
                code.setLength(getMaxCodeLen());
            }
        }
        return code.toString();
    }

    private boolean isVowel(StringBuffer string, int index) {
        return this.vowels.indexOf(string.charAt(index)) >= 0;
    }

    private boolean isPreviousChar(StringBuffer string, int index, char c) {
        if (index <= 0 || index >= string.length()) {
            return false;
        }
        return string.charAt(index + -1) == c;
    }

    private boolean isNextChar(StringBuffer string, int index, char c) {
        if (index < 0 || index >= string.length() - 1) {
            return false;
        }
        return string.charAt(index + 1) == c;
    }

    private boolean regionMatch(StringBuffer string, int index, String test) {
        if (index < 0 || (test.length() + index) - 1 >= string.length()) {
            return false;
        }
        return string.substring(index, test.length() + index).equals(test);
    }

    private boolean isLastChar(int wdsz, int n) {
        return n + 1 == wdsz;
    }

    public Object encode(Object pObject) throws EncoderException {
        if (pObject instanceof String) {
            return metaphone((String) pObject);
        }
        throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
    }

    public String encode(String pString) {
        return metaphone(pString);
    }

    public boolean isMetaphoneEqual(String str1, String str2) {
        return metaphone(str1).equals(metaphone(str2));
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public void setMaxCodeLen(int maxCodeLen) {
        this.maxCodeLen = maxCodeLen;
    }
}
