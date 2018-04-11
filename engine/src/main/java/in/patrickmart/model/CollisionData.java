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
	
	public boolean resolve(boolean gravity, boolean FEAgravity) {
		//calculate momentum of each entity
		Vector2D m1 = first.getVelocity().mult(first.getMass());
		Vector2D m2 = second.getVelocity().mult(second.getMass());
		//project the force onto the vector between the two entities.
		double newM1 = m1.dot(second.getPosition().sub(first.getPosition()).normalize());
		double newM2 = m2.dot(first.getPosition().sub(second.getPosition()).normalize());
		//apply the change in velocity equally to both in opposite directions
		first.setVelocity(first.getVelocity().add(mtv.copy().mult(-1).setMag((newM1 + newM2)/first.getMass())));
		second.setVelocity(second.getVelocity().add(mtv.copy().setMag((newM1 + newM2)/second.getMass())));

		//call each entitie's collision response
		first.collisionResponse(second, mtv.copy().mult(-0.5));
		second.collisionResponse(first, mtv.copy().mult(0.5));
		
		if(gravity){
			//apply the same force in the opposite direction
			new ForceNormal(first,second);
			new ForceNormal(second,first);
		}
		if(FEAgravity){
			//apply gravity opposite and projected onto the mtv.
			new ForceFEA(first).getForce().mult(-1).dot(second.getPosition().sub(first.getPosition()).normalize());
			new ForceFEA(second).getForce().mult(-1).dot(first.getPosition().sub(second.getPosition()).normalize());
		}

		return true;
	}
}
