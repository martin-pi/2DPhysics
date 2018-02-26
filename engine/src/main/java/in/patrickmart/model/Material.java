package in.patrickmart.model;

public enum Material {
    METAL(0, 7000,1, new double[] {0.15, 0.24, 0.23}),
    WOOD(1, 720,1,  new double[] {0.80, 0.59, 0.34}),
    RUBBER(2, 1100,1,  new double[] {0.80, 0.34, 0.45}),
    ICE(3, 917,1, new double[] {0.54, 0.85, 0.79}),
    CONCRETE(4, 2000,1, new double[] {0.51, 0.51, 0.51}),
    LEATHER(5, 900,1, new double[] {0.78, 0.35, 0.25});

    private int id;
    private double density;
    private double buoyancy;
    private double[] color;

    Material(int id, double density, double buoyancy, double[] color) {
        this.id = id;
        this.density = density;
        this.buoyancy = buoyancy;
        this.color = color;
    }

    public double getDensity() {
        return this.density;
    }

    public double getFrictionCoefficient() {
        //TODO implement friction coefficient lookup.
        return 0.1;
    }

    public double getBuoyancy() {
        return buoyancy;
    }

    public double[] getColor() {
        return new double[] {color[0], color[1], color[2], 1.0};
    }
}
