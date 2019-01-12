package phi.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import phi.IReplaceableListTag;

@Mixin(ListTag.class)
public abstract class ListTagMixin extends AbstractListTag<Tag> implements IReplaceableListTag {

    @Shadow
    private byte type;

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
}
