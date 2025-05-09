package graphics.object;

import com.jogamp.opengl.GL2;

import java.time.LocalTime;

public class AlarmClockAnimator {

    private LocalTime time;
    private boolean actualTime;
    private boolean slowMotionActive;
    private boolean isPaused;
    private boolean resumingAtNormalRate; // New field

    // Ringing animation parameters
    private boolean isRinging = false;

    // New parameters for improved ringing animation
    private float ringPhase = 0.0f;
    private float ringVisualFrequency = 8.0f; // Visual frequency of the shake in Hz (cycles per simulated second)
    private float ringMaxAngleZ = 6.0f;     // Max rotation angle in degrees for Z-axis shake (twist)
    private float ringMaxAngleX = 4.0f;     // Max rotation angle in degrees for X-axis shake (nod)
    
    private double lastSecondsDelta = NORMAL_SPEED_SECONDS_PER_FRAME; // Stores the delta of the last time update


    static final double FAST_FORWARD_SECONDS_PER_FRAME = 1.0;
    static final double SLOW_MOTION_SECONDS_PER_FRAME = 0.01;
    public static final double NORMAL_SPEED_SECONDS_PER_FRAME = 1.0 / 60.0; // Assuming 60 FPS target
    private static final float HAMMER_CENTERING_OFFSET_DEGREES = -5.0f; // Adjusts the center of the hammer's swing.
    // Multiplier for hammer's phase relative to ringPhase.
    // Effective hammer frequency = ringVisualFrequency * HAMMER_RING_PHASE_MULTIPLIER
    // 8.0f * 0.2625f = 2.1f Hz.
    // In fast-forward (deltaTime=1.0), total advance for hammer's sin argument:
    // (2*PI*ringVisualFrequency*deltaTime) * HAMMER_RING_PHASE_MULTIPLIER
    // = (2*PI*8.0*1.0) * 0.2625 = 16*PI * 0.2625 = 4.2*PI. Not a multiple of 2*PI, so not stuck.
    private static final float HAMMER_RING_PHASE_MULTIPLIER = 0.2625f; 

    public AlarmClockAnimator() {
        this.actualTime = true;
        this.slowMotionActive = false;
        this.isPaused = false;
        this.resumingAtNormalRate = false; // Initialize new field
        // Time is initialized lazily
        // Ringing is off by default
    }

    public String getTime() {
        return String.format("Time: %tT", time);
    }

    public void getNow() {
        if (!isPaused) {
            time = LocalTime.now();
            this.lastSecondsDelta = NORMAL_SPEED_SECONDS_PER_FRAME; // For actual time, ring updates at normal speed
            // If time was null and we just set it, and we are resuming, ensure resumingAtNormalRate is false
            // to avoid advancing time immediately after getting LocalTime.now() if unpausing to real time.
            // This logic is subtle. If unpausing to "Real Time (Resumed)", advanceTime is called.
            // If unpausing to "Real Time", getNow() is called.
            // This should be fine as is.
        }
    }

    public void advanceTime(double secondsDelta) {
        if (this.time == null) {
            getNow(); // Initialize if null
        }
        if (!isPaused) {
            if (this.time != null) { // Ensure time is not null before advancing
                this.time = this.time.plusNanos((long)(secondsDelta * 1_000_000_000L));
                this.lastSecondsDelta = secondsDelta; // Store the delta used for this advancement
            }
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

    public boolean isRinging() {
        return isRinging;
    }

    public void setRinging(boolean ringing) {
        isRinging = ringing;
    }

    /**
     * Applies a global transformation for the ringing animation if active.
     * This should be called within a glPushMatrix/glPopMatrix block.
     * @param gl The GL2 context.
     */
    public void applyRingTransformation(GL2 gl) {
        if (isRinging) {
            float deltaTimeForRingThisFrame = 0.0f;
            if (!isPaused) {
                deltaTimeForRingThisFrame = (float)this.lastSecondsDelta;
            }

            // Increment phase based on the effective delta time for this frame.
            ringPhase += (2.0f * (float)Math.PI * ringVisualFrequency) * deltaTimeForRingThisFrame;
            
            // Optional: Keep phase within a 0 to 2*PI range to prevent potential float precision issues over very long runtimes.
            // if (ringPhase > 2.0f * (float)Math.PI) {
            //     ringPhase -= 2.0f * (float)Math.PI;
            // }

            float currentRingAngleZ = ringMaxAngleZ * (float) Math.sin(ringPhase);
            // Use a slightly different frequency or phase for the X-axis wobble to make it less uniform.
            float currentRingAngleX = ringMaxAngleX * (float) Math.sin(ringPhase * 0.77f + (float)Math.PI / 3.0f);

            // Apply rotations for the shake effect.
            gl.glRotatef(currentRingAngleZ, 0.0f, 0.0f, 1.0f); // Shake around Z-axis (twist)
            gl.glRotatef(currentRingAngleX, 1.0f, 0.0f, 0.0f); // Shake around X-axis (nod/rock)
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
            case "Sphere.005_Sphere.002": // Bell Hammer
                animateBellHammer(gl, object.getIndex());
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

    private void animateBellHammer(GL2 gl, int objectIndex) {
        gl.glPushMatrix();
        if (isRinging) {
            float maxHammerAngle = 25.0f;     // Max swing angle in degrees from center
            // Hammer's oscillation is now driven by ringPhase for synchronization
            float dynamicHammerAngle = maxHammerAngle * (float) Math.sin(ringPhase * HAMMER_RING_PHASE_MULTIPLIER);

            // Apply centering offset
            float totalHammerAngle = dynamicHammerAngle + HAMMER_CENTERING_OFFSET_DEGREES;

            // Apply rotation for the hammer swing.
            gl.glRotatef(totalHammerAngle, 1.0f, 0.0f, 0.0f);
        }
        gl.glCallList(objectIndex);
        gl.glPopMatrix();
    }
}
