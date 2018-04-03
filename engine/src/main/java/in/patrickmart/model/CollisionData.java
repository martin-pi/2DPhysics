package in.patrickmart.model;

import in.patrickmart.model.forces.*;
import in.patrickmart.model.forces.ForceGeneric;
import org.lwjgl.system.CallbackI;

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
		//calculate momentum of each entity
		Vector2D m1 = first.getVelocity().mult(first.getMass());
		Vector2D m2 = second.getVelocity().mult(second.getMass());
		//project the force onto the vector between the two entities.
		double newM1 = m1.dot(second.getPosition().sub(first.getPosition()).normalize());
		double newM2 = m2.dot(first.getPosition().sub(second.getPosition()).normalize());
		//apply the force equally to both in opposite directions
		new ForceGeneric(second, first, mtv.copy().setMag(newM1 + newM2).mult(-1), second.getPosition());
		new ForceGeneric(first, second, mtv.copy().setMag(newM2 + newM1), first.getPosition());

		//call each entitie's collision response
		first.collisionResponse(second, mtv.copy().mult(-0.5));
		second.collisionResponse(first, mtv.copy().mult(0.5));

		//TODO Apply a normal force to each entity.

		return true;
	}
}
