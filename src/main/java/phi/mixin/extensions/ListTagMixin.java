package phi.mixin.extensions;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import phi.extensions.IListTagExtensions;

@Mixin(ListTag.class)
public abstract class ListTagMixin extends AbstractListTag<Tag> implements IListTagExtensions {

    @Shadow
    private byte type;

    @Shadow
    private boolean canAdd(Tag tag_1) { return true; }

    // This method is used in the bugfix for MC-141815
    @Override
    public void replaceAll(Supplier<Tag> supp, Callback callback) {
        type = 0;
        int length = size();
        for (int i = 0; i < length; i++) {
            Tag newTag = supp.get();
            Tag oldTag = set(i, newTag);
            boolean valid = newTag.getType() == type;
            if(!valid) {
                remove(i);
            }
            callback.call(oldTag, newTag, valid);
        }
    }

    @Override
    public boolean tryAppend(int i, Tag tag) {
        if(canAdd(tag)) {
            append(i, tag);
            return true;
        }
        return false;
    }
}
