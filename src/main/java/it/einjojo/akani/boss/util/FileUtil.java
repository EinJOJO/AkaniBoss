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

    public static void copyRecursive(Path source, Path destination) throws IOException {
        if (!Files.exists(source)) return;

        Stream<Path> walk = Files.walk(source);

        walk.forEach(path -> {
            try {
                Path target = destination.resolve(source.relativize(path));
                if (Files.isDirectory(path)) {
                    Files.createDirectories(target);
                } else {
                    Files.copy(path, target);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        walk.close();
    }

}
