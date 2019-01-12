package phi.mixin.optimization;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import phi.CommandStatics;

@Mixin(targets="net/minecraft/command/arguments/NbtPathArgumentType$class_2207")
public abstract class PredicateListPathMixin {

    @Shadow
    private int method_9364(Tag tag_1, BiConsumer<ListTag, Integer> biConsumer_1) {
        return 0;
    }

	@Inject(method = "method_9376", at = @At("HEAD"), cancellable = true)
    private void setAndCheck(Tag tag, Supplier<Tag> tagSupplier, CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(method_9364(tag, (list, i) -> {
            Tag newTag = tagSupplier.get();
            Object originalTag = list.set(i, newTag);
            CommandStatics.CompoundChanged = CommandStatics.CompoundChanged || !newTag.equals(originalTag);
        }));
        ci.cancel();
    }
}
