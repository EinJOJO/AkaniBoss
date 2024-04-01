package it.einjojo.akani.boss.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Base64ItemStack {

    private Base64ItemStack() {
    }

    public static @NotNull String encode(@Nullable ItemStack item) throws Base64ConvertException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeObject(item);
            return new String(Base64Coder.encode(outputStream.toByteArray()));
        } catch (Exception exception) {
            throw new Base64ConvertException(exception);
        }
    }

    public static @Nullable ItemStack decode(@NotNull String base64) throws Base64ConvertException {
        System.out.println("deserialize-base64: " + base64);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decode(base64));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            return (ItemStack) dataInput.readObject();
        } catch (Exception exception) {
            throw new Base64ConvertException(exception);
        }
    }

    public static class Base64ConvertException extends RuntimeException {

        private final Exception exception;

        public Base64ConvertException(@NotNull Exception exception) {
            super(exception.getClass().getSimpleName() + ": " + exception.getMessage());
            this.exception = exception;
        }

        public @NotNull Exception getException() {
            return exception;
        }
    }
}
