package phi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import phi.CommandStatics;

@Mixin(targets = {"net/minecraft/command/arguments/NbtPathArgumentType$class_2205", "net/minecraft/command/arguments/NbtPathArgumentType$class_2208"})
public class CompoundTagPathMixin {
    @Redirect(method = "method_9376", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)V"))
    private void put(CompoundTag compoundTag, String key, Tag tag) {
        System.out.println("Intercepted compound put");
        if(compoundTag.containsKey(key)) {
            CommandStatics.CompoundChanged = CommandStatics.CompoundChanged || !tag.equals(compoundTag.getTag(key));
        } else {
            CommandStatics.CompoundChanged = true;
        }
        compoundTag.put(key, tag);
	}
}
