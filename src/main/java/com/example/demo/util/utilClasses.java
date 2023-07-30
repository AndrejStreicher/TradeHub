package com.example.demo.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class utilClasses
{
    // Converts an HTML file into a string
    public static String HTMLFileToString(String filePath)
    {
        Path path = Paths.get(filePath);
        try
        {
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
