package phi.mixin.optimization;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.command.DataCommand;
import phi.CommandStatics;
import phi.extensions.IListTagExtensions;

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

	@Redirect(method="method_13910",at=@At(value="INVOKE",target="Lnet/minecraft/nbt/AbstractListTag;append(ILnet/minecraft/nbt/Tag;)V"))
	private static void append(AbstractListTag list, int i, Tag tag) {
		if(list instanceof IListTagExtensions) {
			IListTagExtensions extendedList = (IListTagExtensions)list;
			boolean success = extendedList.tryAppend(i, tag);
			CommandStatics.CompoundChanged = CommandStatics.CompoundChanged || success;
		} else {
			list.append(i, tag);
			// Other list types throw exceptions when appending the wrong type
			CommandStatics.CompoundChanged = true;
		}
	}
}
