package element;

/**
 * Created by Flávio on 06/06/2015.
 */
public interface CanoeObserver {
    void onRow(float leftAngle, float leftVelocity, float rightAngle, float rightVelocity);
}
