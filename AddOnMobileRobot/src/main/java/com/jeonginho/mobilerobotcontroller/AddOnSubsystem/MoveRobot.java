package com.jeonginho.mobilerobotcontroller.AddOnSubsystem;

import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealMap.Map;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealRobot.Robot;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.Simulator.SIM;

import java.util.ArrayList;

public class MoveRobot {
    private int calTurns(int direction, int curX, int curY, int nextX, int nextY){
        int targetDirection;
        if (nextX > curX) {
            targetDirection = 1; // Right
        } else if (nextX < curX) {
            targetDirection = 3; // Left
        } else if (nextY > curY) {
            targetDirection = 0; // Up
        } else {
            targetDirection = 2; // Down
        }
        if(direction > targetDirection) targetDirection += 4;
        return targetDirection - direction;
    }
    protected boolean[] orderMovement(SIM sim,char[][] addOnMap, Robot addOnRobot, Robot realRobot, Map realMap, ArrayList<int[]> path) {

        boolean[] rePathAndError= {false, false};
        int curX = addOnRobot.getPos()[0];
        int curY = addOnRobot.getPos()[1];
        int curDirection = addOnRobot.getPos()[2];
        if(path.size()>1) {
            int nextX = path.get(1)[0];
            int nextY = path.get(1)[1];
            int remainedTurns = calTurns(curDirection, curX, curY, nextX, nextY);

            boolean[] detectedColorBlob = sim.isColorblob(realRobot, realMap);
            for (int i = 0; i < 4; i++) {
                if (detectedColorBlob[i]) {
                    addOnMap[curY + Robot.DIRECTIONS[i][1]][curX + Robot.DIRECTIONS[i][0]] = 'C';
                }
            }
            if(remainedTurns>0) {
                sim.rotateRobot(realRobot);
                addOnRobot.rotate();
            }else {
                if (sim.isHazard(realRobot, realMap)) {
                    rePathAndError[0] = true;
                    addOnMap[nextY][nextX] = 'H';
                } else {
                    sim.moveRobot(realRobot, realMap);

                    if (sim.getPos(realRobot)[0] != nextX || sim.getPos(realRobot)[1] != nextY) {
                        rePathAndError[0] = true;
                        rePathAndError[1] = true;
                    } else
                        path.remove(0);
                    for (int i = 0; i < 4; i++) {
                        if (sim.isColorblob(realRobot, realMap)[i]) {
                            addOnMap[sim.getPos(realRobot)[1] + Robot.DIRECTIONS[i][1]][sim.getPos(realRobot)[0] + Robot.DIRECTIONS[i][0]] = 'C';
                        }
                    }
                    if (addOnMap[sim.getPos(realRobot)[1]][sim.getPos(realRobot)[0]] == 'P') {
                        addOnMap[sim.getPos(realRobot)[1]][sim.getPos(realRobot)[0]] = 'S';
                    }
                    addOnRobot.setPos(sim.getPos(realRobot)[0], sim.getPos(realRobot)[1], sim.getPos(realRobot)[2]);
                }
            }
        }else{
            rePathAndError[0] = true;
        }
        return rePathAndError;
    }
}
