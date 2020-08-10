/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slepcalc;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Yannik Bramkamp
 */
public class Data {

    private Map<Integer, Integer> exp_cost;
    private Map<Integer, Integer> exp_mission;

    public Data(){
         exp_cost = new HashMap();
         exp_mission = new HashMap();
         populate();
    }

    public void populate(){
        this.getExp_cost().put(6, 6);
        this.getExp_cost().put(16, 8);
        this.getExp_cost().put(24, 10);
        this.getExp_cost().put(38, 13);
        this.getExp_cost().put(54, 16);
        this.getExp_cost().put(59, 13);
        this.getExp_cost().put(84, 14);
        this.getExp_cost().put(112, 16);
        this.getExp_cost().put(153, 17);
        this.getExp_mission().put(6, 11);
        this.getExp_mission().put(16, 12);
        this.getExp_mission().put(24, 13);
        this.getExp_mission().put(38, 14);
        this.getExp_mission().put(54, 15);
        this.getExp_mission().put(59, 21);
        this.getExp_mission().put(84, 22);
        this.getExp_mission().put(112, 23);
        this.getExp_mission().put(153, 24);         
        
    }

    /**
     * @return the exp_cost
     */
    public Map<Integer, Integer> getExp_cost() {
        return exp_cost;
    }
    
    public Integer getCost(int exp){
        return exp_cost.get(exp);
    }

    public Integer getMission(int exp){
        return exp_mission.get(exp);
    }
    /**
     * @param exp_cost the exp_cost to set
     */
    public void setExp_cost(Map<Integer, Integer> exp_cost) {
        this.exp_cost = exp_cost;
    }

    /**
     * @return the exp_mission
     */
    public Map<Integer, Integer> getExp_mission() {
        return exp_mission;
    }

    /**
     * @param exp_mission the exp_mission to set
     */
    public void setExp_mission(Map<Integer, Integer> exp_mission) {
        this.exp_mission = exp_mission;
    }

 
}
