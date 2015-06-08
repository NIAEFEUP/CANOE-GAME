package element;

/**
 * Canoes Observer interface.
 */
public interface CanoeObserver {
    void onRow(float leftAngle, float leftVelocity, float rightAngle, float rightVelocity);
}
