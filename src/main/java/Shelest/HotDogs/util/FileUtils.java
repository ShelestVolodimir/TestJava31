package Shelest.HotDogs.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Корисні методи для роботи з файлами на диску
public class FileUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);


    public static class JacksonJSON {

        // Читає JSON файл як список об'єктів переданого типу
        public static <T> List<T> readValuesFromFile(Path filePath, Class<T> valueType) {
            try (Reader reader = new FileReader(filePath.toFile())) {
                ObjectMapper mapper = new ObjectMapper();
                CollectionType returnType = mapper.getTypeFactory().constructCollectionType(List.class, valueType);

                return mapper.readValue(reader, returnType);
            } catch (IOException e) {
                if (!(e instanceof FileNotFoundException)) {
                    LOGGER.error("Exception occurred", e);
                }
                return new ArrayList<>();
            }
        }

        // Записує список об'єктів у вказаний JSON файл БД
        public static void writeValuesToFile(Path filePath, List<?> values) {
            try {
                Files.createDirectories(filePath.getParent());

                if (!Files.exists(filePath)) {
                    Files.createFile(filePath);
                }

                try (Writer writer = new FileWriter(filePath.toFile())) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(SerializationFeature.INDENT_OUTPUT);
                    mapper.writeValue(writer, values);
                }
            } catch (IOException e) {
                LOGGER.error("Exception occurred", e);
            }
        }
    }
}
