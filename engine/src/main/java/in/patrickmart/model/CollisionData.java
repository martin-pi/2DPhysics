package in.patrickmart.model;

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
		first.collisionResponse(second, mtv.copy().mult(-0.5));
		second.collisionResponse(first, mtv.copy().mult(0.5));
		//TODO Apply a normal force to each entity. These are not correct. This collision needs a location.
		//first.applyForce(new ForceGeneric(second,first, mtv.copy(), first.getPosition()));
		//second.applyForce(new ForceGeneric(first,second, mtv.copy().mult(-1), second.getPosition()));
		return true;
	}
}
