package com.ygrzjh;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

public class KafkaTool2LicenseGenerate {
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String DEFAULT_NAME = "Hello";
    private static final String DEFAULT_COMPANY = "World";
    private static final String DEFAULT_VERSION = "2.0";
    private static final String DEFAULT_EXP = "2099-12-31";
    private static final String DEFAULT_SUPPORT = "2099-12-31";
    private static final String DEFAULT_TYPE = "88888888";
    private static final String DEFAULT_NUMBER = "88888888";
    private static final String DEFAULT_FILENAME = "license.ktl";
    private static final String DEFAULT_JAR_PATH = KafkaTool2LicenseGenerate.class.getProtectionDomain().getCodeSource().getLocation().getPath();


    public static void main(String[] args) throws Exception {
        if (args.length < 2 || args.length > 3) {
            printUsage();
            return;
        }
        String inputName = args[0];
        String inputCompany = args[1];

        String inputGenDirect = null;
        if (args.length >= 3) {
            inputGenDirect = args[2];
        }

        final String name = Optional.ofNullable(inputName).orElse(DEFAULT_NAME);
        final String company = Optional.ofNullable(inputCompany).orElse(DEFAULT_COMPANY);
        final String version = DEFAULT_VERSION;
        final String exp = DEFAULT_EXP;
        final String support = DEFAULT_SUPPORT;
        final String type = DEFAULT_TYPE;
        final String number = DEFAULT_NUMBER;
        final String key = genKey(name.getBytes(DEFAULT_CHARSET), company.getBytes(DEFAULT_CHARSET), version.getBytes(DEFAULT_CHARSET), support.getBytes(DEFAULT_CHARSET), exp.getBytes(DEFAULT_CHARSET), number.getBytes(DEFAULT_CHARSET), type.getBytes(DEFAULT_CHARSET));

        final String defaultGenDirect = new File(DEFAULT_JAR_PATH).getParent();
        final String path = Optional.ofNullable(inputGenDirect).orElse(defaultGenDirect);

        String content = String.format("%s=%s\n", "name", name) +
                String.format("%s=%s\n", "company", company) +
                String.format("%s=%s\n", "version", version) +
                String.format("%s=%s\n", "key", key) +
                String.format("%s=%s\n", "exp", exp) +
                String.format("%s=%s\n", "support", support) +
                String.format("%s=%s\n", "number", number) +
                String.format("%s=%s\n", "type", type);

        final File licenseFilePath = new File(path + File.separator + DEFAULT_FILENAME);
        if (licenseFilePath.exists() && !licenseFilePath.delete()) {
            System.out.println("File exists [" + licenseFilePath.getAbsolutePath() + "],Files cannot be deleted.");
        }

        try (FileOutputStream fos = new FileOutputStream(licenseFilePath);
             GZIPOutputStream gzipos = new GZIPOutputStream(fos)) {
            gzipos.write(content.getBytes());
            gzipos.finish();
        }

        System.out.println("License file has been successfully generated [" + licenseFilePath.getAbsolutePath() + "].");
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar KafkaTool2LicenseGenerate.jar <name> <company> [directory]");
        System.out.println();
    }

    private static String genKey(byte[] name, byte[] company, byte[] version, byte[] supportExp, byte[] exp, byte[] number, byte[] type) {
        int len = name.length + company.length + version.length + supportExp.length + exp.length + number.length + type.length;
        byte[] ret = new byte[len];
        int counter = 0;

        int i;
        for (i = 0; i < name.length; ++i) {
            if (name[i] % 2 == 0) {
                ret[counter++] = (byte) (name[i] ^ 255);
            }
        }

        for (i = 0; i < company.length; ++i) {
            if (company[i] % 2 != 0) {
                ret[counter++] = (byte) (company[i] ^ 255);
            }
        }

        for (i = 0; i < version.length; ++i) {
            ret[counter++] = (byte) (version[i] ^ 255);
        }

        for (i = 0; i < supportExp.length; ++i) {
            ret[counter++] = (byte) (supportExp[i] ^ 255);
        }

        for (i = 0; i < exp.length; ++i) {
            ret[counter++] = (byte) (exp[i] ^ 255);
        }

        for (i = 0; i < number.length; ++i) {
            ret[counter++] = (byte) (number[i] ^ 255);
        }

        for (i = 0; i < type.length; ++i) {
            ret[counter++] = (byte) (type[i] ^ 255);
        }

        byte[] finalRet = new byte[counter];
        System.arraycopy(ret, 0, finalRet, 0, counter);
        return bytesToHex(finalRet);
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 15];
        }

        return new String(hexChars);
    }

}
