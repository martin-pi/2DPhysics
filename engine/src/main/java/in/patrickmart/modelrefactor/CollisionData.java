package in.patrickmart.modelrefactor;

import in.patrickmart.model.Entity;

public class CollisionData {
    Entity first;
    Entity second;
    //TODO What do we need to know about this collision in order to resolve it?

	public CollisionData(Entity first, Entity second) {
		this.first = first;
		this.second = second;
	}
	
	public boolean equals(CollisionData other) {
		return (this.first.equals(other.first) || this.first.equals(other.second))
			&& (this.second.equals(other.second) || this.second.equals(other.first));
	}
	
	public boolean resolve() {
		first.collisionResponse();
		second.collisionResponse();
		return true;
	}
}
