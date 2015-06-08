package element;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents the canoe and its two paddles.
 */
public class Canoe extends GameElement {
    private static final float ANGULAR_IMPULSE = 50f;
    private static final float ANGULAR_DAMPING = 0.1f;
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private List<CanoeObserver> observers;

    public Canoe(Body body, float width, float height) {
        super(body, Canoe.class.getSimpleName(), width, height);
        leftPaddle = new Paddle(ANGULAR_DAMPING);
        rightPaddle = new Paddle(ANGULAR_DAMPING);
        observers = new LinkedList<CanoeObserver>();
    }

    /**
     * Applies an impulse to the left paddle.
     */
    public void rowLeft() {
        leftPaddle.setAngularVelocity(leftPaddle.getAngularVelocity() + ANGULAR_IMPULSE);
    }

    /**
     * Applies an impulse to the right paddle.
     */
    public void rowRight() {
        rightPaddle.setAngularVelocity(rightPaddle.getAngularVelocity() + ANGULAR_IMPULSE);
    }

    public void update(float timeStep) {
        leftPaddle.update(timeStep);
        rightPaddle.update(timeStep);
        notifyObservers();
    }

    public void addObserver(CanoeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CanoeObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (CanoeObserver observer : observers)
            observer.onRow(leftPaddle.getAngle(), leftPaddle.getAngularVelocity(),
                    rightPaddle.getAngle(), rightPaddle.getAngularVelocity());
        leftPaddle.setAngle(leftPaddle.getAngle() % 360f);
        rightPaddle.setAngle(rightPaddle.getAngle() % 360f);
    }

    public Paddle getLeftPaddle() {
        return leftPaddle;
    }

    public void setLeftPaddle(Paddle leftPaddle) {
        this.leftPaddle = leftPaddle;
    }

    public Paddle getRightPaddle() {
        return rightPaddle;
    }

    public void setRightPaddle(Paddle rightPaddle) {
        this.rightPaddle = rightPaddle;
    }
}
