package in.patrickmart.model;

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
		//TODO We need to know which entity to apply the negative translation to. This means we need to know where the axis came from.
		first.collisionResponse(mtv.copy().mult(-0.5));
		second.collisionResponse(mtv.copy().mult(0.5));
		//TODO Apply a normal force to each entity.
		return true;
	}
}
