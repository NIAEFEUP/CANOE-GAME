package element;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Flávio on 06/06/2015.
 */
public class Canoe extends GameElement {
    private static final float ANGULAR_IMPULSE = 50f;
    private static final float ANGULAR_DAMPING = 0.2f;
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private List<CanoeObserver> observers;

    public Canoe(Body body, float width, float height) {
        super(body, Canoe.class.getSimpleName(), width, height);
        leftPaddle = new Paddle(ANGULAR_DAMPING);
        rightPaddle = new Paddle(ANGULAR_DAMPING);
        observers = new LinkedList<CanoeObserver>();
    }

    public void rowLeft() {
        leftPaddle.setAngularVelocity(leftPaddle.getAngularVelocity() + ANGULAR_IMPULSE);
    }

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
            observer.onRow(leftPaddle.getAngle(), rightPaddle.getAngle());
        leftPaddle.setAngle(leftPaddle.getAngle() % 360f);
        rightPaddle.setAngle(rightPaddle.getAngle() % 360f);
    }
}
