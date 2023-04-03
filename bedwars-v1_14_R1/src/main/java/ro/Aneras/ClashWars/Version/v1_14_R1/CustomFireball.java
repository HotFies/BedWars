package ro.Aneras.ClashWars.Version.v1_14_R1;

import net.minecraft.server.v1_14_R1.EntityLargeFireball;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.MathHelper;
import net.minecraft.server.v1_14_R1.World;

public class CustomFireball extends EntityLargeFireball {

	public CustomFireball(World world, EntityLiving shooter, double x, double y, double z) {
		super(world, shooter, x, y, z);
	}
	
	@Override
	public void setDirection(double d0, double d1, double d2) {
		double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
		
		this.dirX = d0 / d3 * 0.1D;
		this.dirY = d1 / d3 * 0.1D;
		this.dirZ = d2 / d3 * 0.1D;
	}

}
