package graphics.object;

import com.jogamp.opengl.GL2;

import java.time.LocalTime;

public class AlarmClockAnimator {

    private LocalTime time;
    private boolean actualTime;
    private boolean slowMotionActive;
    private boolean isPaused;
    private boolean resumingAtNormalRate; // New field

    static final double FAST_FORWARD_SECONDS_PER_FRAME = 1.0;
    static final double SLOW_MOTION_SECONDS_PER_FRAME = 0.01;
    public static final double NORMAL_SPEED_SECONDS_PER_FRAME = 1.0 / 60.0; // Assuming 60 FPS target

    public AlarmClockAnimator() {
        this.actualTime = true;
        this.slowMotionActive = false;
        this.isPaused = false;
        this.resumingAtNormalRate = false; // Initialize new field
        // Time is initialized lazily
    }

    public String getTime() {
        return String.format("Time: %tT", time);
    }

    public void getNow() {
        if (!isPaused) {
            time = LocalTime.now();
        }
    }

    public void advanceTime(double secondsDelta) {
        if (this.time == null) {
            getNow(); // Initialize if null
        }
        if (!isPaused) {
            this.time = this.time.plusNanos((long)(secondsDelta * 1_000_000_000L));
        }
    }

    public boolean isActualTime() {
        return actualTime;
    }

    public void setActualTime(boolean actualTime) {
        this.actualTime = actualTime;
        if (this.actualTime) {
            // If switching to actual time, clear other manual modes
            this.slowMotionActive = false;
            this.resumingAtNormalRate = false;
        }
    }

    public boolean isSlowMotion() {
        return slowMotionActive;
    }

    public void setSlowMotion(boolean slowMotionActive) {
        this.slowMotionActive = slowMotionActive;
        if (this.slowMotionActive) {
            // If switching to slow motion, it's not actual time or resuming normal rate
            this.actualTime = false;
            this.resumingAtNormalRate = false;
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isResumingAtNormalRate() {
        return resumingAtNormalRate;
    }

    public void setResumingAtNormalRate(boolean resuming) {
        this.resumingAtNormalRate = resuming;
        if (this.resumingAtNormalRate) {
            // If resuming at normal rate, it's not actual time or slow motion
            this.actualTime = false;
            this.slowMotionActive = false;
        }
    }

    public void render(GL2 gl, Object object) {
        object.getMaterial().applyMaterial(gl);
        String name = object.getName();

        switch (name) {
            case "Second":
            case "Minute":
            case "Hour":
                animateHand(gl, name, object.getIndex());
                break;
            case "GearLeftCW":
                animateGear(gl, object.getIndex(), -0.85f, 0.85f, 1.0f, true);
                break;
            case "GearCenterCCW":
                animateGear(gl, object.getIndex(), 0.0f, 0.0f, 1.0f, false);
                break;
            case "GearRightCW":
                animateGear(gl, object.getIndex(), 0.9f, 0.0f, 2.0f, true);
                break;
            case "GearBottomCW":
                animateGear(gl, object.getIndex(), 0.0f, -0.9f, 2.0f, true);
                break;
            default:
                gl.glPushMatrix();
                gl.glCallList(object.getIndex());
                gl.glPopMatrix();
        }
    }

    private void animateHand(GL2 gl, String hand, int objectIndex) {
        float alpha = 0;

        int h = time.getHour() % 12;
        int m = time.getMinute();
        int s = time.getSecond();

        gl.glPushMatrix();
        switch (hand) {
            case "Second":
                alpha = s * 6;
                break;
            case "Minute":
                alpha = 6 * (m + s / 60.0f);
                break;
            case "Hour":
                alpha = 30 * (h + m / 60.0f);
                break;
        }
        gl.glRotatef(-alpha, 1, 0, 0);
        gl.glCallList(objectIndex);
        gl.glPopMatrix();
    }

    private void animateGear(GL2 gl, int objectIndex, float posY, float posZ, float rotateRate, boolean CW) {
        int second = time.getSecond();
        float alpha = 3 * rotateRate * second;

        gl.glPushMatrix();
        gl.glTranslatef(0, posY, posZ);

        gl.glRotatef((CW) ? -alpha : alpha, 1, 0, 0);

        gl.glTranslatef(0, -posY, -posZ);
        gl.glCallList(objectIndex);
        gl.glPopMatrix();
    }
}
