package graphics.object;

import com.jogamp.opengl.GL2;

import java.time.LocalTime;

public class AlarmClockAnimator {

    private LocalTime time;
    private boolean actualTime;
    private boolean slowMotionActive; // New field for slow motion state

    static final double FAST_FORWARD_SECONDS_PER_FRAME = 1.0;
    static final double SLOW_MOTION_SECONDS_PER_FRAME = 0.01; // Approx 5x slower at 60 FPS

    public AlarmClockAnimator() {
        this.actualTime = true;
        this.slowMotionActive = false;
        // Time is initialized lazily
    }

    public String getTime() {
        return String.format("Time: %tT", time);
    }

    public void getNow() {
        time = LocalTime.now();
    }

    public void advanceTime(double secondsDelta) {
        if (this.time == null) {
            getNow(); // Initialize if null
        }
        this.time = this.time.plusNanos((long)(secondsDelta * 1_000_000_000L));
    }

    public boolean isActualTime() {
        return actualTime;
    }

    public void setActualTime(boolean actualTime) {
        this.actualTime = actualTime;
    }

    public boolean isSlowMotion() { // New getter
        return slowMotionActive;
    }

    public void setSlowMotion(boolean slowMotionActive) { // New setter
        this.slowMotionActive = slowMotionActive;
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
