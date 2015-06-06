import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import element.*;
import element.def.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Flávio on 06/06/2015.
 */
public class Track implements CanoeObserver {
    private static final float UPDATE_STEP = 1f / 120f;
    private World world;
    private float marginWidth;
    private boolean finished;
    private long timeRecord;
    private boolean running;

    private GameElementBodyFactory factory;
    private Canoe canoe;
    private River river;

    private List<Rock> rocks;
    private List<SandBank> sandBanks;

    private float lag = 0.0f;

    public Track(TrackConfig cfg) {
        world = new World(new Vector2(0, cfg.flow), false);
        this.marginWidth = cfg.margin;

        factory = new GameElementBodyFactory(world);

        RiverDef riverDef = new RiverDef(cfg.width, cfg.length);
        river = new River(factory.build(riverDef), riverDef.getWidth(), riverDef.getHeight());
        river.getBody().getPosition().set(0f, river.getHeight() / 2);


        CanoeDef canoeDef = new CanoeDef();
        canoe = new Canoe(factory.build(canoeDef), canoeDef.getWidth(), canoeDef.getHeight());
        canoe.getBody().getPosition().set(0f, canoe.getHeight() / 2);
        canoe.addObserver(this);

        rocks = new LinkedList<Rock>();
        sandBanks = new LinkedList<SandBank>();
    }

    public void addRock(float xpos, float ypos) {
        RockDef rockDef = new RockDef();

        if (!((-river.getWidth() / 2 < xpos && xpos < river.getWidth() / 2) &&
            (canoe.getHeight() < ypos && ypos < river.getHeight())))
            return;

        Rock rock = new Rock(factory.build(rockDef), rockDef.getWidth(), rockDef.getHeight());
        rock.getBody().getPosition().set(xpos, ypos);
        rocks.add(rock);
    }

    public void addSandBank(float xpos, float ypos, float width, float length) {

    }

    public void start() {
        canoe.getBody().getPosition().set(0f, canoe.getHeight() / 2);
        finished = false;
        running = true;

        lag = 0.0f;
    }

    private void update(float deltaTime) {
        if (running) {
            if (!finished) {
                lag += deltaTime;

                while (lag >= UPDATE_STEP) {
                    world.step(UPDATE_STEP, 2, 3);
                    lag -= UPDATE_STEP;
                }
            }
        }
    }

    @Override
    public void onRow(float leftAngle, float rightAngle) {
        if (leftAngle > 360)
            applyImpulseLeft();
        if (rightAngle > 360)
            applyImpulseRight();
    }

    private void applyImpulseLeft() {
        canoe.getBody().applyLinearImpulse(canoe.getBody().getWorldVector(new Vector2(0.0f, 3.0f)), canoe.getBody().getWorldPoint(new Vector2(-0.5f, 0.0f)), true);
    }


    private void applyImpulseRight() {
        canoe.getBody().applyLinearImpulse(canoe.getBody().getWorldVector(new Vector2(0.0f, 3.0f)), canoe.getBody().getWorldPoint(new Vector2(0.5f, 0.0f)), true);
    }

    public float getMarginWidth() {
        return marginWidth;
    }

    public void setMarginWidth(float marginWidth) {
        this.marginWidth = marginWidth;
    }

    public long getTimeRecord() {
        return timeRecord;
    }

    public void setTimeRecord(long timeRecord) {
        this.timeRecord = timeRecord;
    }

    public Canoe getCanoe() {
        return canoe;
    }

    public void setCanoe(Canoe canoe) {
        this.canoe = canoe;
    }

    public River getRiver() {
        return river;
    }

    public void setRiver(River river) {
        this.river = river;
    }

    public List<Rock> getRocks() {
        return rocks;
    }

    public void setRocks(List<Rock> rocks) {
        this.rocks = rocks;
    }

    public List<SandBank> getSandBanks() {
        return sandBanks;
    }

    public void setSandBanks(List<SandBank> sandBanks) {
        this.sandBanks = sandBanks;
    }
}
