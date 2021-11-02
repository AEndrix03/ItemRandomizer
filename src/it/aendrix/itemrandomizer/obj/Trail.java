package it.aendrix.itemrandomizer.obj;

import it.aendrix.itemrandomizer.main.Main;
import it.aendrix.itemrandomizer.main.utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Trail {

    private Particle particle;
    private float speed;
    private double rangeY;
    private double rangeRotation;
    private Location start;
    private Location destination;
    private Location position;
    private ItemStack item;

    public Trail(Particle particle) {
        this.particle = particle;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    protected void start() {
        /*
        La traiettoria della scia è divisa in 6 fasi.
        Calcolo la distanza fra la partenza e la destinazione e la divido per 6.
        Ogni tot tempo controllo in che fase si trova la scia e ne determino la funzione.

        */

        new BukkitRunnable() {

            final double spawnDistance = utils.locationDistance(start,destination);
            final double stateCostant = spawnDistance/6;
            final double angleCoefficientDistance = (destination.getZ()-start.getZ())/(destination.getX()-start.getX());
            final boolean positiveX = start.getX()<destination.getX();
            final boolean positiveZ = start.getZ()<destination.getZ();
            final int stateRepetition = (int) Math.ceil(stateCostant/speed); //Increments in a state
            final int angleIncrementFirstState = ((int)(Math.toRadians(120)))/stateRepetition; //Increments of the angle in first state
            int angleFirstState = 0; //Angle in the first state
            double yDegreeSecondState = 0;
            boolean finishSecondState = false;
            double angleThirdState = 450;
            final int angleIncrementThirdState = 180/stateRepetition; //Increments of the angle in first state
            boolean thirdState = false, fourthState = false, fivethState = false, sixthState = false;
            final double distanceMaxFourthState = stateCostant * 2 / 3;
            final int stateFourRepetition = (int) Math.ceil(distanceMaxFourthState/speed);
            int repetitionFourState = 0;

            @Override
            public void run() {

                position.getWorld().spawnParticle(particle, position.getX(), position.getY(), position.getZ(), 0);

                if (position.getBlock().getType() != Material.AIR) {
                    System.out.println(position.getBlock().getType());
                    cancel();
                    return;
                }

                double distance = utils.locationDistance(position,start);

                if (distance < stateCostant && !finishSecondState) { //Prima fase
                    //Utilizzo il rangeY e incremento Y con un sin.
                    double Y = rangeY*Math.sin(Math.toRadians(angleFirstState));
                    angleFirstState+=angleIncrementFirstState;

                    position.setY(position.getY()+Y);

                    //Trovo X utilizzando il coseno dell'arcotangente del coefficiente angolare della retta della distanza
                    double X = Math.cos(Math.atan(angleCoefficientDistance));

                    //Ricavo Z
                    double Z = Math.sin(Math.atan(angleCoefficientDistance));

                    //Moltiplico il modulo della distanza attuale con i componenti X e Z e ne inserisco il segno
                    position.setX((position.getX())+(utils.booleanToSign(positiveX) * X * (distance+speed)));
                    position.setZ((position.getZ())+(utils.booleanToSign(positiveZ) * Z * (distance+speed)));

                    distance = utils.locationDistance(position,start);

                    //Si applica solo nell'ultimo istante della prima fase. Setta l'incrementazione per il secondo stato
                    if (distance > stateCostant)
                        yDegreeSecondState = Math.abs(position.getY()-destination.getY())/(stateRepetition*5);
                } else if (distance < stateCostant*2 && !finishSecondState) { //Seconda fase
                    //Decremento Y con una costante definita nella fase precedente
                    position.setY(position.getY()-yDegreeSecondState);

                    //Trovo X utilizzando il coseno dell'arcotangente del coefficiente angolare della retta della distanza
                    double X = Math.cos(Math.atan(angleCoefficientDistance));

                    //Ricavo Z
                    double Z = Math.sin(Math.atan(angleCoefficientDistance));

                    //Moltiplico il modulo della distanza attuale con i componenti X e Z e ne inserisco il segno
                    position.setX((position.getX())+(utils.booleanToSign(positiveX) * X * (distance+speed)));
                    position.setZ((position.getZ())+(utils.booleanToSign(positiveZ) * Z * (distance+speed)));

                    distance = utils.locationDistance(position,start);

                    if (distance > stateCostant*2)
                        finishSecondState = true;
                } else if (thirdState) { //Terza fase Fase rotatoria
                    position.setY(position.getY()-yDegreeSecondState);

                    double X = rangeRotation * Math.cos(Math.toRadians(angleThirdState));
                    double Z = rangeRotation * Math.sin(Math.toRadians(angleThirdState));

                    position.setX((position.getX())+(utils.booleanToSign(positiveX)*X));
                    position.setZ((position.getZ())+(utils.booleanToSign(positiveZ)*Z));

                    angleThirdState -= angleIncrementThirdState;

                    if (angleThirdState<=270) {
                        thirdState = false;
                        fourthState = true;
                    }
                } else if (fourthState) { //Quarta fase
                    position.setY(position.getY()-yDegreeSecondState);

                    //Trovo X utilizzando il coseno dell'arcotangente del coefficiente angolare della retta della distanza
                    double X = Math.cos(Math.atan(angleCoefficientDistance));

                    //Ricavo Z
                    double Z = Math.sin(Math.atan(angleCoefficientDistance));

                    //Moltiplico il modulo della distanza attuale con i componenti X e Z e ne inserisco il segno
                    position.setX((position.getX())+(utils.booleanToSign(positiveX) * -1 * X * (distance+speed)));
                    position.setZ((position.getZ())+(utils.booleanToSign(positiveZ) * -1 * Z * (distance+speed)));

                    repetitionFourState++;

                    if (repetitionFourState>=stateFourRepetition) {
                        fourthState = false;
                        fivethState = true;
                        angleThirdState = 90; //Serve nella fase successiva
                    }
                } else if (fivethState) { //Quinta fase Fase rotatoria
                    position.setY(position.getY()-yDegreeSecondState);

                    double X = rangeRotation * Math.cos(Math.toRadians(angleThirdState));
                    double Z = rangeRotation * Math.sin(Math.toRadians(angleThirdState));

                    position.setX((position.getX())+(utils.booleanToSign(positiveX)*X));
                    position.setZ((position.getZ())+(utils.booleanToSign(positiveZ)*Z));

                    angleThirdState += angleIncrementThirdState;

                    if (angleThirdState>=270) {
                        fivethState = false;
                        sixthState = true;
                    }
                } else { //Sesta fase
                    position.setY(position.getY()-yDegreeSecondState);

                    //Trovo X utilizzando il coseno dell'arcotangente del coefficiente angolare della retta della distanza
                    double X = Math.cos(Math.atan(angleCoefficientDistance));

                    //Ricavo Z
                    double Z = Math.sin(Math.atan(angleCoefficientDistance));

                    //Moltiplico il modulo della distanza attuale con i componenti X e Z e ne inserisco il segno
                    position.setX((position.getX())+(utils.booleanToSign(positiveX) * X * (distance+speed)));
                    position.setZ((position.getZ())+(utils.booleanToSign(positiveZ) * Z * (distance+speed)));
                }
            }

        }.runTaskTimer(Main.getInstance(), 2L, 2L);
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getPosition() {
        return position;
    }

    public void setPosition(Location position) {
        this.position = position;
    }

    public double getRangeY() {
        return rangeY;
    }

    public void setRangeY(double rangeY) {
        this.rangeY = rangeY;
    }

    public double getRangeRotation() {
        return rangeRotation;
    }

    public void setRangeRotation(double rangeRotation) {
        this.rangeRotation = rangeRotation;
    }
}
