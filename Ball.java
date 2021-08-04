import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;

public class Ball {
    private static final double RADIUS = 1;
    private double xPos, yPos;
    private double xVel, yVel;

    public Ball()
    {
        xPos = StdRandom.uniform(0, 800);
        yPos = StdRandom.uniform(0, 800);
        xVel = (double) StdRandom.uniform(-1, 1);
        yVel = (double) StdRandom.uniform(-1, 1);
    }

    public void move(double dt)
    {
        if ((xPos + xVel * dt < RADIUS) || (xPos + xVel * dt > 1.0 - RADIUS))
        {
            xVel = -xVel;
        }
        if ((yPos + yVel * dt < RADIUS) || (yPos + yVel * dt > 1.0 - RADIUS))
        {
            yVel = -yVel;
        }
        xPos += xVel * dt;
        yPos += yVel * dt;
    }

    public void draw()
    {
        StdDraw.filledCircle(xPos, yPos, RADIUS);
    }
}
