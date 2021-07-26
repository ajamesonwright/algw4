import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class BouncingBalls {

    private class Ball
    {
        private final double radius = 0.1;
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
            if ((xPos + xVel * dt < radius) || (xPos + xVel * dt > 1.0 - radius))
            {
                xVel = -xVel;
            }
            if ((yPos + yVel * dt < radius) || (yPos + yVel * dt > 1.0 - radius))
            {
                yVel = -yVel;
            }
            xPos += xVel * dt;
            yPos += yVel * dt;
        }

        public void draw()
        {
            StdDraw.filledCircle(xPos, yPos, radius);
        }
    }

    public static void main(String[] args)
    {
        BouncingBalls bb = new BouncingBalls();

        //int N = Integer.parseInt(args[0]);
        int N = 25;
        Ball[] balls = new Ball[N];
        for (int i = 0; i < N; i++)
        {
            balls[i] = bb.new Ball();
        }
        

        StdDraw.setCanvasSize();

        while (true)
        {
            StdDraw.clear();
            for (int i = 0; i < N; i++)
            {
                balls[i].move(0.5);
                balls[i].draw();
            }
            StdDraw.show(50);
        }
    }


}
