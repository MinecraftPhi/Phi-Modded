package phi.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.command.DataCommand;
import phi.CommandStatics;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataCommand.class)
public class DataModifyMixin {
	@Inject(method="method_13920",at=@At("HEAD"))
	private static void startModify(CallbackInfoReturnable<Integer> ci) {
		CommandStatics.CompoundChanged = false;
	}

	@Redirect(method="method_13920",at=@At(value="INVOKE",target="Lnet/minecraft/nbt/CompoundTag;copy()Lnet/minecraft/nbt/CompoundTag;"))
	private static CompoundTag cancelCopy(CompoundTag tag) {
		return tag;
	}

	@Redirect(method="method_13920",at=@At(value="INVOKE",target="Lnet/minecraft/nbt/CompoundTag;equals(Ljava/lang/Object;)Z"))
	private static boolean hasCompoundChanged(CompoundTag tag, Object other) {
		System.out.println(CommandStatics.CompoundChanged);
		return !CommandStatics.CompoundChanged;
	}
}
