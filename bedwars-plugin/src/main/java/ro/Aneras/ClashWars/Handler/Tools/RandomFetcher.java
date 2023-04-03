package ro.Aneras.ClashWars.Handler.Tools;

import java.security.SecureRandom;
import java.util.Random;

public class RandomFetcher {

	private static String text = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVW";
	private static SecureRandom r = new SecureRandom();

	public static Random getRandom() {
		return r;
	}
	
	public static int getRandom(int i) {
		return r.nextInt(i);
	}
	
	public static float getProcentage() {
		return r.nextFloat();
	}
	
	public static double getRandomDouble() {
		return r.nextDouble();
	}
	
	public static int randomRange(int start, int end) {
		return start + r.nextInt(end - start + 1);
	}
	
	public static double randomRange(double start, double end) {
		return start + (end - start) * r.nextDouble();
	}
	
	public static String randomString(int len){
		StringBuilder sb = new StringBuilder(len);
		for(int i = 0; i < len; i++) {
			sb.append(text.charAt(r.nextInt(text.length())));
		}
		return sb.toString();
	}
	
}
