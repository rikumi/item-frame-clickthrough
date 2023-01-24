package dev.rikumi.itemframeclickthrough;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemFrameClickthrough implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("item-frame-clickthrough");

	@Override
	public void onInitialize() {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!(entity instanceof ItemFrameEntity)) {
				return ActionResult.PASS;
			}
			ItemFrameEntity itemFrame = ((ItemFrameEntity) entity);
			if (itemFrame.getHeldItemStack().isEmpty()) {
				return ActionResult.PASS;
			}
			if (player.isSneaking()) {
				return ActionResult.PASS;
			}

			BlockPos decorationBlockPos = itemFrame.getDecorationBlockPos();
			Direction facingDirection = itemFrame.getHorizontalFacing();
			BlockPos clickedBlockPos = decorationBlockPos.offset(facingDirection.getOpposite());
			ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
			BlockHitResult blockHitResult = new BlockHitResult(decorationBlockPos.toCenterPos(), facingDirection, clickedBlockPos, false);
			interactionManager.interactBlock((ClientPlayerEntity) player, hand, blockHitResult);

			return ActionResult.SUCCESS;
		});
	}
}
