package in.patrickmart.model;

import in.patrickmart.model.forces.ForceFEA;
import in.patrickmart.model.forces.ForceGeneric;

public class CollisionData {
    Entity first;
    Entity second;
	Vector2D mtv;
	
	public CollisionData(Entity first, Entity second, Vector2D mtv) {
		this.first = first;
		this.second = second;
		this.mtv = mtv;
	}
	
	public boolean equals(CollisionData other) {
		return (this.first.equals(other.first) || this.first.equals(other.second))
			&& (this.second.equals(other.second) || this.second.equals(other.first));
	}
	
	public boolean resolve() {
		// Apply a negative translation to one object, and a positive translation to the other. Split the overlap.

		Vector2D firstDir = first.getVelocity().add(mtv.copy().mult(-.5).normalize());
		Vector2D secondDir = new Vector2D(firstDir.getY(), firstDir.getX());
		double vfx1 = (((first.getVelocity().getX())*(first.getMass() - second.getMass())) +(2 * second.getMass() * second.getVelocity().getX()))/(first.getMass() + second.getMass());
		double vfy1 = (((first.getVelocity().getY())*(first.getMass() - second.getMass())) +(2 * second.getMass() * second.getVelocity().getY()))/(first.getMass() + second.getMass());
		double vfx2 = (((second.getVelocity().getX())*(second.getMass() - first.getMass())) +(2 * first.getMass() * first.getVelocity().getX()))/(first.getMass() + second.getMass());
		double vfy2 = (((second.getVelocity().getY())*(second.getMass() - first.getMass())) +(2 * first.getMass() * first.getVelocity().getY()))/(first.getMass() + second.getMass());
		Vector2D newFirst = new Vector2D(vfx1, vfy1);
		Vector2D newSecond = new Vector2D(vfx2, vfy2);
		first.setVelocity(firstDir.setMag(firstDir.dot(newFirst)));
		second.setVelocity(secondDir.setMag(secondDir.dot(newSecond)));
		System.out.println(first.getVelocity());
		first.collisionResponse(second, mtv.copy().mult(-0.5));
		second.collisionResponse(first, mtv.copy().mult(0.5));
		//TODO Apply a normal force to each entity. These are not correct. This collision needs a location.
		//Calculate the normal force on the first object.
		double firstNormal = first.getNetForce().dot(mtv);
		double secondNormal = second.getNetForce().dot(mtv);

		//new ForceGeneric(second, first, mtv.copy().setMag(firstNormal), first.getPosition());
		//new ForceGeneric(first, second, mtv.copy().setMag(secondNormal).mult(-1), second.getPosition());
		return true;
	}
}
