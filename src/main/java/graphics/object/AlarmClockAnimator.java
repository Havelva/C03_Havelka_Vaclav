package graphics.object;

import com.jogamp.opengl.GL2;

import java.time.LocalTime;

public class AlarmClockAnimator {

    private LocalTime time;
    private boolean actualTime;

    public AlarmClockAnimator() {
        actualTime = true;
    }

    public String getTime() {
        return String.format("Time: %tT", time);
    }

    public void getNow() {
        time = LocalTime.now();
    }

    public void plus(int i) {
        time = time.plusSeconds(i);
    }

    public boolean isActualTime() {
        return actualTime;
    }

    public void setActualTime(boolean actualTime) {
        this.actualTime = actualTime;
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
