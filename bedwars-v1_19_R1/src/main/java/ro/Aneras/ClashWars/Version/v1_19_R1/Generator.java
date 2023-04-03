package ro.Aneras.ClashWars.Version.v1_19_R1;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;

public class Generator extends EntityArmorStand {
	
	private int tick = 104;
	private boolean turn = true;
	
	public Generator(World world) {
		super(EntityTypes.d, world);
		Q = true;
	}
	
	@Override
	public void w_() {
		if (tick == 58) {
			if (turn) {
				tick = 0;
				turn = false;
			} else {
				tick = 116;
				turn = true;
			}
		}
		double motY;
		float yaw = ds();
		if (Math.abs(tick-58) <= 15) {
			yaw += turn ? 5 : -5;
			motY = turn ? -0.015 : 0.015;
		} else {
			yaw += turn ? 10 : -10;
			motY = turn ? -0.015 : 0.015;
		}
		Entity e = this;
		double d3 = e.x - 10;
		if (d3 < -180.0D) {
			e.x += 360.0F;
		}
		if (d3 >= 180.0D) {
			e.x -= 360.0F;
		}
		o((yaw % 360.0F));
		tick += turn ? -1 : 1;
		super.a(EnumMoveType.b, new Vec3D(0, motY, 0));
		super.a(yaw, dt());
	}

}