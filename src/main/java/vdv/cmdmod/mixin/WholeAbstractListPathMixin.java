package vdv.cmdmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.Tag;
import vdv.cmdmod.CommandStatics;

@Mixin(targets="net/minecraft/command/arguments/NbtPathArgumentType$class_2204")
public class WholeAbstractListPathMixin {
	@Redirect(method="method_9376",at=@At(value="INVOKE", target = "Lnet/minecraft/nbt/AbstractListTag;clear()V"))
	private void cancelClear(AbstractListTag<?> tag) {}

    @Redirect(method = "method_9376", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/AbstractListTag;append(ILnet/minecraft/nbt/Tag;)V"))
    private void convertAppendToSet(AbstractListTag list, int i, Tag tag) {
        if(list.size() == 0) {
            CommandStatics.CompoundChanged = true;
            list.append(i, tag);
        } else {
            Object originalTag = list.set(i, tag);
            CommandStatics.CompoundChanged = CommandStatics.CompoundChanged || !tag.equals(originalTag);
        }
	}
}
