package ro.Aneras.ClashWars.Version.v1_18_R1;


import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.projectile.EntityLargeFireball;
import net.minecraft.world.level.World;

public class CustomFireball extends EntityLargeFireball {

	public CustomFireball(World world, EntityLiving shooter, double x, double y, double z) {
		super(world, shooter, x, y, z, 1);
		double d6 = Math.sqrt(x * x + y * y + z * z);
		this.b = x / d6 * 0.1D;
		this.c = y / d6 * 0.1D;
		this.d = z / d6 * 0.1D;
	}
	
	@Override
	public void setDirection(double d3, double d4, double d5) {
	}

}
