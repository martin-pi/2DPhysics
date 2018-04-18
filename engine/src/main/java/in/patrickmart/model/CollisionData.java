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
		//get velocities along the mtv
		double v2 = second.getVelocity().dot(mtv.copy().normalize().mult(-1));
		double v1 = first.getVelocity().dot(mtv.copy().normalize()) + v2;	//add v2 to combine reference frames
		//calculate massConstants
		double massCons1 = (first.getMass() - second.getMass()) / (first.getMass() + second.getMass());
		double massCons2 = (2 * first.getMass()) / (second.getMass() + first.getMass());
		//multiply v1 by massconstants to get each entity's velocity
		Vector2D newV1 = mtv.copy().setMag((v1 * massCons1) - v2);
		Vector2D newV2 = mtv.copy().setMag((v1 * massCons2) - v2);
		//conserve the velocity perpendicular to the mtv
		first.setVelocity(mtv.copy().normalize().getPerpendicular().setMag(first.getVelocity().dot(mtv.copy().normalize().getPerpendicular())));
		second.setVelocity(mtv.copy().normalize().getPerpendicular().setMag(second.getVelocity().dot(mtv.copy().normalize().getPerpendicular())));
		//add the new velocity along the mtv
		first.setVelocity(first.getVelocity().add(newV1));
		second.setVelocity(second.getVelocity().sub(newV2));
		//calculate and apply the impulse for each entity
		double p1 = newV1.mag() * first.getMass();
		double p2 = newV2.mag() * second.getMass();
		new ForceGeneric(second, first, mtv.copy().setMag((p1 + p2) * .0166), second.getPosition());
		new ForceGeneric(first, second, mtv.copy().setMag((p1 + p2) * .0166),first.getPosition());


		//call each Entity's collision response
		first.collisionResponse(second, mtv.copy().mult(-0.5));
		second.collisionResponse(first, mtv.copy().mult(0.5));
		
		if(gravity){ //
			//apply the normal force while colliding
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
