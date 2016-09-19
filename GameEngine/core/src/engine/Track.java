package engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import element.*;
import element.def.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Game's track. Manages the track's logic.
 */
public class Track implements CanoeObserver, ContactListener {
    private static final float UPDATE_STEP = 1f / 120f;
    private World world;
    private float marginWidth;
    private boolean finished;
    private boolean running;

    private GameElementBodyFactory factory;
    private Canoe canoe;
    private River river;
    private FinishLine finishLine;

    private List<Rock> rocks;
    private List<SandBank> sandBanks;
    private int insideSandBank = 0;

    private boolean impulseAppliedLeft;
    private boolean impulseAppliedRight;

    private float lag = 0.0f;

    public Track(TrackConfig cfg) {
        world = new World(new Vector2(0, cfg.flow), false);
        world.setContactListener(this);
        this.marginWidth = cfg.margin;

        factory = new GameElementBodyFactory(world);

        RiverDef riverDef = new RiverDef(cfg.width, cfg.length);
        river = new River(factory.build(riverDef), riverDef.getWidth(), riverDef.getHeight());
        river.getBody().setTransform(0f, river.getHeight() / 2, 0);

        FinishLineDef finishLineDef = new FinishLineDef(cfg.width);
        finishLine = new FinishLine(factory.build(finishLineDef), finishLineDef.getWidth(), finishLineDef.getHeight());
        finishLine.getBody().setTransform(0f, river.getHeight() - finishLine.getHeight() / 2, 0);

        CanoeDef canoeDef = new CanoeDef();
        canoe = new Canoe(factory.build(canoeDef), canoeDef.getWidth(), canoeDef.getHeight());
        canoe.getBody().setTransform(0f, canoe.getHeight() / 2, 0);
        canoe.addObserver(this);

        rocks = new LinkedList<Rock>();
        sandBanks = new LinkedList<SandBank>();
    }


    /**
     * Adds a rock to the track in the given position.
     *
     * @param xpos  Rock's X position.
     * @param ypos  Rock's Y position.
     */
    public void addRock(float xpos, float ypos) {
        if (!running) {
            if (!((-river.getWidth() / 2 < xpos && xpos < river.getWidth() / 2) &&
                    (canoe.getHeight() < ypos && ypos < river.getHeight())))
                return;

            RockDef rockDef = new RockDef();
            Rock rock = new Rock(factory.build(rockDef), rockDef.getWidth(), rockDef.getHeight());
            rock.getBody().setTransform(xpos, ypos, 0);
            rocks.add(rock);
        }
    }

    /**
     * Adds a sandBank to the track in the given positions and with the given size.
     *
     * @param xpos      Sandbank's center X position.
     * @param ypos      Sandbank's center Y position.
     * @param width     Sandbank's width.
     * @param length    Sandbank's length.
     */
    public void addSandBank(float xpos, float ypos, float width, float length) {
        if (!running) {
            if (!(((-river.getWidth() + width) / 2 < xpos && xpos < (river.getWidth() - width) / 2) &&
                    ((canoe.getHeight() + length / 2) < ypos && ypos < (river.getHeight() - length / 2))))
                return;

            SandBankDef sandBankDef = new SandBankDef(width, length);
            SandBank sandBank = new SandBank(factory.build(sandBankDef), sandBankDef.getWidth(), sandBankDef.getHeight());
            sandBank.getBody().setTransform(xpos, ypos, 0);
            sandBanks.add(sandBank);
        }
    }

    /**
     * Starts the game.
     */
    public void start() {
        canoe.getBody().setTransform(0, canoe.getHeight() / 2, 0);
        finished = false;
        running = true;
        insideSandBank = 0;
        impulseAppliedLeft = false;
        impulseAppliedRight = false;

        lag = 0.0f;
    }

    /**
     * Updates the game after a given time.
     * @param deltaTime time passed.
     */
    public void update(float deltaTime) {
        if (running) {
            if (!finished) {
                lag += deltaTime;

                while (lag >= UPDATE_STEP) {
                    world.step(UPDATE_STEP, 2, 3);
                    canoe.update(UPDATE_STEP);
                    lag -= UPDATE_STEP;
                }
            }
        }
    }

    @Override
    public void onRow(float leftAngle, float leftVelocity, float rightAngle, float rightVelocity) {
        if (leftAngle > 90 && !impulseAppliedLeft) {
            applyImpulseLeft();
            impulseAppliedLeft = true;
        }
        else if (leftAngle > 360)
            impulseAppliedLeft = false;

        if (rightAngle > 90 && !impulseAppliedRight){
            applyImpulseRight();
            impulseAppliedRight = true;
        }
        else if (rightAngle > 360)
            impulseAppliedRight = false;
    }

    private void applyImpulseLeft() {
        canoe.getBody().applyLinearImpulse(canoe.getBody().getWorldVector(new Vector2(0.0f, 50.0f)), canoe.getBody().getWorldPoint(new Vector2(-0.5f, 0.0f)), true);
    }


    /**
     * Applies an impulse the the right side of the canoe.
     */
    private void applyImpulseRight() {
        canoe.getBody().applyLinearImpulse(canoe.getBody().getWorldVector(new Vector2(0.0f, 50.0f)), canoe.getBody().getWorldPoint(new Vector2(0.5f, 0.0f)), true);
    }


    public float getMarginWidth() {
        return marginWidth;
    }

    public Canoe getCanoe() {
        return canoe;
    }


    public River getRiver() {
        return river;
    }


    public List<Rock> getRocks() {
        return rocks;
    }


    public List<SandBank> getSandBanks() {
        return sandBanks;
    }


    @Override
    public void beginContact(Contact contact) {
        String collidedElemType;

        if (Canoe.class.getSimpleName().equals(contact.getFixtureA().getBody().getUserData()))
            collidedElemType = (String) contact.getFixtureB().getBody().getUserData();
        else if (Canoe.class.getSimpleName().equals(contact.getFixtureB().getBody().getUserData()))
            collidedElemType = (String) contact.getFixtureA().getBody().getUserData();
        else
            return;

        if (collidedElemType.equals(SandBank.class.getSimpleName())) {
            ++insideSandBank;
            canoe.getBody().setLinearDamping(SandBank.damping);
            canoe.getBody().setAngularDamping(SandBank.damping);
        }
        else if (collidedElemType.equals(FinishLine.class.getSimpleName())) {
            finished = true;
        }
    }

    @Override
    public void endContact(Contact contact) {
        String collidedElemType;

        if (Canoe.class.getSimpleName().equals(contact.getFixtureA().getBody().getUserData()))
            collidedElemType = (String) contact.getFixtureB().getBody().getUserData();
        else if (Canoe.class.getSimpleName().equals(contact.getFixtureB().getBody().getUserData()))
            collidedElemType = (String) contact.getFixtureA().getBody().getUserData();
        else
            return;

        if (collidedElemType.equals(SandBank.class.getSimpleName())) {
            --insideSandBank;
            if (insideSandBank == 0){
                canoe.getBody().setLinearDamping(CanoeDef.damping);
                canoe.getBody().setAngularDamping(CanoeDef.damping);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public FinishLine getFinishLine() {
        return finishLine;
    }

    public boolean isFinished() {
        return finished;
    }
}
