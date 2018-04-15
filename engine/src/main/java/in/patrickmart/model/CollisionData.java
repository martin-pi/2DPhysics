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
		//Current "Stable" implementation
		Vector2D m1 = first.getVelocity().mult(first.getMass());
		Vector2D m2 = second.getVelocity().mult(second.getMass());
		//project the force onto the vector between the two entities.
		double newM1 = m1.dot(second.getPosition().sub(first.getPosition()).normalize());
		double newM2 = m2.dot(first.getPosition().sub(second.getPosition()).normalize());
		//apply the change in velocity equally to both in opposite directions
		first.setVelocity(first.getVelocity().add(mtv.copy().setMag(-(newM1 + newM2)/first.getMass())));
		second.setVelocity(second.getVelocity().add(mtv.copy().setMag((newM1 + newM2)/second.getMass())));
		new ForceGeneric(second, first, mtv.copy().setMag((newM1 + newM2)* .0166), second.getPosition());
		new ForceGeneric(first, second, mtv.copy().setMag((newM2 + newM1) * .0166), first.getPosition());
		/* Other collision implementation
		double v2 = second.getVelocity().dot(mtv.copy().normalize().mult(-1));
		double v1 = first.getVelocity().dot(mtv.copy().normalize()) + v2;
		double massCons1 = (first.getMass() - second.getMass()) / (first.getMass() + second.getMass());
		double massCons2 = (2 * first.getMass()) / (second.getMass() + first.getMass());
		Vector2D newV1 = mtv.copy().setMag((v1 * massCons1) - v2);
		Vector2D newV2 = mtv.copy().setMag((v1 * massCons2) - v2);
		first.setVelocity(mtv.copy().normalize().getPerpendicular().setMag(first.getVelocity().dot(mtv.copy().normalize().getPerpendicular())));
		second.setVelocity(mtv.copy().normalize().getPerpendicular().setMag(second.getVelocity().dot(mtv.copy().normalize().getPerpendicular())));
		first.setVelocity(first.getVelocity().add(newV1));
		second.setVelocity(second.getVelocity().sub(newV2));
		*/

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
