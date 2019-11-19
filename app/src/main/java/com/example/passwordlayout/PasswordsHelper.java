package com.example.passwordlayout;

import java.util.ArrayList;
import java.util.Random;

public class PasswordsHelper {
    private String[] mRussians;
    private String[] mLatins;

    public PasswordsHelper(String[] russians, String[] latins) {
        if (russians.length != latins.length) {
            throw new IllegalArgumentException();
        }
        mRussians = russians;
        mLatins = latins;
    }

    public String convert(CharSequence source) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String key = String.valueOf(Character.toLowerCase(c));

            for (int dict = 0; dict < mRussians.length; dict++) {
                if (key.equals(mRussians[dict])) {
                    result.append(Character.isUpperCase(c) ? mLatins[dict].toUpperCase() : mLatins[dict]);
                }
            }
            if (result.length() <= i) {
                result.append(c);
            }

        }
        return result.toString();
    }

    public int getQuality(CharSequence password) {
        return Math.min(password.length(), 10);
    }

    public String passwordGenerate(int length, ArrayList additions) {
        StringBuilder pass = new StringBuilder(length);
        Random random = new Random();
        pass.append((char) (random.nextInt(26) + 'a'));
        for (int i = 1; i < length; ++i) {
            char next = '0';
            int range = 10;
            switch ((int) additions.get(random.nextInt(additions.size()))) {
                case 0: {
                    next = 'a';
                    range = 26;
                }
                break;
                case 1: {
                    next = 'A';
                    range = 26;
                }
                break;
                case 2: {
                    next = '0';
                    range = 10;
                }
                break;
                case 3: {
                    next = '!';
                    range = 15;
                }
                break;
            }
            pass.append((char) (random.nextInt(range) + next));
        }

        return pass.toString();
    }
}
