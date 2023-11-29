package com.jeonginho.mobilerobotcontroller.addon;

public class SIM {
    public void rotateRobot(Robot robot) {
        robot.rotate();
    }
    public void moveRobot(Robot robot, Map realMap){
        robot.moveForward(realMap);
    }

    public boolean isHazard(Robot robot, Map realMap){
        int[] robotPos = robot.getPos();
        int xToDetect = robotPos[0] + Robot.DIRECTIONS[robotPos[2]][0];
        int yToDetect = robotPos[1] + Robot.DIRECTIONS[robotPos[2]][1];

        char result = (char) realMap.getSpotType(xToDetect,yToDetect);
        return result=='H';
    }
    public boolean[] isColorblob(Robot robot, Map realMap){
        boolean[] sensor = {false, false, false, false};
        for(int i = 0; i < 4; i++){
            int[] robotPos = robot.getPos();
            int xToDetect = robotPos[0] + Robot.DIRECTIONS[i][0];
            int yToDetect = robotPos[1] + Robot.DIRECTIONS[i][1];
            if(realMap.getSpotType(xToDetect,yToDetect)=='C')
                sensor[i]=true;
        }
        return sensor;
    }
    public int[] getPos(Robot robot){
        return robot.getPos();
    }
}