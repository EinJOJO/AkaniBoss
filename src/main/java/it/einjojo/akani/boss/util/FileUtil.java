package it.einjojo.akani.boss.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileUtil {
    public static void deleteFolder(Path folder) throws IOException {
        if (!Files.exists(folder)) return;
        try (Stream<Path> stream = Files.walk(folder)) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

    }

}
