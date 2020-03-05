package com.vosievskaya.des.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.vosievskaya.des.implementation.ConstantData.amountOfRounds;
import static com.vosievskaya.des.implementation.ConstantData.blockSize;
import static com.vosievskaya.des.implementation.ConstantData.charSize;
import static com.vosievskaya.des.implementation.ConstantData.shiftKey;

public class DES {

    public String[] encode(String message, String key) {
        String[] returnArray = new String[3];

        message = makeInputDataMultipleOfBlockSize(message);
        List<String> blocks = breakDataIntoBlocks(message);
        key = correctKeyLength(key, message.length() / (2 * blocks.size()));

        returnArray[0] = key;

        key = stringToBinaryFormat(key);

        for (int i = 0; i < amountOfRounds; i++) {
            for (int j = 0; j < blocks.size(); j++) {
                blocks.set(j, firstRoundOfDESEncoding(blocks.get(j), key));
            }
            key = shiftKeyRight(key);
        }
        key = shiftKeyLeft(key);

        returnArray[1] = key;

        String result = "";
        for (int i = 0; i < blocks.size(); i++) {
            result += blocks.get(i);
        }

        returnArray[2] = binaryDataToStringFormat(result);
        return returnArray;
    }

    public String decode(String key, String data) {
        data = stringToBinaryFormat(data);

        List<String> blocks = breakBinaryDataIntoBlocks(data);

        for (int i = 0; i < amountOfRounds; i++) {
            for (int j = 0; j < blocks.size(); j++) {
                blocks.set(j, firstRoundOfDESDecoding(blocks.get(j), key));
            }
            key = shiftKeyLeft(key);
        }
//        key = shiftKeyRight(key);

        String result = "";
        for (int i = 0; i < blocks.size(); i++) {
            result += blocks.get(i);
        }

        return removeDirtySymbols(binaryDataToStringFormat(result));
    }

    private String firstRoundOfDESEncoding(String data, String key) {
        String left = data.substring(0, data.length() / 2);
        String right = data.substring(data.length() / 2);

        return right + xor(left, function(right, key));
    }

    private String firstRoundOfDESDecoding(String data, String key) {
        String left = data.substring(0, data.length() / 2);
        String right = data.substring(data.length() / 2);

        return xor(function(left, key), right) + left;
    }

    private String xor(String first, String second) {
        return IntStream.range(0, first.length())
                .mapToObj(i -> first.charAt(i) == second.charAt(i) ? (first.charAt(i) == ' ') ? " " : "0" : "1")
                .collect(Collectors.joining());
    }

    private String function(String first, String second) {
        return xor(first, second);
    }

    private String shiftKeyRight(String key) {
        key = key.replaceAll(" ", "");
        for (int i = 0; i < shiftKey; i++) {
            key = key.charAt(key.length() - 1) + key.substring(0, key.length() - 1);
        }
        return addFormat(key);
    }

    private String shiftKeyLeft(String key) {
        key = key.replaceAll(" ", "");
        for (int i = 0; i < shiftKey; i++) {
            key = key.substring(1) + key.charAt(0);
        }
        return addFormat(key);
    }

    private String addFormat(String key) {
        String result = "";
        for (int i = 0; i < key.length(); i++) {
            if (i % 8 == 0) {
                result += " ";
            }
            result += key.charAt(i);
        }
        return result.substring(1) + " ";
    }

    private String makeInputDataMultipleOfBlockSize(String data) {
        return (data.length() * charSize) % blockSize == 0 ? data : makeInputDataMultipleOfBlockSize(data += "#");
    }

    private String removeDirtySymbols(String data) {
        return data.charAt(data.length() - 1) != '#' ? data : removeDirtySymbols(data.substring(0, data.length() - 1));
    }

    private List<String> breakDataIntoBlocks(String data) {
        List<String> blocks = new ArrayList<>();
        int amountOfBlocks = (data.length() * charSize) / blockSize;
        int blockLength = data.length() / amountOfBlocks;

        IntStream.range(0, amountOfBlocks)
                .forEach(i -> blocks.add(stringToBinaryFormat(data.substring(i * blockLength, i * blockLength + blockLength))));

        return blocks;
    }

    public String stringToBinaryFormat(String text) {
        String bString = "";
        String temp = "";
        for (int i = 0; i < text.length(); i++) {
            temp = Integer.toBinaryString(text.charAt(i));
            for (int j = temp.length(); j < 8; j++) {
                temp = "0" + temp;
            }
            bString += temp + " ";
        }
        return bString;
    }

    public String binaryDataToStringFormat(String binaryCode) {
        String[] code = binaryCode.split(" ");
        String word = "";
        for (int i = 0; i < code.length; i++) {
            word += (char) Integer.parseInt(code[i], 2);
        }
        return word;
    }

    private List<String> breakBinaryDataIntoBlocks(String data) {
        List<String> blocks = new ArrayList<>();
        int amountOfBlocks = data.length() / (blockSize / 2);
        int blockLength = data.length() / amountOfBlocks;

        IntStream.range(0, amountOfBlocks)
                .forEach(i -> blocks.add(data.substring(i * blockLength, i * blockLength + blockLength)));

        return blocks;
    }

    private String correctKeyLength(String key, int necessaryLength) {
        return key.length() == necessaryLength ? key.substring(0, necessaryLength) : correctKeyLength("0" + key, necessaryLength);
    }
}
