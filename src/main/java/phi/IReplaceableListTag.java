package phi;

import java.util.function.Supplier;

import net.minecraft.nbt.Tag;

public interface IReplaceableListTag {
    public void replaceAll(Supplier<Tag> supp, Callback callback);

    @FunctionalInterface
    public static interface Callback {
        public void call(Tag oldTag, Tag newTag, boolean wasValid);
    } 
}