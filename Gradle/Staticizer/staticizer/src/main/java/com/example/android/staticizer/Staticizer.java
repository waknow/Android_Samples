package com.example.android.staticizer;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;

public class Staticizer {
    void generate(File input, File outputDir, String packageName) throws IOException {
        Type type = new TypeToken<LinkedHashMap<String, Object>>() {
        }.getType();
        LinkedHashMap<String, Object> data = new Gson().fromJson(new FileReader(input), type);
        String basename = removeExtension(input.getAbsolutePath());
        TypeSpec.Builder builder = TypeSpec.classBuilder(basename)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, entry.getKey());
            FieldSpec.Builder field;

            if (entry.getValue() instanceof Float) {
                field = FieldSpec.builder(TypeName.FLOAT, fieldName)
                        .initializer("$L", entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                field = FieldSpec.builder(TypeName.DOUBLE, fieldName)
                        .initializer("$L", entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                field = FieldSpec.builder(TypeName.INT, fieldName)
                        .initializer("$L", entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                field = FieldSpec.builder(TypeName.LONG, fieldName)
                        .initializer("$L", entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                field = FieldSpec.builder(TypeName.BOOLEAN, fieldName)
                        .initializer("$L", entry.getValue());
            } else {
                field = FieldSpec.builder(String.class, fieldName)
                        .initializer("$S", entry.getValue().toString());
            }
            field.addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL).build();
            builder.addField(field.build());
        }
        JavaFile.builder(packageName, builder.build())
                .build()
                .writeTo(outputDir);
    }

    String removeExtension(String s) {
        String result;
        int sepIndex = s.lastIndexOf(System.getProperty("file.separator"));
        if (sepIndex == -1) {
            result = s;
        } else {
            result = s.substring(sepIndex + 1);
        }

        int extIndex = result.lastIndexOf(".");
        if (extIndex != -1) {
            result = result.substring(0, extIndex);
        }
        return result;
    }
}
