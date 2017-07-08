package com.android.i18n.phonenumbers.prefixmapper;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

public class MappingFileProvider implements Externalizable {
    private static final Map<String, String> LOCALE_NORMALIZATION_MAP = null;
    private List<Set<String>> availableLanguages;
    private int[] countryCallingCodes;
    private int numOfEntries;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.android.i18n.phonenumbers.prefixmapper.MappingFileProvider.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: com.android.i18n.phonenumbers.prefixmapper.MappingFileProvider.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 5 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 6 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.i18n.phonenumbers.prefixmapper.MappingFileProvider.<clinit>():void");
    }

    public MappingFileProvider() {
        this.numOfEntries = 0;
    }

    public void readFileConfigs(SortedMap<Integer, Set<String>> availableDataFiles) {
        this.numOfEntries = availableDataFiles.size();
        this.countryCallingCodes = new int[this.numOfEntries];
        this.availableLanguages = new ArrayList(this.numOfEntries);
        int index = 0;
        for (Integer intValue : availableDataFiles.keySet()) {
            int countryCallingCode = intValue.intValue();
            int index2 = index + 1;
            this.countryCallingCodes[index] = countryCallingCode;
            this.availableLanguages.add(new HashSet((Collection) availableDataFiles.get(Integer.valueOf(countryCallingCode))));
            index = index2;
        }
    }

    public void readExternal(ObjectInput objectInput) throws IOException {
        this.numOfEntries = objectInput.readInt();
        if (this.countryCallingCodes == null || this.countryCallingCodes.length < this.numOfEntries) {
            this.countryCallingCodes = new int[this.numOfEntries];
        }
        if (this.availableLanguages == null) {
            this.availableLanguages = new ArrayList();
        }
        for (int i = 0; i < this.numOfEntries; i++) {
            this.countryCallingCodes[i] = objectInput.readInt();
            int numOfLangs = objectInput.readInt();
            Set<String> setOfLangs = new HashSet();
            for (int j = 0; j < numOfLangs; j++) {
                setOfLangs.add(objectInput.readUTF());
            }
            this.availableLanguages.add(setOfLangs);
        }
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeInt(this.numOfEntries);
        for (int i = 0; i < this.numOfEntries; i++) {
            objectOutput.writeInt(this.countryCallingCodes[i]);
            Set<String> setOfLangs = (Set) this.availableLanguages.get(i);
            objectOutput.writeInt(setOfLangs.size());
            for (String lang : setOfLangs) {
                objectOutput.writeUTF(lang);
            }
        }
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < this.numOfEntries; i++) {
            output.append(this.countryCallingCodes[i]);
            output.append('|');
            for (String lang : new TreeSet((Collection) this.availableLanguages.get(i))) {
                output.append(lang);
                output.append(',');
            }
            output.append('\n');
        }
        return output.toString();
    }

    String getFileName(int countryCallingCode, String language, String script, String region) {
        if (language.length() == 0) {
            return "";
        }
        int index = Arrays.binarySearch(this.countryCallingCodes, countryCallingCode);
        if (index < 0) {
            return "";
        }
        Set<String> setOfLangs = (Set) this.availableLanguages.get(index);
        if (setOfLangs.size() > 0) {
            String languageCode = findBestMatchingLanguageCode(setOfLangs, language, script, region);
            if (languageCode.length() > 0) {
                StringBuilder fileName = new StringBuilder();
                fileName.append(countryCallingCode).append('_').append(languageCode);
                return fileName.toString();
            }
        }
        return "";
    }

    private String findBestMatchingLanguageCode(Set<String> setOfLangs, String language, String script, String region) {
        String fullLocaleStr = constructFullLocale(language, script, region).toString();
        String normalizedLocale = (String) LOCALE_NORMALIZATION_MAP.get(fullLocaleStr);
        if (normalizedLocale != null && setOfLangs.contains(normalizedLocale)) {
            return normalizedLocale;
        }
        if (setOfLangs.contains(fullLocaleStr)) {
            return fullLocaleStr;
        }
        if (onlyOneOfScriptOrRegionIsEmpty(script, region)) {
            if (setOfLangs.contains(language)) {
                return language;
            }
        } else if (script.length() > 0 && region.length() > 0) {
            String langWithScriptStr = '_' + script;
            if (setOfLangs.contains(langWithScriptStr)) {
                return langWithScriptStr;
            }
            String langWithRegionStr = '_' + region;
            if (setOfLangs.contains(langWithRegionStr)) {
                return langWithRegionStr;
            }
            if (setOfLangs.contains(language)) {
                return language;
            }
        }
        return "";
    }

    private boolean onlyOneOfScriptOrRegionIsEmpty(String script, String region) {
        if (script.length() == 0 && region.length() > 0) {
            return true;
        }
        if (region.length() != 0 || script.length() <= 0) {
            return false;
        }
        return true;
    }

    private StringBuilder constructFullLocale(String language, String script, String region) {
        StringBuilder fullLocale = new StringBuilder(language);
        appendSubsequentLocalePart(script, fullLocale);
        appendSubsequentLocalePart(region, fullLocale);
        return fullLocale;
    }

    private void appendSubsequentLocalePart(String subsequentLocalePart, StringBuilder fullLocale) {
        if (subsequentLocalePart.length() > 0) {
            fullLocale.append('_').append(subsequentLocalePart);
        }
    }
}
