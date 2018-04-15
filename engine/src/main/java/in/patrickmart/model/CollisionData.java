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
		Vector2D p1 = first.getVelocity().mult(first.getMass());
		Vector2D p2 = second.getVelocity().mult(second.getMass());
		//project the force onto the vector between the two entities.
		double newM1 = p1.copy().dot(mtv.copy().normalize().mult(-1));
		double newM2 = p2.copy().dot(mtv.copy().normalize());
		p1 = p1.add(mtv.copy().setMag(newM1 + newM2));
		p2 = p2.add(mtv.copy().setMag(newM2 + newM1));
		//apply the change in velocity equally to both in opposite directions
		first.setVelocity(first.getVelocity().add(p1.div(first.getMass())));
		second.setVelocity(second.getVelocity().add(p2.div(second.getMass())));

		//call each entity's collision response
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
