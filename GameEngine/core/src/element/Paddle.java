package element;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a paddle.
 */
public class Paddle {
    private float angle;
    private float angularVelocity;
    private float angularDamping;
    public final static float ANGULAR_VELOCITY_THRESHOLD = 0.1f;
    public final static float MAX_ANGULAR_VELOCITY = 360f;

    public Paddle(float angularDamping) {
        angle = 0;
        angularVelocity = 0;
        this.angularDamping = (float) Math.max(angularDamping, 1.0);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        if (angularVelocity < ANGULAR_VELOCITY_THRESHOLD)
            this.angularVelocity = 0f;
        else if (angularVelocity > MAX_ANGULAR_VELOCITY)
            this.angularVelocity = MAX_ANGULAR_VELOCITY;
        else
            this.angularVelocity = angularVelocity;
    }


    public void update(float timeStep) {
        angle += angularVelocity * timeStep;
        angularVelocity *= (1 - angularDamping * timeStep);
        if (angularVelocity < ANGULAR_VELOCITY_THRESHOLD)
            angularVelocity = 0;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }
}