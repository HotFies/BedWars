package ro.Aneras.ClashWars.Version.v1_16_R2;

import net.minecraft.server.v1_16_R2.*;

public class Generator extends EntityArmorStand {
	
	private int tick = 104;
	private boolean turn = true;
	
	public Generator(World world) {
		super(EntityTypes.ARMOR_STAND, world);
		noclip = true;
	}
	
	@Override
	public void movementTick() {
		if (tick == 58) {
			if (turn) {
				tick = 0;
				turn = false;
			} else {
				tick = 116;
				turn = true;
			}
		}
		double motY = 0;
		if (Math.abs(tick-58) <= 15) {
			yaw += turn ? 5 : -5;
			motY = turn ? -0.015 : 0.015;
		} else {
			yaw += turn ? 10 : -10;
			motY = turn ? -0.015 : 0.015;
		}
		double d3 = lastYaw - 10;
		if (d3 < -180.0D) {
			lastYaw += 360.0F;
		}
		if (d3 >= 180.0D) {
			lastYaw -= 360.0F;
		}
		yaw = (yaw % 360.0F);
		tick += turn ? -1 : 1;
		super.move(EnumMoveType.PLAYER, new Vec3D(0, motY, 0));
		super.setYawPitch(yaw, pitch);
	}

}