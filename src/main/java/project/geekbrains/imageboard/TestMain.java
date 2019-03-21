package project.geekbrains.imageboard;

import project.geekbrains.imageboard.processors.LinkFormatter;
import project.geekbrains.imageboard.processors.TagFormatter;

public class TestMain {
    public static void main(String[] args) {
        String source = ">>90>";
        LinkFormatter formatter = new LinkFormatter(source);
        String result = formatter.format();
        System.out.println(result);
    }
}
