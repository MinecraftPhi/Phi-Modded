package phi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.Tag;
import phi.CommandStatics;

@Mixin(targets="net/minecraft/command/arguments/NbtPathArgumentType$class_2206")
public class IndexAbstractListPathMixin {
	@Redirect(method = "method_9376", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/AbstractListTag;setRaw(ILnet/minecraft/nbt/Tag;)V"))
    private void setAndCheck(AbstractListTag list, int i, Tag tag) {
        System.out.println("setAndCheck");
        Object originalTag = list.set(i, tag);
        CommandStatics.CompoundChanged = CommandStatics.CompoundChanged || !tag.equals(originalTag);
	}
}
