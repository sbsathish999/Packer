package com.mobiquity.packer;

import com.mobiquity.packer.exception.APIException;

import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException {
        try {
            String result = Packer.pack("F:\\Projects\\Packer\\packages.txt");
            System.out.println(result);
        } catch (APIException e) {
          e.printStackTrace();
        }
    }
}
