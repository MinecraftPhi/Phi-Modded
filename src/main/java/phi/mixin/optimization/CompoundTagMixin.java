package phi.mixin.optimization;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import phi.CommandStatics;

@Mixin(CompoundTag.class)
public abstract class CompoundTagMixin {
    @Redirect(method="copyFrom",at=@At(value="INVOKE",target="Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)V"))
    public void putAndCheck(CompoundTag compound, String key, Tag newTag) {
        Tag oldTag = compound.getTag(key);
        compound.put(key, newTag);
        CommandStatics.CompoundChanged = CommandStatics.CompoundChanged || !newTag.equals(oldTag);
    }
}