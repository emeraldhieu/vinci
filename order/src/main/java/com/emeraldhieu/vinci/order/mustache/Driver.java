package com.emeraldhieu.vinci.order.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class Driver {

    public static void main(String[] args) throws IOException {
        Todo todo = new Todo();
        todo.setText("Today is a good day");
        todo.setTitle("Hello world!");

        Map<String, Object> context = new HashMap<>();
        context.put("todo", todo);

        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile("templates/todo.mustache");

        try (Writer writer = new StringWriter()) {
            mustache.execute(writer, context);
            String html = writer.toString();
            System.out.println(html);
        }
    }
}
