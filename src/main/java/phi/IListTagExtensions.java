package phi;

import java.util.function.Supplier;

import net.minecraft.nbt.Tag;

public interface IListTagExtensions {
    public void replaceAll(Supplier<Tag> supp, Callback callback);

    public boolean tryAppend(int i, Tag tag);

    @FunctionalInterface
    public static interface Callback {
        public void call(Tag oldTag, Tag newTag, boolean wasValid);
    } 
}