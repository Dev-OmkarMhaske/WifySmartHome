package com.wify.smart.home.mqtt;

public class CustomEncryption {

//    static char originalArr[] = {'!','{','}','"','#','@','0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
//
//    static char convertArr[] = {'&','?','+','*','%','^','V', 'T', 'w', 'X', 'D', 'c', '8', 'O', 'k', 'h', 'p', 'I', 'j', 'L', 'Y', 'v', 'f', 'g', '4', 'W', 'M', 'B', 'P', '7', 'z', 'n', 'J', 'S', 'b', 'K', 'r', 'F', '3', '9', 's', 'H', '0', 'x', 't', '1', 'C', '5', '2', 'U', 'q', 'A', 'Z', 'm', 'i', 'R', 'e', 'N', 'o', 'u', 'E', '6', 'd', 'G', 'a', 'l', 'y', 'Q'};

    static char originalArr[] = {'$', '!', '{', '}', '"', '#', '@', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    static char convertArr[] = {'<', '>', ';', '+', '*', '%', '^', 'V', 'T', 'w', 'X', 'D', 'c', '8', 'O', 'k', 'h', 'p', 'I', 'j', 'L', 'Y', 'v', 'f', 'g', '4', 'W', 'M', 'B', 'P', '7', 'z', 'n', 'J', 'S', 'b', 'K', 'r', 'F', '3', '9', 's', 'H', '0', 'x', 't', '1', 'C', '5', '2', 'U', 'q', 'A', 'Z', 'm', 'i', 'R', 'e', 'N', 'o', 'u', 'E', '6', 'd', 'G', 'a', 'l', 'y', 'Q'};

    public static String decode(String data) {

        StringBuilder stringBuilder = new StringBuilder();

        char chr;

        String append;

        for (int i = 0; i < data.length(); i++) {

            chr = data.charAt(i);

            append = null;

            for (int j = 0; j < convertArr.length; j++) {

                if (convertArr[j] == chr) {

                    append = "" + originalArr[j];

                }

            }

            if (append != null) {
                stringBuilder.append(append);
            } else {
                stringBuilder.append("" + chr);
            }

        }

        return stringBuilder.toString().replaceAll(":,", ":\"#@#@#\",");

    }

    public static String encode(String data) {

        StringBuilder stringBuilder = new StringBuilder();

        char chr;

        String append;

        try {

            for (int i = 0; i < data.length(); i++) {

                chr = data.charAt(i);

                append = null;

                for (int j = 0; j < originalArr.length; j++) {

                    if (originalArr[j] == chr) {

                        append = "" + convertArr[j];

                    }

                }

                if (append != null) {
                    stringBuilder.append(append);
                } else {
                    stringBuilder.append("" + chr);
                }

            }


        } catch (Exception e) {

            e.printStackTrace();

        }

        return stringBuilder.toString();
    }

}
