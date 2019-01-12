package phi.mixin.optimization;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.Tag;
import phi.CommandStatics;
import phi.extensions.IListTagExtensions;

@Mixin(targets="net/minecraft/command/arguments/NbtPathArgumentType$class_2204")
public class WholeAbstractListPathMixin {
	// @Redirect(method="method_9376",at=@At(value="INVOKE", target = "Lnet/minecraft/nbt/AbstractListTag;clear()V"))
	// private void cancelClear(AbstractListTag tag) {}

    // @Redirect(method = "method_9376", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/AbstractListTag;append(ILnet/minecraft/nbt/Tag;)V"))
    // private void convertAppendToSet(AbstractListTag list, int i, Tag tag) {
    //     if(list.size() == 0) {
    //         CommandStatics.CompoundChanged = true;
    //         list.append(i, tag);
    //     } else {
    //         Object originalTag = list.set(i, tag);
    //         CommandStatics.CompoundChanged = CommandStatics.CompoundChanged || !tag.equals(originalTag);
    //     }
    // }

    // This is both an optimization and a bugfix for MC-141815
    @Inject(method = "method_9376", at = @At("HEAD"), cancellable = true)
    private void replaceAll(Tag tag, Supplier<Tag> tagSupplier, CallbackInfoReturnable<Integer> ci) {
        if(!(tag instanceof IListTagExtensions) || !(tag instanceof AbstractListTag)) return;

        AbstractListTag list = (AbstractListTag) tag;
        int size = list.size();
        if(size == 0) {
            CommandStatics.CompoundChanged = true;
            list.append(0, tagSupplier.get());
            size = 1;
        } else {
            IListTagExtensions extendedList = (IListTagExtensions)tag;
            extendedList.replaceAll(tagSupplier, (oldTag, newTag, wasValid) -> {
                System.out.println(wasValid ? "valid change" : "invalid change");
                CommandStatics.CompoundChanged = CommandStatics.CompoundChanged || !wasValid || !newTag.equals(oldTag);
            });
        }
        ci.setReturnValue(size);
        ci.cancel();
	}
}
