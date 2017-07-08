package android.icu.text;

import android.icu.impl.ICUResourceBundle;
import android.icu.impl.PatternProps;
import android.icu.impl.coll.CollationSettings;
import android.icu.lang.UCharacter;
import android.icu.math.BigDecimal;
import android.icu.text.DisplayContext.Type;
import android.icu.text.PluralRules.PluralType;
import android.icu.util.ULocale;
import android.icu.util.ULocale.Category;
import android.icu.util.UResourceBundle;
import android.icu.util.UResourceBundleIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;

public class RuleBasedNumberFormat extends NumberFormat {
    private static final boolean DEBUG = false;
    public static final int DURATION = 3;
    public static final int NUMBERING_SYSTEM = 4;
    public static final int ORDINAL = 2;
    public static final int SPELLOUT = 1;
    private static final String[] locnames = null;
    private static final String[] rulenames = null;
    static final long serialVersionUID = -7664252765575395068L;
    private transient BreakIterator capitalizationBrkIter;
    private boolean capitalizationForListOrMenu;
    private boolean capitalizationForStandAlone;
    private boolean capitalizationInfoIsSet;
    private transient DecimalFormat decimalFormat;
    private transient DecimalFormatSymbols decimalFormatSymbols;
    private transient NFRule defaultInfinityRule;
    private transient NFRule defaultNaNRule;
    private transient NFRuleSet defaultRuleSet;
    private boolean lenientParse;
    private transient String lenientParseRules;
    private ULocale locale;
    private transient boolean lookedForScanner;
    private transient String postProcessRules;
    private transient RBNFPostProcessor postProcessor;
    private String[] publicRuleSetNames;
    private int roundingMode;
    private Map<String, String[]> ruleSetDisplayNames;
    private transient NFRuleSet[] ruleSets;
    private transient Map<String, NFRuleSet> ruleSetsMap;
    private transient RbnfLenientScannerProvider scannerProvider;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.icu.text.RuleBasedNumberFormat.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: android.icu.text.RuleBasedNumberFormat.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 7 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 8 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: android.icu.text.RuleBasedNumberFormat.<clinit>():void");
    }

    public RuleBasedNumberFormat(String description) {
        this.ruleSets = null;
        this.ruleSetsMap = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.roundingMode = 7;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.defaultInfinityRule = null;
        this.defaultNaNRule = null;
        this.lenientParse = DEBUG;
        this.capitalizationInfoIsSet = DEBUG;
        this.capitalizationForListOrMenu = DEBUG;
        this.capitalizationForStandAlone = DEBUG;
        this.capitalizationBrkIter = null;
        this.locale = ULocale.getDefault(Category.FORMAT);
        init(description, null);
    }

    public RuleBasedNumberFormat(String description, String[][] localizations) {
        this.ruleSets = null;
        this.ruleSetsMap = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.roundingMode = 7;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.defaultInfinityRule = null;
        this.defaultNaNRule = null;
        this.lenientParse = DEBUG;
        this.capitalizationInfoIsSet = DEBUG;
        this.capitalizationForListOrMenu = DEBUG;
        this.capitalizationForStandAlone = DEBUG;
        this.capitalizationBrkIter = null;
        this.locale = ULocale.getDefault(Category.FORMAT);
        init(description, localizations);
    }

    public RuleBasedNumberFormat(String description, Locale locale) {
        this(description, ULocale.forLocale(locale));
    }

    public RuleBasedNumberFormat(String description, ULocale locale) {
        this.ruleSets = null;
        this.ruleSetsMap = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.roundingMode = 7;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.defaultInfinityRule = null;
        this.defaultNaNRule = null;
        this.lenientParse = DEBUG;
        this.capitalizationInfoIsSet = DEBUG;
        this.capitalizationForListOrMenu = DEBUG;
        this.capitalizationForStandAlone = DEBUG;
        this.capitalizationBrkIter = null;
        this.locale = locale;
        init(description, null);
    }

    public RuleBasedNumberFormat(String description, String[][] localizations, ULocale locale) {
        this.ruleSets = null;
        this.ruleSetsMap = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.roundingMode = 7;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.defaultInfinityRule = null;
        this.defaultNaNRule = null;
        this.lenientParse = DEBUG;
        this.capitalizationInfoIsSet = DEBUG;
        this.capitalizationForListOrMenu = DEBUG;
        this.capitalizationForStandAlone = DEBUG;
        this.capitalizationBrkIter = null;
        this.locale = locale;
        init(description, localizations);
    }

    public RuleBasedNumberFormat(Locale locale, int format) {
        this(ULocale.forLocale(locale), format);
    }

    public RuleBasedNumberFormat(ULocale locale, int format) {
        this.ruleSets = null;
        this.ruleSetsMap = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.roundingMode = 7;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.defaultInfinityRule = null;
        this.defaultNaNRule = null;
        this.lenientParse = DEBUG;
        this.capitalizationInfoIsSet = DEBUG;
        this.capitalizationForListOrMenu = DEBUG;
        this.capitalizationForStandAlone = DEBUG;
        this.capitalizationBrkIter = null;
        this.locale = locale;
        ICUResourceBundle bundle = (ICUResourceBundle) UResourceBundle.getBundleInstance(ICUResourceBundle.ICU_RBNF_BASE_NAME, locale);
        ULocale uloc = bundle.getULocale();
        setLocale(uloc, uloc);
        StringBuilder description = new StringBuilder();
        String[][] strArr = null;
        try {
            UResourceBundleIterator it = bundle.getWithFallback("RBNFRules/" + rulenames[format - 1]).getIterator();
            while (it.hasNext()) {
                description.append(it.nextString());
            }
        } catch (MissingResourceException e) {
        }
        UResourceBundle locNamesBundle = bundle.findTopLevel(locnames[format - 1]);
        if (locNamesBundle != null) {
            strArr = new String[locNamesBundle.getSize()][];
            for (int i = 0; i < strArr.length; i += SPELLOUT) {
                strArr[i] = locNamesBundle.get(i).getStringArray();
            }
        }
        init(description.toString(), strArr);
    }

    public RuleBasedNumberFormat(int format) {
        this(ULocale.getDefault(Category.FORMAT), format);
    }

    public Object clone() {
        return super.clone();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(Object that) {
        if (!(that instanceof RuleBasedNumberFormat)) {
            return DEBUG;
        }
        RuleBasedNumberFormat that2 = (RuleBasedNumberFormat) that;
        if (!this.locale.equals(that2.locale) || this.lenientParse != that2.lenientParse || this.ruleSets.length != that2.ruleSets.length) {
            return DEBUG;
        }
        for (int i = 0; i < this.ruleSets.length; i += SPELLOUT) {
            if (!this.ruleSets[i].equals(that2.ruleSets[i])) {
                return DEBUG;
            }
        }
        return true;
    }

    @Deprecated
    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        NFRuleSet[] nFRuleSetArr = this.ruleSets;
        int length = nFRuleSetArr.length;
        for (int i = 0; i < length; i += SPELLOUT) {
            result.append(nFRuleSetArr[i].toString());
        }
        return result.toString();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(toString());
        out.writeObject(this.locale);
        out.writeInt(this.roundingMode);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        ULocale loc;
        String description = in.readUTF();
        try {
            loc = (ULocale) in.readObject();
        } catch (Exception e) {
            loc = ULocale.getDefault(Category.FORMAT);
        }
        try {
            this.roundingMode = in.readInt();
        } catch (Exception e2) {
        }
        RuleBasedNumberFormat temp = new RuleBasedNumberFormat(description, loc);
        this.ruleSets = temp.ruleSets;
        this.ruleSetsMap = temp.ruleSetsMap;
        this.defaultRuleSet = temp.defaultRuleSet;
        this.publicRuleSetNames = temp.publicRuleSetNames;
        this.decimalFormatSymbols = temp.decimalFormatSymbols;
        this.decimalFormat = temp.decimalFormat;
        this.locale = temp.locale;
        this.defaultInfinityRule = temp.defaultInfinityRule;
        this.defaultNaNRule = temp.defaultNaNRule;
    }

    public String[] getRuleSetNames() {
        return (String[]) this.publicRuleSetNames.clone();
    }

    public ULocale[] getRuleSetDisplayNameLocales() {
        if (this.ruleSetDisplayNames == null) {
            return null;
        }
        Set<String> s = this.ruleSetDisplayNames.keySet();
        String[] locales = (String[]) s.toArray(new String[s.size()]);
        Arrays.sort(locales, String.CASE_INSENSITIVE_ORDER);
        ULocale[] result = new ULocale[locales.length];
        for (int i = 0; i < locales.length; i += SPELLOUT) {
            result[i] = new ULocale(locales[i]);
        }
        return result;
    }

    private String[] getNameListForLocale(ULocale loc) {
        int i = 0;
        if (!(loc == null || this.ruleSetDisplayNames == null)) {
            String[] localeNames = new String[ORDINAL];
            localeNames[0] = loc.getBaseName();
            localeNames[SPELLOUT] = ULocale.getDefault(Category.DISPLAY).getBaseName();
            int length = localeNames.length;
            while (i < length) {
                for (String lname = localeNames[i]; lname.length() > 0; lname = ULocale.getFallback(lname)) {
                    String[] names = (String[]) this.ruleSetDisplayNames.get(lname);
                    if (names != null) {
                        return names;
                    }
                }
                i += SPELLOUT;
            }
        }
        return null;
    }

    public String[] getRuleSetDisplayNames(ULocale loc) {
        String[] names = getNameListForLocale(loc);
        if (names != null) {
            return (String[]) names.clone();
        }
        names = getRuleSetNames();
        for (int i = 0; i < names.length; i += SPELLOUT) {
            names[i] = names[i].substring(SPELLOUT);
        }
        return names;
    }

    public String[] getRuleSetDisplayNames() {
        return getRuleSetDisplayNames(ULocale.getDefault(Category.DISPLAY));
    }

    public String getRuleSetDisplayName(String ruleSetName, ULocale loc) {
        String[] rsnames = this.publicRuleSetNames;
        for (int ix = 0; ix < rsnames.length; ix += SPELLOUT) {
            if (rsnames[ix].equals(ruleSetName)) {
                String[] names = getNameListForLocale(loc);
                if (names != null) {
                    return names[ix];
                }
                return rsnames[ix].substring(SPELLOUT);
            }
        }
        throw new IllegalArgumentException("unrecognized rule set name: " + ruleSetName);
    }

    public String getRuleSetDisplayName(String ruleSetName) {
        return getRuleSetDisplayName(ruleSetName, ULocale.getDefault(Category.DISPLAY));
    }

    public String format(double number, String ruleSet) throws IllegalArgumentException {
        if (!ruleSet.startsWith("%%")) {
            return adjustForContext(format(number, findRuleSet(ruleSet)));
        }
        throw new IllegalArgumentException("Can't use internal rule set");
    }

    public String format(long number, String ruleSet) throws IllegalArgumentException {
        if (!ruleSet.startsWith("%%")) {
            return adjustForContext(format(number, findRuleSet(ruleSet)));
        }
        throw new IllegalArgumentException("Can't use internal rule set");
    }

    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition ignore) {
        if (toAppendTo.length() == 0) {
            toAppendTo.append(adjustForContext(format(number, this.defaultRuleSet)));
        } else {
            toAppendTo.append(format(number, this.defaultRuleSet));
        }
        return toAppendTo;
    }

    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition ignore) {
        if (toAppendTo.length() == 0) {
            toAppendTo.append(adjustForContext(format(number, this.defaultRuleSet)));
        } else {
            toAppendTo.append(format(number, this.defaultRuleSet));
        }
        return toAppendTo;
    }

    public StringBuffer format(BigInteger number, StringBuffer toAppendTo, FieldPosition pos) {
        return format(new BigDecimal(number), toAppendTo, pos);
    }

    public StringBuffer format(java.math.BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
        return format(new BigDecimal(number), toAppendTo, pos);
    }

    public StringBuffer format(BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
        return format(number.doubleValue(), toAppendTo, pos);
    }

    public Number parse(String text, ParsePosition parsePosition) {
        String workingText = text.substring(parsePosition.getIndex());
        ParsePosition workingPos = new ParsePosition(0);
        Number result = NFRule.ZERO;
        ParsePosition highWaterMark = new ParsePosition(workingPos.getIndex());
        int i = this.ruleSets.length - 1;
        while (i >= 0) {
            if (this.ruleSets[i].isPublic() && this.ruleSets[i].isParseable()) {
                Number tempResult = this.ruleSets[i].parse(workingText, workingPos, Double.MAX_VALUE);
                if (workingPos.getIndex() > highWaterMark.getIndex()) {
                    result = tempResult;
                    highWaterMark.setIndex(workingPos.getIndex());
                }
                if (highWaterMark.getIndex() == workingText.length()) {
                    break;
                }
                workingPos.setIndex(0);
            }
            i--;
        }
        parsePosition.setIndex(parsePosition.getIndex() + highWaterMark.getIndex());
        return result;
    }

    public void setLenientParseMode(boolean enabled) {
        this.lenientParse = enabled;
    }

    public boolean lenientParseEnabled() {
        return this.lenientParse;
    }

    public void setLenientScannerProvider(RbnfLenientScannerProvider scannerProvider) {
        this.scannerProvider = scannerProvider;
    }

    public RbnfLenientScannerProvider getLenientScannerProvider() {
        if (this.scannerProvider == null && this.lenientParse && !this.lookedForScanner) {
            try {
                this.lookedForScanner = true;
                setLenientScannerProvider((RbnfLenientScannerProvider) Class.forName("android.icu.impl.text.RbnfScannerProviderImpl").newInstance());
            } catch (Exception e) {
            }
        }
        return this.scannerProvider;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setDefaultRuleSet(String ruleSetName) {
        if (ruleSetName == null) {
            if (this.publicRuleSetNames.length > 0) {
                this.defaultRuleSet = findRuleSet(this.publicRuleSetNames[0]);
            } else {
                this.defaultRuleSet = null;
                int n = this.ruleSets.length;
                String currentName;
                do {
                    n--;
                    if (n >= 0) {
                        currentName = this.ruleSets[n].getName();
                        if (currentName.equals("%spellout-numbering") || currentName.equals("%digits-ordinal")) {
                            this.defaultRuleSet = this.ruleSets[n];
                        }
                    } else {
                        n = this.ruleSets.length;
                        do {
                            n--;
                            if (n < 0) {
                                break;
                            }
                        } while (!this.ruleSets[n].isPublic());
                        this.defaultRuleSet = this.ruleSets[n];
                    }
                } while (!currentName.equals("%duration"));
                this.defaultRuleSet = this.ruleSets[n];
            }
        } else if (ruleSetName.startsWith("%%")) {
            throw new IllegalArgumentException("cannot use private rule set: " + ruleSetName);
        } else {
            this.defaultRuleSet = findRuleSet(ruleSetName);
        }
    }

    public String getDefaultRuleSetName() {
        if (this.defaultRuleSet == null || !this.defaultRuleSet.isPublic()) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return this.defaultRuleSet.getName();
    }

    public void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols) {
        if (newSymbols != null) {
            this.decimalFormatSymbols = (DecimalFormatSymbols) newSymbols.clone();
            if (this.decimalFormat != null) {
                this.decimalFormat.setDecimalFormatSymbols(this.decimalFormatSymbols);
            }
            if (this.defaultInfinityRule != null) {
                this.defaultInfinityRule = null;
                getDefaultInfinityRule();
            }
            if (this.defaultNaNRule != null) {
                this.defaultNaNRule = null;
                getDefaultNaNRule();
            }
            NFRuleSet[] nFRuleSetArr = this.ruleSets;
            int length = nFRuleSetArr.length;
            for (int i = 0; i < length; i += SPELLOUT) {
                nFRuleSetArr[i].setDecimalFormatSymbols(this.decimalFormatSymbols);
            }
        }
    }

    public void setContext(DisplayContext context) {
        super.setContext(context);
        if (!this.capitalizationInfoIsSet && (context == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU || context == DisplayContext.CAPITALIZATION_FOR_STANDALONE)) {
            initCapitalizationContextInfo(this.locale);
            this.capitalizationInfoIsSet = true;
        }
        if (this.capitalizationBrkIter != null) {
            return;
        }
        if (context == DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE || ((context == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU && this.capitalizationForListOrMenu) || (context == DisplayContext.CAPITALIZATION_FOR_STANDALONE && this.capitalizationForStandAlone))) {
            this.capitalizationBrkIter = BreakIterator.getSentenceInstance(this.locale);
        }
    }

    public int getRoundingMode() {
        return this.roundingMode;
    }

    public void setRoundingMode(int roundingMode) {
        if (roundingMode < 0 || roundingMode > 7) {
            throw new IllegalArgumentException("Invalid rounding mode: " + roundingMode);
        }
        this.roundingMode = roundingMode;
    }

    NFRuleSet getDefaultRuleSet() {
        return this.defaultRuleSet;
    }

    RbnfLenientScanner getLenientScanner() {
        if (this.lenientParse) {
            RbnfLenientScannerProvider provider = getLenientScannerProvider();
            if (provider != null) {
                return provider.get(this.locale, this.lenientParseRules);
            }
        }
        return null;
    }

    DecimalFormatSymbols getDecimalFormatSymbols() {
        if (this.decimalFormatSymbols == null) {
            this.decimalFormatSymbols = new DecimalFormatSymbols(this.locale);
        }
        return this.decimalFormatSymbols;
    }

    DecimalFormat getDecimalFormat() {
        if (this.decimalFormat == null) {
            this.decimalFormat = new DecimalFormat(NumberFormat.getPattern(this.locale, 0), getDecimalFormatSymbols());
        }
        return this.decimalFormat;
    }

    PluralFormat createPluralFormat(PluralType pluralType, String pattern) {
        return new PluralFormat(this.locale, pluralType, pattern, getDecimalFormat());
    }

    NFRule getDefaultInfinityRule() {
        if (this.defaultInfinityRule == null) {
            this.defaultInfinityRule = new NFRule(this, "Inf: " + getDecimalFormatSymbols().getInfinity());
        }
        return this.defaultInfinityRule;
    }

    NFRule getDefaultNaNRule() {
        if (this.defaultNaNRule == null) {
            this.defaultNaNRule = new NFRule(this, "NaN: " + getDecimalFormatSymbols().getNaN());
        }
        return this.defaultNaNRule;
    }

    private String extractSpecial(StringBuilder description, String specialName) {
        int lp = description.indexOf(specialName);
        if (lp == -1) {
            return null;
        }
        if (lp != 0 && description.charAt(lp - 1) != ';') {
            return null;
        }
        int lpEnd = description.indexOf(";%", lp);
        if (lpEnd == -1) {
            lpEnd = description.length() - 1;
        }
        int lpStart = lp + specialName.length();
        while (lpStart < lpEnd && PatternProps.isWhiteSpace(description.charAt(lpStart))) {
            lpStart += SPELLOUT;
        }
        String result = description.substring(lpStart, lpEnd);
        description.delete(lp, lpEnd + SPELLOUT);
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void init(String description, String[][] localizations) {
        int i;
        initLocalizations(localizations);
        StringBuilder descBuf = stripWhitespace(description);
        this.lenientParseRules = extractSpecial(descBuf, "%%lenient-parse:");
        this.postProcessRules = extractSpecial(descBuf, "%%post-process:");
        int numRuleSets = SPELLOUT;
        int p = 0;
        while (true) {
            p = descBuf.indexOf(";%", p);
            if (p == -1) {
                break;
            }
            numRuleSets += SPELLOUT;
            p += ORDINAL;
        }
        this.ruleSets = new NFRuleSet[numRuleSets];
        this.ruleSetsMap = new HashMap((numRuleSets * ORDINAL) + SPELLOUT);
        this.defaultRuleSet = null;
        int publicRuleSetCount = 0;
        String[] ruleSetDescriptions = new String[numRuleSets];
        int curRuleSet = 0;
        int start = 0;
        while (true) {
            int length = this.ruleSets.length;
            if (curRuleSet >= r0) {
                break;
            }
            p = descBuf.indexOf(";%", start);
            if (p < 0) {
                p = descBuf.length() - 1;
            }
            ruleSetDescriptions[curRuleSet] = descBuf.substring(start, p + SPELLOUT);
            NFRuleSet ruleSet = new NFRuleSet(this, ruleSetDescriptions, curRuleSet);
            this.ruleSets[curRuleSet] = ruleSet;
            String currentName = ruleSet.getName();
            this.ruleSetsMap.put(currentName, ruleSet);
            if (!currentName.startsWith("%%")) {
                publicRuleSetCount += SPELLOUT;
                if (this.defaultRuleSet == null) {
                }
                if (!currentName.equals("%digits-ordinal")) {
                    if (!currentName.equals("%duration")) {
                    }
                }
                this.defaultRuleSet = ruleSet;
            }
            curRuleSet += SPELLOUT;
            start = p + SPELLOUT;
        }
        if (this.defaultRuleSet == null) {
            for (i = this.ruleSets.length - 1; i >= 0; i--) {
                if (!this.ruleSets[i].getName().startsWith("%%")) {
                    this.defaultRuleSet = this.ruleSets[i];
                    break;
                }
            }
        }
        if (this.defaultRuleSet == null) {
            this.defaultRuleSet = this.ruleSets[this.ruleSets.length - 1];
        }
        i = 0;
        while (true) {
            length = this.ruleSets.length;
            if (i >= r0) {
                break;
            }
            this.ruleSets[i].parseRules(ruleSetDescriptions[i]);
            i += SPELLOUT;
        }
        String[] publicRuleSetTemp = new String[publicRuleSetCount];
        i = this.ruleSets.length - 1;
        int publicRuleSetCount2 = 0;
        while (i >= 0) {
            if (this.ruleSets[i].getName().startsWith("%%")) {
                publicRuleSetCount = publicRuleSetCount2;
            } else {
                publicRuleSetCount = publicRuleSetCount2 + SPELLOUT;
                publicRuleSetTemp[publicRuleSetCount2] = this.ruleSets[i].getName();
            }
            i--;
            publicRuleSetCount2 = publicRuleSetCount;
        }
        if (this.publicRuleSetNames != null) {
            String name;
            i = 0;
            loop5:
            while (true) {
                length = this.publicRuleSetNames.length;
                if (i < r0) {
                    name = this.publicRuleSetNames[i];
                    int j = 0;
                    while (true) {
                        length = publicRuleSetTemp.length;
                        if (j >= r0) {
                            break loop5;
                        }
                        if (name.equals(publicRuleSetTemp[j])) {
                            break;
                        }
                        j += SPELLOUT;
                    }
                    i += SPELLOUT;
                } else {
                    this.defaultRuleSet = findRuleSet(this.publicRuleSetNames[0]);
                    return;
                }
            }
            throw new IllegalArgumentException("did not find public rule set: " + name);
        }
        this.publicRuleSetNames = publicRuleSetTemp;
    }

    private void initLocalizations(String[][] localizations) {
        if (localizations != null) {
            this.publicRuleSetNames = (String[]) localizations[0].clone();
            Map<String, String[]> m = new HashMap();
            for (int i = SPELLOUT; i < localizations.length; i += SPELLOUT) {
                String[] data = localizations[i];
                String loc = data[0];
                String[] names = new String[(data.length - 1)];
                if (names.length != this.publicRuleSetNames.length) {
                    throw new IllegalArgumentException("public name length: " + this.publicRuleSetNames.length + " != localized names[" + i + "] length: " + names.length);
                }
                System.arraycopy(data, SPELLOUT, names, 0, names.length);
                m.put(loc, names);
            }
            if (!m.isEmpty()) {
                this.ruleSetDisplayNames = m;
            }
        }
    }

    private void initCapitalizationContextInfo(ULocale theLocale) {
        boolean z = true;
        try {
            int[] intVector = ((ICUResourceBundle) UResourceBundle.getBundleInstance(ICUResourceBundle.ICU_BASE_NAME, theLocale)).getWithFallback("contextTransforms/number-spellout").getIntVector();
            if (intVector.length >= ORDINAL) {
                boolean z2;
                if (intVector[0] != 0) {
                    z2 = true;
                } else {
                    z2 = DEBUG;
                }
                this.capitalizationForListOrMenu = z2;
                if (intVector[SPELLOUT] == 0) {
                    z = DEBUG;
                }
                this.capitalizationForStandAlone = z;
            }
        } catch (MissingResourceException e) {
        }
    }

    private StringBuilder stripWhitespace(String description) {
        StringBuilder result = new StringBuilder();
        int descriptionLength = description.length();
        int start = 0;
        while (start < descriptionLength) {
            while (start < descriptionLength && PatternProps.isWhiteSpace(description.charAt(start))) {
                start += SPELLOUT;
            }
            if (start >= descriptionLength || description.charAt(start) != ';') {
                int p = description.indexOf(59, start);
                if (p != -1) {
                    if (p >= descriptionLength) {
                        break;
                    }
                    result.append(description.substring(start, p + SPELLOUT));
                    start = p + SPELLOUT;
                } else {
                    result.append(description.substring(start));
                    break;
                }
            }
            start += SPELLOUT;
        }
        return result;
    }

    private String format(double number, NFRuleSet ruleSet) {
        StringBuffer result = new StringBuffer();
        if (getRoundingMode() != 7) {
            number = new BigDecimal(Double.toString(number)).setScale(getMaximumFractionDigits(), this.roundingMode).doubleValue();
        }
        ruleSet.format(number, result, 0, 0);
        postProcess(result, ruleSet);
        return result.toString();
    }

    private String format(long number, NFRuleSet ruleSet) {
        StringBuffer result = new StringBuffer();
        ruleSet.format(number, result, 0, 0);
        postProcess(result, ruleSet);
        return result.toString();
    }

    private void postProcess(StringBuffer result, NFRuleSet ruleSet) {
        if (this.postProcessRules != null) {
            if (this.postProcessor == null) {
                int ix = this.postProcessRules.indexOf(";");
                if (ix == -1) {
                    ix = this.postProcessRules.length();
                }
                String ppClassName = this.postProcessRules.substring(0, ix).trim();
                try {
                    this.postProcessor = (RBNFPostProcessor) Class.forName(ppClassName).newInstance();
                    this.postProcessor.init(this, this.postProcessRules);
                } catch (Exception e) {
                    if (DEBUG) {
                        System.out.println("could not locate " + ppClassName + ", error " + e.getClass().getName() + ", " + e.getMessage());
                    }
                    this.postProcessor = null;
                    this.postProcessRules = null;
                    return;
                }
            }
            this.postProcessor.process(result, ruleSet);
        }
    }

    private String adjustForContext(String result) {
        if (result != null && result.length() > 0 && UCharacter.isLowerCase(result.codePointAt(0))) {
            DisplayContext capitalization = getContext(Type.CAPITALIZATION);
            if (capitalization == DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE || ((capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU && this.capitalizationForListOrMenu) || (capitalization == DisplayContext.CAPITALIZATION_FOR_STANDALONE && this.capitalizationForStandAlone))) {
                if (this.capitalizationBrkIter == null) {
                    this.capitalizationBrkIter = BreakIterator.getSentenceInstance(this.locale);
                }
                return UCharacter.toTitleCase(this.locale, result, this.capitalizationBrkIter, (int) CollationSettings.CASE_FIRST_AND_UPPER_MASK);
            }
        }
        return result;
    }

    NFRuleSet findRuleSet(String name) throws IllegalArgumentException {
        NFRuleSet result = (NFRuleSet) this.ruleSetsMap.get(name);
        if (result != null) {
            return result;
        }
        throw new IllegalArgumentException("No rule set named " + name);
    }
}
