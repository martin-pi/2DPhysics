package in.patrickmart.model;

/**
 *	@author Patrick Martin
 *	@version 0.1
 *	Vector2D implements 2 dimensional vectors, and many of the mathematical functions using these vectors.
 */
public class Vector2D {
    private double x;
    private double y;

    /**
     *	Class Constructor.
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     *	Class Constructor for a vector of components (0, 0).
     */
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    /**
     *	Converts a Vector2D into an easily readable string, similar to "(2.456, -7.543)".
     *	@return this vector represented as a String.
     */
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    /**
     *	Sets the x and y components of this vector to some new values.
     *	@param x	The new x component of this vector.
     *	@param y	The new y component of this vector.
     *	@return 	This vector with its new x and y values.
     */
    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     *	Sets the x component of this vector to some new value.
     *	@param x 	The new x component of this vector.
     *	@return 	This vector with its new x value.
     */
    public Vector2D setX(double x) {
        this.x = x;
        return this;
    }

    /**
     *	Returns the x component of this vector.
     *	@return This vector's x value.
     */
    public double getX() {
        return this.x;
    }

    /**
     *	Sets the y component of this vector to some new value.
     *	@param y 	The new y component of this vector.
     *	@return 	This vector with its new y value.
     */
    public Vector2D setY(double y) {
        this.y = y;
        return this;
    }

    /**
     *	Returns the y component of this vector.
     *	@return This vector's y value.
     */
    public double getY() {
        return this.y;
    }

    /**
     *	Creates a new vector similar in every way to this one and return it.
     *	@return An exact copy of this vector.
     */
    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    /**
     *	Takes 2 Vector2D objects and adds them together without modifying either.
     *	@param first	The first vector to add.
     *	@param second	The second vector to add.
     *	@return 		A third vector, representing the result of addition between the two provided.
     */
    public static Vector2D addVectors(Vector2D first, Vector2D second) {
        Vector2D sum = first.copy();
        return sum.add(second);
    }

    /**
     *	Adds another vector to this vector.
     *	@param vector	A vector to be added to this one.
     *	@return 		This newly modified vector.
     */
    public Vector2D add(Vector2D vector) {
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    /**
     *	Takes 2 Vector2D objects and subtracts the second from the first.
     *	@param first	A vector.
     *	@param second	A vector to be subtracted from the first vector.
     *	@return 		A third vector, representing the result of subtraction between the two provided.
     */
    public static Vector2D subVectors(Vector2D first, Vector2D second) {
        Vector2D sum = first.copy();
        return sum.sub(second);
    }

    /**
     *	Subtracts another vector from this vector.
     *	@param vector	A vector to be subtracted from this one.
     *	@return 		This newly modified vector.
     */
    public Vector2D sub(Vector2D vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        return this;
    }

    /**
     *	Multiplies the x and y components of this vector by a scalar.
     *	@param scalar	A scalar to multiply by.
     *	@return 		This newly modified vector.
     */
    public Vector2D mult(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    /**
     *	Divides the x and y components of this vector by a scalar.
     *	@param scalar	A scalar to divide by.
     *	@return 		This newly modified vector.
     */
    public Vector2D div(double scalar) {
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }

    /**
     *	Calculates the magnitude of this vector from its x and y components using c = squareRoot(a^2 + b^2)
     *	@return The magnitude of this vector.
     */
    public double mag() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     *	Calculates the squared magnitude of this vector, useful for comparing vector magnitudes due to its efficiency.
     *	@return The square of the magnitude of this vector.
     */
    public double magSq() {
        return Math.pow(this.x, 2) + Math.pow(this.y, 2);
    }

    /**
     *	Calculates the dot product of this vector and another.
     *	@param vector	The vector to project this vector onto.
     *	@return 		The length of the calculated projection / dot product.
     */
    public double dot(Vector2D vector) {
        return this.x * vector.x + this.y * vector.y;
    }

    /**
     *	Calculates the "z" component of a cross product and returns it as a scalar.
     *	x and y components of a 2d cross product are 0, so we will just call z a scalar.
     *	@param vector	The vector to cross with this one.
     *	@return 		The length of the calculated z component of the cross product.
     */
    public double cross(Vector2D vector) {
        return (this.x * vector.y - this.y * vector.x);
    }

    /**
     *	Calculates the distance between this vector and another, assuming they are based from the same point.
     *	@param vector	The vector to calculate distance to.
     *	@return 		The distance between the end of this vector and the end of the other vector.
     */
    public double dist(Vector2D vector) {
        return Math.sqrt(Math.pow(Math.abs(this.x - vector.x), 2) + Math.pow(Math.abs(this.y - vector.y), 2));
    }

    /**
     *	Turns this vector into a normal vector, maintaining its angle while setting its length to 1.
     *	@return This newly modified vector.
     */
    public Vector2D normalize() {
        this.setMag(1);
        return this;
    }

    /**
     *	If the vector's magnitude is larger than the provided number, sets the magnitude to that number.
     *	@param max	The maximum magnitude of this vector.
     *	@return		This newly modified vector.
     */
    public Vector2D limit(double max) {
        if (this.magSq() > Math.pow(max, 2)) {
            this.setMag(max);
        }
        return this;
    }

    /**
     *	Sets this vectors magnitude to a specific value.
     *	@param mag	The new magnitude of this vector.
     *	@return		This newly modified vector.
     */
    public Vector2D setMag(double mag) {
        double multiplier = mag / this.mag();
        this.x = this.x * multiplier;
        this.y = this.y * multiplier;
        return this;
    }

    /**
     *	Calculates the angle of this vector.
     *	@return This vector's heading, represented in the current AngleMode.
     */
    public double heading() {
        double angle = Math.atan2(this.y, this.x);
        return angle;
    }

    /**
     *	Rotates this vector by some angle.
     *	@param angle	The angle to rotate this vector by.
     *	@return			This newly modified vector.
     */
    public Vector2D rotate(double angle) {
        double rot = this.heading() + angle;
        double mag = this.mag();
        this.x = Math.cos(rot) * mag;
        this.y = Math.sin(rot) * mag;
        return this;
    }

    /**
     * Returns a vector which is perpendicular to this one.
     * @return a Vector2D object which is perpendicular to this one.
     */
    public Vector2D getPerpendicular() {
        // Get the perpendicular vector by swapping x and y, then negating either one of them.
        return new Vector2D(this.y, -this.x);
    }

    /**
     * Returns a unit vector which is perpendicular or 'normal' to this one.
     * @return a Vector2D object which is normal to this one and has a length of one.
     */
    public Vector2D getNormal() {
        // Get the normal vector by swapping x and y, then negating either one of them, then normalizing.
        return new Vector2D(this.y, -this.x).normalize();
    }

    /**
     *	Calculates the angle between this vector and another vector.
     *	@return The angle between this vector and another vector.
     */
    public double angleBetween(Vector2D vector) {
        return vector.heading() - this.heading();
    }

    /**
     *	Returns this vector represented as an array.
     *	@return The x and y components of this vector as an array.
     */
    public double[] array() {
        double[] arr = {this.x, this.y};
        return arr;
    }

    /**
     *	Checks whether this vector is equal to another.
     *	@return True if the provided vector has the same components as this one.
     */
    public boolean equals(Vector2D vector) {
        return this.x == vector.x && this.y == vector.y;
    }

    /**
     *	Sets this vector to a normal vector, rotated to some angle.
     *	@param angle	The angle that this vector will point toward.
     *	@return 		This newly modified vector.
     */
    public Vector2D fromAngle(double angle) {
        this.x = Math.cos(angle);
        this.y = Math.sin(angle);
        return this;
    }

    /**
     *	Sets this vector to a random vector with x and y components between -1 and 1.
     *	@return This newly modified vector.
     */
    public Vector2D random() {
        this.x = (Math.random() * 2) - 1;
        this.y = (Math.random() * 2) - 1;
        return this;
    }

    /**
     *	Converts an angle represented by radians to degrees.
     *	@param rad	The radian value of an angle.
     *	@return	The provided value represented as degrees.
     */
    public static double radiansToDegrees(double rad) {
        //aDeg = aRad * (180 / PI) --> 180 / PI = 57.2957795131
        return rad * 57.2957795131;
    }

    /**
     *	Converts an angle represented by radians to revolutions.
     *	@param rad	The radian value of an angle.
     *	@return	The provided value represented as revolutions.
     */
    public static double radiansToRevolutions(double rad) {
        //aRev = aRad / (2 * PI) --> 2 PI = 6.28318530718
        return rad / 6.28318530718;
    }

    /**
     *	Converts an angle represented by revolutions to degrees.
     *	@param rev	The revolutionary value of an angle.
     *	@return	The provided value represented as degrees.
     */
    public static double revolutionsToDegrees(double rev) {
        //aDeg = aRev * 360, nice and clean.
        return rev * 360;
    }

    /**
     *	Converts an angle represented by revolutions to radians.
     *	@param rev	The revolutionary value of an angle.
     *	@return	The provided value represented as radians.
     */
    public static double revolutionsToRadians(double rev) {
        //aRad = aRev * (2 * PI) --> 2 PI = 6.28318530718
        return rev * 6.28318530718;
    }

    /**
     *	Converts an angle represented by degrees to radians.
     *	@param deg	The value of an angle as degrees.
     *	@return	The provided value represented as radians.
     */
    public static double degreesToRadians(double deg) {
        //aRad = aDeg * (PI / 180) --> PI / 180 = 0.01745329251
        return deg * 0.01745329251;
    }

    /**
     *	Converts an angle represented by degrees to revolutions.
     *	@param deg	The value of an angle as degrees.
     *	@return	The provided value represented as revolutions.
     */
    public static double degreesToRevolutions(double deg) {
        //aRev = aDeg / 360, nice and clean.
        return deg / 360;
    }
}
